package io.realskill.task.java;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.github.javafaker.Faker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;
import java.util.Collections;

@RunAsClient
@RunWith(Arquillian.class)
public class MoodServletTest {

    static Faker faker;

    static String user1Username;

    static String user1Password;

    static String user2Username;

    static String user2Password;

    @ArquillianResource
    URL context;

    @Drone
    WebDriver browser;

    @Page
    MoodPage moodPage;

    @Page
    HomePage homePage;

    @Page
    LoginPage loginPage;

    @Page
    LoginErrorPage loginErrorPage;

    @Page
    UsersPage usersPage;

    @Page
    ErrorPage errorPage;

    @Deployment
    public static WebArchive createDeployment()
    {
        final String WEBAPP_SRC = "src/main/webapp";
        final File[] dependencies = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .resolve("javax.servlet:jstl")
            .withoutTransitivity()
            .asFile();
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MoodServlet.class, LoginServlet.class, NotFoundException.class, SeedServlet.class,
                UsersServlet.class)
            .addAsLibraries(dependencies)
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "403.html"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "404.html"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "login.html"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "loginError.html"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "userMood.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "users.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebResource(new File(WEBAPP_SRC, "index.jsp"))
            .addAsWebResource(new File("src/main/resources/META-INF", "context.xml"), "META-INF/context.xml");
    }

    @BeforeClass
    public static void setup()
    {
        faker = new Faker();
        user1Username = faker.superhero().name().replaceAll(" ", "");
        do {
            user2Username = faker.superhero().name().replaceAll(" ", "");
        } while (user1Username.equals(user2Username));
        user1Password = faker.superhero().power();
        user2Password = faker.superhero().power();
    }

    @InSequence(-1)
    @Test
    public void seed() throws Exception
    {
        final String url =
            context + "seed?username=" + user1Username + "&username=" + user2Username + "&password=" + user1Password
                + "&password=" + user2Password;
        browser.get(url);
    }

    @InSequence
    @Test
    public void anonymousUser_on_mainPage() throws Exception
    {
        browser.manage().deleteAllCookies();
        homePage.navigate(browser);
        Assert.assertTrue(homePage.getLogin().isPresent());
        homePage.clickLogin();
        Assert.assertTrue(loginPage.isCurrentPage(browser));
    }

    @InSequence(1)
    @Test
    public void loginWithInvalidCredentials() throws Exception
    {
        browser.manage().deleteAllCookies();
        homePage.navigate(browser);
        homePage.clickLogin();
        loginPage.signIn(user1Username, user2Password + user1Password);
        Assert.assertTrue(loginErrorPage.isCurrentPage(browser));
        loginErrorPage.tryAgain();
        Assert.assertTrue(loginPage.isCurrentPage(browser));
    }

    @InSequence(2)
    @Test
    public void loginWithValidCredentials() throws Exception
    {
        browser.manage().deleteAllCookies();
        homePage.navigate(browser);
        homePage.clickLogin();
        loginPage.signIn(user1Username, user1Password);
        Assert.assertTrue(moodPage.isCurrentPage(browser, user1Username));
        Assert.assertEquals("unknown", moodPage.getMood().getText());
        Assert.assertTrue(moodPage.getMakeHappy().isDisplayed());
        Assert.assertTrue(moodPage.getMakeSad().isDisplayed());
        Assert.assertTrue(moodPage.getLogout().isDisplayed());
    }

    @InSequence(3)
    @Test
    public void selectHappyMood() throws Exception
    {
        Assert.assertTrue(moodPage.isCurrentPage(browser, user1Username));
        moodPage.makeHappy();
        Assert.assertTrue(moodPage.isHappy());
        browser.manage().deleteAllCookies();
        moodPage.navigate(browser, user1Username);
        Assert.assertTrue(moodPage.isHappy());
    }

    @InSequence(4)
    @Test
    public void selectUnhappyMood() throws Exception
    {
        homePage.navigate(browser);
        homePage.clickLogin();
        loginPage.signIn(user1Username, user1Password);
        Assert.assertTrue(moodPage.isCurrentPage(browser, user1Username));
        Assert.assertTrue(moodPage.isHappy());
        moodPage.makeSad();
        Assert.assertTrue(moodPage.isSad());
    }

    @InSequence(5)
    @Test
    public void cannotModifyOtherPeoplesMood() throws Exception
    {
        browser.manage().deleteAllCookies();
        homePage.navigate(browser);
        homePage.clickLogin();
        loginPage.signIn(user2Username, user2Password);
        moodPage.navigate(browser, user1Username);
        Assert.assertTrue(moodPage.isSad());
        Assert.assertFalse(moodPage.getMakeHappy().isPresent());
        Assert.assertFalse(moodPage.getMakeSad().isPresent());
    }

    @InSequence(6)
    @Test
    public void adminGoesToUsersLis() throws Exception
    {
        browser.manage().deleteAllCookies();
        homePage.navigate(browser);
        homePage.clickLogin();
        loginPage.signIn(user1Username, user1Password);
        usersPage.navigate(browser);
        Assert.assertTrue(usersPage.getUsers().contains(user1Username));
        Assert.assertTrue(usersPage.getUsers().contains(user2Username));
        usersPage.click(user1Username);
        Assert.assertTrue(moodPage.isCurrentPage(browser, user1Username));
    }

    @InSequence(7)
    @Test
    public void userGoesToUsersLis() throws Exception
    {
        browser.manage().deleteAllCookies();
        homePage.navigate(browser);
        homePage.clickLogin();
        loginPage.signIn(user2Username, user2Password);
        usersPage.navigate(browser);
        Assert.assertTrue(usersPage.getUsers().isEmpty());
        Assert.assertEquals("403", errorPage.getHeader().getText());
        Assert.assertEquals("Access denied", errorPage.getMessage().getText());
    }

    @InSequence(8)
    @Test
    public void userGoesToNonExistingMoodPage() throws Exception
    {
        browser.manage().deleteAllCookies();
        moodPage.navigate(browser, user1Username + user1Username);
        Assert.assertEquals("404", errorPage.getHeader().getText());
        Assert.assertEquals("Resource not found", errorPage.getMessage().getText());
    }

    @InSequence(9)
    @Test
    public void unauthorizedMoodPost() throws Exception
    {
        browser.manage().deleteAllCookies();
        homePage.navigate(browser);
        homePage.clickLogin();
        loginPage.signIn(user2Username, user2Password);
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        final org.openqa.selenium.Cookie jsessionid = browser.manage().getCookieNamed("JSESSIONID");
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getCookieManager()
            .addCookie(new Cookie(jsessionid.getDomain(), jsessionid.getName(), jsessionid.getValue()));
        final WebRequest post = new WebRequest(new URL(context + "user/" + user1Username), HttpMethod.POST);
        post.setRequestParameters(Collections.singletonList(new NameValuePair("mood", "terrible")));
        final HtmlPage page = webClient.getPage(post);
        Assert.assertTrue("Should contain 'Access denied':\n" + page.getWebResponse().getContentAsString(),
            page.getWebResponse().getContentAsString().contains("Access denied"));
    }

    @InSequence(10)
    @Test
    public void anonymousMoodPost() throws Exception
    {
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        final WebRequest post = new WebRequest(new URL(context + "user/" + user1Username), HttpMethod.POST);
        post.setRequestParameters(Collections.singletonList(new NameValuePair("mood", "dupa")));
        final HtmlPage page = webClient.getPage(post);
        Assert.assertFalse("Should contain form with username", page.getElementsByName("j_username").isEmpty());
    }
}

package io.realskill.task.java;

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
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

@RunAsClient
@RunWith(Arquillian.class)
public class SessionListenerTest {

    @ArquillianResource
    private URL contextRoot;

    @Drone
    WebDriver browser;

    @Page
    HomePage homePage;

    static Faker faker;

    static String firstUserNickname;

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
            .addClass(ActiveUsers.class)
            .addClass(SessionListener.class)
            .addAsLibraries(dependencies)
            .addAsWebResource(new File(WEBAPP_SRC, "index.jsp"));
    }

    @BeforeClass
    public static void setup()
    {
        faker = new Faker();
        firstUserNickname = faker.name().username();
    }

    @InSequence
    @Test
    public void initially() throws Exception
    {
        browser.get(contextRoot.toString());
        Assert.assertEquals(1, homePage.getActiveUsers().size());
        Assert.assertEquals(getSessionIdFromCookie(), homePage.getActiveUsers().get(0));
    }

    @InSequence(1)
    @Test
    public void onNicknameChange() throws Exception
    {
        homePage.getNickname().sendKeys(firstUserNickname);
        guardHttp(homePage.getChangeNickname()).click();
        Assert.assertEquals(1, homePage.getActiveUsers().size());
        Assert.assertEquals(firstUserNickname, homePage.getActiveUsers().get(0));
    }

    @InSequence(2)
    @Test
    public void whenUserRemovesCookies() throws Exception
    {
        browser.manage().deleteAllCookies();
        browser.get(browser.getCurrentUrl());
        final String sessionId = getSessionIdFromCookie();
        Assert.assertEquals(2, homePage.getActiveUsers().size());
        Assert.assertTrue(homePage.getActiveUsers().contains(firstUserNickname));
        Assert.assertTrue(homePage.getActiveUsers().contains(sessionId));
    }

    @InSequence(3)
    @Test
    public void whenUserLogsOut() throws Exception
    {
        final String oldSessionId = getSessionIdFromCookie();
        guardHttp(homePage.getLogout()).click();
        final String newSessionId = getSessionIdFromCookie();
        Assert.assertEquals(2, homePage.getActiveUsers().size());
        Assert.assertTrue(homePage.getActiveUsers().contains(firstUserNickname));
        Assert.assertFalse(homePage.getActiveUsers().contains(oldSessionId));
        Assert.assertTrue(homePage.getActiveUsers().contains(newSessionId));
    }

    @InSequence(4)
    @Test
    public void whenOtherUserChangesNickname() throws Exception
    {
        final String newNickname = faker.chuckNorris().fact();
        homePage.getNickname().sendKeys(newNickname);
        guardHttp(homePage.getChangeNickname()).click();
        Assert.assertEquals(2, homePage.getActiveUsers().size());
        Assert.assertTrue(homePage.getActiveUsers().contains(firstUserNickname));
        Assert.assertTrue(homePage.getActiveUsers().contains(newNickname));
    }

    private String getSessionIdFromCookie()
    {
        Cookie jsessionid = browser.manage().getCookieNamed("JSESSIONID");
        return null == jsessionid ? null : jsessionid.getValue();
    }
}

package io.realskill.task.java;

import com.github.javafaker.Faker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

@RunAsClient
@RunWith(Arquillian.class)
public class PageServletTest {

    static Faker faker;

    static String hero1;

    static String hero2;

    @ArquillianResource
    private URL contextRoot;

    @Drone
    WebDriver browser;

    @Page
    PageEditPage pageEditPage;

    @Page
    PageListPage pageListPage;

    @Page
    PageViewPage pageViewPage;

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
            .addClass(PageServlet.class)
            .addAsLibraries(dependencies)
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "pageEdit.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "pageList.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "page.jsp"))
            .addAsWebResource(new File(WEBAPP_SRC, "index.jsp"));
    }

    @BeforeClass
    public static void setup()
    {
        faker = new Faker();
    }

    @Before
    public void loadPage()
    {
        browser.get(contextRoot.toString());
    }

    @InSequence
    @Test
    public void createNewPage() throws InterruptedException
    {
        final String hero = faker.ancient().hero();
        hero1 = hero;
        pageListPage.getPathInput().sendKeys("/" + hero);
        guardHttp(pageListPage.getSubmit()).click();
        pageEditPage.getTitle().sendKeys(hero);
        pageEditPage.getBody().sendKeys("<h1>This is page about " + hero + "</h1>");
        guardHttp(pageEditPage.getSubmit()).click();
        Assert.assertEquals("This is page about " + hero, pageViewPage.getBody().getText());
        guardHttp(pageViewPage.getPagesLink()).click();
        Assert.assertFalse("Should see page list", pageListPage.getPages().isEmpty());
    }

    @InSequence(1)
    @Test
    public void createDraft() throws InterruptedException
    {
        String hero = anotherRandomHero();
        hero2 = hero;
        final String permalink = toPermalink(hero);
        pageListPage.getPathInput().sendKeys("/" + permalink);
        guardHttp(pageListPage.getSubmit()).click();
        pageEditPage.getTitle().sendKeys(hero);
        final String body = faker.gameOfThrones().quote();
        pageEditPage.getBody().sendKeys(body);
        guardHttp(pageEditPage.getSubmitDraft()).click();
        assertDraft("/" + permalink);
        browser.get(contextRoot.toString() + "cms/" + permalink);
        Assert.assertFalse("Should see page list", pageListPage.getPages().isEmpty());
        assertDraft("/" + permalink);
        assertNotPublished("/" + permalink);
        browser.get(contextRoot.toString() + "cms/" + permalink + "?edit");
        Assert.assertEquals(hero, pageEditPage.getTitle().getAttribute("value"));
        Assert.assertEquals(body, pageEditPage.getBody().getAttribute("value"));
        browser.manage().deleteAllCookies();
        browser.get(contextRoot.toString() + "cms/" + permalink + "?edit");
        Assert.assertEquals("", pageEditPage.getTitle().getAttribute("value"));
        Assert.assertEquals("", pageEditPage.getBody().getAttribute("value"));
    }

    @InSequence(2)
    @Test
    public void finalizeDraft() throws InterruptedException
    {
        pageListPage.getPathInput().sendKeys("/gallery");
        guardHttp(pageListPage.getSubmit()).click();
        pageEditPage.getTitle().sendKeys("Gallery");
        pageEditPage.getBody().sendKeys("Image A, Image B");
        guardHttp(pageEditPage.getSubmitDraft()).click();
        assertDraft("/gallery");
        browser.get(contextRoot.toString() + "cms/gallery?edit");
        guardHttp(pageEditPage.getSubmit()).click();
        Assert.assertEquals("Image A, Image B", pageViewPage.getBody().getText());
        browser.get(contextRoot.toString() + "cms");
        assertPublished("/gallery");
        assertNotDraft("/gallery");
    }

    @InSequence(3)
    @Test
    public void editPublicPage_draftIt_and_comeBackToEditing() throws Exception
    {
        guardHttp(pageListPage.getPage("/about-us")).click();
        guardHttp(pageViewPage.getEdit()).click();
        final String newBody = faker.chuckNorris().fact();
        pageEditPage.getBody().clear();
        pageEditPage.getBody().sendKeys(newBody);
        guardHttp(pageEditPage.getSubmitDraft()).click();
        Assert.assertEquals("That's us", pageViewPage.getBody().getText());
        browser.get(contextRoot.toString());
        assertDraft("/about-us");
        assertPublished("/about-us");
        guardHttp(pageListPage.getPage("/about-us")).click();
        guardHttp(pageViewPage.getEdit()).click();
        Assert.assertEquals(newBody, pageEditPage.getBody().getText());
        guardHttp(pageEditPage.getSubmit()).click();
        Assert.assertEquals(newBody, pageViewPage.getBody().getText());
    }

    private void assertDraft(String path)
    {
        final Set<String> pages = new HashSet<>();
        for (GrapheneElement page : pageListPage.getDrafts()) {
            pages.add(page.getText());
        }
        Assert.assertTrue("Page " + path + " should be a draft", pages.contains(path));
    }

    private void assertNotDraft(String path)
    {
        for (GrapheneElement page : pageListPage.getDrafts()) {
            Assert.assertEquals("There should be no " + path + " page among drafts", path, page.getText());
        }
    }

    private void assertPublished(String path)
    {
        final Set<String> pages = new HashSet<>();
        for (GrapheneElement page : pageListPage.getPages()) {
            pages.add(page.getText());
        }
        Assert.assertTrue("Page " + path + " should be public", pages.contains(path));
    }

    private void assertNotPublished(String path)
    {
        for (GrapheneElement page : pageListPage.getPages()) {
            Assert.assertNotEquals("There should be no " + path + " page publicly available yet", path, page.getText());
        }
    }

    private String toPermalink(String str)
    {
        return str.toLowerCase().replace(" ", "-");
    }

    private String anotherRandomHero()
    {
        String hero;
        do {
            hero = faker.gameOfThrones().character();
        } while (hero.equals(hero1));
        return hero;
    }
}

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
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

@RunAsClient
@RunWith(Arquillian.class)
public class ChatServletTest {

    static Faker faker;

    static String firstChatroomName;

    @ArquillianResource
    URL contextRoot;

    @Drone
    WebDriver browser;

    @Page
    NicknamePage nicknamePage;

    @Page
    ChatroomListPage chatroomListPage;

    @Page
    ChatroomPage chatroomPage;

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
            .addClass(ChatServlet.class)
            .addAsLibraries(dependencies)
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "chatroomList.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "chatroom.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "nicknamePicker.jsp"))
            .addAsWebResource(new File(WEBAPP_SRC, "index.jsp"));
    }

    @BeforeClass
    public static void setup()
    {
        faker = new Faker();
    }

    @InSequence
    @Test
    public void anonymousUserEntersProtectedUrls() throws InterruptedException
    {
        browser.get(contextRoot + "/chat");
        Assert.assertTrue(nicknamePage.getNickname().isPresent());
        Assert.assertTrue(chatroomListPage.getActiveUsers().isEmpty());
        Assert.assertFalse(chatroomListPage.getChatroomsTable().isPresent());
        browser.get(contextRoot + "/chat/Mesos");
        Assert.assertTrue(nicknamePage.getNickname().isPresent());
        Assert.assertTrue(chatroomListPage.getActiveUsers().isEmpty());
        Assert.assertFalse(chatroomListPage.getChatroomsTable().isPresent());
    }

    @InSequence(1)
    @Test
    public void whenNicknameIsAvailable() throws InterruptedException
    {
        browser.get(contextRoot.toString());
        nicknamePage.getNickname().sendKeys("John Doe");
        guardHttp(nicknamePage.getSubmit()).click();
        Assert.assertFalse(nicknamePage.getNickname().isPresent());
        Assert.assertFalse(chatroomListPage.getActiveUsers().isEmpty());
        Assert.assertTrue(chatroomListPage.getChatroomsTable().isPresent());
        Assert.assertEquals(0, chatroomListPage.getChatrooms().size());
    }

    @InSequence(2)
    @Test
    public void whenNicknameIsAlreadyTaken() throws InterruptedException
    {
        browser.manage().deleteAllCookies();
        browser.get(contextRoot.toString());
        nicknamePage.getNickname().sendKeys("John Doe");
        guardHttp(nicknamePage.getSubmit()).click();
        Assert.assertTrue(nicknamePage.getNickname().isPresent());
        Assert.assertTrue(chatroomListPage.getActiveUsers().isEmpty());
        Assert.assertFalse(chatroomListPage.getChatroomsTable().isPresent());
        Assert.assertEquals("Nickname already taken", nicknamePage.getError().getText());
    }

    @InSequence(3)
    @Test
    public void whenUserEntersRootContext() throws InterruptedException
    {
        browser.manage().deleteAllCookies();
        browser.get(contextRoot.toString());
        nicknamePage.getNickname().sendKeys("Matt");
        guardHttp(nicknamePage.getSubmit()).click();
        browser.get(contextRoot.toString());
        Assert.assertFalse(nicknamePage.getNickname().isPresent());
        Assert.assertFalse(chatroomListPage.getActiveUsers().isEmpty());
        Assert.assertTrue(chatroomListPage.getChatroomsTable().isPresent());
        Assert.assertTrue(chatroomListPage.getActiveUsers().contains("Matt"));
        Assert.assertTrue(chatroomListPage.getActiveUsers().contains("John Doe"));
    }

    @InSequence(4)
    @Test
    public void whenUserCreatesNewChatroom() throws InterruptedException
    {
        final String chatroomName = faker.address().cityName();
        firstChatroomName = chatroomName;
        chatroomListPage.getNewChatroomName().sendKeys(chatroomName);
        guardHttp(chatroomListPage.getNewChatroomName()).submit();
        Assert.assertTrue(chatroomPage.getChatroomName().isPresent());
        Assert.assertEquals(chatroomName, chatroomPage.getChatroomName().getText());
        Assert.assertTrue(chatroomPage.getNewMessageInput().isPresent());
    }

    @InSequence(5)
    @Test
    public void whenUserWritesMessages() throws InterruptedException
    {
        final String message1 = faker.chuckNorris().fact();
        final String message2 = faker.chuckNorris().fact();
        chatroomPage.getNewMessageInput().sendKeys(message1);
        guardHttp(chatroomPage.getNewMessageInput()).submit();
        Assert.assertTrue(chatroomPage.getChatroomName().isPresent());
        Assert.assertTrue(chatroomPage.getMessages().contains(message1));

        chatroomPage.getNewMessageInput().sendKeys(message2);
        guardHttp(chatroomPage.getNewMessageInput()).submit();
        Assert.assertTrue(chatroomPage.getChatroomName().isPresent());
        Assert.assertTrue(chatroomPage.getMessages().contains(message1));
        Assert.assertTrue(chatroomPage.getMessages().contains(message2));
    }

    @InSequence(6)
    @Test
    public void whenUserCreatesNewChatroomButNameAlreadyTaken() throws InterruptedException
    {
        browser.get(contextRoot.toString());
        chatroomListPage.getNewChatroomName().sendKeys(firstChatroomName);
        guardHttp(chatroomListPage.getNewChatroomName()).submit();
        Assert.assertFalse(chatroomPage.getChatroomName().isPresent());
        Assert.assertTrue(chatroomListPage.getNewChatroomName().isPresent());
        Assert.assertTrue(chatroomListPage.getError().isPresent());
        Assert.assertEquals("Chatroom name already taken", chatroomListPage.getError().getText());
    }

    @InSequence(7)
    @Test
    public void whenUserCreatesNewChatroomWithUniqueName() throws InterruptedException
    {
        browser.get(contextRoot.toString());
        String chatroomName;
        do {
            chatroomName = faker.address().cityName();
        } while (firstChatroomName.equals(chatroomName));
        chatroomListPage.getNewChatroomName().sendKeys(chatroomName);
        guardHttp(chatroomListPage.getNewChatroomName()).submit();
        Assert.assertTrue(chatroomPage.getChatroomName().isPresent());
        Assert.assertEquals(chatroomName, chatroomPage.getChatroomName().getText());
        Assert.assertTrue(chatroomPage.getNewMessageInput().isPresent());
        Assert.assertTrue(chatroomPage.getMessages().isEmpty());
    }

    @InSequence(8)
    @Test
    public void whenUserAddsMessagesInNewRoom() throws InterruptedException
    {
        chatroomPage.getNewMessageInput().sendKeys(faker.chuckNorris().fact());
        guardHttp(chatroomPage.getNewMessageInput()).submit();
        chatroomPage.getNewMessageInput().sendKeys(faker.chuckNorris().fact());
        guardHttp(chatroomPage.getNewMessageInput()).submit();
        chatroomPage.getNewMessageInput().sendKeys(faker.chuckNorris().fact());
        guardHttp(chatroomPage.getNewMessageInput()).submit();
        Assert.assertEquals(3, chatroomPage.getMessages().size());
    }

    @InSequence(9)
    @Test
    public void whenUserAccessesChatroomByUrl() throws InterruptedException
    {
        browser.get(contextRoot + "/chat/" + firstChatroomName);
        Assert.assertEquals(2, chatroomPage.getMessages().size());
    }
}

package io.realskill.task.java;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.javafaker.Faker;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

@RunWith(Arquillian.class)
public class UserServletTest {

    static Faker faker;

    static String secret;

    static String user1Username;

    static String user1Password;

    static String user2Username;

    static String user2Password;

    @ArquillianResource
    URL context;

    @Deployment(testable = false)
    public static WebArchive createDeployment()
    {
        final String WEBAPP_SRC = "src/main/webapp";
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(UserServlet.class, LoginServlet.class, SeedServlet.class, WebTokenFilter.class)
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebResource(new File("src/main/resources/META-INF", "context.xml"), "META-INF/context.xml");
    }

    @BeforeClass
    public static void setup()
    {
        faker = new Faker();
        secret = faker.chuckNorris().fact();
        user1Username = faker.superhero().name().replaceAll(" ", "");
        do {
            user2Username = faker.superhero().name().replaceAll(" ", "");
        } while (user1Username.equals(user2Username));
        user1Password = faker.superhero().power();
        user2Password = faker.superhero().power();
    }

    @InSequence(0)
    @Test
    public void seed() throws Exception
    {
        final List<NameValuePair> formParams = Form.form()
            .add("username", user1Username)
            .add("username", user2Username)
            .add("password", user1Password)
            .add("password", user2Password)
            .add("secret", secret)
            .build();
        Request.Post(context + "seed").bodyForm(formParams).execute().discardContent();
    }

    @InSequence(1)
    @Test
    public void signIn_validCredentials_getsJWToken() throws Exception
    {
        String response = Request.Post(context.toString() + "login")
            .bodyForm(Form.form().add("username", user1Username).add("password", user1Password).build())
            .execute()
            .returnContent()
            .asString();
        JsonObject jsonObject = Json.createReader(new StringReader(response)).readObject();
        Assert.assertNotNull(jsonObject.getString("token"));
        Assert.assertEquals(1, jsonObject.size());
        response = Request.Get(context.toString() + "me")
            .addHeader("authorization", "Bearer " + jsonObject.getString("token"))
            .execute()
            .returnContent()
            .asString();
        jsonObject = Json.createReader(new StringReader(response)).readObject();
        Assert.assertEquals(user1Username, jsonObject.getString("username"));
        Assert.assertTrue(jsonObject.getBoolean("admin"));
        Assert.assertEquals(2, jsonObject.size());
    }

    @InSequence(1)
    @Test()
    public void signIn_invalidCredentials_returns403() throws Exception
    {
        final HttpResponse response = Request.Post(context.toString() + "login")
            .bodyForm(Form.form().add("username", user1Username).build())
            .execute()
            .returnResponse();
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    @InSequence(1)
    @Test
    public void getMe_validSecret_returnsUser() throws Exception
    {
        String token = JWT.create().withIssuer("auth0").withSubject(user1Username).sign(Algorithm.HMAC256(secret));
        final String response = Request.Get(context.toString() + "me")
            .addHeader("authorization", "Bearer " + token)
            .execute()
            .returnContent()
            .asString();
        final JsonObject jsonObject = Json.createReader(new StringReader(response)).readObject();
        Assert.assertEquals(user1Username, jsonObject.getString("username"));
        Assert.assertTrue(jsonObject.getBoolean("admin"));
    }

    @InSequence(1)
    @Test
    public void getMe_validSecret2_returnsUser() throws Exception
    {
        String token = JWT.create().withIssuer("auth0").withSubject(user2Username).sign(Algorithm.HMAC256(secret));
        final String response = Request.Get(context.toString() + "me")
            .addHeader("authorization", "Bearer " + token)
            .execute()
            .returnContent()
            .asString();
        final JsonObject jsonObject = Json.createReader(new StringReader(response)).readObject();
        Assert.assertEquals(user2Username, jsonObject.getString("username"));
        Assert.assertFalse(jsonObject.getBoolean("admin"));
    }

    @InSequence(1)
    @Test
    public void getMe_withoutHeader_returns401() throws Exception
    {
        final HttpResponse response = Request.Get(context.toString() + "me").execute().returnResponse();
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    @InSequence(1)
    @Test
    public void getMe_invalidHeader_returns401() throws Exception
    {
        final HttpResponse response = Request.Get(context.toString() + "me")
            .addHeader("authorization", "Bearer")
            .execute()
            .returnResponse();
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    @InSequence(1)
    @Test
    public void getMe_invalidHeader2_returns401() throws Exception
    {
        final HttpResponse response = Request.Get(context.toString() + "me")
            .addHeader("authorization", "Bearer 123")
            .execute()
            .returnResponse();
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    @InSequence(1)
    @Test
    public void getMe_invalidSecret_returns401() throws Exception
    {
        String token = JWT.create().withIssuer("auth0").withSubject(user1Username).sign(Algorithm.HMAC256("secret"));
        final HttpResponse response = Request.Get(context.toString() + "me")
            .addHeader("authorization", "Bearer " + token)
            .execute()
            .returnResponse();
        Assert.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    @InSequence(1)
    @Test
    public void getMe_invalidSubject_returns404() throws Exception
    {
        String token = JWT.create()
            .withIssuer("auth0")
            .withSubject(user1Username + user1Username)
            .sign(Algorithm.HMAC256(secret));
        final HttpResponse response = Request.Get(context.toString() + "me")
            .addHeader("authorization", "Bearer " + token)
            .execute()
            .returnResponse();
        Assert.assertEquals(404, response.getStatusLine().getStatusCode());
    }
}

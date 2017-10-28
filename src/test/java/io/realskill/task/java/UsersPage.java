package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

public class UsersPage {

    @ArquillianResource
    private URL contextRoot;

    @FindBy(className = "user")
    private Collection<GrapheneElement> users;

    public List<String> getUsers()
    {
        final List<String> result = new ArrayList<>();
        for (GrapheneElement user : users) {
            result.add(user.getText());
        }
        return result;
    }

    public void click(String username)
    {
        for (GrapheneElement user : users) {
            if (user.getText().equals(username)) {
                guardHttp(user).click();
                return;
            }
        }
        throw new NotFoundException("User not found: " + username);
    }

    public void navigate(WebDriver browser)
    {
        browser.get(contextRoot.toString() + "users");
    }
}

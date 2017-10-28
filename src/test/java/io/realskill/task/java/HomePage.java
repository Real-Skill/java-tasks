package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

public class HomePage {

    @ArquillianResource
    private URL contextRoot;

    @FindBy(className = "login")
    private GrapheneElement login;

    public GrapheneElement getLogin()
    {
        return login;
    }

    public void clickLogin()
    {
        guardHttp(login).click();
    }

    public void navigate(WebDriver browser)
    {
        browser.get(contextRoot.toString());
    }
}

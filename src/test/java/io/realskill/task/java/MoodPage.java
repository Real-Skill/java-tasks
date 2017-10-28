package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

public class MoodPage {

    @ArquillianResource
    private URL contextRoot;

    @FindBy(className = "mood")
    private GrapheneElement mood;

    @FindBy(id = "makeHappy")
    private GrapheneElement makeHappy;

    @FindBy(id = "makeSad")
    private GrapheneElement makeSad;

    @FindBy(className = "logout")
    private GrapheneElement logout;

    @FindBy(className = "login")
    private GrapheneElement login;

    public GrapheneElement getMood()
    {
        return mood;
    }

    public GrapheneElement getMakeHappy()
    {
        return makeHappy;
    }

    public GrapheneElement getMakeSad()
    {
        return makeSad;
    }

    public GrapheneElement getLogout()
    {
        return logout;
    }

    public GrapheneElement getLogin()
    {
        return login;
    }

    public void navigate(WebDriver browser, String username)
    {
        browser.get(contextRoot.toString() + "user/" + username);
    }

    public boolean isCurrentPage(WebDriver browser, String username)
    {
        return browser.getCurrentUrl().endsWith("user/" + username);
    }

    public void makeHappy()
    {
        guardHttp(makeHappy).click();
    }

    public void makeSad()
    {
        guardHttp(makeSad).click();
    }

    public boolean isHappy()
    {
        return ":)".equals(mood.getText());
    }

    public boolean isSad()
    {
        return ":(".equals(mood.getText());
    }
}

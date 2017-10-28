package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

public class LoginPage {

    @FindBy(name = "j_username")
    private GrapheneElement username;

    @FindBy(name = "j_password")
    private GrapheneElement password;

    @FindBy(css = "[type=submit]")
    private GrapheneElement submit;

    public void signIn(String username, String password)
    {
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        guardHttp(this.submit).click();
    }

    public boolean isCurrentPage(WebDriver browser)
    {
        return browser.getCurrentUrl().endsWith("/login");
    }
}

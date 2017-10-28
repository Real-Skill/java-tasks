package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

public class LoginErrorPage {

    @FindBy(tagName = "a")
    private GrapheneElement tryAgain;

    public void tryAgain()
    {
        guardHttp(tryAgain).click();
    }

    public boolean isCurrentPage(WebDriver browser)
    {
        return browser.getCurrentUrl().endsWith("/j_security_check");
    }
}

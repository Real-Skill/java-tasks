package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;

public class NicknamePage {

    @FindBy(className = "error")
    private GrapheneElement error;

    @FindBy(name = "nickname")
    private GrapheneElement nickname;

    @FindBy(name = "submit")
    private GrapheneElement submit;

    public GrapheneElement getError()
    {
        return error;
    }

    public GrapheneElement getNickname()
    {
        return nickname;
    }

    public GrapheneElement getSubmit()
    {
        return submit;
    }
}

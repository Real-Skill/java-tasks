package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;

public class PageEditPage {

    @FindBy(name = "title")
    private GrapheneElement title;

    @FindBy(name = "body")
    private GrapheneElement body;

    @FindBy(id = "submit")
    private GrapheneElement submit;

    @FindBy(id = "submitDraft")
    private GrapheneElement submitDraft;

    public GrapheneElement getTitle()
    {
        return title;
    }

    public GrapheneElement getBody()
    {
        return body;
    }

    public GrapheneElement getSubmit()
    {
        return submit;
    }

    public GrapheneElement getSubmitDraft()
    {
        return submitDraft;
    }
}

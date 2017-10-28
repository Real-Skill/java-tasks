package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;

public class PageViewPage {

    @FindBy(id = "cmsBody")
    private GrapheneElement body;

    @FindBy(id = "edit")
    private GrapheneElement edit;

    @FindBy(id = "list")
    private GrapheneElement pagesLink;

    @FindBy(id = "title")
    private GrapheneElement title;

    public GrapheneElement getBody()
    {
        return body;
    }

    public GrapheneElement getEdit()
    {
        return edit;
    }

    public GrapheneElement getPagesLink()
    {
        return pagesLink;
    }

    public GrapheneElement getTitle()
    {
        return title;
    }
}

package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;

public class ErrorPage {

    @FindBy(tagName = "h1")
    private GrapheneElement header;

    @FindBy(tagName = "div")
    private GrapheneElement message;

    public GrapheneElement getHeader()
    {
        return header;
    }

    public GrapheneElement getMessage()
    {
        return message;
    }
}

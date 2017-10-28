package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;

import java.util.Collection;

public class PageListPage {

    @FindBy(css = "table .draft")
    private Collection<GrapheneElement> drafts;

    @FindBy(name = "path")
    private GrapheneElement pathInput;

    @FindBy(css = "table .page")
    private Collection<GrapheneElement> pages;

    @FindBy(id = "submit")
    private GrapheneElement submit;

    public Collection<GrapheneElement> getDrafts()
    {
        return drafts;
    }

    public GrapheneElement getPathInput()
    {
        return pathInput;
    }

    public GrapheneElement getPage(String path)
    {
        for (GrapheneElement page : pages) {
            if (page.getText().equals(path)) {
                return page;
            }
        }
        return null;
    }

    public Collection<GrapheneElement> getPages()
    {
        return pages;
    }

    public GrapheneElement getSubmit()
    {
        return submit;
    }
}

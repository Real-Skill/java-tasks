package io.realskill.task.java;

import java.io.Serializable;

public class Page implements Serializable {

    private String title;

    private String body;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Page()
    {
    }

    public Page(String title, String body)
    {
        this.title = title;
        this.body = body;
    }
}

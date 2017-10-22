package io.realskill.task.java;

import java.util.Date;

public class ChatMessage {

    private String author;

    private String message;

    private Date date;

    public ChatMessage(String author, String message)
    {
        this.author = author;
        this.message = message;
        this.date = new Date();
    }

    public String getAuthor()
    {
        return author;
    }

    public String getMessage()
    {
        return message;
    }

    public Date getDate()
    {
        return date;
    }
}

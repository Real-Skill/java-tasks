package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class ChatroomPage {

    @FindBy(id = "chatroomName")
    private GrapheneElement chatroomName;

    @FindBy(className = "message")
    private List<GrapheneElement> messages;

    @FindBy(css = "[name=chatroomForm] [name=message]")
    private GrapheneElement newMessageInput;

    public GrapheneElement getChatroomName()
    {
        return chatroomName;
    }

    public List<String> getMessages()
    {
        final ArrayList<String> result = new ArrayList<>();
        for (GrapheneElement element : messages) {
            final GrapheneElement textElement = element.findElement(By.className("messageText"));
            final String text = null == textElement ? null : textElement.getText();
            result.add(text);
        }
        return result;
    }

    public GrapheneElement getNewMessageInput()
    {
        return newMessageInput;
    }
}

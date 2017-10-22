package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class ChatroomListPage {

    @FindBy(className = "user")
    private List<GrapheneElement> activeUsers;

    @FindBy(className = "chatroom")
    private List<GrapheneElement> chatrooms;

    @FindBy(id = "chatrooms")
    private GrapheneElement chatroomsTable;

    @FindBy(className = "error")
    private GrapheneElement error;

    @FindBy(name = "newChatroomForm")
    private GrapheneElement newChatroomForm;

    @FindBy(css = "[name=newChatroomForm] [name=name]")
    private GrapheneElement newChatroomName;

    public GrapheneElement getNewChatroomName()
    {
        return newChatroomName;
    }

    public List<String> getChatrooms()
    {
        final ArrayList<String> names = new ArrayList<>();
        for (GrapheneElement element : chatrooms) {
            names.add(element.getText());
        }
        return names;
    }

    public List<String> getActiveUsers()
    {
        final ArrayList<String> nicknames = new ArrayList<>();
        for (GrapheneElement element : activeUsers) {
            nicknames.add(element.getText());
        }
        return nicknames;
    }

    public GrapheneElement getError()
    {
        return error;
    }

    public GrapheneElement getChatroomsTable()
    {
        return chatroomsTable;
    }
}

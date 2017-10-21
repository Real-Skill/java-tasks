package io.realskill.task.java;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class HomePage {

    @FindBy(className = "user")
    private List<GrapheneElement> activeUsers;

    @FindBy(id = "changeNickname")
    private GrapheneElement changeNickname;

    @FindBy(id = "logout")
    private GrapheneElement logout;

    @FindBy(name = "nickname")
    private GrapheneElement nickname;

    public List<String> getActiveUsers()
    {
        final ArrayList<String> nicknames = new ArrayList<>();
        for (GrapheneElement element : activeUsers) {
            nicknames.add(element.getText());
        }
        return nicknames;
    }

    public GrapheneElement getChangeNickname()
    {
        return changeNickname;
    }

    public GrapheneElement getLogout()
    {
        return logout;
    }

    public GrapheneElement getNickname()
    {
        return nickname;
    }
}

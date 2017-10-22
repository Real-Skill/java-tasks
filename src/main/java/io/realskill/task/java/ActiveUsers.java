package io.realskill.task.java;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public final class ActiveUsers {

    public static Map<String, String> get(ServletContext servletContext)
    {
        final String ACTIVE_USERS_ATTRIBUTE_NAME = "activeUsers";
        @SuppressWarnings("unchecked") Map<String, String> activeUsers = (Map<String, String>) servletContext.getAttribute(
            ACTIVE_USERS_ATTRIBUTE_NAME);
        if (null == activeUsers) {
            activeUsers = new HashMap<>();
            servletContext.setAttribute(ACTIVE_USERS_ATTRIBUTE_NAME, activeUsers);
        }
        return activeUsers;
    }
}

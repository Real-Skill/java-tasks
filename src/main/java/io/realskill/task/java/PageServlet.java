package io.realskill.task.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/cms", "/cms/*"})
public class PageServlet extends HttpServlet {

    @Override
    public void init() throws ServletException
    {
        getPages().put("/about-us", new Page("About us", "<p>That's <b>us</b></p>"));
        getPages().put("/contact", new Page("Contact", "Here"));
    }

    private Map<String, Page> getPages()
    {
        return new HashMap<>();
    }
}

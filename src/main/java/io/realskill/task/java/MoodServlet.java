package io.realskill.task.java;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

@WebServlet(urlPatterns = "/user/*", loadOnStartup = 1)
public class MoodServlet extends HttpServlet {

    @Resource(name = "jdbc/authority")
    DataSource dataSource;

    @Override
    public void init() throws ServletException
    {
        try {
            final String createUsers = "create table IF NOT EXISTS USERS (user_name varchar(255) not null primary key,user_pass varchar(255) not null,mood varchar(5));";
            final String createRoles = "create table IF NOT EXISTS USER_ROLES ( user_name varchar(255) not null, role_name varchar(255) not null, primary key (user_name, role_name) );";
            final Connection connection = dataSource.getConnection();
            final Statement statement = connection.createStatement();
            statement.execute(createUsers);
            statement.execute(createRoles);
            statement.execute("insert into USERS (user_name, user_pass) values ('admin','admin'), ('user', 'user')");
            statement.execute(
                "insert into USER_ROLES (user_name, role_name) values ('admin','admin'), ('admin','user'), ('user','user')");
            statement.close();
            connection.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        req.getRequestDispatcher("/WEB-INF/userMood.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {

    }
}

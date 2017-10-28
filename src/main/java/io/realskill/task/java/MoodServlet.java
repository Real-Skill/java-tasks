package io.realskill.task.java;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@ServletSecurity(httpMethodConstraints = @HttpMethodConstraint(value = "POST", rolesAllowed = "user"))
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
        final String pathInfo = req.getPathInfo();
        final String hash = null == pathInfo ? "" : pathInfo.substring(1);
        if (0 == hash.trim().length()) {
            res.sendRedirect(req.getContextPath());
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("select mood from USERS where user_name=?");
            statement.setString(1, hash);
            statement.execute();
            resultSet = statement.getResultSet();
            resultSet.first();
            if (resultSet.isFirst()) {
                final String mood = resultSet.getString(1);
                req.setAttribute("mood", mood);
            } else {
                throw new NotFoundException("User not found: " + hash);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        final Principal userPrincipal = req.getUserPrincipal();
        if (null != userPrincipal && hash.equals(userPrincipal.getName())) {
            req.setAttribute("owner", true);
        }
        req.getRequestDispatcher("/WEB-INF/userMood.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        final String mood = req.getParameter("mood");
        final String pathInfo = req.getPathInfo();
        final String hash = null == pathInfo ? "" : pathInfo.substring(1);
        if (hash.equals(req.getUserPrincipal().getName())) {
            try {
                final Connection connection = dataSource.getConnection();
                final PreparedStatement statement = connection.prepareStatement(
                    "update users set mood=? where user_name=?");
                statement.setString(1, mood);
                statement.setString(2, hash);
                statement.execute();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        } else {
            res.sendError(403);
            return;
        }
        res.sendRedirect(req.getContextPath() + "/user/" + hash);
    }
}

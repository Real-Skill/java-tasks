package io.realskill.task.java;

import com.auth0.jwt.algorithms.Algorithm;

import javax.annotation.Resource;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(urlPatterns = "/me", loadOnStartup = 1)
public class UserServlet extends HttpServlet {

    @Resource(name = "jdbc/authority")
    DataSource dataSource;

    @Override
    public void init() throws ServletException
    {
        try {
            getServletContext().setAttribute("jwtAlgorithm", Algorithm.HMAC256("secret"));
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }
        try {
            final String createUsers = "create table IF NOT EXISTS USERS (username varchar(255) not null primary key, password varchar(255) not null,admin boolean);";
            final Connection connection = dataSource.getConnection();
            final Statement statement = connection.createStatement();
            statement.execute(createUsers);
            statement.execute(
                "insert into USERS (username, password,admin) values ('admin','admin',true), ('user','user',false)");
            statement.close();
            connection.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        String username = req.getUserPrincipal().getName();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("select username, admin from USERS where username=?");
            statement.setString(1, username);
            statement.execute();
            resultSet = statement.getResultSet();
            resultSet.first();
            if (resultSet.isFirst()) {
                Json.createWriter(res.getWriter())
                    .writeObject(Json.createObjectBuilder()
                        .add("username", resultSet.getString("username"))
                        .add("admin", resultSet.getBoolean("admin"))
                        .build());
            } else {
                res.sendError(404);
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
    }
}

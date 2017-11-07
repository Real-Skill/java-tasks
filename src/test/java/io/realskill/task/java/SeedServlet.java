package io.realskill.task.java;

import com.auth0.jwt.algorithms.Algorithm;

import javax.annotation.Resource;
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
import java.sql.SQLException;

@WebServlet(urlPatterns = "/seed")
public class SeedServlet extends HttpServlet {

    @Resource(name = "jdbc/authority")
    DataSource dataSource;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try {
            getServletContext().setAttribute("jwtAlgorithm", Algorithm.HMAC256(req.getParameter("secret")));
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }

        final Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("delete from USERS");
            statement.execute();
            statement.close();

            final String userSQL = "insert into USERS (username, password, admin) values (?,?,?)";
            PreparedStatement userStatement = connection.prepareStatement(userSQL);

            final String[] usernames = req.getParameterValues("username");
            final String[] passwords = req.getParameterValues("password");
            for (int i = 0; i < usernames.length; i++) {
                userStatement.setString(1, usernames[i]);
                userStatement.setString(2, passwords[i]);
                userStatement.setBoolean(3, 0 == i);
                userStatement.execute();
            }
            userStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

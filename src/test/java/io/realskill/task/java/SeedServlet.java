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
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/seed")
public class SeedServlet extends HttpServlet {

    @Resource(name = "jdbc/authority")
    DataSource dataSource;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        final Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("delete from USERS");
            statement.execute();
            statement.close();
            statement = connection.prepareStatement("delete from USER_ROLES");
            statement.execute();
            statement.close();

            final String userSQL = "insert into USERS (user_name, user_pass) values (?,?)";
            final String roleSQL = "insert into USER_ROLES (user_name, role_name) values (?,?)";
            PreparedStatement userStatement = connection.prepareStatement(userSQL);
            PreparedStatement roleStatement = connection.prepareStatement(roleSQL);

            final String[] usernames = req.getParameterValues("username");
            final String[] passwords = req.getParameterValues("password");
            for (int i = 0; i < usernames.length; i++) {
                userStatement.setString(1, usernames[i]);
                userStatement.setString(2, passwords[i]);
                userStatement.execute();

                if (0 == i) {
                    roleStatement.setString(1, usernames[i]);
                    roleStatement.setString(2, "admin");
                    roleStatement.execute();
                }
                roleStatement.setString(1, usernames[i]);
                roleStatement.setString(2, "user");
                roleStatement.execute();
            }
            userStatement.close();
            roleStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

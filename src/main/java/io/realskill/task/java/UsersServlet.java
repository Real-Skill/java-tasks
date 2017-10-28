package io.realskill.task.java;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ServletSecurity(@HttpConstraint(rolesAllowed = "admin"))
@WebServlet(urlPatterns = "/users")
public class UsersServlet extends HttpServlet {

    @Resource(name = "jdbc/authority")
    DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        try {
            final Connection connection = dataSource.getConnection();
            final PreparedStatement statement = connection.prepareStatement("select user_name from USERS");
            statement.execute();
            final ResultSet resultSet = statement.getResultSet();
            final List<String> users = new ArrayList<>();
            req.setAttribute("users", users);
            while (resultSet.next()) {
                users.add(resultSet.getString(1));
            }
            System.out.println(users);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/users.jsp").forward(req, res);
    }
}

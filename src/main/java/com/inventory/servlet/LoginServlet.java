package com.inventory.servlet;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import com.inventory.util.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Login Servlet
 * Handles user authentication
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.getUserByUsername(username);

            if (user != null && PasswordUtils.verifyPassword(password, user.getPassword())) {
                // Authentication successful
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes

                // Send success response
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"success\": true, \"message\": \"Login successful\", \"redirect\": \"dashboard.html\"}");
            } else {
                // Authentication failed
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid username or password\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error. Please try again later.\"}");
        }
    }
}

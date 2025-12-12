package com.inventory.servlet;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import com.inventory.util.PasswordUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Register Servlet
 * Handles user registration
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;
    private Gson gson;

    @Override
    public void init() {
        userDAO = new UserDAO();
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Read JSON body if possible, or parameters.
        // login.html sends JSON or form data?
        // Based on login.html logic, it doesn't actually send a request yet (it uses
        // localStorage),
        // but robust APIs usually accept JSON.
        // However, standard HTML forms send x-www-form-urlencoded.
        // Let's support parameters as it's easier for standard servlets without a JSON
        // body reader helper.

        String username = request.getParameter("username");
        // Check if parameters are missing, might be JSON body
        if (username == null) {
            // Very basic JSON parsing fallback (or just assume form data for now as we
            // control frontend)
            // For a "real world" app, we'd use a Reader.
            // I'll stick to getParameter and ensure frontend sends form data (or query
            // params).
        }

        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String role = request.getParameter("role"); // Optional, default to user

        if (role == null || role.isEmpty()) {
            role = "staff";
        }

        if (username == null || password == null || fullName == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Missing required fields\"}");
            return;
        }

        try {
            // Check if user exists
            if (userDAO.getUserByUsername(username) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"success\": false, \"message\": \"Username already exists\"}");
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // Will be hashed by UserDAO
            user.setFullName(fullName);
            user.setEmail(email);
            user.setRole(role);

            if (userDAO.addUser(user)) {
                response.getWriter().write("{\"success\": true, \"message\": \"Registration successful\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Registration failed\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}

package com.inventory.servlet;

import com.inventory.dao.OrderDAO;
import com.inventory.model.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Order Servlet
 * Handles all order-related operations
 */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private Gson gson;

    @Override
    public void init() {
        orderDAO = new OrderDAO();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if ("getAll".equals(action)) {
                List<Order> orders = orderDAO.getAllOrders();
                response.getWriter().write(gson.toJson(orders));

            } else if ("getById".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Order order = orderDAO.getOrderById(id);
                response.getWriter().write(gson.toJson(order));

            } else if ("byStatus".equals(action)) {
                String status = request.getParameter("status");
                List<Order> orders = orderDAO.getOrdersByStatus(status);
                response.getWriter().write(gson.toJson(orders));

            } else if ("stats".equals(action)) {
                int totalCount = orderDAO.getTotalOrderCount();
                BigDecimal totalValue = orderDAO.getTotalOrderValue();
                String stats = String.format("{\"totalOrders\": %d, \"totalValue\": %.2f}",
                        totalCount, totalValue.doubleValue());
                response.getWriter().write(stats);

            } else {
                List<Order> orders = orderDAO.getAllOrders();
                response.getWriter().write(gson.toJson(orders));
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            Date orderDate = Date.valueOf(request.getParameter("orderDate"));
            Date expectedDate = Date.valueOf(request.getParameter("expectedDate"));
            String status = request.getParameter("status");
            BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));
            int userId = Integer.parseInt(request.getParameter("userId"));

            Order order = new Order();
            order.setOrderNumber(orderDAO.generateOrderNumber());
            order.setSupplierId(supplierId);
            order.setOrderDate(orderDate);
            order.setExpectedDeliveryDate(expectedDate);
            order.setStatus(status);
            order.setTotalAmount(totalAmount);
            order.setUserId(userId);

            boolean success = orderDAO.addOrder(order);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Order created successfully\", \"id\": "
                        + order.getId() + ", \"orderNumber\": \"" + order.getOrderNumber() + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to create order\"}");
            }

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String action = request.getParameter("action");
            int id = Integer.parseInt(request.getParameter("id"));

            boolean success = false;

            if ("updateStatus".equals(action)) {
                String status = request.getParameter("status");
                success = orderDAO.updateOrderStatus(id, status);
            } else {
                // Full update
                int supplierId = Integer.parseInt(request.getParameter("supplierId"));
                Date orderDate = Date.valueOf(request.getParameter("orderDate"));
                Date expectedDate = Date.valueOf(request.getParameter("expectedDate"));
                String status = request.getParameter("status");
                BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));

                Order order = new Order();
                order.setId(id);
                order.setSupplierId(supplierId);
                order.setOrderDate(orderDate);
                order.setExpectedDeliveryDate(expectedDate);
                order.setStatus(status);
                order.setTotalAmount(totalAmount);

                success = orderDAO.updateOrder(order);
            }

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Order updated successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to update order\"}");
            }

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = orderDAO.deleteOrder(id);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Order deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to delete order\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}

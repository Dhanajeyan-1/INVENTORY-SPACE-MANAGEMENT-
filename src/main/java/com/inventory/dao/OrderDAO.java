package com.inventory.dao;

import com.inventory.model.Order;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Data Access Object
 * Handles all database operations for orders
 */
public class OrderDAO {

    /**
     * Get all orders with supplier information
     */
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.name as supplier_name, u.full_name as user_name " +
                "FROM orders o " +
                "LEFT JOIN suppliers s ON o.supplier_id = s.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "ORDER BY o.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        }
        return orders;
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(int id) throws SQLException {
        String sql = "SELECT o.*, s.name as supplier_name, u.full_name as user_name " +
                "FROM orders o " +
                "LEFT JOIN suppliers s ON o.supplier_id = s.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE o.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractOrderFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get order by order number
     */
    public Order getOrderByNumber(String orderNumber) throws SQLException {
        String sql = "SELECT o.*, s.name as supplier_name, u.full_name as user_name " +
                "FROM orders o " +
                "LEFT JOIN suppliers s ON o.supplier_id = s.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE o.order_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractOrderFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Add new order
     */
    public boolean addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (order_number, supplier_id, order_date, expected_delivery_date, status, total_amount, user_id) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getOrderNumber());
            stmt.setInt(2, order.getSupplierId());
            stmt.setDate(3, order.getOrderDate());
            stmt.setDate(4, order.getExpectedDeliveryDate());
            stmt.setString(5, order.getStatus());
            stmt.setBigDecimal(6, order.getTotalAmount());
            stmt.setInt(7, order.getUserId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Update order
     */
    public boolean updateOrder(Order order) throws SQLException {
        String sql = "UPDATE orders SET supplier_id = ?, order_date = ?, expected_delivery_date = ?, " +
                "status = ?, total_amount = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getSupplierId());
            stmt.setDate(2, order.getOrderDate());
            stmt.setDate(3, order.getExpectedDeliveryDate());
            stmt.setString(4, order.getStatus());
            stmt.setBigDecimal(5, order.getTotalAmount());
            stmt.setInt(6, order.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Update order status
     */
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete order
     */
    public boolean deleteOrder(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.name as supplier_name, u.full_name as user_name " +
                "FROM orders o " +
                "LEFT JOIN suppliers s ON o.supplier_id = s.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE o.status = ? " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(extractOrderFromResultSet(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Generate unique order number
     */
    public String generateOrderNumber() throws SQLException {
        String sql = "SELECT order_number FROM orders ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastOrderNumber = rs.getString("order_number");
                // Extract number from PO-2024-XXX format
                String[] parts = lastOrderNumber.split("-");
                int nextNumber = Integer.parseInt(parts[2]) + 1;
                return String.format("PO-%d-%03d", java.time.Year.now().getValue(), nextNumber);
            } else {
                return String.format("PO-%d-001", java.time.Year.now().getValue());
            }
        }
    }

    /**
     * Get total order count
     */
    public int getTotalOrderCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Get total order value
     */
    public BigDecimal getTotalOrderValue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status = 'received'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Helper method to extract Order from ResultSet
     */
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setSupplierId(rs.getInt("supplier_id"));
        order.setSupplierName(rs.getString("supplier_name"));
        order.setOrderDate(rs.getDate("order_date"));
        order.setExpectedDeliveryDate(rs.getDate("expected_delivery_date"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setUserId(rs.getInt("user_id"));
        order.setUserName(rs.getString("user_name"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        return order;
    }
}

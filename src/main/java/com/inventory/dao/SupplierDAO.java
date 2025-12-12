package com.inventory.dao;

import com.inventory.model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Supplier Data Access Object
 * Handles all database operations for suppliers
 */
public class SupplierDAO {

    /**
     * Get all suppliers
     */
    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(extractSupplierFromResultSet(rs));
            }
        }
        return suppliers;
    }

    /**
     * Get supplier by ID
     */
    public Supplier getSupplierById(int id) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSupplierFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Add new supplier
     */
    public boolean addSupplier(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (name, contact_person, email, phone, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getPhone());
            stmt.setString(5, supplier.getAddress());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        supplier.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Update supplier
     */
    public boolean updateSupplier(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET name = ?, contact_person = ?, email = ?, phone = ?, address = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getPhone());
            stmt.setString(5, supplier.getAddress());
            stmt.setInt(6, supplier.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete supplier
     */
    public boolean deleteSupplier(int id) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Search suppliers by name
     */
    public List<Supplier> searchSuppliers(String keyword) throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE name LIKE ? OR contact_person LIKE ? ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    suppliers.add(extractSupplierFromResultSet(rs));
                }
            }
        }
        return suppliers;
    }

    /**
     * Get product count by supplier
     */
    public int getProductCountBySupplier(int supplierId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE supplier_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Helper method to extract Supplier from ResultSet
     */
    private Supplier extractSupplierFromResultSet(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getInt("id"));
        supplier.setName(rs.getString("name"));
        supplier.setContactPerson(rs.getString("contact_person"));
        supplier.setEmail(rs.getString("email"));
        supplier.setPhone(rs.getString("phone"));
        supplier.setAddress(rs.getString("address"));
        supplier.setCreatedAt(rs.getTimestamp("created_at"));
        return supplier;
    }
}

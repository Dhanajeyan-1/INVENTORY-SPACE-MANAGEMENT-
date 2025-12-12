package com.inventory.dao;

import com.inventory.model.Product;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Product Data Access Object
 * Handles all database operations for products
 */
public class ProductDAO {

    /**
     * Get all products with category and supplier information
     */
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, s.name as supplier_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                    "ORDER BY p.id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }
        return products;
    }

    /**
     * Get product by ID
     */
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT p.*, c.name as category_name, s.name as supplier_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                    "WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractProductFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Add new product
     */
    public boolean addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, sku, category_id, supplier_id, description, " +
                    "unit_price, quantity_in_stock, reorder_level, image_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getSku());
            stmt.setInt(3, product.getCategoryId());
            stmt.setInt(4, product.getSupplierId());
            stmt.setString(5, product.getDescription());
            stmt.setBigDecimal(6, product.getUnitPrice());
            stmt.setInt(7, product.getQuantityInStock());
            stmt.setInt(8, product.getReorderLevel());
            stmt.setString(9, product.getImageUrl());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Update existing product
     */
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, sku = ?, category_id = ?, supplier_id = ?, " +
                    "description = ?, unit_price = ?, quantity_in_stock = ?, reorder_level = ?, " +
                    "image_url = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getSku());
            stmt.setInt(3, product.getCategoryId());
            stmt.setInt(4, product.getSupplierId());
            stmt.setString(5, product.getDescription());
            stmt.setBigDecimal(6, product.getUnitPrice());
            stmt.setInt(7, product.getQuantityInStock());
            stmt.setInt(8, product.getReorderLevel());
            stmt.setString(9, product.getImageUrl());
            stmt.setInt(10, product.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete product
     */
    public boolean deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Search products by name or SKU
     */
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, s.name as supplier_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                    "WHERE p.name LIKE ? OR p.sku LIKE ? OR p.description LIKE ? " +
                    "ORDER BY p.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
        }
        return products;
    }

    /**
     * Get low stock products
     */
    public List<Product> getLowStockProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, s.name as supplier_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                    "WHERE p.quantity_in_stock <= p.reorder_level " +
                    "ORDER BY p.quantity_in_stock ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        }
        return products;
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(int categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name, s.name as supplier_name " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                    "WHERE p.category_id = ? " +
                    "ORDER BY p.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
        }
        return products;
    }

    /**
     * Get total number of products
     */
    public int getTotalProductCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";

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
     * Get total inventory value
     */
    public BigDecimal getTotalInventoryValue() throws SQLException {
        String sql = "SELECT SUM(unit_price * quantity_in_stock) FROM products";

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
     * Helper method to extract Product object from ResultSet
     */
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setSku(rs.getString("sku"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setSupplierId(rs.getInt("supplier_id"));
        product.setDescription(rs.getString("description"));
        product.setUnitPrice(rs.getBigDecimal("unit_price"));
        product.setQuantityInStock(rs.getInt("quantity_in_stock"));
        product.setReorderLevel(rs.getInt("reorder_level"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        product.setCategoryName(rs.getString("category_name"));
        product.setSupplierName(rs.getString("supplier_name"));
        return product;
    }
}

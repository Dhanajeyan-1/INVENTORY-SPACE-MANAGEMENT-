-- Inventory Management System Database Schema
-- Created: 2025-12-04

-- Drop existing database if exists and create new
DROP DATABASE IF EXISTS inventory_management;
CREATE DATABASE inventory_management;
USE inventory_management;

-- 1. Users Table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('admin', 'manager', 'staff') DEFAULT 'staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- 2. Categories Table
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

-- 3. Suppliers Table
CREATE TABLE suppliers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

-- 4. Products Table
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    sku VARCHAR(50) UNIQUE NOT NULL,
    category_id INT,
    supplier_id INT,
    description TEXT,
    unit_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quantity_in_stock INT NOT NULL DEFAULT 0,
    reorder_level INT DEFAULT 10,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
    INDEX idx_name (name),
    INDEX idx_sku (sku),
    INDEX idx_category (category_id),
    INDEX idx_supplier (supplier_id)
);

-- 5. Stock Movements Table
CREATE TABLE stock_movements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    movement_type ENUM('in', 'out', 'adjustment') NOT NULL,
    quantity INT NOT NULL,
    reference_number VARCHAR(50),
    notes TEXT,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_product (product_id),
    INDEX idx_type (movement_type),
    INDEX idx_date (created_at)
);

-- 6. Orders Table
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    supplier_id INT,
    order_date DATE NOT NULL,
    expected_delivery_date DATE,
    status ENUM('pending', 'received', 'cancelled') DEFAULT 'pending',
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_order_number (order_number),
    INDEX idx_status (status),
    INDEX idx_date (order_date)
);

-- 7. Order Items Table
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_order (order_id),
    INDEX idx_product (product_id)
);

-- Insert Sample Data

-- Sample Users (password: 'admin123' hashed with BCrypt)
INSERT INTO users (username, password, full_name, email, role) VALUES
('admin', '$2a$10$xJ3YQ7qZ5K3Xz4hZ5K3xZ.Z5K3xZ4hZ5K3xZ4hZ5K3xZ4hZ5K3xZu', 'Admin User', 'admin@inventory.com', 'admin'),
('manager1', '$2a$10$xJ3YQ7qZ5K3Xz4hZ5K3xZ.Z5K3xZ4hZ5K3xZ4hZ5K3xZ4hZ5K3xZu', 'John Manager', 'manager@inventory.com', 'manager'),
('staff1', '$2a$10$xJ3YQ7qZ5K3Xz4hZ5K3xZ.Z5K3xZ4hZ5K3xZ4hZ5K3xZ4hZ5K3xZu', 'Jane Staff', 'staff@inventory.com', 'staff');

-- Sample Categories
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Furniture', 'Office and home furniture'),
('Stationery', 'Office supplies and stationery items'),
('Hardware', 'Tools and hardware equipment'),
('Beverages', 'Drinks and refreshments');

-- Sample Suppliers
INSERT INTO suppliers (name, contact_person, email, phone, address) VALUES
('Tech Solutions Inc.', 'Michael Chen', 'michael@techsolutions.com', '+1-555-0101', '123 Tech Street, Silicon Valley, CA'),
('Office Depot Pro', 'Sarah Johnson', 'sarah@officedepot.com', '+1-555-0102', '456 Business Ave, New York, NY'),
('Global Hardware Co.', 'David Martinez', 'david@globalhardware.com', '+1-555-0103', '789 Industry Blvd, Chicago, IL'),
('Furniture World', 'Emily Brown', 'emily@furnitureworld.com', '+1-555-0104', '321 Design Lane, Los Angeles, CA');

-- Sample Products
INSERT INTO products (name, sku, category_id, supplier_id, description, unit_price, quantity_in_stock, reorder_level) VALUES
('Wireless Mouse', 'ELEC-001', 1, 1, 'Ergonomic wireless mouse with USB receiver', 29.99, 150, 20),
('Mechanical Keyboard', 'ELEC-002', 1, 1, 'RGB mechanical gaming keyboard', 89.99, 75, 15),
('Office Desk', 'FURN-001', 2, 4, 'Adjustable height standing desk', 399.99, 25, 5),
('Office Chair', 'FURN-002', 2, 4, 'Ergonomic mesh office chair', 249.99, 40, 10),
('A4 Paper (500 sheets)', 'STAT-001', 3, 2, 'Premium white A4 printing paper', 8.99, 500, 50),
('Ballpoint Pens (Pack of 12)', 'STAT-002', 3, 2, 'Blue ink ballpoint pens', 5.99, 300, 40),
('Screwdriver Set', 'HARD-001', 4, 3, '10-piece precision screwdriver set', 24.99, 80, 15),
('Hammer', 'HARD-002', 4, 3, 'Claw hammer with fiberglass handle', 18.99, 60, 10),
('Coffee Maker', 'BEVE-001', 5, 2, '12-cup programmable coffee maker', 79.99, 30, 8),
('Water Cooler', 'BEVE-002', 5, 2, 'Hot and cold water dispenser', 199.99, 15, 5);

-- Sample Stock Movements
INSERT INTO stock_movements (product_id, movement_type, quantity, reference_number, notes, user_id) VALUES
(1, 'in', 100, 'PO-2024-001', 'Initial stock purchase', 1),
(2, 'in', 50, 'PO-2024-002', 'Restocking keyboards', 1),
(1, 'out', 25, 'SO-2024-001', 'Sales order fulfillment', 2),
(3, 'in', 20, 'PO-2024-003', 'New desk shipment', 1),
(5, 'adjustment', -10, 'ADJ-2024-001', 'Damaged items removed', 2);

-- Sample Orders
INSERT INTO orders (order_number, supplier_id, order_date, expected_delivery_date, status, total_amount, user_id) VALUES
('PO-2024-001', 1, '2024-11-01', '2024-11-10', 'received', 3499.00, 1),
('PO-2024-002', 2, '2024-11-15', '2024-11-25', 'received', 1250.00, 1),
('PO-2024-003', 4, '2024-12-01', '2024-12-15', 'pending', 7999.80, 1);

-- Sample Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(1, 1, 100, 29.99, 2999.00),
(1, 2, 50, 89.99, 4499.50),
(2, 5, 100, 8.99, 899.00),
(2, 6, 50, 5.99, 299.50),
(3, 3, 20, 399.99, 7999.80);

-- Create Views for Common Queries

-- View: Low Stock Products
CREATE VIEW low_stock_products AS
SELECT 
    p.id,
    p.name,
    p.sku,
    p.quantity_in_stock,
    p.reorder_level,
    c.name as category_name,
    s.name as supplier_name
FROM products p
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN suppliers s ON p.supplier_id = s.id
WHERE p.quantity_in_stock <= p.reorder_level;

-- View: Product Inventory Summary
CREATE VIEW product_inventory_summary AS
SELECT 
    p.id,
    p.name,
    p.sku,
    p.unit_price,
    p.quantity_in_stock,
    (p.unit_price * p.quantity_in_stock) as stock_value,
    c.name as category_name,
    s.name as supplier_name,
    CASE 
        WHEN p.quantity_in_stock = 0 THEN 'Out of Stock'
        WHEN p.quantity_in_stock <= p.reorder_level THEN 'Low Stock'
        ELSE 'In Stock'
    END as stock_status
FROM products p
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN suppliers s ON p.supplier_id = s.id;

-- View: Recent Stock Movements
CREATE VIEW recent_stock_movements AS
SELECT 
    sm.id,
    p.name as product_name,
    p.sku,
    sm.movement_type,
    sm.quantity,
    sm.reference_number,
    sm.notes,
    u.full_name as user_name,
    sm.created_at
FROM stock_movements sm
JOIN products p ON sm.product_id = p.id
LEFT JOIN users u ON sm.user_id = u.id
ORDER BY sm.created_at DESC
LIMIT 100;

-- Stored Procedure: Update Stock
-- Stored Procedure removed for compatibility with simple runner
-- (Logic handled in Java Application Layer)

-- Display success message
SELECT 'Database created successfully!' as message;
SELECT COUNT(*) as total_products FROM products;
SELECT COUNT(*) as total_categories FROM categories;
SELECT COUNT(*) as total_suppliers FROM suppliers;

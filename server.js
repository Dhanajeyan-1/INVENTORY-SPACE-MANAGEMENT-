const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const db = require('./db');
const bcrypt = require('bcryptjs');

const app = express();
const PORT = 8080;

// Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'src', 'main', 'webapp')));

// --- AUTHENTICATION ROUTES ---

app.post('/login', async (req, res) => {
    const { username, password } = req.body;

    try {
        const [rows] = await db.execute('SELECT * FROM users WHERE username = ?', [username]);

        if (rows.length > 0) {
            const user = rows[0];
            const isValid = await bcrypt.compare(password, user.password);

            if (isValid) {
                // In a real app, set a session or JWT here
                res.json({
                    success: true,
                    message: 'Login successful',
                    redirect: 'dashboard.html',
                    user: {
                        id: user.id,
                        username: user.username,
                        fullName: user.full_name,
                        role: user.role
                    }
                });
            } else {
                res.status(401).json({ success: false, message: 'Invalid username or password' });
            }
        } else {
            res.status(401).json({ success: false, message: 'Invalid username or password' });
        }
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Server error' });
    }
});

app.post('/register', async (req, res) => {
    const { username, password, fullName, email, role } = req.body;
    const userRole = role || 'user';

    try {
        // Check if user exists
        const [existing] = await db.execute('SELECT id FROM users WHERE username = ?', [username]);
        if (existing.length > 0) {
            return res.status(409).json({ success: false, message: 'Username already exists' });
        }

        const hashedPassword = await bcrypt.hash(password, 10);

        await db.execute(
            'INSERT INTO users (username, password, full_name, email, role) VALUES (?, ?, ?, ?, ?)',
            [username, hashedPassword, fullName, email || '', userRole]
        );

        res.json({ success: true, message: 'Registration successful' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: 'Server error: ' + error.message });
    }
});

app.post('/logout', (req, res) => {
    // Clear session if any
    res.json({ success: true });
});

// --- PRODUCT ROUTES ---

app.get('/products', async (req, res) => {
    const action = req.query.action || 'getAll';

    try {
        if (action === 'getAll') {
            const [rows] = await db.execute(`
                SELECT p.*, c.name as category_name, s.name as supplier_name 
                FROM products p 
                LEFT JOIN categories c ON p.category_id = c.id 
                LEFT JOIN suppliers s ON p.supplier_id = s.id 
                ORDER BY p.id DESC
            `);
            res.json(rows);

        } else if (action === 'getById') {
            const [rows] = await db.execute('SELECT * FROM products WHERE id = ?', [req.query.id]);
            res.json(rows[0] || {});

        } else if (action === 'search') {
            const keyword = `%${req.query.keyword}%`;
            const [rows] = await db.execute(`
                SELECT p.*, c.name as category_name, s.name as supplier_name 
                FROM products p 
                LEFT JOIN categories c ON p.category_id = c.id 
                LEFT JOIN suppliers s ON p.supplier_id = s.id 
                WHERE p.name LIKE ? OR p.sku LIKE ?
            `, [keyword, keyword]);
            res.json(rows);

        } else if (action === 'stats') {
            const [countRows] = await db.execute('SELECT COUNT(*) as count FROM products');
            const [valRows] = await db.execute('SELECT SUM(unit_price * quantity_in_stock) as totalValue FROM products');
            res.json({
                totalProducts: countRows[0].count,
                totalValue: valRows[0].totalValue || 0
            });
        }
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: error.message });
    }
});

app.post('/products', async (req, res) => {
    const { name, sku, categoryId, supplierId, description, unitPrice, quantityInStock, reorderLevel, imageUrl } = req.body;

    try {
        const [result] = await db.execute(
            `INSERT INTO products (name, sku, category_id, supplier_id, description, unit_price, quantity_in_stock, reorder_level, image_url) 
             VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
            [name, sku, categoryId, supplierId, description, unitPrice, quantityInStock, reorderLevel, imageUrl || '']
        );
        res.json({ success: true, message: 'Product added', id: result.insertId });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: error.message });
    }
});

app.put('/products', async (req, res) => {
    // Check URL params first (id)
    const id = req.query.id || req.body.id;
    const { name, sku, categoryId, supplierId, description, unitPrice, quantityInStock, reorderLevel, imageUrl } = req.body;

    try {
        await db.execute(
            `UPDATE products SET name=?, sku=?, category_id=?, supplier_id=?, description=?, unit_price=?, quantity_in_stock=?, reorder_level=?, image_url=? WHERE id=?`,
            [name, sku, categoryId, supplierId, description, unitPrice, quantityInStock, reorderLevel, imageUrl || '', id]
        );
        res.json({ success: true, message: 'Product updated' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: error.message });
    }
});

app.delete('/products', async (req, res) => {
    const id = req.query.id;
    try {
        await db.execute('DELETE FROM products WHERE id = ?', [id]);
        res.json({ success: true, message: 'Product deleted' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: error.message });
    }
});

// --- ORDER ROUTES --- (Basic implementation)
app.get('/orders', async (req, res) => {
    try {
        const [rows] = await db.execute(`
            SELECT o.*, s.name as supplier_name 
            FROM orders o 
            LEFT JOIN suppliers s ON o.supplier_id = s.id 
            ORDER BY o.created_at DESC
        `);
        res.json(rows);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Start Server
app.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}/`);
    console.log(`Open http://localhost:${PORT}/login.html to start`);
});

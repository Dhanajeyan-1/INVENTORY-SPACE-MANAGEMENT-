const express = require('express');
const Order = require('../models/Order');
const requireAuth = require('../middleware/auth');

const router = express.Router();

router.get('/', requireAuth, async (req, res) => {
    try {
        const orders = await Order.find().populate('supplier').sort({ createdAt: -1 });
        res.json(orders.map(o => ({
            ...o.toObject(),
            id: o._id,
            supplierName: o.supplier && o.supplier.name ? o.supplier.name : null
        })));
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

router.post('/', requireAuth, async (req, res) => {
    const { supplierId, items } = req.body;

    try {
        const orderNumber = 'PO-' + Date.now();
        const order = await Order.create({
            orderNumber,
            supplier: supplierId || null,
            createdBy: req.user.id,
            items: items || []
        });
        res.json({ success: true, message: 'Order created', id: order._id, orderNumber });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;

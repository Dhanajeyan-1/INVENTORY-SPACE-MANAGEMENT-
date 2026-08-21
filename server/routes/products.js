const express = require('express');
const Product = require('../models/Product');
const requireAuth = require('../middleware/auth');

const router = express.Router();

function serialize(product) {
    const obj = product.toObject();
    return {
        ...obj,
        id: obj._id,
        categoryId: obj.category ? obj.category._id || obj.category : null,
        categoryName: obj.category && obj.category.name ? obj.category.name : null,
        supplierId: obj.supplier ? obj.supplier._id || obj.supplier : null,
        supplierName: obj.supplier && obj.supplier.name ? obj.supplier.name : null
    };
}

router.get('/', requireAuth, async (req, res) => {
    const { action, id, keyword } = req.query;

    try {
        if (action === 'getById') {
            const product = await Product.findById(id).populate('category').populate('supplier');
            return res.json(product ? serialize(product) : {});
        }

        if (action === 'search') {
            const regex = new RegExp(keyword, 'i');
            const products = await Product.find({ $or: [{ name: regex }, { sku: regex }] })
                .populate('category').populate('supplier').sort({ createdAt: -1 });
            return res.json(products.map(serialize));
        }

        if (action === 'lowStock') {
            const products = await Product.find({ $expr: { $lte: ['$quantityInStock', '$reorderLevel'] } })
                .populate('category').populate('supplier');
            return res.json(products.map(serialize));
        }

        if (action === 'stats') {
            const totalProducts = await Product.countDocuments();
            const products = await Product.find({}, 'unitPrice quantityInStock');
            const totalValue = products.reduce((sum, p) => sum + p.unitPrice * p.quantityInStock, 0);
            return res.json({ totalProducts, totalValue });
        }

        const products = await Product.find().populate('category').populate('supplier').sort({ createdAt: -1 });
        res.json(products.map(serialize));
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: error.message });
    }
});

router.post('/', requireAuth, async (req, res) => {
    const { name, sku, categoryId, supplierId, description, unitPrice, quantityInStock, reorderLevel, imageUrl } = req.body;

    try {
        const product = await Product.create({
            name, sku,
            category: categoryId || null,
            supplier: supplierId || null,
            description, unitPrice, quantityInStock, reorderLevel,
            imageUrl: imageUrl || ''
        });
        res.json({ success: true, message: 'Product added', id: product._id });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: error.message });
    }
});

router.put('/', requireAuth, async (req, res) => {
    const id = req.query.id || req.body.id;
    const { name, sku, categoryId, supplierId, description, unitPrice, quantityInStock, reorderLevel, imageUrl } = req.body;

    try {
        await Product.findByIdAndUpdate(id, {
            name, sku,
            category: categoryId || null,
            supplier: supplierId || null,
            description, unitPrice, quantityInStock, reorderLevel,
            imageUrl: imageUrl || ''
        });
        res.json({ success: true, message: 'Product updated' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: error.message });
    }
});

router.delete('/', requireAuth, async (req, res) => {
    try {
        await Product.findByIdAndDelete(req.query.id);
        res.json({ success: true, message: 'Product deleted' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;

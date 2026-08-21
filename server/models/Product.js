const mongoose = require('mongoose');

const productSchema = new mongoose.Schema({
    name: { type: String, required: true, trim: true },
    sku: { type: String, required: true, unique: true, trim: true },
    category: { type: mongoose.Schema.Types.ObjectId, ref: 'Category', default: null },
    supplier: { type: mongoose.Schema.Types.ObjectId, ref: 'Supplier', default: null },
    description: { type: String, default: '' },
    unitPrice: { type: Number, required: true, default: 0 },
    quantityInStock: { type: Number, required: true, default: 0 },
    reorderLevel: { type: Number, default: 10 },
    imageUrl: { type: String, default: '' }
}, { timestamps: true });

module.exports = mongoose.model('Product', productSchema);

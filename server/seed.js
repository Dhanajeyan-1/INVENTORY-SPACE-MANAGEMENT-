require('dotenv').config();
const bcrypt = require('bcryptjs');
const connectDB = require('./db');
const User = require('./models/User');
const Category = require('./models/Category');
const Supplier = require('./models/Supplier');
const Product = require('./models/Product');

async function seed() {
    await connectDB();

    await Promise.all([
        User.deleteMany({}),
        Category.deleteMany({}),
        Supplier.deleteMany({}),
        Product.deleteMany({})
    ]);

    const passwordHash = await bcrypt.hash('admin123', 10);
    await User.insertMany([
        { username: 'admin', password: passwordHash, fullName: 'Admin User', email: 'admin@example.com', role: 'admin' },
        { username: 'manager1', password: passwordHash, fullName: 'Manager One', email: 'manager1@example.com', role: 'manager' },
        { username: 'staff1', password: passwordHash, fullName: 'Staff One', email: 'staff1@example.com', role: 'staff' }
    ]);

    const categories = await Category.insertMany([
        { name: 'Electronics', description: 'Electronic devices and gadgets' },
        { name: 'Accessories', description: 'Accessories for devices' },
        { name: 'Furniture', description: 'Office and home furniture' }
    ]);

    const suppliers = await Supplier.insertMany([
        { name: 'TechCorp', contactPerson: 'John Doe', email: 'sales@techcorp.com', phone: '555-0100' },
        { name: 'GadgetWorld', contactPerson: 'Jane Smith', email: 'sales@gadgetworld.com', phone: '555-0101' },
        { name: 'OfficeDepot', contactPerson: 'Sam Lee', email: 'sales@officedepot.com', phone: '555-0102' },
        { name: 'DisplayInc', contactPerson: 'Amy Chen', email: 'sales@displayinc.com', phone: '555-0103' }
    ]);

    await Product.insertMany([
        { name: 'Wireless Mouse', sku: 'ELEC-001', category: categories[0]._id, supplier: suppliers[0]._id, unitPrice: 19.99, quantityInStock: 150, reorderLevel: 20 },
        { name: 'USB-C Cable', sku: 'ACC-001', category: categories[1]._id, supplier: suppliers[1]._id, unitPrice: 9.99, quantityInStock: 300, reorderLevel: 50 },
        { name: 'Office Chair', sku: 'FURN-001', category: categories[2]._id, supplier: suppliers[2]._id, unitPrice: 149.99, quantityInStock: 8, reorderLevel: 10 }
    ]);

    console.log('Seed complete. Demo login: admin / admin123');
    process.exit(0);
}

seed().catch((error) => {
    console.error(error);
    process.exit(1);
});

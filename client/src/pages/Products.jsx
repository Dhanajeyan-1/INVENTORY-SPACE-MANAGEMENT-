import { useEffect, useState } from 'react';
import Layout from '../Layout';
import api from '../api';

const emptyForm = {
    name: '', sku: '', categoryId: '', supplierId: '',
    description: '', unitPrice: '', quantityInStock: '', reorderLevel: 10, imageUrl: ''
};

export default function Products() {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [suppliers, setSuppliers] = useState([]);
    const [search, setSearch] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingId, setEditingId] = useState(null);
    const [form, setForm] = useState(emptyForm);

    async function loadProducts() {
        const { data } = await api.get('/products?action=getAll');
        setProducts(data);
    }

    useEffect(() => {
        loadProducts();
        api.get('/categories').then((res) => setCategories(res.data));
        api.get('/suppliers').then((res) => setSuppliers(res.data));
    }, []);

    async function handleSearch(e) {
        e.preventDefault();
        if (!search.trim()) return loadProducts();
        const { data } = await api.get(`/products?action=search&keyword=${encodeURIComponent(search)}`);
        setProducts(data);
    }

    function openAddForm() {
        setForm(emptyForm);
        setEditingId(null);
        setShowForm(true);
    }

    function openEditForm(p) {
        setForm({
            name: p.name, sku: p.sku,
            categoryId: p.categoryId || '', supplierId: p.supplierId || '',
            description: p.description || '', unitPrice: p.unitPrice,
            quantityInStock: p.quantityInStock, reorderLevel: p.reorderLevel, imageUrl: p.imageUrl || ''
        });
        setEditingId(p.id);
        setShowForm(true);
    }

    async function handleSubmit(e) {
        e.preventDefault();
        if (editingId) {
            await api.put(`/products?id=${editingId}`, form);
        } else {
            await api.post('/products', form);
        }
        setShowForm(false);
        loadProducts();
    }

    async function handleDelete(id) {
        if (!confirm('Delete this product?')) return;
        await api.delete(`/products?id=${id}`);
        loadProducts();
    }

    return (
        <Layout>
            <div className="page-header">
                <h1>Products</h1>
                <button onClick={openAddForm}>+ Add Product</button>
            </div>

            <form className="search-bar" onSubmit={handleSearch}>
                <input
                    placeholder="Search by name or SKU..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <button type="submit">Search</button>
            </form>

            <table className="data-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>SKU</th>
                        <th>Category</th>
                        <th>Supplier</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map((p) => (
                        <tr key={p.id}>
                            <td>{p.name}</td>
                            <td>{p.sku}</td>
                            <td>{p.categoryName || '-'}</td>
                            <td>{p.supplierName || '-'}</td>
                            <td>${Number(p.unitPrice).toFixed(2)}</td>
                            <td>{p.quantityInStock}</td>
                            <td>
                                <button onClick={() => openEditForm(p)}>Edit</button>
                                <button onClick={() => handleDelete(p.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                    {products.length === 0 && (
                        <tr><td colSpan="7">No products found</td></tr>
                    )}
                </tbody>
            </table>

            {showForm && (
                <div className="modal-overlay" onClick={() => setShowForm(false)}>
                    <form className="modal" onClick={(e) => e.stopPropagation()} onSubmit={handleSubmit}>
                        <h2>{editingId ? 'Edit Product' : 'Add Product'}</h2>

                        <label>Name
                            <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
                        </label>

                        <label>SKU
                            <input value={form.sku} onChange={(e) => setForm({ ...form, sku: e.target.value })} required />
                        </label>

                        <label>Category
                            <select value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })} required>
                                <option value="">Select Category</option>
                                {categories.map((c) => <option key={c._id} value={c._id}>{c.name}</option>)}
                            </select>
                        </label>

                        <label>Supplier
                            <select value={form.supplierId} onChange={(e) => setForm({ ...form, supplierId: e.target.value })} required>
                                <option value="">Select Supplier</option>
                                {suppliers.map((s) => <option key={s._id} value={s._id}>{s.name}</option>)}
                            </select>
                        </label>

                        <label>Unit Price
                            <input type="number" step="0.01" value={form.unitPrice} onChange={(e) => setForm({ ...form, unitPrice: e.target.value })} required />
                        </label>

                        <label>Quantity In Stock
                            <input type="number" value={form.quantityInStock} onChange={(e) => setForm({ ...form, quantityInStock: e.target.value })} required />
                        </label>

                        <label>Reorder Level
                            <input type="number" value={form.reorderLevel} onChange={(e) => setForm({ ...form, reorderLevel: e.target.value })} />
                        </label>

                        <label>Description
                            <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
                        </label>

                        <div className="modal-actions">
                            <button type="button" onClick={() => setShowForm(false)}>Cancel</button>
                            <button type="submit">{editingId ? 'Save' : 'Add'}</button>
                        </div>
                    </form>
                </div>
            )}
        </Layout>
    );
}

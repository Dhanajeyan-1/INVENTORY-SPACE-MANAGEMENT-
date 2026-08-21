import { useEffect, useState } from 'react';
import Layout from '../Layout';
import api from '../api';

export default function Dashboard() {
    const [stats, setStats] = useState({ totalProducts: 0, totalValue: 0 });
    const [lowStock, setLowStock] = useState([]);

    useEffect(() => {
        api.get('/products?action=stats').then((res) => setStats(res.data));
        api.get('/products?action=lowStock').then((res) => setLowStock(res.data));
    }, []);

    return (
        <Layout>
            <h1>Dashboard</h1>

            <div className="stat-grid">
                <div className="stat-card">
                    <span className="stat-label">Total Products</span>
                    <span className="stat-value">{stats.totalProducts}</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Inventory Value</span>
                    <span className="stat-value">${Number(stats.totalValue || 0).toFixed(2)}</span>
                </div>
                <div className="stat-card">
                    <span className="stat-label">Low Stock Items</span>
                    <span className="stat-value">{lowStock.length}</span>
                </div>
            </div>

            <h2>Low Stock Alerts</h2>
            <table className="data-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>SKU</th>
                        <th>In Stock</th>
                        <th>Reorder Level</th>
                    </tr>
                </thead>
                <tbody>
                    {lowStock.map((p) => (
                        <tr key={p.id}>
                            <td>{p.name}</td>
                            <td>{p.sku}</td>
                            <td>{p.quantityInStock}</td>
                            <td>{p.reorderLevel}</td>
                        </tr>
                    ))}
                    {lowStock.length === 0 && (
                        <tr><td colSpan="4">No low stock items</td></tr>
                    )}
                </tbody>
            </table>
        </Layout>
    );
}

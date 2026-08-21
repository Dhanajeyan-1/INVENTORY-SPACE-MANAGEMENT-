import { Link, useLocation } from 'react-router-dom';
import { useAuth } from './AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Layout({ children }) {
    const { user, logout } = useAuth();
    const location = useLocation();
    const navigate = useNavigate();

    function handleLogout() {
        logout();
        navigate('/login');
    }

    return (
        <div className="app-shell">
            <aside className="sidebar">
                <h2>Inventory</h2>
                <nav>
                    <Link className={location.pathname === '/dashboard' ? 'active' : ''} to="/dashboard">Dashboard</Link>
                    <Link className={location.pathname === '/products' ? 'active' : ''} to="/products">Products</Link>
                </nav>
                <div className="sidebar-footer">
                    <p>{user?.fullName} ({user?.role})</p>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            </aside>
            <main className="content">{children}</main>
        </div>
    );
}

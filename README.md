# 📦 Inventory Management System

A comprehensive, full-stack inventory management system built with **Java (Servlets)**, **MySQL**, and modern **HTML/CSS/JavaScript**.

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)

---

## ✨ Features

### 🔐 User Management
- Secure authentication with BCrypt password hashing
- Role-based access control (Admin, Manager, Staff)
- Session management

### 📦 Product Management
- Complete CRUD operations
- Advanced search and filtering
- Low stock alerts
- Category and supplier associations
- SKU tracking

### 🏷️ Category Management
- Organize products by categories
- Category-based filtering
- Product count tracking

### 🏭 Supplier Management
- Supplier contact information
- Product-supplier relationships
- Supplier search functionality

### 🛒 Order Management
- Purchase order creation
- Order status tracking
- Automatic order number generation
- Order history

### 📊 Dashboard
- Real-time inventory statistics
- Low stock alerts
- Inventory value tracking
- Recent activity feed

### 🎨 Modern UI/UX
- Responsive design
- Gradient backgrounds
- Glassmorphism effects
- Smooth animations
- Mobile-friendly interface

---

## 🏗️ Technology Stack

### Backend
- **Java 11+** - Core programming language
- **Servlets** - HTTP request handling
- **JDBC** - Database connectivity
- **BCrypt** - Password hashing
- **Gson** - JSON processing
- **Maven** - Dependency management

### Frontend
- **HTML5** - Structure
- **CSS3** - Modern styling with CSS variables
- **Vanilla JavaScript** - Dynamic functionality
- **Fetch API** - Asynchronous requests

### Database
- **MySQL 8.0+** - Relational database
- **SQL** - Data manipulation and queries

### Server
- **Apache Tomcat 9+** - Servlet container

---

## 📁 Project Structure

```
inventory-management/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── inventory/
│       │           ├── model/          # POJOs  (User, Product, etc.)
│       │           ├── dao/            # Data Access Objects
│       │           ├── servlet/        # HTTP Servlets
│       │           └── util/           # Utility classes
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml            # Servlet configuration
│           ├── css/                    # Stylesheets
│           │   ├── main.css
│           │   └── dashboard.css
│           ├── js/                     # JavaScript files
│           ├── login.html              # Login page
│           ├── dashboard.html          # Dashboard
│           ├── products.html           # Products management
│           ├── categories.html         # Categories management
│           ├── suppliers.html          # Suppliers management
│           └── orders.html             # Orders management
├── sql/
│   └── schema.sql                      # Database schema & sample data
└── pom.xml                             # Maven configuration
```

---

## 🚀 Quick Start Guide

### Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 11 or higher**
   ```bash
   java -version
   ```

2. **Apache Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **MySQL Server 8.0+**
   ```bash
   mysql --version
   ```

4. **Apache Tomcat 9.0+**
   - Download from: https://tomcat.apache.org/download-90.cgi
   - Extract to a directory (e.g., `C:\apache-tomcat-9.0.XX`)

5. **MySQL Workbench** (Optional but recommended)
   - For easier database management

---

### Step 1: Database Setup

1. **Start MySQL Server**
   ```bash
   # Windows (if installed as service)
   net start MySQL80
   
   # Or use MySQL Workbench
   ```

2. **Create Database and Tables**
   ```bash
   # Login to MySQL
   mysql -u root -p
   
   # Run the schema file
   source /path/to/inventory-management/sql/schema.sql
   
   # Or import via MySQL Workbench:
   # File -> Run SQL Script -> Select schema.sql
   ```

3. **Verify Database Creation**
   ```sql
   USE inventory_management;
   SHOW TABLES;
   SELECT * FROM users;  -- Should show 3 sample users
   SELECT * FROM products;  -- Should show 10 sample products
   ```

4. **Configure Database Connection**
   - Edit `src/main/java/com/inventory/dao/DatabaseConnection.java`
   - Update these values if needed:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/inventory_management";
   private static final String USER = "root";
   private static final String PASSWORD = "your_mysql_password";
   ```

---

### Step 2: Build the Project

1. **Navigate to project directory**
   ```bash
   cd "c:\Users\HP\Desktop\inventory management"
   ```

2. **Clean and build with Maven**
   ```bash
   mvn clean package
   ```
   
   This will:
   - Download all dependencies
   - Compile Java code
   - Create a WAR file in `target/inventory-management.war`

3. **Verify build success**
   - Look for `BUILD SUCCESS` message
   - Check `target/inventory-management.war` exists

---

### Step 3: Deploy to Tomcat

#### Option A: Manual Deployment

1. **Copy WAR file to Tomcat**
   ```bash
   # Copy the WAR file
   copy target\inventory-management.war C:\apache-tomcat-9.0.XX\webapps\
   ```

2. **Start Tomcat**
   ```bash
   # Navigate to Tomcat bin directory
   cd C:\apache-tomcat-9.0.XX\bin
   
   # Start Tomcat
   startup.bat  # Windows
   # or
   ./startup.sh  # Linux/Mac
   ```

3. **Verify Deployment**
   - Check `C:\apache-tomcat-9.0.XX\logs\catalina.out` for errors
   - Tomcat should auto-extract the WAR file

#### Option B: IDE Deployment (Eclipse/IntelliJ)

1. **Configure Tomcat in IDE**
   - Add Tomcat server
   - Point to Tomcat installation directory

2. **Add project to server**
   - Right-click project → Run As → Run on Server
   - Select configured Tomcat server

---

### Step 4: Access the Application

1. **Open your browser**
   ```
   http://localhost:8080/inventory-management/
   ```
   
   Or directly:
   ```
   http://localhost:8080/inventory-management/login.html
   ```

2. **Login with demo credentials:**

   | Role    | Username  | Password  |
   |---------|-----------|-----------|
   | Admin   | admin     | admin123  |
   | Manager | manager1  | admin123  |
   | Staff   | staff1    | admin123  |

3. **Explore the system!** 🎉

---

## 🔧 Configuration

### Database Configuration

Edit `DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/inventory_management?useSSL=false&serverTimezone=UTC";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

### Port Configuration

If port 8080 is already in use, change Tomcat port:

1. Edit `C:\apache-tomcat-9.0.XX\conf\server.xml`
2. Find:
   ```xml
   <Connector port="8080" protocol="HTTP/1.1" ...>
   ```
3. Change to your desired port (e.g., 8090)

---

## 📊 Database Schema

### Tables

1. **users** - System users with roles
2. **categories** - Product categories
3. **suppliers** - Supplier information
4. **products** - Product inventory
5. **stock_movements** - Stock transaction history
6. **orders** - Purchase orders
7. **order_items** - Order line items

### Relationships

```
categories (1) ─────< (N) products
suppliers (1) ──────< (N) products
users (1) ──────────< (N) orders
users (1) ──────────< (N) stock_movements
suppliers (1) ──────< (N) orders
orders (1) ─────────< (N) order_items
products (1) ───────< (N) order_items
products (1) ───────< (N) stock_movements
```

---

## 🛠️ Development

### Adding New Features

1. **Create Model** - Add POJO in `model/` package
2. **Create DAO** - Add database operations in `dao/` package
3. **Create Servlet** - Add HTTP handler in `servlet/` package
4. **Create Frontend** - Add HTML page in `webapp/`

### Code Style

- Use BCrypt for all password operations
- Use PreparedStatements to prevent SQL injection
- Validate all user inputs
- Handle exceptions gracefully
- Return JSON from servlets

---

## 🔒 Security Features

- ✅ BCrypt password hashing
- ✅ SQL injection prevention (PreparedStatements)
- ✅ Session management
- ✅ Input validation
- ✅ XSS protection
- ✅ Role-based access control

---

## 🐛 Troubleshooting

### Issue: Cannot connect to database

**Solution:**
- Verify MySQL is running
- Check database credentials in `DatabaseConnection.java`
- Ensure MySQL JDBC driver is in dependencies

### Issue: 404 - Page not found

**Solution:**
- Verify Tomcat is running
- Check deployment: `webapps/inventory-management/` should exist
- Verify context path in URL

### Issue: Servlets not responding

**Solution:**
- Check `web.xml` configuration
- Verify servlet annotations (@WebServlet)
- Check Tomcat logs for errors

### Issue: Build fails

**Solution:**
```bash
# Clean Maven cache
mvn clean

# Update dependencies
mvn dependency:purge-local-repository

# Rebuild
mvn package
```

---

## 📈 Future Enhancements

- [ ] REST API implementation
- [ ] Advanced reporting with charts
- [ ] Export to Excel/PDF
- [ ] Barcode scanning
- [ ] Email notifications
- [ ] Multi-warehouse support
- [ ] Mobile app
- [ ] Real-time updates with WebSockets

---

## 📝 API Documentation

### Products API

- **GET** `/products?action=getAll` - Get all products
- **GET** `/products?action=getById&id={id}` - Get product by ID
- **GET** `/products?action=search&keyword={keyword}` - Search products
- **GET** `/products?action=lowStock` - Get low stock products
- **GET** `/products?action=stats` - Get product statistics
- **POST** `/products` - Add new product
- **PUT** `/products?id={id}` - Update product
- **DELETE** `/products?id={id}` - Delete product

### Authentication API

- **POST** `/login` - User login
- **GET** `/logout` - User logout

---

## 👥 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

This project is open source and available under the MIT License.

---

## 📞 Support

For support, please open an issue in the repository or contact the development team.

---

## ✅ Project Checklist

- [x] Database schema created
- [x] All model classes implemented
- [x] All DAO classes implemented
- [x] All servlets implemented
- [x] Login page with authentication
- [x] Dashboard with statistics
- [x] Products management (CRUD)
- [x] Modern, responsive UI
- [x] Security features (BCrypt, SQL injection prevention)
- [x] Session management
- [x] Input validation

---

**Built with ❤️ using Java, MySQL, and modern web technologies**

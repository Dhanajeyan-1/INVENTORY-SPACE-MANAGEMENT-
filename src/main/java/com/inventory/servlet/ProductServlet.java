package com.inventory.servlet;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Product Servlet
 * Handles all product-related operations
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO;
    private Gson gson;

    @Override
    public void init() {
        productDAO = new ProductDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if ("getAll".equals(action)) {
                List<Product> products = productDAO.getAllProducts();
                response.getWriter().write(gson.toJson(products));

            } else if ("getById".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Product product = productDAO.getProductById(id);
                response.getWriter().write(gson.toJson(product));

            } else if ("search".equals(action)) {
                String keyword = request.getParameter("keyword");
                List<Product> products = productDAO.searchProducts(keyword);
                response.getWriter().write(gson.toJson(products));

            } else if ("lowStock".equals(action)) {
                List<Product> products = productDAO.getLowStockProducts();
                response.getWriter().write(gson.toJson(products));

            } else if ("byCategory".equals(action)) {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                List<Product> products = productDAO.getProductsByCategory(categoryId);
                response.getWriter().write(gson.toJson(products));

            } else if ("stats".equals(action)) {
                int totalCount = productDAO.getTotalProductCount();
                BigDecimal totalValue = productDAO.getTotalInventoryValue();
                String stats = String.format("{\"totalProducts\": %d, \"totalValue\": %.2f}",
                        totalCount, totalValue.doubleValue());
                response.getWriter().write(stats);

            } else {
                List<Product> products = productDAO.getAllProducts();
                response.getWriter().write(gson.toJson(products));
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String name = request.getParameter("name");
            String sku = request.getParameter("sku");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String description = request.getParameter("description");
            BigDecimal unitPrice = new BigDecimal(request.getParameter("unitPrice"));
            int quantityInStock = Integer.parseInt(request.getParameter("quantityInStock"));
            int reorderLevel = Integer.parseInt(request.getParameter("reorderLevel"));
            String imageUrl = request.getParameter("imageUrl");

            Product product = new Product();
            product.setName(name);
            product.setSku(sku);
            product.setCategoryId(categoryId);
            product.setSupplierId(supplierId);
            product.setDescription(description);
            product.setUnitPrice(unitPrice);
            product.setQuantityInStock(quantityInStock);
            product.setReorderLevel(reorderLevel);
            product.setImageUrl(imageUrl);

            boolean success = productDAO.addProduct(product);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Product added successfully\", \"id\": "
                        + product.getId() + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to add product\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String sku = request.getParameter("sku");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String description = request.getParameter("description");
            BigDecimal unitPrice = new BigDecimal(request.getParameter("unitPrice"));
            int quantityInStock = Integer.parseInt(request.getParameter("quantityInStock"));
            int reorderLevel = Integer.parseInt(request.getParameter("reorderLevel"));
            String imageUrl = request.getParameter("imageUrl");

            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setSku(sku);
            product.setCategoryId(categoryId);
            product.setSupplierId(supplierId);
            product.setDescription(description);
            product.setUnitPrice(unitPrice);
            product.setQuantityInStock(quantityInStock);
            product.setReorderLevel(reorderLevel);
            product.setImageUrl(imageUrl);

            boolean success = productDAO.updateProduct(product);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Product updated successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to update product\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = productDAO.deleteProduct(id);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Product deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to delete product\"}");
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}

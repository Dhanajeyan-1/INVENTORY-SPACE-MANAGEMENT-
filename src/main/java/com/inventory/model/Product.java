package com.inventory.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Product Model - Represents an inventory product
 */
public class Product {
    private int id;
    private String name;
    private String sku;
    private int categoryId;
    private int supplierId;
    private String description;
    private BigDecimal unitPrice;
    private int quantityInStock;
    private int reorderLevel;
    private String imageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joined data
    private String categoryName;
    private String supplierName;

    // Constructors
    public Product() {
    }

    public Product(String name, String sku, int categoryId, int supplierId, 
                   String description, BigDecimal unitPrice, int quantityInStock, int reorderLevel) {
        this.name = name;
        this.sku = sku;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
        this.reorderLevel = reorderLevel;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    // Utility methods
    public boolean isLowStock() {
        return quantityInStock <= reorderLevel;
    }

    public String getStockStatus() {
        if (quantityInStock == 0) {
            return "Out of Stock";
        } else if (isLowStock()) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }

    public BigDecimal getStockValue() {
        return unitPrice.multiply(new BigDecimal(quantityInStock));
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantityInStock=" + quantityInStock +
                ", stockStatus='" + getStockStatus() + '\'' +
                '}';
    }
}

package com.inventory.model;

import java.sql.Timestamp;

/**
 * StockMovement Model - Represents stock in/out transactions
 */
public class StockMovement {
    private int id;
    private int productId;
    private String movementType; // 'in', 'out', 'adjustment'
    private int quantity;
    private String referenceNumber;
    private String notes;
    private int userId;
    private Timestamp createdAt;
    
    // Additional fields for joined data
    private String productName;
    private String productSku;
    private String userName;

    // Constructors
    public StockMovement() {
    }

    public StockMovement(int productId, String movementType, int quantity, 
                        String referenceNumber, String notes, int userId) {
        this.productId = productId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.referenceNumber = referenceNumber;
        this.notes = notes;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", productId=" + productId +
                ", movementType='" + movementType + '\'' +
                ", quantity=" + quantity +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

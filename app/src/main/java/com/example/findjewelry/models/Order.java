package com.example.findjewelry.models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private String name;
    private String phone;
    private List<CartItem> items;
    private double totalAmount;
    private String city;
    private String address;
    private String notes;
    private String paymentMethod;
    private String status;
    private long timestamp;

    public Order() {}

    public Order(String orderId, String userId, String name, String phone, List<CartItem> items, double totalAmount,
                 String city, String address, String notes, String paymentMethod, String status,long timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.items = items;
        this.totalAmount = totalAmount;
        this.city = city;
        this.address = address;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

}

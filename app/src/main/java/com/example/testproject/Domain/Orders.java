package com.example.testproject.Domain;

import java.io.Serializable;
import java.util.List;
public class Orders implements Serializable {
    private String orderId1; // Mã đơn hàng
    private String userId; // ID người dùng
    private String status; // Trạng thái đơn hàng (e.g., "Processing", "Completed")
    private String orderDate; // Ngày đặt hàng
    private double totalPrice; // Tổng giá trị đơn hàng
    private String location;
    private String Username;
    private String phone;
    private List<OrderItem> items; // Danh sách các món trong đơn hàng

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Orders() {
    }

    public Orders(String orderId1, String userId, String status, String orderDate, double totalPrice, String location, String Username, String phone, List<OrderItem> items) {
        this.orderId1 = orderId1;
        this.userId = userId;
        this.status = status;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.location = location;
        this.Username = Username;
        this.items = items;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrderId1() {
        return orderId1;
    }

    public void setOrderId1(String orderId1) {
        this.orderId1 = orderId1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}


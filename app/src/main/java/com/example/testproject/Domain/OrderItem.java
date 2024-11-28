package com.example.testproject.Domain;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private int FoodId;
    private int Quantity;
    private double Price;
    private String Title;
    private String ImagePath;

    public OrderItem(int foodId, int quantity, double price, String title, String imagePath) {
        FoodId = foodId;
        Quantity = quantity;
        Price = price;
        Title = title;
        ImagePath = imagePath;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public OrderItem() {
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}

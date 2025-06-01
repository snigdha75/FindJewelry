package com.example.findjewelry.models;

public class Product {
    private String name;
    private int image;
    private String imageUrl;
    private String status;
    private double price;

    private String description;
    private int stock;
    private String id;

    public Product() {}

    public Product(String name, int image, String imageUrl,String status, double price, String description, int stock, String id) {
        this.name = name;
        this.image = image;
        this.imageUrl = imageUrl;
        this.status = status;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }


    public void setImage(int image) {
        this.image = image;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

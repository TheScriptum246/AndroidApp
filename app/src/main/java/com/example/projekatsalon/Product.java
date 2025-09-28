package com.example.projekatsalon;

public class Product {
    private int id;
    private String name;
    private String price;
    private String description;
    private String imageName;

    public Product(int id, String name, String price, String description, String imageName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return name;
    }
}
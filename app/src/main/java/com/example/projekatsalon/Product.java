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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
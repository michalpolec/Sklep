package shopProject.entity;


import lombok.Data;

import java.sql.Blob;

@Data
public class Product {

    private int productID;
    private String nameOfProduct;
    private double price;
    private String description;
    private int manufacturerID;
    private String manufacturer;
    private int categoryID;
    private String category;
    private int subcategoryID;
    private String subcategory;
    private int detailsID;
    private int colorID;
    private String color;
    private int dimensionID;
    private double width;
    private double height;
    private double length;
    private int positionID;
    private int shelf;
    private int regal;
    private int stock;
    private Blob image;


    public Product(int productID, String nameOfProduct, double price, String description,int manufacturerID, String manufacturer,int categoryID, String category, int subcategoryID, String subcategory, int detailsID, int colorID, String color, int dimensionID, double width, double height, double length,int positionID, int shelf, int regal, int stock, Blob image) {
        this.productID = productID;
        this.nameOfProduct = nameOfProduct;
        this.price = price;
        this.description = description;
        this.manufacturerID = manufacturerID;
        this.manufacturer = manufacturer;
        this.categoryID = categoryID;
        this.category = category;
        this.subcategoryID = subcategoryID;
        this.subcategory = subcategory;
        this.detailsID = detailsID;
        this.colorID = colorID;
        this.color = color;
        this.dimensionID = dimensionID;
        this.width = width;
        this.height = height;
        this.length = length;
        this.positionID = positionID;
        this.shelf = shelf;
        this.regal = regal;
        this.stock = stock;
        this.image = image;
    }

    public String getDimension(){
        return width + "cm x " + height + "cm x " + length + "cm";
    }

    public String getPosition(){
        return "Półka: " + shelf + ", Regał: " + regal;
    }


}

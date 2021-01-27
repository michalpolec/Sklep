package shopProject;

import javafx.scene.image.Image;

import java.sql.Blob;

public class Product extends Object {

    private int productID;
    private String nameOfProduct;
    private double price;
    private String description;
    private String room;
    private String category;
    private String subcategory;
    private String color;
    private String material;
    private double width;
    private double height;
    private double length;
    private int shelf;
    private int regal;
    private int stock;

    private Blob image;

    public Product(int productID, String nameOfProduct, double price, String description, String room, String category, String subcategory, String color, String material, double width, double height, double length, int shelf, int regal, int stock, Blob image) {
        this.productID = productID;
        this.nameOfProduct = nameOfProduct;
        this.price = price;
        this.description = description;
        this.room = room;
        this.category = category;
        this.subcategory = subcategory;
        this.color = color;
        this.material = material;
        this.width = width;
        this.height = height;
        this.length = length;
        this.shelf = shelf;
        this.regal = regal;
        this.stock = stock;
        this.image = image;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getNameOfProduct() {
        return nameOfProduct;
    }

    public void setNameOfProduct(String nameOfProduct) {
        this.nameOfProduct = nameOfProduct;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getShelf() {
        return shelf;
    }

    public void setShelf(int shelf) {
        this.shelf = shelf;
    }

    public int getRegal() {
        return regal;
    }

    public void setRegal(int regal) {
        this.regal = regal;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

}

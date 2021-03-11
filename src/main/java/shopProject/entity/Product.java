package shopProject.entity;


import lombok.Data;

@Data
public class Product {

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


    public Product(int productID, String nameOfProduct, double price, String description, String room, String category, String subcategory, String color, String material, double width, double height, double length, int shelf, int regal, int stock) {
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
    }


}

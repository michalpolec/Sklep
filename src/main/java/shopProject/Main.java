package shopProject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shopProject.controllers.ControllerOfMainScreen;
import shopProject.entity.Product;

import java.sql.*;

public class Main extends Application {


    ObservableList<Product> products =  FXCollections.observableArrayList();

    @Override
    public void start(Stage loginStage) throws Exception{

        getAllDataFromDB();
        loginStage.setTitle("Baza danych hurtowni");
        FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("MainScreen.fxml")));
        Parent root = loader.load();
        loginStage.setScene(new Scene(root));
       // scene.getStylesheets().add("Stylesheets/style.css");
        ControllerOfMainScreen newController = loader.getController();
        newController.getAllData(products);
        loginStage.setResizable(false);
        loginStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void getAllDataFromDB()
    {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
            Statement statement = connection.createStatement();

            String sql = "SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName, \n" +
                    "subcategory.subcategoryName, product.detailsID, color.colorName, dimension.width, dimension.height, dimension.length,\n" +
                    "positions.shelf, positions.regal, stock\n" +
                    "FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)\n" +
                    "INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)\n" +
                    "INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)\n" +
                    "INNER JOIN category ON subcategory.categoryID = category.categoryID)\n" +
                    "INNER JOIN color ON details.colorID = color.colorID)\n" +
                    "INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)\n" +
                    "INNER JOIN positions ON details.positionID = positions.positionID);";

            ResultSet resultSet = statement.executeQuery(sql);


            while(resultSet.next()) {


                Product newProduct = new Product(
                        resultSet.getInt("productID"),
                        resultSet.getString("productName"),
                        resultSet.getDouble("productPrice"),
                        resultSet.getString("productDescription"),
                        resultSet.getString("manufacturerName"),
                        resultSet.getString("categoryName"),
                        resultSet.getString("subcategoryName"),
                        resultSet.getInt("detailsID"),
                        resultSet.getString("colorName"),
                        resultSet.getDouble("width"),
                        resultSet.getDouble("height"),
                        resultSet.getDouble("length"),
                        resultSet.getInt("shelf"),
                        resultSet.getInt("regal"),
                        resultSet.getInt("stock"));

                products.add(newProduct);

            }

            statement.close();
            connection.close();
        }
        catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }


}

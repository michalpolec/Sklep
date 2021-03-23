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

            String sql = """
                    SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName,\s
                    subcategory.subcategoryName,  subcategory.categoryID, product.detailsID, product.manufacturerID, product.subcategoryID, details.colorID, details.dimensionID, details.positionID, color.colorName, dimension.width, dimension.height, dimension.length,
                    positions.shelf, positions.regal, stock
                    FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)
                    INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)
                    INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)
                    INNER JOIN category ON subcategory.categoryID = category.categoryID)
                    INNER JOIN color ON details.colorID = color.colorID)
                    INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)
                    INNER JOIN positions ON details.positionID = positions.positionID);""";

            ResultSet resultSet = statement.executeQuery(sql);


            while(resultSet.next()) {


                Product newProduct = new Product(
                        resultSet.getInt("productID"),
                        resultSet.getString("productName"),
                        resultSet.getDouble("productPrice"),
                        resultSet.getString("productDescription"),
                        resultSet.getInt("manufacturerID"),
                        resultSet.getString("manufacturerName"),
                        resultSet.getInt("categoryID"),
                        resultSet.getString("categoryName"),
                        resultSet.getInt("subcategoryID"),
                        resultSet.getString("subcategoryName"),
                        resultSet.getInt("detailsID"),
                        resultSet.getInt("colorID"),
                        resultSet.getString("colorName"),
                        resultSet.getInt("dimensionID"),
                        resultSet.getDouble("width"),
                        resultSet.getDouble("height"),
                        resultSet.getDouble("length"),
                        resultSet.getInt("positionID"),
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

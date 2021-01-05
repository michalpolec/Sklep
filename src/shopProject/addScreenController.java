package shopProject;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.sql.*;
import java.util.ArrayList;

public class addScreenController {


    ObservableList<String> rooms =  FXCollections.observableArrayList();
    ObservableList<String> categories =  FXCollections.observableArrayList();
    ObservableList<String> subcategories =  FXCollections.observableArrayList();
    ObservableList<String> colors =  FXCollections.observableArrayList();
    ObservableList<String> materials =  FXCollections.observableArrayList();


    public ComboBox roomBox;
    public Button roomButton;

    public ComboBox categoryBox;
    public Button categoryButton;

    public ComboBox subcategoryBox;
    public Button subcategoryButton;

    public ComboBox colorBox;
    public Button colorButton;

    public ComboBox materialBox;
    public Button materialButton;


   public void initialize() throws SQLException, ClassNotFoundException {

       getChosenDataFromDB("NazwaPomieszczenia", "pomieszczenie", rooms);
       getChosenDataFromDB("NazwaKategorii", "kategoria", categories);
       getChosenDataFromDB("NazwaPodkategorii", "podkategoria", subcategories);
       getChosenDataFromDB("NazwaKoloru", "kolor", colors);
       getChosenDataFromDB("NazwaMaterialu", "material", materials);

       roomBox.setItems(rooms);
       categoryBox.setItems(categories);
       subcategoryBox.setItems(subcategories);
       colorBox.setItems(colors);
       materialBox.setItems(materials);

   }

    public void getChosenDataFromDB(String nameOfColumn, String nameOfTable, ObservableList<String> litsOfData) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM sklep." + nameOfTable;
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {
            String name = resultSet.getString(nameOfColumn);
            litsOfData.add(name);
        }

        statement.close();
        connection.close();


    }



}

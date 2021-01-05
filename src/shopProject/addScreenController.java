package shopProject;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class addScreenController {


    ObservableList<String> rooms =  FXCollections.observableArrayList();
    ObservableList<String>  categories = FXCollections.observableArrayList();
    ObservableList<String>  category = FXCollections.observableArrayList();
    ObservableList<Subcategory> subcategories =  FXCollections.observableArrayList();
    ObservableList<String> colors =  FXCollections.observableArrayList();
    ObservableList<String> materials =  FXCollections.observableArrayList();


    public ComboBox roomBox;
    public Button roomButton;

    public ComboBox subcategoryBox;
    public Button subcategoryButton;

    public ComboBox categoryBox;
    public Button categoryButton;

    public ComboBox colorBox;
    public Button colorButton;

    public ComboBox materialBox;
    public Button materialButton;


   public void initialize() throws SQLException, ClassNotFoundException {


       categoryBox.setDisable(true);
       getDataToArrays();

   }

   public void chosenSubcategory() {

       category.clear();
       categoryBox.setDisable(false);
       int index = 0;
       for(Subcategory sub : subcategories){
           if(sub.getSubcategoryName().contains(subcategoryBox.getValue().toString())){
               Subcategory subcategory = subcategories.get(index);
               category.add(categories.get(subcategory.getCategoryID()-1));
           }
           else {
               index++;
           }
       }

   }

   public void addRoom() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego pomieszczenia", "IDPomieszczenia", "NazwaPomieszczenia", "pomieszczenie", rooms.size(), "Wpisz nowe pomieszczenie");

   }

   public void addCategory() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowej kategorii", "IDKategorii", "NazwaKategorii", "kategoria",  categories.size(), "Wpisz nową kategorie" );
   }

   public void addSubcategory() throws IOException, SQLException, ClassNotFoundException {

        openSubcategoryScreen();
   }

   public void addColor() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego koloru" ,"IDKoloru", "NazwaKoloru", "kolor", colors.size(), "Wpisz nowy kolor");
   }

   public void addMaterial() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego materiału" , "IDMaterialu", "NazwaMaterialu", "material", materials.size(), "Wpisz nowy materiał");
   }

   public void openElementScreen(String nameOfStage, String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTabel, int lastIDofArray, String textOfLabel) throws IOException, SQLException, ClassNotFoundException {

       Stage addElement =  new Stage();
       addElement.setTitle(nameOfStage);
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addElementScreen.fxml"));
       Parent root = loader.load();
       addElement.setScene(new Scene(root));

       addElementScreenController newController = loader.getController();
       newController.setOptionOfScreen(nameOfFirstColumn,nameOfSecondColumn, nameOfTabel, lastIDofArray, textOfLabel);

       addElement.show();
       addElement.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               try {
                   getDataToArrays();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       });


   }

   public void openSubcategoryScreen() throws IOException, SQLException, ClassNotFoundException {

       Stage addSubcategory =  new Stage();
       addSubcategory.setTitle("Dodawnie nowej podkategorii");
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addSubcategoryScreen.fxml"));
       Parent root = loader.load();
       addSubcategory.setScene(new Scene(root));

       addSubcategoryScreenController newController = loader.getController();
       newController.setOptionOfSubcategoryScreen("IDPodkategorii","NazwaPodkategorii", "IDKategorii", "podkategoria", subcategories.size(), categories, "Wybierz kategorie oraz wpisz nową podkategorie");
       newController.setCategoriesInComboBox();

       addSubcategory.show();
       addSubcategory.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               try {
                   getDataToArrays();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       });
   }
   public void getDataToArrays() throws SQLException, ClassNotFoundException {

       rooms.clear();
       categories.clear();
       subcategories.clear();
       colors.clear();
       materials.clear();

       getChosenDataFromDB("NazwaPomieszczenia", "pomieszczenie", rooms);
       getChosenDataFromDB("NazwaKategorii", "kategoria", categories);
       getSubcategoryData();
       getChosenDataFromDB("NazwaKoloru", "kolor", colors);
       getChosenDataFromDB("NazwaMaterialu", "material", materials);

       roomBox.setItems(rooms);
       categoryBox.setItems(category);
       subcategoryBox.setItems(subcategories);
       colorBox.setItems(colors);
       materialBox.setItems(materials);
   }

   public void getSubcategoryData() throws ClassNotFoundException, SQLException {

       Class.forName("com.mysql.cj.jdbc.Driver");
       Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
       Statement statement = connection.createStatement();

       String sql = "SELECT * FROM sklep.podkategoria";
       ResultSet resultSet = statement.executeQuery(sql);

       while(resultSet.next()) {
            Subcategory subcategory = new Subcategory(Integer.parseInt(resultSet.getString("IDPodkategorii")),resultSet.getString("NazwaPodkategorii"),Integer.parseInt(resultSet.getString("IDKategorii")));
            subcategories.add(subcategory);
       }

       statement.close();
       connection.close();
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

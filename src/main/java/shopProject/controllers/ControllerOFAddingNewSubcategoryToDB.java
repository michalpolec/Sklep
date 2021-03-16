package shopProject.controllers;

import shopProject.entity.restOfElements;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ControllerOFAddingNewSubcategoryToDB {

    public Button addSubcategory;
    public Label titleLabel;
    public TextField subcategoryField;
    public ComboBox categoryBox;

    String nameOfTable;
    String nameOfFirstColumn;
    String nameOfSecondColumn;

    ObservableList<restOfElements>  categories;

    // Ustawienie zmiennyhc klasy
    public void setOptionOfSubcategoryScreen(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, ObservableList<restOfElements> categories, String textOfLabel) throws ClassNotFoundException, SQLException {

        this.nameOfFirstColumn = nameOfFirstColumn;
        this.nameOfSecondColumn = nameOfSecondColumn;
        this.nameOfTable = nameOfTable;
        this.titleLabel.setText(textOfLabel);
        this.categories = categories;

    }

    // Dodanie do ComboBox'a wybranych kategorii
    public void setCategoriesInComboBox(){

        categoryBox.setItems(categories);

    }

    // Dodanie nowej podkategorii do bazy danych
    public void addToDatabase() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();



        int idOfCategory = 0;

        for(restOfElements cat : categories){
            if(cat.getName().contains(categoryBox.getValue().toString())){
                idOfCategory = cat.getID();
            }
        }

        String contentOfFiled = subcategoryField.getText();

        if (contentOfFiled.equals(("")))
        {
            Alert();
        }
        else {
            String sql = "INSERT INTO " + nameOfTable + " (" + nameOfFirstColumn + ", " + nameOfSecondColumn  +")"
                    + " VALUES ('"  + contentOfFiled + "', '" + idOfCategory + "')";
            try {
                statement.executeUpdate(sql);

            }
            catch (Exception e){
                Alert();
            }
            Stage closeLoginStage = (Stage) addSubcategory.getScene().getWindow();
            closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            closeLoginStage.close();

        }
        statement.close();
        connection.close();

    }

    // Funkcja wyświetlająca błąd
    public void Alert(){
        Alert nullData = new Alert(Alert.AlertType.ERROR);
        nullData.setTitle("Błąd podczas wpisywania");
        nullData.setHeaderText(null);
        nullData.setContentText("Nic nie wpisano");

        nullData.showAndWait();
    }
}

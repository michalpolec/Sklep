package shopProject;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class addElementScreenController {

    public Button addButton;
    public Label labelOfElement;
    public TextField typeElementField;

    String nameOfTable;
    String nameOfFirstColumn;
    String nameOfSecondColumn;

    int lastIDofArray;


    public void setOptionOfScreen(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, int lastIDofArray, String textOfLabel) throws ClassNotFoundException, SQLException {

        this.nameOfFirstColumn = nameOfFirstColumn;
        this.nameOfSecondColumn = nameOfSecondColumn;
        this.nameOfTable = nameOfTable;
        this.lastIDofArray = lastIDofArray;
        this.labelOfElement.setText(textOfLabel);

    }


    public void addToDatabase() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();

        String contentOfFiled = typeElementField.getText();

        if (contentOfFiled.equals(("")))
        {
            Alert();
        }
        else {
            String sql = "INSERT INTO " + nameOfTable + " (" + nameOfFirstColumn + ", " + nameOfSecondColumn + ")"
                    + " VALUES ('" + (lastIDofArray+1)+ "', '" + contentOfFiled + "')";
           try {
                statement.executeUpdate(sql);

           }
           catch (Exception e){
               Alert();
            }
            Stage closeLoginStage = (Stage) addButton.getScene().getWindow();
            closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            closeLoginStage.close();

        }
        statement.close();
        connection.close();

    }


    public void Alert(){
        Alert nullData = new Alert(Alert.AlertType.ERROR);
        nullData.setTitle("Błąd podczas wpisywania");
        nullData.setHeaderText(null);
        nullData.setContentText("Nic nie wpisano");

        nullData.showAndWait();
    }






}

package shopProject.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ControllerOfAddingNewDimensionToDB {


    public Label titleOfScreen;
    public TextField widthField;
    public TextField heightField;
    public TextField lenghtField;
    public Button addButtom;


    public void initialize(){ //Inicjalizacja klasy

        OnlyNumbersInTextField(widthField);
        OnlyNumbersInTextField(heightField);
        OnlyNumbersInTextField(lenghtField);

    }

    // Ustawienie zmiennych
    public void setOptionOfDimensionScreen(String textOfLabel) {

        this.titleOfScreen.setText(textOfLabel);

    }

    //Dodawanie nowych wymiarów do bazy danych
    public void addToDatabase() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String width = widthField.getText();
        String height = heightField.getText();
        String lenght = lenghtField.getText();

        if (width.equals("") || height.equals("") || lenght.equals("") || width.equals(" ") || lenght.equals(" ") || height.equals(" ") )
        {
            Alert();
        }
        else {
            String sql = "INSERT INTO dimension (width, height, length)"
                    + " VALUES ('" + Double.parseDouble(width) + "', '" + Double.parseDouble(height)  + "', '" + Double.parseDouble(lenght) +"')";
            try {
                statement.executeUpdate(sql);

            }
            catch (Exception e){
                System.out.println(e);
                Alert();
            }
            Stage closeLoginStage = (Stage) addButtom.getScene().getWindow();
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

    private void OnlyNumbersInTextField(TextField textfield){

        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}([\\.]\\d{0,2})?")) {
                textfield.setText(oldValue);
            }
        });
    }

}

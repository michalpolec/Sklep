package shopProject.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ControllerOfAddingNewPositionToDB {


    public Label titleOfScreen;
    public TextField shelfField;
    public TextField regalField;
    public Button addPosition;

    String nameOfTable;
    String nameOfFirstColumn;
    String nameOfSecondColumn;

    //Inicjalizacja klasy
    public void initialize(){

        OnlyNumbersInTextField(shelfField);
        OnlyNumbersInTextField(regalField);

    }

    //Ustawienie zmiennych klasy
    public void setOptionOfPositionScreen(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable,  String textOfLabel) throws ClassNotFoundException, SQLException {

        this.nameOfFirstColumn = nameOfFirstColumn;
        this.nameOfSecondColumn = nameOfSecondColumn;
        this.nameOfTable = nameOfTable;

    }

    //Dodanie nowej pozycji do bazy danych
    public void addToDatabase() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String shelf = shelfField.getText();
        String regal = regalField.getText();

        if (shelf.equals("") || regal.equals(""))
        {
            Alert();
        }
        else {
            String sql = "INSERT INTO " + nameOfTable + " (" + nameOfFirstColumn + ", " + nameOfSecondColumn + ")"
                    + " VALUES ('" + Integer.parseInt(shelf) + "', '" + Integer.parseInt(regal)  + "')";
            try {
                statement.executeUpdate(sql);

            }
            catch (Exception e){
                Alert();
            }
            Stage closeLoginStage = (Stage) addPosition.getScene().getWindow();
            closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            closeLoginStage.close();

        }
        statement.close();
        connection.close();

    }

    //Funkcja wyświetlająca błąd
    public void Alert(){
        Alert nullData = new Alert(Alert.AlertType.ERROR);
        nullData.setTitle("Błąd podczas wpisywania");
        nullData.setHeaderText(null);
        nullData.setContentText("Nic nie wpisano");

        nullData.showAndWait();
    }

    // Funkcja ustawiające pole TextField aby przyjmował wartości int
    public void OnlyNumbersInTextField(TextField textfield){

        textfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textfield.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

}

package shopProject;

import javafx.collections.ObservableList;
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

public class addPositionScreenController {


    public Label titleOfScreen;
    public TextField shelfField;
    public TextField regalField;
    public Button addPosition;


    String nameOfTable;
    String nameOfFirstColumn;
    String nameOfSecondColumn;



    public void setOptionOfPositionScreen(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable,  String textOfLabel) throws ClassNotFoundException, SQLException {

        this.nameOfFirstColumn = nameOfFirstColumn;
        this.nameOfSecondColumn = nameOfSecondColumn;
        this.nameOfTable = nameOfTable;

    }

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

    public void Alert(){
        Alert nullData = new Alert(Alert.AlertType.ERROR);
        nullData.setTitle("Błąd podczas wpisywania");
        nullData.setHeaderText(null);
        nullData.setContentText("Nic nie wpisano");

        nullData.showAndWait();
    }

}

package shopProject;

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

public class addDimensionScreenController {


    public Label titleOfScreen;
    public TextField widthField;
    public TextField heightField;
    public TextField lenghtField;
    public Button addButtom;

    String nameOfTable;
    String nameOfFirstColumn;
    String nameOfSecondColumn;
    String nameOfThirdColumn;

    public void setOptionOfDimensionScreen(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfThirdColumn, String nameOfTable,  String textOfLabel) throws ClassNotFoundException, SQLException {

        this.nameOfFirstColumn = nameOfFirstColumn;
        this.nameOfSecondColumn = nameOfSecondColumn;
        this.nameOfThirdColumn = nameOfThirdColumn;
        this.nameOfTable = nameOfTable;
        this.titleOfScreen.setText(textOfLabel);

    }


    public void addToDatabase() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String width = widthField.getText();
        String height = heightField.getText();
        String lenght = lenghtField.getText();

        if (widthField.equals("") || heightField.equals("")  || lenghtField.equals("")  )
        {
            Alert();
        }
        else {
            String sql = "INSERT INTO " + nameOfTable + " (" + nameOfFirstColumn + ", " + nameOfSecondColumn +  ", " + nameOfThirdColumn + ")"
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

}

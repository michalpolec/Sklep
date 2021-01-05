package shopProject;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class addElementScreenController {

    public Button addButton;
    public Label labelOfElement;
    public TextField typeElementField;

    String nameOfTable;
    String nameOfFirstColumn;
    String nameOfSecondColumn;

    int lastIDofArray;


    public void setOptionOfScreen( String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, int lastIDofArray, String textOfLabel) throws ClassNotFoundException, SQLException {

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

        String sql = "INSERT INTO " + nameOfTable + " (" + nameOfFirstColumn + ", " + nameOfSecondColumn + ")"
                + " VALUES ('" + (lastIDofArray+1) + "', '" + typeElementField.getText() + "')";

        statement.executeUpdate(sql);

        statement.close();
        connection.close();

        Stage closeLoginStage = (Stage) typeElementField.getScene().getWindow();
        closeLoginStage.close();


    }









}

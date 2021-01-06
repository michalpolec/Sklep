package shopProject;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class addSubcategoryScreenController {

    public Button addSubcategory;
    public Label titleLabel;
    public TextField subcategoryField;
    public ComboBox categoryBox;

    String nameOfTable;
    String nameOfFirstColumn;
    String nameOfSecondColumn;
    String nameOfThirdColumn;

    int lastIDofArray;

    ObservableList<twoColumnsTable>  categories;


    public void setOptionOfSubcategoryScreen(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfThirdColumn, String nameOfTable, int lastIDofArray, ObservableList<twoColumnsTable> categories, String textOfLabel) throws ClassNotFoundException, SQLException {

        this.nameOfFirstColumn = nameOfFirstColumn;
        this.nameOfSecondColumn = nameOfSecondColumn;
        this.nameOfThirdColumn = nameOfThirdColumn;
        this.nameOfTable = nameOfTable;
        this.lastIDofArray = lastIDofArray;
        this.titleLabel.setText(textOfLabel);
        this.categories = categories;

    }
    public void setCategoriesInComboBox(){

        categoryBox.setItems(categories);

    }

    public void addToDatabase() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();



        int idOfCategory = 0;

        for(twoColumnsTable cat : categories){
            if(cat.getName().contains(categoryBox.getValue().toString())){
                idOfCategory = cat.ID;
            }
        }

        String contentOfFiled = subcategoryField.getText();

        if (contentOfFiled.equals(("")))
        {
            Alert();
        }
        else {
            String sql = "INSERT INTO " + nameOfTable + " (" + nameOfFirstColumn + ", " + nameOfSecondColumn +  ", " + nameOfThirdColumn +")"
                    + " VALUES ('" + (lastIDofArray + 1) + "', '" + contentOfFiled + "', '" + (idOfCategory+1) + "')";
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

    public void Alert(){
        Alert nullData = new Alert(Alert.AlertType.ERROR);
        nullData.setTitle("Błąd podczas wpisywania");
        nullData.setHeaderText(null);
        nullData.setContentText("Nic nie wpisano");

        nullData.showAndWait();
    }
}

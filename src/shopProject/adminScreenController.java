package shopProject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class adminScreenController {

    public Button addNewElement;
    public Button editDatabase;


    public void addNewElementAction() throws IOException {

        Stage addNewElementStage = new Stage();
        addNewElementStage.setTitle("Dodawanie nowego elementu");
        addNewElementStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("resources/addScreen.fxml"))));
        addNewElementStage.show();

    }

    public void editDatabaseAction() throws IOException {

        Stage editDatabaseStage = new Stage();
        editDatabaseStage.setTitle("Edycja bazy danych");
        editDatabaseStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("resources/editScreen.fxml"))));
        editDatabaseStage.show();

    }
}

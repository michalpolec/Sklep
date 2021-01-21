package shopProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage loginStage) throws Exception{

        loginStage.setTitle("Baza danych sklepu");
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("resources/mainScreen.fxml")));
        scene.getStylesheets().add("Stylesheets/style.css");
        loginStage.setScene(scene);
        loginStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

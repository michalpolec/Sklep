package shopProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class Main extends Application {



    @Override
    public void start(Stage loginStage) throws Exception{


        loginStage.setTitle("Baza danych sklepu");
        loginStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("resources/mainScreen.fxml"))));
        loginStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }





}

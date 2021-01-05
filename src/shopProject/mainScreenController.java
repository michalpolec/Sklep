package shopProject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class mainScreenController {


    public Button loginAsAdmin;
    public Button loginAsUser;
    public TextField loginField;
    public PasswordField passwordField;

    @FXML
    private void adminLoginButtonPressed() throws IOException {
        if(checkCorrectionOfLoginAndPassword(loginField.getText(), passwordField.getText())) {

            Stage adminStage = new Stage();
            adminStage.setTitle("Administracja bazy danych");
            adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("resources/adminScreen.fxml"))));
            adminStage.show();

            Stage closeLoginStage = (Stage) loginAsAdmin.getScene().getWindow();
            closeLoginStage.close();

        }
        else {
            Alert wrongLoginData = new Alert(Alert.AlertType.ERROR);
            wrongLoginData.setTitle("Błąd podczas logowania");
            wrongLoginData.setHeaderText(null);
            wrongLoginData.setContentText("Wpisano niepoprawny login lub hasło.");

            wrongLoginData.showAndWait();
        }
        loginField.setText("");
        passwordField.setText("");
    }

    @FXML
    private void userLoginButtonPressed() throws IOException {
        Stage userStage = new Stage();
        userStage.setTitle("Sklep meblowy!");
        userStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("resources/userScreen.fxml"))));
        userStage.show();

        Stage closeLoginStage = (Stage) loginAsUser.getScene().getWindow();
        closeLoginStage.close();
    }


    boolean checkCorrectionOfLoginAndPassword(String typedLogin, String typedPassword)
    {
        String correctLogin = "admin";
        String correctPassword = "admin";

        if (correctLogin.equals(typedLogin) && correctPassword.equals(typedPassword)){
            return true;
        }
        else {
            return false;
        }

    }

}

package shopProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

public class mainScreenController {


    public Button loginAsAdmin;
    public Button loginAsUser;
    public TextField loginField;
    public PasswordField passwordField;
    ObservableList<Product> products =  FXCollections.observableArrayList();

    public void initialize(){

        getAllDataFromDB();
    }

    @FXML
    private void adminLoginButtonPressed() throws IOException {
        if(checkCorrectionOfLoginAndPassword(loginField.getText(), passwordField.getText())) {

            Stage editDatabaseStage = new Stage();
            editDatabaseStage.setTitle("Administracja");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/adminScreen.fxml"));
            Parent root = loader.load();
            root.getStylesheets().add("Stylesheets/style.css");
            editDatabaseStage.setScene(new Scene(root));

            adminScreenController newController = loader.getController();
            newController.getAllData(products);
            editDatabaseStage.show();

        }
        else {
            Alert("Błąd podczas logowania", "Wpisano niepoprawny login lub hasło.");
        }
        loginField.setText("");
        passwordField.setText("");
    }

    @FXML
    private void userLoginButtonPressed() throws IOException {
        Stage userStage = new Stage();
        userStage.setTitle("Sklep meblowy!");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/userScreen.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add("Stylesheets/style.css");
        userStage.setScene(new Scene(root));

        userScreenController userScreenController = new userScreenController();
        userScreenController.getAllData(products);
        userStage.show();

        Stage closeLoginStage = (Stage) loginAsUser.getScene().getWindow();
        closeLoginStage.close();
    }

    public void getAllDataFromDB()
    {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
            Statement statement = connection.createStatement();

            String sql = "SELECT produkty.IDProduktu, NazwaProduktu, CenaProduktu, OpisProduktu, pomieszczenie.NazwaPomieszczenia, kategoria.NazwaKategorii, \n" +
                    "podkategoria.NazwaPodkategorii, kolor.NazwaKoloru, material.NazwaMaterialu, wymiary.Szerokosc, wymiary.Wysokosc, wymiary.Dlugosc,\n" +
                    " pozycja.Polka, pozycja.Regal, StanMagazynowy, Zdjecie\n" +
                    "FROM ((((((((produkty INNER JOIN szczegoly ON produkty.IDProduktu = szczegoly.IDProduktu)\n" +
                    "INNER JOIN pomieszczenie ON produkty.IDPomieszczenia = pomieszczenie.IDPomieszczenia)\n" +
                    "INNER JOIN podkategoria ON produkty.IDPodkategorii = podkategoria.IDPodkategorii)\n" +
                    "INNER JOIN kategoria ON podkategoria.IDKategorii = kategoria.IDKategorii)\n" +
                    "INNER JOIN kolor ON szczegoly.IDKoloru = kolor.IDKoloru)\n" +
                    "INNER JOIN material ON szczegoly.IDMaterialu = material.IDMaterialu)\n" +
                    "INNER JOIN wymiary ON szczegoly.IDWymiarow = wymiary.IDWymiarow)\n" +
                    "INNER JOIN pozycja ON szczegoly.IDPozycji = pozycja.IDPozycji);";

            ResultSet resultSet = statement.executeQuery(sql);


            while(resultSet.next()) {


                Product newProduct = new Product(
                        resultSet.getInt("IDProduktu"),
                        resultSet.getString("NazwaProduktu"),
                        resultSet.getDouble("CenaProduktu"),
                        resultSet.getString("OpisProduktu"),
                        resultSet.getString("NazwaPomieszczenia"),
                        resultSet.getString("NazwaKategorii"),
                        resultSet.getString("NazwaPodkategorii"),
                        resultSet.getString("NazwaKoloru"),
                        resultSet.getString("NazwaMaterialu"),
                        resultSet.getDouble("Szerokosc"),
                        resultSet.getDouble("Wysokosc"),
                        resultSet.getDouble("Dlugosc"),
                        resultSet.getInt("Polka"),
                        resultSet.getInt("Regal"),
                        resultSet.getInt("StanMagazynowy"),
                        resultSet.getBlob("Zdjecie"));

                products.add(newProduct);

            }

            statement.close();
            connection.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }



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

    void Alert(String setTitle, String setContents) {
        Alert badClick = new Alert(Alert.AlertType.ERROR);
        badClick.setTitle(setTitle);
        badClick.setHeaderText(null);
        badClick.setContentText(setContents);

        badClick.showAndWait();
    }

}

package shopProject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shopProject.controllers.ControllerOfMainScreen;
import shopProject.entity.Product;

import java.sql.*;

public class Main extends Application {


    ObservableList<Product> products =  FXCollections.observableArrayList();

    @Override
    public void start(Stage loginStage) throws Exception{

        getAllDataFromDB();
        loginStage.setTitle("Baza danych sklepu");
        FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("MainScreen.fxml")));
        Parent root = loader.load();
        loginStage.setScene(new Scene(root));
       // scene.getStylesheets().add("Stylesheets/style.css");
        ControllerOfMainScreen newController = loader.getController();
        newController.getAllData(products);
        loginStage.setResizable(false);
        loginStage.show();

    }

    public static void main(String[] args) {
        launch(args);
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
                        resultSet.getInt("StanMagazynowy"));

                products.add(newProduct);

            }

            statement.close();
            connection.close();
        }
        catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }


}

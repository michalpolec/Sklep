package shopProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class adminScreenController {

    public Button addNewElement;
    public Button editDatabase;

    ObservableList<Product> products =  FXCollections.observableArrayList();

    public void initialize(){

        getAllDataFromDB();
    }

    public void addNewElementAction() throws IOException {


        Stage addNewElementStage = new Stage();
        addNewElementStage.setTitle("Dodawanie nowego elementu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addScreen.fxml"));
        Parent root = loader.load();
        addNewElementStage.setScene(new Scene(root));
        addNewElementStage.show();

    }

    public void editDatabaseAction() throws IOException {

        Stage editDatabaseStage = new Stage();
        editDatabaseStage.setTitle("Edycja bazy danych");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/editScreen.fxml"));
        Parent root = loader.load();
        editDatabaseStage.setScene(new Scene(root));

        editScreenController newController = loader.getController();
        newController.getAllData(products);
        editDatabaseStage.show();


    }


    public void getAllDataFromDB()
    {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
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

                Product newProduct = new Product(Integer.parseInt(resultSet.getString("IDProduktu")),
                        resultSet.getString("NazwaProduktu"),
                        Double.parseDouble(resultSet.getString("CenaProduktu")),
                        resultSet.getString("OpisProduktu"),
                        resultSet.getString("NazwaPomieszczenia"),
                        resultSet.getString("NazwaKategorii"),
                        resultSet.getString("NazwaPodkategorii"),
                        resultSet.getString("NazwaKoloru"),
                        resultSet.getString("NazwaMaterialu"),
                        Double.parseDouble(resultSet.getString("Szerokosc")),
                        Double.parseDouble(resultSet.getString("Wysokosc")),
                        Double.parseDouble(resultSet.getString("Dlugosc")),
                        Integer.parseInt(resultSet.getString("Polka")),
                        Integer.parseInt(resultSet.getString("Regal")),
                        Integer.parseInt(resultSet.getString("StanMagazynowy")));

                products.add(newProduct);


            }

            statement.close();
            connection.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }



    }
}

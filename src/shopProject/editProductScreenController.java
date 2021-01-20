package shopProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.*;

public class editProductScreenController {
    private Product selectedProduct;

    ObservableList<restOfElements> room =  FXCollections.observableArrayList();
    ObservableList<restOfElements>  categories = FXCollections.observableArrayList();
    ObservableList<restOfElements>  category = FXCollections.observableArrayList();
    ObservableList<Subcategory> subcategories =  FXCollections.observableArrayList();
    ObservableList<restOfElements> colors =  FXCollections.observableArrayList();
    ObservableList<restOfElements> materials =  FXCollections.observableArrayList();
    ObservableList<Dimension> dimensions = FXCollections.observableArrayList();
    ObservableList<Position> positions = FXCollections.observableArrayList();

    public TextField IDTF;
    public TextField NameTF;
    public TextField PriceTF;
    public TextField DescriptionTF;
    public TextField StockTF;
    public Button OKbutton;
    public Button AnnulujButton;

    public ComboBox roomCB;
    public ComboBox categoryCB;
    public ComboBox subcategoryCB;
    public ComboBox colorCB;
    public ComboBox materialCB;
    public ComboBox dimensionCB;
    public ComboBox positionCB;

    public void initialize() throws SQLException, ClassNotFoundException {
        if(selectedProduct == null){
            System.out.println("brak danych o produkcie");
        } else {
            initializeData();
        }
    }

    public void initializeData() throws SQLException, ClassNotFoundException {
        categoryCB.setDisable(true);
        IDTF.setText(String.valueOf(selectedProduct.getProductID()));
        NameTF.setText(selectedProduct.getNameOfProduct());
        PriceTF.setText(String.valueOf(selectedProduct.getPrice()));
        DescriptionTF.setText(selectedProduct.getDescription());
        StockTF.setText(String.valueOf(selectedProduct.getStock()));
        getDataToArrays();
    }

    public void getSelectedProduct(Product product) throws SQLException, ClassNotFoundException {
        this.selectedProduct = product;
        initializeData(); //to musi byÄ‡!!!!
    }

    public void chosenSubcategory() {

        category.clear();
        categoryCB.setDisable(false);

        for(Subcategory sub : subcategories){
            if(sub.getSubcategoryName().equals(subcategoryCB.getValue().toString())){

                category.add(categories.get(sub.getCategoryID()-1));
            }
        }

    }


    public void onAnnulujButtonPressed(ActionEvent actionEvent) {
        //nothing to do
        Stage stage = (Stage) AnnulujButton.getScene().getWindow();
        stage.close();
    }

    public void onOKButtonPressed(ActionEvent actionEvent) throws IOException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "admin");
        Statement statement = connection.createStatement();


        /*String sql = "INSERT INTO " + nameOfTable + " (" + nameOfFirstColumn + ", " + nameOfSecondColumn + ")"
                + " VALUES ('" + Integer.parseInt(shelf) + "', '" + Integer.parseInt(regal)  + "')";
        try {
            statement.executeUpdate(sql);

        }
        catch (Exception e){
            Alert();
        }*/

        Stage stage = (Stage) OKbutton.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();

        statement.close();
        connection.close();

    }

    public void getDataToArrays() throws SQLException, ClassNotFoundException {

        room.clear();
        categories.clear();
        subcategories.clear();
        colors.clear();
        materials.clear();
        dimensions.clear();
        positions.clear();

        getChosenDataFromDB("IDPomieszczenia", "NazwaPomieszczenia", "pomieszczenie", room);
        getChosenDataFromDB("IDKategorii","NazwaKategorii", "kategoria", categories);
        getSubcategoryData();
        getChosenDataFromDB("IDKoloru", "NazwaKoloru", "kolor", colors);
        getChosenDataFromDB("IDMaterialu","NazwaMaterialu", "material", materials);
        getDimensionData();
        getPositionData();

        roomCB.setItems(room);
        categoryCB.setItems(category);
        subcategoryCB.setItems(subcategories);
        colorCB.setItems(colors);
        materialCB.setItems(materials);
        dimensionCB.setItems(dimensions);
        positionCB.setItems(positions);

    }

    public void getSubcategoryData() throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM sklep.podkategoria";
        ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);

        while(resultSet.next()) {
            Subcategory subcategory = new Subcategory(Integer.parseInt(resultSet.getString("IDPodkategorii")),resultSet.getString("NazwaPodkategorii"),Integer.parseInt(resultSet.getString("IDKategorii")));
            subcategories.add(subcategory);
        }

        closeStatementAndConnection(resultSet.getStatement());
    }

    public void getChosenDataFromDB(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, ObservableList<restOfElements> litsOfData) throws ClassNotFoundException, SQLException {


        String sql = "SELECT * FROM sklep." + nameOfTable;
        ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);

        while(resultSet.next()) {
            restOfElements table = new restOfElements(Integer.parseInt(resultSet.getString(nameOfFirstColumn)),
                    resultSet.getString(nameOfSecondColumn));
            litsOfData.add(table);
        }

        closeStatementAndConnection(resultSet.getStatement());

    }

    public void getDimensionData() throws ClassNotFoundException, SQLException {


        String sql = "SELECT * FROM sklep.wymiary";
        ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);


        while(resultSet.next()) {

            Dimension dimension = new Dimension(Integer.parseInt(resultSet.getString("IDWymiarow")), (int) Double.parseDouble(resultSet.getString("Szerokosc")), (int) Double.parseDouble(resultSet.getString("Wysokosc")), (int) Double.parseDouble(resultSet.getString("Dlugosc")));
            dimensions.add(dimension);
        }

        closeStatementAndConnection(resultSet.getStatement());
    }

    public void getPositionData() throws ClassNotFoundException, SQLException {



        String sql = "SELECT * FROM sklep.pozycja";
        ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);

        while(resultSet.next()) {

            Position position = new Position(Integer.parseInt(resultSet.getString("IDPozycji")), Integer.parseInt(resultSet.getString("Polka")), Integer.parseInt(resultSet.getString("Regal")));
            positions.add(position);
        }

        closeStatementAndConnection(resultSet.getStatement());

    }

    public void Alert(){
        Alert nullData = new Alert(Alert.AlertType.ERROR);
        nullData.setTitle("BĹ‚Ä…d podczas wpisywania");
        nullData.setHeaderText(null);
        nullData.setContentText("Nic nie wpisano");

        nullData.showAndWait();
    }

    public void Info(){
        javafx.scene.control.Alert nullData = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        nullData.setTitle("Wpisano nowy produkt do bazy");
        nullData.setHeaderText(null);
        nullData.setContentText("Dodano nowy produkt!");

        nullData.showAndWait();
    }

    public Statement createConnectionAndStatement() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();

        return statement;
    }

    public void closeStatementAndConnection(Statement statement) throws SQLException {

        Connection connection = statement.getConnection();
        statement.close();
        connection.close();

    }
}

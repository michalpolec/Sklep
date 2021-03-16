package shopProject.controllers;

import shopProject.entity.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;


public class ControllerOfMainScreen {

    private ObservableList<Product> products =  FXCollections.observableArrayList();
    public Button editionButton;
    public TableView<Product> tableOfDB;
    public TableColumn<Product, Integer> IDproduct;
    public TableColumn<Product, String> NameProduct;
    public TableColumn<Product, Double> PriceProduct;
    public TableColumn<Product, String> Descrpition;
    public TableColumn<Product, String> room;
    public TableColumn<Product, String> Category;
    public TableColumn<Product, String> Subcategory;
    public TableColumn<Product, String> color;
    public TableColumn<Product, String> material;
    public TableColumn<Product, Double> width;
    public TableColumn<Product, Double> height;
    public TableColumn<Product, Double> length;
    public TableColumn<Product, Integer> shelf;
    public TableColumn<Product, Integer> regal;
    public TableColumn<Product, Integer> stock;

    private Product selectedProduct;

    public void initialize() {
        fillCellsWithData();
        initializeTable();
    }

    private void fillCellsWithData() {

        IDproduct.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productID"));
        NameProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("nameOfProduct"));
        PriceProduct.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        Descrpition.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        room.setCellValueFactory(new PropertyValueFactory<Product, String>("room"));
        Category.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
        Subcategory.setCellValueFactory(new PropertyValueFactory<Product, String>("subcategory"));
        color.setCellValueFactory(new PropertyValueFactory<Product, String>("color"));
        material.setCellValueFactory(new PropertyValueFactory<Product, String>("material"));
        width.setCellValueFactory(new PropertyValueFactory<Product, Double>("width"));
        height.setCellValueFactory(new PropertyValueFactory<Product, Double>("height"));
        length.setCellValueFactory(new PropertyValueFactory<Product, Double>("length"));
        shelf.setCellValueFactory(new PropertyValueFactory<Product, Integer>("shelf"));
        regal.setCellValueFactory(new PropertyValueFactory<Product, Integer>("regal"));
        stock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
    }

    public void getAllData(ObservableList<Product> products) {
        this.products.setAll(products);
    }

    void initializeTable() {
        tableOfDB.setItems(products);
    }

    @FXML
    private void onAddButtonPressed() throws IOException, SQLException, ClassNotFoundException {

       FXMLLoader loader = getFxmlLoader("ModifyProductScreen.fxml");
       Parent root = getRoot(loader);
       ControllerOfModifyScreen newController = usingMethodsOfModifyScreen("Dodawanie nowego elementu",loader, true);
        Stage newModifyStage = CreateNewModifyStage();
        customizeStage("Dodawanie nowego elementu", root, newModifyStage);
       newModifyStage.setOnCloseRequest(we -> {
            products.add(newController.getSelectedProduct());
            tableOfDB.refresh();
        });
    }

    @FXML
    private void onEditionButtonPressed() throws IOException, SQLException, ClassNotFoundException {
        if(checkIfItemIsSelected()) {
            FXMLLoader loader = getFxmlLoader("ModifyProductScreen.fxml");
            Parent root = getRoot(loader);
            ControllerOfModifyScreen newController = usingMethodsOfModifyScreen("Edycja elementu o numerze: ", loader, false);
            Stage newModifyStage = CreateNewModifyStage();
            customizeStage("Edycja istniejącego elementu", root, newModifyStage);
            newModifyStage.setOnCloseRequest(we -> {
                changeProductInTable(newController.getSelectedProduct());
                tableOfDB.refresh();
            });
        }
    }

    private Stage CreateNewModifyStage() throws IOException {
        Stage newModifyStage = new Stage();
        return newModifyStage;

    }

    private void customizeStage(String title, Parent root, Stage newModifyStage) {
        newModifyStage.setTitle(title);
        //root.getStylesheets().add("Stylesheets/style.css");
        newModifyStage.setScene(new Scene(root));
        newModifyStage.setResizable(false);
        newModifyStage.show();
    }

    private Parent getRoot(FXMLLoader loader) throws IOException {
        Parent root = loader.load();
        return root;
    }

    private FXMLLoader getFxmlLoader(String resource) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource));
        return loader;
    }

    private ControllerOfModifyScreen usingMethodsOfModifyScreen(String label, FXMLLoader loader, Boolean AddOrEdit) throws SQLException, ClassNotFoundException, IOException {

        ControllerOfModifyScreen newController = loader.getController();
        if(AddOrEdit) {
            newController.setAddOrEdit(true);
            newController.setLabelINFO(label);
        }
        else {
            newController.setAddOrEdit(false);
            newController.setLabelINFO(label + selectedProduct.getProductID());
            newController.setSelectedProduct(selectedProduct);
            newController.initializeData();
            newController.addProductToDB.setText("Edytuj");
        }
        return newController;
    }

    void changeProductInTable(Product product){
        int IDproduktu = product.getProductID();

        for(int i = 0; i < products.size(); i++)
        {
            if(IDproduktu == products.get(i).getProductID()){
                products.get(i).setNameOfProduct(product.getNameOfProduct());
                products.get(i).setPrice(product.getPrice());
                products.get(i).setDescription(product.getDescription());
                products.get(i).setManufacturer(product.getManufacturer());
                products.get(i).setCategory(product.getCategory());
                products.get(i).setSubcategory(product.getSubcategory());
                products.get(i).setColor(product.getColor());
                products.get(i).setWidth(product.getWidth());
                products.get(i).setHeight(product.getHeight());
                products.get(i).setLength(product.getLength());
                products.get(i).setShelf(product.getShelf());
                products.get(i).setRegal(product.getRegal());
                products.get(i).setStock(product.getStock());
                break;
            }
        }
    }

    @FXML
    private void onDeleteButtonPressed() throws SQLException, ClassNotFoundException, IOException {
        if(checkIfItemIsSelected()){
            Alert deleteClick = Alert( "Usuwanie elementu", "Czy na pewno usunąć element?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> action = deleteClick.showAndWait();
            if(action.get() == ButtonType.OK) {

                deleteElementFromDatabase();
                deleteElementFromList();
                tableOfDB.refresh();
            }
        }
    }

    private void deleteElementFromList() {
        for (Product product : products) {
            if (product.getProductID() == selectedProduct.getProductID()) {
                products.remove(product);
                break;
            }
        }
    }

    private void deleteElementFromDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();
        String sql = "DELETE FROM `sklep`.`produkty` WHERE (`IDProduktu` = '" + selectedProduct.getProductID() + "')";
        statement.executeUpdate(sql);
        sql = "DELETE FROM `sklep`.`szczegoly` WHERE (`IDProduktu` = '" + selectedProduct.getProductID() + "')";
        statement.executeUpdate(sql);
        statement.close();
        connection.close();
    }

    private boolean checkIfItemIsSelected() throws IOException, SQLException, ClassNotFoundException {
        selectedProduct = tableOfDB.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert errorAlert = Alert("Błąd","Błąd Należy zaznaczyć odpowiedni wiersz do edycji." , Alert.AlertType.ERROR);
            errorAlert.showAndWait();
            return false;
        }
        else{
            return true;
        }
    }

    private Alert Alert(String setTitle, String setContents, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(setTitle);
        alert.setHeaderText(null);
        alert.setContentText(setContents);

        return alert;
    }
}

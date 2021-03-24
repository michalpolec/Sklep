package shopProject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import shopProject.entity.Product;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;


public class ControllerOfMainScreen {

    public Button addButton;
    public Button editionButton;
    public Button deleteButton;
    public TextField researchField;
    public Button researchButton;
    public Button clearButton;
    private final ObservableList<Product> allproducts =  FXCollections.observableArrayList();
    private ObservableList<Product> currentproducts = FXCollections.observableArrayList();

    public TableView<Product> tableOfDB;
    public TableColumn<Product, Integer> IDproduct;
    public TableColumn<Product, String> NameProduct;
    public TableColumn<Product, Double> PriceProduct;
    public TableColumn<Product, String> Descrpition;
    public TableColumn<Product, String> manufacturer;
    public TableColumn<Product, String> Category;
    public TableColumn<Product, String> Subcategory;
    public TableColumn<Product, String> color;
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
        manufacturer.setCellValueFactory(new PropertyValueFactory<Product, String>("manufacturer"));
        Category.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
        Subcategory.setCellValueFactory(new PropertyValueFactory<Product, String>("subcategory"));
        color.setCellValueFactory(new PropertyValueFactory<Product, String>("color"));
        width.setCellValueFactory(new PropertyValueFactory<Product, Double>("width"));
        height.setCellValueFactory(new PropertyValueFactory<Product, Double>("height"));
        length.setCellValueFactory(new PropertyValueFactory<Product, Double>("length"));
        shelf.setCellValueFactory(new PropertyValueFactory<Product, Integer>("shelf"));
        regal.setCellValueFactory(new PropertyValueFactory<Product, Integer>("regal"));
        stock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
    }

    public void getAllData(ObservableList<Product> products) {
        this.allproducts.setAll(products);
    }

    void initializeTable() {
        tableOfDB.setItems(allproducts);
    }

    @FXML
    private void onAddButtonPressed() throws IOException, SQLException, ClassNotFoundException {

       FXMLLoader loader = getFxmlLoader("ModifyProductScreen.fxml");
       Parent root = getRoot(loader);
       ControllerOfModifyScreen newController = usingMethodsOfModifyScreen("Dodawanie nowego elementu",loader, true);
        Stage newModifyStage = CreateNewModifyStage();
        customizeStage("Dodawanie nowego elementu", root, newModifyStage);
       newModifyStage.setOnCloseRequest(we -> {
            allproducts.add(newController.getSelectedProduct());
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

        for(int i = 0; i < allproducts.size(); i++)
        {
            if(IDproduktu == allproducts.get(i).getProductID()){
                allproducts.get(i).setNameOfProduct(product.getNameOfProduct());
                allproducts.get(i).setPrice(product.getPrice());
                allproducts.get(i).setDescription(product.getDescription());
                allproducts.get(i).setManufacturer(product.getManufacturer());
                allproducts.get(i).setCategory(product.getCategory());
                allproducts.get(i).setSubcategory(product.getSubcategory());
                allproducts.get(i).setDetailsID(product.getDetailsID());
                allproducts.get(i).setColor(product.getColor());
                allproducts.get(i).setWidth(product.getWidth());
                allproducts.get(i).setHeight(product.getHeight());
                allproducts.get(i).setLength(product.getLength());
                allproducts.get(i).setShelf(product.getShelf());
                allproducts.get(i).setRegal(product.getRegal());
                allproducts.get(i).setStock(product.getStock());
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
        for (Product product : allproducts) {
            if (product.getProductID() == selectedProduct.getProductID()) {
                allproducts.remove(product);
                break;
            }
        }
    }

    private void deleteElementFromDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();
        String sql = "DELETE FROM `hurtownia`.`product` WHERE (`productID` = '" + selectedProduct.getProductID() + "')";
        statement.executeUpdate(sql);
        sql = "DELETE FROM `hurtownia`.`details` WHERE (`detailsID` = '" + selectedProduct.getDetailsID() + "')";
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

    public void researchItmes(String key)
    {
        currentproducts.clear();
        String keyword = key.toLowerCase();
        String [] keys = keyword.split(" ");
        String name, desc, subcategory, manufacture, ID, color;

        //after split string - check all string in keyword
        for(String k: keys){
            if(k.equals("") || k.equals(",") || k.equals("."))
            {
                //do nothing
            }
            else{
                for(Product p: allproducts){
                    ID = String.valueOf(p.getProductID()).toLowerCase();
                    name = p.getNameOfProduct().toLowerCase();
                    desc = p.getDescription().toLowerCase();
                    subcategory = p.getSubcategory().toLowerCase();
                    manufacture = p.getManufacturer().toLowerCase();
                    color = p.getColor().toLowerCase();

                    if(ID.contains(k) || name.contains(k) || desc.contains(k) || subcategory.contains(k)  || manufacture.contains(k) || color.contains(k)){
                        currentproducts.add(p);
                    }
                }
            }
        }
    }

    public void onResearchButtonClicked() {
        String keyword = researchField.getText();
        researchItmes(keyword);
        tableOfDB.setItems(removeDuplicates(currentproducts));
        tableOfDB.refresh();
    }

    public void onClearButtonClicked() {
        tableOfDB.setItems(allproducts);
        tableOfDB.refresh();
        researchField.clear();
        researchField.setPromptText("szukaj");
    }

    ObservableList<Product> removeDuplicates(ObservableList<Product> products){
        ObservableSet<Product> productsSet = FXCollections.observableSet();
        productsSet.addAll(products);
        ObservableList<Product> returnList = FXCollections.observableArrayList();
        for(Product p: productsSet){
            returnList.add(p);
        }

        return returnList;
    }


}

package shopProject.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import shopProject.entity.*;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;


public class ControllerOfMainScreen {

    public ComboBox manufacturerComboBox;
    public ComboBox categoryComboBox;
    public ComboBox subcategoryComboBox;
    public ComboBox colorComboBox;

    public TextField lowerPriceLimit;
    public TextField upperPriceLimit;
    public TextField researchField;

    public Button addButton;
    public Button editionButton;
    public Button deleteButton;
    public Button researchButton;
    public Button clearButton;
    public Button applyButton;

    private final ObservableList<Product> allproducts =  FXCollections.observableArrayList();
    private ObservableList<Product> currentproducts = FXCollections.observableArrayList();

    private ObservableList<RestOfElements> manufacturers =  FXCollections.observableArrayList();
    private ObservableList<RestOfElements>  categories = FXCollections.observableArrayList();
    private final ObservableList<Subcategory> subcategories =  FXCollections.observableArrayList();
    private final ObservableList<Subcategory> subcategoriesFromSelectedCategory = FXCollections.observableArrayList();
    private ObservableList<RestOfElements> colors =  FXCollections.observableArrayList();


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

    public void initialize() throws SQLException, ClassNotFoundException {
        subcategoryComboBox.setDisable(true);
        getDataToArrays();
        setTextAsOnlyNumbers(lowerPriceLimit);
        setTextAsOnlyNumbers(upperPriceLimit);
        fillCellsWithData();
        initializeTable();
    }

    private Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
    }

    public void getDataToArrays() throws SQLException, ClassNotFoundException {
        clearAllLists();
        getAllDataFromDBToLists();
        setItemsToAllComboBoxes();
    }

    private void clearAllLists() {
        manufacturers.clear();
        categories.clear();
        subcategories.clear();
        colors.clear();
    }

    private void getAllDataFromDBToLists() throws ClassNotFoundException, SQLException {

        getRegularDataFromDatabase();
        getSubcategoryDataFromDatabase();
    }

    public void getRegularDataFromDatabase() throws ClassNotFoundException, SQLException {

        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        manufacturers = getElementsFromDatabase(getResultSet(statement, "manufacturer"), "manufacturerID", "manufacturerName" );
        categories = getElementsFromDatabase(getResultSet(statement, "category"), "categoryID", "categoryName");
        colors = getElementsFromDatabase(getResultSet(statement, "color"),  "colorID", "colorName" );
        statement.close();
        connection.close();

    }

    private ObservableList<RestOfElements> getElementsFromDatabase(ResultSet resultSet, String nameOfFirstColumn, String nameOfSecondColumn) throws SQLException {
        ObservableList<RestOfElements> elements =  FXCollections.observableArrayList();
        while(resultSet.next()) {
            RestOfElements singleElement = new RestOfElements(Integer.parseInt(resultSet.getString(nameOfFirstColumn)),
                    resultSet.getString(nameOfSecondColumn));
            elements.add(singleElement);
        }

        return elements;
    }

    private ResultSet getResultSet(Statement statement, String nameOfTable) throws SQLException {
        return statement.executeQuery(getSQLQuery(nameOfTable));
    }

    private String getSQLQuery(String nameOfTable) {
        return "SELECT * FROM hurtownia." + nameOfTable;
    }

    public void getSubcategoryDataFromDatabase() throws ClassNotFoundException, SQLException {

        Connection connection = createConnection();
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM hurtownia.subcategory";
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {
            Subcategory subcategory = new Subcategory(Integer.parseInt(resultSet.getString("subcategoryID")),
                    resultSet.getString("subcategoryName"),
                    Integer.parseInt(resultSet.getString("categoryID")));
            subcategories.add(subcategory);
        }
        statement.close();
        connection.close();
    }

    private void setItemsToAllComboBoxes() {
        manufacturerComboBox.setItems(manufacturers);
        categoryComboBox.setItems(categories);
        subcategoryComboBox.setItems(subcategoriesFromSelectedCategory);
        colorComboBox.setItems(colors);
    }

    public void chosenCategory() {

        setCorrectSubcategoriesFromSelectedCategory();

    }

    public void setCorrectSubcategoriesFromSelectedCategory(){
        subcategoriesFromSelectedCategory.clear();
        int IDofCategory = getIDofElement(categoryComboBox.getValue().toString(), categories);

        for(Subcategory subcategory : subcategories){
            if(subcategory.getCategoryID() == IDofCategory){

                subcategoriesFromSelectedCategory.add(subcategory);
            }
        }
        subcategoryComboBox.setDisable(false);
    }


    public int getIDofElement(String nameOfElement, ObservableList<RestOfElements> listsOfElements) {

        int ID = 0;
        for(RestOfElements element : listsOfElements)
        {
            if(element.toString().equals(nameOfElement))
            {
                ID =  element.getID();
            }
        }
        return ID;
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
        String name, desc,  subcategory, manufacturer, ID, color;

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
                    manufacturer = p.getManufacturer().toLowerCase();
                    color = p.getColor().toLowerCase();

                    if(ID.contains(k) || name.contains(k) || desc.contains(k) ||  subcategory.contains(k)  || manufacturer.contains(k) || color.contains(k)){
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

    public void onApplyButtonClicked(){

        currentproducts.addAll(allproducts);

      try{ updateCurrentProducts(manufacturerComboBox.getValue().toString().toLowerCase(), 1);}  catch(Exception e){}
      try{ updateCurrentProducts(categoryComboBox.getValue().toString().toLowerCase(), 2);}  catch(Exception e){}
      try{ updateCurrentProducts(subcategoryComboBox.getValue().toString().toLowerCase(), 3);}  catch(Exception e){}
      try{ updateCurrentProducts(colorComboBox.getValue().toString().toLowerCase(), 4);}  catch(Exception e){}

//      checkIfElementHasCorrectPrice(Double.parseDouble(lowerPriceLimit.getText()), Double.parseDouble(upperPriceLimit.getText()));

        tableOfDB.setItems(removeDuplicates(currentproducts));
        tableOfDB.refresh();
    }

    private void updateCurrentProducts(String element, int whichElement) {
        ObservableList<Product> productsToDelete = FXCollections.observableArrayList();
        for(Product product: currentproducts){
            String checkElement = "";

                if (whichElement == 1) {
                    checkElement = product.getManufacturer().toLowerCase();
                } else if (whichElement == 2) {
                    checkElement = product.getCategory().toLowerCase();
                } else if (whichElement == 3) {
                    checkElement = product.getSubcategory().toLowerCase();
                } else if (whichElement == 4) {
                    checkElement = product.getColor().toLowerCase();
                }
                else{
                    Alert("Błędnie wybrany element", "Wybrano nieporawny element", Alert.AlertType.ERROR);
                    break;
                }

                if (!checkElement.contains(element)) {
                    productsToDelete.add(product);
                }
        }

        for(Product productToDelete: productsToDelete)
        {
            currentproducts.remove(productToDelete);
        }
        productsToDelete.clear();
    }


    private void checkIfElementHasCorrectPrice(double lowerLimit, double upperLimit) {

        ObservableList<Product> productsToDelete = FXCollections.observableArrayList();
        double price;
        for(Product product: currentproducts){
            price = product.getPrice();
            if(!(price >= lowerLimit && price <= upperLimit)){
                productsToDelete.add(product);
            }
        }
        for(Product productToDelete: productsToDelete)
        {
            currentproducts.remove(productToDelete);
        }
        productsToDelete.clear();
    }


    private void setTextAsOnlyNumbers(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


}

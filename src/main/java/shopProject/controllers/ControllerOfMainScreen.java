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
    private void onAddButtonPressed() throws IOException {

        CreateNewModifyStage("Dodawanie nowego elementu","ModifyProductScreen.fxml","Dodawanie nowego elementu" );

    }

    private void CreateNewModifyStage(String title, String resource, String label) throws IOException {
        Stage newModifyStage = new Stage();
        newModifyStage.setTitle(title);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource));
        Parent root = loader.load();
        //root.getStylesheets().add("Stylesheets/style.css");

        newModifyStage.setScene(new Scene(root));
        newModifyStage.setResizable(false);
        newModifyStage.show();
        ControllerOfModifyScreen newController = loader.getController();
        newController.setAddOrEdit(true);
        newController.setLabelINFO(label);

        newModifyStage.setOnCloseRequest(we -> {
            products.add(newController.getSelectedProduct());
            tableOfDB.refresh();
        });
    }

    @FXML
    private void onEditionButtonPressed() throws IOException, SQLException, ClassNotFoundException {
        selectedProduct = tableOfDB.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert("Błąd","Błąd Należy zaznaczyć odpowiedni wiersz do edycji." );
        }
        else{

            CreateNewModifyStage("Edycja istniejącego elementu","ModifyProductScreen.fxml","Edycja istniejącego elementu" );

          /*




            newController.setAddOrEdit(false);
            newController.setSelectedProduct(selectedProduct);
            newController.initializeData();
            newController.addProductToDB.setText("Edytuj");
            editProductbaseStage.setTitle("Edycja produktu o ID:" + selectedProduct.getProductID());
            */
        }

    }



    @FXML
    private void onDeleteButtonPressed() throws SQLException, ClassNotFoundException {
        selectedProduct = tableOfDB.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert("Błąd","Błąd Należy zaznaczyć odpowiedni wiersz do usunięcia." );
        }
        else{

            Alert deleteClick = new Alert(Alert.AlertType.CONFIRMATION);
            deleteClick.setTitle("Usuwanie elementu");
            deleteClick.setHeaderText(null);
            deleteClick.setContentText("Czy na pewno usunąć element?");
            Optional<ButtonType> action = deleteClick.showAndWait();
            if(action.get() == ButtonType.OK) {


                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
                Statement statement = connection.createStatement();
                String sql = "DELETE FROM `sklep`.`produkty` WHERE (`IDProduktu` = '" + selectedProduct.getProductID() + "')";
                statement.executeUpdate(sql);
                sql = "DELETE FROM `sklep`.`szczegoly` WHERE (`IDProduktu` = '" + selectedProduct.getProductID() + "')";
                statement.executeUpdate(sql);


                for (Product pro : products) {
                    if (pro.getProductID() == selectedProduct.getProductID()) {
                        products.remove(pro);
                        break;
                    }
                }

                tableOfDB.refresh();

                statement.close();
                connection.close();
            }
        }
    }

    void changeProductInTable(Product product){
        int IDproduktu = product.getProductID();

        for(int i = 0; i < products.size(); i++)
        {
            if(IDproduktu == products.get(i).getProductID()){
                products.get(i).setNameOfProduct(product.getNameOfProduct());
                products.get(i).setPrice(product.getPrice());
                products.get(i).setDescription(product.getDescription());
                products.get(i).setRoom(product.getRoom());
                products.get(i).setCategory(product.getCategory());
                products.get(i).setSubcategory(product.getSubcategory());
                products.get(i).setColor(product.getColor());
                products.get(i).setMaterial(product.getMaterial());
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

    void Alert(String setTitle, String setContents) {
        Alert badClick = new Alert(Alert.AlertType.ERROR);
        badClick.setTitle(setTitle);
        badClick.setHeaderText(null);
        badClick.setContentText(setContents);

        badClick.showAndWait();
    }
}

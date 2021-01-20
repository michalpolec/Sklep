package shopProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class editScreenController {
    ObservableList<Product> productObservableList =  FXCollections.observableArrayList();
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

    Product selectedProduct;


    public void getAllData(ObservableList<Product> products)
    {
        //setAll i addAll to samo
        productObservableList.setAll(products);
    }

    void initializeTable()
    {
        tableOfDB.setItems(productObservableList);
    }

    public void initialize() {
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

        initializeTable();
    }

    @FXML
    private void onEditionButtonPressed() throws IOException, SQLException, ClassNotFoundException {
        selectedProduct = tableOfDB.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert badClick = new Alert(Alert.AlertType.ERROR);
            badClick.setTitle("BĹ‚Ä…d");
            badClick.setHeaderText(null);
            badClick.setContentText("BĹ‚Ä…d! NaleĹĽy zaznaczyÄ‡ odpowiedni wiersz do edycji");

            badClick.showAndWait();
        }
        else{

                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/editProductScreen.fxml"));
                Parent root = loader.load();

                editProductScreenController newController = loader.getController();
                newController.getSelectedProduct(selectedProduct);


            Stage editProductbaseStage = new Stage();
            editProductbaseStage.setScene(new Scene(root));
            editProductbaseStage.setTitle("Edycja produktu o ID:" + selectedProduct.getProductID());
            editProductbaseStage.show();
            /*editProductbaseStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    try {
                        //changeProductInTable();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });*/


        }
    }

    @FXML
    private void onDeleteButtonPressed() throws IOException, SQLException, ClassNotFoundException {
        selectedProduct = tableOfDB.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert badClick = new Alert(Alert.AlertType.ERROR);
            badClick.setTitle("BĹ‚Ä…d");
            badClick.setHeaderText(null);
            badClick.setContentText("BĹ‚Ä…d! NaleĹĽy zaznaczyÄ‡ odpowiedni wiersz do edycji");

            badClick.showAndWait();
        }
        else{

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM `sklep`.`produkty` WHERE (`IDProduktu` = '"+ selectedProduct.getProductID() +"')";
            statement.executeUpdate(sql);
            sql = "DELETE FROM `sklep`.`szczegoly` WHERE (`IDProduktu` = '"+ selectedProduct.getProductID() +"')";
            statement.executeUpdate(sql);

            statement.close();
            connection.close();

        }
    }

    @FXML
    private void onAddButtonPressed() throws IOException {

        Stage addNewElementStage = new Stage();
        addNewElementStage.setTitle("Dodawanie nowego elementu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addScreen.fxml"));
        Parent root = loader.load();
        addNewElementStage.setScene(new Scene(root));
        addNewElementStage.show();
    }


    void changeProductInTable(Product product){
        int IDproduktu = product.getProductID();

        for(int i = 0; i < productObservableList.size(); i++)
        {
            if(IDproduktu == productObservableList.get(i).getProductID()){
                productObservableList.get(i).setProductID(product.getProductID());
                productObservableList.get(i).setNameOfProduct(product.getNameOfProduct());
                productObservableList.get(i).setPrice(product.getPrice());
                productObservableList.get(i).setDescription(product.getDescription());
                productObservableList.get(i).setRoom(product.getRoom());
                productObservableList.get(i).setCategory(product.getCategory());
                productObservableList.get(i).setSubcategory(product.getSubcategory());
                productObservableList.get(i).setColor(product.getColor());
                productObservableList.get(i).setMaterial(product.getMaterial());
                productObservableList.get(i).setShelf(product.getShelf());
                productObservableList.get(i).setRegal(product.getRegal());
                productObservableList.get(i).setStock(product.getStock());
                break;
            }
        }

        tableOfDB.refresh();

    }
}

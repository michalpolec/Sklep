package shopProject;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class editProductScreen {
    private Product selectedProduct;

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
    public ComboBox shelfCB;
    public ComboBox regalCB;
    public ComboBox widthCB;
    public ComboBox lengthCB;
    public ComboBox heightCB;

    public void initialize() {
        if(selectedProduct == null){
            System.out.println("brak danych o produkcie");
        } else {
            initializeData();
        }
    }

    public void initializeData(){
        IDTF.setText(String.valueOf(selectedProduct.getProductID()));
        NameTF.setText(selectedProduct.getNameOfProduct());
        PriceTF.setText(String.valueOf(selectedProduct.getPrice()));
        DescriptionTF.setText(selectedProduct.getDescription());
        StockTF.setText(String.valueOf(selectedProduct.getStock()));
    }

    public void getSelectedProduct(Product product){
        this.selectedProduct = product;
        initializeData(); //to musi byÄ‡!!!!
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

    public void Alert(){
        Alert nullData = new Alert(Alert.AlertType.ERROR);
        nullData.setTitle("BĹ‚Ä…d podczas wpisywania");
        nullData.setHeaderText(null);
        nullData.setContentText("Nic nie wpisano");

        nullData.showAndWait();
    }
}

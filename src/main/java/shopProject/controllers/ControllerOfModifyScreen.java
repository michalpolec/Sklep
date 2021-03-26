package shopProject.controllers;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import shopProject.entity.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

public class ControllerOfModifyScreen {

    boolean AddOrEdit;

    public TextField nameField;
    public TextField priceField;
    public TextField descriptionField;
    public TextField stockField;

    public ComboBox manufacturerBox;
    public ComboBox subcategoryBox;
    public ComboBox categoryBox;
    public ComboBox colorBox;
    public ComboBox dimensionsBox;
    public ComboBox positionBox;

    public Button roomButton;
    public Button subcategoryButton;
    public Button categoryButton;
    public Button colorButton;
    public Button dimensionsButton;
    public Button positionButton;
    public Button addProductToDB;
    public Button cancelButton;

    public Label labelINFO;

    private Product selectedProduct;

    private ObservableList<RestOfElements> manufacturers =  FXCollections.observableArrayList();
    private ObservableList<RestOfElements>  categories = FXCollections.observableArrayList();
    private final ObservableList<Subcategory> subcategories =  FXCollections.observableArrayList();
    private final ObservableList<Subcategory> subcategoriesFromSelectedCategory = FXCollections.observableArrayList();
    private ObservableList<RestOfElements> colors =  FXCollections.observableArrayList();
    private final ObservableList<Dimension> dimensions = FXCollections.observableArrayList();
    private final ObservableList<Position> positions = FXCollections.observableArrayList();

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Product getSelectedProduct(){
        return selectedProduct;
    }

   public void initialize() throws SQLException, ClassNotFoundException {
       subcategoryBox.setDisable(true);
       setTextAsOnlyNumbers(priceField);
       setTextAsOnlyNumbers(stockField);
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

    public void initializeData() {

       nameField.setText(getSelectedProduct().getNameOfProduct());
       priceField.setText(String.valueOf(getSelectedProduct().getPrice()));
       descriptionField.setText(getSelectedProduct().getDescription());
       stockField.setText(String.valueOf(getSelectedProduct().getStock()));
       manufacturerBox.getSelectionModel().select(getSelectedProduct().getManufacturer());
       categoryBox.getSelectionModel().select(getSelectedProduct().getCategory());
       setCorrectSubcategoriesFromSelectedCategory();
       subcategoryBox.getSelectionModel().select(getSelectedProduct().getSubcategory());
       colorBox.getSelectionModel().select(getSelectedProduct().getColor());
       dimensionsBox.getSelectionModel().select(getSelectedProduct().getDimension());
       positionBox.getSelectionModel().select(getSelectedProduct().getPosition());
    }

    public void setCorrectSubcategoriesFromSelectedCategory(){
        subcategoriesFromSelectedCategory.clear();
        int IDofCategory = getIDofElement(categoryBox.getValue().toString(), categories);

        for(Subcategory subcategory : subcategories){
            if(subcategory.getCategoryID() == IDofCategory){

                subcategoriesFromSelectedCategory.add(subcategory);
            }
        }
        subcategoryBox.setDisable(false);
    }


    //Metoda umożliwiająca edytowanie/dodanie produktu - pobiera nowo-wpisane dane i odpowiednio edytuje bądź dodaje jako nowy produkt
   public void modifyDatabaseButtonPressed() throws ClassNotFoundException, SQLException, IOException {

       int productID = 0;
       String productName = "";
       double productPrice = 0.0;
       String productDescription = "";
       int manufacturerID = 0;
       int subcategoryID = 0;
       int detailsID = 0;
       int colorID = 0;
       int dimensionID = 0;
       int positionID = 0;
       int productStock = 0;
       boolean Continue = true;

       try {
           productName = nameField.getText();
           productPrice = Double.parseDouble(priceField.getText());
           productDescription = descriptionField.getText();
           manufacturerID = getIDofElement(manufacturerBox.getValue().toString(), manufacturers);
           subcategoryID = getIDofElementForSubcategory(subcategoryBox.getValue().toString(), subcategoriesFromSelectedCategory);
           colorID = getIDofElement(colorBox.getValue().toString(), colors);
           dimensionID = getIDofElementForDimension(dimensionsBox.getValue().toString(), dimensions);
           positionID = getIDofElementForPosition(positionBox.getValue().toString(), positions);
           productStock = Integer.parseInt(stockField.getText());

       }
       catch (Exception e){
         Alert("Błąd wprowadzania danych", "Wprowadzono niepoprawne dane lub nie wprowadzono ich wcale.", Alert.AlertType.ERROR).showAndWait();
         Continue = false;
       }

       if(AddOrEdit && Continue) {

           Connection connection = createConnection();
           Statement statement = connection.createStatement();

           boolean continueTypingData = true;


           try {

               String sql_details = "INSERT INTO details ( positionID, dimensionID, colorID) VALUES ('" + positionID + "', '" + dimensionID + "', '" + colorID + "');";
               statement.executeUpdate(sql_details);
           } catch (SQLException e) {
               Alert("Błąd polecenia", "Błędne polecenie", Alert.AlertType.ERROR).showAndWait();
               continueTypingData = false;
           }

           if (continueTypingData) {

               try {

                   String sql_products = "INSERT INTO product (productName, productPrice, productDescription,subcategoryID, detailsID, manufacturerID, stock) VALUES ('" + productName+ "', '" + productPrice + "', '" + productDescription + "', '" + subcategoryID+  "', '" + getIDofDetails(statement) + "', '" + manufacturerID + "', '" + productStock + "');";
                   statement.executeUpdate(sql_products);
                   setSelectedProductFromDB(0);

               } catch (SQLException e) {


                   System.out.println(e);
                   Alert("Błąd bazy danych", "Niezgodność ID produktów", Alert.AlertType.ERROR).showAndWait();
                   statement.close();
                   connection.close();
               }
           }
           statement.close();
           connection.close();

           Alert("Wpisano nowy produkt do bazy", "Poprawnie dodano nowy element.", Alert.AlertType.INFORMATION).showAndWait();

           Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
           closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
           closeLoginStage.close();


       }
       else if (Continue){


           Connection connection = createConnection();
           Statement statement = connection.createStatement();

           productID = getSelectedProduct().getProductID();
           detailsID = getSelectedProduct().getDetailsID();

           String sql_details = "UPDATE `hurtownia`.`details` SET `positionID` = '"+ positionID +"', `dimensionID` = '"+ dimensionID + "', `colorID` = '"+ colorID +"' WHERE (`detailsID` = '"+ detailsID +"');";
           statement.executeUpdate(sql_details);

           String sql_products = "UPDATE `hurtownia`.`product` SET productName = '"+ productName +"', productPrice = '"+ productPrice +"', productDescription = '"+ productDescription +"', manufacturerID = '"+ manufacturerID +"', subcategoryID = '"+ subcategoryID +"', detailsID = '"+ detailsID +"', stock = '"+ productStock +"' WHERE (productID = '"+ productID +"');";
           statement.executeUpdate(sql_products);

           setSelectedProductFromDB(productID);

           statement.close();
           connection.close();

           Alert("Edytowano nowy produkt do bazy", "Poprawnie edytowno element.", Alert.AlertType.INFORMATION).showAndWait();

           Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
           closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
           closeLoginStage.close();


       }
   }

    private Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
    }


    private int getIDofDetails(Statement statement) throws SQLException {
        int detailsID = 0;
        String sql_getIDofDetails = "SELECT max(detailsID) FROM details";
        ResultSet resultSet = statement.executeQuery(sql_getIDofDetails);
        while(resultSet.next()) {
            detailsID = resultSet.getInt("max(detailsID)");
        }
        return detailsID;
    }

    //Metoda działająca na kliknięcie przycisku 'dodaj nowe' pomieszczenie - otwiera nowe okno
   public void addManufacturer() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego producenta", "manufacturerName", "manufacturer",  "Wpisz nowe pomieszczenie");

   }

    //Metoda działająca na kliknięcie przycisku 'dodaj nową' ketgorię - otwiera nowe okno
   public void addCategory() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowej kategorii", "categoryName", "category",  "Wpisz nową kategorie" );
   }


   public void addSubcategory() throws IOException, SQLException, ClassNotFoundException {

        openSubcategoryScreen();
   }

    public void chosenCategory() {

        setCorrectSubcategoriesFromSelectedCategory();

    }

    //Metoda działająca na kliknięcie przycisku 'dodaj nowy' kolor - otwiera nowe okno
   public void addColor() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego koloru" , "colorName", "color",  "Wpisz nowy kolor");
   }

    //Metoda działająca na kliknięcie przycisku 'dodaj nowe' wymiary - otwiera nowe okno
    public void addDimensions() throws SQLException, IOException, ClassNotFoundException {

        openDimensionScreen();
    }

    //Metoda działająca na kliknięcie przycisku 'dodaj nową' pozycję na magazynie - otwiera nowe okno
    public void addPosition() throws IOException, SQLException, ClassNotFoundException {
       openPositionScreen();
    }

   public void openElementScreen(String nameOfStage, String nameOfFirstColumn, String nameOfTabel, String textOfLabel) throws IOException, SQLException, ClassNotFoundException {

       FXMLLoader loader = getFxmlLoader("AddElementScreen.fxml");
       Parent root = getRoot(loader);
       ControllerOfAddingNewElementToDB newController = loader.getController();
       newController.setOptionOfScreen(nameOfFirstColumn, nameOfTabel,  textOfLabel);
       Stage newAddStage = CreateNewAddStage();
       customizeStage(nameOfStage, root, newAddStage);
       newAddStage.setOnCloseRequest(we -> {
          /* try {
               getDataToArrays();
           } catch (SQLException | ClassNotFoundException throwables) {
               throwables.printStackTrace();
           }*/
       });
   }

   //Metoda otwierająca nowe okno dodania podakategorii
   public void openSubcategoryScreen() throws IOException, SQLException, ClassNotFoundException {

       FXMLLoader loader = getFxmlLoader("AddSubcategoryScreen.fxml");
       Parent root = getRoot(loader);
       ControllerOFAddingNewSubcategoryToDB newController = loader.getController();
       newController.setOptionOfSubcategoryScreen( categories, "Wybierz kategorie oraz wpisz nową podkategorie");
       Stage newAddStage = CreateNewAddStage();
       customizeStage("Dodawnie nowej podkategorii", root, newAddStage);
       newAddStage.setOnCloseRequest(we -> {
          /* try {
               getDataToArrays();
           } catch (SQLException | ClassNotFoundException throwables) {
               throwables.printStackTrace();
           }*/
       });
   }

   //Metoda otwierająca nowe okno dodania wymiarów
   public void openDimensionScreen() throws IOException, SQLException, ClassNotFoundException {

       FXMLLoader loader = getFxmlLoader("AddDimensionScreen.fxml");
       Parent root = getRoot(loader);
       ControllerOfAddingNewDimensionToDB newController = loader.getController();
       newController.setOptionOfDimensionScreen("Wpisz szerokość, długość oraz wysokość produktu");
       Stage newAddStage = CreateNewAddStage();
       customizeStage("Dodawnie nowego wymiaru", root, newAddStage);
       newAddStage.setOnCloseRequest(we -> {
          /* try {
               getDataToArrays();
           } catch (SQLException | ClassNotFoundException throwables) {
               throwables.printStackTrace();
           }*/
       });
   }

    //Metoda otwierająca nowe okno dodania pozycji w magazynie
   public void openPositionScreen() throws IOException, SQLException, ClassNotFoundException {

       FXMLLoader loader = getFxmlLoader("AddPositionScreen.fxml");
       Parent root = getRoot(loader);
       ControllerOfAddingNewPositionToDB newController = loader.getController();
       newController.setOptionOfPositionScreen("Wpisz półkę oraz regał");
       Stage newAddStage = CreateNewAddStage();
       customizeStage("Dodawnie nowej pozycji", root, newAddStage);
       newAddStage.setOnCloseRequest(we -> {
          /* try {
               getDataToArrays();
           } catch (SQLException | ClassNotFoundException throwables) {
               throwables.printStackTrace();
           }*/
       });

    }

    private Stage CreateNewAddStage() {
        return new Stage();

    }

    private void customizeStage(String title, Parent root, Stage newModifyStage) {
        newModifyStage.setTitle(title);
        //root.getStylesheets().add("Stylesheets/style.css");
        newModifyStage.setScene(new Scene(root));
        newModifyStage.setResizable(false);
        newModifyStage.show();
    }

    private Parent getRoot(FXMLLoader loader) throws IOException {
        return loader.load();
    }

    private FXMLLoader getFxmlLoader(String resource) {
        return new FXMLLoader(getClass().getClassLoader().getResource(resource));
    }




    public int getIDofElementForSubcategory(String nameOfElement, ObservableList<Subcategory> listsOfElements) {

       int ID = 0;
       for(Subcategory element : listsOfElements)
       {
           if(element.toString().equals(nameOfElement))
           {
               ID =  element.getSubcategoryID();
           }
       }
       return ID;
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

    public int getIDofElementForDimension(String nameOfElement, ObservableList<Dimension> listsOfElements) {

        int ID = 0;
        for(Dimension element : listsOfElements)
        {
            if((element.toString().equals(nameOfElement)))
            {
                ID =  element.getDimensionID();
            }
        }
        return ID;
    }

    public int getIDofElementForPosition(String nameOfElement, ObservableList<Position> listsOfElements) {

        int ID = 0;
        for(Position element : listsOfElements)
        {
            if(element.toString().equals(nameOfElement))
            {
                ID =  element.getPositionID();
            }
        }
        return ID;
    }


    //Metoda decydująca o edycji lub dodawanius
    public void setAddOrEdit(boolean AddOrEdit){
       this.AddOrEdit = AddOrEdit;
    }

    //Metoda zwracająca listę stringów
    public ObservableList<String> getStringArray(ObservableList objects) {

        ObservableList<String> stringArray = FXCollections.observableArrayList();
        for(Object testObject: objects)
        {

            stringArray.add(testObject.toString());

        }

        return stringArray;

    }


    //Metoda ustawiająca wybrany produkt od danym ID z bazy danych
    public void setSelectedProductFromDB(int IDofProduct) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        String sql_getID;
        if(AddOrEdit) {

            sql_getID   = """
                    SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName,\s
                    subcategory.subcategoryName, subcategory.categoryID, product.detailsID, product.manufacturerID, product.subcategoryID, details.colorID, details.dimensionID, details.positionID, color.colorName, dimension.width, dimension.height, dimension.length,
                    positions.shelf, positions.regal, stock
                    FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)
                    INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)
                    INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)
                    INNER JOIN category ON subcategory.categoryID = category.categoryID)
                    INNER JOIN color ON details.colorID = color.colorID)
                    INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)
                    INNER JOIN positions ON details.positionID = positions.positionID) WHERE product.productID = (SELECT MAX(product.productID) FROM product);""";

        }
        else {

            sql_getID = "SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName, \n" +
                    "subcategory.subcategoryName,  subcategory.categoryID, product.detailsID, product.manufacturerID, product.subcategoryID, details.colorID, details.dimensionID, details.positionID, color.colorName, dimension.width, dimension.height, dimension.length,\n" +
                    "positions.shelf, positions.regal, stock\n" +
                    "FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)\n" +
                    "INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)\n" +
                    "INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)\n" +
                    "INNER JOIN category ON subcategory.categoryID = category.categoryID)\n" +
                    "INNER JOIN color ON details.colorID = color.colorID)\n" +
                    "INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)\n" +
                    "INNER JOIN positions ON details.positionID = positions.positionID) WHERE product.productID = " + IDofProduct + ";";
        }

        ResultSet resultSet = statement.executeQuery(sql_getID);


        while(resultSet.next()) {

            setSelectedProduct(new Product(
                    resultSet.getInt("productID"),
                    resultSet.getString("productName"),
                    resultSet.getDouble("productPrice"),
                    resultSet.getString("productDescription"),
                    resultSet.getInt("manufacturerID"),
                    resultSet.getString("manufacturerName"),
                    resultSet.getInt("categoryID"),
                    resultSet.getString("categoryName"),
                    resultSet.getInt("subcategoryID"),
                    resultSet.getString("subcategoryName"),
                    resultSet.getInt("detailsID"),
                    resultSet.getInt("colorID"),
                    resultSet.getString("colorName"),
                    resultSet.getInt("dimensionID"),
                    resultSet.getDouble("width"),
                    resultSet.getDouble("height"),
                    resultSet.getDouble("length"),
                    resultSet.getInt("positionID"),
                    resultSet.getInt("shelf"),
                    resultSet.getInt("regal"),
                    resultSet.getInt("stock")));
        }
        statement.close();
        connection.close();
    }


    private Alert Alert(String setTitle, String setContents, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(setTitle);
        alert.setHeaderText(null);
        alert.setContentText(setContents);

        return alert;
    }

    void setLabelINFO(String labelText)
    {
        labelINFO.setText(labelText);
    }

    public void onCancelAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}

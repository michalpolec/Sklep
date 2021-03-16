package shopProject.controllers;


import shopProject.entity.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.sql.*;

public class ControllerOfModifyScreen {

    private Product selectedProduct;
    boolean AddOrEdit;
    public Image imageFromFile;

    ObservableList<restOfElements> manufacturers =  FXCollections.observableArrayList();
    ObservableList<restOfElements>  categories = FXCollections.observableArrayList();
    ObservableList<Subcategory> subcategories =  FXCollections.observableArrayList();
    ObservableList<Subcategory>  subcategory = FXCollections.observableArrayList();
    ObservableList<restOfElements> colors =  FXCollections.observableArrayList();
    ObservableList<restOfElements> materials =  FXCollections.observableArrayList();
    ObservableList<Dimension> dimensions = FXCollections.observableArrayList();
    ObservableList<Position> positions = FXCollections.observableArrayList();

    public ImageView imageView;

    public TextField nameField;
    public TextField priceField;
    public TextField descriptionField;
    public TextField stockField;

    public ComboBox manufacturerBox;
    public ComboBox subcategoryBox;
    public ComboBox categoryBox;
    public ComboBox colorBox;
    public ComboBox materialBox;
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
    public Button imageButton;

    public Label labelINFO;

    //Inicjalizacja klasy - ustawienie odpowiednich textField'ow tak aby mozna bylo wpisywac tylko cyfry
   public void initialize() throws SQLException, ClassNotFoundException {

       subcategoryBox.setDisable(true);
       getDataToArrays();
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

    //Inicjalizowanie wybranymi danymi podczas edycji produktu
    public void initializeData() throws SQLException, ClassNotFoundException, IOException {

       nameField.setText(selectedProduct.getNameOfProduct());
       priceField.setText(String.valueOf(selectedProduct.getPrice()));
       descriptionField.setText(selectedProduct.getDescription());
       stockField.setText(String.valueOf(selectedProduct.getStock()));
       manufacturerBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getManufacturer(),getStringArray(manufacturers)));
       categoryBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getCategory(), getStringArray(categories)));
       setCorrectSubcategories();
        subcategoryBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getSubcategory(), getStringArray(subcategory)));
       colorBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getColor(), getStringArray(colors)));
       dimensionsBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getWidth() + "cm x " + selectedProduct.getHeight() + "cm x " + selectedProduct.getLength() + "cm",getStringArray(dimensions)));
       positionBox.getSelectionModel().select(getIndexToComboBox("Półka: " + selectedProduct.getShelf() + ", Regał: " + selectedProduct.getRegal(), getStringArray(positions)));


    }


    public void onCancelAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    //Metoda umożliwiająca edytowanie/dodanie produktu - pobiera nowo-wpisane dane i odpowiednio edytuje bądź dodaje jako nowy produkt
   public void modifyDatabaseButtonPressed() throws ClassNotFoundException, SQLException, IOException {

       String productName = "";
       double productPrice = 0.0;
       String productDescription = "";
       int manufacturerID = 0;
       int subcategoryID = 0;
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
           subcategoryID = getIDofElementForSubcategory(subcategoryBox.getValue().toString(), subcategory);
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

           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
           Statement statement = connection.createStatement();

           boolean continueTypingData = true;

           String sql_getIDfromProducts = "SELECT max(productID) FROM hurtownia.product";
           int greatestIDofProducts = 0;
           ResultSet resultSet1 = statement.executeQuery(sql_getIDfromProducts);
           while(resultSet1.next()) {
               greatestIDofProducts = resultSet1.getInt("max(productID)") + 1;
           }

           String sql_getIDfromDetails = "SELECT max(productID) FROM hurtownia.product";
           int greatestIDofDetails = 0;
           ResultSet resultSet2 = statement.executeQuery(sql_getIDfromDetails);
           while(resultSet2.next()) {
               greatestIDofDetails = resultSet2.getInt("max(productID)") + 1;
           }

           if (greatestIDofProducts == greatestIDofDetails) {

               String sql_increment1 = "ALTER TABLE `hurtownia`.`product` AUTO_INCREMENT = " + greatestIDofProducts + ";";
               statement.executeUpdate(sql_increment1);
               String sql_increment2 = "ALTER TABLE `hurtownia`.`details` AUTO_INCREMENT = " + greatestIDofDetails + ";";
               statement.executeUpdate(sql_increment2);

               try {

                   String sql_details = "INSERT INTO details ( positionID, dimensionID, colorID) VALUES ('" + positionID + "', '" + dimensionID + "', '" + colorID + "');";
                   statement.executeUpdate(sql_details);
               } catch (SQLException e) {
                   Alert("Błąd polecenia", "Błędne polecenie", Alert.AlertType.ERROR).showAndWait();
                   continueTypingData = false;
               }

               if (continueTypingData) {

                   try {

                       String sql_products = "INSERT INTO product (productName, productPrice, productPrice, manufacturerID, subcategoryID, stock) VALUES (?,?,?,?,?,?);";

                       PreparedStatement preparedStatement = connection.prepareStatement(sql_products);
                       preparedStatement.setString(1, productName);
                       preparedStatement.setDouble(2, productPrice);
                       preparedStatement.setString(3, productDescription);
                       preparedStatement.setInt(4, manufacturerID);
                       preparedStatement.setInt(5, subcategoryID);
                       preparedStatement.setInt(6, productStock);

                       preparedStatement.executeUpdate();

                       setSelectedProductFromDB(0); // Dodwanie do wyświetlanych produktów

                       preparedStatement.close();

                   } catch (SQLException e) {

                       Alert("Błąd bazy danych", "Niezgodność ID produktów", Alert.AlertType.ERROR).showAndWait();
                       String sql_backroll_details = "DELETE FROM `hurtownia`.`szczegoly` WHERE (`IDProduktu` = '" + greatestIDofDetails + "')";
                       statement.executeUpdate(sql_backroll_details);

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
           else {
               Alert("Błąd bazy danych", "Niezgodność tabel Produkty i Szczegóły", Alert.AlertType.ERROR).showAndWait();
               Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
               closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
               closeLoginStage.close();
           }

       }
       else if (Continue){

           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
           Statement statement = connection.createStatement();

           int IDProduct = selectedProduct.getProductID();

           String sql_details = "UPDATE `hurtownia`.`details` SET `positionID` = '"+ positionID +"', `dimensionID` = '"+ dimensionID + "', `colorID` = '"+ colorID +"' WHERE (`productID` = '"+ IDProduct +"');";
           statement.executeUpdate(sql_details);

           String sql_products = "UPDATE `hurtownia`.`product` SET productName = ?, productPrice = ?, productDescription = ?, manufacturerID = ?, subcategoryID = ?, stock = ? WHERE (productID = ?);";
           PreparedStatement preparedStatement = connection.prepareStatement(sql_products);

           preparedStatement.setString(1,productName);
           preparedStatement.setDouble(2,productPrice);
           preparedStatement.setString(3, productDescription);
           preparedStatement.setInt(4, manufacturerID);
           preparedStatement.setInt(5, subcategoryID);
           preparedStatement.setInt(6, productStock);
           preparedStatement.setInt(7, IDProduct);
           preparedStatement.executeUpdate();

           setSelectedProductFromDB(IDProduct);

           preparedStatement.close();
           statement.close();
           connection.close();

           Alert("Edytowano nowy produkt do bazy", "Poprawnie edytowno element.", Alert.AlertType.INFORMATION).showAndWait();

           Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
           closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
           closeLoginStage.close();


       }
   }

   //Metoda działająca na kliknięcie przycisku 'dodaj nowe' pomieszczenie - otwiera nowe okno
   public void addManufacturer() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego producenta", "manufacturerName", "manufacturer",  "Wpisz nowe pomieszczenie");

   }

    //Metoda działająca na kliknięcie przycisku 'dodaj nową' ketgorię - otwiera nowe okno
   public void addCategory() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowej kategorii", "NazwaKategorii", "kategoria",  "Wpisz nową kategorie" );
   }


   public void addSubcategory() throws IOException, SQLException, ClassNotFoundException {

        openSubcategoryScreen();
   }

    public void chosenCategory() {

        setCorrectSubcategories();

    }

    //Metoda działająca na kliknięcie przycisku 'dodaj nowy' kolor - otwiera nowe okno
   public void addColor() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego koloru" , "NazwaKoloru", "kolor",  "Wpisz nowy kolor");
   }

    //Metoda działająca na kliknięcie przycisku 'dodaj nowy' materiał - otwiera nowe okno
   public void addMaterial() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego materiału" ,  "NazwaMaterialu", "material", "Wpisz nowy materiał");
   }

    //Metoda działająca na kliknięcie przycisku 'dodaj nowe' wymiary - otwiera nowe okno
    public void addDimensions(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

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
       newAddStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               try {
                   getDataToArrays();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       });
   }

   //Metoda otwierająca nowe okno dodania podakategorii
   public void openSubcategoryScreen() throws IOException, SQLException, ClassNotFoundException {

       FXMLLoader loader = getFxmlLoader("AddSubcategoryScreen.fxml");
       Parent root = getRoot(loader);
       ControllerOFAddingNewSubcategoryToDB newController = loader.getController();
       newController.setOptionOfSubcategoryScreen("subcategoryName", "categoryID",  "subcategory", categories, "Wybierz kategorie oraz wpisz nową podkategorie");
       Stage newAddStage = CreateNewAddStage();
       customizeStage("Dodawnie nowej podkategorii", root, newAddStage);
       newAddStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               try {
                   getDataToArrays();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       });
   }

   //Metoda otwierająca nowe okno dodania wymiarów
   public void openDimensionScreen() throws IOException, SQLException, ClassNotFoundException {

       Stage addDimension =  new Stage();
       addDimension.setTitle("Dodawnie nowego wymiaru");
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/AddDimensionScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addDimension.setScene(new Scene(root));

       ControllerOfAddingNewDimensionToDB newController = loader.getController();
       newController.setOptionOfDimensionScreen("Szerokosc", "Wysokosc","Dlugosc", "wymiary",  "Wpisz szerokość, długość oraz wysokość produktu");

       addDimension.setResizable(false);
       addDimension.show();
       addDimension.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               try {
                   getDataToArrays();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       });
   }

    //Metoda otwierająca nowe okno dodania pozycji w magazynie
   public void openPositionScreen() throws IOException, SQLException, ClassNotFoundException {

       Stage addPosition =  new Stage();
       addPosition.setTitle("Dodawnie nowej pozycji");
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/AddPositionScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addPosition.setScene(new Scene(root));

       ControllerOfAddingNewPositionToDB newController = loader.getController();
       newController.setOptionOfPositionScreen( "Polka", "Regal", "pozycja", "Wpisz półkę oraz regał");

       addPosition.setResizable(false);
       addPosition.show();
       addPosition.setOnCloseRequest(new EventHandler<WindowEvent>() {
           public void handle(WindowEvent we) {
               try {
                   getDataToArrays();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
       });

    }



    private Stage CreateNewAddStage() throws IOException {
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

    //Metoda pobierająca wszystkie podkategorie z bazy danych
   public void getSubcategoryData() throws ClassNotFoundException, SQLException {

       Class.forName("com.mysql.cj.jdbc.Driver");
       Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
       Statement statement = connection.createStatement();


       String sql = "SELECT * FROM hurtownia.subcategory";
       ResultSet resultSet = statement.executeQuery(sql);

       while(resultSet.next()) {
            Subcategory subcategory = new Subcategory(Integer.parseInt(resultSet.getString("subcategoryID")),resultSet.getString("subcategoryName"),Integer.parseInt(resultSet.getString("categoryID")));
            subcategories.add(subcategory);
       }
       statement.close();
       connection.close();
   }

   //Metoda pobierająca wybrane dane z bazy danych
    public void getChosenDataFromDB(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, ObservableList<restOfElements> litsOfData) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM hurtownia." + nameOfTable;
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {
            restOfElements table = new restOfElements(Integer.parseInt(resultSet.getString(nameOfFirstColumn)),
                    resultSet.getString(nameOfSecondColumn));
            litsOfData.add(table);
        }
        statement.close();
        connection.close();

    }

    //Metoda pobierająca wszystkie wymiary z bazy danych
    public void getDimensionData() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM hurtownia.dimension";
        ResultSet resultSet = statement.executeQuery(sql);


        while(resultSet.next()) {

            Dimension dimension = new Dimension(Integer.parseInt(resultSet.getString("dimensionID")), (int) Double.parseDouble(resultSet.getString("width")), (int) Double.parseDouble(resultSet.getString("height")), (int) Double.parseDouble(resultSet.getString("length")));
            dimensions.add(dimension);
        }
        statement.close();
        connection.close();
    }

    //Metoda pobierająca wszystkie pozycje z bazy danych
    public void getPositionData() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM hurtownia.positions";
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {

            Position position = new Position(Integer.parseInt(resultSet.getString("positionID")), Integer.parseInt(resultSet.getString("shelf")), Integer.parseInt(resultSet.getString("regal")));
            positions.add(position);
        }

        statement.close();
        connection.close();

    }

    //Metoda wyszukująca ID danej podkategorii
    public int getIDofElementForSubcategory(String nameOfElement, ObservableList<Subcategory> listsOfElements) {

       int ID = 0;
       for(Subcategory element : listsOfElements)
       {
           if(element.getSubcategoryName().equals(nameOfElement))
           {
               ID =  element.getSubcategoryID();
           }
       }
       return ID;
    }

    //Ogólna Metoda wyszukująca ID restOfElements
    public int getIDofElement(String nameOfElement, ObservableList<restOfElements> listsOfElements) {

        int ID = 0;
        for(restOfElements element : listsOfElements)
        {
            if(element.getName().equals(nameOfElement))
            {
                ID =  element.getID();
            }
        }
        return ID;
    }

    //Metoda wyszukująca ID wymiaru
    public int getIDofElementForDimension(String nameOfElement, ObservableList<Dimension> listsOfElements) {

        int ID = 0;
        for(Dimension element : listsOfElements)
        {
            if((element.getWidth()  + "cm x " + element.getHeight()  + "cm x " + element.getLenght()  + "cm").equals(nameOfElement))
            {
                ID =  element.getDimensionID();
            }
        }
        return ID;
    }

    //Metoda wyszukująca ID pozycji
    public int getIDofElementForPosition(String nameOfElement, ObservableList<Position> listsOfElements) {

        int ID = 0;
        for(Position position : listsOfElements)
        {
            if(("Półka: " + position.getShelf() + ", Regał: " + position.getRegal()).equals(nameOfElement))
            {
                ID =  position.getPositionID();
            }
        }
        return ID;
    }

    //Metoda ustawiająca dane w comboboxach
    public void getDataToArrays() throws SQLException, ClassNotFoundException {
        //czyszczenie
        manufacturers.clear();
        categories.clear();
        subcategories.clear();
        colors.clear();
        materials.clear();
        dimensions.clear();
        positions.clear();

        //pobieranie danych
        getChosenDataFromDB("manufacturerID", "manufacturerName", "manufacturer", manufacturers);
        getChosenDataFromDB("categoryID","categoryName", "category", categories);
        getSubcategoryData();
        getChosenDataFromDB("colorID", "colorName", "color", colors);
        getDimensionData();
        getPositionData();

        //ustawianie danych
        manufacturerBox.setItems(manufacturers);
        categoryBox.setItems(categories);
        subcategoryBox.setItems(subcategory);
        colorBox.setItems(colors);
        dimensionsBox.setItems(dimensions);
        positionBox.setItems(positions);

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

    //Metoda zwracająca indeks wybranej opcji comboBoxa
    public int getIndexToComboBox(String object, ObservableList<String> objects) {
        int i = 0;
        for(String testObject: objects)
        {
            if(object.equals(testObject))
            {
                break;
            }
            i++;
        }
        return i;
    }

    //Metoda ustawiająca właściwe podkategorie w zależności od wybranej Kateogrii
    public void setCorrectSubcategories(){
        subcategory.clear();
        int IDofCategory = getIDofElement(categoryBox.getValue().toString(), categories);

        for(Subcategory sub : subcategories){
            if(sub.getCategoryID() == IDofCategory){

                subcategory.add(sub);
            }
        }
        subcategoryBox.setDisable(false);
    }

    //Metoda ustawiająca wybrany produkt
    public void setSelectedProduct(Product selectedProduct) throws SQLException, ClassNotFoundException {
        this.selectedProduct = selectedProduct;
    }

    //Metoda zwracająca wybrany produkt
    public Product getSelectedProduct(){
        return selectedProduct;
    }

    //Metoda ustawiająca wybrany produkt od danym ID z bazy danych
    public void setSelectedProductFromDB(int IDofProduct) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hurtownia?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();
        String sql_getID = "";
        if(AddOrEdit) {

            sql_getID   = "SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName, \n" +
                    "subcategory.subcategoryName, color.colorName, dimension.width, dimension.height, dimension.length,\n" +
                    "positions.shelf, positions.regal, stock\n" +
                    "FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)\n" +
                    "INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)\n" +
                    "INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)\n" +
                    "INNER JOIN category ON subcategory.categoryID = category.categoryID)\n" +
                    "INNER JOIN color ON details.colorID = color.colorID)\n" +
                    "INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)\n" +
                    "INNER JOIN positions ON details.positionID = positions.positionID) WHERE produkty.IDProduktu = (SELECT MAX(produkty.IDProduktu) FROM produkty);";

        }
        else {

            sql_getID = "SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName, \n" +
                    "subcategory.subcategoryName, color.colorName, dimension.width, dimension.height, dimension.length,\n" +
                    "positions.shelf, positions.regal, stock\n" +
                    "FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)\n" +
                    "INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)\n" +
                    "INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)\n" +
                    "INNER JOIN category ON subcategory.categoryID = category.categoryID)\n" +
                    "INNER JOIN color ON details.colorID = color.colorID)\n" +
                    "INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)\n" +
                    "INNER JOIN positions ON details.positionID = positions.positionID) WHERE produkty.IDProduktu = " + IDofProduct + ";";
        }

        ResultSet resultSet = statement.executeQuery(sql_getID);


        while(resultSet.next()) {

            selectedProduct = new Product(
                    resultSet.getInt("productID"),
                    resultSet.getString("productName"),
                    resultSet.getDouble("productPrice"),
                    resultSet.getString("productDescription"),
                    resultSet.getString("manufacturerName"),
                    resultSet.getString("categoryName"),
                    resultSet.getString("subcategoryName"),
                    resultSet.getString("colorName"),
                    resultSet.getDouble("width"),
                    resultSet.getDouble("height"),
                    resultSet.getDouble("length"),
                    resultSet.getInt("shelf"),
                    resultSet.getInt("regal"),
                    resultSet.getInt("stock"));
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
    //Metoda ustawiająca tytułowy label - edycja/dodawanie
    void setLabelINFO(String labelText)
    {
        labelINFO.setText(labelText);
    }

}

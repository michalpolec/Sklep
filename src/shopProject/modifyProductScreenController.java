package shopProject;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;

public class modifyProductScreenController {

    private Product selectedProduct;
    boolean AddOrEdit;
    public Image imageFromFile;

    ObservableList<restOfElements> room =  FXCollections.observableArrayList();
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

    public ComboBox roomBox;
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
    public Button materialButton;
    public Button dimensionsButton;
    public Button positionButton;
    public Button addProductToDB;
    public Button cancelButton;
    public Button imageButton;

    public Label labelINFO;

   public void initialize() throws SQLException, ClassNotFoundException {

       subcategoryBox.setDisable(true);
       getDataToArrays();

       priceField.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue,
                               String newValue) {
               if (!newValue.matches("\\d*")) {
                   priceField.setText(newValue.replaceAll("[^\\d]", ""));
               }
           }
       });

       stockField.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue,
                               String newValue) {
               if (!newValue.matches("\\d*")) {
                   stockField.setText(newValue.replaceAll("[^\\d]", ""));
               }
           }
       });

   }

    public void initializeData() throws SQLException, ClassNotFoundException, IOException {

       nameField.setText(selectedProduct.getNameOfProduct());
       priceField.setText(String.valueOf(selectedProduct.getPrice()));
       descriptionField.setText(selectedProduct.getDescription());
       stockField.setText(String.valueOf(selectedProduct.getStock()));
       roomBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getRoom(),getStringArray(room)));
       categoryBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getCategory(), getStringArray(categories)));
        setCorrectSubcategories();
        subcategoryBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getSubcategory(), getStringArray(subcategory)));
       colorBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getColor(), getStringArray(colors)));
       materialBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getMaterial(), getStringArray(materials)));
       dimensionsBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getWidth() + "cm x " + selectedProduct.getHeight() + "cm x " + selectedProduct.getLength() + "cm",getStringArray(dimensions)));
       positionBox.getSelectionModel().select(getIndexToComboBox("Półka: " + selectedProduct.getShelf() + ", Regał: " + selectedProduct.getRegal(), getStringArray(positions)));

        InputStream in = selectedProduct.getImage().getBinaryStream(1, (int) selectedProduct.getImage().length());
        Image image = new Image(in);

        imageView.setImage(image);

    }

    public void onCancelAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

   public void modifyDatabaseButtonPressed() throws ClassNotFoundException, SQLException, IOException {

       String productName = "";
       double productPrice = 0.0;
       String productDescription = "";
       int roomID = 0;
       int subcategoryID = 0;
       int colorID = 0;
       int materialID = 0;
       int dimensionID = 0;
       int positionID = 0;
       int productStock = 0;
       InputStream image = null;
       boolean Continue = true;

       try {
           productName = nameField.getText();
           productPrice = Double.parseDouble(priceField.getText());
           productDescription = descriptionField.getText();
           roomID = getIDofElement(roomBox.getValue().toString(), room);
           subcategoryID = getIDofElementForSubcategory(subcategoryBox.getValue().toString(), subcategory);
           colorID = getIDofElement(colorBox.getValue().toString(), colors);
           materialID = getIDofElement(materialBox.getValue().toString(), materials);
           dimensionID = getIDofElementForDimension(dimensionsBox.getValue().toString(), dimensions);
           positionID = getIDofElementForPosition(positionBox.getValue().toString(), positions);
           productStock = Integer.parseInt(stockField.getText());


           BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
           ByteArrayOutputStream s = new ByteArrayOutputStream();
           ImageIO.write(bImage, "png", s);
           byte[] res  = s.toByteArray();
           image = new ByteArrayInputStream(res);
       }
       catch (Exception e){
         System.out.println(e);
         Alert("Błąd wprowadzania danych", "Wprowadzono niepoprawne dane lub nie wprowadzono ich wcale.");
         Continue = false;
       }

       if(AddOrEdit && Continue) {

           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
           Statement statement = connection.createStatement();


           String sql = "SELECT max(IDProduktu) FROM sklep.produkty";
           int greatestID = 0;
           ResultSet resultSet = statement.executeQuery(sql);

           while(resultSet.next()) {
               greatestID = resultSet.getInt("max(IDProduktu)") + 1;
           }



           String sql_increment1 = "ALTER TABLE `sklep`.`produkty` AUTO_INCREMENT = " + greatestID + ";";
           statement.executeUpdate(sql_increment1);
           String sql_increment2 = "ALTER TABLE `sklep`.`szczegoly` AUTO_INCREMENT = " + greatestID + ";";
           statement.executeUpdate(sql_increment2);

           try {

               String sql_details = "INSERT INTO szczegoly ( IDPozycji, IDWymiarow, IDMaterialu, IDKoloru) VALUES ('" + positionID + "', '" + dimensionID + "', '" + materialID + "', '" + colorID + "');";
               statement.executeUpdate(sql_details);

               String sql_products = "INSERT INTO produkty (NazwaProduktu, CenaProduktu, OpisProduktu, IDPomieszczenia, IDPodkategorii, StanMagazynowy, Zdjecie) VALUES (?,?,?,?,?,?,?);";

               PreparedStatement preparedStatement = connection.prepareStatement(sql_products);
               preparedStatement.setString(1,productName);
               preparedStatement.setDouble(2,productPrice);
               preparedStatement.setString(3, productDescription);
               preparedStatement.setInt(4, roomID);
               preparedStatement.setInt(5, subcategoryID);
               preparedStatement.setInt(6, productStock);
               preparedStatement.setBinaryStream(7, image);

               preparedStatement.executeUpdate();


               setSelectedProductFromDB(0);

               preparedStatement.close();
               statement.close();
               connection.close();

           }
           catch (Exception e){
               System.out.println(e);
           }

           Info("Wpisano nowy produkt do bazy", "Poprawnie dodano nowy element.");

           Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
           closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
           closeLoginStage.close();
       }
       else if (Continue){

           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
           Statement statement = connection.createStatement();

           int IDProduct = selectedProduct.getProductID();

           String sql_details = "UPDATE `sklep`.`szczegoly` SET `IDPozycji` = '"+ positionID +"', `IDWymiarow` = '"+ dimensionID +"', `IDMaterialu` = '"+ materialID +"', `IDKoloru` = '"+ colorID +"' WHERE (`IDProduktu` = '"+ IDProduct +"');";
           statement.executeUpdate(sql_details);

           String sql_products = "UPDATE `sklep`.`produkty` SET NazwaProduktu = ?, CenaProduktu = ?, OpisProduktu = ?, IDPomieszczenia = ?, IDPodkategorii = ?, StanMagazynowy = ?, Zdjecie = ? WHERE (IDProduktu = ?);";
           PreparedStatement preparedStatement = connection.prepareStatement(sql_products);

           preparedStatement.setString(1,productName);
           preparedStatement.setDouble(2,productPrice);
           preparedStatement.setString(3, productDescription);
           preparedStatement.setInt(4, roomID);
           preparedStatement.setInt(5, subcategoryID);
           preparedStatement.setInt(6, productStock);
           preparedStatement.setBinaryStream(7, image);
           preparedStatement.setInt(8, IDProduct);
           preparedStatement.executeUpdate();

           setSelectedProductFromDB(IDProduct);

           preparedStatement.close();
           statement.close();
           connection.close();

           Info("Edytowano nowy produkt do bazy", "Poprawnie edytowno element.");

           Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
           closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
           closeLoginStage.close();


       }
   }

   public void addRoom() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego pomieszczenia", "NazwaPomieszczenia", "pomieszczenie",  "Wpisz nowe pomieszczenie");

   }

   public void addCategory() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowej kategorii", "NazwaKategorii", "kategoria",  "Wpisz nową kategorie" );
   }

   public void addSubcategory() throws IOException, SQLException, ClassNotFoundException {

        openSubcategoryScreen();
   }

    public void chosenCategory() {

        setCorrectSubcategories();

    }

   public void addColor() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego koloru" , "NazwaKoloru", "kolor",  "Wpisz nowy kolor");
   }

   public void addMaterial() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego materiału" ,  "NazwaMaterialu", "material", "Wpisz nowy materiał");
   }

    public void addDimensions(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        openDimensionScreen();
    }

    public void addPosition() throws IOException, SQLException, ClassNotFoundException {

       openPositionScreen();
    }

    public void getImageFromFile(ActionEvent actionEvent) throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj nowe zdjęcie");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki obrazów", "*.jpg", "*.png")
        );
        Stage stage = (Stage) imageButton.getScene().getWindow();

        File file = fileChooser.showOpenDialog(null);

        FileInputStream in =  new FileInputStream(file.getPath());
        imageFromFile = new Image(in);
        imageView.setImage(imageFromFile);
    }

   public void openElementScreen(String nameOfStage, String nameOfFirstColumn, String nameOfTabel, String textOfLabel) throws IOException, SQLException, ClassNotFoundException {

       Stage addElement =  new Stage();
       addElement.setTitle(nameOfStage);
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addElementScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addElement.setScene(new Scene(root));

       addElementScreenController newController = loader.getController();
       newController.setOptionOfScreen(nameOfFirstColumn, nameOfTabel,  textOfLabel);

       addElement.show();
       addElement.setOnCloseRequest(new EventHandler<WindowEvent>() {
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

   public void openSubcategoryScreen() throws IOException, SQLException, ClassNotFoundException {

       Stage addSubcategory =  new Stage();
       addSubcategory.setTitle("Dodawnie nowej podkategorii");
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addSubcategoryScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addSubcategory.setScene(new Scene(root));

       addSubcategoryScreenController newController = loader.getController();
       newController.setOptionOfSubcategoryScreen("NazwaPodkategorii", "IDKategorii", "podkategoria",  categories, "Wybierz kategorie oraz wpisz nową podkategorie");
       newController.setCategoriesInComboBox();

       addSubcategory.show();
       addSubcategory.setOnCloseRequest(new EventHandler<WindowEvent>() {
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

   public void openDimensionScreen() throws IOException, SQLException, ClassNotFoundException {

       Stage addDimension =  new Stage();
       addDimension.setTitle("Dodawnie nowego wymiaru");
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addDimensionScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addDimension.setScene(new Scene(root));

       addDimensionScreenController newController = loader.getController();
       newController.setOptionOfDimensionScreen("Szerokosc", "Wysokosc","Dlugosc", "wymiary",  "Wpisz szerokość, długość oraz wysokość produktu");


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

   public void openPositionScreen() throws IOException, SQLException, ClassNotFoundException {

       Stage addPosition =  new Stage();
       addPosition.setTitle("Dodawnie nowej pozycji");
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addPositionScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addPosition.setScene(new Scene(root));

       addPositionScreenController newController = loader.getController();
       newController.setOptionOfPositionScreen( "Polka", "Regal", "pozycja", "Wpisz półkę oraz regał");


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


   public void getSubcategoryData() throws ClassNotFoundException, SQLException {

       Class.forName("com.mysql.cj.jdbc.Driver");
       Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
       Statement statement = connection.createStatement();


       String sql = "SELECT * FROM sklep.podkategoria";
       ResultSet resultSet = statement.executeQuery(sql);

       while(resultSet.next()) {
            Subcategory subcategory = new Subcategory(Integer.parseInt(resultSet.getString("IDPodkategorii")),resultSet.getString("NazwaPodkategorii"),Integer.parseInt(resultSet.getString("IDKategorii")));
            subcategories.add(subcategory);
       }
       statement.close();
       connection.close();
   }

    public void getChosenDataFromDB(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, ObservableList<restOfElements> litsOfData) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM sklep." + nameOfTable;
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {
            restOfElements table = new restOfElements(Integer.parseInt(resultSet.getString(nameOfFirstColumn)),
                    resultSet.getString(nameOfSecondColumn));
            litsOfData.add(table);
        }
        statement.close();
        connection.close();

    }

    public void getDimensionData() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM sklep.wymiary";
        ResultSet resultSet = statement.executeQuery(sql);


        while(resultSet.next()) {

            Dimension dimension = new Dimension(Integer.parseInt(resultSet.getString("IDWymiarow")), (int) Double.parseDouble(resultSet.getString("Szerokosc")), (int) Double.parseDouble(resultSet.getString("Wysokosc")), (int) Double.parseDouble(resultSet.getString("Dlugosc")));
            dimensions.add(dimension);
        }
        statement.close();
        connection.close();
    }

    public void getPositionData() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM sklep.pozycja";
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {

            Position position = new Position(Integer.parseInt(resultSet.getString("IDPozycji")), Integer.parseInt(resultSet.getString("Polka")), Integer.parseInt(resultSet.getString("Regal")));
            positions.add(position);
        }

        statement.close();
        connection.close();

    }


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

        roomBox.setItems(room);
        categoryBox.setItems(categories);
        subcategoryBox.setItems(subcategory);
        colorBox.setItems(colors);
        materialBox.setItems(materials);
        dimensionsBox.setItems(dimensions);
        positionBox.setItems(positions);

    }

    public void setAddOrEdit(boolean AddOrEdit){
       this.AddOrEdit = AddOrEdit;
    }

    public ObservableList<String> getStringArray(ObservableList objects) {

        ObservableList<String> stringArray = FXCollections.observableArrayList();;
        for(Object testObject: objects)
        {

            stringArray.add(testObject.toString());

        }

        return stringArray;

    }

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

    public void setSelectedProduct(Product selectedProduct) throws SQLException, ClassNotFoundException {
        this.selectedProduct = selectedProduct;
    }

    public Product getSelectedProduct(){
        return selectedProduct;
    }

    public void setSelectedProductFromDB(int IDofProduct) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();
        String sql_getID = "";
        if(AddOrEdit) {

            sql_getID   = "SELECT produkty.IDProduktu, NazwaProduktu, CenaProduktu, OpisProduktu, pomieszczenie.NazwaPomieszczenia, kategoria.NazwaKategorii, \n" +
                    "podkategoria.NazwaPodkategorii, kolor.NazwaKoloru, material.NazwaMaterialu, wymiary.Szerokosc, wymiary.Wysokosc, wymiary.Dlugosc,\n" +
                    " pozycja.Polka, pozycja.Regal, StanMagazynowy, Zdjecie\n" +
                    "FROM ((((((((produkty INNER JOIN szczegoly ON produkty.IDProduktu = szczegoly.IDProduktu)\n" +
                    "INNER JOIN pomieszczenie ON produkty.IDPomieszczenia = pomieszczenie.IDPomieszczenia)\n" +
                    "INNER JOIN podkategoria ON produkty.IDPodkategorii = podkategoria.IDPodkategorii)\n" +
                    "INNER JOIN kategoria ON podkategoria.IDKategorii = kategoria.IDKategorii)\n" +
                    "INNER JOIN kolor ON szczegoly.IDKoloru = kolor.IDKoloru)\n" +
                    "INNER JOIN material ON szczegoly.IDMaterialu = material.IDMaterialu)\n" +
                    "INNER JOIN wymiary ON szczegoly.IDWymiarow = wymiary.IDWymiarow)\n" +
                    "INNER JOIN pozycja ON szczegoly.IDPozycji = pozycja.IDPozycji) WHERE produkty.IDProduktu = (SELECT MAX(produkty.IDProduktu) FROM produkty);";

        }
        else {

            sql_getID = "SELECT produkty.IDProduktu, NazwaProduktu, CenaProduktu, OpisProduktu, pomieszczenie.NazwaPomieszczenia, kategoria.NazwaKategorii, \n" +
                    "podkategoria.NazwaPodkategorii, kolor.NazwaKoloru, material.NazwaMaterialu, wymiary.Szerokosc, wymiary.Wysokosc, wymiary.Dlugosc,\n" +
                    " pozycja.Polka, pozycja.Regal, StanMagazynowy, Zdjecie\n" +
                    "FROM ((((((((produkty INNER JOIN szczegoly ON produkty.IDProduktu = szczegoly.IDProduktu)\n" +
                    "INNER JOIN pomieszczenie ON produkty.IDPomieszczenia = pomieszczenie.IDPomieszczenia)\n" +
                    "INNER JOIN podkategoria ON produkty.IDPodkategorii = podkategoria.IDPodkategorii)\n" +
                    "INNER JOIN kategoria ON podkategoria.IDKategorii = kategoria.IDKategorii)\n" +
                    "INNER JOIN kolor ON szczegoly.IDKoloru = kolor.IDKoloru)\n" +
                    "INNER JOIN material ON szczegoly.IDMaterialu = material.IDMaterialu)\n" +
                    "INNER JOIN wymiary ON szczegoly.IDWymiarow = wymiary.IDWymiarow)\n" +
                    "INNER JOIN pozycja ON szczegoly.IDPozycji = pozycja.IDPozycji) WHERE produkty.IDProduktu = " + IDofProduct + ";";
        }

        ResultSet resultSet = statement.executeQuery(sql_getID);


        while(resultSet.next()) {


            selectedProduct = new Product(
                    resultSet.getInt("IDProduktu"),
                    resultSet.getString("NazwaProduktu"),
                    resultSet.getDouble("CenaProduktu"),
                    resultSet.getString("OpisProduktu"),
                    resultSet.getString("NazwaPomieszczenia"),
                    resultSet.getString("NazwaKategorii"),
                    resultSet.getString("NazwaPodkategorii"),
                    resultSet.getString("NazwaKoloru"),
                    resultSet.getString("NazwaMaterialu"),
                    resultSet.getDouble("Szerokosc"),
                    resultSet.getDouble("Wysokosc"),
                    resultSet.getDouble("Dlugosc"),
                    resultSet.getInt("Polka"),
                    resultSet.getInt("Regal"),
                    resultSet.getInt("StanMagazynowy"),
                    resultSet.getBlob("Zdjecie"));
        }
        statement.close();
        connection.close();
    }


    public void Info(String titleOfInfo, String contentOfInfo){
        javafx.scene.control.Alert nullData = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        nullData.setTitle(titleOfInfo);
        nullData.setHeaderText(null);
        nullData.setContentText(contentOfInfo);

        nullData.showAndWait();
    }

    void Alert(String setTitle, String setContents) {
        Alert badClick = new Alert(Alert.AlertType.ERROR);
        badClick.setTitle(setTitle);
        badClick.setHeaderText(null);
        badClick.setContentText(setContents);

        badClick.showAndWait();
    }

    void setLabelINFO(String labelText)
    {
        labelINFO.setText(labelText);
    }

}

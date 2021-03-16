package shopProject.controllers;


import shopProject.entity.*;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

public class ControllerOfModifyScreen {

    // Incjalizacja zmiennych
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
    public Button materialButton;
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

   //Inicjalizowanie wybranymi danymi podczas edycji produktu
    public void initializeData() throws SQLException, ClassNotFoundException, IOException {

       nameField.setText(selectedProduct.getNameOfProduct());
       priceField.setText(String.valueOf(selectedProduct.getPrice()));
       descriptionField.setText(selectedProduct.getDescription());
       stockField.setText(String.valueOf(selectedProduct.getStock()));
       manufacturerBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getManufacturer(),getStringArray(room)));
       categoryBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getCategory(), getStringArray(categories)));
       setCorrectSubcategories();
        subcategoryBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getSubcategory(), getStringArray(subcategory)));
       colorBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getColor(), getStringArray(colors)));
       dimensionsBox.getSelectionModel().select(getIndexToComboBox(selectedProduct.getWidth() + "cm x " + selectedProduct.getHeight() + "cm x " + selectedProduct.getLength() + "cm",getStringArray(dimensions)));
       positionBox.getSelectionModel().select(getIndexToComboBox("Półka: " + selectedProduct.getShelf() + ", Regał: " + selectedProduct.getRegal(), getStringArray(positions)));


    }

    //Metoda działające na przycisk 'anuluj'
    public void onCancelAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    //Metoda umożliwiająca edytowanie/dodanie produktu - pobiera nowo-wpisane dane i odpowiednio edytuje bądź dodaje jako nowy produkt
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
           roomID = getIDofElement(manufacturerBox.getValue().toString(), room);
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
         Alert("Błąd wprowadzania danych", "Wprowadzono niepoprawne dane lub nie wprowadzono ich wcale.");
         Continue = false;
       }

       if(AddOrEdit && Continue) {

           Class.forName("com.mysql.cj.jdbc.Driver");
           Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
           Statement statement = connection.createStatement();

           boolean continueTypingData = true;

           String sql_getIDfromProducts = "SELECT max(IDProduktu) FROM sklep.produkty";
           int greatestIDofProducts = 0;
           ResultSet resultSet1 = statement.executeQuery(sql_getIDfromProducts);
           while(resultSet1.next()) {
               greatestIDofProducts = resultSet1.getInt("max(IDProduktu)") + 1;
           }

           String sql_getIDfromDetails = "SELECT max(IDProduktu) FROM sklep.szczegoly";
           int greatestIDofDetails = 0;
           ResultSet resultSet2 = statement.executeQuery(sql_getIDfromDetails);
           while(resultSet2.next()) {
               greatestIDofDetails = resultSet2.getInt("max(IDProduktu)") + 1;
           }

           if (greatestIDofProducts == greatestIDofDetails) {

               String sql_increment1 = "ALTER TABLE `sklep`.`produkty` AUTO_INCREMENT = " + greatestIDofProducts + ";";
               statement.executeUpdate(sql_increment1);
               String sql_increment2 = "ALTER TABLE `sklep`.`szczegoly` AUTO_INCREMENT = " + greatestIDofDetails + ";";
               statement.executeUpdate(sql_increment2);

               try {

                   String sql_details = "INSERT INTO szczegoly ( IDPozycji, IDWymiarow, IDMaterialu, IDKoloru) VALUES ('" + positionID + "', '" + dimensionID + "', '" + materialID + "', '" + colorID + "');";
                   statement.executeUpdate(sql_details);
               } catch (SQLException e) {
                   Alert("Błąd polecenia", "Błędne polecenie");
                   continueTypingData = false;
               }

               if (continueTypingData) {

                   try {

                       String sql_products = "INSERT INTO produkty (NazwaProduktu, CenaProduktu, OpisProduktu, IDPomieszczenia, IDPodkategorii, StanMagazynowy, Zdjecie) VALUES (?,?,?,?,?,?,?);";

                       PreparedStatement preparedStatement = connection.prepareStatement(sql_products);
                       preparedStatement.setString(1, productName);
                       preparedStatement.setDouble(2, productPrice);
                       preparedStatement.setString(3, productDescription);
                       preparedStatement.setInt(4, roomID);
                       preparedStatement.setInt(5, subcategoryID);
                       preparedStatement.setInt(6, productStock);
                       preparedStatement.setBinaryStream(7, image);

                       preparedStatement.executeUpdate();

                       setSelectedProductFromDB(0); // Dodwanie do wyświetlanych produktów

                       preparedStatement.close();

                   } catch (SQLException e) {

                       Alert("Błąd bazy danych", "Niezgodność ID produktów");
                       String sql_backroll_details = "DELETE FROM `sklep`.`szczegoly` WHERE (`IDProduktu` = '" + greatestIDofDetails + "')";
                       statement.executeUpdate(sql_backroll_details);

                       statement.close();
                       connection.close();


                   }
               }
               statement.close();
               connection.close();

               Info("Wpisano nowy produkt do bazy", "Poprawnie dodano nowy element.");

               Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
               closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
               closeLoginStage.close();
           }
           else {
               Alert("Błąd bazy danych", "Niezgodność tabel Produkty i Szczegóły");
               Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
               closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
               closeLoginStage.close();
           }

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

   //Metoda działająca na kliknięcie przycisku 'dodaj nowe' pomieszczenie - otwiera nowe okno
   public void addRoom() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego pomieszczenia", "NazwaPomieszczenia", "pomieszczenie",  "Wpisz nowe pomieszczenie");

   }

    //Metoda działająca na kliknięcie przycisku 'dodaj nową' ketgorię - otwiera nowe okno
   public void addCategory() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowej kategorii", "NazwaKategorii", "kategoria",  "Wpisz nową kategorie" );
   }

    //Metoda działająca na kliknięcie przycisku 'dodaj nową' podkategorię - otwiera nowe okno
   public void addSubcategory() throws IOException, SQLException, ClassNotFoundException {

        openSubcategoryScreen();
   }

   //Metoda ustawiająca podaktegorie w zależności od wybranej wcześniej kategorii
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

    //Metoda działająca na kliknięcie przycisku 'dodaj obraz'- otwiera FileChoosera i jest możliwe wybranie obrazu png/jpg
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

    //Ogólna Metoda otwierająca nowe okno w celu dodania nowych danych/szczegółów
   public void openElementScreen(String nameOfStage, String nameOfFirstColumn, String nameOfTabel, String textOfLabel) throws IOException, SQLException, ClassNotFoundException {

       Stage addElement =  new Stage();
       addElement.setTitle(nameOfStage);
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addElementScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addElement.setScene(new Scene(root));

       addElementScreenController newController = loader.getController();
       newController.setOptionOfScreen(nameOfFirstColumn, nameOfTabel,  textOfLabel);

       addElement.setResizable(false);
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

   //Metoda otwierająca nowe okno dodania podakategorii
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

       addSubcategory.setResizable(false);
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

   //Metoda otwierająca nowe okno dodania wymiarów
   public void openDimensionScreen() throws IOException, SQLException, ClassNotFoundException {

       Stage addDimension =  new Stage();
       addDimension.setTitle("Dodawnie nowego wymiaru");
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addDimensionScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addDimension.setScene(new Scene(root));

       addDimensionScreenController newController = loader.getController();
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
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addPositionScreen.fxml"));
       Parent root = loader.load();
       root.getStylesheets().add("Stylesheets/style.css");
       addPosition.setScene(new Scene(root));

       addPositionScreenController newController = loader.getController();
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

    //Metoda pobierająca wszystkie podkategorie z bazy danych
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

   //Metoda pobierająca wybrane dane z bazy danych
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

    //Metoda pobierająca wszystkie wymiary z bazy danych
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

    //Metoda pobierająca wszystkie pozycje z bazy danych
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
        room.clear();
        categories.clear();
        subcategories.clear();
        colors.clear();
        materials.clear();
        dimensions.clear();
        positions.clear();

        //pobieranie danych
        getChosenDataFromDB("IDPomieszczenia", "NazwaPomieszczenia", "pomieszczenie", room);
        getChosenDataFromDB("IDKategorii","NazwaKategorii", "kategoria", categories);
        getSubcategoryData();
        getChosenDataFromDB("IDKoloru", "NazwaKoloru", "kolor", colors);
        getChosenDataFromDB("IDMaterialu","NazwaMaterialu", "material", materials);
        getDimensionData();
        getPositionData();

        //ustawianie danych
        manufacturerBox.setItems(room);
        categoryBox.setItems(categories);
        subcategoryBox.setItems(subcategory);
        colorBox.setItems(colors);
        materialBox.setItems(materials);
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

    //Ogólna metoda informująca
    public void Info(String titleOfInfo, String contentOfInfo){
        javafx.scene.control.Alert nullData = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        nullData.setTitle(titleOfInfo);
        nullData.setHeaderText(null);
        nullData.setContentText(contentOfInfo);

        nullData.showAndWait();
    }

    //Ogólna metoda ostrzegająca
    void Alert(String setTitle, String setContents) {
        Alert badClick = new Alert(Alert.AlertType.ERROR);
        badClick.setTitle(setTitle);
        badClick.setHeaderText(null);
        badClick.setContentText(setContents);

        badClick.showAndWait();
    }

    //Metoda ustawiająca tytułowy label - edycja/dodawanie
    void setLabelINFO(String labelText)
    {
        labelINFO.setText(labelText);
    }

}

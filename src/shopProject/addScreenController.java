package shopProject;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.sql.*;

public class addScreenController {


    ObservableList<restOfElements> room =  FXCollections.observableArrayList();
    ObservableList<restOfElements>  categories = FXCollections.observableArrayList();
    ObservableList<Subcategory> subcategories =  FXCollections.observableArrayList();
    ObservableList<Subcategory>  subcategory = FXCollections.observableArrayList();
    ObservableList<restOfElements> colors =  FXCollections.observableArrayList();
    ObservableList<restOfElements> materials =  FXCollections.observableArrayList();
    ObservableList<Dimension> dimensions = FXCollections.observableArrayList();
    ObservableList<Position> positions = FXCollections.observableArrayList();


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


   public void initialize() throws SQLException, ClassNotFoundException {

       subcategoryBox.setDisable(true);
       getDataToArrays();

   }

   public void addProductToDB() throws ClassNotFoundException, SQLException {


       int positionID = getIDofElementForPosition(positionBox.getValue().toString() , positions);
       int dimensionID = getIDofElementForDimension(dimensionsBox.getValue().toString(), dimensions);
       int materialID = getIDofElement(materialBox.getValue().toString(), materials);
       int colorID = getIDofElement(colorBox.getValue().toString(), colors);

       String productName = nameField.getText();
       double productPrice = Double.parseDouble(priceField.getText());
       String descriptionName = descriptionField.getText();
       int roomID = getIDofElement(roomBox.getValue().toString(), room);
       int subcategoryID = getIDofElementForSubcategory(subcategoryBox.getValue().toString(), subcategories);
       int productStock = Integer.parseInt(stockField.getText());


       Statement statement = createConnectionAndStatement();

       String sql =  "INSERT INTO szczegoly (IDPozycji, IDWymiarow, IDMaterialu, IDKoloru) VALUES ('" + positionID + "', '" + dimensionID + "', '" + materialID + "', '" + colorID + "');";
       createConnectionAndStatement().executeUpdate(sql);
       sql = "INSERT INTO produkty (NazwaProduktu, CenaProduktu, OpisProduktu, IDPomieszczenia, IDPodkategorii, StanMagazynowy) VALUES ('" + productName + "', '" + productPrice + "', '" + descriptionName + "', '" + roomID + "', '" + subcategoryID + "','" +  productStock + "');";
       createConnectionAndStatement().executeUpdate(sql);

       closeStatementAndConnection(statement);

       Info();

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

        subcategory.clear();
        int IDofCategory = getIDofElement(categoryBox.getValue().toString(), categories);


        for(Subcategory sub : subcategories){
            if(sub.getCategoryID() == IDofCategory){

                subcategory.add(sub);
            }
        }
        subcategoryBox.setDisable(false);

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


   public void openElementScreen(String nameOfStage, String nameOfFirstColumn, String nameOfTabel, String textOfLabel) throws IOException, SQLException, ClassNotFoundException {

       Stage addElement =  new Stage();
       addElement.setTitle(nameOfStage);
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addElementScreen.fxml"));
       Parent root = loader.load();
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

   public void getSubcategoryData() throws ClassNotFoundException, SQLException {

       String sql = "SELECT * FROM sklep.podkategoria";
       ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);

       while(resultSet.next()) {
            Subcategory subcategory = new Subcategory(Integer.parseInt(resultSet.getString("IDPodkategorii")),resultSet.getString("NazwaPodkategorii"),Integer.parseInt(resultSet.getString("IDKategorii")));
            subcategories.add(subcategory);
       }

       closeStatementAndConnection(resultSet.getStatement());
   }

    public void getChosenDataFromDB(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, ObservableList<restOfElements> litsOfData) throws ClassNotFoundException, SQLException {


        String sql = "SELECT * FROM sklep." + nameOfTable;
        ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);

        while(resultSet.next()) {
            restOfElements table = new restOfElements(Integer.parseInt(resultSet.getString(nameOfFirstColumn)),
                    resultSet.getString(nameOfSecondColumn));
            litsOfData.add(table);
        }

        closeStatementAndConnection(resultSet.getStatement());

    }

    public void getDimensionData() throws ClassNotFoundException, SQLException {


        String sql = "SELECT * FROM sklep.wymiary";
        ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);


        while(resultSet.next()) {

            Dimension dimension = new Dimension(Integer.parseInt(resultSet.getString("IDWymiarow")), (int) Double.parseDouble(resultSet.getString("Szerokosc")), (int) Double.parseDouble(resultSet.getString("Wysokosc")), (int) Double.parseDouble(resultSet.getString("Dlugosc")));
            dimensions.add(dimension);
        }

        closeStatementAndConnection(resultSet.getStatement());
    }

    public void getPositionData() throws ClassNotFoundException, SQLException {



        String sql = "SELECT * FROM sklep.pozycja";
        ResultSet resultSet = createConnectionAndStatement().executeQuery(sql);

        while(resultSet.next()) {

            Position position = new Position(Integer.parseInt(resultSet.getString("IDPozycji")), Integer.parseInt(resultSet.getString("Polka")), Integer.parseInt(resultSet.getString("Regal")));
            positions.add(position);
        }

        closeStatementAndConnection(resultSet.getStatement());

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

    public void Info(){
        javafx.scene.control.Alert nullData = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        nullData.setTitle("Wpisano nowy produkt do bazy");
        nullData.setHeaderText(null);
        nullData.setContentText("Dodano nowy produkt!");

        nullData.showAndWait();
    }

    public Statement createConnectionAndStatement() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();

        return statement;
    }

    public void closeStatementAndConnection(Statement statement) throws SQLException {

           Connection connection = statement.getConnection();
           statement.close();
           connection.close();

    }

}

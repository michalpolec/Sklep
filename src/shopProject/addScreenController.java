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


    ObservableList<twoColumnTable> room =  FXCollections.observableArrayList();
    ObservableList<twoColumnTable>  categories = FXCollections.observableArrayList();
    ObservableList<twoColumnTable>  category = FXCollections.observableArrayList();
    ObservableList<Subcategory> subcategories =  FXCollections.observableArrayList();
    ObservableList<twoColumnTable> colors =  FXCollections.observableArrayList();
    ObservableList<twoColumnTable> materials =  FXCollections.observableArrayList();
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


       categoryBox.setDisable(true);
       getDataToArrays();

   }

   public void addProductToDB() throws ClassNotFoundException, SQLException {

       int productID = (getMaxIDofProducts() + 1);

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


       Class.forName("com.mysql.cj.jdbc.Driver");
       Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
       Statement statement = connection.createStatement();

       String sql =  "INSERT INTO szczegoly (IDProduktu, IDPozycji, IDWymiarow, IDMaterialu, IDKoloru) VALUES ('" + productID + "', '" + positionID + "', '" + dimensionID + "', '" + materialID + "', '" + colorID + "');";
       statement.executeUpdate(sql);
       sql = "INSERT INTO produkty (IDProduktu, NazwaProduktu, CenaProduktu, OpisProduktu, IDPomieszczenia, IDPodkategorii, StanMagazynowy) VALUES ('" + productID+ "', '" + productName + "', '" + productPrice + "', '" + descriptionName + "', '" + roomID + "', '" + subcategoryID + "','" +  productStock + "');";
       statement.executeUpdate(sql);


       statement.close();
       connection.close();

       Info();





   }

   public void addRoom() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego pomieszczenia", "IDPomieszczenia", "NazwaPomieszczenia", "pomieszczenie", setLastIDofElement(room), "Wpisz nowe pomieszczenie");

   }

   public void addCategory() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowej kategorii", "IDKategorii", "NazwaKategorii", "kategoria",  setLastIDofElement(categories), "Wpisz nową kategorie" );
   }

   public void addSubcategory() throws IOException, SQLException, ClassNotFoundException {

        openSubcategoryScreen();
   }

    public void chosenSubcategory() {

        category.clear();
        categoryBox.setDisable(false);

        for(Subcategory sub : subcategories){
            if(sub.getSubcategoryName().equals(subcategoryBox.getValue().toString())){

                category.add(categories.get(sub.getCategoryID()-1));
            }
        }

    }

   public void addColor() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego koloru" ,"IDKoloru", "NazwaKoloru", "kolor", setLastIDofElement(colors), "Wpisz nowy kolor");
   }

   public void addMaterial() throws IOException, SQLException, ClassNotFoundException {

       openElementScreen("Dodawanie nowego materiału" , "IDMaterialu", "NazwaMaterialu", "material", setLastIDofElement(materials), "Wpisz nowy materiał");
   }

    public void addDimensions(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        openDimensionScreen();
    }

    public void addPosition() throws IOException, SQLException, ClassNotFoundException {

       openPositionScreen();
    }


   public void openElementScreen(String nameOfStage, String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTabel, int lastIDofArray, String textOfLabel) throws IOException, SQLException, ClassNotFoundException {

       Stage addElement =  new Stage();
       addElement.setTitle(nameOfStage);
       FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addElementScreen.fxml"));
       Parent root = loader.load();
       addElement.setScene(new Scene(root));

       addElementScreenController newController = loader.getController();
       newController.setOptionOfScreen(nameOfFirstColumn,nameOfSecondColumn, nameOfTabel, lastIDofArray, textOfLabel);

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
       newController.setOptionOfSubcategoryScreen("IDPodkategorii","NazwaPodkategorii", "IDKategorii", "podkategoria", setLastIDofElementForSubcategory(subcategories), categories, "Wybierz kategorie oraz wpisz nową podkategorie");
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
       newController.setOptionOfDimensionScreen("IDWymiarow","Szerokosc", "Wysokosc","Dlugosc", "wymiary", setLastIDofElementForDimension(dimensions),  "Wpisz szerokość, długość oraz wysokość produktu");


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
       newController.setOptionOfPositionScreen("IDPozycji", "Polka", "Regal", "pozycja",setLastIDofElementForPosition(positions), "Wpisz półkę oraz regał");


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
       categoryBox.setItems(category);
       subcategoryBox.setItems(subcategories);
       colorBox.setItems(colors);
       materialBox.setItems(materials);
       dimensionsBox.setItems(dimensions);
       positionBox.setItems(positions);

   }

   public void getSubcategoryData() throws ClassNotFoundException, SQLException {

       Class.forName("com.mysql.cj.jdbc.Driver");
       Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
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

    public void getChosenDataFromDB(String nameOfFirstColumn, String nameOfSecondColumn, String nameOfTable, ObservableList<twoColumnTable> litsOfData) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM sklep." + nameOfTable;
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {
            twoColumnTable table = new twoColumnTable(Integer.parseInt(resultSet.getString(nameOfFirstColumn)),
                    resultSet.getString(nameOfSecondColumn));
            litsOfData.add(table);
        }

        statement.close();
        connection.close();


    }

    public void getDimensionData() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM sklep.wymiary";
        ResultSet resultSet = statement.executeQuery(sql);


        int width;
        int height;
        int length;

        while(resultSet.next()) {

            width = (int) Double.parseDouble(resultSet.getString("Szerokosc"));
            height = (int) Double.parseDouble(resultSet.getString("Wysokosc"));
            length = (int) Double.parseDouble(resultSet.getString("Dlugosc"));


            Dimension dimension = new Dimension(Integer.parseInt(resultSet.getString("IDWymiarow")), width, height, length);
            dimensions.add(dimension);
        }

        statement.close();
        connection.close();
    }

    public void getPositionData() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
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

    public int setLastIDofElementForSubcategory(ObservableList<Subcategory> elements) {
        int i = 0;
        int maxID = 0;
        for(Subcategory sub: elements){
            if(sub.getSubcategoryID() > i)
            {
                maxID = sub.getSubcategoryID();
            }
            i++;
        }
        return maxID;
    }

    public int setLastIDofElement(ObservableList<twoColumnTable> elements){

       int i = 0;
       int maxID = 0;
       for(twoColumnTable table: elements){
           if(table.getID() > i)
           {
               maxID = table.getID();
           }
           i++;
       }
       return maxID;
    }

    public int setLastIDofElementForDimension(ObservableList<Dimension> dimensions){
        int i = 0;
        int maxID = 0;
        for(Dimension dim: dimensions){
            if(dim.getDimensionID() > i)
            {
                maxID = dim.getDimensionID();
            }
            i++;
        }
        return maxID;
    }

    public int setLastIDofElementForPosition(ObservableList<Position> positions){
        int i = 0;
        int maxID = 0;
        for(Position pos: positions){
            if(pos.getPositionID() > i)
            {
                maxID = pos.getPositionID();
            }
            i++;
        }
        return maxID;
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

    public int getIDofElement(String nameOfElement, ObservableList<twoColumnTable> listsOfElements) {

        int ID = 0;
        for(twoColumnTable element : listsOfElements)
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

    public int getMaxIDofProducts() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "niemamsil");
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM sklep.produkty";
        ResultSet resultSet = statement.executeQuery(sql);

        int index = 0;
        int ID = 0;

        while(resultSet.next()) {

            int currentID = Integer.parseInt(resultSet.getString("IDProduktu"));
            if(currentID > index)
            {
                ID =  currentID;
            }
        }
        statement.close();
        connection.close();

        return ID;
    }

    public void Info(){
        javafx.scene.control.Alert nullData = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        nullData.setTitle("Wpisano nowy produkt do bazy");
        nullData.setHeaderText(null);
        nullData.setContentText("Dodano nowy produkt!");

        nullData.showAndWait();
    }
}

package shopProject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import shopProject.entity.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.Optional;

public class ControllerOfModifyScreen {

    boolean AddOrEdit;

    private ObservableList<Elements> manufacturers = FXCollections.observableArrayList();
    private ObservableList<Elements>  categories = FXCollections.observableArrayList();
    private final ObservableList<Subcategory> subcategories = FXCollections.observableArrayList();
    private final ObservableList<Subcategory> subcategoriesFromSelectedCategory = FXCollections.observableArrayList();
    private ObservableList<Elements> colors = FXCollections.observableArrayList();
    private final ObservableList<Dimension> dimensions = FXCollections.observableArrayList();
    private final ObservableList<Position> positions = FXCollections.observableArrayList();

    public ImageView imageView;

    private Image imageFromFile;

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

    public Button manufacturerButton;
    public Button subcategoryButton;
    public Button categoryButton;
    public Button colorButton;
    public Button dimensionsButton;
    public Button positionButton;
    public Button addProductToDB;
    public Button cancelButton;
    public Button imageButton;

    public Label labelINFO;

    private Product selectedProduct;

    private boolean clearComboBoxOrNot = false;

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Product getSelectedProduct(){
        return selectedProduct;
    }

    public void initialize() throws SQLException, ClassNotFoundException {
        subcategoryBox.setDisable(true);
        getDataToArrays();
        setTextAsOnlyNumbers(priceField);
        setTextAsOnlyNumbers(stockField);
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
        dimensions.clear();
        positions.clear();
    }

    private void getAllDataFromDBToLists() throws ClassNotFoundException, SQLException {

        getRegularDataFromDatabase();
        getSubcategoryDataFromDatabase();
        getDimensionDataFromDatabase();
        getPositionDataFromDatabase();
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

    private ObservableList<Elements> getElementsFromDatabase(ResultSet resultSet, String nameOfFirstColumn, String nameOfSecondColumn) throws SQLException {
        ObservableList<Elements> elements =  FXCollections.observableArrayList();
        while(resultSet.next()) {
            Elements singleElement = new Elements(Integer.parseInt(resultSet.getString(nameOfFirstColumn)),
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

    public void getDimensionDataFromDatabase() throws ClassNotFoundException, SQLException {

        Connection connection = createConnection();
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM hurtownia.dimension";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()) {

            Dimension dimension = new Dimension(Integer.parseInt(resultSet.getString("dimensionID")),
                    (int) Double.parseDouble(resultSet.getString("width")),
                    (int) Double.parseDouble(resultSet.getString("height")),
                    (int) Double.parseDouble(resultSet.getString("length")));
            dimensions.add(dimension);
        }
        statement.close();
        connection.close();

    }

    public void getPositionDataFromDatabase() throws ClassNotFoundException, SQLException {

        Connection connection = createConnection();
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM hurtownia.positions";
        ResultSet resultSet = statement.executeQuery(sql);

        while(resultSet.next()) {

            Position position = new Position(Integer.parseInt(resultSet.getString("positionsID")),
                    Integer.parseInt(resultSet.getString("shelf")),
                    Integer.parseInt(resultSet.getString("regal")));
            positions.add(position);
        }

        statement.close();
        connection.close();

    }

    private void setItemsToAllComboBoxes() {
        manufacturerBox.setItems(manufacturers);
        categoryBox.setItems(categories);
        subcategoryBox.setItems(subcategoriesFromSelectedCategory);
        colorBox.setItems(colors);
        dimensionsBox.setItems(dimensions);
        positionBox.setItems(positions);
    }

    private void setTextAsOnlyNumbers(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void initializeData() throws SQLException {

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

        InputStream input = selectedProduct.getImage().getBinaryStream(1, (int) selectedProduct.getImage().length());
        Image image = new Image(input);
        imageView.setImage(image);
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
        int positionsID = 0;
        int productStock = 0;
        InputStream image = null;
        boolean Continue = true;

        try {
            productName = nameField.getText();
            productPrice = Double.parseDouble(priceField.getText());
            productDescription = descriptionField.getText();
            manufacturerID = getIDofElement(manufacturerBox.getValue().toString(), manufacturers);
            subcategoryID = getIDofElementForSubcategory(subcategoryBox.getValue().toString(), subcategoriesFromSelectedCategory);
            colorID = getIDofElement(colorBox.getValue().toString(), colors);
            dimensionID = getIDofElementForDimension(dimensionsBox.getValue().toString(), dimensions);
            positionsID = getIDofElementForPosition(positionBox.getValue().toString(), positions);
            productStock = Integer.parseInt(stockField.getText());

            BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", s);
            byte[] res  = s.toByteArray();
            image = new ByteArrayInputStream(res);

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

                String sql_details = "INSERT INTO details ( positionsID, dimensionID, colorID) VALUES ('" + positionsID + "', '" + dimensionID + "', '" + colorID + "');";
                statement.executeUpdate(sql_details);
            } catch (SQLException e) {
                Alert("Błąd polecenia", "Błędne polecenie", Alert.AlertType.ERROR).showAndWait();
                continueTypingData = false;
            }

            if (continueTypingData) {

               try {

                    String sql_products = "INSERT INTO `hurtownia`.`product` (productName, productPrice, productDescription,subcategoryID, detailsID, manufacturerID, stock, image) VALUES (?,?,?,?,?,?,?,?);";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql_products);
                    preparedStatement.setString(1, productName);
                    preparedStatement.setDouble(2, productPrice);
                    preparedStatement.setString(3, productDescription);
                    preparedStatement.setInt(4, subcategoryID);
                    preparedStatement.setInt(5, getIDofDetails(statement));
                    preparedStatement.setInt(6, manufacturerID);
                    preparedStatement.setInt(7, productStock);
                    preparedStatement.setBinaryStream(8, image);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    setSelectedProductFromDB(0);

               } catch (SQLException e) {

                    Alert("Błąd bazy danych", "", Alert.AlertType.ERROR).showAndWait();
                    statement.close();
                    connection.close();
                }
            }
            statement.close();
            connection.close();

            Alert("Nowy produkt", "Poprawnie dodano nowy produkt do bazy danych.", Alert.AlertType.INFORMATION).showAndWait();

            Stage closeLoginStage = (Stage) positionBox.getScene().getWindow();
            closeLoginStage.getOnCloseRequest().handle(new WindowEvent(closeLoginStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            closeLoginStage.close();


        }
        else if (Continue){


            Connection connection = createConnection();
            Statement statement = connection.createStatement();

            productID = getSelectedProduct().getProductID();
            detailsID = getSelectedProduct().getDetailsID();

            String sql_details = "UPDATE `hurtownia`.`details` SET `positionsID` = '"+ positionsID +"', `dimensionID` = '"+ dimensionID + "', `colorID` = '"+ colorID +"' WHERE (`detailsID` = '"+ detailsID +"');";
            statement.executeUpdate(sql_details);

            String sql_products = "UPDATE `hurtownia`.`product` SET productName = ?, productPrice = ?, productDescription = ?, manufacturerID = ?, subcategoryID = ?, detailsID = ?, stock = ?, image = ? WHERE (productID = '"+ productID +"');";

            PreparedStatement preparedStatement = connection.prepareStatement(sql_products);

            preparedStatement.setString(1, productName);
            preparedStatement.setDouble(2, productPrice);
            preparedStatement.setString(3, productDescription);
            preparedStatement.setInt(4, manufacturerID);
            preparedStatement.setInt(5, subcategoryID);
            preparedStatement.setInt(6, getIDofDetails(statement));
            preparedStatement.setInt(7, productStock);
            preparedStatement.setBinaryStream(8, image);

            preparedStatement.executeUpdate();

            setSelectedProductFromDB(productID);

            preparedStatement.close();
            statement.close();
            connection.close();

            Alert("Edycja produktu", "Poprawnie edytowano element.", Alert.AlertType.INFORMATION).showAndWait();

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

    public void addManufacturer() throws IOException {

        openElementScreen("Dodawanie nowego producenta", "manufacturerName", "manufacturer",  "Wpisz nowego producenta do bazy:");

    }

    public void addCategory() throws IOException {

        openElementScreen("Dodawanie nowej kategorii", "categoryName", "category",  "Wpisz nową kategorie:" );
    }

    public void addSubcategory() throws IOException {

        openSubcategoryScreen();
    }

    public void chosenCategory() {

        if(!clearComboBoxOrNot){
            setCorrectSubcategoriesFromSelectedCategory();}

        clearComboBoxOrNot = false;

    }

    public void addColor() throws IOException {

        openElementScreen("Dodawanie nowego koloru" , "colorName", "color",  "Wpisz nowy kolor:");
    }

    public void addDimensions() throws IOException {

        openDimensionScreen();
    }

    public void addPosition() throws IOException {
        openPositionScreen();
    }

    public void getImageFromFile(ActionEvent actionEvent) throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj nowe zdjęcię");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki obrazów", "*.jpg", "*.png")
        );
        Stage stage = (Stage) imageButton.getScene().getWindow();

        File file = fileChooser.showOpenDialog(null);

        FileInputStream input =  new FileInputStream(file.getPath());
        imageFromFile = new Image(input);
        imageView.setImage(imageFromFile);
    }

    public void openElementScreen(String nameOfStage, String nameOfFirstColumn, String nameOfTabel, String textOfLabel) throws IOException {

        FXMLLoader loader = getFxmlLoader("AddElementScreen.fxml");
        Parent root = getRoot(loader);
        ControllerOfAddingNewElementToDB newController = loader.getController();
        newController.setOptionOfScreen(nameOfFirstColumn, nameOfTabel,  textOfLabel);
        Stage newAddStage = CreateNewAddStage();
        customizeStage(nameOfStage, root, newAddStage);
        newAddStage.setOnCloseRequest(we -> {
            try {
                getDataToArrays();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void openSubcategoryScreen() throws IOException {

        FXMLLoader loader = getFxmlLoader("AddSubcategoryScreen.fxml");
        Parent root = getRoot(loader);
        ControllerOFAddingNewSubcategoryToDB newController = loader.getController();
        newController.setOptionOfSubcategoryScreen( categories, "Wybierz kategorie oraz wpisz nową podkategorie");
        Stage newAddStage = CreateNewAddStage();
        clearComboBoxOrNot = true;
        customizeStage("Dodawnie nowej podkategorii", root, newAddStage);
        newAddStage.setOnCloseRequest(we -> {
            try {
                getDataToArrays();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void openDimensionScreen() throws IOException {

        FXMLLoader loader = getFxmlLoader("AddDimensionScreen.fxml");
        Parent root = getRoot(loader);
        ControllerOfAddingNewDimensionToDB newController = loader.getController();
        newController.setOptionOfDimensionScreen("Wpisz szerokość, długość oraz wysokość produktu");
        Stage newAddStage = CreateNewAddStage();
        customizeStage("Dodawnie nowego wymiaru", root, newAddStage);
        newAddStage.setOnCloseRequest(we -> {
            try {
                getDataToArrays();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void openPositionScreen() throws IOException {

        FXMLLoader loader = getFxmlLoader("AddPositionScreen.fxml");
        Parent root = getRoot(loader);
        ControllerOfAddingNewPositionToDB newController = loader.getController();
        newController.setOptionOfPositionScreen("Wpisz półkę oraz regał");
        Stage newAddStage = CreateNewAddStage();
        customizeStage("Dodawnie nowej pozycji", root, newAddStage);
        newAddStage.setOnCloseRequest(we -> {
            try {
                getDataToArrays();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    public void deleteManufacturer() throws SQLException, ClassNotFoundException {

        deleteElementFromDatabase(getIDofElement(manufacturerBox.getValue().toString(), manufacturers), "manufacturer");
        manufacturerBox.getSelectionModel().clearSelection();
        getDataToArrays();
    }

    public void deleteCategory() throws SQLException, ClassNotFoundException {

        deleteElementFromDatabase(getIDofElement(categoryBox.getValue().toString(), categories), "category");
        categoryBox.getSelectionModel().clearSelection();
        getDataToArrays();
    }

    public void deleteSubcategory() throws SQLException, ClassNotFoundException {

        deleteElementFromDatabase(getIDofElementForSubcategory(subcategoryBox.getValue().toString(), subcategories), "subcategory");
        subcategoryBox.getSelectionModel().clearSelection();
        clearComboBoxOrNot = true;
        subcategoryBox.setDisable(true);
        getDataToArrays();
    }

    public void deleteColor() throws SQLException, ClassNotFoundException {

        deleteElementFromDatabase(getIDofElement(colorBox.getValue().toString(), colors), "color");
        colorBox.getSelectionModel().clearSelection();
        getDataToArrays();
    }

    public void deleteDimension() throws SQLException, ClassNotFoundException {

        deleteElementFromDatabase(getIDofElementForDimension(dimensionsBox.getValue().toString(), dimensions), "dimension");
        dimensionsBox.getSelectionModel().clearSelection();
        getDataToArrays();
    }

    public void deletePosition() throws SQLException, ClassNotFoundException {

        deleteElementFromDatabase(getIDofElementForPosition(positionBox.getValue().toString(), positions), "positions");
        positionBox.getSelectionModel().clearSelection();
        getDataToArrays();
    }


    private void deleteElementFromDatabase(int IDofElement, String nameOfTable) throws ClassNotFoundException, SQLException {

        Alert deleteClick = Alert( "Usuwanie elementu", "Czy na pewno usunąć element?", Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> action = deleteClick.showAndWait();
        if(action.get() == ButtonType.OK) {

            Connection connection = createConnection();
            Statement statement = connection.createStatement();

            String sql = "DELETE FROM `hurtownia`.`" + nameOfTable + "` WHERE (`" + nameOfTable + "ID` = '" + IDofElement + "')";
            statement.executeUpdate(sql);

            statement.close();
            connection.close();

        }
    }


    private Stage CreateNewAddStage() {
        return new Stage();

    }

    private void customizeStage(String title, Parent root, Stage newModifyStage) {
        newModifyStage.setTitle(title);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Stylesheets/mainStyle.css");
        newModifyStage.setScene(scene);
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

    public int getIDofElement(String nameOfElement, ObservableList<Elements> listsOfElements) {

        int ID = 0;
        for(Elements element : listsOfElements)
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


    public void setAddOrEdit(boolean AddOrEdit){
        this.AddOrEdit = AddOrEdit;
    }

    public ObservableList<String> getStringArray(ObservableList objects) {

        ObservableList<String> stringArray = FXCollections.observableArrayList();
        for(Object testObject: objects)
        {

            stringArray.add(testObject.toString());

        }

        return stringArray;

    }

    public void setSelectedProductFromDB(int IDofProduct) throws SQLException, ClassNotFoundException {
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        String sql_getID;
        if(AddOrEdit) {

            sql_getID   = """
                    SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName,\s
                    subcategory.subcategoryName, subcategory.categoryID, product.detailsID, product.manufacturerID, product.subcategoryID, details.colorID, details.dimensionID, details.positionsID, color.colorName, dimension.width, dimension.height, dimension.length,
                    positions.shelf, positions.regal, stock, image
                    FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)
                    INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)
                    INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)
                    INNER JOIN category ON subcategory.categoryID = category.categoryID)
                    INNER JOIN color ON details.colorID = color.colorID)
                    INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)
                    INNER JOIN positions ON details.positionsID = positions.positionsID) WHERE product.productID = (SELECT MAX(product.productID) FROM product);""";

        }
        else {

            sql_getID = "SELECT productID, productName, productPrice, productDescription, manufacturer.manufacturerName, category.categoryName, \n" +
                    "subcategory.subcategoryName,  subcategory.categoryID, product.detailsID, product.manufacturerID, product.subcategoryID, details.colorID, details.dimensionID, details.positionsID, color.colorName, dimension.width, dimension.height, dimension.length,\n" +
                    "positions.shelf, positions.regal, stock, image\n" +
                    "FROM (((((((product INNER JOIN details ON product.detailsID = details.detailsID)\n" +
                    "INNER JOIN manufacturer ON product.manufacturerID = manufacturer.manufacturerID)\n" +
                    "INNER JOIN subcategory ON product.subcategoryID = subcategory.subcategoryID)\n" +
                    "INNER JOIN category ON subcategory.categoryID = category.categoryID)\n" +
                    "INNER JOIN color ON details.colorID = color.colorID)\n" +
                    "INNER JOIN dimension ON details.dimensionID = dimension.dimensionID)\n" +
                    "INNER JOIN positions ON details.positionsID = positions.positionsID) WHERE product.productID = " + IDofProduct + ";";
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
                    resultSet.getInt("positionsID"),
                    resultSet.getInt("shelf"),
                    resultSet.getInt("regal"),
                    resultSet.getInt("stock"),
                    resultSet.getBlob("image")));
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
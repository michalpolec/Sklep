package shopProject;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class userScreenController {
    ObservableList<Product> products =  FXCollections.observableArrayList();
    ObservableList<Product> currentProducts = FXCollections.observableArrayList();
    ObservableList<Subcategory> currentSubcategories = FXCollections.observableArrayList();
    ObservableList<restOfElements> currentColors = FXCollections.observableArrayList();
    ObservableList<restOfElements> currentMaterial = FXCollections.observableArrayList();
    ObservableList<restOfElements>  categories = FXCollections.observableArrayList();
    ObservableList<Subcategory>  subcategories = FXCollections.observableArrayList();
    ObservableList<restOfElements> colors =  FXCollections.observableArrayList();
    ObservableList<restOfElements> materials =  FXCollections.observableArrayList();

    public JFXHamburger hamburgerFx;
    public JFXDrawer drawerFX;
    public StackPane stackPaneFX;
    public Button productsButton;
    public Button roomButton;
    public Pane livingroomPane;
    public Pane bedroomPane;
    public Pane kitchenPane;
    public Pane diningroomPane;
    public Pane kidsroomPane;
    public Pane homeofficePane;
    public Pane bathroomPane;
    public Pane hallPane;
    public Pane gardenPane;


    public ImageView livingroomImage;
    public ImageView bedroomImage;
    public ImageView diningImage;
    public ImageView kitchenImage;
    public ImageView kidsImage;
    public ImageView officeImage;
    public ImageView bathroomImage;
    public ImageView hallImage;
    public ImageView gardenImage;

    public GridPane gridPane;
    public ScrollPane scrollPane;

    public AnchorPane roomAnchorPane;
    public AnchorPane productsAnchorPane;

    public JFXListView JFXcategoriesListView;

    public ComboBox subcategoryComboBox;
    public ComboBox colorComboBox;
    public ComboBox materialComboBox;

    public Button sortButton;

    //variable use to sort products
    String room = "";
    String category = "";
    String subcategory = "";
    String color = "";
    String material = "";
    int sizeOfCurrentProducts = 0;


    public void initialize() throws SQLException, ClassNotFoundException {
        initializeDrawer();
        getAllDataFromDB();
        getData();
        drawerAction();
        subcategoryComboBox.setVisible(false);
        colorComboBox.setVisible(false);
        materialComboBox.setVisible(false);
        sortButton.setVisible(false);
    }

    private void drawerAction()
    {
        HamburgerBackArrowBasicTransition transitionClose = new HamburgerBackArrowBasicTransition(hamburgerFx);
        transitionClose.setRate(-1);
        hamburgerFx.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) ->{
            transitionClose.setRate(transitionClose.getRate()*-1);
            transitionClose.play();

            if(drawerFX.isOpened())
            {
                drawerFX.close();
            }
            else{
                drawerFX.open();
            }
        });
    }

    public void initializeDrawer()
    {
        drawerFX.close();
        drawerFX.setSidePane(stackPaneFX);
        drawerFX.setDefaultDrawerSize(280);
        drawerFX.setOverLayVisible(true);
        drawerFX.setResizableOnDrag(true);
        roomAnchorPane.setVisible(false);
        productsAnchorPane.setVisible(false);
    }

    public void initializeGridPane(ObservableList<Product> products) throws SQLException {

        String ifAvaliable;

        gridPane.getChildren().clear(); //delete previous data

        int i = 0;
        for(Product product:products)
        {
            if (product.getStock() > 0) {
                ifAvaliable = "Dostępny w ilości" + products.get(i).getStock();
            } else {
                ifAvaliable = "Niedostępny";
            }

            final Label nameOfProducts = new Label(product.getNameOfProduct());
            final Label priceOfProducts = new Label(String.valueOf(product.getPrice()) + " PLN");
            final Label stockOfProducts = new Label(ifAvaliable);
            final ImageView imageOfProducts = new ImageView(new Image(product.getImage().getBinaryStream(1, (int) product.getImage().length())));
            imageOfProducts.setFitHeight(100);
            imageOfProducts.setFitWidth(100);

            final AnchorPane anchorPane = new AnchorPane();
            anchorPane.setMinHeight(120);
            anchorPane.setMinWidth(420);

            anchorPane.getChildren().add(imageOfProducts);
            imageOfProducts.setLayoutX(10);
            imageOfProducts.setLayoutY(10);

            anchorPane.getChildren().add(nameOfProducts);
            nameOfProducts.setLayoutX(135);
            nameOfProducts.setLayoutY(15);

            anchorPane.getChildren().add(priceOfProducts);
            priceOfProducts.setLayoutX(320);
            priceOfProducts.setLayoutY(50);

            anchorPane.getChildren().add(stockOfProducts);
            stockOfProducts.setLayoutX(320);
            stockOfProducts.setLayoutY(80);

            anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage userStage = new Stage();
                    userStage.setTitle(product.getNameOfProduct());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/detailsOfProductScreen.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userStage.setScene(new Scene(root));

                    detailsOfProductScreenController newController = loader.getController();
                    newController.setSelectedProduct(product);

                    userStage.show();


                }
            });

            final Separator separator1 = new Separator();
            separator1.setOrientation(Orientation.VERTICAL);

            gridPane.add(anchorPane, 0, i);
            gridPane.add(separator1, 1, i);
            i++;

            final Separator separator2 = new Separator();
            final Separator separator3 = new Separator();
            gridPane.add(separator2, 0, i);
            gridPane.add(separator3, 1, i);
            i++;
        }

    }


    //it works
    public void onLivingRoomPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("living room");
        room = "Salon";
        currentProducts.clear();
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void onBedroomPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("bedroom");
        currentProducts.clear();
        room = "Sypialnia";
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void onKitchenPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("kitchen");
        currentProducts.clear();
        room = "Kuchnia";
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void onDiningroomPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("dining room");
        currentProducts.clear();
        room = "Jadalnia";
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void onKidsroomPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("kids room");
        currentProducts.clear();
        room = "Pokój dziecięcy";
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void onOfficePressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("home office");
        currentProducts.clear();
        room = "Domowe biuro";
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }


    public void onBathroomPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("bathroom");
        currentProducts.clear();
        room = "Łazienka";

        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void onHallPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("hall");
        currentProducts.clear();
        room = "Przedpokój";

        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void onGardenPressed(MouseEvent mouseEvent) throws SQLException {
        System.out.println("garden");
        currentProducts.clear();
        room = "Ogród";
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(p.getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);
    }

    public void getAllDataFromDB()
    {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
            Statement statement = connection.createStatement();

            String sql = "SELECT produkty.IDProduktu, NazwaProduktu, CenaProduktu, OpisProduktu, pomieszczenie.NazwaPomieszczenia, kategoria.NazwaKategorii, \n" +
                    "podkategoria.NazwaPodkategorii, kolor.NazwaKoloru, material.NazwaMaterialu, wymiary.Szerokosc, wymiary.Wysokosc, wymiary.Dlugosc,\n" +
                    " pozycja.Polka, pozycja.Regal, StanMagazynowy, Zdjecie\n" +
                    "FROM ((((((((produkty INNER JOIN szczegoly ON produkty.IDProduktu = szczegoly.IDProduktu)\n" +
                    "INNER JOIN pomieszczenie ON produkty.IDPomieszczenia = pomieszczenie.IDPomieszczenia)\n" +
                    "INNER JOIN podkategoria ON produkty.IDPodkategorii = podkategoria.IDPodkategorii)\n" +
                    "INNER JOIN kategoria ON podkategoria.IDKategorii = kategoria.IDKategorii)\n" +
                    "INNER JOIN kolor ON szczegoly.IDKoloru = kolor.IDKoloru)\n" +
                    "INNER JOIN material ON szczegoly.IDMaterialu = material.IDMaterialu)\n" +
                    "INNER JOIN wymiary ON szczegoly.IDWymiarow = wymiary.IDWymiarow)\n" +
                    "INNER JOIN pozycja ON szczegoly.IDPozycji = pozycja.IDPozycji);";

            ResultSet resultSet = statement.executeQuery(sql);


            while(resultSet.next()) {


                Product newProduct = new Product(
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

                products.add(newProduct);

            }

            statement.close();
            connection.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void onRoomButtonPressed(MouseEvent mouseEvent) {
        productsAnchorPane.setVisible(false);
        roomAnchorPane.setVisible(true);
        subcategoryComboBox.setVisible(false);
        colorComboBox.setVisible(false);
        materialComboBox.setVisible(false);
        sortButton.setVisible(false);
        livingroomImage.setImage(new Image("images/living.png"));
        bedroomImage.setImage(new Image("images/bedroom.png"));
        diningImage.setImage(new Image("images/diningroom.png"));
        kitchenImage.setImage(new Image("images/kitchen.png"));
        kidsImage.setImage(new Image("images/kidsroom.png"));
        officeImage.setImage(new Image("images/homeoffice.png"));
        bathroomImage.setImage(new Image("images/bathroom.png"));
        hallImage.setImage(new Image("images/hall.png"));
        gardenImage.setImage(new Image("images/garden.png"));
    }

    public void onProductsPressed(MouseEvent mouseEvent) {
        roomAnchorPane.setVisible(false);
        productsAnchorPane.setVisible(true);
        JFXcategoriesListView.setVisible(true);
        JFXcategoriesListView.setItems(categories);
    }

    public void getData() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String subcategory = "SELECT * FROM sklep.podkategoria";
        ResultSet resultSet = statement.executeQuery(subcategory);

        while(resultSet.next()) {
            Subcategory podkategoria = new Subcategory(Integer.parseInt(resultSet.getString("IDPodkategorii")),resultSet.getString("NazwaPodkategorii"),Integer.parseInt(resultSet.getString("IDKategorii")));
            subcategories.add(podkategoria);
        }

        String category = "SELECT * FROM sklep.kategoria";
        ResultSet resultSetCategories = statement.executeQuery(category);

        while(resultSetCategories.next()){
            restOfElements kategoria = new restOfElements(resultSetCategories.getInt(1), resultSetCategories.getString(2));
            categories.add(kategoria);
        }

        String color = "SELECT * FROM sklep.kolor";
        ResultSet resultSetColor = statement.executeQuery(color);

        while(resultSetColor.next()){
            restOfElements kolor = new restOfElements(resultSetColor.getInt(1), resultSetColor.getString(2));
            colors.add(kolor);
        }

        String material = "SELECT * FROM sklep.material";
        ResultSet resultSetMaterial = statement.executeQuery(material);

        while(resultSetMaterial.next()){
            restOfElements mat = new restOfElements(resultSetMaterial.getInt(1), resultSetMaterial.getString(2));
            materials.add(mat);
        }

        statement.close();
        connection.close();
    }

    //not works
    public void onListClicked(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        category = JFXcategoriesListView.getSelectionModel().getSelectedItems().toString();
        category = category.substring(1, category.length()-1); //it must be here!!!
        System.out.println(category);
        currentProducts.clear();
        sizeOfCurrentProducts = 0;
        for(Product product:products)
        {
            if(product.getCategory().equals(category))
            {
                currentProducts.add(product);
                sizeOfCurrentProducts++;
            }
        }
        System.out.println(sizeOfCurrentProducts);
        initializeGridPane(currentProducts);

        int IDofCategory = 0;
        for(restOfElements cat:categories)
        {
            if(cat.getName().equals(category)) {
                IDofCategory = cat.getID();
            }
        }

        currentSubcategories.clear();
        for(Subcategory sub:subcategories)
        {
            if(sub.getCategoryID() == IDofCategory)
            {
                currentSubcategories.add(sub);
            }
        }

        subcategoryComboBox.setVisible(true);
        subcategoryComboBox.setItems(currentSubcategories);

        setColorsComboBox(category);
        setMaterialsComboBox(category);
        sortButton.setVisible(true);
    }

    public void setColorsComboBox(String category) throws ClassNotFoundException, SQLException {
        currentColors.clear();

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT DISTINCT kolor.IDKoloru, kolor.NazwaKoloru\n" +
                "                    FROM ((((produkty INNER JOIN szczegoly ON produkty.IDProduktu = szczegoly.IDProduktu)\n" +
                "                    INNER JOIN podkategoria ON produkty.IDPodkategorii = podkategoria.IDPodkategorii) \n" +
                "                    INNER JOIN kategoria ON podkategoria.IDKategorii = kategoria.IDKategorii)\n" +
                "                    INNER JOIN kolor ON szczegoly.IDKoloru = kolor.IDKoloru)\n" +
                "WHERE kategoria.NazwaKategorii = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,category);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            restOfElements kolor = new restOfElements(resultSet.getInt(1), resultSet.getString(2));
            currentColors.add(kolor);
        }

        colorComboBox.setItems(currentColors);
        colorComboBox.setVisible(true);

        preparedStatement.close();
        statement.close();
        connection.close();
    }

    public void setMaterialsComboBox(String category) throws ClassNotFoundException, SQLException {
        currentMaterial.clear();

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        String sql = "SELECT distinct material.IDMaterialu, material.NazwaMaterialu\n" +
                "                    FROM ((((produkty INNER JOIN szczegoly ON produkty.IDProduktu = szczegoly.IDProduktu)\n" +
                "                    INNER JOIN podkategoria ON produkty.IDPodkategorii = podkategoria.IDPodkategorii) \n" +
                "                    INNER JOIN kategoria ON podkategoria.IDKategorii = kategoria.IDKategorii)\n" +
                "                    INNER JOIN material ON szczegoly.IDMaterialu = material.IDMaterialu)\n" +
                "WHERE kategoria.NazwaKategorii = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,category);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            restOfElements material = new restOfElements(resultSet.getInt(1), resultSet.getString(2));
            currentMaterial.add(material);
        }

        materialComboBox.setItems(currentMaterial);
        materialComboBox.setVisible(true);

        preparedStatement.close();
        statement.close();
        connection.close();
    }

    public void onSortButtonClicked(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException {
        boolean colorbool = colorComboBox.getSelectionModel().isEmpty();
        boolean materialbool = materialComboBox.getSelectionModel().isEmpty();
        boolean subcategorybool = subcategoryComboBox.getSelectionModel().isEmpty();

        if(colorbool == true) {
            color = "IS NOT NULL";
        }
        else{
            color =  colorComboBox.getValue().toString();
        }

        if(materialbool == true) {
            material = "IS NOT NULL";
        }
        else{
            material = materialComboBox.getValue().toString();
        }

        if(subcategorybool == true) {
            subcategory = "IS NOT NULL";
        }
        else{
            subcategory =  subcategoryComboBox.getValue().toString();
        }


        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Sklep?serverTimezone=UTC", "root", "bazadanych1-1");
        Statement statement = connection.createStatement();

        ResultSet resultSet = null;

        currentProducts.clear();

        String sql = "SELECT produkty.IDProduktu, NazwaProduktu, CenaProduktu, OpisProduktu, pomieszczenie.NazwaPomieszczenia, kategoria.NazwaKategorii,\n" +
                    "                    podkategoria.NazwaPodkategorii, kolor.NazwaKoloru, material.NazwaMaterialu, wymiary.Szerokosc, wymiary.Wysokosc, wymiary.Dlugosc,\n" +
                    "                     pozycja.Polka, pozycja.Regal, StanMagazynowy, Zdjecie\n" +
                    "                    FROM ((((((((produkty INNER JOIN szczegoly ON produkty.IDProduktu = szczegoly.IDProduktu)\n" +
                    "                    INNER JOIN pomieszczenie ON produkty.IDPomieszczenia = pomieszczenie.IDPomieszczenia)\n" +
                    "                    INNER JOIN podkategoria ON produkty.IDPodkategorii = podkategoria.IDPodkategorii)\n" +
                    "                    INNER JOIN kategoria ON podkategoria.IDKategorii = kategoria.IDKategorii)\n" +
                    "                    INNER JOIN kolor ON szczegoly.IDKoloru = kolor.IDKoloru)\n" +
                    "                    INNER JOIN material ON szczegoly.IDMaterialu = material.IDMaterialu)\n" +
                    "                    INNER JOIN wymiary ON szczegoly.IDWymiarow = wymiary.IDWymiarow)\n" +
                    "                    INNER JOIN pozycja ON szczegoly.IDPozycji = pozycja.IDPozycji)\n" +
                    "WHERE (podkategoria.NazwaPodkategorii = ?) AND (kolor.NazwaKoloru = ?) AND (material.NazwaMaterialu = ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,subcategory);
        preparedStatement.setString(2, color);
        preparedStatement.setString(3, material);
        resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            Product product = new Product(
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
            currentProducts.add(product);
        }

        initializeGridPane(currentProducts);
        preparedStatement.close();
        statement.close();
        connection.close();
    }
}

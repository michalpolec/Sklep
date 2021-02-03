package shopProject;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.sql.*;

public class userScreenController {
    ObservableList<Product> products =  FXCollections.observableArrayList();
    ObservableList<Product> currentProducts = FXCollections.observableArrayList();
    public JFXHamburger hamburgerFx;
    public JFXDrawer drawerFX;
    public StackPane stackPaneFX;
    public Button productsButton;
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

    //variable use to sort products
    String room = "";
    String category = "";
    String subcategory = "";
    String color = "";
    int sizeOfCurrentProducts = 0;


    public void initialize() throws SQLException {
        initializeDrawer();
        getAllDataFromDB();
        initializeGridPane();
        drawerAction();
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

    public void initializeGridPane() throws SQLException {
        //how space need to products in grid pane

        gridPane.resize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);


        int numberofproducts = products.size()%2;
        if(numberofproducts == 0)
            numberofproducts = products.size()/2;
        else
            numberofproducts = products.size()/2 + 1;

        if(numberofproducts > 0)
        {
            for(int i = 0; i < numberofproducts; i++)
            {

            }
        }

        ImageView imageview = new ImageView();
        imageview.setFitHeight(50);
        imageview.setFitWidth(65);

        gridPane.add(new ImageView(new Image(products.get(0).getImage().getBinaryStream(1, (int) products.get(0).getImage().length()))), 0, 0);
        gridPane.add(new ImageView(new Image(products.get(1).getImage().getBinaryStream(1, (int) products.get(1).getImage().length()))), 1, 0);

        scrollPane.setContent(gridPane);
        scrollPane.autosize();
        scrollPane.resize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

    }


    //it works
    public void onLivingRoomPressed(MouseEvent mouseEvent) {
        System.out.println("living room");
        room = "Salon";
        currentProducts.clear();
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }

    public void onBedroomPressed(MouseEvent mouseEvent) {
        System.out.println("bedroom");
        currentProducts.clear();
        room = "Sypialnia";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }

    public void onKitchenPressed(MouseEvent mouseEvent) {
        System.out.println("kitchen");
        currentProducts.clear();
        room = "Kuchnia";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }

    public void onDiningroomPressed(MouseEvent mouseEvent) {
        System.out.println("dining room");
        currentProducts.clear();
        room = "Jadalnia";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }

    public void onKidsroomPressed(MouseEvent mouseEvent) {
        System.out.println("kids room");
        currentProducts.clear();
        room = "Pokój dziecięcy";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }

    public void onOfficePressed(MouseEvent mouseEvent) {
        System.out.println("home office");
        currentProducts.clear();
        room = "Domowe biuro";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }


    public void onBathroomPressed(MouseEvent mouseEvent) {
        System.out.println("bathroom");
        currentProducts.clear();
        room = "Łazienka";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }

    public void onHallPressed(MouseEvent mouseEvent) {
        System.out.println("hall");
        currentProducts.clear();
        room = "Przedpokój";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
    }

    public void onGardenPressed(MouseEvent mouseEvent) {
        System.out.println("garden");
        currentProducts.clear();
        room = "Ogród";
        int i = 0;
        sizeOfCurrentProducts = 0;
        for(Product p:products)
        {
            if(products.get(i).getRoom().equals(room))
            {
                currentProducts.add(p);
                sizeOfCurrentProducts++;
            }
            i++;
        }
        System.out.println(sizeOfCurrentProducts);
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

}

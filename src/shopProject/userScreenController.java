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
import javafx.scene.layout.*;

import java.io.InputStream;
import java.sql.SQLException;

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


    public ImageView livingroomImage;
    public ImageView bedroomImage;
    public ImageView diningImage;
    public ImageView kitchenImage;
    public ImageView kidsImage;
    public ImageView officeImage;
    public ImageView bathroomImage;
    public ImageView hallImage;

    public GridPane gridPane;


    public void getAllData(ObservableList<Product> products) {
        this.products.setAll(products);
    }

    public void initialize()  {
        initializeDrawer();
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
    }

    public void initializeGridPane()  {
        //how space need to products in grid pane
        gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        gridPane.add(new ImageView("images/hall.png"), 0, 0);
        gridPane.add(new ImageView("images/hall.png"), 1, 0);
        gridPane.add(new ImageView("images/hall.png"), 1, 1);
        gridPane.add(new ImageView("images/hall.png"), 0, 1);
        gridPane.add(new ImageView("images/hall.png"), 0, 2);
        gridPane.add(new ImageView("images/hall.png"), 1, 2);
        gridPane.add(new ImageView("images/hall.png"), 0, 3);
        gridPane.add(new ImageView("images/hall.png"), 1, 3);




        int numberofproducts = products.size()%2;
        if(numberofproducts == 0)
            numberofproducts = products.size()/2;
        else
            numberofproducts = products.size()/2 + 1;

        if(numberofproducts > 0)
        {

        }

    }


    //it works
    public void onLivingRoomPressed(MouseEvent mouseEvent) {
        System.out.println("living room");
    }

    public void onBedroomPressed(MouseEvent mouseEvent) {
        System.out.println("bedroom");
    }

    public void onKitchenPressed(MouseEvent mouseEvent) {
        System.out.println("kitchen");
    }

    public void onDiningroomPressed(MouseEvent mouseEvent) {
        System.out.println("dining room");
    }

    public void onKidsroomPressed(MouseEvent mouseEvent) {
        System.out.println("kids room");
    }

    public void onOfficePressed(MouseEvent mouseEvent) {
        System.out.println("home office");
    }

    public void onBathroomPressed(MouseEvent mouseEvent) {
        System.out.println("bathroom");
    }

    public void onHallPressed(MouseEvent mouseEvent) {
        System.out.println("hall");
    }

}

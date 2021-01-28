package shopProject;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class userScreenController {
    ObservableList<Product> products =  FXCollections.observableArrayList();
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


    public void getAllData(ObservableList<Product> products) {
        this.products.setAll(products);
    }

    public void initialize()
    {
        initializeDrawer();
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

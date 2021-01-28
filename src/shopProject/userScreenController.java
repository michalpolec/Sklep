package shopProject;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
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


    public void getAllData(ObservableList<Product> products) {
        this.products.setAll(products);
    }

    public void initialize()
    {
        drawerAction();
        initializeDrawer();
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
        drawerFX.setDefaultDrawerSize(300);
        drawerFX.setOverLayVisible(true);
        drawerFX.setResizableOnDrag(true);
    }


    //it works
    public void onLivingRoomPressed(MouseEvent mouseEvent) {
        System.out.println("living room");
    }
}

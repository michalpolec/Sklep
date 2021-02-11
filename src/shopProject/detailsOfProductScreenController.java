package shopProject;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;

public class detailsOfProductScreenController {

    Product selectedProduct;

    public ImageView detailImageView;
    public Label colorLabel;
    public Label materialLabel;
    public Label priceLabel;
    public Label descriptionLabel;
    public Label dimensionLabel;
    public Label shelfLabel;
    public Label regalLabel;
    public Label stockLabel;
    public Label nameLabel;


    public void setSelectedProduct(Product selectedProduct) throws SQLException {
        this.selectedProduct = selectedProduct;
        setDetails(selectedProduct);
    }

    public void setDetails(Product product) throws SQLException {
        detailImageView.setImage(new Image(product.getImage().getBinaryStream()));
        colorLabel.setText(product.getColor());
        materialLabel.setText(product.getMaterial());

        double num = product.getPrice();
        long iPart = (long) num;
        double fPart = num - iPart;
        if(fPart == 0){
            priceLabel.setText(iPart + " PLN");
        }
        else {
            priceLabel.setText(num + " PLN");
        }

        nameLabel.setText(product.getNameOfProduct());
        descriptionLabel.setText(product.getDescription());
        dimensionLabel.setText(product.getWidth() + "cm x " + product.getHeight() + "cm x " + product.getLength() + "cm");
        shelfLabel.setText(String.valueOf(product.getShelf()));
        regalLabel.setText(String.valueOf(product.getRegal()));

        String ifAvaliable;

        if (product.getStock() > 0) {
            ifAvaliable = "Dostępny w ilości " + product.getStock();
        } else {
            ifAvaliable = "Niedostępny";
        }
        stockLabel.setText(ifAvaliable);
    }

}

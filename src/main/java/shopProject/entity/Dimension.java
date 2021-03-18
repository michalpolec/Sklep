package shopProject.entity;

import lombok.Data;

@Data
public class Dimension {

    private int dimensionID;
    private double width;
    private double height;
    private double lenght;

    public Dimension(int dimensionID, double width, double height, double lenght) {
        this.dimensionID = dimensionID;
        this.width = width;
        this.height = height;
        this.lenght = lenght;
    }

    @Override
    public String toString() {
        return width + "cm x " + height + "cm x " + lenght + "cm";
    }
}

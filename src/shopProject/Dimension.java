package shopProject;

public class Dimension {

    int dimensionID;
    double width;
    double height;
    double lenght;

    public Dimension(int dimensionID, double width, double height, double lenght) {
        this.dimensionID = dimensionID;
        this.width = width;
        this.height = height;
        this.lenght = lenght;
    }

    public int getDimensionID() {
        return dimensionID;
    }

    public void setDimensionID(int dimensionID) {
        this.dimensionID = dimensionID;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLenght() {
        return lenght;
    }

    public void setLenght(double lenght) {
        this.lenght = lenght;
    }

    @Override
    public String toString() {
        return width + "cm x " + height + "cm x " + lenght + "cm";
    }
}

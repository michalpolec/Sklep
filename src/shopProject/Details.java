package shopProject;

public class Details extends  Object{

    int productID;
    int positionID;
    int demensionID;
    int materialID;
    int colorID;

    public Details(int productID, int positionID, int demensionID, int materialID, int colorID) {
        this.productID = productID;
        this.positionID = positionID;
        this.demensionID = demensionID;
        this.materialID = materialID;
        this.colorID = colorID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public int getDemensionID() {
        return demensionID;
    }

    public void setDemensionID(int demensionID) {
        this.demensionID = demensionID;
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }
}

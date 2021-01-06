package shopProject;

public class Position extends Object{

    int positionID;
    int shelf;
    int regal;

    public Position(int positionID, int shelf, int regal) {
        this.positionID = positionID;
        this.shelf = shelf;
        this.regal = regal;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public int getShelf() {
        return shelf;
    }

    public void setShelf(int shelf) {
        this.shelf = shelf;
    }

    public int getRegal() {
        return regal;
    }

    public void setRegal(int regal) {
        this.regal = regal;
    }

    @Override
    public String toString() {
        return "Półka: " + shelf + ", Regał: " + regal;
    }
}

package shopProject;

public class twoColumnTable extends Object{

    int ID;
    String name;

    public twoColumnTable(int roomID, String name) {
        this.ID = roomID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int roomID) {
        this.ID = roomID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

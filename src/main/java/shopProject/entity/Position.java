package shopProject.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Position {

    private int positionID;
    private int shelf;
    private int regal;

    public Position(int positionID, int shelf, int regal) {
        this.positionID = positionID;
        this.shelf = shelf;
        this.regal = regal;
    }


    @Override
    public String toString() {
        return "Półka: " + shelf + ", Regał: " + regal;
    }
}

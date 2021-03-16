package shopProject.entity;

import lombok.Data;

@Data
public class restOfElements {

    private int ID;
    private String name;

    public restOfElements(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}


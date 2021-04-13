package shopProject.entity;

import lombok.Data;

@Data
public class Elements {

    private int ID;
    private String name;

    public Elements(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}


package shopProject.entity;

import lombok.Data;

@Data
public class RestOfElements {

    private int ID;
    private String name;

    public RestOfElements(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}


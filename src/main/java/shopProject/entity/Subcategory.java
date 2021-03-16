package shopProject.entity;

import lombok.Data;

@Data
public class Subcategory {

    private int subcategoryID;
    private String subcategoryName;
    private int categoryID;

    public Subcategory(int subcategoryID, String subcategoryName, int categoryID) {
        this.subcategoryID = subcategoryID;
        this.subcategoryName = subcategoryName;
        this.categoryID = categoryID;
    }

    @Override
    public String toString() {
        return subcategoryName;
    }
}


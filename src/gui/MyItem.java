package gui;

import java.awt.*;
import java.io.Serializable;

public class MyItem implements Serializable {
    private String description;
    private Color status;

    public MyItem(){}
    public MyItem(String description, Color status){
        this.description=description;
        this.status=status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getStatus() {
        return status;
    }

    public void setStatus(Color status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return this.getDescription();
    }
}

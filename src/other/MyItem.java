package other;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;

public class MyItem implements Serializable {
    private String description;
    private Color status;
    private Date time;

    public MyItem() {
    }

    public MyItem(String description, Color status, Date time) {
        this.description = description;
        this.status = status;
        this.time = time;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }
}
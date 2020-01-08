package hr.ml.izdajracun.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rental_property_info")
public class RentalPropertyInfo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String address;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerIBAN;
    private String ownerOIB;

    public RentalPropertyInfo(String name, String address, String ownerFirstName, String ownerLastName, String ownerIBAN, String ownerOIB) {
        this.name = name;
        this.address = address;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerIBAN = ownerIBAN;
        this.ownerOIB = ownerOIB;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public String getOwnerIBAN() {
        return ownerIBAN;
    }

    public String getOwnerOIB() {
        return ownerOIB;
    }
}

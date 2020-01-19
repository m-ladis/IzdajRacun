package hr.ml.izdajracun.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "invoice", foreignKeys =
        {@ForeignKey(entity = RentalPropertyInfo.class,
                parentColumns = "id",
                childColumns = "propertyId",
                onDelete = CASCADE)})

public class Invoice implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int propertyId;
    private int number;
    private String customerName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String description;
    private Calendar date;
    private int year;

    public Invoice(int propertyId, int number, String customerName, int quantity, double unitPrice,
                   double totalPrice, String description) {
        this.propertyId = propertyId;
        this.number = number;
        this.customerName = customerName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.description = description;
    }

    @Ignore
    public Invoice(int number, String customerName, int quantity, double unitPrice,
                   double totalPrice, String description) {
        this.number = number;
        this.customerName = customerName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.description = description;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getDate() {
        return date;
    }

    public int getYear() {
        return year;
    }
}

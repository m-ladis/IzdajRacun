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
    private RentalPropertyInfo propertyInfo;
    private int number;
    private String customerName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String description;
    private Calendar date;
    private int year;

    public Invoice() {}

    @Ignore
    public Invoice(RentalPropertyInfo propertyInfo, int number, String customerName, int quantity,
                   double unitPrice, double totalPrice, String description, Calendar date, int year) {
        this.propertyInfo = propertyInfo;
        this.propertyId = propertyInfo.getId();
        this.number = number;
        this.customerName = customerName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.description = description;
        this.date = date;
        this.year = year;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDescription(String description) {
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

    public RentalPropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    public void setPropertyInfo(RentalPropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }
}

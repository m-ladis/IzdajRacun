package hr.ml.izdajracun.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "invoice")
public class Invoice {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int number;
    private String customerName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String description;
    private Calendar date;
    private int year;

    public Invoice(int id, int number, String customerName, int quantity, double unitPrice,
                   double totalPrice, String description, Calendar date) {
        this.id = id;
        this.number = number;
        this.customerName = customerName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.description = description;
        this.date = date;
        this.year = date.get(Calendar.YEAR);
    }

    public void setId(int id) {
        this.id = id;
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

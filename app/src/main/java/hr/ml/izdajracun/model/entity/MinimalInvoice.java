package hr.ml.izdajracun.model.entity;

import java.util.Calendar;

public class MinimalInvoice {

    private int id;
    private int number;
    private String customerName;
    private double totalPrice;
    private Calendar date;

    public MinimalInvoice(int id, int number, String customerName, double totalPrice, Calendar date) {
        this.id = id;
        this.number = number;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.date = date;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public Calendar getDate() {
        return date;
    }
}

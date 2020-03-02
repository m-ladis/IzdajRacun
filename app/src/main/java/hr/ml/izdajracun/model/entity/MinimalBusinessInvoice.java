package hr.ml.izdajracun.model.entity;

import java.util.Calendar;

public class MinimalBusinessInvoice extends MinimalInvoice{

    private String paymentMethod;

    public MinimalBusinessInvoice(int id, int number, String customerName, double totalPrice,
                                  Calendar date, String paymentMethod) {
        super(id, number, customerName, totalPrice, date);
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}

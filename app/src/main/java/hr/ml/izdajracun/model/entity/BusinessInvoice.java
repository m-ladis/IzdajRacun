package hr.ml.izdajracun.model.entity;

import androidx.room.Entity;

import java.util.Calendar;

@Entity(tableName = "business_invoice")
public class BusinessInvoice extends Invoice {

    private String customerAddress;
    private String customerOib;
    private String paymentMethod;
    private Calendar payDueDate;
    private Calendar deliveryDate;

    public BusinessInvoice(int propertyId ,Invoice invoice, String customerAddress,
                           String customerOib, String paymentMethod, Calendar payDueDate,
                           Calendar deliveryDate) {

        super(propertyId ,invoice.getNumber(), invoice.getCustomerName(), invoice.getQuantity(),
                invoice.getUnitPrice(), invoice.getTotalPrice(), invoice.getDescription());

        this.customerAddress = customerAddress;
        this.customerOib = customerOib;
        this.paymentMethod = paymentMethod;
        this.payDueDate = payDueDate;
        this.deliveryDate = deliveryDate;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerOib() {
        return customerOib;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Calendar getPayDueDate() {
        return payDueDate;
    }

    public Calendar getDeliveryDate() {
        return deliveryDate;
    }
}

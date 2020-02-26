package hr.ml.izdajracun.model.entity;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.Calendar;

@Entity(tableName = "business_invoice")
public class BusinessInvoice extends Invoice {

    private String customerAddress;
    private String customerOib;
    private String paymentMethod;
    private Calendar payDueDate;
    private Calendar deliveryDate;

    public BusinessInvoice() {}

    @Ignore
    public BusinessInvoice(Invoice invoice, String customerAddress, String customerOib,
                           String paymentMethod, Calendar payDueDate, Calendar deliveryDate) {

        super(invoice.getPropertyInfo() ,invoice.getNumber(), invoice.getCustomerName(),
                invoice.getQuantity(), invoice.getUnitPrice(), invoice.getTotalPrice(),
                invoice.getDescription(), invoice.getDate(), invoice.getYear());

        this.customerAddress = customerAddress;
        this.customerOib = customerOib;
        this.paymentMethod = paymentMethod;
        this.payDueDate = payDueDate;
        this.deliveryDate = deliveryDate;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerOib(String customerOib) {
        this.customerOib = customerOib;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public void setPayDueDate(Calendar payDueDate) {
        this.payDueDate = payDueDate;
    }

    public void setDeliveryDate(Calendar deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}

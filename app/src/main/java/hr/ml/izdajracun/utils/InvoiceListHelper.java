package hr.ml.izdajracun.utils;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;

public class InvoiceListHelper {
    public static Object getObjectAtIndex(int index, @NonNull List<Invoice> invoices,
                                          @NonNull List<BusinessInvoice> businessInvoices){

        Object objectAtIndex = null;

        Stack<Invoice> invoiceStack = new Stack<>();
        Stack<BusinessInvoice> businessInvoiceStack = new Stack<>();

        invoiceStack.addAll(invoices);
        businessInvoiceStack.addAll(businessInvoices);

        while(index >= 0){
            if(invoiceStack.isEmpty() && businessInvoiceStack.isEmpty()){
                return null;
            } else if(invoiceStack.isEmpty()){
                objectAtIndex = businessInvoiceStack.pop();
                index--;
            } else if(businessInvoiceStack.isEmpty()){
                objectAtIndex = invoiceStack.pop();
                index--;
            } else {
                Invoice invoice1 = invoiceStack.peek();
                BusinessInvoice invoice2 = businessInvoiceStack.peek();

                if(invoice1.getNumber() >= invoice2.getNumber()){
                    objectAtIndex = invoiceStack.pop();
                    index--;
                } else {
                    objectAtIndex = businessInvoiceStack.pop();
                    index--;
                }
            }
        }
        return objectAtIndex;
    }
}

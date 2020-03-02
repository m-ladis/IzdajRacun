package hr.ml.izdajracun.utils;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;

public class InvoiceListHelper {
    public static Object getObjectAtIndex(int index, @NonNull List<MinimalInvoice> invoices,
                                          @NonNull List<MinimalBusinessInvoice> businessInvoices){

        Object objectAtIndex = null;

        Stack<MinimalInvoice> invoiceStack = new Stack<>();
        Stack<MinimalBusinessInvoice> businessInvoiceStack = new Stack<>();

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
                MinimalInvoice invoice1 = invoiceStack.peek();
                MinimalBusinessInvoice invoice2 = businessInvoiceStack.peek();

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

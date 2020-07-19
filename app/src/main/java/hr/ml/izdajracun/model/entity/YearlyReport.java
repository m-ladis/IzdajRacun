package hr.ml.izdajracun.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class YearlyReport {
    private int year;
    private String taxpayerFullName;
    private String taxpayerOib;
    private String taxpayerPlace;
    private String taxpayerAddress;
    private List<MinimalInvoice> invoices;
    private List<MinimalBusinessInvoice> businessInvoices;
    private List<MinimalInvoice> allInvoices = new ArrayList<>();

    public YearlyReport(int year, String taxpayerFullName, String taxpayerOib, String taxpayerPlace,
                        String taxpayerAddress) {
        this.year = year;
        this.taxpayerFullName = taxpayerFullName;
        this.taxpayerOib = taxpayerOib;
        this.taxpayerPlace = taxpayerPlace;
        this.taxpayerAddress = taxpayerAddress;
    }

    public void setInvoices(List<MinimalInvoice> invoices) {
        this.invoices = invoices;
    }

    public void setBusinessInvoices(List<MinimalBusinessInvoice> businessInvoices) {
        this.businessInvoices = businessInvoices;
    }

    public int getYear() {
        return year;
    }

    public String getTaxpayerFullName() {
        return taxpayerFullName;
    }

    public String getTaxpayerOib() {
        return taxpayerOib;
    }

    public String getTaxpayerPlace() {
        return taxpayerPlace;
    }

    public String getTaxpayerAddress() {
        return taxpayerAddress;
    }

    public List<MinimalInvoice> getAllInvoices() {
        return allInvoices;
    }

    public boolean isComplete(){
        if((invoices != null) && (businessInvoices != null)) {
            return true;
        } else {
            return false;
        }
    }

    public void generate(){
        allInvoices.addAll(invoices);
        allInvoices.addAll(businessInvoices);
        Collections.sort(allInvoices, new Comparator<MinimalInvoice>() {
            @Override
            public int compare(MinimalInvoice o1, MinimalInvoice o2) {
                return Integer.compare(o1.getNumber(), o2.getNumber());
            }
        });
    }
}

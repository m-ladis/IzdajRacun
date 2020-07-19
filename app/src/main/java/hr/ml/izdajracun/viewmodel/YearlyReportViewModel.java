package hr.ml.izdajracun.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.BusinessInvoiceRepository;
import hr.ml.izdajracun.repository.InvoiceRepository;

public class YearlyReportViewModel extends AndroidViewModel {

    public LiveData<List<Integer>> invoiceYears;
    public LiveData<List<Integer>> businessInvoiceYears;
    public LiveData<List<MinimalInvoice>> minimalInvoices;
    public LiveData<List<MinimalBusinessInvoice>> businessMinimalInvoices;

    private InvoiceRepository invoiceRepository;
    private BusinessInvoiceRepository businessInvoiceRepository;
    private RentalPropertyInfo propertyInfo;

    public YearlyReportViewModel(@NonNull Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        businessInvoiceRepository = new BusinessInvoiceRepository(application);
    }

    public void start(Bundle bundle){
        propertyInfo = (RentalPropertyInfo) bundle.getSerializable("property");
        updateInvoiceYears();
        updateBusinessYears();
    }

    private void updateInvoiceYears(){
        invoiceYears = invoiceRepository.getYears(propertyInfo.getId());
    }

    private void updateBusinessYears(){
        businessInvoiceYears = businessInvoiceRepository.getYears(propertyInfo.getId());
    }

    public RentalPropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    public void getMinimalInvoicesInYear(int year){
        minimalInvoices = invoiceRepository.getAllMinimalInvoicesInYear(propertyInfo.getId(), year);
    }

    public void getMinimalBusinessInvoicesInYear(int year){
        businessMinimalInvoices = businessInvoiceRepository
                .getAllMinimalBusinessInvoicesInYear(propertyInfo.getId(), year);
    }
}

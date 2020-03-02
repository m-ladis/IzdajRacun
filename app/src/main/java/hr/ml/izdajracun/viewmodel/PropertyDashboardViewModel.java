package hr.ml.izdajracun.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.BusinessInvoiceRepository;
import hr.ml.izdajracun.repository.InvoiceRepository;
import hr.ml.izdajracun.repository.RentalPropertyInfoRepository;
import hr.ml.izdajracun.utils.CustomTimeUtils;
import hr.ml.izdajracun.utils.InvoiceGenerator;

public class PropertyDashboardViewModel extends AndroidViewModel {

    private static final String TAG = "PropertyDashboardVM";

    public MutableLiveData<Integer> selectedYear = new MutableLiveData<>();
    public MutableLiveData<File> fileToOpen = new MutableLiveData<>();
    public LiveData<List<MinimalInvoice>> invoices;
    public LiveData<List<MinimalBusinessInvoice>> businessInvoices;
    public LiveData<Invoice> invoice;
    public LiveData<BusinessInvoice> businessInvoice;

    private InvoiceRepository invoiceRepository;
    private BusinessInvoiceRepository businessInvoiceRepository;
    private RentalPropertyInfoRepository propertyInfoRepository;
    private RentalPropertyInfo propertyInfo;

    public PropertyDashboardViewModel(@NonNull Application application) {
        super(application);

        invoiceRepository = new InvoiceRepository(application);
        businessInvoiceRepository = new BusinessInvoiceRepository(application);
        propertyInfoRepository = new RentalPropertyInfoRepository(application);

        int year = CustomTimeUtils.getCurrentYear();

        selectedYear.setValue(year);
    }

    public void start(Bundle arguments) {
        if(arguments != null){
            propertyInfo = (RentalPropertyInfo) arguments.getSerializable("property");
        } else {
            Log.d(TAG, "failed to get arguments");
        }
    }

    public void updateInvoices(){
        invoices = invoiceRepository
                .getAllMinimalInvoicesInYear(propertyInfo.getId(), selectedYear.getValue());
    }

    public void updateBusinessInvoices(){
        businessInvoices = businessInvoiceRepository
                .getAllMinimalBusinessInvoicesInYear(propertyInfo.getId(), selectedYear.getValue());
    }

    public void deleteInvoiceById(int id){
        invoiceRepository.deleteById(id);
    }

    public void deleteBusinessInvoiceById(int id) {
        businessInvoiceRepository.deleteById(id);
    }

    public void getInvoiceById(int id){
        invoice = invoiceRepository.getInvoiceById(id);
    }

    public void getBusinessInvoiceById(int id){
        businessInvoice = businessInvoiceRepository.getBusinessInvoiceById(id);
    }

    public void deleteAllPropertyData(){
        propertyInfoRepository.delete(propertyInfo);
    }

    public void incrementYear(){
        selectedYear.setValue(selectedYear.getValue() + 1);
    }

    public void decrementYear(){
        selectedYear.setValue(selectedYear.getValue() - 1);
    }

    public void openPdf(Invoice invoice) {
        File pdfFile = InvoiceGenerator.getInvoiceFile(invoice);

        if(pdfFile.isFile()){
            fileToOpen.setValue(pdfFile);
        } else {
            try {
                fileToOpen.setValue(InvoiceGenerator.generate(invoice));
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }
        }
    }
}

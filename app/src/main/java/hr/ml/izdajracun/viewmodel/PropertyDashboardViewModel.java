package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.InvoiceRepository;
import hr.ml.izdajracun.repository.RentalPropertyInfoRepository;
import hr.ml.izdajracun.utils.CustomTimeUtils;

public class PropertyDashboardViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> selectedYear = new MutableLiveData<>();
    public LiveData<List<Invoice>> invoices;

    private InvoiceRepository invoiceRepository;
    private RentalPropertyInfoRepository propertyInfoRepository;
    private int propertyInfoId;

    public PropertyDashboardViewModel(@NonNull Application application) {
        super(application);

        invoiceRepository = new InvoiceRepository(application);
        propertyInfoRepository = new RentalPropertyInfoRepository(application);

        int year = CustomTimeUtils.getCurrentYear();

        selectedYear.setValue(year);
    }

    public void updateInvoices(){
        invoices = invoiceRepository.getAllInvoicesInYear(propertyInfoId, selectedYear.getValue());
    }

    public void deleteAllPropertyData(RentalPropertyInfo propertyInfo){
        propertyInfoRepository.delete(propertyInfo);
    }

    public void incrementYear(){
        selectedYear.setValue(selectedYear.getValue() + 1);
    }

    public void decrementYear(){
        selectedYear.setValue(selectedYear.getValue() - 1);
    }

    public void setPropertyId(int propertyInfoId) {
        this.propertyInfoId = propertyInfoId;
    }
}

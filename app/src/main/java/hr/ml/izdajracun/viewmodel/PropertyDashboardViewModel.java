package hr.ml.izdajracun.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

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

    private static final String TAG = "PropertyDashboardVM";

    public MutableLiveData<Integer> selectedYear = new MutableLiveData<>();
    public LiveData<List<Invoice>> invoices;

    private InvoiceRepository invoiceRepository;
    private RentalPropertyInfoRepository propertyInfoRepository;
    private RentalPropertyInfo propertyInfo;

    public PropertyDashboardViewModel(@NonNull Application application) {
        super(application);

        invoiceRepository = new InvoiceRepository(application);
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
                .getAllInvoicesInYear(propertyInfo.getId(), selectedYear.getValue());
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
}

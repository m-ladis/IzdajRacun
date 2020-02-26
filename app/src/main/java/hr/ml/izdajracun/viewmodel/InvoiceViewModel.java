package hr.ml.izdajracun.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.InvoiceRepository;
import hr.ml.izdajracun.utils.DataValidationStatus;
import hr.ml.izdajracun.utils.InputFieldValidator;
import hr.ml.izdajracun.utils.ViewModelMode;

public class InvoiceViewModel extends AndroidViewModel {

    private static final String TAG = "InvoiceViewModel";

    public MutableLiveData<DataValidationStatus> dataValidationStatus = new MutableLiveData<>();
    public MutableLiveData<Calendar> invoiceDate = new MutableLiveData<>();
    public MutableLiveData<ViewModelMode> mode = new MutableLiveData<>();
    public Invoice invoiceToUpdate;

    private InvoiceRepository invoiceRepository;
    public RentalPropertyInfo propertyInfo;

    public InvoiceViewModel(@NonNull Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
    }

    public void start(Bundle arguments) {
        if(arguments != null){
            propertyInfo = (RentalPropertyInfo) arguments.getSerializable("property");
            invoiceToUpdate = (Invoice) arguments.getSerializable("invoice");

            if(invoiceToUpdate == null) {
                mode.setValue(ViewModelMode.MODE_ADD);
            } else {
                mode.setValue(ViewModelMode.MODE_UPDATE);
                invoiceDate.setValue(invoiceToUpdate.getDate());
            }
        } else {
            Log.d(TAG, "failed to get arguments");
        }
    }

    public void handleData(Invoice invoice){
        switch (mode.getValue()){
            case MODE_ADD:
                insert(invoice);
                break;
            case MODE_UPDATE:
                invoiceToUpdate.setNumber(invoice.getNumber());
                invoiceToUpdate.setCustomerName(invoice.getCustomerName());
                invoiceToUpdate.setQuantity(invoice.getQuantity());
                invoiceToUpdate.setUnitPrice(invoice.getUnitPrice());
                invoiceToUpdate.setTotalPrice(invoice.getTotalPrice());
                invoiceToUpdate.setDescription(invoice.getDescription());
                invoiceToUpdate.setDate(invoice.getDate());

                update(invoiceToUpdate);
                break;
        }
    }

    private void insert(Invoice invoice){
        invoiceRepository.insert(invoice);
    }

    private void update(Invoice invoice){
        invoiceRepository.update(invoice);
    }

    public void setYearMonthDay(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        invoiceDate.setValue(calendar);
    }

    public void isInvoiceDataValid(String invoiceNumber, String customerName, String quantity,
                                   String unitPrice, String totalPrice, String date) {
        if(InputFieldValidator.isAnyStringEmpty(invoiceNumber, customerName, quantity, unitPrice,
                totalPrice, date)){

            dataValidationStatus.setValue(DataValidationStatus.DATA_HAS_EMPTY_FIELD);
            return;
        }

        if(!InputFieldValidator.isPriceValid(Integer.parseInt(quantity),
                Double.parseDouble(unitPrice), Double.parseDouble(totalPrice))){

            dataValidationStatus.setValue(DataValidationStatus.PRICE_NOT_VALID);
            return;
        }

        dataValidationStatus.setValue(DataValidationStatus.VALID);
    }
}

package hr.ml.izdajracun.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.BusinessInvoiceRepository;
import hr.ml.izdajracun.utils.DataValidationStatus;
import hr.ml.izdajracun.utils.InputFieldValidator;
import hr.ml.izdajracun.utils.ViewModelMode;

public class BuisnessInvoiceViewModel extends AndroidViewModel {

    private static final String TAG = "BusinessInvoiceVM";

    private BusinessInvoiceRepository businessInvoiceRepository;

    public MutableLiveData<DataValidationStatus> dataValidationStatus = new MutableLiveData<>();
    public MutableLiveData<ViewModelMode> mode = new MutableLiveData<>();
    public MutableLiveData<Calendar> issueDate = new MutableLiveData<>();
    public MutableLiveData<Calendar> payDueDate = new MutableLiveData<>();
    public MutableLiveData<Calendar> deliveryDate = new MutableLiveData<>();

    public RentalPropertyInfo propertyInfo;
    public BusinessInvoice invoiceToUpdate;

    public BuisnessInvoiceViewModel(@NonNull Application application) {
        super(application);
        businessInvoiceRepository = new BusinessInvoiceRepository(application);
    }

    public void start(Bundle arguments) {
        if(arguments != null){
            invoiceToUpdate = (BusinessInvoice) arguments.getSerializable("invoice");
            propertyInfo = (RentalPropertyInfo) arguments.getSerializable("property");

            if(invoiceToUpdate == null) {
                mode.setValue(ViewModelMode.MODE_ADD);
            } else {
                mode.setValue(ViewModelMode.MODE_UPDATE);
                issueDate.setValue(invoiceToUpdate.getDate());
                payDueDate.setValue(invoiceToUpdate.getPayDueDate());
                deliveryDate.setValue(invoiceToUpdate.getDeliveryDate());
            }
        } else {
            Log.d(TAG, "failed to get arguments");
        }
    }

    public void setDeliveryDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        deliveryDate.setValue(calendar);
    }

    public void setIssueDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        issueDate.setValue(calendar);
    }

    public void setPayDueDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        payDueDate.setValue(calendar);
    }

    public void isInvoiceDataValid(String invoiceNumber, String customerName,
                                   String customerAddress, String customerOib, String quantity,
                                   String unitPrice, String totalPrice, String issueDate,
                                   String payDueDate, String deliveryDate) {

        if(InputFieldValidator.isAnyStringEmpty(invoiceNumber, customerName, customerAddress,
                customerOib, quantity, unitPrice, totalPrice, issueDate, payDueDate, deliveryDate)){

            dataValidationStatus.setValue(DataValidationStatus.DATA_HAS_EMPTY_FIELD);
            return;
        }

        if(!InputFieldValidator.isOib(customerOib)){
            dataValidationStatus.setValue(DataValidationStatus.OIB_NOT_VALID);
            return;
        }

        if(!InputFieldValidator.isPriceValid(Integer.parseInt(quantity),
                Double.parseDouble(unitPrice), Double.parseDouble(totalPrice))){

            dataValidationStatus.setValue(DataValidationStatus.PRICE_NOT_VALID);
            return;
        }

        dataValidationStatus.setValue(DataValidationStatus.VALID);
    }

    public void handleData(BusinessInvoice businessInvoice) {
        switch (mode.getValue()){
            case MODE_ADD:
                insert(businessInvoice);
                break;
            case MODE_UPDATE:
                invoiceToUpdate.setNumber(businessInvoice.getNumber());
                invoiceToUpdate.setCustomerName(businessInvoice.getCustomerName());
                invoiceToUpdate.setCustomerAddress(businessInvoice.getCustomerAddress());
                invoiceToUpdate.setCustomerOib(businessInvoice.getCustomerOib());
                invoiceToUpdate.setQuantity(businessInvoice.getQuantity());
                invoiceToUpdate.setUnitPrice(businessInvoice.getUnitPrice());
                invoiceToUpdate.setTotalPrice(businessInvoice.getTotalPrice());
                invoiceToUpdate.setDescription(businessInvoice.getDescription());
                invoiceToUpdate.setPaymentMethod(businessInvoice.getPaymentMethod());
                invoiceToUpdate.setDate(businessInvoice.getDate());
                invoiceToUpdate.setPayDueDate(businessInvoice.getPayDueDate());
                invoiceToUpdate.setDeliveryDate(businessInvoice.getDeliveryDate());

                update(invoiceToUpdate);
                break;
        }
    }

    private void update(BusinessInvoice businessInvoice) {
        businessInvoiceRepository.update(businessInvoice);
    }

    private void insert(BusinessInvoice businessInvoice) {
        businessInvoiceRepository.insert(businessInvoice);
    }
}

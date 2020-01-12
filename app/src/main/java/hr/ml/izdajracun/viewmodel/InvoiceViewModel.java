package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.repository.InvoiceRepository;
import hr.ml.izdajracun.utils.DataValidationStatus;
import hr.ml.izdajracun.utils.ViewModelMode;

public class InvoiceViewModel extends AndroidViewModel {

    public MutableLiveData<DataValidationStatus> dataValidationStatus;

    private MutableLiveData<Calendar> invoiceDate = new MutableLiveData<>();
    private InvoiceRepository invoiceRepository;
    private ViewModelMode viewModelMode;
    private Invoice invoiceToUpdate;

    public InvoiceViewModel(@NonNull Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        viewModelMode = ViewModelMode.MODE_ADD;
    }

    public void handleData(Invoice invoice){
        Calendar calendar = getInvoiceDate().getValue();

        invoice.setDate(calendar);
        invoice.setYear(calendar.get(Calendar.YEAR));

        switch (viewModelMode){
            case MODE_ADD:
                insert(invoice);
                break;
            case MODE_UPDATE:
                invoice.setId(invoiceToUpdate.getId());
                update(invoice);
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

    public MutableLiveData<Calendar> getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceToUpdate(Invoice invoiceToUpdate) {
        this.invoiceToUpdate = invoiceToUpdate;
    }

    public void setViewModelMode(ViewModelMode viewModelMode) {
        this.viewModelMode = viewModelMode;
    }
}

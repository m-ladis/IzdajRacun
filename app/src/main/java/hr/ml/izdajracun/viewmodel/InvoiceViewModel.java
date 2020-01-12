package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.repository.InvoiceRepository;

public class InvoiceViewModel extends AndroidViewModel {

    private MutableLiveData<Calendar> invoiceDate = new MutableLiveData<>();
    private InvoiceRepository invoiceRepository;

    public InvoiceViewModel(@NonNull Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
    }

    public void handleData(Invoice invoice){
        insert(invoice);
    }

    private void insert(Invoice invoice){
        Calendar calendar = getInvoiceDate().getValue();

        invoice.setDate(calendar);
        invoice.setYear(calendar.get(Calendar.YEAR));

        invoiceRepository.insert(invoice);
    }

    public void setYearMonthDay(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        invoiceDate.setValue(calendar);
    }

    public MutableLiveData<Calendar> getInvoiceDate() {
        return invoiceDate;
    }
}

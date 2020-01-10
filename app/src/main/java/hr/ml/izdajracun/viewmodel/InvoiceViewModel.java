package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

public class InvoiceViewModel extends AndroidViewModel {

    private MutableLiveData<Calendar> invoiceDate = new MutableLiveData<>();

    public InvoiceViewModel(@NonNull Application application) {
        super(application);
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

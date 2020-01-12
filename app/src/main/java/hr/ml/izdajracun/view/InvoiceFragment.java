package hr.ml.izdajracun.view;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.utils.CustomTimeUtils;
import hr.ml.izdajracun.utils.ViewModelMode;
import hr.ml.izdajracun.viewmodel.InvoiceViewModel;


public class InvoiceFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "InvoiceFragment";

    private EditText invoiceNumberEditText;
    private EditText customerNameEditText;
    private EditText quantityEditText;
    private EditText unitPriceEditText;
    private EditText totalPriceEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private FloatingActionButton invoiceDoneButton;

    private InvoiceViewModel viewModel;
    private RentalPropertyInfo propertyInfo;

    public InvoiceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_invoice, container, false);

        Invoice invoiceToUpdate = (Invoice) getArguments().getSerializable("invoice");
        propertyInfo = (RentalPropertyInfo) getArguments().getSerializable("property");

        viewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);

        invoiceNumberEditText = rootView.findViewById(R.id.invoice_number);
        customerNameEditText = rootView.findViewById(R.id.customer_name);
        quantityEditText = rootView.findViewById(R.id.quantity);
        unitPriceEditText = rootView.findViewById(R.id.unit_price);
        totalPriceEditText = rootView.findViewById(R.id.total_price);
        descriptionEditText = rootView.findViewById(R.id.description);
        dateEditText = rootView.findViewById(R.id.date);
        invoiceDoneButton = rootView.findViewById(R.id.invoice_done_button);

        dateEditText.setOnClickListener(this);
        invoiceDoneButton.setOnClickListener(this);

        viewModel.getInvoiceDate().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                dateEditText.setText(day + "." + month+1 + "." + year + "." );
            }
        });

        if(invoiceToUpdate != null){
            viewModel.setViewModelMode(ViewModelMode.MODE_UPDATE);
            viewModel.setInvoiceToUpdate(invoiceToUpdate);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == dateEditText){
            showDatePicker();
        } else if(v == invoiceDoneButton){
            invoiceDoneButtonAction();
        }
    }

    private void invoiceDoneButtonAction() {
        int invoiceNumber = Integer.parseInt(invoiceNumberEditText.getText().toString());
        String customerName = customerNameEditText.getText().toString();
        int quantity = Integer.parseInt(quantityEditText.getText().toString());
        double unitPrice = Double.parseDouble(unitPriceEditText.getText().toString());
        double totoalPrice = Double.parseDouble(totalPriceEditText.getText().toString());
        String description = descriptionEditText.getText().toString();

        viewModel.handleData(new Invoice(propertyInfo.getId(), invoiceNumber, customerName,
                quantity, unitPrice, totoalPrice, description));
    }

    private void showDatePicker() {
        new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        viewModel.setYearMonthDay(year, month, dayOfMonth);
                    }
                },
                CustomTimeUtils.getCurrentYear(),
                CustomTimeUtils.getCurrentMonth(),
                CustomTimeUtils.getCurrentDay()).show();
    }
}

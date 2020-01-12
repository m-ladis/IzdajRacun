package hr.ml.izdajracun.view;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.utils.CustomTimeUtils;
import hr.ml.izdajracun.utils.DataValidationStatus;
import hr.ml.izdajracun.utils.ViewModelMode;
import hr.ml.izdajracun.viewmodel.InvoiceViewModel;


public class InvoiceFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "InvoiceFragment";

    private Animation shake;

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
    private Invoice invoice;
    private String invoiceNumber;
    private String customerName;
    private String quantity;
    private String unitPrice;
    private String totalPrice;
    private String description;
    private String date;

    public InvoiceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_invoice, container, false);

        Invoice invoiceToUpdate = (Invoice) getArguments().getSerializable("invoice");
        propertyInfo = (RentalPropertyInfo) getArguments().getSerializable("property");

        viewModel = ViewModelProviders.of(this).get(InvoiceViewModel.class);

        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

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
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                dateEditText.setText(day + "." + month + "." + year + "." );
            }
        });

        viewModel.dataValidationStatus.observe(this, new Observer<DataValidationStatus>() {
            @Override
            public void onChanged(DataValidationStatus dataValidationStatus) {
                switch (dataValidationStatus){
                    case DATA_HAS_EMPTY_FIELD:
                        startAnimationOnFirstEmptyEditText(invoiceNumberEditText,
                                customerNameEditText, quantityEditText, unitPriceEditText,
                                totalPriceEditText, dateEditText);
                        break;

                    case PRICE_NOT_VALID:
                        showUnitPriceNotValid();
                        break;

                    case VALID:
                        invoice = new Invoice(propertyInfo.getId(), Integer.parseInt(invoiceNumber),
                                customerName, Integer.parseInt(quantity), Double.parseDouble(unitPrice),
                                Double.parseDouble(totalPrice), description);

                        viewModel.handleData(invoice);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("property", propertyInfo);

                        NavHostFragment.findNavController(getParentFragment())
                                .navigate(R.id.action_invoiceFragment_to_propertyDashboard, bundle);

                        break;
                }

            }
        });

        if(invoiceToUpdate != null){
            viewModel.setViewModelMode(ViewModelMode.MODE_UPDATE);
            viewModel.setInvoiceToUpdate(invoiceToUpdate);

            //update views with invoice data
            invoiceNumberEditText.setText(String.valueOf(invoiceToUpdate.getNumber()));
            customerNameEditText.setText(invoiceToUpdate.getCustomerName());
            quantityEditText.setText(String.valueOf(invoiceToUpdate.getQuantity()));
            unitPriceEditText.setText(String.valueOf(invoiceToUpdate.getUnitPrice()));
            totalPriceEditText.setText(String.valueOf(invoiceToUpdate.getTotalPrice()));
            descriptionEditText.setText(invoiceToUpdate.getDescription());

            Calendar calendar = invoiceToUpdate.getDate();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            viewModel.setYearMonthDay(year, month, day);
        }

        return rootView;
    }

    private void showUnitPriceNotValid() {
        Toast.makeText(getContext(), R.string.unit_price_not_valid_message, Toast.LENGTH_LONG)
                .show();

        startAnimationOnView(unitPriceEditText);
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
        invoiceNumber = invoiceNumberEditText.getText().toString();
        customerName = customerNameEditText.getText().toString();
        quantity = quantityEditText.getText().toString();
        unitPrice = unitPriceEditText.getText().toString();
        totalPrice = totalPriceEditText.getText().toString();
        description = descriptionEditText.getText().toString();
        date = dateEditText.getText().toString();

        viewModel.isInvoiceDataValid(invoiceNumber, customerName, quantity,
                unitPrice, totalPrice, date);
    }

    private void startAnimationOnFirstEmptyEditText(EditText...editTexts){

        for (EditText editText : editTexts){
            if(editText.getText().toString().trim().isEmpty()){
                startAnimationOnView(editText);
                break;
            }
        }
    }

    private void startAnimationOnView(View view){
        view.startAnimation(shake);
        view.requestFocus();
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

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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.utils.CustomTimeUtils;
import hr.ml.izdajracun.utils.DataValidationStatus;
import hr.ml.izdajracun.utils.ViewModelMode;
import hr.ml.izdajracun.viewmodel.BuisnessInvoiceViewModel;

public class BusinessInvoiceFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "BusinessInvoiceFragment";

    private Animation shake;

    private EditText invoiceNumberEditText;
    private EditText customerNameEditText;
    private EditText customerAddressEditText;
    private EditText customerOibEditText;
    private EditText quantityEditText;
    private EditText unitPriceEditText;
    private EditText totalPriceEditText;
    private EditText descriptionEditText;
    private Spinner paymentMethodSpinner;
    private EditText issueDateEditText;
    private EditText deliveryDateEditText;
    private EditText payDueDateEditText;
    private FloatingActionButton invoiceDoneButton;

    private BuisnessInvoiceViewModel viewModel;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private BusinessInvoice businessInvoice;

    public BusinessInvoiceFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_business_invoice, container, false);

        viewModel = ViewModelProviders.of(this).get(BuisnessInvoiceViewModel.class);
        viewModel.start(getArguments());

        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        invoiceNumberEditText = rootView.findViewById(R.id.invoice_number);
        customerNameEditText = rootView.findViewById(R.id.customer_name);
        customerAddressEditText = rootView.findViewById(R.id.customer_address);
        customerOibEditText = rootView.findViewById(R.id.customer_oib);
        quantityEditText = rootView.findViewById(R.id.quantity);
        unitPriceEditText = rootView.findViewById(R.id.unit_price);
        totalPriceEditText = rootView.findViewById(R.id.total_price);
        descriptionEditText = rootView.findViewById(R.id.description);
        paymentMethodSpinner = rootView.findViewById(R.id.payment_method);
        issueDateEditText = rootView.findViewById(R.id.issue_date);
        deliveryDateEditText = rootView.findViewById(R.id.delivery_date);
        payDueDateEditText = rootView.findViewById(R.id.pay_due_date);
        invoiceDoneButton = rootView.findViewById(R.id.invoice_done_button);

        issueDateEditText.setOnClickListener(this);
        deliveryDateEditText.setOnClickListener(this);
        payDueDateEditText.setOnClickListener(this);
        invoiceDoneButton.setOnClickListener(this);

        arrayAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.payment_methods, R.layout.support_simple_spinner_dropdown_item);

        paymentMethodSpinner.setAdapter(arrayAdapter);

        viewModel.mode.observe(this, new Observer<ViewModelMode>() {
            @Override
            public void onChanged(ViewModelMode viewModelMode) {
                if(viewModelMode == ViewModelMode.MODE_UPDATE){
                    updateUIWithInvoice(viewModel.invoiceToUpdate);
                }
            }
        });

        viewModel.issueDate.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                issueDateEditText.setText(day + "." + month + "." + year + "." );
            }
        });

        viewModel.payDueDate.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                payDueDateEditText.setText(day + "." + month + "." + year + "." );
            }
        });

        viewModel.deliveryDate.observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                deliveryDateEditText.setText(day + "." + month + "." + year + "." );
            }
        });

        viewModel.dataValidationStatus.observe(this, new Observer<DataValidationStatus>() {
            @Override
            public void onChanged(DataValidationStatus dataValidationStatus) {
                switch (dataValidationStatus){
                    case DATA_HAS_EMPTY_FIELD:
                        startAnimationOnFirstEmptyEditText(invoiceNumberEditText,
                                customerNameEditText, customerAddressEditText, customerOibEditText,
                                quantityEditText, unitPriceEditText, totalPriceEditText,
                                issueDateEditText, payDueDateEditText, deliveryDateEditText);

                        break;

                    case PRICE_NOT_VALID:
                        showUnitPriceNotValid();
                        break;

                    case VALID:
                        viewModel.handleData(businessInvoice);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("property", viewModel.propertyInfo);

                        NavHostFragment.findNavController(getParentFragment())
                                .navigate(R.id.action_businessInvoiceFragment_to_propertyDashboard, bundle);

                        break;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == issueDateEditText){
            showDatePicker(issueDateEditText.getId());
        } else if(v == deliveryDateEditText){
            showDatePicker(deliveryDateEditText.getId());
        } else if (v == payDueDateEditText) {
            showDatePicker(payDueDateEditText.getId());
        } else if(v == invoiceDoneButton){
            invoiceDoneButtonAction();
        }
    }

    private void invoiceDoneButtonAction() {
        String invoiceNumber = invoiceNumberEditText.getText().toString();
        String customerName = customerNameEditText.getText().toString();
        String customerAddress = customerAddressEditText.getText().toString();
        String customerOib = customerOibEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String unitPrice = unitPriceEditText.getText().toString();
        String totalPrice = totalPriceEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String paymentMethod = (String) paymentMethodSpinner.getSelectedItem();
        String issueDate = issueDateEditText.getText().toString();
        String payDueDate = payDueDateEditText.getText().toString();
        String deliveryDate = deliveryDateEditText.getText().toString();

        try{
            Invoice invoice = new Invoice(Integer.parseInt(invoiceNumber), customerName,
                    Integer.parseInt(quantity), Double.parseDouble(unitPrice),
                    Double.parseDouble(totalPrice), description);

            businessInvoice = new BusinessInvoice(invoice,
                    customerAddress, customerOib, paymentMethod);

        } catch (Exception ignored){}

        viewModel.isInvoiceDataValid(invoiceNumber, customerName, quantity,
                unitPrice, totalPrice, issueDate, payDueDate, deliveryDate);
    }

    private void showDatePicker(final int id) {
        new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        switch (id){
                            case R.id.delivery_date:
                                viewModel.setDeliveryDate(year, month, dayOfMonth);
                                break;
                            case R.id.pay_due_date:
                                viewModel.setPayDueDate(year, month, dayOfMonth);
                                break;
                            case R.id.issue_date:
                                viewModel.setIssueDate(year, month, dayOfMonth);
                                break;
                        }
                    }
                },
                CustomTimeUtils.getCurrentYear(),
                CustomTimeUtils.getCurrentMonth(),
                CustomTimeUtils.getCurrentDay()).show();
    }

    private void updateUIWithInvoice(BusinessInvoice invoiceToUpdate) {
        invoiceNumberEditText.setText(String.valueOf(invoiceToUpdate.getNumber()));
        customerNameEditText.setText(invoiceToUpdate.getCustomerName());
        customerAddressEditText.setText(invoiceToUpdate.getCustomerAddress());
        customerOibEditText.setText(invoiceToUpdate.getCustomerOib());
        quantityEditText.setText(String.valueOf(invoiceToUpdate.getQuantity()));
        unitPriceEditText.setText(String.valueOf(invoiceToUpdate.getUnitPrice()));
        totalPriceEditText.setText(String.valueOf(invoiceToUpdate.getTotalPrice()));
        descriptionEditText.setText(invoiceToUpdate.getDescription());
        paymentMethodSpinner.setSelection(
                arrayAdapter.getPosition(invoiceToUpdate.getPaymentMethod()));
    }

    private void showUnitPriceNotValid() {
        Toast.makeText(getContext(), R.string.unit_price_not_valid_message, Toast.LENGTH_LONG)
                .show();

        startAnimationOnView(unitPriceEditText);
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
}

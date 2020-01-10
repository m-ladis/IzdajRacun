package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import hr.ml.izdajracun.R;


public class InvoiceFragment extends Fragment implements View.OnClickListener {

    private EditText invoiceNumberEditText;
    private EditText customerNameEditText;
    private EditText quantityEditText;
    private EditText unitPriceEditText;
    private EditText totalPriceEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;

    public InvoiceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_invoice, container, false);

        invoiceNumberEditText = rootView.findViewById(R.id.invoice_number);
        customerNameEditText = rootView.findViewById(R.id.customer_name);
        quantityEditText = rootView.findViewById(R.id.quantity);
        unitPriceEditText = rootView.findViewById(R.id.unit_price);
        totalPriceEditText = rootView.findViewById(R.id.total_price);
        descriptionEditText = rootView.findViewById(R.id.description);
        dateEditText = rootView.findViewById(R.id.date);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == dateEditText){
        }
    }
}

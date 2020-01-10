package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.ml.izdajracun.R;


public class InvoiceFragment extends Fragment {


    public InvoiceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_invoice, container, false);

        return rootView;
    }

}

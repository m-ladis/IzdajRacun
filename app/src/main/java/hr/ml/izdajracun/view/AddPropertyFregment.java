package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.ml.izdajracun.R;

public class AddPropertyFregment extends Fragment {

    private static final String TAG = "AddPropertyFregment";

    public AddPropertyFregment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_property_fregment, container, false);

        return root;
    }
}

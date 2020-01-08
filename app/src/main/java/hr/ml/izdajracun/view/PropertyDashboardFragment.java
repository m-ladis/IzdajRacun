package hr.ml.izdajracun.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.ml.izdajracun.R;

public class PropertyDashboardFragment extends Fragment {

    public PropertyDashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_property_dashboard, container, false);
    }

}

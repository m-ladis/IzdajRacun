package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hr.ml.izdajracun.R;

public class PropertiesFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addPropertyButton;

    public PropertiesFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_properties, container, false);

        //getting references to UI
        addPropertyButton = root.findViewById(R.id.addNewProperty);

        //adding onClickListeners
        addPropertyButton.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {
        if(v == addPropertyButton){
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertiesFragment_to_addPropertyFregment);
        }
    }
}

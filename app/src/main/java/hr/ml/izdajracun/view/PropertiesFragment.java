package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.adapter.PropertiesAdapter;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.viewmodel.PropertiesViewModel;

public class PropertiesFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PropertiesFragment";

    private FloatingActionButton addPropertyButton;
    private RecyclerView propertiesRecyclerView;
    private PropertiesAdapter adapter;

    public PropertiesFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_properties, container, false);

        //getting references to UI
        addPropertyButton = root.findViewById(R.id.addNewProperty);
        propertiesRecyclerView = root.findViewById(R.id.properties);
        propertiesRecyclerView.addItemDecoration(new DividerItemDecoration(
                propertiesRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        //adding onClickListeners
        addPropertyButton.setOnClickListener(this);

        //recycler view now can navigate fragments
        Navigation.setViewNavController(propertiesRecyclerView,
                NavHostFragment.findNavController(this));

        adapter = new PropertiesAdapter(getContext());
        propertiesRecyclerView.setAdapter(adapter);

        PropertiesViewModel viewModel = ViewModelProviders.of(this)
                .get(PropertiesViewModel.class);

        viewModel.properties.observe(this, new Observer<List<RentalPropertyInfo>>() {
            @Override
            public void onChanged(List<RentalPropertyInfo> rentalPropertyInfos) {
                adapter.setProperties(rentalPropertyInfos);
                adapter.notifyDataSetChanged();
            }
        });

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

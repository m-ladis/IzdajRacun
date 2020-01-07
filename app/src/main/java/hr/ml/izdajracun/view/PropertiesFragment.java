package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.adapter.PropertiesAdapter;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.viewmodel.RentalPropertyViewModel;

public class PropertiesFragment extends Fragment implements View.OnClickListener,
        OnItemClickListener {

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

        //adding onClickListeners
        addPropertyButton.setOnClickListener(this);

        adapter = new PropertiesAdapter(getContext());
        adapter.setOnItemClickedListener(this);
        propertiesRecyclerView.setAdapter(adapter);

        RentalPropertyViewModel propertyViewModel = ViewModelProviders.of(this)
                .get(RentalPropertyViewModel.class);

        propertyViewModel.properties.observe(this, new Observer<List<RentalPropertyInfo>>() {
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

    @Override
    public void itemClicked(int position) {
        Log.d(TAG, "clicked" + Integer.valueOf(position));
        String name = adapter.getPropertyAtIndex(position).getName();
        Log.d(TAG, "cliked " + name);
    }
}

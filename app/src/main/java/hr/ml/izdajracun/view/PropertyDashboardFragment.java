package hr.ml.izdajracun.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

public class PropertyDashboardFragment extends Fragment {

    private static final int editMenuItemId = 1000;

    private RentalPropertyInfo propertyInfo;

    public PropertyDashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_property_dashboard, container, false);

        setHasOptionsMenu(true);

        propertyInfo = (RentalPropertyInfo) getArguments().getSerializable("property");

        TextView propertyName = rootView.findViewById(R.id.property_name);
        propertyName.setText(propertyInfo.getName());


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.NONE, editMenuItemId, Menu.NONE, R.string.edit_property_info_menu_item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == editMenuItemId){

            Bundle bundle = new Bundle();
            bundle.putSerializable("property", propertyInfo);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertyDashboard_to_addPropertyFregment, bundle);
        }
        return true;
    }
}

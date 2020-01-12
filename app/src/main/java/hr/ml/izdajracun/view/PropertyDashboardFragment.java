package hr.ml.izdajracun.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.viewmodel.PropertyDashboardViewModel;

public class PropertyDashboardFragment extends Fragment implements View.OnClickListener, Observer {

    private static final String TAG = "PropertyDashboard";
    private static final int editMenuItemId = 1000;

    private RentalPropertyInfo propertyInfo;

    private FloatingActionButton incrementYearButton;
    private FloatingActionButton decrementYearButton;
    private FloatingActionButton newInvoiceButton;

    PropertyDashboardViewModel propertyDashboardViewModel;
    private TextView currentYearTextView;

    public PropertyDashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_property_dashboard, container, false);

        setHasOptionsMenu(true);

        propertyInfo = (RentalPropertyInfo) getArguments().getSerializable("property");

        //getting references to UI
        newInvoiceButton = rootView.findViewById(R.id.new_invoice);
        incrementYearButton = rootView.findViewById(R.id.increment_year);
        decrementYearButton = rootView.findViewById(R.id.decrement_year);
        TextView propertyNameTextView = rootView.findViewById(R.id.property_name);
        TextView propertyAddressTextView = rootView.findViewById(R.id.property_address);
        TextView ownerNameTextView = rootView.findViewById(R.id.owner_name);
        currentYearTextView = rootView.findViewById(R.id.year);

        //add content to views
        propertyNameTextView.setText(propertyInfo.getName());
        propertyAddressTextView.setText(propertyInfo.getAddress());
        ownerNameTextView.setText(
                propertyInfo.getOwnerFirstName() + " " + propertyInfo.getOwnerLastName());

        //set listeners
        incrementYearButton.setOnClickListener(this);
        decrementYearButton.setOnClickListener(this);
        newInvoiceButton.setOnClickListener(this);

        propertyDashboardViewModel = ViewModelProviders.of(this)
                .get(PropertyDashboardViewModel.class);

        propertyDashboardViewModel.selectedYear.observe(this, this);
        propertyDashboardViewModel.invoices.observe(this, this);

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

    @Override
    public void onClick(View v) {
        if(v == incrementYearButton){
            propertyDashboardViewModel.incrementYear();
        } else if (v == decrementYearButton){
            propertyDashboardViewModel.decrementYear();
        } else if (v == newInvoiceButton){
            Bundle args = new Bundle();
            args.putSerializable("property", propertyInfo);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertyDashboard_to_invoiceFragment, args);
        }
    }

    @Override
    public void onChanged(Object o) {
        if(o instanceof List){
            List<Invoice> invoices = (List<Invoice>) o;
            for(Invoice invoice : invoices){
                Log.d(TAG, invoice.getCustomerName());
            }

        } else if (o instanceof Integer){
            currentYearTextView.setText(o.toString());
            propertyDashboardViewModel.invoices.removeObservers(this);
            propertyDashboardViewModel.invoiceYearChanged();
            propertyDashboardViewModel.invoices.observe(this, this);
        }
    }
}

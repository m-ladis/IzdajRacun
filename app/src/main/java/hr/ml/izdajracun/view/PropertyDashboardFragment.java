package hr.ml.izdajracun.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

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
import hr.ml.izdajracun.adapter.InvoicesAdapter;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.viewmodel.PropertyDashboardViewModel;

public class PropertyDashboardFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PropertyDashboard";
    private static final int editMenuItemId = 1000;

    private RentalPropertyInfo propertyInfo;

    private RecyclerView invoicesRecyclerView;
    private TextView currentYearTextView;
    private FloatingActionButton incrementYearButton;
    private FloatingActionButton decrementYearButton;
    private FloatingActionButton newInvoiceButton;

    private InvoicesAdapter adapter;
    private PropertyDashboardViewModel propertyDashboardViewModel;
    private Bundle bundle;

    public PropertyDashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_property_dashboard, container, false);

        setHasOptionsMenu(true);

        bundle = getArguments();

        propertyInfo = (RentalPropertyInfo) bundle.getSerializable("property");

        //getting references to UI
        invoicesRecyclerView = rootView.findViewById(R.id.invoices);
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
        invoicesRecyclerView.addItemDecoration(new DividerItemDecoration(
                invoicesRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        //set listeners
        incrementYearButton.setOnClickListener(this);
        decrementYearButton.setOnClickListener(this);
        newInvoiceButton.setOnClickListener(this);

        propertyDashboardViewModel = ViewModelProviders.of(this)
                .get(PropertyDashboardViewModel.class);

        Navigation.setViewNavController(invoicesRecyclerView,
                NavHostFragment.findNavController(this));

        propertyDashboardViewModel.selectedYear.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentYearTextView.setText(integer.toString());

                propertyDashboardViewModel.invoices.removeObservers(getViewLifecycleOwner());
                propertyDashboardViewModel.invoiceYearChanged();
                propertyDashboardViewModel.invoices.observe(getViewLifecycleOwner(),
                        new Observer<List<Invoice>>() {
                    @Override
                    public void onChanged(List<Invoice> invoices) {
                        adapter.setInvoices(invoices);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        adapter = new InvoicesAdapter(getContext(), bundle);
        invoicesRecyclerView.setAdapter(adapter);

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

            Bundle bundle = new Bundle();
            bundle.putSerializable("property", propertyInfo);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertyDashboard_to_invoiceFragment, bundle);
        }
    }
}

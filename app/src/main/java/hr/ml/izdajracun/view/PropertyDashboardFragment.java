package hr.ml.izdajracun.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.adapter.InvoicesAdapter;
import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.utils.InvoiceGenerator;
import hr.ml.izdajracun.viewmodel.PropertyDashboardViewModel;

public class PropertyDashboardFragment extends Fragment implements View.OnClickListener,
        OnInvoicePopupMenuItemSelectedListener{

    private static final String TAG = "PropertyDashboard";
    private static final int RC_PERMISSION_WRITE_EXTERNAL = 100;
    private static final int editMenuItemId = 1000;
    private static final int deleteAllIMenutemId = 1001;

    private RentalPropertyInfo propertyInfo;

    private RecyclerView invoicesRecyclerView;
    private TextView currentYearTextView;
    private FloatingActionButton incrementYearButton;
    private FloatingActionButton decrementYearButton;
    private ExtendedFloatingActionButton newPersonalInvoiceButton;
    private ExtendedFloatingActionButton newBusinessInvoiceButton;
    private FloatingActionButton invoiceButton;

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
        newPersonalInvoiceButton = rootView.findViewById(R.id.new_personal_invoice);
        newBusinessInvoiceButton = rootView.findViewById(R.id.new_business_invioce);
        invoiceButton = rootView.findViewById(R.id.new_invoice);
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
        newPersonalInvoiceButton.setOnClickListener(this);
        newBusinessInvoiceButton.setOnClickListener(this);
        invoiceButton.setOnClickListener(this);

        propertyDashboardViewModel = ViewModelProviders.of(this)
                .get(PropertyDashboardViewModel.class);
        propertyDashboardViewModel.start(getArguments());
        propertyDashboardViewModel.updateInvoices();
        propertyDashboardViewModel.updateBusinessInvoices();

        propertyDashboardViewModel.selectedYear.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentYearTextView.setText(integer.toString());

                propertyDashboardViewModel.invoices.removeObservers(getViewLifecycleOwner());
                propertyDashboardViewModel.businessInvoices.removeObservers(getViewLifecycleOwner());
                propertyDashboardViewModel.updateInvoices();
                propertyDashboardViewModel.updateBusinessInvoices();
                propertyDashboardViewModel.invoices.observe(getViewLifecycleOwner(),
                        new Observer<List<Invoice>>() {
                    @Override
                    public void onChanged(List<Invoice> invoices) {
                        adapter.setInvoices(invoices);
                        adapter.notifyDataSetChanged();
                    }
                });
                propertyDashboardViewModel.businessInvoices.observe(getViewLifecycleOwner(),
                        new Observer<List<BusinessInvoice>>() {
                    @Override
                    public void onChanged(List<BusinessInvoice> businessInvoices) {
                        adapter.setBusinessInvoices(businessInvoices);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        adapter = new InvoicesAdapter(getContext());
        adapter.setItemOptionsListener(this);
        invoicesRecyclerView.setAdapter(adapter);

        checkStoragePermission();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.NONE, editMenuItemId, Menu.NONE, R.string.edit_property_info_menu_item);
        menu.add(Menu.NONE, deleteAllIMenutemId, Menu.NONE, R.string.delete_all_property_info_menu_item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == editMenuItemId){
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertyDashboard_to_addPropertyFregment, getArguments());

        } else if(item.getItemId() == deleteAllIMenutemId){
            propertyDashboardViewModel.deleteAllPropertyData();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertyDashboard_to_propertiesFragment);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == incrementYearButton){
            propertyDashboardViewModel.incrementYear();
        } else if (v == decrementYearButton){
            propertyDashboardViewModel.decrementYear();
        } else if (v == newPersonalInvoiceButton){

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertyDashboard_to_invoiceFragment, getArguments());

        } else if (v == newBusinessInvoiceButton){

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_propertyDashboard_to_businessInvoiceFragment, getArguments());

        } else if (v == invoiceButton) {
            int visibility = newBusinessInvoiceButton.getVisibility();

            if (visibility == View.GONE){
                visibility = View.VISIBLE;
            } else if (visibility == View.VISIBLE){
                visibility = View.GONE;
            }

            newBusinessInvoiceButton.setVisibility(visibility);
            newPersonalInvoiceButton.setVisibility(visibility);
        }
    }

    @Override
    public void edit(Invoice invoice) {
        bundle.putSerializable("invoice", invoice);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_propertyDashboard_to_invoiceFragment, bundle);
    }

    @Override
    public void delete(Invoice invoice) {
        propertyDashboardViewModel.deleteInvoice(invoice);
    }

    @Override
    public void exportAsPdf(Invoice invoice) {
        try {
            InvoiceGenerator.generate(invoice);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(BusinessInvoice invoice) {
        bundle.putSerializable("invoice", invoice);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_propertyDashboard_to_businessInvoiceFragment, bundle);
    }

    @Override
    public void delete(BusinessInvoice invoice) {
        propertyDashboardViewModel.deleteInvoice(invoice);
    }

    @Override
    public void exportAsPdf(BusinessInvoice invoice) {
        try {
            InvoiceGenerator.generate(invoice);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public  void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");

            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSION_WRITE_EXTERNAL);
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
        }
    }
}

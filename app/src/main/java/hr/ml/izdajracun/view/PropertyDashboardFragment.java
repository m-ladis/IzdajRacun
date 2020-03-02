package hr.ml.izdajracun.view;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.adapter.InvoicesAdapter;
import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.utils.InvoiceGenerator;
import hr.ml.izdajracun.viewmodel.PropertyDashboardViewModel;

public class PropertyDashboardFragment extends Fragment implements View.OnClickListener,
        OnSelectedInvoiceListener {

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
                        new Observer<List<MinimalInvoice>>() {
                    @Override
                    public void onChanged(List<MinimalInvoice> invoices) {
                        adapter.setInvoices(invoices);
                        adapter.notifyDataSetChanged();
                    }
                });
                propertyDashboardViewModel.businessInvoices.observe(getViewLifecycleOwner(),
                        new Observer<List<MinimalBusinessInvoice>>() {
                    @Override
                    public void onChanged(List<MinimalBusinessInvoice> businessInvoices) {
                        adapter.setBusinessInvoices(businessInvoices);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        propertyDashboardViewModel.fileToOpen.observe(this, new Observer<File>() {
            @Override
            public void onChanged(File file) {
                openPdf(file);
            }
        });

        adapter = new InvoicesAdapter(getContext());
        adapter.setSelectedInvoiceListener(this);
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
    public void edit(MinimalInvoice minimalInvoice) {
        propertyDashboardViewModel.getInvoiceById(minimalInvoice.getId());
        propertyDashboardViewModel.invoice.observe(this, new Observer<Invoice>() {
            @Override
            public void onChanged(Invoice invoice) {
                bundle.putSerializable("invoice", invoice);

                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_propertyDashboard_to_invoiceFragment, bundle);
            }
        });
    }

    @Override
    public void delete(MinimalInvoice minimalInvoice) {
        propertyDashboardViewModel.deleteInvoiceById(minimalInvoice.getId());
    }

    @Override
    public void open(MinimalInvoice minimalInvoice) {
        propertyDashboardViewModel.getInvoiceById(minimalInvoice.getId());
        propertyDashboardViewModel.invoice.observe(this, new Observer<Invoice>() {
            @Override
            public void onChanged(Invoice invoice) {
                propertyDashboardViewModel.openPdf(invoice);
            }
        });
    }

    @Override
    public void edit(MinimalBusinessInvoice minimalBusinessInvoice) {
        propertyDashboardViewModel.getBusinessInvoiceById(minimalBusinessInvoice.getId());
        propertyDashboardViewModel.businessInvoice.observe(this, new Observer<BusinessInvoice>() {
            @Override
            public void onChanged(BusinessInvoice invoice) {
                bundle.putSerializable("invoice", invoice);

                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_propertyDashboard_to_businessInvoiceFragment, bundle);
            }
        });
    }

    @Override
    public void delete(MinimalBusinessInvoice minimalBusinessInvoice) {
        propertyDashboardViewModel.deleteBusinessInvoiceById(minimalBusinessInvoice.getId());
    }

    @Override
    public void open(MinimalBusinessInvoice minimalBusinessInvoice) {
        propertyDashboardViewModel.getBusinessInvoiceById(minimalBusinessInvoice.getId());
        propertyDashboardViewModel.businessInvoice.observe(this, new Observer<BusinessInvoice>() {
            @Override
            public void onChanged(BusinessInvoice invoice) {
                propertyDashboardViewModel.openPdf(invoice);
            }
        });
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

    private void openPdf(File file){
        Context context = getContext();
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "Install .pdf viewer");
        }
    }
}

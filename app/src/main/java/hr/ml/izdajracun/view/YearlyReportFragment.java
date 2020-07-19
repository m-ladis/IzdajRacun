package hr.ml.izdajracun.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.model.entity.YearlyReport;
import hr.ml.izdajracun.utils.YearlyReportGenerator;
import hr.ml.izdajracun.viewmodel.YearlyReportViewModel;

public class YearlyReportFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "YearlyReportFragment";

    private EditText taxpayerFullNameEditText;
    private EditText taxpayerOibEditText;
    private EditText taxpayerPlaceEditText;
    private EditText taxpayerAddressEditText;
    private Spinner yearsSpinner;
    private FloatingActionButton doneButton;

    private ArrayAdapter<Integer> spinnerAdapter ;

    private YearlyReportViewModel viewModel;
    private YearlyReport yearlyReport;

    public YearlyReportFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_yearly_report, container, false);

        taxpayerFullNameEditText = rootView.findViewById(R.id.taxpayer_fullname);
        taxpayerOibEditText = rootView.findViewById(R.id.taxpayer_oib);
        taxpayerPlaceEditText = rootView.findViewById(R.id.taxpayer_place);
        taxpayerAddressEditText = rootView.findViewById(R.id.taxpayer_address);
        yearsSpinner = rootView.findViewById(R.id.years);
        doneButton = rootView.findViewById(R.id.yearly_report_done_button);

        doneButton.setOnClickListener(this);

        spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item);
        yearsSpinner.setAdapter(spinnerAdapter);

        viewModel = ViewModelProviders.of(this).get(YearlyReportViewModel.class);
        viewModel.start(getArguments());

        viewModel.invoiceYears.observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> years) {
                addSortedYearsToSpinner(years);
            }
        });

        viewModel.businessInvoiceYears.observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> years) {
                addSortedYearsToSpinner(years);
            }
        });

        RentalPropertyInfo propertyInfo = viewModel.getPropertyInfo();
        taxpayerFullNameEditText.setText(propertyInfo.getOwnerFirstName() + " "
                + propertyInfo.getOwnerLastName());
        taxpayerOibEditText.setText(propertyInfo.getOwnerOIB());

        return rootView;
    }

    private void addSortedYearsToSpinner(List<Integer> years) {
        for(Integer year : years){
            if (spinnerAdapter.getPosition(year) == -1) spinnerAdapter.add(year);
        }

        spinnerAdapter.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // decrement order
                return Integer.compare(o2, o1);
            }
        });

        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == doneButton) {
            doneButtonAction();
        }
    }

    private void doneButtonAction() {
        String fullname = taxpayerFullNameEditText.getText().toString();
        String oib = taxpayerOibEditText.getText().toString();
        String place = taxpayerPlaceEditText.getText().toString();
        String address = taxpayerAddressEditText.getText().toString();
        Integer reportYear = spinnerAdapter.getItem(yearsSpinner.getSelectedItemPosition());

        yearlyReport = new YearlyReport(reportYear, fullname, oib, place, address);

        viewModel.getMinimalBusinessInvoicesInYear(reportYear);
        viewModel.getMinimalInvoicesInYear(reportYear);

        viewModel.minimalInvoices.observe(this, new Observer<List<MinimalInvoice>>() {
            @Override
            public void onChanged(List<MinimalInvoice> minimalInvoices) {
                viewModel.minimalInvoices.removeObservers(getViewLifecycleOwner());
                yearlyReport.setInvoices(minimalInvoices);

                if(yearlyReport.isComplete()) {
                    yearlyReport.generate();
                    try{
                        YearlyReportGenerator reportGenerator = new YearlyReportGenerator(
                                yearlyReport.getYear(), viewModel.getPropertyInfo(),
                                yearlyReport.getAllInvoices(), yearlyReport.getTaxpayerAddress(),
                                yearlyReport.getTaxpayerPlace());

                        File yearlyReport = reportGenerator.generate();

                        openPdf(yearlyReport);

                    } catch (DocumentException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        viewModel.businessMinimalInvoices.observe(this, new Observer<List<MinimalBusinessInvoice>>() {
            @Override
            public void onChanged(List<MinimalBusinessInvoice> businessInvoices) {
                viewModel.businessMinimalInvoices.removeObservers(getViewLifecycleOwner());
                yearlyReport.setBusinessInvoices(businessInvoices);

                if(yearlyReport.isComplete()) {
                    yearlyReport.generate();
                    try{
                        YearlyReportGenerator reportGenerator = new YearlyReportGenerator(
                                yearlyReport.getYear(), viewModel.getPropertyInfo(),
                                yearlyReport.getAllInvoices(), yearlyReport.getTaxpayerAddress(),
                                yearlyReport.getTaxpayerPlace());

                        File yearlyReport = reportGenerator.generate();

                        openPdf(yearlyReport);

                    } catch (DocumentException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

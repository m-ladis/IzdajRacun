package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.viewmodel.AddEditPropertyViewModel;


public class AddEditPropertyFragment extends Fragment implements View.OnClickListener, Observer<AddEditPropertyViewModel.DataValidationStatus>{

    private static final String TAG = "AddEditPropertyFragment";

    private Animation shake;

    private FloatingActionButton doneButton;

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText ownerFirstNameEditText;
    private EditText ownerLastNameEditText;
    private EditText ownerIbanEditText;
    private EditText ownerOibEditText;

    private AddEditPropertyViewModel viewModel;

    public AddEditPropertyFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater
                .inflate(R.layout.fragment_add_property_fregment, container, false);

        viewModel = ViewModelProviders.of(this)
                .get(AddEditPropertyViewModel.class);

        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        nameEditText = root.findViewById(R.id.rental_property_name);
        addressEditText = root.findViewById(R.id.rental_property_address);
        ownerFirstNameEditText = root.findViewById(R.id.owner_first_name);
        ownerLastNameEditText = root.findViewById(R.id.owner_last_name);
        ownerIbanEditText = root.findViewById(R.id.owner_iban);
        ownerOibEditText = root.findViewById(R.id.owner_oib);
        doneButton = root.findViewById(R.id.done_button);

        doneButton.setOnClickListener(this);

        if (getArguments() != null) {
            RentalPropertyInfo propertyInfo = (RentalPropertyInfo) getArguments()
                    .getSerializable("property");

            viewModel.setMode(AddEditPropertyViewModel.Mode.MODE_UPDATE);
            viewModel.setPropertyInfoToUpdate(propertyInfo);

            nameEditText.setText(propertyInfo.getName());
            addressEditText.setText(propertyInfo.getAddress());
            ownerFirstNameEditText.setText(propertyInfo.getOwnerFirstName());
            ownerLastNameEditText.setText(propertyInfo.getOwnerLastName());
            ownerIbanEditText.setText(propertyInfo.getOwnerIBAN());
            ownerOibEditText.setText(propertyInfo.getOwnerOIB());
        }

        viewModel.dataValidationStatus.observe(this, this);

        return root;
    }

    @Override
    public void onChanged(AddEditPropertyViewModel.DataValidationStatus validationStatus) {
        switch (validationStatus){
            case DATA_HAS_EMPTY_FIELD:
                startAnimationOnFirstEmptyEditText(nameEditText, ownerFirstNameEditText,
                        ownerLastNameEditText, ownerOibEditText,
                        ownerIbanEditText, addressEditText);

                break;
            case OIB_NOT_VALID:
                showOibNotValid();
                break;
            case IBAN_NOT_VALID:
                showIbanNotValid();
                break;
            case VALID:
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_addPropertyFregment_to_propertiesFragment);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == doneButton){
            doneButtonAction();
        }
    }

    private void doneButtonAction() {
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String ownerFirstName = ownerFirstNameEditText.getText().toString().trim();
        String ownerLastName = ownerLastNameEditText.getText().toString().trim();
        String ownerIban = ownerIbanEditText.getText().toString().trim();
        String ownerOib = ownerOibEditText.getText().toString().trim();


        RentalPropertyInfo rentalPropertyInfo = new RentalPropertyInfo(
                name, address, ownerFirstName,
                ownerLastName, ownerIban, ownerOib);

        viewModel.handlePropertyInfo(rentalPropertyInfo);

    }

    private void showIbanNotValid() {
        Toast.makeText(getContext(), R.string.non_valid_oib_toast_content, Toast.LENGTH_SHORT)
                .show();

        startAnimationOnView(ownerOibEditText);
    }

    private void showOibNotValid() {
        Toast.makeText(getContext(), R.string.non_valid_iban_toast_content, Toast.LENGTH_SHORT)
                .show();

        startAnimationOnView(ownerIbanEditText);
    }

    private void startAnimationOnFirstEmptyEditText(EditText...editTexts){

        for (EditText editText : editTexts){
            if(editText.getText().toString().trim().isEmpty()){
                startAnimationOnView(editText);
                break;
            }
        }
    }

    private void startAnimationOnView(View view){
        view.startAnimation(shake);
        view.requestFocus();
    }
}

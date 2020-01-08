package hr.ml.izdajracun.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import hr.ml.izdajracun.repository.IzdajRacunRepository;

public class AddPropertyFregment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddPropertyFregment";

    private Animation shake;

    private FloatingActionButton doneButton;

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText ownerFirstNameEditText;
    private EditText ownerLastNameEditText;
    private EditText ownerIbanEditText;
    private EditText ownerOibEditText;

    private String name;
    private String address;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerIban;
    private String ownerOib;
    private IzdajRacunRepository repository;

    public AddPropertyFregment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater
                .inflate(R.layout.fragment_add_property_fregment, container, false);

        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        nameEditText = root.findViewById(R.id.rental_property_name);
        addressEditText = root.findViewById(R.id.rental_property_address);
        ownerFirstNameEditText = root.findViewById(R.id.owner_first_name);
        ownerLastNameEditText = root.findViewById(R.id.owner_last_name);
        ownerIbanEditText = root.findViewById(R.id.owner_iban);
        ownerOibEditText = root.findViewById(R.id.owner_oib);
        doneButton = root.findViewById(R.id.done_button);

        doneButton.setOnClickListener(this);

        repository = new IzdajRacunRepository(getActivity().getApplication());

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == doneButton){
            doneButtonAction();
        }
    }

    private void doneButtonAction() {
        name = nameEditText.getText().toString().trim();
        address = addressEditText.getText().toString().trim();
        ownerFirstName = ownerFirstNameEditText.getText().toString().trim();
        ownerLastName = ownerLastNameEditText.getText().toString().trim();
        ownerIban = ownerIbanEditText.getText().toString().trim();
        ownerOib = ownerOibEditText.getText().toString().trim();

        if(!isAnyEditTextFieldEmpty() && isOIB(ownerOib) && isIBAN(ownerIban)){
            RentalPropertyInfo rentalPropertyInfo = new RentalPropertyInfo(
                    name, address,ownerFirstName,
                    ownerLastName, ownerIban, ownerOib);

            repository.insert(rentalPropertyInfo);

            //navigate to PropertiesFragment
            NavHostFragment.findNavController(this)
                    .popBackStack();
        }
    }

    private boolean isAnyEditTextFieldEmpty(){

        if (name.isEmpty()) {
            nameEditText.startAnimation(shake);
            nameEditText.requestFocus();

            return true;

        } else if(ownerFirstName.isEmpty()){
            ownerFirstNameEditText.startAnimation(shake);
            ownerFirstNameEditText.requestFocus();

            return true;

        } else if(ownerLastName.isEmpty()){
            ownerLastNameEditText.startAnimation(shake);
            ownerLastNameEditText.requestFocus();

            return true;

        } else if(ownerOib.isEmpty()){
            ownerOibEditText.startAnimation(shake);
            ownerOibEditText.requestFocus();

            return true;

        } else if(ownerIban.isEmpty()){
            ownerIbanEditText.startAnimation(shake);
            ownerIbanEditText.requestFocus();

            return true;

        } else if(address.isEmpty()){
            addressEditText.startAnimation(shake);
            addressEditText.requestFocus();

            return true;

        }

        return false;

    }

    private boolean isOIB(String OIB){
        if(OIB.length() == 11){
            return true;
        }
        else{
            Toast.makeText(getContext(), "OIB mora sadržavati 11 brojeva.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isIBAN(String IBAN){
        if(IBAN.length() == 21){
            return true;
        }
        else{
            Toast.makeText(getContext(), "IBAN mora sadržavati 21 znak.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

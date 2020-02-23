package hr.ml.izdajracun.view;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.utils.Base64Utils;
import hr.ml.izdajracun.utils.DataValidationStatus;
import hr.ml.izdajracun.utils.ViewModelMode;
import hr.ml.izdajracun.viewmodel.AddEditPropertyViewModel;

import static android.app.Activity.*;


public class AddEditPropertyFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddEditPropertyFragment";
    private static final int RC_CHOOSE_LOGO = 100;

    private Animation shake;

    private FloatingActionButton doneButton;
    private Button selectLogoButton;
    private Button deleteLogoButton;
    private ImageView logoPreviewImageView;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText ownerFirstNameEditText;
    private EditText ownerLastNameEditText;
    private EditText ownerIbanEditText;
    private EditText ownerOibEditText;

    private RentalPropertyInfo rentalPropertyInfo;
    private AddEditPropertyViewModel viewModel;

    public AddEditPropertyFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater
                .inflate(R.layout.fragment_add_property_fregment, container, false);

        viewModel = ViewModelProviders.of(this).get(AddEditPropertyViewModel.class);
        viewModel.start(getArguments());

        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        nameEditText = root.findViewById(R.id.rental_property_name);
        addressEditText = root.findViewById(R.id.rental_property_address);
        ownerFirstNameEditText = root.findViewById(R.id.owner_first_name);
        ownerLastNameEditText = root.findViewById(R.id.owner_last_name);
        ownerIbanEditText = root.findViewById(R.id.owner_iban);
        ownerOibEditText = root.findViewById(R.id.owner_oib);
        logoPreviewImageView = root.findViewById(R.id.logo_preview);
        selectLogoButton = root.findViewById(R.id.select_logo);
        deleteLogoButton = root.findViewById(R.id.delete_logo);
        doneButton = root.findViewById(R.id.done_button);

        deleteLogoButton.setOnClickListener(this);
        selectLogoButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

        viewModel.mode.observe(this, new Observer<ViewModelMode>() {
            @Override
            public void onChanged(ViewModelMode viewModelMode) {
                if(viewModelMode == ViewModelMode.MODE_UPDATE){
                    RentalPropertyInfo propertyInfo = viewModel.getPropertyInfoToUpdate();

                    nameEditText.setText(propertyInfo.getName());
                    addressEditText.setText(propertyInfo.getAddress());
                    ownerFirstNameEditText.setText(propertyInfo.getOwnerFirstName());
                    ownerLastNameEditText.setText(propertyInfo.getOwnerLastName());
                    ownerIbanEditText.setText(propertyInfo.getOwnerIBAN());
                    ownerOibEditText.setText(propertyInfo.getOwnerOIB());

                    if(propertyInfo.getLogoImage() != null){
                        logoPreviewImageView.setImageBitmap(Base64Utils.stringToBitmap(
                                propertyInfo.getLogoImage()));

                        Log.d(TAG, String.valueOf(propertyInfo.getLogoImage().getBytes().length));
                    }
                }
            }
        });

        viewModel.dataValidationStatus.observe(this,
                new Observer<DataValidationStatus>() {
                    @Override
                    public void onChanged(DataValidationStatus validationStatus) {
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
                                viewModel.handlePropertyInfo(rentalPropertyInfo);
                                navigate();
                                break;
                        }
                    }
                });

        return root;
    }

    private void navigate() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_addPropertyFregment_to_propertiesFragment);
    }

    @Override
    public void onClick(View v) {
        if(v == doneButton){
            doneButtonAction();
        } else if(v == selectLogoButton){
            selectLogoButtonAction();
        } else if(v == deleteLogoButton){
            deleteLogoButtonAction();
        }
    }

    private void deleteLogoButtonAction() {
        logoPreviewImageView.setImageDrawable(null);
    }

    private void selectLogoButtonAction() {
        Intent chooseLogo = new Intent(Intent.ACTION_GET_CONTENT);
        chooseLogo.setType("image/*");
        startActivityForResult(Intent.createChooser(chooseLogo, "Choose logo"), RC_CHOOSE_LOGO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_CHOOSE_LOGO && resultCode == RESULT_OK){
            if(data != null){
                Uri uri = data.getData();
                ContentResolver contentResolver = getActivity().getContentResolver();
                try {
                    InputStream inputStream = contentResolver.openInputStream(uri);
                    Bitmap bitImage = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaledImage = Bitmap.createScaledBitmap(bitImage, 250,250,true);
                    logoPreviewImageView.setImageBitmap(scaledImage);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doneButtonAction() {
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String ownerFirstName = ownerFirstNameEditText.getText().toString().trim();
        String ownerLastName = ownerLastNameEditText.getText().toString().trim();
        String ownerIban = ownerIbanEditText.getText().toString().trim();
        String ownerOib = ownerOibEditText.getText().toString().trim();
        String logoImage = Base64Utils.drawableToString(logoPreviewImageView.getDrawable());

        rentalPropertyInfo = new RentalPropertyInfo(
                name, address, ownerFirstName,
                ownerLastName, ownerIban, ownerOib, logoImage);

        viewModel.isPropertyInfoDataValid(rentalPropertyInfo);

    }

    private void showIbanNotValid() {
        Toast.makeText(getContext(), R.string.non_valid_oib_toast_content, Toast.LENGTH_SHORT)
                .show();

        startAnimationOnView(ownerIbanEditText);
    }

    private void showOibNotValid() {
        Toast.makeText(getContext(), R.string.non_valid_iban_toast_content, Toast.LENGTH_SHORT)
                .show();

        startAnimationOnView(ownerOibEditText);
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

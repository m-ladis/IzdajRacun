package hr.ml.izdajracun.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.RentalPropertyInfoRepository;
import hr.ml.izdajracun.utils.DataValidationStatus;
import hr.ml.izdajracun.utils.InputFieldValidator;
import hr.ml.izdajracun.utils.ViewModelMode;

public class AddEditPropertyViewModel extends AndroidViewModel {

    private static final String TAG = "AddEditPropertyVM";

    public MutableLiveData<DataValidationStatus> dataValidationStatus = new MutableLiveData<>();
    public MutableLiveData<ViewModelMode> mode = new MutableLiveData<>();

    private RentalPropertyInfo propertyInfoToUpdate;
    private RentalPropertyInfoRepository repository;

    public AddEditPropertyViewModel(@NonNull Application application) {
        super(application);

        repository = new RentalPropertyInfoRepository(application);
    }

    public void start(Bundle bundle){
        if (bundle == null){
            mode.setValue(ViewModelMode.MODE_ADD);
        } else {
            mode.setValue(ViewModelMode.MODE_UPDATE);
            propertyInfoToUpdate = (RentalPropertyInfo) bundle.getSerializable("property");
        }
    }

    public void handlePropertyInfo(RentalPropertyInfo propertyInfo) {
        switch (mode.getValue()) {
            case MODE_ADD:
                repository.insert(propertyInfo);
                break;
            case MODE_UPDATE:
                propertyInfo.setId(propertyInfoToUpdate.getId());
                repository.update(propertyInfo);
                break;
        }
    }

    public boolean isPropertyInfoDataValid(RentalPropertyInfo propertyInfo) {
        if(InputFieldValidator.isAnyStringEmpty(propertyInfo.getName(), propertyInfo.getAddress(),
                propertyInfo.getOwnerFirstName(), propertyInfo.getOwnerLastName(),
                propertyInfo.getOwnerOIB(), propertyInfo.getOwnerIBAN())){

            dataValidationStatus.setValue(DataValidationStatus.DATA_HAS_EMPTY_FIELD);
            return false;
        }

        if(!InputFieldValidator.isOib(propertyInfo.getOwnerOIB())){
            dataValidationStatus.setValue(DataValidationStatus.OIB_NOT_VALID);
            return false;
        }

        if(!InputFieldValidator.isHrIban(propertyInfo.getOwnerIBAN())){
            dataValidationStatus.setValue(DataValidationStatus.IBAN_NOT_VALID);
            return false;
        }

        dataValidationStatus.setValue(DataValidationStatus.VALID);
        return true;
    }

    public RentalPropertyInfo getPropertyInfoToUpdate() {
        return propertyInfoToUpdate;
    }
}

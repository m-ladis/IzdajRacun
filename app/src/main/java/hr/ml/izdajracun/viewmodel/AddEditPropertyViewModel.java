package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.RentalPropertyInfoRepository;
import hr.ml.izdajracun.utils.InputFieldValidator;

public class AddEditPropertyViewModel extends AndroidViewModel {

    private static final String TAG = "AddEditPropertyVM";

    public enum Mode {
        MODE_ADD, MODE_UPDATE
    }

    public enum DataValidationStatus {
        VALID, DATA_HAS_EMPTY_FIELD, OIB_NOT_VALID, IBAN_NOT_VALID, NONE
    }

    public MutableLiveData<DataValidationStatus> dataValidationStatus = new MutableLiveData<>();

    private Mode mode;
    private RentalPropertyInfo propertyInfoToUpdate;
    private RentalPropertyInfoRepository repository;

    public AddEditPropertyViewModel(@NonNull Application application) {
        super(application);

        repository = new RentalPropertyInfoRepository(application);
        dataValidationStatus.setValue(DataValidationStatus.NONE);
        mode = Mode.MODE_ADD;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setPropertyInfoToUpdate(RentalPropertyInfo propertyInfoToUpdate) {
        this.propertyInfoToUpdate = propertyInfoToUpdate;
    }

    public void handlePropertyInfo(RentalPropertyInfo propertyInfo){
        if(isPropertyInfoDataValid(propertyInfo)){
            switch (mode){
                case MODE_ADD:
                    insert(propertyInfo);
                    break;

                case MODE_UPDATE:
                    if(propertyInfoToUpdate != null){
                        propertyInfo.setId(propertyInfoToUpdate.getId());
                        update(propertyInfo);
                    }

                    break;
            }
        }
    }

    private boolean isPropertyInfoDataValid(RentalPropertyInfo propertyInfo) {
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

    private void insert(RentalPropertyInfo propertyInfo){
        repository.insert(propertyInfo);
    }

    private void update(RentalPropertyInfo propertyInfo){
        repository.update(propertyInfo);
    }
}

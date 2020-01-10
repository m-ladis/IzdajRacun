package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.IzdajRacunRepository;

public class AddEditPropertyViewModel extends AndroidViewModel {

    private static final String TAG = "AddEditPropertyVM";

    public enum Mode {
        MODE_ADD, MODE_UPDATE
    }

    private Mode mode;
    private RentalPropertyInfo propertyInfoToUpdate;
    private IzdajRacunRepository repository;

    public AddEditPropertyViewModel(@NonNull Application application) {
        super(application);

        repository = new IzdajRacunRepository(application);
        mode = Mode.MODE_ADD;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setPropertyInfoToUpdate(RentalPropertyInfo propertyInfoToUpdate) {
        this.propertyInfoToUpdate = propertyInfoToUpdate;
    }

    public void handlePropertyInfo(RentalPropertyInfo propertyInfo){
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

    private void insert(RentalPropertyInfo propertyInfo){
        repository.insert(propertyInfo);
    }

    private void update(RentalPropertyInfo propertyInfo){
        repository.update(propertyInfo);
    }
}

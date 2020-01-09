package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

public class AddEditPropertyViewModel extends RentalPropertyViewModel {

    private static final String TAG = "AddEditPropertyVM";

    public enum Mode {
        MODE_ADD, MODE_UPDATE
    }

    private Mode mode;
    private RentalPropertyInfo propertyInfoToUpdate;

    public AddEditPropertyViewModel(@NonNull Application application) {
        super(application);

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
}

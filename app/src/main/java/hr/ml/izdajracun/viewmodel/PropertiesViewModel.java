package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.IzdajRacunRepository;

public class PropertiesViewModel extends AndroidViewModel {

    private IzdajRacunRepository repository;
    public LiveData<List<RentalPropertyInfo>> properties;

    public PropertiesViewModel(@NonNull Application application) {
        super(application);

        repository = new IzdajRacunRepository(application);
        properties = repository.getAllPropertiesInfo();
    }

    public LiveData<List<RentalPropertyInfo>> getAllPropertiesInfo(){
        return repository.getAllPropertiesInfo();
    }
}

package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.IzdajRacunRepository;

public class RentalPropertyViewModel extends AndroidViewModel {

    private IzdajRacunRepository repository;
    public LiveData<List<RentalPropertyInfo>> properties;

    public RentalPropertyViewModel(@NonNull Application application) {
        super(application);

        repository = new IzdajRacunRepository(application);
        properties = repository.getAllPropertiesInfo();
    }

    public void insert(RentalPropertyInfo propertyInfo){
        repository.insert(propertyInfo);
    }

    public void update(RentalPropertyInfo propertyInfo){
        repository.update(propertyInfo);
    }

    public void delete(RentalPropertyInfo propertyInfo){
        repository.delete(propertyInfo);
    }

    public LiveData<List<RentalPropertyInfo>> getAllPropertiesInfo(){
        return repository.getAllPropertiesInfo();
    }
}

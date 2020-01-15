package hr.ml.izdajracun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.repository.RentalPropertyInfoRepository;

public class PropertiesViewModel extends AndroidViewModel {

    private RentalPropertyInfoRepository repository;
    public LiveData<List<RentalPropertyInfo>> properties;

    public PropertiesViewModel(@NonNull Application application) {
        super(application);

        repository = new RentalPropertyInfoRepository(application);
        properties = repository.getAllPropertiesInfo();
    }
}

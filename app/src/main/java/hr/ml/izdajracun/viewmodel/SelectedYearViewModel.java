package hr.ml.izdajracun.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hr.ml.izdajracun.utils.CustomTimeUtils;

public class SelectedYearViewModel extends ViewModel {

    public MutableLiveData<Integer> selectedYear = new MutableLiveData<>();

    public SelectedYearViewModel() {
        selectedYear.setValue(CustomTimeUtils.getCurrentYear());
    }

    public void incrementYear(){
        selectedYear.setValue(selectedYear.getValue() + 1);
    }

    public void decrementYear(){
        selectedYear.setValue(selectedYear.getValue() - 1);
    }
}

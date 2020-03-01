package hr.ml.izdajracun.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

@Dao
public interface RentalPropertyInfoDao extends BaseDao<RentalPropertyInfo>{

    @Query("SELECT * FROM rental_property_info ORDER BY id DESC")
    LiveData<List<RentalPropertyInfo>> getAllRentalPropertiesInfo();

}

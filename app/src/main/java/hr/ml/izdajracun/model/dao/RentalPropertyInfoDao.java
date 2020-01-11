package hr.ml.izdajracun.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

@Dao
public interface RentalPropertyInfoDao {

    @Insert
    void insert(RentalPropertyInfo rentalPropertyInfo);

    @Update
    void update(RentalPropertyInfo rentalPropertyInfo);

    @Delete
    void delete(RentalPropertyInfo rentalPropertyInfo);

    @Query("SELECT * FROM rental_property_info ORDER BY id ASC")
    LiveData<List<RentalPropertyInfo>> getAllRentalPropertiesInfo();

}

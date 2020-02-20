package hr.ml.izdajracun.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hr.ml.izdajracun.model.entity.BusinessInvoice;

@Dao
public interface BusinessInvoiceDao {

    @Insert
    void insert(BusinessInvoice invoice);

    @Update
    void update(BusinessInvoice invoice);

    @Delete
    void delete(BusinessInvoice invoice);

    @Query(value = "SELECT * FROM business_invoice WHERE year = :year AND propertyId = :propertyId ORDER BY number ASC ")
    LiveData<List<BusinessInvoice>> getAllInvoicesInYear(int propertyId, int year);
}

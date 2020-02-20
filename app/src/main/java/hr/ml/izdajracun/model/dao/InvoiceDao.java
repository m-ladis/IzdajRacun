package hr.ml.izdajracun.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hr.ml.izdajracun.model.entity.Invoice;

@Dao
public interface InvoiceDao {

    @Insert
    void insert(Invoice invoice);

    @Update
    void update(Invoice invoice);

    @Delete
    void delete(Invoice invoice);

    @Query(value = "SELECT * FROM invoice WHERE year = :year AND propertyId = :propertyId ORDER BY number DESC ")
    LiveData<List<Invoice>> getAllInvoicesInYear(int propertyId, int year);
}

package hr.ml.izdajracun.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import hr.ml.izdajracun.model.entity.Invoice;

@Dao
public interface InvoiceDao extends BaseDao<Invoice>{

    @Query(value = "SELECT * FROM invoice WHERE year = :year AND propertyId = :propertyId ORDER BY number ASC ")
    LiveData<List<Invoice>> getAllInvoicesInYear(int propertyId, int year);

}

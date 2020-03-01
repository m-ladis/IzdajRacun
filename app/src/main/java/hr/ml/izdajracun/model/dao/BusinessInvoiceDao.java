package hr.ml.izdajracun.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import hr.ml.izdajracun.model.entity.BusinessInvoice;

@Dao
public interface BusinessInvoiceDao extends BaseDao<BusinessInvoice>{

    @Query(value = "SELECT * FROM business_invoice WHERE year = :year AND propertyId = :propertyId ORDER BY number ASC ")
    LiveData<List<BusinessInvoice>> getAllInvoicesInYear(int propertyId, int year);

}

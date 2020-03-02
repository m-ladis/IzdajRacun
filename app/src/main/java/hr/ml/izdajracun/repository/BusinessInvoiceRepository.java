package hr.ml.izdajracun.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.dao.BusinessInvoiceDao;
import hr.ml.izdajracun.model.database.IzdajRacunDatabase;
import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;

public class BusinessInvoiceRepository {
    private BusinessInvoiceDao invoiceDao;

    public BusinessInvoiceRepository(Application application) {
        IzdajRacunDatabase database = IzdajRacunDatabase.getInstance(application);
        invoiceDao = database.businessInvoiceDao();
    }

    public void insert(BusinessInvoice invoice){
        new BusinessInvoiceRepository.InsertInvoiceAsyncTask(invoiceDao).execute(invoice);
    }

    public void update(BusinessInvoice invoice){
        new BusinessInvoiceRepository.UpdateInvoiceAsyncTask(invoiceDao).execute(invoice);
    }

    public void deleteById(int id){
        new BusinessInvoiceRepository.DeleteInvoiceAsyncTask(invoiceDao).execute(id);
    }

    public LiveData<List<BusinessInvoice>> getAllInvoicesInYear(int propertyId, int year){
        return invoiceDao.getAllInvoicesInYear(propertyId, year);
    }

    public LiveData<List<MinimalBusinessInvoice>> getAllMinimalBusinessInvoicesInYear
            (int propertyId, int year){
        return invoiceDao.getAllMinimalInvoicesInYear(propertyId, year);
    }

    public LiveData<BusinessInvoice> getBusinessInvoiceById(int id){
        return invoiceDao.getBusinessInvoiceById(id);
    }

    private static class InsertInvoiceAsyncTask
            extends AsyncTask<BusinessInvoice, Void, Void> {

        private final BusinessInvoiceDao dao;

        public InsertInvoiceAsyncTask(BusinessInvoiceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(BusinessInvoice... invoices) {
            BusinessInvoice invoice = invoices[0];

            dao.insert(invoice);

            return null;
        }
    }

    private static class UpdateInvoiceAsyncTask
            extends AsyncTask<BusinessInvoice, Void, Void> {

        private final BusinessInvoiceDao dao;

        public UpdateInvoiceAsyncTask(BusinessInvoiceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(BusinessInvoice... invoices) {
            BusinessInvoice invoice = invoices[0];

            dao.update(invoice);

            return null;
        }
    }

    private static class DeleteInvoiceAsyncTask
            extends AsyncTask<Integer, Void, Void> {

        private final BusinessInvoiceDao dao;

        public DeleteInvoiceAsyncTask(BusinessInvoiceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Integer... values) {
            Integer id = values[0];

            dao.deleteById(id);

            return null;
        }
    }
}

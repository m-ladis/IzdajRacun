package hr.ml.izdajracun.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.dao.BusinessInvoiceDao;
import hr.ml.izdajracun.model.database.IzdajRacunDatabase;
import hr.ml.izdajracun.model.entity.BusinessInvoice;

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

    public void delete(BusinessInvoice invoice){
        new BusinessInvoiceRepository.DeleteInvoiceAsyncTask(invoiceDao).execute(invoice);
    }

    public LiveData<List<BusinessInvoice>> getAllInvoicesInYear(int propertyId, int year){
        return invoiceDao.getAllInvoicesInYear(propertyId, year);
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
            extends AsyncTask<BusinessInvoice, Void, Void> {

        private final BusinessInvoiceDao dao;

        public DeleteInvoiceAsyncTask(BusinessInvoiceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(BusinessInvoice... invoices) {
            BusinessInvoice invoice = invoices[0];

            dao.delete(invoice);

            return null;
        }
    }
}

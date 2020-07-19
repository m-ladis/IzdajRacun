package hr.ml.izdajracun.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.dao.InvoiceDao;
import hr.ml.izdajracun.model.database.IzdajRacunDatabase;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;

public class InvoiceRepository {

    private InvoiceDao invoiceDao;

    public InvoiceRepository(Application application) {
        IzdajRacunDatabase database = IzdajRacunDatabase.getInstance(application);
        invoiceDao = database.invoiceDao();
    }

    public void insert(Invoice invoice){
        new InsertInvoiceAsyncTask(invoiceDao).execute(invoice);
    }

    public void update(Invoice invoice){
        new UpdateInvoiceAsyncTask(invoiceDao).execute(invoice);
    }

    public void deleteById(int id){
        new DeleteInvoiceAsyncTask(invoiceDao).execute(id);
    }

    public LiveData<List<Invoice>> getAllInvoicesInYear(int propertyId, int year){
        return invoiceDao.getAllInvoicesInYear(propertyId, year);
    }

    public LiveData<List<MinimalInvoice>> getAllMinimalInvoicesInYear(int propertyId, int year){
        return invoiceDao.getAllMinimalInvoicesInYear(propertyId, year);
    }

    public LiveData<List<Integer>> getYears(int propertyId){
        return invoiceDao.getYears(propertyId);
    }

    public LiveData<Invoice> getInvoiceById(int id){
        return invoiceDao.getInvoiceById(id);
    }

    private static class InsertInvoiceAsyncTask
            extends AsyncTask<Invoice, Void, Void> {

        private final InvoiceDao dao;

        public InsertInvoiceAsyncTask(InvoiceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Invoice... invoices) {
            Invoice invoice = invoices[0];

            dao.insert(invoice);

            return null;
        }
    }

    private static class UpdateInvoiceAsyncTask
            extends AsyncTask<Invoice, Void, Void> {

        private final InvoiceDao dao;

        public UpdateInvoiceAsyncTask(InvoiceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Invoice... invoices) {
            Invoice invoice = invoices[0];

            dao.update(invoice);

            return null;
        }
    }

    private static class DeleteInvoiceAsyncTask
            extends AsyncTask<Integer, Void, Void> {

        private final InvoiceDao dao;

        public DeleteInvoiceAsyncTask(InvoiceDao dao) {
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

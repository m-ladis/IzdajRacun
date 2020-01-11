package hr.ml.izdajracun.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import hr.ml.izdajracun.model.dao.InvoiceDao;
import hr.ml.izdajracun.model.database.IzdajRacunDatabase;
import hr.ml.izdajracun.model.entity.Invoice;

public class InvoiceRepository {

    private InvoiceDao invoiceDao;
    private LiveData<List<Invoice>> invoices;

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

    public void delete(Invoice invoice){
        new DeleteInvoiceAsyncTask(invoiceDao).execute(invoice);
    }

    public LiveData<List<Invoice>> getAllInvoicesInYear(int year){
        return invoices;
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
            extends AsyncTask<Invoice, Void, Void> {

        private final InvoiceDao dao;

        public DeleteInvoiceAsyncTask(InvoiceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Invoice... invoices) {
            Invoice invoice = invoices[0];

            dao.delete(invoice);

            return null;
        }
    }
}

package hr.ml.izdajracun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.Invoice;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.InvoiceViewHolder> {

    private List<Invoice> invoices;
    private Context context;

    public InvoicesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoice_item, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);
        int year = invoice.getDate().get(Calendar.YEAR);
        int month = invoice.getDate().get(Calendar.MONTH) + 1;
        int day = invoice.getDate().get(Calendar.DAY_OF_MONTH);

        holder.invoiceNumberTextView.setText(String.valueOf(invoice.getNumber()));
        holder.invoiceCustomerTextView.setText(invoice.getCustomerName());
        holder.invoiceDateTextView.setText(day + "." + month + "." + year + ".");
        holder.invoiceTotalPriceTextView.setText(String.valueOf(invoice.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return invoices == null ? 0 : invoices.size();
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public class InvoiceViewHolder extends RecyclerView.ViewHolder {

        private TextView invoiceNumberTextView;
        private TextView invoiceCustomerTextView;
        private TextView invoiceDateTextView;
        private TextView invoiceTotalPriceTextView;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            invoiceNumberTextView = itemView.findViewById(R.id.invoice_item_number);
            invoiceCustomerTextView = itemView.findViewById(R.id.invoice_item_customer);
            invoiceDateTextView = itemView.findViewById(R.id.invoice_item_date);
            invoiceTotalPriceTextView = itemView.findViewById(R.id.invoice_item_total_price);
        }
    }
}

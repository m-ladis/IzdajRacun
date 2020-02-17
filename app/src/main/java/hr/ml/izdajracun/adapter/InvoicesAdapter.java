package hr.ml.izdajracun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.view.OnInvoicePopupMenuItemSelectedListener;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.InvoiceViewHolder> {

    private List<Invoice> invoices;
    private Context context;
    private OnInvoicePopupMenuItemSelectedListener itemOptionsListener;

    public InvoicesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
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

    public void setItemOptionsListener(OnInvoicePopupMenuItemSelectedListener optionsListener) {
        this.itemOptionsListener = optionsListener;
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu itemMenu = new PopupMenu(context, v);
                    itemMenu.inflate(R.menu.invoice_popup_menu);
                    itemMenu.show();

                    itemMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Invoice invoice = invoices.get(getAdapterPosition());

                            switch (item.getItemId()){
                                case R.id.invoice_edit:
                                    itemOptionsListener.edit(invoice);
                                    return true;
                                case R.id.invoice_delete:
                                    itemOptionsListener.delete(invoice);
                                    return true;
                                case R.id.invoice_export_as_pdf:
                                    itemOptionsListener.exportAsPdf(invoice);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    return true;
                }
            });
        }
    }
}

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.utils.InvoiceGenerator;
import hr.ml.izdajracun.utils.InvoiceListHelper;
import hr.ml.izdajracun.view.OnInvoicePopupMenuItemSelectedListener;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.InvoiceViewHolder> {

    private static final String TAG = "InvoicesAdepter";

    private List<Invoice> invoices = new ArrayList<>();
    private List<BusinessInvoice> businessInvoices = new ArrayList<>();
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
        Object objectAtIndex = InvoiceListHelper.getObjectAtIndex(position, invoices, businessInvoices);
        Invoice invoice = (Invoice) objectAtIndex;
        int year = invoice.getDate().get(Calendar.YEAR);
        int month = invoice.getDate().get(Calendar.MONTH) + 1;
        int day = invoice.getDate().get(Calendar.DAY_OF_MONTH);

        holder.invoiceNumberTextView.setText(String.valueOf(invoice.getNumber()));
        holder.invoiceCustomerTextView.setText(invoice.getCustomerName());
        holder.invoiceDateTextView.setText(day + "." + month + "." + year + ".");
        holder.invoiceTotalPriceTextView.setText(InvoiceGenerator.roundTwoDecimal(
                String.valueOf(invoice.getTotalPrice())));
    }

    @Override
    public int getItemCount() {
        int size = 0;
        size += invoices == null ? 0 : invoices.size();
        size += businessInvoices == null ? 0 : businessInvoices.size();
        return size;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public void setItemOptionsListener(OnInvoicePopupMenuItemSelectedListener optionsListener) {
        this.itemOptionsListener = optionsListener;
    }

    public void setBusinessInvoices(List<BusinessInvoice> businessInvoices) {
        this.businessInvoices = businessInvoices;
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
                            Object objectAtIndex = InvoiceListHelper.getObjectAtIndex(
                                    getAdapterPosition(), invoices, businessInvoices);

                            if (objectAtIndex instanceof BusinessInvoice) {
                                BusinessInvoice businessInvoice = (BusinessInvoice) objectAtIndex;

                                switch (item.getItemId()) {
                                    case R.id.invoice_edit:
                                        itemOptionsListener.edit(businessInvoice);
                                        return true;
                                    case R.id.invoice_delete:
                                        itemOptionsListener.delete(businessInvoice);
                                        return true;
                                    case R.id.invoice_export_as_pdf:
                                        itemOptionsListener.exportAsPdf(businessInvoice);
                                        return true;
                                    default:
                                        return false;
                                }
                            } else {
                                Invoice invoice = (Invoice) objectAtIndex;

                                switch (item.getItemId()) {
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
                        }
                    });
                    return true;
                }
            });
        }
    }
}

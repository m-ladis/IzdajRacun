package hr.ml.izdajracun.view;

import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;

public interface OnInvoicePopupMenuItemSelectedListener {
    void edit(MinimalInvoice invoice);
    void delete(MinimalInvoice invoice);
    void exportAsPdf(MinimalInvoice invoice);
    void edit(MinimalBusinessInvoice invoice);
    void delete(MinimalBusinessInvoice invoice);
    void exportAsPdf(MinimalBusinessInvoice invoice);
}

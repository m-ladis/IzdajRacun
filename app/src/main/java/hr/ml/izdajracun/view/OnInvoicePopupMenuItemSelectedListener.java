package hr.ml.izdajracun.view;

import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;

public interface OnInvoicePopupMenuItemSelectedListener {
    void edit(Invoice invoice);
    void delete(Invoice invoice);
    void exportAsPdf(Invoice invoice);
    void edit(BusinessInvoice invoice);
    void delete(BusinessInvoice invoice);
    void exportAsPdf(BusinessInvoice invoice);
}

package hr.ml.izdajracun.view;

import hr.ml.izdajracun.model.entity.Invoice;

public interface OnInvoicePopupMenuItemSelectedListener {
    void edit(Invoice invoice);
    void delete(Invoice invoice);
    void exportAsPdf(Invoice invoice);
}

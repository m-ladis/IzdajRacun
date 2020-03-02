package hr.ml.izdajracun.view;

import hr.ml.izdajracun.model.entity.MinimalBusinessInvoice;
import hr.ml.izdajracun.model.entity.MinimalInvoice;

public interface OnSelectedInvoiceListener {
    void edit(MinimalInvoice invoice);
    void delete(MinimalInvoice invoice);
    void open(MinimalInvoice invoice);
    void edit(MinimalBusinessInvoice invoice);
    void delete(MinimalBusinessInvoice invoice);
    void open(MinimalBusinessInvoice invoice);
}

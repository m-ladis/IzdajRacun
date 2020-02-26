package hr.ml.izdajracun;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.utils.InputFieldValidator;
import hr.ml.izdajracun.utils.InvoiceListHelper;

public class InvoiceListHelperUnitTest {

    private List<Invoice> invoices;
    private List<BusinessInvoice> businessInvoices;

    @Before
    public void setUp(){
        Invoice invoice = new Invoice(0, "marino", 1, 200, 100, "");
        Invoice invoice2 = new Invoice(1, "marino", 1, 200, 100, "");
        Invoice invoice3 = new Invoice(4, "marino", 1, 200, 100, "");
        Invoice invoice4 = new Invoice(8, "marino", 1, 200, 100, "");
        Invoice invoice5 = new Invoice(10, "marino", 1, 200, 100, "");

        BusinessInvoice businessInvoice = new BusinessInvoice(new Invoice(2, "marino", 1,
                200, 100, ""), "ulica perosa4",
                "83730737841", "gotovina");

        BusinessInvoice businessInvoice2 = new BusinessInvoice(new Invoice(3, "marino", 1,
                200, 100, ""), "ulica perosa4",
                "83730737841", "gotovina");

        BusinessInvoice businessInvoice3 = new BusinessInvoice(new Invoice(7, "marino", 1,
                200, 100, ""), "ulica perosa4",
                "83730737841", "gotovina");

        invoices = new ArrayList<>();
        invoices.add(invoice);
        invoices.add(invoice2);
        invoices.add(invoice3);
        invoices.add(invoice4);
        invoices.add(invoice5);


        businessInvoices = new ArrayList<>();
        businessInvoices.add(businessInvoice);
        businessInvoices.add(businessInvoice2);
        businessInvoices.add(businessInvoice3);
    }

    @Test
    public void shouldReturnNull(){
        List<Invoice> invoices = new ArrayList<>();
        List<BusinessInvoice> businessInvoices = new ArrayList<>();

        Assert.assertNull(InvoiceListHelper.getObjectAtIndex(5, invoices, businessInvoices));
    }

    @Test
    public void shouldReturnInvoiceWithNumber10(){
        Invoice invoiceAt = (Invoice) InvoiceListHelper.getObjectAtIndex(0, invoices, businessInvoices);

        Assert.assertEquals(10,invoiceAt.getNumber());
    }

    @Test
    public void shouldReturnInvoiceWithNumber8(){
        Invoice invoiceAt = (Invoice) InvoiceListHelper.getObjectAtIndex(1, invoices, businessInvoices);

        Assert.assertEquals(8,invoiceAt.getNumber());
    }

    @Test
    public void shouldReturnInvoiceWithNumber7(){
        Invoice invoiceAt = (Invoice) InvoiceListHelper.getObjectAtIndex(2, invoices, businessInvoices);

        Assert.assertEquals(7,invoiceAt.getNumber());
    }

    @Test
    public void shouldReturnInvoiceWithNumber5(){
        Invoice invoiceAt = (Invoice) InvoiceListHelper.getObjectAtIndex(3, invoices, businessInvoices);

        Assert.assertEquals(4,invoiceAt.getNumber());
    }

    @Test
    public void shouldReturnInvoiceWithNumber0(){
        Invoice invoiceAt = (Invoice) InvoiceListHelper.getObjectAtIndex(7, invoices, businessInvoices);

        Assert.assertEquals(0,invoiceAt.getNumber());
    }

    @Test
    public void isHrIban_ValidHrIbans_ExpectReturnTrue(){
        Assert.assertTrue(InputFieldValidator.isHrIban("HR2124070003205695542"));
        Assert.assertTrue(InputFieldValidator.isHrIban("HR4124070003104577920"));
        Assert.assertTrue(InputFieldValidator.isHrIban("HR2224070003104968008"));
        Assert.assertTrue(InputFieldValidator.isHrIban("HR5324070003104943491"));
    }

    @Test
    public void isHrIban_InvalidHrIbans_ExpectReturnFalse(){
        Assert.assertFalse(InputFieldValidator.isHrIban("HR2124070003205695541"));
    }

    @Test
    public void isPriceValid_ValidTotalPrices_ExpectReturnTrue(){
        Assert.assertTrue(InputFieldValidator.isPriceValid(5, 500, 2500));
        Assert.assertTrue(InputFieldValidator.isPriceValid(5, 500, 2400));
    }

    @Test
    public void isPriceValid_InvalidTotalPrices_ExpectReturnFalse(){
        Assert.assertFalse(InputFieldValidator.isPriceValid(5, 500, 2600));
        Assert.assertFalse(InputFieldValidator.isPriceValid(5, 500, 2700));
    }
}

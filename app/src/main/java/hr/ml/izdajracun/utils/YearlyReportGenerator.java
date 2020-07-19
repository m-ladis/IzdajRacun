package hr.ml.izdajracun.utils;

import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import hr.ml.izdajracun.model.entity.MinimalInvoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

public class YearlyReportGenerator {
    private int year;
    private RentalPropertyInfo propertyInfo;
    private List<MinimalInvoice> invoices;
    private String ownerAddress;
    private String ownerPlace;

    public YearlyReportGenerator(int year, RentalPropertyInfo propertyInfo, List<MinimalInvoice> invoices, String ownerAddress, String ownerPlace) {
        this.year = year;
        this.propertyInfo = propertyInfo;
        this.invoices = invoices;
        this.ownerAddress = ownerAddress;
        this.ownerPlace = ownerPlace;
    }

    public File generate() throws IOException, DocumentException {
        BaseFont baseFont = BaseFont.createFont("/system/fonts/Roboto-Regular.ttf", BaseFont.CP1250 , BaseFont.EMBEDDED);

        Font font = new Font(baseFont, 12, Font.NORMAL);
        Font bold = new Font(baseFont, 12, Font.BOLD);
        Font italic = new Font(baseFont, 12, Font.ITALIC);

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/IzdajRacun/" + propertyInfo.getId() + "/" + year);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File myFile = new File(dir, "obrazacEP.pdf");

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A4, 50,50,50,50);
        PdfWriter.getInstance(document, output);
        document.open();

        //structure
        Paragraph obrazacEp = new Paragraph("Obrazac EP", italic);
        Paragraph naslov = new Paragraph("EVIDENCIJA O PROMETU", bold);

        PdfPTable opciPodaci = new PdfPTable(1);
        opciPodaci.addCell(getCell(new Phrase("1. Opći podaci o poreznom obvezniku", font)));
        opciPodaci.setWidthPercentage(100);

        PdfPTable vlasnikImePrezime = new PdfPTable(1);
        vlasnikImePrezime.addCell(getCell(new Phrase("1.1. Ime i prezime: " + propertyInfo.getOwnerFirstName() + " "
                + propertyInfo.getOwnerLastName(), font)));
        vlasnikImePrezime.setWidthPercentage(100);

        PdfPTable vlasnikOib = new PdfPTable(1);
        vlasnikOib.addCell(getCell(new Phrase("1.2. OIB: " + propertyInfo.getOwnerOIB(), font)));
        vlasnikOib.setWidthPercentage(100);

        PdfPTable vlasnikMjesto = new PdfPTable(1);
        vlasnikOib.addCell(getCell(new Phrase("1.3. Mjesto prebivališta/boravišta: " + ownerPlace, font)));
        vlasnikOib.setWidthPercentage(100);

        PdfPTable vlasnikAdresa = new PdfPTable(1);
        vlasnikOib.addCell(getCell(new Phrase("1.4. Adresa prebivališta/boravišta: " + ownerAddress, font)));
        vlasnikOib.setWidthPercentage(100);

        float[] omjer = {1.5f,5,5,3};

        PdfPTable labele = new PdfPTable(omjer);
        labele.setWidthPercentage(100);

        PdfPCell cell = getCell(new Phrase("", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell.setPhrase(new Phrase("R. br."));
        labele.addCell(cell);

        cell.setBorder(0);
        PdfPTable labelaRacun = new PdfPTable(1);
        cell.setPhrase(new Phrase("", font));
        cell.setBorder(Rectangle.NO_BORDER);
        labelaRacun.addCell(cell);
        PdfPTable labeleBrojNadnevak = new PdfPTable(2);
        cell.setPhrase(new Phrase("Broj", font));
        cell.setBorder(Rectangle.BOX);
        labeleBrojNadnevak.addCell(cell);
        cell.setPhrase(new Phrase("Nadnevak", font));
        labeleBrojNadnevak.addCell(cell);
        labelaRacun.addCell(labeleBrojNadnevak);
        labele.addCell(labelaRacun);


        PdfPTable labeleKorisnikImePrezime = new PdfPTable(1);
        cell.setPhrase(new Phrase("Korisnik usluge", font));
        cell.setBorder(Rectangle.NO_BORDER);
        labeleKorisnikImePrezime.addCell(cell);
        cell.setPhrase(new Phrase("Ime i prezime", font));
        cell.setBorder(Rectangle.TOP);
        labeleKorisnikImePrezime.addCell(cell);
        labele.addCell(labeleKorisnikImePrezime);

        cell.setPhrase(new Phrase("Iznos računa", font));
        cell.setBorder(Rectangle.BOX);
        labele.addCell(cell);

        //styling
        obrazacEp.setAlignment(Element.ALIGN_RIGHT);
        naslov.setAlignment(Element.ALIGN_CENTER);
        naslov.setSpacingAfter(10);


        //adding
        document.add(obrazacEp);
        document.add(naslov);
        document.add(opciPodaci);
        document.add(vlasnikImePrezime);
        document.add(vlasnikOib);
        document.add(vlasnikMjesto);
        document.add(vlasnikAdresa);
        document.add(labele);

        int size = invoices.size();
        for(int i = 0; i < size ; i++){
            MinimalInvoice invoice = invoices.get(i);

            float[] omjer2 = {1.5f,2.5f, 2.5f,5,3};
            PdfPTable invoiceRow = new PdfPTable(omjer2);
            invoiceRow.addCell(getCell(new Phrase(i+1 + ".")));
            invoiceRow.addCell(getCell(new Phrase(String.valueOf(invoice.getNumber()), font)));
            invoiceRow.addCell(getCell(new Phrase(InvoiceGenerator.calendarFormat(
                    invoice.getDate()), font)));
            invoiceRow.addCell(getCell(new Phrase(invoice.getCustomerName(), font)));


            PdfPCell iznos = getCell(new Phrase(InvoiceGenerator.roundTwoDecimal
                    (String.valueOf(invoice.getTotalPrice())) + " kn" , font));
            iznos.setHorizontalAlignment(Element.ALIGN_RIGHT);
            invoiceRow.addCell(iznos);

            invoiceRow.setWidthPercentage(100);
            document.add(invoiceRow);
        }


        //closing
        document.close();

        return myFile;
    }

    public PdfPCell getCell(Phrase phrase){
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(3);
        return cell;
    }
}

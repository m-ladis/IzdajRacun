package hr.ml.izdajracun.utils;

import android.os.Environment;
import android.util.Base64;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import hr.ml.izdajracun.model.entity.BusinessInvoice;
import hr.ml.izdajracun.model.entity.Invoice;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

public class InvoiceGenerator {
    public static File generate(BusinessInvoice invoice) throws IOException, DocumentException {
        RentalPropertyInfo propertyInfo = invoice.getPropertyInfo();
        BaseFont baza = BaseFont.createFont("/system/fonts/Roboto-Regular.ttf", BaseFont.CP1250 , BaseFont.EMBEDDED);

        Font font = new Font(baza, 10, Font.BOLD);

        Font bold = new Font(baza, 25, Font.BOLD);

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/IzdajRacun/" + propertyInfo.getId() + "/" + invoice.getYear());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String vlasnik = propertyInfo.getOwnerFirstName() + " " + propertyInfo.getOwnerLastName() + "\n" +
                propertyInfo.getAddress() + "\n" +
                "OIB: " + propertyInfo.getOwnerOIB() + "\n" +
                "IBAN: " + propertyInfo.getOwnerIBAN();

        String gost = invoice.getCustomerName() + "\n" +
                invoice.getCustomerAddress() + "\n" +
                "OIB/PDVID: " + invoice.getCustomerOib();

        String racunNaslov = "Račun br. " + invoice.getNumber();

        String boravisna = "Obveznik nije u sustavu PDV-a." + "\n" +
                "Boravišna pristojba je uključena u cijenu smještaja.";

        String datumIzdao = "Račun izdao/la:     " + propertyInfo.getOwnerFirstName() + " " + propertyInfo.getOwnerLastName() + "\n" +
                "Datum izdavanja:    " + calendarFormat(invoice.getDate()) + "\n" +
                "Način plaćanja:     " + invoice.getPaymentMethod() + "\n" +
                "Rok plaćanja:       " + calendarFormat(invoice.getPayDueDate()) + "\n" +
                "Datum isporuke:     " + calendarFormat(invoice.getDeliveryDate());

        String ukupno = calculateTotal(String.valueOf(invoice.getQuantity()), String.valueOf(invoice.getUnitPrice()));

        String logoStr = propertyInfo.getLogoImage();

        File myFile = new File(dir, invoice.getNumber() + ".pdf");
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        //structure
        Paragraph brRacunaPar = new Paragraph(racunNaslov, bold);

        if(logoStr != null){
            Image logo = Image.getInstance(Base64.decode(logoStr, Base64.DEFAULT));
            logo.scaleAbsolute(50,50);
            Paragraph logoTable = new Paragraph(new Chunk(logo, 0,-25));
            document.add(logoTable);
        }

        PdfPTable tableHead = new PdfPTable(2);
        tableHead.addCell(getCell("IZNAJMLJIVAČ", Element.ALIGN_LEFT, font));
        tableHead.addCell(getCell("UNAJMITELJ", Element.ALIGN_RIGHT, font));

        PdfPTable tableHeadInfo = new PdfPTable(2);
        tableHeadInfo.addCell(getCell(vlasnik, Element.ALIGN_LEFT, font));
        tableHeadInfo.addCell(getCell(gost, Element.ALIGN_RIGHT, font));

        PdfPTable podTablica = new PdfPTable(2);
        podTablica.setWidthPercentage(40);
        podTablica.addCell(getCell("Popust", Element.ALIGN_LEFT, font));
        podTablica.addCell(new Phrase(roundTwoDecimal(calculateDiscount(ukupno, String.valueOf(invoice.getTotalPrice()))) + " kn", font));
        podTablica.addCell(getCell("Za naplatu", Element.ALIGN_LEFT, font));
        podTablica.addCell(new Phrase(roundTwoDecimal(String.valueOf(invoice.getTotalPrice())) + " kn", font));

        PdfPTable racunTab = new PdfPTable(5);

        PdfPCell tempvrstaUsluge = new PdfPCell(new Phrase("Vrsta usluge", font));
        tempvrstaUsluge.setMinimumHeight(30);
        racunTab.addCell(tempvrstaUsluge);
        racunTab.addCell(new Phrase("Br. smještajnih jedinica", font));
        racunTab.addCell(new Phrase("Br. noćenja", font));
        racunTab.addCell(new Phrase("Jedinična cijena", font));
        racunTab.addCell(new Phrase("Ukupno", font));
        PdfPCell uslugaSmj = new PdfPCell(new Phrase("Usluga smještaja", font));
        uslugaSmj.setMinimumHeight(18);
        racunTab.addCell(uslugaSmj);
        racunTab.addCell(new Phrase("1", font));
        racunTab.addCell(new Phrase(String.valueOf(invoice.getQuantity()), font));
        racunTab.addCell(new Phrase(roundTwoDecimal(String.valueOf(invoice.getUnitPrice())) + " kn", font));
        racunTab.addCell(new Phrase(roundTwoDecimal(ukupno) + " kn", font));

        Paragraph boravisnaPar = new Paragraph(boravisna, font);

        Phrase napomenaTag = new Phrase("Napomena:", font);

        PdfPTable napomenaTab = new PdfPTable(1);
        PdfPCell napomenaCell = new PdfPCell(new Phrase(invoice.getDescription(), font));
        napomenaCell.setMinimumHeight(40);
        napomenaTab.addCell(napomenaCell);

        Paragraph datumPotpisPar = new Paragraph(datumIzdao, font);

        //styling
        racunTab.setSpacingAfter(10);
        tableHead.setSpacingAfter(3);
        tableHead.setWidthPercentage(100);
        tableHead.setSpacingBefore(28);
        tableHeadInfo.setWidthPercentage(100);
        tableHeadInfo.setSpacingAfter(15);
        podTablica.setWidthPercentage(40);
        racunTab.setWidthPercentage(100);
        napomenaTab.setWidthPercentage(60);
        brRacunaPar.setSpacingAfter(70);
        napomenaTab.setSpacingAfter(25);
        boravisnaPar.setSpacingAfter(5);
        napomenaTab.setHorizontalAlignment(Element.ALIGN_LEFT);
        brRacunaPar.setAlignment(Element.ALIGN_CENTER);
        podTablica.setHorizontalAlignment(Element.ALIGN_RIGHT);
        racunTab.setHorizontalAlignment(Element.ALIGN_CENTER);

        //adding
        document.add(tableHead);
        document.add(tableHeadInfo);
        document.add(new Paragraph(" "));
        document.add(brRacunaPar);
        document.add(racunTab);
        document.add(podTablica);
        document.add(boravisnaPar);
        document.add(napomenaTag);
        document.add(napomenaTab);
        document.add(datumPotpisPar);

        //closing
        document.close();

        return myFile;
    }

    public static File generate(Invoice invoice) throws IOException, DocumentException {
        RentalPropertyInfo propertyInfo = invoice.getPropertyInfo();
        BaseFont baza = BaseFont.createFont("/system/fonts/Roboto-Regular.ttf", BaseFont.CP1250 , BaseFont.EMBEDDED);

        Font font = new Font(baza, 10, Font.BOLD);

        Font bold = new Font(baza, 25, Font.BOLD);

        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/IzdajRacun/" + propertyInfo.getId() + "/" + invoice.getYear());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String vlasnik = propertyInfo.getOwnerFirstName() + " " + propertyInfo.getOwnerLastName() + "\n" +
                propertyInfo.getAddress() + "\n" +
                "OIB: " + propertyInfo.getOwnerOIB() + "\n" +
                "IBAN: " + propertyInfo.getOwnerIBAN();

        String gost = invoice.getCustomerName();


        String racunNaslov = "Račun br. " + invoice.getNumber();

        String boravisna = "Obveznik nije u sustavu PDV-a." + "\n" +
                "Boravišna pristojba je uključena u cijenu smještaja.";

        String datumIzdao = "Račun izdao/la:     " + propertyInfo.getOwnerFirstName() + " " + propertyInfo.getOwnerLastName() + "\n" +
                "Datum izdavanja:    " + calendarFormat(invoice.getDate());

        String ukupno = calculateTotal(String.valueOf(invoice.getQuantity()), String.valueOf(invoice.getUnitPrice()));

        String logoStr = invoice.getPropertyInfo().getLogoImage();

        File myFile = new File(dir, invoice.getNumber() + ".pdf");
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        //structure
        Paragraph brRacunaPar = new Paragraph(racunNaslov, bold);

        if(logoStr != null){
            Image logo = Image.getInstance(Base64.decode(logoStr, Base64.DEFAULT));
            logo.scaleAbsolute(50,50);
            Paragraph logoTable = new Paragraph(new Chunk(logo, 0,-25));
            document.add(logoTable);
        }

        PdfPTable tableHead = new PdfPTable(2);
        tableHead.addCell(getCell("IZNAJMLJIVAČ", Element.ALIGN_LEFT, font));
        tableHead.addCell(getCell("UNAJMITELJ", Element.ALIGN_RIGHT, font));

        PdfPTable tableHeadInfo = new PdfPTable(2);
        tableHeadInfo.addCell(getCell(vlasnik, Element.ALIGN_LEFT, font));
        tableHeadInfo.addCell(getCell(gost, Element.ALIGN_RIGHT, font));

        PdfPTable podTablica = new PdfPTable(2);
        podTablica.setWidthPercentage(40);
        podTablica.addCell(getCell("Popust", Element.ALIGN_LEFT, font));
        podTablica.addCell(new Phrase(roundTwoDecimal(calculateDiscount(ukupno, String.valueOf(invoice.getTotalPrice()))) + " kn", font));
        podTablica.addCell(getCell("Za naplatu", Element.ALIGN_LEFT, font));
        podTablica.addCell(new Phrase(roundTwoDecimal(String.valueOf(invoice.getTotalPrice())) + " kn", font));

        PdfPTable racunTab = new PdfPTable(5);

        PdfPCell tempvrstaUsluge = new PdfPCell(new Phrase("Vrsta usluge", font));
        tempvrstaUsluge.setMinimumHeight(30);
        racunTab.addCell(tempvrstaUsluge);
        racunTab.addCell(new Phrase("Br. smještajnih jedinica", font));
        racunTab.addCell(new Phrase("Br. noćenja", font));
        racunTab.addCell(new Phrase("Jedinična cijena", font));
        racunTab.addCell(new Phrase("Ukupno", font));
        PdfPCell uslugaSmj = new PdfPCell(new Phrase("Usluga smještaja", font));
        uslugaSmj.setMinimumHeight(18);
        racunTab.addCell(uslugaSmj);
        racunTab.addCell(new Phrase("1", font));
        racunTab.addCell(new Phrase(String.valueOf(invoice.getQuantity()), font));
        racunTab.addCell(new Phrase(roundTwoDecimal(String.valueOf(invoice.getUnitPrice())) + " kn", font));
        racunTab.addCell(new Phrase(roundTwoDecimal(ukupno) + " kn", font));

        Paragraph boravisnaPar = new Paragraph(boravisna, font);

        Phrase napomenaTag = new Phrase("Napomena:", font);

        PdfPTable napomenaTab = new PdfPTable(1);
        PdfPCell napomenaCell = new PdfPCell(new Phrase(invoice.getDescription(), font));
        napomenaCell.setMinimumHeight(40);
        napomenaTab.addCell(napomenaCell);

        Paragraph datumPotpisPar = new Paragraph(datumIzdao, font);

        //styling
        racunTab.setSpacingAfter(10);
        tableHead.setSpacingAfter(3);
        tableHead.setWidthPercentage(100);
        tableHead.setSpacingBefore(28);
        tableHeadInfo.setWidthPercentage(100);
        tableHeadInfo.setSpacingAfter(15);
        podTablica.setWidthPercentage(40);
        racunTab.setWidthPercentage(100);
        napomenaTab.setWidthPercentage(60);
        brRacunaPar.setSpacingAfter(70);
        napomenaTab.setSpacingAfter(25);
        boravisnaPar.setSpacingAfter(5);
        napomenaTab.setHorizontalAlignment(Element.ALIGN_LEFT);
        brRacunaPar.setAlignment(Element.ALIGN_CENTER);
        podTablica.setHorizontalAlignment(Element.ALIGN_RIGHT);
        racunTab.setHorizontalAlignment(Element.ALIGN_CENTER);

        //adding
        document.add(tableHead);
        document.add(tableHeadInfo);
        document.add(new Paragraph(" "));
        document.add(brRacunaPar);
        document.add(racunTab);
        document.add(podTablica);
        document.add(boravisnaPar);
        document.add(napomenaTag);
        document.add(napomenaTab);
        document.add(datumPotpisPar);

        //closing
        document.close();

        return myFile;
    }


    public static PdfPCell getCell(String text, int align, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(0);
        cell.setHorizontalAlignment(align);
        cell.setBorder(0);
        return cell;
    }

    public static String calculateTotal(String quantity, String unitPrice){
        int qnt = Integer.parseInt(quantity);
        float price = Float.parseFloat(unitPrice);

        float total = qnt*price;

        return (String.valueOf(total));
    }

    public static String roundTwoDecimal(String number){
        double price = Double.parseDouble(number);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);

        return numberFormat.format(price);
    }

    public static String calculateDiscount(String total, String guestPays){
        double totalPrice = Double.parseDouble(total);
        double guestPaysPrice = Double.parseDouble(guestPays);

        double discount = totalPrice - guestPaysPrice;

        return (String.valueOf(discount));
    }

    public static String calendarFormat(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return (day + "." + month + "." + year + "." );
    }

    public static File getInvoiceFile(Invoice invoice){
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/IzdajRacun/"
                + invoice.getPropertyInfo().getId() + "/" + invoice.getYear());

        return new File(dir, invoice.getNumber() + ".pdf");
    }
}

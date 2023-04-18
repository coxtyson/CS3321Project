package OACRental;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;

import java.io.InputStream;
import java.text.FieldPosition;
import java.time.LocalDate;

import java.awt.print.PrinterJob;
import java.util.List;

import java.io.File;

public class Receipt {
    private PDDocument pdf;
    private List<Product> products;
    private Customer customer;
    private Transaction transaction;

    public Receipt() {
        try{
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("OACReceipt.pdf");
            pdf = PDDocument.load(is);
        }
        catch (java.io.IOException ex){
            System.out.println("IOException occured while loading the document");
        }

        products = null;
        customer = null;
        transaction = null;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setItems(List<Product> products) {
        this.products = products;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void print() throws Exception {
        generatePages();

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(pdf));

        if (job.printDialog()) {
            job.print();
        }
    }

    public void save(String path) throws Exception {
        generatePages();
        pdf.save(path);
        close();
    }

    public void close() {
        try {
            pdf.close();
        }
        catch (java.io.IOException ex) {
            System.out.println("IOException occurred while closing document, ignoring");
        }
    }

    private void generatePages() throws Exception {
        if (customer == null || products == null) {
            throw new IllegalStateException("Can't generate pages, customer and/or items are null");
        }

        if (this.pdf == null) {
            throw new IllegalStateException("No pdf template is loaded");
        }

        // Create the first
        PDPage firstPage = pdf.getPage(0);
        LocalDate todaysDate = LocalDate.now();

        var first = customer.getFirstName();
        var last = customer.getLastName();

        String name = null;
        String email = customer.getEmail();
        String phone = customer.getPhone();

        if (first != null && last != null) {
            name = first + " " + last;
        }
        else if (first != null) {
            name = first;
        }
        else if (last != null) {
            name = last;
        }
        else {
            name = "N/A";
        }

        if (email == null) {
            email = "N/A";
        }

        if (phone == null) {
            phone = "N/A";
        }

        try (PDPageContentStream stream = new PDPageContentStream(pdf, firstPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
            // Sets the date on the receipt
            stream.beginText();
            stream.newLineAtOffset(90, 713);
            stream.setFont(PDType1Font.COURIER, 9);
            stream.showText(todaysDate.toString());
            stream.endText();

            // Putting all of the customer's info into the receipt
            stream.beginText();
            stream.newLineAtOffset(400, 713);
            stream.setFont(PDType1Font.COURIER, 9);
            stream.setLeading(10.5f);
            stream.showText(name);
            stream.newLine();
            stream.showText(email);
            stream.newLine();
            stream.showText(phone);
            stream.endText();

            stream.beginText();
            stream.newLineAtOffset(130, 648);
            stream.setFont(PDType1Font.COURIER, 9);
            stream.showText(customer.getID());
            stream.endText();

            // Adds the checkout and return dates
            stream.beginText();
            stream.newLineAtOffset(136, 613);
            stream.setFont(PDType1Font.COURIER, 9);
            stream.showText(transaction.getCheckout().toString() + " - " + transaction.getExpectedReturn().toString());
            stream.endText();

            // Adds the cart's products and their prices
            stream.beginText();
            stream.newLineAtOffset(57, 570);
            stream.setFont(PDType1Font.COURIER, 9);
            for(int i = 0; i < products.size(); i ++){
                stream.showText(products.get(i).getName());
                stream.newLine();
            }
            stream.endText();
            stream.beginText();
            stream.newLineAtOffset(420, 570);
            stream.setFont(PDType1Font.COURIER, 9);
            for(int i = 0; i < products.size(); i ++){
                stream.showText(products.get(i).getPrice().toString());
                stream.newLine();
            }
            stream.endText();
            stream.beginText();
            stream.newLineAtOffset(490, 570);
            stream.setFont(PDType1Font.COURIER, 9);
            for(int i = 0; i < products.size(); i ++){
                stream.showText(products.get(i).getPrice().toString());
                stream.newLine();
            }
            stream.endText();
            stream.beginText();
            stream.newLineAtOffset(490, 405);
            stream.setFont(PDType1Font.COURIER, 9);
            stream.showText(transaction.getTotalPrice().toString());
            stream.endText();

            Price taxPrice = transaction.getTotalPrice();
            taxPrice.multiply(0.06);
            stream.beginText();
            stream.newLineAtOffset(490, 382);
            stream.setFont(PDType1Font.COURIER, 9);
            stream.showText(taxPrice.toString());
            stream.endText();

            Price totalPrice = transaction.getTotalPrice();
            totalPrice.add(taxPrice);
            stream.beginText();
            stream.newLineAtOffset(490, 360);
            stream.setFont(PDType1Font.COURIER, 9);
            stream.showText(totalPrice.toString());
            stream.endText();
        }
    }
}


package OACRental;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;
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
        File recieptFile = new File("OACReciept.pdf");
        try{
            pdf = PDDocument.load(recieptFile);
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
        // Create the first
        PDPage firstPage = pdf.getPage(0);
        LocalDate todaysDate = LocalDate.now();

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
            stream.showText(customer.getFirstName() + " " + customer.getLastName());
            stream.newLine();
            stream.showText(customer.getEmail());
            stream.newLine();
            stream.showText(customer.getPhone());
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
        }
    }
}


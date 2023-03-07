package OACRental;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.print.PrinterJob;
import java.util.List;

public class Receipt {
    private PDDocument pdf;
    private List<Product> products;
    private Customer customer;

    public Receipt() {
        pdf = new PDDocument();
        products = null;
        customer = null;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setItems(List<Product> products) {
        this.products = products;
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

        // Clear existing pages
        int pageCount = pdf.getNumberOfPages();
        for(int i = 0; i < pageCount; i++) {
            pdf.removePage(i);
        }

        // Create the first
        PDPage firstPage = new PDPage();
        pdf.addPage(firstPage);

        try (var stream = new PDPageContentStream(pdf, firstPage)) {
            stream.beginText();
            stream.newLineAtOffset(25, 25);
            stream.setFont(PDType1Font.HELVETICA, 10);
            stream.showText("Hello world");
            stream.endText();
        }
    }
}


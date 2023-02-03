package OACRental;


import java.time.LocalDateTime;

public class TransactionRecord {
    private String notes;
    private Transaction transaction;
    private LocalDateTime actualReturn;
    private long transactionID;

    TransactionRecord(String notes, Transaction transaction, LocalDateTime actualReturn, long transactionID){
        this.notes = notes;
        this.transaction = transaction;
        this.actualReturn = actualReturn;
        this.transactionID = transactionID;
    }

    public String getNotes(){ return this.notes; }
    public void setNotes(String notes){ this.notes = notes; }
    public Transaction getTransaction(){ return this.transaction; }
    public LocalDateTime getActualReturn(){ return this.actualReturn; }
    public long getTransactionID(){ return this.transactionID; }
}

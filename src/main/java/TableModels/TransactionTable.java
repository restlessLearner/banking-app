package TableModels;

import java.util.Date;

public class TransactionTable {
    private String idNumber;
    private Date date;
    private int senderAccountNumber;
    private int receiverAccountNumber;
    private double amount;

    public TransactionTable(Date date, int senderAccountNumber, int receiverAccountNumber, Double amount) {
        this.date = date;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
    }

    public TransactionTable( String idNumber , Date date, int senderAccountNumber, int receiverAccountNumber, double amount) {
        this.idNumber = idNumber;
        this.date = date;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public int getSenderAccountNumber() {
        return senderAccountNumber;
    }
    public void setSenderAccountNumber(int senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;

    }

    public int getReceiverAccountNumber() {
        return receiverAccountNumber;
    }
    public void setReceiverAccountNumber(int receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;

    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }



    public String getIdNumber() {
        return idNumber;
    }



    public Integer getSenderAcc() {
        return senderAccountNumber;
    }


    public Integer getPayeeAcc() {
        return receiverAccountNumber ;
    }
}




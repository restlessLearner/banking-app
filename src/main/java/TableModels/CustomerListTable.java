package TableModels;

public class CustomerListTable {


    String idNumber;
    String accountType;
    String firstName;
    String surName;
    String DOB;
    String Email;
    Integer phoneNumber;
    Integer cardNumber;
    Integer accountNumber;

    public CustomerListTable(String idNumber, String accountType, String firstName, String surName, String DOB, String Email, Integer
            phoneNumber, Integer cardNumber, Integer accountNumber) {
        this.idNumber = idNumber;
        this.accountType = accountType;
        this.firstName = firstName;
        this.surName = surName;
        this.DOB = DOB;
        this.Email = Email;
        this.phoneNumber = phoneNumber;
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;

    }


    public String getIdNumber() {
        return idNumber;
    }


    public String getAccountType() {
        return accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public String getDOB() {
        return DOB;
    }

    public String getEmail() {
        return Email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }
}



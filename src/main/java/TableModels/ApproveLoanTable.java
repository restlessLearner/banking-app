package TableModels;

public class ApproveLoanTable {
    public String idNumber;
    double salary;
    String loan_Type;
    double loan_Amount;
    int duration;
    String status;

    public ApproveLoanTable (String idNumber, double salary, String loan_Type, double loanAmount, int duration, String status){
        this.idNumber = idNumber;
        this.salary = salary;
        this.loan_Type = loan_Type;
        this.loan_Amount = loanAmount;
        this.duration = duration;
        this.status = status;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getLoanType() {
        return loan_Type;
    }

    public void setLoanType(String loanType) {
        this.loan_Type = loanType;
    }

    public double getLoanAmount() {
        return loan_Amount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loan_Amount = loanAmount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

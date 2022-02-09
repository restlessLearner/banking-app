package polestarbank;

import MYSQLConnector.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoanController extends LoginSceneController implements Initializable {

    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private TextField amount;

    @FXML
    private TextField duration;


    @FXML
    private ComboBox<String> loanTypes;
    @FXML
    private TextField idNum;
    @FXML
    private TextField salary;
    @FXML
    private Button submitBTN;
    @FXML
    private Text resultTxt;
    @FXML
    private Text interestRate;
    @FXML
    private Text successTxT;


    //this method is for searching account by IDnumber to get account's information about the account type and then show the interest rate for each user.
    @FXML
    public void searchAcc(ActionEvent event) throws SQLException {
        getData(idNum.getText());
        String idNumber = idNum.getText();
        if(idNumber == null || idNumber.isEmpty()){
            interestRate.setText("Please enter your IDnumber to view your interest rate.");
            return;
        }
        if (Objects.equals(databaseAccountType, "Student")){
            interestRate.setText("0,2 %");
        } else if(Objects.equals(databaseAccountType, "Youth")) {
            interestRate.setText("0,0 %");
        } else {
            interestRate.setText("0,3%");
        }
    }


    /*  this method is for handling and check all the loan application information when the user click on submit.
    If there are empty fields, the submission will not be made */
    @FXML
    public void handleSubmit(ActionEvent event) throws SQLException {
        resultTxt.setText("");
        successTxT.setText("");

        String idLoan = idNum.getText();
        String durations = duration.getText();
        getData(idLoan);

        if(idLoan == null || idLoan.isEmpty()){
            resultTxt.setText("Please fill in your ID number");
            return;
        }
        if(durations == null || durations.isEmpty()){
            resultTxt.setText("Please fill in your ID number");
            return;
        }
        if(amount.getText() == null || amount.getText().isEmpty()){
            resultTxt.setText("Please fill in the amount of loan");
            return;
        }
        if(salary.getText() == null || salary.getText().isEmpty()){
            resultTxt.setText("Please fill in your salary number");
            return;
        }

        String loanType = loanTypes.getSelectionModel().getSelectedItem().toString();
        double loanAmount = Double.parseDouble(amount.getText());;
        if(Objects.equals(databaseAccountType, "Student") && (loanAmount > 10000) ){
            resultTxt.setText("You can not apply for this amount of loan");
            return;
        }
        if(Objects.equals(databaseAccountType, "Youth")){
            resultTxt.setText("You can not apply for a loan");
            return;
        }
        if(Objects.equals(databaseAccountType, "Student") && loanType.equals("Mortgage Loan") || loanType.equals("Small Business Loan")){
            resultTxt.setText("You can not apply for this type of loan");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Submission Confirmation");
        alert.setHeaderText("Do you wish to proceed this submission?");
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            insertSubmission();
            successTxT.setText("Your submission was made successfully");
            resultTxt.setText("");
        }else{
            resultTxt.setText("Your submission was canceled.");
        }
        idNum.clear();
        duration.clear();
        amount.clear();
        salary.clear();
        interestRate.setText("");

    }


    //this method is for inserting the submission details to the database
    public void insertSubmission() throws SQLException {
        String id = idNum.getText();
        String salaryAmount = salary.getText();
        String loanType = loanTypes.getSelectionModel().getSelectedItem().toString();
        String loanAmount = amount.getText();
        String durations = duration.getText();
        String status = "pending...";

        String sql = "INSERT INTO Loan(IDnumber,salary,loan_type,loan_amount,duration,status)VALUES(?, ?, ?, ?, ?,?)";
        PreparedStatement statement = connectDB.prepareStatement(sql);
        statement.setString(1,id);
        statement.setString(2,salaryAmount);
        statement.setString(3,loanType);
        statement.setString(4,loanAmount);
        statement.setString(5,durations);
        statement.setString(6,status);
        statement.execute();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanTypes.setItems(FXCollections.observableArrayList("Personal Loan", "Student Loan",
                "Small Business Loan", "Mortgage Loan"));

    }
}


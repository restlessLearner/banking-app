package polestarbank;

import MYSQLConnector.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CustomerHomeScreenController  extends LoginSceneController implements Initializable  {

    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Text REAccountNum;
    @FXML
    private Text REAccountType;
    @FXML
    private Text REfirstname;
    @FXML
    private Text SDAccountNum;
    @FXML
    private Text SDaccountType;
    @FXML
    private Text SDfirstname;
    @FXML
    private Text SDsurname;
    @FXML
    private TextField amount;
    @FXML
    private Text currentBalance;
    @FXML
    private TextField receiverID;
    @FXML
    private Text warningMSG;

    @FXML
    private Text resultTxT;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //display account details in home screen
    public void displayName(String username, String surname, int accountNumber, double balance) {
        nameLabel.setText(username);
        surnameLabel.setText(surname);
        accountLabel.setText(String.valueOf(accountNumber));
        balanceLabel.setText(String.valueOf(balance + " Kr"));
    }

    //display customer account details on the transfer page
    public void displayAccountDetails(String firstname, String surname, double balance, int accountNum, String accountType) {
        SDfirstname.setText(String.valueOf(firstname));
        SDsurname.setText(String.valueOf(surname));
        currentBalance.setText(String.valueOf(balance));
        SDAccountNum.setText(String.valueOf(accountNum));
        SDaccountType.setText(String.valueOf(accountType));
    }

//method to load a fxml page
    private void loadPage(String page)  throws IOException {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page + ".fxml"));
        root = loader.load();
        bp.setCenter(root);
    }


    @FXML // display the transfer page on the home screen
    void transferPressed(ActionEvent event) throws SQLException, IOException {
        getData(loginID);
        bp.setCenter(ap);
    }

    //load account details fxml page and calls the method displayAccountDetails from
    // the AccountDetailsController class to display here
    @FXML
    void accountDetailsButtonPressed(ActionEvent event) throws IOException, SQLException {
        getData(loginID);
        //bp.setCenter(ap);
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountDetails.fxml"));
        root = loader.load();
        bp.setCenter(root);
        AccountDetailsController controller = loader.getController();
        controller.displayAccountDetails(databaseName, databaseLastName, databaseEmail, databasePhoneNumber, databaseAccountType ,databaseNumber ,databaseCardNumber);
    }

    //load virtual card fxml page and calls the method displayCardDetails from
    // the AccountDetailsController class to display here
    @FXML
    public void cardsButtonPressed(ActionEvent event) throws SQLException, IOException {
        getData(loginID);
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("virtualCard.fxml"));
        root = loader.load();
        bp.setCenter(root);
        AccountDetailsController controller = loader.getController();
        controller.displayCardDetails(databaseCardNumber, databaseCardNumber,databaseName, databaseLastName, databaseCCV, databaseBalance);
    }

//load transaction history
    @FXML
    void transcationHistoryButtonPressed(ActionEvent event) throws IOException {
        loadPage("TransactionHistory");
    }

    //load deactivate account fxml page and calls the method displayDetails from
    // the AccountDetailsController class to display here
    @FXML
    void deactivateAccountButtonPressed(ActionEvent event) throws IOException, SQLException {
        getData(loginID);
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("deactivateAccount.fxml"));
        root = loader.load();
        bp.setCenter(root);
        AccountDetailsController controller = loader.getController();
       controller.displayDetails(databaseNumber, databaseBalance);
    }
//load the loan page
    @FXML
    void loanButtonPressed(ActionEvent event) throws IOException {
        loadPage("Loan");
    }
//load the customer service page
    @FXML
    void CustomerServiceButtonPressed(ActionEvent event) throws IOException {
        loadPage("CustomerService");
    }
 //load the login page
    @FXML
    void logoutButtonPressed(ActionEvent event) throws IOException {
        //return to login page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


    /////////////////////////////////Transfer///////////////////////////////////////////////////

    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    // this method is for checking the validity of the information inputted.
    @FXML
    public void handleTransaction(ActionEvent event) throws SQLException {
        String REidNumber = receiverID.getText();
        String tAmount = amount.getText();
        resultTxT.setText("");
        warningMSG.setText("");

        if (REidNumber == null || REidNumber.isEmpty()) {
            warningMSG.setText("Please enter receiver account number.");
            receiverID.requestFocus();
            return;
        }
        if (tAmount == null || tAmount.isEmpty()) {
            warningMSG.setText("Please enter amount.");
            receiverID.requestFocus();
            return;
        }
        double amt = Double.parseDouble(amount.getText());
        if(amt <= 0 ){
            warningMSG.setText("Please enter new amount.");
            return;
        }

        getData(loginID);
        if(databaseBalance < amt ){
            warningMSG.setText("You don't have enough money in your account.");
        }else if (String.valueOf(databaseNumber).equals(REidNumber)){
            warningMSG.setText("You can not transfer to your own account.");
        }else{
            insertTransaction();
            String sql = "UPDATE customer_details SET Current_balance = '" + (databaseBalance -= amt) + "' WHERE Account_number = '" + databaseNumber + "'";
            PreparedStatement statement2 = connectDB.prepareStatement(sql);
            statement2.executeUpdate(sql);
            getReceiverAccount(receiverID.getText());

            resultTxT.setText("Your transaction was made successfully.");
        }

        amount.clear();

    }

    // this method is for searching receiver account whom the logged-in user want to transfer money to.
    @FXML
    public void searchAccount(ActionEvent event) throws SQLException {
        String accNumber = receiverID.getText();

        try {
            PreparedStatement statement = connectDB.prepareStatement("SELECT * from customer_details WHERE Account_number = ? ");
            statement.setString(1, accNumber);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Not found");
                alert.setHeaderText("Please enter the right Account number.");
                alert.showAndWait();

            } else {
                REfirstname.setText(resultSet.getString("First_name"));
                REAccountNum.setText(resultSet.getString("Account_number"));
                REAccountType.setText(resultSet.getString("Account_type"));
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    // for getting receiver account when updating the current balance of the receiver account.
    public void getReceiverAccount(String receiverAcc ) throws SQLException {
        double amt = Double.parseDouble(amount.getText());
        double balance = 0.0;
        Statement statement = connectDB.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from customer_details WHERE Account_number = '" + receiverAcc +"'");
        if(rs.next()) {
            balance = Double.parseDouble(rs.getString("Current_balance"));
            updateREBalance(balance, amt,receiverAcc);
        }

    }

    // this is for getting the current date of when the transaction is made.
        public String getDate(){
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return dtf.format(localDateTime);
    }


    public void updateREBalance(double balance,double amt, String reAccNum) throws SQLException {
        String sql = "UPDATE customer_details SET Current_balance = '" + ( balance += amt) + "' WHERE Account_number = '" + reAccNum
                + "'";
        PreparedStatement statement2 = connectDB.prepareStatement(sql);
        statement2.executeUpdate(sql);

    }

    // this method is for inserting each transaction information into the database
    public void insertTransaction() throws SQLException {

        String idNum = databaseID;
        String date = getDate();
        String senderAcc = String.valueOf(databaseNumber);
        String transferAmount = amount.getText();
        String receiverAcc = receiverID.getText();

        String sql = "INSERT INTO Transcations(IDnumber,Date,Sender_account,Transfered_amount,Receiver_account)VALUES(?, ?, ?, ?,?)";

        PreparedStatement statement = connectDB.prepareStatement(sql);
        statement.setString(1,idNum);
        statement.setString(2,date);
        statement.setString(3,senderAcc);
        statement.setString(4,transferAmount);
        statement.setString(5,receiverAcc);
        statement.execute();

    }

}

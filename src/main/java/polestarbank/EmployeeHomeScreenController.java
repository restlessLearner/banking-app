package polestarbank;

import MYSQLConnector.DatabaseConnector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class EmployeeHomeScreenController  extends LoginSceneController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private BorderPane bp;

    //method to load a fxml page
    private void loadPage(String page)  throws IOException {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page + ".fxml"));
        root = loader.load();
        bp.setCenter(root);
    }

    //display the transfer page on the home screen
    @FXML
    void transferPressed(ActionEvent event) {
        bp.setCenter(ap);
    }
    //load loans fxml page
    @FXML
    void approveLoanPressed(ActionEvent event) throws IOException {
        loadPage("Loans");
    }

    //load register employee fxml page
    @FXML
    void createNewEmployeePressed(ActionEvent event) throws IOException {
        loadPage("RegisterEmployee");
    }

    //load customer list fxml page
    @FXML
    void customerListPressed(ActionEvent event) throws IOException {
        loadPage("customerList");
    }

    //load the login fxml page
    @FXML
    void logoutPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSceneController.class.getResource("Login.fxml"));
        Stage stage1 = (Stage) closeButton1.getScene().getWindow();
        stage1.setScene(new Scene(fxmlLoader.load(), 849, 609));
    }

//load transaction history fxml page
    @FXML
    void transcationHistoryButtonPressed(ActionEvent event) throws IOException {
        loadPage("CustomerTransactionHistory");
    }

//laod update existing account fxml page
    @FXML
    void updateExistingCustomerPressed(ActionEvent event) throws IOException {
        loadPage("updateExistingAccount");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();


    @FXML
    private Label errorLabel;
    @FXML
    private TextField amount;
    @FXML
    private Button closeButton1;
    @FXML
    private TextField reAccnum;
    @FXML
    private Text reCurrentBalance;
    @FXML
    private Text reFirstname;
    @FXML
    private Text reLastname;
    @FXML
    private TextField sdAccnum;
    @FXML
    private Text sdCurrentBalance;
    @FXML
    private Text sdFirstname;
    @FXML
    private Text sdLastname;
    @FXML
    private Text resultTxT;


    //////////////////Transfer//////////////////////


    //this method is for searching for receiver's account
    @FXML
    public void searchREAcc(ActionEvent event) throws SQLException {
        String receiverAccNum = reAccnum.getText();

        try {
            PreparedStatement statement = connectDB.prepareStatement("SELECT * from customer_Details WHERE Account_number = ? ");
            statement.setString(1, receiverAccNum);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Not found");
                alert.setHeaderText("Please enter the right Account number.");
                alert.showAndWait();

            } else{
                reFirstname.setText(resultSet.getString("First_name"));
                reLastname.setText(resultSet.getString("Surname"));
                reCurrentBalance.setText(resultSet.getString("Current_balance"));
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    //this method is for searching for sender's account
    @FXML
    public void searchSDAcc(ActionEvent event) throws SQLException {
        String senderAccNum = sdAccnum.getText();

        try {
            PreparedStatement statement = connectDB.prepareStatement("SELECT * from customer_Details WHERE Account_number = ? ");
            statement.setString(1, senderAccNum);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Not found");
                alert.setHeaderText("Please enter the right Account number.");
                alert.showAndWait();

            } else{
                sdFirstname.setText(resultSet.getString("First_name"));
                sdLastname.setText(resultSet.getString("Surname"));
                sdCurrentBalance.setText(resultSet.getString("Current_balance"));
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }


    // this method is for checking the validity of the information inputted.
    @FXML
    public void transferMoney(ActionEvent event) throws SQLException {
        resultTxT.setText("");
        errorLabel.setText("");
        String SDAccNum = sdAccnum.getText();
        String REAccNum = reAccnum.getText();
        String sdAccNum = sdAccnum.getText();
        String tAmount = amount.getText();

        if (SDAccNum == null || SDAccNum.isEmpty()) {
            errorLabel.setText("Please enter sender customer account's account number.");
            sdAccnum.requestFocus();
            return;

        }

        if (REAccNum == null || REAccNum.isEmpty()) {
            errorLabel.setText("Please enter receiver customer account's account number.");
            sdAccnum.requestFocus();
            return;

        }

        if (tAmount == null || tAmount.isEmpty()) {
            errorLabel.setText("Please enter amount.");
            return;

        }

        if(REAccNum.equals(SDAccNum)){
            errorLabel.setText("You can not transfer to the same account.");
            return;
        }

        double amt = Double.parseDouble(tAmount);

        if(amt <= 0 ){
            errorLabel.setText("Please enter new amount.");
            return;
        }

        Statement statement = connectDB.createStatement();
        ResultSet rs = statement.executeQuery("SELECT Current_balance from customer_details WHERE Account_number = '" + SDAccNum +"'");
        if(rs.next()) {
            double currentBalance = Double.parseDouble(rs.getString("Current_balance"));
            if (currentBalance < amt) {
                errorLabel.setText("There's insufficient money in sender's account.");
            }  else {
                handleTransactionREAcc();
                handleTransactionSDAcc();
                insertTransaction();
                resultTxT.setText("Transaction has been made successfully.");
            }

        }
        amount.clear();


    }


    // this method is for updating sender account balance information
    public void handleTransactionSDAcc() throws SQLException {
        double amt = Double.parseDouble(amount.getText());
        String senderAccNum = sdAccnum.getText();
        double balance = 0.0;

        Statement statement = connectDB.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from customer_details WHERE Account_number = '" + senderAccNum + "'");

        if (rs.next()) {
            balance = Double.parseDouble(rs.getString("Current_balance"));
            String sql = "UPDATE customer_details SET Current_balance = '" + (balance -= amt) + "' WHERE Account_number = '" + senderAccNum
                    + "'";
            PreparedStatement statement2 = connectDB.prepareStatement(sql);
            statement2.executeUpdate(sql);

        }


    }


    // this method is for updating receiver account balance information
    public void handleTransactionREAcc() throws SQLException {
        double amt = Double.parseDouble(amount.getText());
        String receiverAccNum = reAccnum.getText();
        double balance = 0.0;

        Statement statement = connectDB.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from customer_details WHERE Account_number = '" + receiverAccNum +"'");
        if(rs.next()) {
            balance = Double.parseDouble(rs.getString("Current_balance"));

            String sql = "UPDATE customer_details SET Current_balance = '" + ( balance += amt) + "' WHERE Account_number = '" + receiverAccNum
                    + "'";
            PreparedStatement statement2 = connectDB.prepareStatement(sql);
            statement2.executeUpdate(sql);

        }

    }

    //this method is for getting the current date of the transaction made
    public String getDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dtf.format(localDateTime);

    }


    // this method is for inserting each transaction information into the database
    public void insertTransaction() throws SQLException {
        getData(sdAccnum.getText());

        String idNum = databaseID;
        String date = getDate();
        String senderAcc = String.valueOf(databaseNumber);
        String transferAmount = amount.getText();
        String receiverAcc = reAccnum.getText();

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


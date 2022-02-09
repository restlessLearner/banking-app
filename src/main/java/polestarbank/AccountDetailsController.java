package polestarbank;


import MYSQLConnector.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.*;


public class AccountDetailsController extends LoginSceneController {


    ////////////////////////////////////////////////VIEW AND UPDATE ACCOUNT DETAILS//////////////////////////////////////////////////////////////////
    @FXML
    private TextField accountNumberTextField;
    @FXML
    private TextField accountTypeTextField;
    @FXML
    private TextField cardNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private PasswordField newPasswordTextField;
    @FXML
    private PasswordField oldPasswordTextField;
    @FXML
    private Label wariningMSG;
    @FXML
    private Label sucsessLabel;

    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    /* method to display account details on the screen*/
    public void displayAccountDetails(String name, String surname, String email, int phoneNumber, String accountType, int accountNumber, int cardNumber) {
        nameTextField.setText(name);
        surnameTextField.setText(surname);
        emailTextField.setText(email);
        phoneNumberTextField.setText(String.valueOf(phoneNumber));
        accountTypeTextField.setText(accountType);
        accountNumberTextField.setText(String.valueOf(accountNumber));
        cardNumberTextField.setText(String.valueOf(cardNumber));
    }

/*create sql query that updates customer details */
    @FXML
    void updateDetailsButton(ActionEvent event) throws SQLException {
        getData(loginID);
        if (surnameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || phoneNumberTextField.getText().isEmpty()) {
            wariningMSG.setText("Fields cannot be blank");
        } else {
            String sql1 = "UPDATE customer_Details SET Surname = '" + surnameTextField.getText() + "' WHERE IDnumber = '" + databaseID + "'";
            PreparedStatement statement = connectDB.prepareStatement(sql1);
            statement.executeUpdate(sql1);

            String sql2 = "UPDATE customer_Details SET Email = '" + emailTextField.getText() + "' WHERE IDnumber = '" + databaseID + "'";
            PreparedStatement statement1 = connectDB.prepareStatement(sql2);
            statement1.executeUpdate(sql2);

            String sql3 = "UPDATE customer_Details SET Phone_number = '" + phoneNumberTextField.getText() + "' WHERE IDnumber = '" + databaseID + "'";
            PreparedStatement statement2 = connectDB.prepareStatement(sql3);
            statement2.executeUpdate(sql3);
            sucsessLabel.setText("Your details has been successfully updated");

        }
    }
/*create a sql query that updates the password*/
    @FXML
    void confirmNewPassword(ActionEvent event) throws SQLException {
        getData(loginID);
        if (oldPasswordTextField.getText().isBlank()) {
            wariningMSG.setText("Enter your old password code");
        } else if (!oldPasswordTextField.getText().equals(databasePassword)) {
            wariningMSG.setText("incorrect old password");
        } else if (newPasswordTextField.getText().isEmpty()) {
            wariningMSG.setText("Fill in your new password");
        } else if (newPasswordTextField.getText().length() < 10) {
            wariningMSG.setText("Your password must contain at least 10 characters");
        } else {
            String sql2 = "UPDATE customer_Details SET Password = '" + newPasswordTextField.getText() + "' WHERE IDnumber = '" + databaseID + "'";
            PreparedStatement statement = connectDB.prepareStatement(sql2);
            statement.executeUpdate(sql2);
            sucsessLabel.setText("Your password was successfully changed");
        }
    }


    /////////////////////////////////////////////Deactivate account/////////////////////////////////////////////////////////////

    @FXML
    private Label accountNumberLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private CheckBox checkBox;

/* display account details on the screen*/
    public void displayDetails(int accountNumber, double balance) {
        accountNumberLabel.setText(String.valueOf(accountNumber));
        balanceLabel.setText(String.valueOf(balance + "kr"));
    }

    /*if the password correct an alert message  will be shown to make sure the user knows what happens after they deactivate
     their account, else and error message will be shown to the user*/
    @FXML
    private void proceedButton() throws SQLException {
        getData(loginID);
        if (passwordTextField.getText().equals(databasePassword)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Terms and conditions");
            alert.setTitle("Polestar BANK");
            alert.setContentText("After deactivation \n\n1. If you had any money in your account you \n  wont be able to access it permanently" +
                    "\n2. You wont be able to login again with your previous credentials\n   you will have to register a new account " +
                    "\n3. You will be logged out\n\nSelect ok to continue with deactivation " +
                    "or click cancel ");
            alert.showAndWait();
        } else {
            errorLabel.setText("Enter correct password to permanently deactivate your account");
        }
    }


    /*if the checkbox is selected, the sql will permanently delete the customer from the database,
    * else and error message will be shown to the user*/
    @FXML
    void deavtivate(ActionEvent event) throws SQLException, IOException {
        if (checkBox.isSelected()) {
            String sql = "delete from customer_Details where IDnumber = '" + databaseID + "'";
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            preparedStatement.execute();

            /* redirecting the user to the login page by creating an instance of the
            "customerHomeScreenController class and calling the method in this class*/
            CustomerHomeScreenController homeScreen = new CustomerHomeScreenController();
            homeScreen.logoutButtonPressed(event);
        } else {
            errorLabel.setText("Please check the terms and conditions if you want to proceed.");
        }

    }


    //////////////////////////////////////////////VIRTUAL CARD ////////////////////////////////////////////////////////////////////
    @FXML
    private Label balance;
    @FXML
    private Label cardNumberLabel;
    @FXML
    private Label cardNumberLabel1;
    @FXML
    private Label ccvLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label error;
    @FXML
    private Label changesMadeLabel;
    @FXML
    private PasswordField newPinTextField;
    @FXML
    private PasswordField oldPinTextField;

    /*display account holders information and card details*/
    public void displayCardDetails(int cardNumber, int cardNumber1, String name, String surname, int ccv, double balance) {
        cardNumberLabel.setText(String.valueOf(cardNumber));
        cardNumberLabel1.setText(String.valueOf(cardNumber1));
        nameLabel.setText(name + " " + surname);
        ccvLabel.setText(String.valueOf(ccv));
        this.balance.setText(String.valueOf(balance));
    }

    /*the user can change their pin code, and the new password will be updated in the database.
    if the old pin code does not match the password in the database, or if the old password
    does not match the format that the system requires the pin code change will be denied*/
    @FXML
    public void changePin(ActionEvent event) throws SQLException {
        getData(loginID);

        if (oldPinTextField.getText().isBlank()) {
            error.setText("Enter your old pin code");
        } else if (!oldPinTextField.getText().equals(databaseCardPin)) {
            error.setText("incorrect old pin code");
        } else if (newPinTextField.getText().isEmpty()) {
            error.setText("Fill in your new pin code");
        } else if (newPinTextField.getText().length() != 4) {
            error.setText("Your pin must contain 4 digits");
        } else {
            String sql2 = "UPDATE customer_Details SET Card_pin = '" + newPinTextField.getText() + "' WHERE IDnumber = '" + databaseID + "'";
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql2);
            preparedStatement.execute();
            changesMadeLabel.setText("Your pin code has been updated");
        }
    }


}


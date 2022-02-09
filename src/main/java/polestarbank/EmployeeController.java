package polestarbank;


import MYSQLConnector.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TextField customerAccountNumberTextField;
    @FXML
    private TextField customerEmailTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField customerPhoneNumberTextFiled;
    @FXML
    private TextField customerSurnameTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label label;

    public static String EOL = System.lineSeparator();
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    /*Connect to the database and use the account number to search for an account,
    * if a non-existing account number is provided, a error message will be shown*/
    @FXML
    void searchAccountButton(ActionEvent event) {

        String accountNumber =  customerAccountNumberTextField.getText();
        try {
            PreparedStatement statement = connectDB.prepareStatement("select First_name, Surname, Email, Phone_number from customer_Details where Account_number = ?");
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            if (accountNumber.isEmpty()) {
                errorLabel.setText("Enter an account number.");
            }
            else if (resultSet.next()) {
                String name = resultSet.getString(1);
                String surname = resultSet.getString(2);
                String email = resultSet.getString(3);
                String phoneNumber = resultSet.getString(4);

                customerNameTextField.setText(name);
                customerSurnameTextField.setText(surname);
                customerEmailTextField.setText(email);
                customerPhoneNumberTextFiled.setText(phoneNumber);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Not found");
                alert.setHeaderText("Account number not found, " +  EOL + " Please enter a valid account number.");
                alert.showAndWait();}

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//create a sql query to update customer details
    @FXML
    void UpdatePressed(ActionEvent event) throws SQLException {

        String accountNo = customerAccountNumberTextField.getText();


        String sql1 = "UPDATE customer_Details SET Surname = '" + customerSurnameTextField.getText() + "' WHERE  Account_number = '" + accountNo + "'";
        PreparedStatement statement = connectDB.prepareStatement(sql1);
        statement.executeUpdate(sql1);

        String sql2 = "UPDATE customer_Details SET Email = '" + customerEmailTextField.getText() + "' WHERE  Account_number = '" + accountNo + "'";
        PreparedStatement statement1 = connectDB.prepareStatement(sql2);
        statement1.executeUpdate(sql2);

        String sql3 = "UPDATE customer_Details SET Phone_number = '" + customerPhoneNumberTextFiled.getText() + "' WHERE  Account_number = '" + accountNo + "'";
        PreparedStatement statement2 = connectDB.prepareStatement(sql3);
        statement2.executeUpdate(sql3);
        label.setText("Customer account details are updated");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerNameTextField.setEditable(false);
    }
}


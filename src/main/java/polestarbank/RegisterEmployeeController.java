package polestarbank;


import MYSQLConnector.DatabaseConnector;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterEmployeeController implements Initializable {

    @FXML
    Label registrationLabel;
    @FXML
    TextField employeeID;
    @FXML
    TextField Username;
    @FXML
    PasswordField Password;
    @FXML
    TextField name;
    @FXML
    TextField Surname;
    @FXML
    Button signUpButton;
    @FXML
    Text registrationText;


            @FXML
            public void Register() throws SQLException {

                DatabaseConnector connection = new DatabaseConnector();
                Connection connectDB = connection.getConnection();

              //prevent user from leaving empty fields
                if( employeeID.getText().isEmpty() ||Username.getText().isEmpty() || Password.getText().isEmpty() || name.getText().isBlank() || Surname.getText().isEmpty()){
                    registrationLabel.setText("You must fill all boxes!");
//prevents user from choosing a password length less than 10
                } else if
                (Password.getText().length()<10){
                    registrationLabel.setText("Please choose at least 10 characters, numbers, and symbols");

                //If all is valid, info will be inserted into the database
                } else {
                    String ID = employeeID.getText();
                    String password = Password.getText();
                    String userName = Username.getText();
                    String firstName = name.getText();
                    String surName = Surname.getText();


                    String Fields = "INSERT INTO employeedetails(EmployeeID, Username, Password, Name, Surname) VALUES('";
                    String Values = ID + "','" + password + "','" + userName + "','" + firstName + "','" + surName + "')";
                    String register = Fields + Values;

                    try {
                        Statement statement = connectDB.createStatement();

                        statement.execute(register);
                        registrationLabel.setText("");
                        registrationText.setText("The employee account has been registered successfully!");


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}


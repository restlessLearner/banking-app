package polestarbank;


import MYSQLConnector.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;


public class signUpController implements Initializable {

    @FXML
    Label Card_number;
    @FXML
    Label CCV;
    @FXML
    Label Account_number;
    @FXML
    Label Card_pin;
    @FXML
    Label checkLabel;
    @FXML
    Label registrationLabel;
    @FXML
    TextField IDnumber;
    @FXML
    TextField first_name;
    @FXML
    TextField Surname;
    @FXML
    PasswordField password;
    @FXML
    ComboBox<String>  question;
    @FXML
    TextField answer;
    @FXML
    ComboBox<String> accountType;
    @FXML
    TextField phoneNumber;
    @FXML
    TextField Email;
    @FXML
    TextField DOB;
    @FXML
    Button signUpButton;
    @FXML
    Button goBackBtn;


  
    DatabaseConnector connection = new DatabaseConnector();
    Connection connectDB = connection.getConnection();




    @FXML //sign up controller
    public void Register() throws SQLException {

        //prevents the user from leaving any empty fields
        if (first_name.getText().isEmpty() || Surname.getText().isEmpty() || password.getText().isEmpty() || IDnumber.getText().isBlank() || phoneNumber.getText().isEmpty() ||
                String.valueOf(DOB.getText()).isEmpty() || Email.getText().isEmpty() || answer.getText().isEmpty() || question.getItems().isEmpty() || accountType.getItems().isEmpty()) {

            registrationLabel.setText("You must fill in all boxes!");

        } else if //prevents user from creating a password that is shorter than 10 letters and numbers to make it more secure.
        (password.getLength() < 10 ) {

            registrationLabel.setText("Please choose at least 10 characters, numbers, or symbols for your password");
        //Prevents the user from creating an IDnumber,phone number, or Birth date that is longer or shorter than what is required in the format.
        } else if (IDnumber.getLength() != 9 || phoneNumber.getLength() != 9 || DOB.getLength() != 10) {

            registrationLabel.setText("Please make sure your info is valid");

        } else {
            //get the user's input info
            String Idnumber = IDnumber.getText();
            String pass = password.getText();
            String firstName = first_name.getText();
            String surName = Surname.getText();
            String dob = String.valueOf(DOB.getText());
            String eMail = Email.getText();
            String phoneNum = phoneNumber.getText();
            String accountTyp = accountType.getSelectionModel().getSelectedItem().toString();
            String ques = question.getSelectionModel().getSelectedItem().toString();
            String ans = answer.getText();


            //generate card number, ccv, account number, card pin and get the values
            Random random = new Random();
            int cardNumber = random.nextInt(999999999);
            cardNumber += 100000000;
            Card_number.setText(String.valueOf(cardNumber));

            Random random1 = new Random();
            int ccv = random1.nextInt(999);
            ccv += 100;
            CCV.setText(String.valueOf(ccv));

            Random random2 = new Random();
            int accountNumber = random2.nextInt(9999999);
            accountNumber += 1000000;
            Account_number.setText(String.valueOf(accountNumber));

            Random random3 = new Random();
            int cardPin = random3.nextInt(9999);
            cardPin += 1000;
            Card_pin.setText(String.valueOf(cardPin));

            //Insert the users info into the database
            String Fields = "INSERT INTO customer_details(IDnumber, Password, First_name, Surname, DOB, Email, Phone_number, Account_type, Card_number, CCV, Card_pin, Account_number, Security_question, Answer) VALUES('";
            String Values = Idnumber + "','" + pass + "','" + firstName + "','" + surName + "','" + dob + "','" + eMail + "','" + phoneNum + "','" + accountTyp + "','" + cardNumber + "','" + ccv + "','" + cardPin + "','" + accountNumber + "','" + ques + "','" + ans + "')";
            String register = Fields + Values;
            try {
                Statement statement = connectDB.createStatement();
                if  (statement.execute(register)) {

                }
                signupBtn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//switch screens to pop up success window
    public void signupBtn() throws Exception {
        DatabaseConnector connect = new DatabaseConnector();
        FXMLLoader fxmlLoader = new FXMLLoader(SuccessWindow.class.getResource("SuccessfulSignup.fxml"));
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 568, 266));
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        question.setItems(FXCollections.observableArrayList("What high school did you attend?","In what city were you born?", "What was the first concert you attended?", "What is your pets name?",
                "What is your mothers middle name?", "What is your grandmothers first name?","What was your dream job as a child?","What was your childhood nickname?","Who was your childhood hero?"));

        accountType.setItems(FXCollections.observableArrayList("Student","Savings","Youth","Regular"));
    }

    public void goBackBtn() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSceneController.class.getResource("Login.fxml"));
        Stage stage1 = (Stage) goBackBtn.getScene().getWindow();
        stage1.setScene(new Scene(fxmlLoader.load(), 849, 609));


    }





}

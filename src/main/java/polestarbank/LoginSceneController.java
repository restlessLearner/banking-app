package polestarbank;
import MYSQLConnector.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginSceneController {
    static protected String databaseID = " ";
    static protected String databasePassword = " ";
    static protected String databaseName = " ";
    static protected String databaseLastName = " ";
    static protected String databaseEmail = " ";
    static protected int databaseCardNumber;
    static protected int databaseCCV;
    static protected String databaseCardPin;
    static protected int databaseNumber;
    static protected double databaseBalance = 0;
    static protected String databaseAccountType = "";
    static protected int databasePhoneNumber = 0;

    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private TextField IDnumber;
    @FXML
    private Label validateLabel;
    @FXML
    Button exitBtn;
    @FXML
    Button signUpBtn;
    @FXML
    Button forgotPassButton;
    @FXML
    Button EmployeeButton;

    protected Stage stage;
    protected Scene scene;
    protected String loginID;

 // get and store information about logged-in user from the database
    public void getData(String loginID) throws SQLException {
        try {
            String sql = "SELECT * FROM customer_Details  WHERE  IDnumber = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            preparedStatement.setString(1, loginID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                databaseID = rs.getString("IDnumber");
                databasePassword = rs.getString("Password");
                databaseName = rs.getString("First_name");
                databaseLastName = rs.getString("Surname");
                databaseEmail = rs.getString("Email");
                databasePhoneNumber = Integer.parseInt(rs.getString("Phone_number"));
                databaseNumber = Integer.parseInt(rs.getString("Account_number"));
                databaseBalance = Double.parseDouble(rs.getString("Current_balance"));
                databaseCardNumber = Integer.parseInt(rs.getString("Card_number"));
                databaseCCV = Integer.parseInt(rs.getString("CCV"));
                databaseCardPin = rs.getString("Card_pin");
                databaseAccountType = rs.getString("Account_Type");
                System.out.println("Account Initialized.");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*if the details provided are do not match the database, an error message will be shown,
    if they are correct the user will be directed to the hmoe screen*/
    public void loginButtonOnAction(ActionEvent event) throws SQLException, IOException {
        loginID = IDnumber.getText();
        String loginPassword = password.getText();
        getData(loginID);
        if (IDnumber.getText().isBlank() || password.getText().isBlank()) {
            validateLabel.setText("enter both ID number and password.");
        } else if (!loginPassword.equals(databasePassword)) {
            validateLabel.setText("invalid login. Please try again!");
        } else {
            switchToHomeScreen(event);
        }
    }

//load home screen fxml page
    public void switchToHomeScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("customerHomeScreen.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        CustomerHomeScreenController controller = loader.getController();
        controller.displayName(databaseName, databaseLastName, databaseNumber, databaseBalance);
        controller.displayAccountDetails(databaseName, databaseLastName, databaseBalance, databaseNumber, databaseAccountType);

    }

//close the system
    public void exit(ActionEvent event) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    //laod sign up fxml page
    public void signupBtn(ActionEvent event) throws Exception {
        DatabaseConnector connect = new DatabaseConnector();
        FXMLLoader fxmlLoader = new FXMLLoader(signUpController.class.getResource("signUp.fxml"));
        Stage signupStage = (Stage) signUpBtn.getScene().getWindow();
        signupStage.setScene(new Scene(fxmlLoader.load(), 800, 650));
        signupStage.show();

    }

    //load forgot password fxml page
    public void forgotPassBtn(javafx.event.ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ForgotPasswordController.class.getResource("newPass.fxml"));
        Stage newPassStage = (Stage) forgotPassButton.getScene().getWindow();
        newPassStage.setScene(new Scene(fxmlLoader.load(), 635, 500));
        newPassStage.show();
    }

    //load employee login fxml page
    @FXML
    public void employeePage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeLogin.class.getResource("EmployeeLogin.fxml"));
        Stage stage = (Stage) EmployeeButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 650, 450));
        stage.show();

    }

}

package polestarbank;

import MYSQLConnector.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerServiceController extends LoginSceneController {


    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private Button back;

    @FXML
    private Text resultTXT;

    @FXML
    private TextArea comment;

   @FXML
    private Rating rating;

   @FXML
   private  Text successTxT;

    // this method is for checking if the user have selected the rating yet and inserting the review information into the database, If you have selected the rating between 1 - 5
    @FXML
    public void handleSubmit(ActionEvent event) throws SQLException {
        successTxT.setText("");
        resultTXT.setText("");

        if(rating.getRating() <= 0){
            resultTXT.setText("Please select between 1 - 5");
            return;
        }


        String rate = String.valueOf(rating.getRating());
        String comm = comment.getText();

        String sql = "INSERT INTO Reviews (rating,comment) VALUES (?,?)";
        PreparedStatement statement = connectDB.prepareStatement(sql);
        statement.setString(1, rate);
        statement.setString(2, comm);
        statement.execute();

        successTxT.setText("Your review was registered!");
        comment.clear();
    }




}

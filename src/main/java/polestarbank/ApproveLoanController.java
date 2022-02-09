package polestarbank;

import MYSQLConnector.DatabaseConnector;
import TableModels.ApproveLoanTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ApproveLoanController implements Initializable {

    @FXML
    private Button approve;
    @FXML
    private Button reject;
    @FXML
    private Button backButton;
    @FXML
    private TableView<ApproveLoanTable> tableView;
    @FXML
    private TableColumn<ApproveLoanTable, String> idNumber;
    @FXML
    private TableColumn <ApproveLoanTable, Double> salary;
    @FXML
    private TableColumn <ApproveLoanTable, String> loan_Type;
    @FXML
    private
    TableColumn <ApproveLoanTable, Double> loan_Amount;
    @FXML
    private TableColumn <ApproveLoanTable, Integer> duration;
    @FXML
    private TableColumn <ApproveLoanTable, String> status;

    final ObservableList<ApproveLoanTable> tableObservableList = FXCollections.observableArrayList();

    public ApproveLoanController() {}

    @Override
    public void initialize(URL url, ResourceBundle resource) {

        DatabaseConnector connectDB = new DatabaseConnector();
        Connection connection = connectDB.getConnection();

        @SuppressWarnings
                ("SpellCheckingInspection") String loanQuery =
                "SELECT IDnumber, salary, loan_type, loan_amount, duration, status FROM loan;";

        try{
            tableFill(connection, loanQuery);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void approveButtonOnAction(ActionEvent event) {

        DatabaseConnector connectDB = new DatabaseConnector();
        Connection connection = connectDB.getConnection();

        String updateQuery = "UPDATE loan SET status = 'Approved' WHERE IDnumber = ? ";

        try {
            update(connection, updateQuery);
            refresh();

        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public void rejectButtonOnAction(ActionEvent event){

        DatabaseConnector connectDB = new DatabaseConnector();
        Connection connection = connectDB.getConnection();

        String updateQuery = "UPDATE loan SET status = 'Rejected' WHERE IDnumber = ? ";

        try {
            update(connection, updateQuery);
            refresh();

        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void refresh() {

        DatabaseConnector connectDB = new DatabaseConnector();
        Connection connection = connectDB.getConnection();

        String refreshQuery = "SELECT * FROM loan;";

        try{
            tableFill(connection, refreshQuery);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void tableFill(Connection connection, String loanQuery) throws SQLException {
        Statement connectionStatement = connection.createStatement();
        ResultSet queryOutput = connectionStatement.executeQuery(loanQuery);

        while (queryOutput.next()){
            tableObservableList.add(new ApproveLoanTable(queryOutput.getString ("idNumber"),
                    queryOutput.getDouble("salary"),
                    queryOutput.getString("loan_Type"),
                    queryOutput.getDouble("loan_Amount"),
                    queryOutput.getInt("duration"),
                    queryOutput.getString("status")));

            idNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
            salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
            loan_Type.setCellValueFactory(new PropertyValueFactory<>("loanType"));
            loan_Amount.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
            duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));

            tableView.setItems(tableObservableList);
        }
    }

    public void update(Connection connection, String updateQuery) throws SQLException {

        ApproveLoanTable loan = tableView.getSelectionModel().getSelectedItem();

        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, loan.idNumber);
        preparedStatement.executeUpdate();

        tableObservableList.clear();

    }


}

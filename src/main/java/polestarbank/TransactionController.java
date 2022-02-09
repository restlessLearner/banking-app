package polestarbank;



import MYSQLConnector.DatabaseConnector;

import TableModels.TransactionTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TransactionController extends LoginSceneController implements Initializable {
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    @FXML
    private TableColumn<TransactionTable, Integer> col_destinationAccountNumber;
    @FXML
    private TableColumn<TransactionTable, Integer> col_senderAccountNumber;
    @FXML
    private TableColumn<TransactionTable, Double> col_amount;
    @FXML
    private TableColumn<TransactionTable, Date> col_date;
    @FXML
    private TableView<TransactionTable> tableView;
    @FXML
    private TableColumn<TransactionTable, Double> col_amount1;
    @FXML
    private TableColumn<TransactionTable, Date> col_date1;
    @FXML
    private TableColumn<TransactionTable, Integer> col_destinationAccountNumber1;
    @FXML
    private TableColumn<TransactionTable, Integer> col_senderAccountNumber1;
    @FXML
    private TableView<TransactionTable> tableView1;

    ObservableList<TransactionTable> transactionData = FXCollections.observableArrayList();
    ObservableList<TransactionTable> transactionData2 = FXCollections.observableArrayList();

    private void initCol() throws SQLException {
        getData(loginID);
        String sql2 = "SELECT * FROM Transcations WHERE Receiver_account = '" + databaseNumber + "'";
        PreparedStatement statement = connectDB.prepareStatement(sql2);
        ResultSet resultSet = statement.executeQuery(sql2);
        while (resultSet.next()) {
            Date date = Date.valueOf(resultSet.getString("Date"));
            Double amount = Double.valueOf(resultSet.getString("Transfered_amount"));
            int ReceiverAccountNumber = Integer.parseInt(resultSet.getString("Receiver_account"));
            int SenderAccountNumber = Integer.parseInt(resultSet.getString("Sender_account"));
            transactionData.add(new TransactionTable(date, SenderAccountNumber, ReceiverAccountNumber, amount));
        }
    }

    private void initCol2() throws SQLException {
        getData(loginID);
        String sql2 = "SELECT * FROM Transcations WHERE Sender_account = '" + databaseNumber + "'";
        PreparedStatement statement = connectDB.prepareStatement(sql2);
        ResultSet resultSet = statement.executeQuery(sql2);
        while (resultSet.next()) {
            Date date = Date.valueOf(resultSet.getString("Date"));
            Double amount = Double.valueOf(resultSet.getString("Transfered_amount"));
            int ReceiverAccountNumber = Integer.parseInt(resultSet.getString("Receiver_account"));
            int SenderAccountNumber = Integer.parseInt(resultSet.getString("Sender_account"));
            transactionData2.add(new TransactionTable(date, SenderAccountNumber, ReceiverAccountNumber, amount));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setItems(transactionData);
        tableView1.setItems(transactionData2);
        try {
            initCol();
            initCol2();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_senderAccountNumber.setCellValueFactory(new PropertyValueFactory<>("senderAccountNumber"));
        col_destinationAccountNumber.setCellValueFactory(new PropertyValueFactory<>("receiverAccountNumber"));
        col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        col_date1.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_senderAccountNumber1.setCellValueFactory(new PropertyValueFactory<>("senderAccountNumber"));
        col_destinationAccountNumber1.setCellValueFactory(new PropertyValueFactory<>("receiverAccountNumber"));
        col_amount1.setCellValueFactory(new PropertyValueFactory<>("amount"));


        tableView.setItems(transactionData);
        tableView1.setItems(transactionData2);
    }
}


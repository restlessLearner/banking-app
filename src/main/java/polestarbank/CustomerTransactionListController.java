package polestarbank;

import MYSQLConnector.DatabaseConnector;
import TableModels.TransactionTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerTransactionListController implements Initializable {

    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    @FXML
    private TableView<TransactionTable> transactionsTable;
    @FXML
    private TableColumn<TransactionTable, String> col_id;
    @FXML
    private TableColumn<TransactionTable, Date> col_date;
    @FXML
    private TableColumn<TransactionTable, Integer> col_sender;
    @FXML
    private TableColumn<TransactionTable, Integer> col_amount;
    @FXML
    private TableColumn<TransactionTable, Integer> col_payee;
    @FXML
    private TextField keywordTextField;


    ObservableList<TransactionTable> transactionsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String transactionViewQuery = "SELECT * FROM Transcations;";
        try {
            Statement stm = connectDB.createStatement();
            ResultSet queryOutput = stm.executeQuery(transactionViewQuery);
            while (queryOutput.next()) {

                String queryId = queryOutput.getString("IDnumber");
                Date queryDate = queryOutput.getDate("Date");
                int querySenderAcc = queryOutput.getInt("Sender_account");
                Integer queryAmount = queryOutput.getInt("Transfered_amount");
                Integer queryPayeeAcc = queryOutput.getInt("Receiver_account");


                transactionsList.add(new TransactionTable(queryId, queryDate,
                        querySenderAcc, queryAmount, queryPayeeAcc));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
            col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            col_sender.setCellValueFactory(new PropertyValueFactory<>("senderAccountNumber"));
            col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            col_payee.setCellValueFactory(new PropertyValueFactory<>("receiverAccountNumber"));

            transactionsTable.setItems(transactionsList);

            FilteredList<TransactionTable> filteredData = new FilteredList<>(transactionsList, b -> true);
            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(TransactionTable -> {

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (TransactionTable.getIdNumber().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else if (TransactionTable.getSenderAcc().toString().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (TransactionTable.getPayeeAcc().toString().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false; //no match
                    }
                });
            });

            SortedList<TransactionTable> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(transactionsTable.comparatorProperty());

            transactionsTable.setItems(sortedData);

        } catch (SQLException e) {
            Logger.getLogger(TransactionTable.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }


}


package application;


/**
 * @author Komal
 * @since 06-12-2024
 * @version 1.0
 * @implNote GUI interface to simulate ATM transactions
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    private Account account = new Account(1000.0); // Starting balance
    private ArrayList<Transaction> transactions = new ArrayList<>(); // List of transactions

    @Override
    public void start(Stage primaryStage) {
        // GUI Components
        Label balanceLabel = new Label("Balance: " + account.getBalance());
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        ToggleGroup transactionType = new ToggleGroup();
        RadioButton depositRadio = new RadioButton("Deposit");
        RadioButton withdrawRadio = new RadioButton("Withdraw");
        depositRadio.setToggleGroup(transactionType);
        withdrawRadio.setToggleGroup(transactionType);
        depositRadio.setSelected(true);

        Label addAmount = new Label("Enter the amount");
        Button addTransactionButton = new Button("Add Transaction");
        Label executeTransaction = new Label("Click the button below to execute selected transactions.");
        Button executeButton = new Button("Execute Transactions");
        Button exitButton = new Button("Exit");

        // ListView to show the transactions
        ListView<String> transactionListView = new ListView<>();
        VBox layout = new VBox(10, balanceLabel, depositRadio, withdrawRadio, addAmount, amountField, addTransactionButton, 
                              transactionListView, executeTransaction, executeButton, exitButton);
        layout.setPadding(new javafx.geometry.Insets(10));

        // Adding transactions
        addTransactionButton.setOnAction(e -> {
            String input = amountField.getText();
            if (!input.isEmpty() && transactionType.getSelectedToggle() != null) {
                double amount = Double.parseDouble(input);
                boolean isDeposit = depositRadio.isSelected();

                // Check if it's a withdrawal and the amount is greater than balance
                if (!isDeposit && amount > account.getBalance()) {
                    showError("Insufficient funds for withdrawal.");
                    return; // Stop adding the transaction if the balance is insufficient
                }

                // Add the transaction to the list
                transactions.add(new Transaction(account, amount, isDeposit));
                amountField.clear();

                // Show the added transaction in the ListView
                String transactionTypeText = isDeposit ? "Deposit" : "Withdraw";
                transactionListView.getItems().add(transactionTypeText + ": " + amount);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transaction Added");
                alert.setHeaderText(null);
                alert.setContentText("Transaction added successfully to the list!");
                alert.showAndWait();
            } else {
                showError("Please enter an amount and select a transaction type.");
            }
        });

        // Executing transactions
        executeButton.setOnAction(e -> {
            if (!transactions.isEmpty()) {
                ExecutorService executor = Executors.newFixedThreadPool(3);
                for (Transaction transaction : transactions) {
                    executor.execute(transaction);
                }
                executor.shutdown();
                while (!executor.isTerminated()) {
                }
                transactions.clear(); // Clear transactions after execution
                transactionListView.getItems().clear(); // Clear the list view after execution
                updateBalance(balanceLabel);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transactions Executed");
                alert.setHeaderText(null);
                alert.setContentText("All transactions executed successfully!");
                alert.showAndWait();
            } else {
                showError("No transactions to execute!");
            }
        });

        // Exit button functionality
        exitButton.setOnAction(e -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
            exitAlert.setTitle("Exit");
            exitAlert.setHeaderText(null);
            exitAlert.setContentText("Are you sure you want to exit?");
            exitAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    primaryStage.close();
                }
            });
        });

        // Scene and Stage
        Scene scene = new Scene(layout, 300, 350);
        primaryStage.setTitle("ATM Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateBalance(Label balanceLabel) {
        // Update the balance label
        balanceLabel.setText("Balance: " + account.getBalance());
    }

    private void showError(String message) {
        // Display error messages
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Test the AccountTest class
        AccountTest test = new AccountTest();
        test.testTransactions();

        // Launch the GUI
        launch(args);
    }
}

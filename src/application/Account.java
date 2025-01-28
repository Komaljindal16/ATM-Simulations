package application;


/**
 * @author Komal
 * @since 06-12-2024
 * @version 1.0
 * @implNote Account class to manage deposit and withdrawal actions
 */


public class Account {
    private double balance;

    public Account(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.balance = initialBalance;
    }

    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        System.out.printf("Deposited: %.2f, New Balance: %.2f%n", amount, balance);
    }

    public synchronized void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            System.out.printf("Insufficient funds for withdrawal of %.2f. Current Balance: %.2f%n", amount, balance);
            return;
        }
        balance -= amount;
        System.out.printf("Withdrew: %.2f, New Balance: %.2f%n", amount, balance);
    }

    public synchronized double getBalance() {
        return balance;
    }
}

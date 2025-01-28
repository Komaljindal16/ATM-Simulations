package application;

/**
 * @author Komal
 * @since 06-12-2024
 * @version 1.0
 * @implNote Account test for executor service 
 */

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AccountTest {
    public void testTransactions() {
        Account account = new Account(1000.0); // Initial balance
        ArrayList<Transaction> transactions = new ArrayList<>();

  

        // Executing transactions using ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (Transaction transaction : transactions) {
            executor.execute(transaction);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        System.out.printf("Final Balance: %.2f%n", account.getBalance());
    }
}

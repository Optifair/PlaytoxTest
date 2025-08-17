package com.example;

import com.example.model.Account;
import com.example.service.TransferThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final int INITIAL_BALANCE = 10000;
    private static final int ACCOUNT_COUNT = 4;
    private static final int THREAD_COUNT = 4;

    public static List<Account> createAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            String id = "ACC-" + UUID.randomUUID().toString().substring(0, 4);
            accounts.add(new Account(id, INITIAL_BALANCE));
        }
        return accounts;
    }

    public static void main(String[] args) {

        try {
            Files.createDirectories(Paths.get("logs"));
            logger.info("Logs directory created/accessed");
        } catch (Exception e) {
            logger.error("Failed to create logs directory", e);
        }

        List<Account> accounts = createAccounts();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(new TransferThread(accounts));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("Threads interrupted", e);
        }

        logger.info("Final account balances:");
        for (Account account : accounts) {
            logger.info("Account {}: {} units", account.getId(), account.getMoney());
        }
    }
}

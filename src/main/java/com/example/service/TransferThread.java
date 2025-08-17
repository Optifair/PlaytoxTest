package com.example.service;

import com.example.model.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Random;
import com.example.exception.InsufficientFundsException;

public class TransferThread implements Runnable {
    private final List<Account> accounts;
    private final Random random = new Random();
    private static final Logger logger = LogManager.getLogger(TransferThread.class);
    private static volatile int transactionCount = 0;

    public TransferThread(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void run() {
        while (transactionCount < 30) {
            try {
                Account from = accounts.get(random.nextInt(accounts.size()));
                Account to = accounts.get(random.nextInt(accounts.size()));
                if (from == to) continue;

                int amount = random.nextInt(10000) + 1;

                synchronized (from) {
                    synchronized (to) {
                        int fromBalanceBefore = from.getMoney();
                        int toBalanceBefore = to.getMoney();

                        from.withdraw(amount);
                        to.deposit(amount);
                        transactionCount++;

                        logger.info(
                                "Transferred {} from {} (before: {}, after: {}) to {} (before: {}, after: {})",
                                amount,
                                from.getId(), fromBalanceBefore, from.getMoney(),
                                to.getId(), toBalanceBefore, to.getMoney()
                        );
                    }
                }

                Thread.sleep(random.nextInt(1000) + 1000);
            } catch (InterruptedException | InsufficientFundsException e) {
                logger.error("Error in transfer: {}", e.getMessage());
            }
        }
    }
}

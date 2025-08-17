package com.example;

import com.example.model.Account;
import com.example.service.TransferThread;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testConcurrentTransfers() throws InterruptedException {
        List<Account> accounts = Arrays.asList(
                new Account("ACC-1", 10000),
                new Account("ACC-2", 10000)
        );
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executor.submit(new TransferThread(accounts));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        assertEquals(20000, accounts.get(0).getMoney() + accounts.get(1).getMoney());
    }

    @Test
    public void testTransferToSameAccount() {
        Account account = new Account("ACC-1", 10000);
        List<Account> accounts = Collections.singletonList(account);
        TransferThread thread = new TransferThread(accounts);
        thread.run();
        assertEquals(10000, account.getMoney());
    }
}

package com.example.service;

import com.example.exception.InsufficientFundsException;
import com.example.model.Account;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TransferThreadTest {
    @Test
    public void testSingleTransfer() throws InsufficientFundsException {
        List<Account> accounts = Arrays.asList(
                new Account("ACC-1", 10000),
                new Account("ACC-2", 10000)
        );
        TransferThread thread = new TransferThread(accounts);
        thread.run();
        assertEquals(20000, accounts.get(0).getMoney() + accounts.get(1).getMoney());
    }

    @Test
    public void testTransferWithInsufficientFunds() {
        List<Account> accounts = Arrays.asList(
                new Account("ACC-1", 10),
                new Account("ACC-2", 10000)
        );
        TransferThread thread = new TransferThread(accounts);
        thread.run();
        assertTrue(accounts.getFirst().getMoney() >= 0);
    }
}

package com.example.model;

import com.example.exception.InsufficientFundsException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {
    @Test
    public void testDepositAndWithdraw() throws InsufficientFundsException {
        Account account = new Account("ACC-123", 1000);
        account.withdraw(500);
        account.deposit(200);
        assertEquals(700, account.getMoney());
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficientFunds() throws InsufficientFundsException {
        Account account = new Account("ACC-123", 100);
        account.withdraw(200);
    }
}

package com.example.model;

import com.example.exception.InsufficientFundsException;

public class Account {
    private final String id;
    private int money;

    public Account(String id, int money) {
        this.id = id;
        this.money = money;
    }

    public synchronized void deposit(int amount) {
        money += amount;
    }

    public synchronized void withdraw(int amount) throws InsufficientFundsException {
        if (money < amount) {
            throw new InsufficientFundsException("Not enough money on account " + id);
        }
        money -= amount;
    }

    public String getId() { return id; }
    public int getMoney() { return money; }
}
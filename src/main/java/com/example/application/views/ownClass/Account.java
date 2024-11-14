package com.example.application.views.ownClass;

public class Account {
    private double balance;

    public Account() {
        this.balance = 0;
    }

    public double getBalance() {
        return balance;
    }

    public double deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
        return balance; 
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) { 
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(double amount, String iban) {
        if (amount > 0 && amount <= this.balance && isValidIban(iban)) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    private boolean isValidIban(String iban) {
        return iban.startsWith("CH") && iban.length() == 23;
    }
}

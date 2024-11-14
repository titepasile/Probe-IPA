package com.example.application.views.ownClass;

public class Account {
    private double balance;

    public Account() {
        this.balance = 0;
    }

    public double getBalance(double balance, double amount) {
        return balance += amount;
    }

    public double deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
        return balance; 
    }

    public double withdraw(double amount) {
        if (amount > 0) {
            this.balance -= amount;
        }
        return balance;
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

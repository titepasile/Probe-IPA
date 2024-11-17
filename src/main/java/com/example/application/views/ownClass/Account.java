package com.example.application.views.ownClass;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance;

    @ManyToOne
    @JoinColumn(name = "account")
    private AppUser user;

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    public Long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(double amount, String iban) {
        if (amount <= balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}

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
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    // Konstruktoren
    public Account() {
    }

    public Account(double balance) {
        this.balance = balance;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser user) {
        this.appUser = user;
    }

    // Einzahlen
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    // Abheben
    public boolean withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    // Überweisung
    public boolean transfer(Account recipientAccount, double amount) {
        if (amount > 0 && this.balance >= amount && recipientAccount != null) {
            this.balance -= amount;
            recipientAccount.deposit(amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", user=" + (appUser != null ? appUser.getNames() : null) +
                '}';
    }
}

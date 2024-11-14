package com.example.application.views.ownClass;

import java.util.ArrayList;
import java.util.List;

public class AppUser {
    private String name;
    private List<Account> accounts;

    public AppUser(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
        this.accounts.add(new Account());  // Standardkonto erstellen
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount() {
        this.accounts.add(new Account());
    }
}

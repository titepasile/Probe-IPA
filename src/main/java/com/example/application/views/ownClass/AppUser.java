package com.example.application.views.ownClass;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.template.Id;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //From: https://stackoverflow.com/questions/54540749/how-to-update-onetomany-hibernate-relationship
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();
    //

    @Column(name = "name")
    private String name;

    @Column(name = "account")
    private String account; 

    @Column(name = "balance")
    private double balance; 

    public Long getId(){
        return id; 
    }

    public void setId(Long id){
        this.id = id; 
    }

    public String getNames(){
        return name; 
    }

    public void setNames(String name){
        this.name = name; 
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setUser(this);
        this.accounts.add(account);
    }
}

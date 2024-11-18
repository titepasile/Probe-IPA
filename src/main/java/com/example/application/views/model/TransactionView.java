package com.example.application.views.model;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;
import com.example.application.views.repositorys.AccountRepository;
import com.example.application.views.repositorys.AppUserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Optional;

@Route("/transaction")
public class TransactionView extends VerticalLayout implements HasUrlParameter<String> {

    private final AppUserRepository appUserRepository;
    private final AccountRepository accountRepository;
    private AppUser user;

    public TransactionView(AppUserRepository appUserRepository, AccountRepository accountRepository) {
        this.appUserRepository = appUserRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void setParameter(BeforeEvent event, String username) {
        Optional<AppUser> userOptional = appUserRepository.findByName(username);

        if (userOptional.isPresent()) {
            user = userOptional.get();
            createTransactionLayout();
        } else {
            add(new Span("Benutzer nicht gefunden."));
        }
    }

    private void createTransactionLayout() {
        removeAll();
        add(new H3("Transaktionen für: " + user.getNames()));

        List<Account> accounts = accountRepository.findByAppUser(user);

        if (accounts.isEmpty()) {
            add(new Span("Keine Konten für diesen Benutzer gefunden."));
        } else {
            for (Account account : accounts) {
                Button accountButton = new Button(
                        "Konto: " + account.getId() + "| " + account.getBalance() + " CHF",
                        click -> openTransactionDialog(account));
                add(accountButton);
            }
        }

        Button backButton = new Button("Zurück zur Übersicht",
                click -> UI.getCurrent().navigate("/user/" + user.getNames()));
        add(backButton);
    }

    private void openTransactionDialog(Account account) {
        Dialog dialog = new Dialog();
        NumberField amountField = new NumberField("Betrag");

        Button depositButton = new Button("Einzahlen", click -> {
            double amount = amountField.getValue();

            if (amount <= 0) {
                Notification.show("Geben Sie eine Zahl über 0 an.");
                return;
            }
            account.deposit(amount);
            accountRepository.save(account);
            Notification
                    .show("Betrag eingezahlt: " + amount + " CHF. Neuer Kontostand: " + account.getBalance() + " CHF.");
            dialog.close();

            appUserRepository.save(user);
            Notification.show("Aktueller Kontostand: " + account.getBalance() + " CHF");
            dialog.close();
        });

        Button withdrawButton = new Button("Abheben", click -> {
            double amount = amountField.getValue();

            if (amount <= 0) {
                Notification.show("Geben Sie eine Zahl über 0 an.");
                return;
            } else if (amount >= account.getBalance()) {
                Notification.show("Ihr Guthaben ist zu klein.");
                return;
            }

            account.deposit(amount);
            accountRepository.save(account);
            Notification
                    .show("Betrag eingezahlt: " + amount + " CHF. Neuer Kontostand: " + account.getBalance() + " CHF.");
            dialog.close();

            appUserRepository.save(user);
            Notification.show("Aktueller Kontostand: " + account.getBalance() + " CHF");
            dialog.close();
        });

        dialog.add(amountField, withdrawButton, depositButton);
        dialog.open();
    }
}

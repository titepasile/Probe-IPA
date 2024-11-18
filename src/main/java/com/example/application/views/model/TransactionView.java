package com.example.application.views.model;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;
import com.example.application.views.repositorys.AccountRepository;
import com.example.application.views.repositorys.AppUserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
                        "Konto: " + account.getBalance() + " CHF",
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

    VerticalLayout transactionLayout = new VerticalLayout();
    
    Button depositButton = new Button("Einzahlen", click -> {
        openDepositDialog(dialog, account);
    });

    Button withdrawButton = new Button("Abheben", click -> {
        openWithdrawDialog(dialog, account);
    });

    Button transferButton = new Button("Überweisen", click -> {
        openTransferDialog(dialog, account);
    });

    transactionLayout.add(depositButton, withdrawButton, transferButton);
    dialog.add(transactionLayout);
    dialog.open();
}

private void openDepositDialog(Dialog dialog, Account account) {
    NumberField amountField = new NumberField("Betrag");
    Button confirmButton = new Button("Bestätigen", event -> {
        double amount = amountField.getValue();

        if (amount <= 0) {
            Notification.show("Geben Sie einen grösseren Betrag als 0 an.");
            return;
        }

        account.deposit(amount);
        accountRepository.save(account);
        Notification.show("Betrag eingezahlt: " + amount + " CHF. Neuer Kontostand: " + account.getBalance() + " CHF.");
        dialog.close();
    });

    dialog.removeAll();
    dialog.add(amountField, confirmButton);
}

private void openWithdrawDialog(Dialog dialog, Account account) {
    NumberField amountField = new NumberField("Betrag");
    Button confirmButton = new Button("Bestätigen", event -> {
        double amount = amountField.getValue();

        if (amount <= 0) {
            Notification.show("Geben Sie einen grösseren Betrag als 0 an.");
            return;
        } else if (amount > account.getBalance()) {
            Notification.show("Sie haben nicht genügend Guthaben.");
            return;
        }

        account.withdraw(amount);
        accountRepository.save(account);
        Notification.show("Betrag abgehoben: " + amount + " CHF. Neuer Kontostand: " + account.getBalance() + " CHF.");
        dialog.close();
    });

    dialog.removeAll();
    dialog.add(amountField, confirmButton);
}

private void openTransferDialog(Dialog dialog, Account account) {
    ComboBox<AppUser> userDropdown = new ComboBox<>("Empfänger auswählen");
    userDropdown.setItems(appUserRepository.findAll());
    userDropdown.setItemLabelGenerator(AppUser::getNames);

    NumberField amountField = new NumberField("Betrag");
    Button confirmButton = new Button("Bestätigen", event -> {
        double amount = amountField.getValue();

        if (amount <= 0) {
            Notification.show("Geben Sie einen grösseren Betrag als 0 an.");
            return;
        } else if (amount > account.getBalance()) {
            Notification.show("Sie haben nicht genügend Guthaben.");
            return;
        }

        AppUser selectedUser = userDropdown.getValue();
        if (selectedUser == null) {
            Notification.show("Bitte wählen Sie einen Empfänger aus.");
            return;
        }

        List<Account> recipientAccounts = accountRepository.findByAppUser(selectedUser);
        if (recipientAccounts.isEmpty()) {
            Notification.show("Der Empfänger hat kein Konto.");
            return;
        }

        Account recipientAccount = recipientAccounts.get(0);

        boolean success = account.transfer(recipientAccount, amount);
        if (success) {
            accountRepository.save(account);
            accountRepository.save(recipientAccount);
            Notification.show("Überweisung erfolgreich: " + amount + " CHF an " + selectedUser.getNames());
        } else {
            Notification.show("Überweisung fehlgeschlagen.");
        }

        dialog.close();
    });

    dialog.removeAll();
    dialog.add(userDropdown, amountField, confirmButton);
    }
}

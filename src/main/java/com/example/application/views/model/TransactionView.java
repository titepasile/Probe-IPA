package com.example.application.views.model;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;
import com.example.application.views.repositorys.AppUserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route("/transaction")
public class TransactionView extends VerticalLayout implements HasUrlParameter<String> {

    private AppUserRepository userRepository;
    private Account account;
    private AppUser user;

    public TransactionView(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setParameter(BeforeEvent event, String name) {
        if (name == null || name.isEmpty()) {
            Notification.show("Kein Benutzername angegeben.");
            return;
        }

        Optional<AppUser> userOptional = userRepository.findByName(name);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getAccounts().isEmpty()) {
                createTransactionLayout();
            } else {
                Notification.show("Kein Konto für diesen Benutzer gefunden.");
            }
        } else {
            Notification.show("Benutzer nicht gefunden.");
        }
    }

    private void createTransactionLayout() {
        removeAll();

        Button withdrawButton = new Button("Geld abheben", click -> openTransactionDialog("Abheben", false));
        Button depositButton = new Button("Geld einzahlen", click -> openTransactionDialog("Einzahlen", true));
        Button overviewButton = new Button("Zurück zur Übersicht",
                click -> getUI().ifPresent(ui -> ui.navigate("/user")));

        add(withdrawButton, depositButton, overviewButton);
    }

    private void openTransactionDialog(String action, boolean isDeposit) {
        Dialog dialog = new Dialog();
        NumberField amountField = new NumberField("Betrag");

        Button confirmButton = new Button("Bestätigen", click -> {
            double amount = amountField.getValue();

            if (isDeposit) {
                account.deposit(amount);
            } else if (account.withdraw(amount)) {
                // Betrag erfolgreich abgehoben
            } else {
                Notification.show("Nicht genügend Guthaben.");
            }

            // Konto in der Datenbank speichern, um die Änderungen zu übernehmen
            userRepository.save(user);
            Notification.show("Aktueller Kontostand: " + account.getBalance() + " CHF");
            dialog.close();
        });

        dialog.add(amountField, confirmButton);
        dialog.open();
    }
}

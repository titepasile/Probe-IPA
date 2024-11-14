package com.example.application.views.list;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("transaction")
public class TransactionView extends VerticalLayout implements HasUrlParameter<String> {

    private Account account;
    private AppUser user;

    // Dummy-Benutzerliste, um Benutzer zu finden (wie in MainView)
    private List<AppUser> users = new ArrayList<>();

    public TransactionView() {
        users.add(new AppUser("Alice"));
        users.add(new AppUser("Bob"));
        users.add(new AppUser("Charlie"));
    }

    @Override
    public void setParameter(com.vaadin.flow.router.BeforeEvent event, String username) {
        Optional<AppUser> userOptional = users.stream()
                                              .filter(u -> u.getName().equals(username))
                                              .findFirst();

        if (userOptional.isPresent()) {
            user = userOptional.get();
            account = user.getAccounts().get(0);
            createTransactionLayout(); 
        } else {
            Notification.show("Benutzer nicht gefunden.");
        }
    }

    private void createTransactionLayout() {
        removeAll(); // Vorherigen Inhalt löschen, falls vorhanden

        // Buttons für Transaktionsoptionen
        Button withdrawButton = new Button("Geld abheben", event -> openTransactionDialog("Abheben", false));
        Button depositButton = new Button("Geld einzahlen", event -> openTransactionDialog("Einzahlen", true));
        Button transferButton = new Button("Geld überweisen", event -> openTransferDialog());
        Button overviewButton = new Button("Zurück zur Übersicht", event -> {
            getUI().ifPresent(ui -> ui.navigate("user/" + user.getName()));
        });
        

        add(withdrawButton, depositButton, transferButton, overviewButton);
    }

    private void openTransactionDialog(String action, boolean isDeposit) {
        Dialog dialog = new Dialog();
        NumberField amountField = new NumberField("Betrag");
        Button confirmButton = new Button("Bestätigen", event -> {
            double amount = amountField.getValue();
            /*if (isDeposit) {
                account.deposit(amount);
                Notification.show("Betrag eingezahlt.");
            } else if (amount > balance) {
                Notification.show("Nicht genügend Guthaben.");
            } else {
                Notification.show("Betrag abgehoben.");
            }
            dialog.close();*/
        });
        dialog.add(amountField, confirmButton);
        dialog.open();
    }

    private void openTransferDialog() {
        Dialog dialog = new Dialog();
        TextField ibanField = new TextField("IBAN");
        NumberField amountField = new NumberField("Betrag");
        
        Button confirmButton = new Button("Überweisen", e -> {
            String iban = ibanField.getValue();
            double amount = amountField.getValue();
            
            if (!account.transfer(amount, iban)) {
                Notification.show("Ungültige IBAN oder unzureichendes Guthaben.");
            } else {
                Notification.show("Überweisung erfolgreich.");

            }
            dialog.close();
        });
        
        dialog.add(ibanField, amountField, confirmButton);
        dialog.open();
    }
}

package com.example.application.views.model;

import java.util.List;
import java.util.Optional;

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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/user")
@PageTitle("Kontenübersicht")
public class UsersView extends VerticalLayout implements HasUrlParameter<String> {

    private final AppUserRepository appUserRepository;
    private final AccountRepository accountRepository;

    public UsersView(AppUserRepository appUserRepository, AccountRepository accountRepository) {
        this.appUserRepository = appUserRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void setParameter(BeforeEvent event, String username) {
        Optional<AppUser> userOptional = appUserRepository.findByName(username);

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            createUserAccountLayout(user);
        } else {
            add(new Span("Benutzer nicht gefunden."));
        }
    }

    private void createUserAccountLayout(AppUser user) {
        add(new H3("Benutzer: " + user.getNames()));
        //add(new H4("Gesammtguthaben: " + ""));

        List<Account> accounts = accountRepository.findByAppUser(user);

        if (accounts.isEmpty()) {
            add(new Span("Keine Konten für diesen Benutzer gefunden."));
        } else {
            for (Account account : accounts) {
                Button accountButton = new Button(account.getBalance() + " CHF",
                        event -> UI.getCurrent().navigate("/transaction/" + user.getNames()));
                add(accountButton);
            }
        }

        Button createAccountButton = new Button("Neues Konto erstellen", click -> createNewAccountForUser(user));
        add(createAccountButton);
    }

    private void createNewAccountForUser(AppUser user) {
        Dialog dialog = new Dialog();
        dialog.setWidth("300px");

        Button createButton = new Button("Konto erstellen", event -> {
            Account newAccount = new Account();
            newAccount.setUser(user);

            accountRepository.save(newAccount);
            Notification.show("Neues Konto für " + user.getNames() + " wurde erfolgreich erstellt.");

            dialog.close();

            removeAll();
            createUserAccountLayout(user);
        });

        Button closeButton = new Button("Abbrechen", event -> dialog.close());

        dialog.add(createButton, closeButton);
        dialog.open();
    }
}

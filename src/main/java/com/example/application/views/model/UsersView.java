package com.example.application.views.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;
import com.example.application.views.repositorys.AccountRepository;
import com.example.application.views.repositorys.AppUserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
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
        // Benutzer anhand des Namens laden
        Optional<AppUser> userOptional = appUserRepository.findByName(username);

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            createUserAccountLayout(user);
        } else {
            add(new Span("Benutzer nicht gefunden."));
        }
    }

    private void createUserAccountLayout(AppUser user) {
        // Benutzername anzeigen
        add(new H3("Benutzer: " + user.getNames()));

        // Konten für diesen Benutzer abrufen
        List<Account> accounts = accountRepository.findByAppUser(user);

        if (accounts.isEmpty()) {
            add(new Span("Keine Konten für diesen Benutzer gefunden."));
        } else {
            for (Account account : accounts) {
                // Button für jedes Konto erstellen
                Button accountButton = new Button(account.getBalance() + " CHF", event -> 
                    UI.getCurrent().navigate("transaction/" + user.getNames())
                );
                add(accountButton);
            }
        }
    }
}

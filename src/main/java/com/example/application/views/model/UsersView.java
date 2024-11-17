package com.example.application.views.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;
import com.example.application.views.repositorys.AppUserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route("/user")
public class UsersView extends VerticalLayout implements HasUrlParameter<String> {

    private AppUserRepository userRepository;
    private AppUser user;

    
    @Autowired
    public void UserView(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setParameter(BeforeEvent event, String username) {
        Optional<AppUser> userOptional = userRepository.findByName(username);
        if (userOptional.isPresent()) {
            this.user = userOptional.get();
            createUserLayout();
        } else {
            Notification.show("Benutzer nicht gefunden.");
        }
    }

    private void createUserLayout() {
        removeAll();

        for (Account account : user.getAccounts()) {
            Button accountButton = new Button("Konto: " + account.getBalance() + " CHF", click -> {
                getUI().ifPresent(ui -> ui.navigate("transaction/" + user.getNames()));
            });
            add(accountButton);
        }
    }
}

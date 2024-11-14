package com.example.application.views.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route("/user")
public class UsersView extends VerticalLayout implements HasUrlParameter<String> {

    private List<AppUser> users;

    public UsersView() {
        users = new ArrayList<>();
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
            AppUser user = userOptional.get();
            setUserLayout(user);
        } 
    }

    private void setUserLayout(AppUser user) {
        removeAll(); // vorherigen Inhalt löschen, falls vorhanden
        for (Account account : user.getAccounts()) {
            HorizontalLayout accountLayout = new HorizontalLayout();
            
            Button accountButton = new Button("Konto anzeigen", event -> 
                getUI().ifPresent(ui -> ui.navigate("transaction/" + user.getName()))
            );
            Button balanceButton = new Button("Guthaben: " + account.getBalance() + " CHF");

            accountLayout.add(accountButton, balanceButton);
            add(accountLayout);
        }

        Button addAccountButton = new Button("+ Konto hinzufügen", event -> {
            user.addAccount();
            Notification.show("Neues Konto hinzugefügt.");
            setUserLayout(user);  // Layout neu laden, um das neue Konto anzuzeigen
        });
        add(addAccountButton);
    }
}

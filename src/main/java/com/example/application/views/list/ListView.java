package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.example.application.views.ownClass.AppUser;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class ListView extends VerticalLayout {

    private List<AppUser> users = new ArrayList<>();

    public ListView() {
        // Benutzerliste mit Beispieldaten initialisieren
        users.add(new AppUser("Alice"));
        users.add(new AppUser("Bob"));
        users.add(new AppUser("Charlie"));

        // Benutzer-Buttons erstellen
        for (AppUser user : users) {
            Button userButton = new Button(user.getName(), event -> getUI().ifPresent(ui -> ui.navigate("user/" + user.getName())));
            add(userButton);
        }
    }
}

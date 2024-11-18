package com.example.application.views.model;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.example.application.views.ownClass.AppUser;
import com.example.application.views.repositorys.AppUserRepository;

import java.util.List;

@Route("")
@PageTitle("Benutzerübersicht")
public class MainView extends VerticalLayout {

    public MainView(AppUserRepository appUserRepository) {
        add(new H1("Benutzerübersicht"));

        List<AppUser> users = appUserRepository.findAll();

        if (users.isEmpty()) {
            add(new Span("Keine Benutzer vorhanden."));
        } else {
            for (AppUser user : users) {
                Button userButton = new Button(user.getNames(), event -> {
                    UI.getCurrent().navigate("/user/" + user.getNames());
                });
                add(userButton);
            }
        }
    }
}

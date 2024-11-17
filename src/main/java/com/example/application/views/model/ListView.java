package com.example.application.views.model;

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
        for (AppUser user : users) {
            Button userButton = new Button(user.getNames(), event -> getUI().ifPresent(ui -> ui.navigate("user/" + user.getNames())));
            add(userButton);
        }
    }
}

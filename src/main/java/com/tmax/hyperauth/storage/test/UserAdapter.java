package com.tmax.hyperauth.storage.test;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

public class UserAdapter extends AbstractUserAdapterFederatedStorage {

    private final User user;
    private final String keycloakId;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, User user) {
        super(session, realm, model);
        this.user = user;
        this.keycloakId = StorageId.keycloakId(model, user.getId());
    }

    @Override
    public String getId() {
        System.out.println("UserAdapter -  getId keycloakId : " + keycloakId);
        return keycloakId;
    }

    @Override
    public String getUsername() {
        System.out.println("UserAdapter -  getUsername  : " + user.getUsername());

        return user.getUsername();
    }

    @Override
    public void setUsername(String username) {
        System.out.println("UserAdapter -  setUsername  : " + username);
        user.setUsername(username);
    }

    @Override
    public String getEmail() {
        System.out.println("UserAdapter -  getEmail  : " + user.getEmail());

        return user.getEmail();
    }

    @Override
    public void setEmail(String email) {
        System.out.println("UserAdapter -  setEmail  : " + email);
        user.setEmail(email);
    }
}

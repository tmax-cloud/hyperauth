package com.tmax.hyperauth.storage.test;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

@Slf4j
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
        log.debug("UserAdapter -  getId keycloakId : " + keycloakId);
        return keycloakId;
    }

    @Override
    public String getUsername() {
        log.debug("UserAdapter -  getUsername  : " + user.getEmail());
        return user.getEmail();
    }

    @Override
    public void setUsername(String email) {
        log.debug("UserAdapter -  setUsername  : " + email);
        user.setUsername(email);
    }

    @Override
    public String getEmail() {
        log.debug("UserAdapter -  getEmail  : " + user.getEmail());
        return user.getEmail();
    }

    @Override
    public void setEmail(String email) {
        log.debug("UserAdapter -  setEmail  : " + email);
        user.setEmail(email);
    }
}

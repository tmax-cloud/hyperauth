package com.tmax.hyperauth.storage.test;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TestUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputUpdater, CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final UserRepository repository;

    public TestUserStorageProvider(KeycloakSession session, ComponentModel model, UserRepository repository) {
        this.session = session;
        this.model = model;
        this.repository = repository;
    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {
        log.debug("pre-remove group");
    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {
        log.debug("pre-remove role");
    }

    @Override
    public void close() {
        log.debug("closing");
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        log.debug("lookup user by id: realm=" + realm.getId() + ", userId=" + id  );
        String externalId = StorageId.externalId(id);
        return new UserAdapter(session, realm, model, repository.findUserById(externalId));
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        log.debug("lookup user by Username: realm=" + realm.getId() + ", username=" + username  );
        return new UserAdapter(session, realm, model, repository.findUserByUsernameOrEmail(username));
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        log.debug("lookup user by Email: realm=" + realm.getId() + ", email=" + email  );
        return getUserByUsername(email, realm);
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        log.debug("getUsersCount(RealmModel realm)"  );
        return repository.getUsersCount();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        log.debug("list users: realm=" + realm.getId() );
        return repository.getAllUsers().stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        log.debug("list users: realm=" + realm.getId() + " firstResult=" + firstResult + " maxResults="+  maxResults);
        return getUsers(realm);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        log.debug("searchForUser(String search, RealmModel realm)");
        return repository.findUsers(search).stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        log.debug("searchForUser(String search, RealmModel realm, int firstResult, int maxResults)");
        return searchForUser(search, realm);
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        log.debug("searchForUser(Map<String, String> params, RealmModel realm)");
        return null;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        log.debug("searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults)  firstResult : " + firstResult + ", maxResult : " + maxResults);
        return repository.getAllUsers(firstResult,maxResults).stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        log.debug("getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults)");

        return Collections.emptyList();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        log.debug("getGroupMembers(RealmModel realm, GroupModel group)");
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        log.debug("searchForUserByUserAttribute");

        return null;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        log.debug("supportsCredentialType");

        return CredentialModel.PASSWORD.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        log.debug("isConfiguredFor");
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        log.debug("isValid user credential: userId= " +  user.getId());
        log.debug("isValid user credential: userEmail first = " +  user.getEmail());

        //TODO : user에 id 만 채워 져있는 상황인듯
        user = getUserById ( user.getId(), realm);

        log.debug("isValid user credential: userEmail second = " +  user.getEmail());

        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }

        UserCredentialModel cred = (UserCredentialModel) input;
        return repository.validateCredentials(user.getEmail(), cred.getChallengeResponse()); //FIXME : 로그인 할때 쓰는거일듯?
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        log.debug("updating credential: realm=" + realm.getId()+ ", user=" + user.getUsername() );

        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }

        UserCredentialModel cred = (UserCredentialModel) input;
        return repository.updateCredentials(user.getUsername(), cred.getChallengeResponse());    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        log.debug("disableCredentialType(RealmModel realm, UserModel user, String credentialType)" );
    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        log.debug("getDisableableCredentialTypes(RealmModel realm, UserModel user)" );
        return Collections.emptySet();
    }
}

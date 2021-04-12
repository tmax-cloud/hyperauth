package com.tmax.hyperauth.storage.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserRepository {

    private List<User> users;

    public UserRepository() {
        users = Arrays.asList(
                new User("1", "wootmax","taegeon_woo@tmax.co.kr",  "admin"),
                new User("2", "woonaver","dnxorjs1@naver.co.kr",  "admin"),
                new User("3", "woonate","dnxorjs1@nate.co.kr",  "admin"),
                new User("4", "song","song@tmax.co.kr",  "admin")
        );
    }

    public List<User> getAllUsers() {
        log.debug("getAllUsers()");
        return users;
    }

    public List<User> getAllUsers(int start, int max) {
        log.debug("getAllUsers(int start, int max)");
        if ( users.size() >= start + max) {
            return users.subList(start, start + max);
        } else{
            return users;
        }
    }

    public int getUsersCount() {
        log.debug("getUsersCount() : " + users.size());
        return users.size();
    }

    public User findUserById(String id) {
        log.debug("findUserById(String id) :" + id);
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().get();
    }

    public User findUserByUsernameOrEmail(String username) {
        log.debug("findUserByUsernameOrEmail(String username) : " + username);
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username) || user.getEmail().equalsIgnoreCase(username))
                .findFirst().get();
    }

    public User findUserByEmail(String email) {
        log.debug("findUserByEmail Email : " + email);
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst().get();
    }

    public List<User> findUsers(String query) {
        log.debug("findUsers(String query) : " + query);

        return users.stream()
                .filter(user -> user.getUsername().contains(query) || user.getEmail().contains(query))
                .collect(Collectors.toList());
    }

    public boolean validateCredentials(String email, String password) {
        log.debug("validateCredentials Email : " + email);
        log.debug("validateCredentials password : " + password);
        log.debug("findUserByEmail(email).getPassword() : " + findUserByEmail(email).getPassword());
        return findUserByEmail(email).getPassword().equals(password);
    }

    public boolean updateCredentials(String email, String password) {
        log.debug("updateCredentials(String email, String password) email :  " + email + "   password : " + password);
        findUserByEmail(email).setPassword(password);
        return true;
    }
}

package com.tmax.hyperauth.storage.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return users;
    }

    public int getUsersCount() {
        return users.size();
    }

    public User findUserById(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().get();
    }

    public User findUserByUsernameOrEmail(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username) || user.getEmail().equalsIgnoreCase(username))
                .findFirst().get();
    }

    public User findUserByEmail(String email) {
        System.out.println("findUserByEmail Email : " + email);

        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst().get();
    }

    public List<User> findUsers(String query) {
        return users.stream()
                .filter(user -> user.getUsername().contains(query) || user.getEmail().contains(query))
                .collect(Collectors.toList());
    }

    public boolean validateCredentials(String email, String password) {
        System.out.println("validateCredentials Email : " + email);
        System.out.println("validateCredentials password : " + password);
        System.out.println("findUserByEmail(email).getPassword() : " + findUserByEmail(email).getPassword());
        return findUserByEmail(email).getPassword().equals(password);
    }

    public boolean updateCredentials(String email, String password) {
        findUserByEmail(email).setPassword(password);
        return true;
    }
}

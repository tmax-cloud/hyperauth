package com.tmax.hyperauth.timer;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.timer.TimerProvider;
import org.keycloak.timer.TimerProviderFactory;
import org.keycloak.timer.basic.TimerTaskContextImpl;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author taegeon_woo@tmax.co.kr
 */
public class UserDeleteTimerFactory implements TimerProviderFactory {
    private Timer timer;
    private ConcurrentMap<String, TimerTaskContextImpl> scheduledTasks = new ConcurrentHashMap<>();


    @Override
    public TimerProvider create(KeycloakSession session) {
        return new UserDeleteTimer(session, timer, this);
    }

    @Override
    public void init(Config.Scope config) {
        timer = new Timer();
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {
        timer.cancel();
        timer = null;
    }

    @Override
    public String getId() {
        return "userDeleteTimer";
    }

    protected TimerTaskContextImpl putTask(String taskName, TimerTaskContextImpl task) {
        return scheduledTasks.put(taskName, task);
    }

    protected TimerTaskContextImpl removeTask(String taskName) {
        return scheduledTasks.remove(taskName);
    }
}

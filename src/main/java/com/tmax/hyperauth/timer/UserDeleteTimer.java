package com.tmax.hyperauth.timer;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.timer.ScheduledTask;
import org.keycloak.timer.TimerProvider;

import java.util.Timer;
import java.util.TimerTask;

public class UserDeleteTimer implements TimerProvider {
    private static final Logger logger = Logger.getLogger(UserDeleteTimer.class);

    private final KeycloakSession session;
    private final Timer timer;
    private final UserDeleteTimerFactory factory;

    public UserDeleteTimer(KeycloakSession session, Timer timer, UserDeleteTimerFactory factory) {
        this.session = session;
        this.timer = timer;
        this.factory = factory;
    }

    @Override
    public void schedule(Runnable runnable, long intervalMillis, String taskName) {
        //TODO : 각 Client에게 noti?

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(" Timer Wake Up!!!!!!");
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @Override
    public void scheduleTask(ScheduledTask scheduledTask, long intervalMillis, String taskName) {
//        ScheduledTaskRunner scheduledTaskRunner = new ScheduledTaskRunner(session.getKeycloakSessionFactory(), scheduledTask);
//        this.schedule(scheduledTaskRunner, intervalMillis, taskName);
    }

    @Override
    public TimerTaskContext cancelTask(String taskName) {
        return null;
    }

    @Override
    public void close() {

    }
}

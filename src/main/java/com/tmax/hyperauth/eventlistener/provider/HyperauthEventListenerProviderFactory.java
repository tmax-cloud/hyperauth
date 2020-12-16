package com.tmax.hyperauth.eventlistener.provider;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class HyperauthEventListenerProviderFactory implements EventListenerProviderFactory {

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new HyperauthEventListenerProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        try {
            JobDataMap data = new JobDataMap();
            data.put("session", keycloakSessionFactory.create());

            JobDetail job = JobBuilder.newJob( UserDeleteJob.class ).usingJobData(data)
                    .withIdentity( "UserDeleteJob" ).build();

            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity( "UserDeleteCronTrigger" )
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule( "*/10 * * * * ?" ))
                    .build();

            Scheduler sch = new StdSchedulerFactory().getScheduler();
            sch.start();
            sch.scheduleJob( job, cronTrigger );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "hyperauth_event_listener";
    }


}
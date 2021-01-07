package com.tmax.hyperauth.eventlistener.provider;

import com.tmax.hyperauth.eventlistener.queue.EventsConsumer;
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
    private EventsConsumer consumer;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new HyperauthEventListenerProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {
        System.out.println("Setting up Event provider factory");
        this.consumer = new EventsConsumer(new JMSSender(
                scope.get(ProviderConstants.JMS_CONNECTION_FACTORY),
                scope.get(ProviderConstants.JMS_EVENT_TOPIC),
                scope.get(ProviderConstants.JMS_ADMIN_EVENT_TOPIC)
        ));
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        try {
            JobDetail job = JobBuilder.newJob( UserDeleteJob.class )
                    .withIdentity( "UserDeleteJob" ).build();
            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity( "UserDeleteCronTrigger" )
                    .withSchedule( CronScheduleBuilder.cronSchedule( "0 0 0 ? * * *" )) // EveryDay at 0am
//                    .withSchedule(CronScheduleBuilder.cronSchedule( "*/30 * * * * ?" )) // For test every 30 sec
                    .build();

            Scheduler sch = new StdSchedulerFactory().getScheduler();
            sch.start();
            sch.scheduleJob( job, cronTrigger );

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.consumer.init(); // TODO
    }

    @Override
    public void close() {
        this.consumer.shutdown();
    }

    @Override
    public String getId() {
        return "hyperauth_event_listener";
    }

}
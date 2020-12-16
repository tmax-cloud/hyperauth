package com.tmax.hyperauth.eventlistener.provider;

import org.keycloak.models.KeycloakSession;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UserDeleteJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        KeycloakSession session = (KeycloakSession) context.getJobDetail().getJobDataMap().get("session");
        System.out.println();
        System.out.println(" Timer Wake Up !!!!!");
        System.out.println(session.users().getUserByEmail("admin@tmax.co.kr", session.realms().getRealmByName("tmax")).getUsername());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }
}

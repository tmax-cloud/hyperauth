package com.tmax.hyperauth.eventlistener.provider;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class UserDeleteJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(" Timer Wake Up !!!!!");
        KeycloakSession session = (KeycloakSession) context.getJobDetail().getJobDataMap().get("session");
        if (!session.getTransactionManager().isActive()) {
            session.getTransactionManager().begin();
        }
        List<UserModel> users = session.users().getUsers(session.realms().getRealmByName("tmax"),false);
        for( UserModel user : users) {
            System.out.println( user.getEmail());
        }
    }
}

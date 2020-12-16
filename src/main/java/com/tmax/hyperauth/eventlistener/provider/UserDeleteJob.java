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

        KeycloakSession session = (KeycloakSession) context.getJobDetail().getJobDataMap().get("session");
        session.getTransactionManager().begin();
        System.out.println();
        System.out.println(" Timer Wake Up !!!!!");
        List<UserModel> users = session.users().getUsers(session.realms().getRealmByName("tmax"),false);
        for( UserModel user : users) {
            System.out.println( user.getEmail());
        }

        session.getTransactionManager().commit();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }
}

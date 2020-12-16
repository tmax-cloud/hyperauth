package com.tmax.hyperauth.eventlistener.provider;

import com.tmax.hyperauth.caller.HypercloudOperatorCaller;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserDeleteJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(" [UserDelete Job] User Deletion Job Start !! ");
        Date currentDate = new Date();
        System.out.println( "Now : " + currentDate);
        KeycloakSession session = (KeycloakSession) context.getJobDetail().getJobDataMap().get("session");
        if (session != null) {
            if (!session.getTransactionManager().isActive()) {
                session.getTransactionManager().begin();
            }
            RealmModel realm = session.realms().getRealmByName("tmax");
            List<UserModel> users = session.users().getUsers(realm,false);
            for( UserModel user : users) {
                try {
                    if ( user.getAttributes() != null && user.getAttributes().get("DeletionDate") != null){
                        System.out.println( " user.getAttributes().get(\"DeletionDate\") : " + user.getAttributes().get("DeletionDate").toString());
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date deletionDate = transFormat.parse(user.getAttributes().get("DeletionDate").toString()
                            .replace("[", "").replace("]", ""));

                        if ( currentDate.after(deletionDate)){
                            System.out.println(" [UserDelete Job] User [ " + user.getUsername() + " ] Delete Start ");
                            ClientConnection clientConnection = session.getContext().getConnection();
                            EventBuilder event = new EventBuilder(realm, session, clientConnection).detail("username", user.getUsername()); // FIXME
                            session.users().removeUser(realm, user);
                            event.event(EventType.REMOVE_FEDERATED_IDENTITY).user(user).realm("tmax").success();
                            System.out.println("Delete user role in k8s");
                            HypercloudOperatorCaller.deleteNewUserRole(user.getUsername());
                            System.out.println(" [UserDelete Job] User [ " + user.getUsername() + " ] Delete Success ");
                        }
                    }
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println(" [UserDelete Job] Keycloak Session Not Ready Yet ");
        }
        System.out.println(" [UserDelete Job] User Deletion Job Finish !! ");
    }
}

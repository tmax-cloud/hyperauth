package com.tmax.hyperauth.eventlistener.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tmax.hyperauth.caller.HyperAuthCaller;
import com.tmax.hyperauth.caller.HypercloudOperatorCaller;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.account.UserRepresentation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserDeleteJob implements Job {
    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(" [UserDelete Job] User Deletion Job Start !! ");
        Date currentDate = new Date();
        System.out.println( "Now : " + currentDate);
        JsonArray users = null;
        String accessToken = null;
        try{
            accessToken = HyperAuthCaller.loginAsAdmin();
            users = HyperAuthCaller.getUserList(accessToken);
        }catch( Exception e){
            System.out.println(" [UserDelete Job] HyperAuth Not Ready yet ");
        }
        if ( users != null) {
            for( JsonElement user : users) {
                Gson gson = new Gson();
                UserRepresentation UserRepresentation = gson.fromJson(user, UserRepresentation.class);
                try {
                    if ( UserRepresentation.getAttributes() != null && UserRepresentation.getAttributes().get("DeletionDate") != null){
                        System.out.println( " user.getAttributes().get(\"DeletionDate\") : " + UserRepresentation.getAttributes().get("DeletionDate").toString());
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date deletionDate = transFormat.parse(UserRepresentation.getAttributes().get("DeletionDate").toString()
                                .replace("[", "").replace("]", ""));

                        if ( currentDate.after(deletionDate)){
                            System.out.println(" [UserDelete Job] User [ " + UserRepresentation.getUsername() + " ] Delete Start ");
                            HyperAuthCaller.deleteUser(UserRepresentation.getId(), accessToken);
//                        System.out.println("Delete user role in k8s");
//                        HypercloudOperatorCaller.deleteNewUserRole(userModel.getUsername());
                            System.out.println(" [UserDelete Job] User [ " + UserRepresentation.getUsername() + " ] Delete Success ");
                        }
                    }
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(" [UserDelete Job] User Deletion Job Finish !! ");
    }
}

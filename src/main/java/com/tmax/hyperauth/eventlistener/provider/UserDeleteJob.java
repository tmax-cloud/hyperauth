package com.tmax.hyperauth.eventlistener.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.tmax.hyperauth.caller.Constants;
import com.tmax.hyperauth.caller.HyperAuthCaller;

import com.tmax.hyperauth.rest.Util;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.account.UserRepresentation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDeleteJob implements Job {
    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
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
                UserRepresentation userRepresentation = gson.fromJson(user, UserRepresentation.class);
                try {
                    if ( userRepresentation.getAttributes() != null && userRepresentation.getAttributes().get("deletionDate") != null){
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date deletionDate = transFormat.parse(userRepresentation.getAttributes().get("deletionDate").toString()
                                .replace("[", "").replace("]", ""));

                        if ( currentDate.after(deletionDate)
                                && user.getAsJsonObject().get("enabled").toString().replace("\"","").equalsIgnoreCase("false")){
                            System.out.println(" [UserDelete Job] User [ " + userRepresentation.getUsername() + " ] Delete Start ");
                            HyperAuthCaller.deleteUser(userRepresentation.getId(), accessToken);
                            String email = userRepresentation.getEmail();
                            String subject = "[Tmax 통합서비스] 고객님의 계정 탈퇴가 완료되었습니다.";
                            String msg = Constants.ACCOUNT_WITHDRAWAL_APPROVAL_BODY;
                            Util.sendMail(null, email, subject, msg, null, null );
                            System.out.println(" [UserDelete Job] User [ " + userRepresentation.getUsername() + " ] Delete Success ");
                        }
                    }
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        System.out.println(" [UserDelete Job] User Deletion Job Finish !! ");
    }
}

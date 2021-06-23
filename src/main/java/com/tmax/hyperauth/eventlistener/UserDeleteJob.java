package com.tmax.hyperauth.eventlistener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.caller.HyperAuthCaller;

import com.tmax.hyperauth.eventlistener.kafka.producer.KafkaProducer;
import com.tmax.hyperauth.rest.Util;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.representations.account.UserRepresentation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class UserDeleteJob implements Job {
    public static final int USER_COUNT_UNIT = 1000;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
        KeycloakSessionFactory keycloakSessionFactory = (KeycloakSessionFactory) context.getJobDetail().getJobDataMap().get("keycloakSessionFactory");
        KeycloakSession session = keycloakSessionFactory.create();

        // CronJob 선언부는 HyperauthEventListenerPropviderFactory PostInit()에 존재
        log.info(" [UserDelete Job] User Deletion Job Start !! ");
        Date currentDate = new Date();
        log.info( "Now : " + currentDate);
        JsonArray users = new JsonArray();
        String accessToken = null;
        try{
            // Get Admin AccessToken
            accessToken = HyperAuthCaller.loginAsAdmin();

            // Get User Count
            int userCount = HyperAuthCaller.getUserCount(accessToken);
            log.info(" [UserDelete Job] User Count : " + userCount);

            // Get UserList per 1000 Person for preventing memory problem
            for (int i = 0; i < (userCount / USER_COUNT_UNIT) + 1; i++){
                int first = 0;
                if (i != 0) first = i * USER_COUNT_UNIT;
                JsonArray userListGet = HyperAuthCaller.getUserList(accessToken,first, USER_COUNT_UNIT);
                users.addAll(userListGet);
            }
        }catch( Exception e){
            log.error(" [UserDelete Job] HyperAuth Not Ready yet ");
            log.error("Error Occurs!!", e);
        }

        if ( users != null) {
            for( JsonElement user : users) {
                Gson gson = new Gson();
                UserRepresentation userRepresentation = gson.fromJson(user, UserRepresentation.class);
                try {
                    if ( userRepresentation.getAttributes() != null && userRepresentation.getAttributes().get("deletionDate") != null){
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date deletionDate = transFormat.parse(userRepresentation.getAttributes().get(AuthenticatorConstants.USER_ATTR_DELETION_DATE).get(0)); // FIXME

                        if ( currentDate.after(deletionDate)){
                            log.info(" [UserDelete Job] User [ " + userRepresentation.getUsername() + " ] Delete Start ");
                            HyperAuthCaller.deleteUser(userRepresentation.getId(), accessToken);

                            // Mail Send
                            String email = userRepresentation.getEmail();
                            String subject = "[Tmax 통합계정] 고객님의 계정 탈퇴가 완료되었습니다.";
                            String body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/tmax/email/html/etc/account-withdrawal-completed.html");

                            String emailTheme = session.realms().getRealmByName(session.getContext().getRealm().getName()).getEmailTheme();
                            if(!emailTheme.equalsIgnoreCase("tmax") && !emailTheme.equalsIgnoreCase("base") && !emailTheme.equalsIgnoreCase("keycloak")) {
                                subject = "[" + emailTheme + "] 고객님의 계정 탈퇴가 완료되었습니다.";
                                body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/" + emailTheme + "/email/html/etc/account-withdrawal-completed.html");
                            }

                            Util.sendMail(session, email, subject, body, null, session.getContext().getRealm().getId() );

                            // Topic Event Publish
                            TopicEvent topicEvent = TopicEvent.makeOtherTopicEvent("USER_DELETE", userRepresentation.getUsername(), currentDate.getTime() );
                            KafkaProducer.publishEvent("tmax", topicEvent);

                            log.info(" [UserDelete Job] User [ " + userRepresentation.getUsername() + " ] Delete Success ");
                        }
                    }
                } catch (ParseException | IOException e) {
                    log.error("Error Occurs!!", e);
                } catch (Throwable throwable) {
                    log.error("Error Occurs!!", throwable);
                }
            }
        }
        log.info(" [UserDelete Job] User Deletion Job Finish !! ");
    }
}

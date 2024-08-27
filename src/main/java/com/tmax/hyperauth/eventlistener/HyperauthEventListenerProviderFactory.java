package com.tmax.hyperauth.eventlistener;


import com.tmax.hyperauth.caller.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            log.info("Register UserDeleteJob Cronjob");
            JobDataMap data = new JobDataMap();
            data.put("keycloakSessionFactory", keycloakSessionFactory);

            boolean isDeleteSched = Boolean.parseBoolean(StringUtil.isEmpty(System.getenv("USER_DELETE_SCHEDULER")) ? "false" : System.getenv("USER_DELETE_SCHEDULER"));
            if(isDeleteSched){
                // CRON_TEST 환경변수가 true이면 30초마다 테스트 실행 아니면 매일 0시에 실행
                boolean isCronTest = Boolean.parseBoolean(System.getenv("DELETE_USER_TEST") == null ? "false" : System.getenv("DELETE_USER_TEST"));
                JobDetail job = JobBuilder.newJob( UserDeleteJob.class )
                    .withIdentity( "UserDeleteJob" ).usingJobData(data).build();
                CronTrigger cronTrigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity( "UserDeleteCronTrigger" )
                        .withSchedule( CronScheduleBuilder.cronSchedule( !isCronTest ? "0 0 0 ? * * *" : "*/30 * * * * ?"))
                        .build();

                Scheduler sch = new StdSchedulerFactory().getScheduler();
                sch.start();
                sch.scheduleJob( job, cronTrigger );
            }

        } catch (Exception e) {
            log.error("Error Occurs!!", e);
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

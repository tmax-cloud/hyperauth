package com.tmax.hyperauth.eventlistener.consumer;

import com.google.gson.Gson;
import com.tmax.hyperauth.eventlistener.provider.TopicEvent;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class EventConsumer {
    private static final Logger logger = Logger.getLogger(EventConsumer.class);
    private static final String TOPIC_NAME = "tmax";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1.hyperauth:9092,kafka-2.hyperauth:9092,kafka-3.hyperauth:9092");
        properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "hypercloud");   // TODO: Change group id to your Hyperauth client Name
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); //중복처리방지, 성능향상을 위해서는 true로 바꿔준다.

        /**
         * TODO : Keystore, Truststore를 가져오기 위해서는 Keystore, Truststore로 만든 secret을 mount (특정 path)해서 가져온다.
         * < kafka-consumer (hypercloud) >
         * hypercloud 부분을 각자 상황에 맞는 제품명으로 바꿔서 사용
         * hypercloud4-system Namespace도 바꿔서 사용
         *
         * keytool -keystore hypercloud.truststore.jks -alias ca-cert -import -file /etc/kubernetes/pki/hypercloud-root-ca.crt -storepass tmax@23 -noprompt
         * keytool -keystore hypercloud.keystore.jks -alias hypercloud -validity 3650 -genkey -keyalg RSA -ext SAN=dns:hypercloud4-operator-service.hypercloud4-system -dname "CN=hypercloud4-operator-service.hypercloud4-system" -storepass tmax@23 -keypass tmax@23
         * keytool -keystore hypercloud.keystore.jks -alias hypercloud -certreq -file ca-request-hypercloud -storepass tmax@23
         * openssl x509 -req -CA /etc/kubernetes/pki/hypercloud-root-ca.crt -CAkey /etc/kubernetes/pki/hypercloud-root-ca.key -in ca-request-hypercloud -out ca-signed-hypercloud -days 3650 -CAcreateserial
         * keytool -keystore hypercloud.keystore.jks -alias ca-cert -import -file /etc/kubernetes/pki/hypercloud-root-ca.crt -storepass tmax@23 -noprompt
         * keytool -keystore hypercloud.keystore.jks -alias hypercloud -import -file ca-signed-hypercloud -storepass tmax@23 -noprompt
         *
         * kubectl create secret generic hypercloud-kafka-jks --from-file=./hypercloud.keystore.jks --from-file=./hypercloud.truststore.jks -n hypercloud4-system
         *
         * TODO : Password를 변수처리 하기 위해서는 Secret을 생성하고 Deployment Env의 ValueFrom secret을 이용한다.
         *
         *  ex) apiVersion: v1
         *      kind: Secret
         *      metadata:
         *        name: passwords
         *        namespace: hyperauth
         *      type: Opaque
         *      data:
         *        CERTS_PASSWORD: dG1heEAyMw==
         *
         *  ex)
         *      env:
         *      - name: CERTS_PASSWORD
         *        valueFrom:
         *          secretKeyRef:
         *            name: passwords
         *            key: CERTS_PASSWORD
         */
        // for SSL
        properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/etc/x509/kafka/hyperauth.truststore.jks");
        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));
        properties.setProperty(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/x509/kafka/hyperauth.keystore.jks");
        properties.setProperty(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));
        properties.setProperty(SslConfigs.SSL_KEY_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));

        // Subscribe to Tmax topic
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                String s = record.topic();
                if (TOPIC_NAME.equals(s)) {
                    logger.info("[[ MESSAGE FROM TMAX TOPIC ]]");
                    logger.info(record.value());
                    try {
                            Gson gson = new Gson();
                        TopicEvent topicEvent = gson.fromJson(record.value(), TopicEvent.class);
                        switch (topicEvent.getType()){
                            case "LOGIN":
                                logger.info("User [ " + topicEvent.getUserName() + " ] Login !!");
                                break;
                            case "LOGOUT":
                                logger.info("User [ " + topicEvent.getUserName() + " ] Logout !!");
                                break;
                            case "LOGIN_FAILED":
                                logger.info("User [ " + topicEvent.getUserName() + " ] Login failed due to " + topicEvent.getError());
                                break;
                            case "USER_DELETE":
                                logger.info("User [ " + topicEvent.getUserName() + " ] Deleted !!");
                                break;
                            case "USER_WITHDRAWAL":
                                logger.info("User [ " + topicEvent.getUserName() + " ] Withdrawal request has been submitted !!");
                                break;
                            default:
                                logger.info("Unknown Event");
                                break;
                        }
                    }catch(Exception e) {
                        logger.info(e.getMessage());
                    }
                } else {
                    logger.error("get message on topic " + record.topic());
                }
            }
            try{
                consumer.commitSync(); // 중복 처리 방지
            }catch(CommitFailedException e){
                logger.error("commit failed");
            }
        }
    }
}

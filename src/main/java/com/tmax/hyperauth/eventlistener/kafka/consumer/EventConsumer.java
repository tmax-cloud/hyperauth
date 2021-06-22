package com.tmax.hyperauth.eventlistener.kafka.consumer;

import com.google.gson.Gson;
import com.tmax.hyperauth.eventlistener.TopicEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author taegeon_woo@tmax.co.kr
 * This Code is only for Guide
 */

@Slf4j
public class EventConsumer {
    private static final String TOPIC_NAME = "tmax";
//    private static final String BOOTSTRAP_SERVERS_INTERNAL = "kafka-1.hyperauth:9092,kafka-2.hyperauth:9092,kafka-3.hyperauth:9092"; // Hyperauth가 떠 있는 Kubernetes Cluster 내부에서 Subscribe 하는 경우 사용
    private final static String BOOTSTRAP_SERVER_EXTERNAL = "dev-kafka1.tmaxoneaccount.com:9093,dev-kafka2.tmaxoneaccount.com:9093,dev-kafka3.tmaxoneaccount.com:9093"; // Hyperauth가 떠 있는 Kubernetes Cluster 외부에서 Subscribe하는 경우 dev-kafka1.tmaxoneaccount.com:9093 등을 상황에 맞게 바꾸어서 사용한다.

    // kafka broker 3개가 노출되어 있는 주소는 hyperauth, kafka 관리자에게 문의해서 추가한다.

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER_EXTERNAL);
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
         * keytool -keystore hypercloud.keystore.jks -alias hypercloud -validity 3650 -genkey -keyalg RSA -dname "CN=consumer" -storepass tmax@23 -keypass tmax@23
         * keytool -keystore hypercloud.keystore.jks -alias hypercloud -certreq -file ca-request-hypercloud -storepass tmax@23
         * sudo openssl x509 -req -CA /etc/kubernetes/pki/hypercloud-root-ca.crt -CAkey /etc/kubernetes/pki/hypercloud-root-ca.key -in ca-request-hypercloud -out ca-signed-hypercloud -days 3650 -CAcreateserial
         * keytool -keystore hypercloud.keystore.jks -alias ca-cert -import -file /etc/kubernetes/pki/hypercloud-root-ca.crt -storepass tmax@23 -noprompt
         * keytool -keystore hypercloud.keystore.jks -alias hypercloud -import -file ca-signed-hypercloud -storepass tmax@23 -noprompt
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
        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/etc/ssl/kafka/hypercloud.truststore.jks");
        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));
        properties.setProperty(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/ssl/kafka/hypercloud.keystore.jks");
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
                    log.info("[[ MESSAGE FROM TMAX TOPIC ]]");
                    log.info(record.value());
                    try {
                        Gson gson = new Gson();
                        TopicEvent topicEvent = gson.fromJson(record.value(), TopicEvent.class);
                        switch (topicEvent.getType()){
                            case "LOGIN":
                                log.info("User [ " + topicEvent.getUserName() + " ] Login !!");
                                break;
                            case "LOGOUT":
                                log.info("User [ " + topicEvent.getUserName() + " ] Logout !!");
                                break;
                            case "LOGIN_FAILED":
                                log.info("User [ " + topicEvent.getUserName() + " ] Login failed due to " + topicEvent.getError());
                                break;
                            case "USER_DELETE":
                                log.info("User [ " + topicEvent.getUserName() + " ] Deleted !!");
                                break;
                            case "USER_WITHDRAWAL":
                                log.info("User [ " + topicEvent.getUserName() + " ] Withdrawal request has been submitted !!");
                                break;
                            default:
                                log.info("Unknown Event");
                                break;
                        }
                    }catch(Exception e) {
                        log.error("Error Occurs!!", e);
                    }
                } else {
                    log.error("get message on topic " + record.topic());
                }
            }
            try{
                consumer.commitSync(); // 중복 처리 방지
            }catch(CommitFailedException e){
                log.error("Error Occurs, commit failed!!", e);
            }
        }
    }
}

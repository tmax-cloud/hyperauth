package com.tmax.hyperauth.eventlistener.kafka.producer;

import com.google.gson.Gson;
import com.tmax.hyperauth.caller.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
public class KafkaProducer {
    private static String nameSpace = StringUtil.isNotEmpty(System.getenv("NAMESPACE"))? System.getenv("NAMESPACE") : "hyperauth";
    private static String BOOTSTRAP_SERVER = "kafka-kafka-bootstrap."+ nameSpace +":9092"; //modified 211208
    public static void publishEvent(String topic, Object value){
        //reset thread context
        resetThreadContext();

        // create the producer
        org.apache.kafka.clients.producer.KafkaProducer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(getProperties());

        // create a producer record
        Gson gson = new Gson();
        String jsonValue = gson.toJson(value);
        ProducerRecord<String, String> eventRecord = new ProducerRecord<String, String>(topic, jsonValue);

        // Async Call producer.send
        new Thread(
                () -> {
                    try {
                        log.info("kafka produce start");
                        producer.send(eventRecord, (metadata, exception) -> {
                            if (exception != null) {
                                log.error("Error Occurs!!", exception);
                                log.error("Failed to Send to Topic Server");
                            } else {
                                log.info("kafka produce success");
                            }
                        });
                    } catch (Exception e) {
                        log.error("Error Occurs!!", e);
                    } finally {
                        producer.flush();
                        producer.close();
                    }
                }
        ).start();
    }

    private static void resetThreadContext() {
        Thread.currentThread().setContextClassLoader(null);
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        if( StringUtil.isNotEmpty(System.getenv("KAFKA_BROKERS_ADDR")) ){
            BOOTSTRAP_SERVER = System.getenv("KAFKA_BROKERS_ADDR");
        }
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG, "30000"); // Wait 30 Seconds until producer.send() timeout

//      for SSL
        properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        //TODO : v1.1.1.34 버전부터 cert-manager를 사용하면서 jks 이름이 변경 주의 요망
        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/etc/x509/kafka/truststore.jks");
        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));
        properties.setProperty(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/x509/kafka/keystore.jks");
        properties.setProperty(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));
        properties.setProperty(SslConfigs.SSL_KEY_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));

        return properties;
    }

}



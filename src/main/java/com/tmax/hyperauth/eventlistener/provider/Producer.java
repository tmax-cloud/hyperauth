package com.tmax.hyperauth.eventlistener.provider;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
public class Producer {
    private final static String BOOTSTRAP_SERVER = "kafka-1."+System.getenv("NAMESPACE")+":9092,kafka-2."+System.getenv("NAMESPACE")+":9092,kafka-3."+System.getenv("NAMESPACE")+":9092";
    public static void publishEvent(String topic, Object value){
        //reset thread context
        resetThreadContext();

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(getProperties());

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
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG, "30000"); // Wait 30 Seconds until producer.send() timeout

////      for SSL
//        properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/etc/x509/kafka/hyperauth.truststore.jks");
//        properties.setProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));
//        properties.setProperty(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/x509/kafka/hyperauth.keystore.jks");
//        properties.setProperty(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));
//        properties.setProperty(SslConfigs.SSL_KEY_PASSWORD_CONFIG, System.getenv("CERTS_PASSWORD"));

        // for SASL_OAUTH_BEARER
        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "kafka-producer");
        properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required;");
        properties.setProperty(SaslConfigs.SASL_MECHANISM, "OAUTHBEARER");
        properties.setProperty(SaslConfigs.SASL_LOGIN_CALLBACK_HANDLER_CLASS, "com.bfm.kafka.security.oauthbearer.OAuthAuthenticateLoginCallbackHandler");
//        properties.setProperty("KAFKA_OAUTH_SERVER_PROP_FILE", "oauth-configuration.properties");

        // for props file
        properties.setProperty("oauth.server.base.uri", "http://172.22.6.8:8080/auth/realms/tmax/protocol/openid-connect");
        properties.setProperty("oauth.server.token.endpoint.path", "/token");
        properties.setProperty("oauth.server.introspection.endpoint.path", "/token/introspect");
        properties.setProperty("oauth.server.client.id", "kafka-producer");
        properties.setProperty("oauth.server.client.secret", "4115340e-cdcd-45c6-910e-41413b0d8ad8");
        properties.setProperty("oauth.server.grant.type", "client_credentials");
        properties.setProperty("oauth.server.scopes", "urn:kafka:topic:tmax:write");
        properties.setProperty("oauth.server.accept.unsecure.server", "true");
        properties.setProperty("unsecuredLoginStringClaim_sub", "admin");

//        oauth.server.base.uri=http://localhost:8080/auth/realms/tmax/protocol/openid-connect
//        oauth.server.token.endpoint.path=/token
//        oauth.server.introspection.endpoint.path=/token/introspect
//        oauth.server.client.id=kafka-producer
//        oauth.server.client.secret=4115340e-cdcd-45c6-910e-41413b0d8ad8
//        oauth.server.grant.type=client_credentials
//        oauth.server.scopes=urn:kafka:topic:tmax:write
//        oauth.server.accept.unsecure.server=true
        return properties;
    }

}



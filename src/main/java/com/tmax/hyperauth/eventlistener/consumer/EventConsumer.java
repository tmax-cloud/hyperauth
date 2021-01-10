package com.tmax.hyperauth.eventlistener.consumer;

import com.google.gson.Gson;
import com.tmax.hyperauth.eventlistener.provider.HyperauthEventListenerProvider;
import com.tmax.hyperauth.eventlistener.provider.TopicEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.Properties;

public class EventConsumer {
    private static final Logger logger = Logger.getLogger(EventConsumer.class);
    private static final String TOPIC_NAME = "tmax";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://kafkas.hyperauth:9092");
        properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "hypercloud");   // TODO: Change group id to your Hyperauth client Name
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(500);
            for (ConsumerRecord<String, String> record : records) {
                String s = record.topic();
                if (TOPIC_NAME.equals(s)) {
                    logger.info("[[ MESSAGE FROM TMAX TOPIC ]]");
                    logger.info(record.value());
                    Gson gson = new Gson();
                    TopicEvent.Item topic = gson.fromJson(record.value(), TopicEvent.Item.class);
                    switch (topic.getVerb()){
                        case "USER_DELETE":
                            logger.info("User [ " + topic.getUser().getUsername() + " ] Deleted !!");
                            break;
                        case "USER_WITHDRAWAL":
                            logger.info("User [ " + topic.getUser().getUsername() + " ] Withdrawal request has been submitted !!");
                            break;
                        default:
                            logger.info("Unknown Event");
                            break;
                    }
                } else {
                    throw new IllegalStateException("get message on topic " + record.topic());
                }
            }
        }
    }
}

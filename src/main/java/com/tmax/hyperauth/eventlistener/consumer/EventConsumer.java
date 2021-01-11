package com.tmax.hyperauth.eventlistener.consumer;

import com.google.gson.Gson;
import com.tmax.hyperauth.eventlistener.provider.HyperauthEventListenerProvider;
import com.tmax.hyperauth.eventlistener.provider.TopicEvent;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class EventConsumer {
    private static final Logger logger = Logger.getLogger(EventConsumer.class);
    private static final String TOPIC_NAME = "tmax";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://kafkas.hyperauth:9092"); // k8s cluster 내부의 경우에 사용 , 외부에서 사용하는 경우 : http://{nodeIP}:32576
        properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "hypercloud");   // TODO: Change group id to your Hyperauth client Name
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); //중복처리방지, 성능향상을 위해서는 true로 바꿔준다.

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                String s = record.topic();
                if (TOPIC_NAME.equals(s)) {
                    logger.info("[[ MESSAGE FROM TMAX TOPIC ]]");
                    logger.info(record.value());
                    Gson gson = new Gson();
                    TopicEvent.Event topicEvent = gson.fromJson(record.value(), TopicEvent.Event.class);
                    switch (topicEvent.getVerb()){
                        case "LOGIN":
                            logger.info("User [ " + topicEvent.getUser().getUsername() + " ] Login !!");
                            break;
                        case "LOGOUT":
                            logger.info("User [ " + topicEvent.getUser().getUsername() + " ] Logout !!");
                            break;
                        case "LOGIN_FAILED":
                            logger.info("User [ " + topicEvent.getUser().getUsername() + " ] Login failed due to " + topicEvent.getResponseStatus().getReason());
                            break;
                        case "USER_DELETE":
                            logger.info("User [ " + topicEvent.getUser().getUsername() + " ] Deleted !!");
                            break;
                        case "USER_WITHDRAWAL":
                            logger.info("User [ " + topicEvent.getUser().getUsername() + " ] Withdrawal request has been submitted !!");
                            break;
                        default:
                            logger.info("Unknown Event");
                            break;
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

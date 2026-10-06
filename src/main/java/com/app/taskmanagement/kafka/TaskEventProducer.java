package com.app.taskmanagement.kafka;

import com.app.taskmanagement.event.TaskCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskEventProducer {
    private final KafkaTemplate<Long, TaskCreatedEvent> kafkaTemplate;
    public TaskEventProducer(KafkaTemplate<Long, TaskCreatedEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void send(String topic,Long key,TaskCreatedEvent value){
        kafkaTemplate.send(topic, key, value);
    }
}

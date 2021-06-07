package com.kafka.consumer.api.model;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component

public class LibraryEventConsumerManual implements AcknowledgingMessageListener<Integer, String> {
	


	@Override
	@KafkaListener(topics = {"library-events"})
	public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
		System.out.println("Consumer Record: - "+data);
		acknowledgment.acknowledge();
		
	}
	
	

}

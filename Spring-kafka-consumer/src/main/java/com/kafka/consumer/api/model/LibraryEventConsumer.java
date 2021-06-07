package com.kafka.consumer.api.model;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.consumer.api.service.LibraryEventService;

@Component

public class LibraryEventConsumer {
	
	@Autowired
	LibraryEventService libraryEventService;
	
	
	@KafkaListener(topics = {"library-events"})
	private void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
		System.out.println("Consumer Record: - "+consumerRecord);
		try {
			libraryEventService.processLibraryEventRecord(consumerRecord);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	

}

package com.kafka.consumer.api.service;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.consumer.api.model.LibraryEvent;
import com.kafka.consumer.api.repo.LibraryEventRepo;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationsException;

import org.springframework.stereotype.Service;

@Service
public class LibraryEventService {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	LibraryEventRepo libraryEventRepo;
	
	public void processLibraryEventRecord(ConsumerRecord<Integer, String> consumerRecord) throws JsonMappingException, JsonProcessingException {
		LibraryEvent event  = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
		
		switch (event.getLibraryEventType()) {
		case NEW:
			SaveEvent(event);
			break;
		case UPDATE:
			Validate(event);
			SaveEvent(event);
			break;
		default:
			System.out.println("Invalid Lirary Event Type");
			break;
		}
		
	}
	
	private void Validate(LibraryEvent event) {
		if(event.getLibraryEventId() == null) {
			throw new IllegalArgumentException("Library Event Id is Missing");
		}
		
		Optional<LibraryEvent> libraryEventOptional = libraryEventRepo.findById(event.getLibraryEventId());
		
		if(!libraryEventOptional.isPresent()) {
			throw new IllegalArgumentException("Library Event is Invalid");
		}
	}
	
	
	
	private void SaveEvent(LibraryEvent event) {
		event.getBook().setLibraryEventId(event);
		libraryEventRepo.save(event);
		System.out.println("Successfully Persisted the library Event -->" + event);
	}
	
	
	

}

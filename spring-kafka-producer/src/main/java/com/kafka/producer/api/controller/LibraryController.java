package com.kafka.producer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.producer.api.model.LibEventType;
import com.kafka.producer.api.model.LibraryEvent;
import com.kafka.producer.api.producer.LibraryEventProducer;

@RestController
public class LibraryController {

	@Autowired
	LibraryEventProducer libEventProducer;
	
	@PostMapping("/newBook")
	public ResponseEntity<LibraryEvent> addNewBook(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
		
		libraryEvent.setLibraryEventType(LibEventType.NEW);
		libEventProducer.sendLibrarySyncEventMessage(libraryEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
}

package com.kafka.producer.api.config;


import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.api.controller.LibraryController;
import com.kafka.producer.api.model.Book;
import com.kafka.producer.api.model.LibEventType;
import com.kafka.producer.api.model.LibraryEvent;
import com.kafka.producer.api.producer.LibraryEventProducer;

@WebMvcTest(LibraryController.class)
@AutoConfigureMockMvc
public class LibraryControllerUnitTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	LibraryEventProducer libraryEventProducer;
	
	ObjectMapper objMapper = new ObjectMapper();
	
	@Test
	void postLibraryEvent() throws Exception {
		LibraryEvent event = new LibraryEvent();
		event.setLibraryEventId(null);
		event.setLibraryEventType(LibEventType.NEW);
		
		Book book = new Book();
		book.setBookId(1);
		book.setBookName("Test Book");
		book.setAuthorName("Test Name");
		event.setBook(book);
		
		
		String json = objMapper.writeValueAsString(event);
		doNothing().when(libraryEventProducer).sendLibraryEvent_Approach2(event);
		
;		mockMvc.perform(post("/newBook").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}
	

}

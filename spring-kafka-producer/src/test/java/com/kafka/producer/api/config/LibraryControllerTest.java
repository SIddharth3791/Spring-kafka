package com.kafka.producer.api.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.kafka.producer.api.model.Book;
import com.kafka.producer.api.model.LibEventType;
import com.kafka.producer.api.model.LibraryEvent;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 2)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
@Timeout(5000)
public class LibraryControllerTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	EmbeddedKafkaBroker broker;
	
	private Consumer<Integer, String> consumer;
	
	
	@BeforeEach
	void setUp() {
		Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", broker));
		consumer = new DefaultKafkaConsumerFactory(configs,new IntegerDeserializer(), new StringDeserializer()).createConsumer();
		broker.consumeFromAllEmbeddedTopics(consumer);
	}
	
	@AfterEach
	void tearDown() {
		consumer.close();
	}
	
	
	@Test
	void addNewBook() throws InterruptedException {
		
		LibraryEvent event = new LibraryEvent();
		event.setLibraryEventId(null);
		event.setLibraryEventType(LibEventType.NEW);
		
		Book book = new Book();
		book.setBookId(1);
		book.setBookName("Test Book");
		book.setAuthorName("Test Name");
		event.setBook(book);
		
		HttpHeaders header = new HttpHeaders();
		header.set("content-type", MediaType.APPLICATION_JSON.toString());
		HttpEntity<LibraryEvent> request = new HttpEntity<>(event,header);
		
		ResponseEntity<LibraryEvent> response  =  restTemplate.exchange("/newBook", HttpMethod.POST,request, LibraryEvent.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		
		ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "library-events");
		
		String expectedVal = "{\"libraryEventId\":null,\"libraryEventType\":\"NEW\",\"book\":{\"bookId\":1,\"bookName\":\"Test Book\",\"authorName\":\"Test Name\"}}";
		String value = consumerRecord.value();
		
		
		assertEquals(expectedVal, value);
		
	}

}

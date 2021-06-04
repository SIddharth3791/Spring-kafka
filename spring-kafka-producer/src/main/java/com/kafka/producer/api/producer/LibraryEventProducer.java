package com.kafka.producer.api.producer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.api.model.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventProducer {

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	ObjectMapper objMapper;

	private static final String TOPIC="library-events";

	public void sendLibraryEventMessageWithTopic(LibraryEvent libraryEvent) throws JsonProcessingException {

		Integer key = libraryEvent.getLibraryEventId();
		String value = objMapper.writeValueAsString(libraryEvent);;


		ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.send(TOPIC, key, value);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer,String>>(){

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccessMessage(key, value ,result);

			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailMessage(key, value, ex);

			}

		});
	}

	public SendResult<Integer,String> sendLibrarySyncEventMessage(LibraryEvent libraryEvent) throws JsonProcessingException {

		Integer key = libraryEvent.getLibraryEventId();
		String value = objMapper.writeValueAsString(libraryEvent);;
		SendResult<Integer,String> result = null;

		try {
			result = kafkaTemplate.sendDefault( key, value).get();
		} catch (InterruptedException  | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}


	public void sendLibraryEventMessageNoTopic(LibraryEvent libraryEvent) throws JsonProcessingException {

		Integer key = libraryEvent.getLibraryEventId();
		String value = objMapper.writeValueAsString(libraryEvent);;


		ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer,String>>(){

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccessMessage(key, value ,result);

			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailMessage(key, value, ex);

			}

		});
	}


	public void sendLibraryEvent_Approach2(LibraryEvent libraryEvent) throws JsonProcessingException {

		Integer key = libraryEvent.getLibraryEventId();
		String value = objMapper.writeValueAsString(libraryEvent);

		ProducerRecord<Integer,String> producerRecord = buildProducerRecord(key, value, TOPIC);

		ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(producerRecord);

		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onFailure(Throwable ex) {
				handleFailMessage(key, value, ex);
			}

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccessMessage(key, value, result);
			}
		});
	}

	private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {


		List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

		return new ProducerRecord<>(topic, null, key, value, recordHeaders);
	}

	private void handleSuccessMessage(Integer key, String value, SendResult<Integer,String> result) {
		System.out.print("Message Sent Successfully with partition:- " + result.getRecordMetadata().partition());
	}

	private void handleFailMessage(Integer key, String value, Throwable ex) {
		System.out.print("Message Sent operation failed");
		try {
			throw ex;

		}catch(Throwable tr) {
			System.out.println("Error due to failure: "+tr);
		}
	}

}

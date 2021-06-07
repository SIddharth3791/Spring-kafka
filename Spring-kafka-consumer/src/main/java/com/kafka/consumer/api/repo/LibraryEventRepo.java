package com.kafka.consumer.api.repo;

import org.springframework.data.repository.CrudRepository;

import com.kafka.consumer.api.model.LibraryEvent;

public interface LibraryEventRepo  extends CrudRepository<LibraryEvent, Integer> {

}

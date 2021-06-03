package com.kafka.producer.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryEvent {
	
	private Integer libraryEventId;
	private LibEventType libraryEventType;
	private Book book;
	
	
	
	public LibraryEvent() {
	}
	public Integer getLibraryEventId() {
		return libraryEventId;
	}
	public void setLibraryEventId(Integer libraryEventId) {
		this.libraryEventId = libraryEventId;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public LibEventType getLibraryEventType() {
		return libraryEventType;
	}
	public void setLibraryEventType(LibEventType libraryEventType) {
		this.libraryEventType = libraryEventType;
	}
}

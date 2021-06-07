package com.kafka.consumer.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class LibraryEvent {

	@Id
	@GeneratedValue
	private Integer libraryEventId;
	@Enumerated(EnumType.STRING)
	private LibEventType libraryEventType;
	@OneToOne(mappedBy = "libraryEventId", cascade = {CascadeType.ALL})
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
	@Override
	public String toString() {
		return "LibraryEvent [libraryEventId=" + libraryEventId + ", libraryEventType=" + libraryEventType + "]";
	}
	
	
}

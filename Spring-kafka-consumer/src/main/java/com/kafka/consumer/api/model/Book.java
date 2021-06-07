package com.kafka.consumer.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Book {
	
	@Id
	private Integer bookId;
	private String bookName;
	private String authorName;
	@OneToOne
	@JoinColumn(name="libraryEventId")
	private LibraryEvent libraryEventId;
	
	public Book() {}
	
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public LibraryEvent getLibraryEventId() {
		return libraryEventId;
	}

	public void setLibraryEventId(LibraryEvent libraryEventId) {
		this.libraryEventId = libraryEventId;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", bookName=" + bookName + ", authorName=" + authorName + ", libraryEventId="
				+ libraryEventId + "]";
	}

	
	

	
}

// File: Book.java
package com.example.bookme;

public class Book {
    private String bookID;
    private String title;
    private String author;
    private String pdfUrl;

    public Book() {
    }

    public Book(String bookID, String title, String author, String pdfUrl) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.pdfUrl = pdfUrl;
    }

    public String getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }
}

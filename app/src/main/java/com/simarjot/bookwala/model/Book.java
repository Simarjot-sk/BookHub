package com.simarjot.bookwala.model;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Book {
    private String bookUUID;
    private String userUUID;
    private String title;
    private String author;
    private String subject;
    private String description;
    private int level;
    private String price;
    private String currency;
    private Category category;
    private Timestamp createdOn;
    private List<String> imageDownloadUris;
    private String coverDownloadUri;
    //todo add location coordinates

    public Book(String title, String author, String subject, String description, String price, String currency, Category category, int level) {
        this.bookUUID = UUID.randomUUID().toString();
        this.userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.createdOn = new Timestamp(new Date());
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.description = description;
        this.price = price;
        this.category = category;
        this.level = level;
        this.currency = currency;
    }

    public Book() {
    }

    public String getBookUUID() {
        return bookUUID;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
    }

    public List<String> getImageDownloadUris() {
        return imageDownloadUris;
    }

    public void setImageDownloadUris(List<String> imageDownloadUris) {
        this.imageDownloadUris = imageDownloadUris;
    }

    public String getCoverDownloadUri() {
        return coverDownloadUri;
    }

    public void setCoverDownloadUri(String coverDownloadUri) {
        this.coverDownloadUri = coverDownloadUri;
    }
}

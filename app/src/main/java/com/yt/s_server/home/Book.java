package com.yt.s_server.home;

public class Book {
    private String title;
    private String author;
    private String date;
    private String fine;

    public Book(String title, String author, String date, String fine) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.fine = fine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return "著者：" + author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return "还书日期：" + date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }
}

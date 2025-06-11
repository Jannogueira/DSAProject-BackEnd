package edu.upc.dsa.models;

public class Question {
    int id;
    java.sql.Date date; //fecha con el estilo YYYY-MM-DD
    String title;
    String message;
    String sender;

    public Question() {
    }

    public Question(java.sql.Date date, String title, String message, String sender) {
        this.date = date;
        this.title = title;
        this.message = message;
        this.sender = sender;
    }
    public Question(int id, java.sql.Date date, String title, String message, String sender) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.message = message;
        this.sender = sender;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public java.sql.Date getDate() {
        return date;
    }
    public void setDate(java.sql.Date date) {
        this.date = date;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
}

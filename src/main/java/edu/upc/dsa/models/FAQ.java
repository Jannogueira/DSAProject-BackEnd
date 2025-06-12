package edu.upc.dsa.models;

public class FAQ {
    int id;
    java.sql.Date date;
    String question;
    String answer;
    String sender;

    public FAQ(int id, java.sql.Date date,String pregunta, String respuesta, String sender) {
        this.id = id;
        this.date = date;
        this.question = pregunta;
        this.answer = respuesta;
        this.sender = sender;
    }

    public FAQ() {}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public java.sql.Date getDate() {return date;}
    public void setDate(java.sql.Date date) {this.date = date;}
    public String getQuestion() {return question;}
    public void setQuestion(String question) {this.question = question;}
    public String getAnswer() {return answer;}
    public void setAnswer(String answer){this.answer = answer;}
    public String getSender() {return sender;}
    public void setSender(String sender) {this.sender = sender;}
}

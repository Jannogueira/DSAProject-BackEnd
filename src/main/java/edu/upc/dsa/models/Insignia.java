package edu.upc.dsa.models;

public class Insignia {
    int id;
    String name;
    String avatar;

    public Insignia() {
    }
    public Insignia(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }
    public Insignia(int id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getAvatar() {return avatar;}
    public void setAvatar(String avatar) {this.avatar = avatar;}
}

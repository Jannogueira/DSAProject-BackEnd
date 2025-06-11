package edu.upc.dsa.models;

public class Insignias {
    String User;
    int Id_Insignia;

    public Insignias(String user, int id_Insignia) {
        User = user;
        Id_Insignia = id_Insignia;
    }

    public Insignias(){}

    public String getUser() {return User;}
    public void setUser(String user) {User = user;}
    public int getId_Insignia() {return Id_Insignia;}
    public void setId_Insignia(int id_Insignia) {Id_Insignia = id_Insignia;}
}
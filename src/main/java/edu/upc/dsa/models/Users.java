package edu.upc.dsa.models;

public class Users {
    private int id;
    private String usuario;
    private String correo;
    private String password;
    private int score;
    private int money;

    public Users(int id, String usuario, String correo, String password , int score, int money) {
        this.id = id;
        this.usuario = usuario;
        this.correo = correo;
        this.password = password;
        this.money = money;
        this.score = score;
    }
    public Users(String usuario, String correo, String password) {
        this.usuario = usuario;
        this.correo = correo;
        this.password = password;
    }
    public Users(){}

    public int getId() {return id;}
    public String getUsuario() {
        return usuario;
    }
    public String getCorreo() {
        return correo;
    }
    public String getPassword() {
        return password;
    }
    public int getScore() {return score;}
    public int getMoney() {return money;}

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setScore(int score) {this.score = score;}
    public void setMoney(int money) {this.money = money;}
}

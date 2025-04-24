package edu.upc.dsa.models;

public class User {
    String usuario;
    String correo;
    String password;
    String id;
    String score;

    public User(String usuario, String correo, String password){
        this.usuario = usuario;
        this.correo = correo;
        this.password = password;
    }
    public User(){}

    public String getUsario() {
        return usuario;
    }
    public String getCorreo() {
        return correo;
    }
    public String getPassword() {
        return password;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

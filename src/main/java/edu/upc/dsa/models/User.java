package edu.upc.dsa.models;

public class User {
    String usario;
    String correo;
    String password;

    public User(String usuario, String correo, String password){
        this.usario = usuario;
        this.correo = correo;
        this.password = password;
    }

    public String getUsario() {
        return usario;
    }
    public String getCorreo() {
        return correo;
    }
    public String getPassword() {
        return password;
    }

    public void setUsario(String usario) {
        this.usario = usario;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

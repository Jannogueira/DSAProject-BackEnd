package edu.upc.dsa.models;

public class Items {
    int id;
    String nombre;
    String descripcion;
    String url_icon;
    int precio;

    public Items(int id, String nombre, String descripcion, String url_icon, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.url_icon = url_icon;
        this.precio = precio;
    }
    public Items(){}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getNombre() {return nombre;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public String getDescripcion() {return descripcion;}
    public void setUrl_icon(String url_icon) {this.url_icon = url_icon;}
    public String getUrl_icon() {return url_icon;}
    public void setPrecio(int precio) {this.precio = precio;}
    public int getPrecio() {return precio;}
}

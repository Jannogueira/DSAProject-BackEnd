package edu.upc.dsa.models;

public class ItemInventarioDTO {
    private int id;
    private String nombre;
    private String descripcion;
    private String url_icon;
    private int cantidad;

    public ItemInventarioDTO() {}

    public ItemInventarioDTO(int id, String nombre, String descripcion, String url_icon, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.url_icon = url_icon;
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getUrl_icon() { return url_icon; }
    public int getCantidad() { return cantidad; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setUrl_icon(String url_icon) { this.url_icon = url_icon; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
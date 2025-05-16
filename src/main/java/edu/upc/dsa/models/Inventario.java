package edu.upc.dsa.models;

public class Inventario {
    int ID_user;
    int ID_item;
    int Cantidad;

    public Inventario(int ID_user, int ID_item, int Cantidad) {
        this.ID_user = ID_user;
        this.ID_item = ID_item;
        this.Cantidad = Cantidad;
    }

    public Inventario() {}

    public int getID_user() {return ID_user;}
    public int getID_item() {return ID_item;}
    public int getCantidad() {return Cantidad;}
    public void setID_user(int ID_user) {this.ID_user = ID_user;}
    public void setID_item(int ID_item) {this.ID_item = ID_item;}
    public void setCantidad(int Cantidad) {this.Cantidad = Cantidad;}
}

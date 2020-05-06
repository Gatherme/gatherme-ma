package com.example.gatherme.Tinder.View.Activities;

public class Card {
    private String nombre;
    private String ubicacion;
    private String biografia;
    private String nick;

    public Card(String nombre, String ubicacion, String biografia, String nick) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.biografia = biografia;
        this.nick = nick;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}

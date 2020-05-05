package com.example.gatherme.Tinder.View.Activities;

public class Card {
    private String nombre;
    private String ubicacion;
    private String biografia;

    public Card(String nombre, String ubicacion, String biografia) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.biografia = biografia;
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

}

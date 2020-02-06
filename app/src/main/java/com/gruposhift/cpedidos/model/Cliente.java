package com.gruposhift.cpedidos.model;

import androidx.annotation.NonNull;

public class Cliente {

    private  int SysPK;
    private String nombre;

    public Cliente() {
    }

    public Cliente(int sysPK, String nombre) {
        SysPK = sysPK;
        this.nombre = nombre;
    }

    public int getSysPK() {
        return SysPK;
    }

    public void setSysPK(int sysPK) {
        SysPK = sysPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return nombre;
    }
}

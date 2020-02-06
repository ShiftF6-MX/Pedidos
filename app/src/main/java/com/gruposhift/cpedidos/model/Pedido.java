package com.gruposhift.cpedidos.model;

import androidx.annotation.NonNull;

public class Pedido {

    private int sysPK;
    private String descripcion;

    public Pedido() {

    }

    public Pedido(int sysPK, String descripcion) {
        this.sysPK = sysPK;
        this.descripcion = descripcion;
    }

    public int getSysPK() {
        return sysPK;
    }

    public void setSysPK(int sysPK) {
        this.sysPK = sysPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}

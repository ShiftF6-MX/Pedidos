package com.gruposhift.cpedidos.model;

public class Producto {

    private int sysPK;
    private String descripcion;

    public Producto(int sysPK) {
        this.sysPK = sysPK;
    }

    public Producto(int sysPK, String descripcion) {
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
}

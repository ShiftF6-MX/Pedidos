package com.gruposhift.cpedidos.model;

public class DVenta {

    private int sysPK;
    private double cantidad;
    private String status;
    private String unidad;
    private Pedido pedido;
    private Venta venta;

    public DVenta() {

    }

    public DVenta(int sysPK, double cantidad, String status, String unidad, Pedido pedido, Venta venta) {
        this.sysPK = sysPK;
        this.cantidad = cantidad;
        this.status = status;
        this.unidad = unidad;
        this.pedido = pedido;
        this.venta = venta;
    }

    public int getSysPK() {
        return sysPK;
    }

    public void setSysPK(int sysPK) {
        this.sysPK = sysPK;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}

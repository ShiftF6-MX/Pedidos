package com.gruposhift.cpedidos.model;

import androidx.annotation.NonNull;

import java.sql.Date;

public class Venta {

    private int sysPK;
    private int documento;
    private String fecha;
    private int formaPago;
    private String referencia;
    private int consumo;
    private Cliente cliente;
    private int divisa;
    private FoliosDocumentos foliosDocumentos;

    public Venta() {
    }

    public Venta(int sysPK, int documento, String fecha, int formaPago, String referencia, int consumo, Cliente cliente, int divisa, FoliosDocumentos foliosDocumentos) {
        this.sysPK = sysPK;
        this.documento = documento;
        this.fecha = fecha;
        this.formaPago = formaPago;
        this.referencia = referencia;
        this.consumo = consumo;
        this.cliente = cliente;
        this.divisa = divisa;
        this.foliosDocumentos = foliosDocumentos;
    }

    public int getSysPK() {
        return sysPK;
    }

    public void setSysPK(int sysPK) {
        this.sysPK = sysPK;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getConsumo() {
        return consumo;
    }

    public void setConsumo(int consumo) {
        this.consumo = consumo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getDivisa() {
        return divisa;
    }

    public void setDivisa(int divisa) {
        this.divisa = divisa;
    }

    public FoliosDocumentos getFoliosDocumentos() {
        return foliosDocumentos;
    }

    public void setFoliosDocumentos(FoliosDocumentos foliosDocumentos) {
        this.foliosDocumentos = foliosDocumentos;
    }
}

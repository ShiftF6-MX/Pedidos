package com.gruposhift.cpedidos.model;

public class BlockDocumento {

    private int sysPK;
    private String serie;
    private int fultimo;

    public BlockDocumento() {

    }

    public BlockDocumento(int sysPK, String serie, int fultimo) {
        this.sysPK = sysPK;
        this.serie = serie;
        this.fultimo = fultimo;
    }

    public int getSysPK() {
        return sysPK;
    }

    public void setSysPK(int sysPK) {
        this.sysPK = sysPK;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getFultimo() {
        return fultimo;
    }

    public void setFultimo(int fultimo) {
        this.fultimo = fultimo;
    }
}

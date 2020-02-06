package com.gruposhift.cpedidos.model;

public class FoliosDocumentos {

    private int sysPK;
    private BlockDocumento blockDocumentos;

    public FoliosDocumentos() {
    }

    public FoliosDocumentos(int sysPK, BlockDocumento blockDocumentos) {
        this.sysPK = sysPK;
        this.blockDocumentos = blockDocumentos;
    }

    public int getSysPK() {
        return sysPK;
    }

    public void setSysPK(int sysPK) {
        this.sysPK = sysPK;
    }

    public BlockDocumento getBlockDocumentos() {
        return blockDocumentos;
    }

    public void setBlockDocumentos(BlockDocumento blockDocumentos) {
        this.blockDocumentos = blockDocumentos;
    }
}

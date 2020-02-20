package com.gruposhift.cpedidos.constantes;

public class URLs {
    public static String url = "http://192.168.1.66:8080/pedidos/";
    public static String readPedidosActivos = url + "ReadMaxi.php";
    public static  String readPedidosActivosPorFecha = url + "readMaxiFecha.php?Fecha=";
    public static String buscarPedidosActivos = url + "";
    public static String readBlockDocumento = url + "readBlockDocumento.php";
    public static String updateBlockDocumento = url + "updateBlockDocumentos.php?";
    public static String insertFoliosDocumento = url + "insertFoliosDocumentos.php?";
    public static String readUltimoFolioDocumento = url + "readUltimoFoliosDocumento.php";
    public static String insertVenta = url + "insertVenta.php?";
    public static String readUltimaVeta = url + "readUltimoVenta.php";
    public static String insertDetalleVenta = url + "insertDventa.php?";
    public static String readClienteSysPK = url + "readClienteSysPK.php?Sys_PK=";
    public static String readDetalleVentaSysPK = url + "readDetalleVentaSysPK.php?FK_Venta_Detalle=";

    //URL para Spinners
    public static String readClientes = url + "readClientes.php";
    public static String readPRoductos = url + "readPRoducto.php";
}

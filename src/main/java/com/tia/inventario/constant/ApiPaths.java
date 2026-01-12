package com.tia.inventario.constant;
public class ApiPaths {
    public static final String BASE = "/api";
    public static final String PRODUCTOS = "/productos";
    public static final String PRODUCTOS_ID = "/productos/{id}";
    public static final String PRODUCTOS_BUSCAR = "/productos/buscar";
    public static final String INVENTARIO = "/inventario";
    public static final String INVENTARIO_LOCAL = "/inventario/local/{localId}";
    public static final String INVENTARIO_STOCK_BAJO = "/inventario/stock-bajo";
    public static final String INVENTARIO_ASIGNAR = "/inventario/asignar";
    public static final String VENTAS = "/ventas";
    public static final String VENTAS_ID = "/ventas/{id}";
    public static final String REPORTES = "/reportes";
    public static final String REPORTES_STOCK_CONSOLIDADO = "/reportes/stock-consolidado";
    public static final String REPORTES_VENTAS_DIARIAS = "/reportes/ventas-diarias";
    public static final String REPORTES_MAS_VENDIDOS = "/reportes/mas-vendidos";
    private ApiPaths() {
    }
}
package com.tia.inventario.constant;
public class ErrorMessages {
    public static final String PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
    public static final String CODIGO_DUPLICADO = "Ya existe un producto con ese código";
    public static final String STOCK_INSUFICIENTE = "Stock insuficiente para completar la operación";
    public static final String VENTA_NO_ENCONTRADA = "Venta no encontrada";
    public static final String ERROR_SERVIDOR = "Error del servidor. Intente más tarde.";
    private ErrorMessages() {
    }
}
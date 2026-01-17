package com.tia.inventario.exception;
public class BusinessException extends RuntimeException {
    public BusinessException(String mensaje) {
        super(mensaje);
    }
    public BusinessException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
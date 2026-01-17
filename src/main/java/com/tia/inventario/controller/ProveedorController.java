package com.tia.inventario.controller;

import com.tia.inventario.model.entity.Proveedor;
import com.tia.inventario.service.ProveedorService;
import com.tia.inventario.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
@Tag(name = "Proveedores", description = "API de gestión de proveedores")
@RequiredArgsConstructor
public class ProveedorController {
    
    private final ProveedorService proveedorService;

    @GetMapping
    @Operation(summary = "Listar todos los proveedores activos")
    public ResponseEntity<ApiResponse<List<Proveedor>>> listarTodos() {
        List<Proveedor> proveedores = proveedorService.listarActivos();
        return ResponseEntity.ok(ApiResponse.success(proveedores));
    }
}

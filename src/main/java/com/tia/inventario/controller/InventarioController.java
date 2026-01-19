package com.tia.inventario.controller;
import com.tia.inventario.dto.ApiResponse;
import com.tia.inventario.dto.InventarioDTO;
import com.tia.inventario.dto.MovimientoInventarioDTO;
import com.tia.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.tia.inventario.dto.PageResponse;

@RestController
@RequestMapping("/inventario")
@Tag(name = "Inventario", description = "Gestión de Stock y Movimientos")
@CrossOrigin(origins = "*")
public class InventarioController {
    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/local/{localId}")
    @Operation(summary = "Listar inventario de un local")
    public ResponseEntity<ApiResponse<List<InventarioDTO>>> listarPorLocal(@PathVariable Long localId) {
        List<InventarioDTO> data = inventarioService.listarPorLocal(localId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/local/{localId}/buscar")
    @Operation(summary = "Buscar productos en inventario de un local por nombre o código (paginado)")
    public ResponseEntity<ApiResponse<PageResponse<InventarioDTO>>> buscarPorLocal(
            @PathVariable Long localId,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        PageResponse<InventarioDTO> data = inventarioService.buscarPorLocalPaginated(localId, query, page, size);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    @PostMapping("/asignar")
    @Operation(summary = "Asignar producto a un local (Alta en inventario)")
    public ResponseEntity<ApiResponse<String>> asignarProducto(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-User", defaultValue = "system") String username) {
        Long localId = Long.valueOf(payload.get("localId").toString());
        Long productoId = Long.valueOf(payload.get("productoId").toString());
        int stockInicial = Integer.parseInt(payload.get("stockInicial").toString());
        inventarioService.asignarProducto(localId, productoId, stockInicial, username);
        return ResponseEntity.ok(ApiResponse.success("Producto asignado correctamente"));
    }
    @PostMapping("/movimiento")
    @Operation(summary = "Registrar movimiento de inventario (Entrada/Salida)")
    public ResponseEntity<ApiResponse<String>> registrarMovimiento(
            @RequestBody MovimientoInventarioDTO movimiento,
            @RequestHeader(value = "X-User", defaultValue = "system") String username) {
        inventarioService.registrarMovimiento(movimiento, username);
        return ResponseEntity.ok(ApiResponse.success("Movimiento registrado correctamente"));
    }
}
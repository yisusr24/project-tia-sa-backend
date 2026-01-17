package com.tia.inventario.controller;
import com.tia.inventario.dto.ApiResponse;
import com.tia.inventario.model.dto.BusquedaProductoDTO;
import com.tia.inventario.model.dto.PageResponse;
import com.tia.inventario.model.dto.ProductoDTO;
import com.tia.inventario.model.entity.Producto;
import com.tia.inventario.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "API de gestión de productos")
public class ProductoController {
    private final ProductoService service;
    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene productos activos. Soporta paginación opcional.")
    public ResponseEntity<ApiResponse<Object>> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            log.info("GET /productos (paginado) - page: {}, size: {}", page, size);
            PageResponse<Producto> response = service.findAllPaginated(page, size);
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        log.info("GET /productos (todos)");
        List<Producto> productos = service.findAll();
        return ResponseEntity.ok(ApiResponse.success(productos));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto", description = "Busca un producto por su ID")
    public ResponseEntity<ApiResponse<Producto>> obtener(@PathVariable Long id) {
        log.info("GET /productos/{}", id);
        Producto producto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(producto));
    }
    @PostMapping("/buscar")
    @Operation(summary = "Buscar productos", description = "Búsqueda full-text por nombre")
    public ResponseEntity<ApiResponse<List<Producto>>> buscar(@Valid @RequestBody BusquedaProductoDTO busqueda) {
        log.info("POST /productos/buscar - query: {}", busqueda.getQuery());
        List<Producto> productos = service.buscarPorNombre(busqueda.getQuery());
        return ResponseEntity.ok(ApiResponse.success(productos));
    }
    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto")
    public ResponseEntity<ApiResponse<Producto>> crear(
            @Valid @RequestBody ProductoDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "superadmin") String username) {
        log.info("POST /productos - Usuario: {}", username);
        Producto producto = service.crear(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado exitosamente", producto));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Modifica un producto existente")
    public ResponseEntity<ApiResponse<Producto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "superadmin") String username) {
        log.info("PUT /productos/{} - Usuario: {}", id, username);
        Producto producto = service.actualizar(id, dto, username);
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado", producto));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Marca un producto como eliminado")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /productos/{}", id);
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado", null));
    }
    @GetMapping("/eliminados")
    @Operation(summary = "Listar eliminados", description = "Obtiene los productos en papelera. Soporta paginación opcional.")
    public ResponseEntity<ApiResponse<Object>> listarEliminados(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            log.info("GET /productos/eliminados (paginado) - page: {}, size: {}", page, size);
            PageResponse<Producto> response = service.findDeletedPaginated(page, size);
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        log.info("GET /productos/eliminados (todos)");
        List<Producto> productos = service.findDeleted();
        return ResponseEntity.ok(ApiResponse.success(productos));
    }
    @PutMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar producto", description = "Restaura un producto eliminado")
    public ResponseEntity<ApiResponse<Void>> restaurar(@PathVariable Long id) {
        log.info("PUT /productos/{}/restaurar", id);
        service.restaurar(id);
        return ResponseEntity.ok(ApiResponse.success("Producto restaurado", null));
    }
}
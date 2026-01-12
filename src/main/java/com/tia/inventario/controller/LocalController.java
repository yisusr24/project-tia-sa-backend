package com.tia.inventario.controller;
import com.tia.inventario.dto.LocalDTO;
import com.tia.inventario.model.Local;
import com.tia.inventario.dto.ApiResponse;
import com.tia.inventario.service.LocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/locales")
@RequiredArgsConstructor
@Tag(name = "Locales", description = "Gestión de Locales y Sucursales")
public class LocalController {
    private final LocalService localService;
    @GetMapping
    @Operation(summary = "Listar todos los locales")
    public ResponseEntity<ApiResponse<List<Local>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(localService.getAll()));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener local por ID")
    public ResponseEntity<ApiResponse<Local>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(localService.getById(id)));
    }
    @PostMapping
    @Operation(summary = "Crear nuevo local")
    public ResponseEntity<ApiResponse<Local>> create(
            @Valid @RequestBody LocalDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "system") String username) {
        return ResponseEntity.ok(ApiResponse.success("Local creado", localService.create(dto, username)));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar local existente")
    public ResponseEntity<ApiResponse<Local>> update(
            @PathVariable Long id,
            @Valid @RequestBody LocalDTO dto,
            @RequestHeader(value = "X-User", defaultValue = "system") String username) {
        return ResponseEntity.ok(ApiResponse.success("Local actualizado", localService.update(id, dto, username)));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar local (soft delete)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        localService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Local eliminado", null));
    }
    @GetMapping("/eliminados")
    @Operation(summary = "Listar locales eliminados")
    public ResponseEntity<ApiResponse<List<Local>>> getDeleted() {
        return ResponseEntity.ok(ApiResponse.success(localService.getDeleted()));
    }
    @PutMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar local eliminado")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable Long id) {
        localService.restore(id);
        return ResponseEntity.ok(ApiResponse.success("Local restaurado", null));
    }
}
package com.tia.ventas.controller;
import com.tia.ventas.dto.VentaDTO;
import com.tia.ventas.service.VentaService;
import com.tia.inventario.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
@Slf4j
public class VentaController {
    private final VentaService ventaService;
    @PostMapping
    public ResponseEntity<ApiResponse<VentaDTO>> crearVenta(
            @Valid @RequestBody VentaDTO venta,
            @RequestHeader(value = "X-User", defaultValue = "system") String username) {
        log.info("API: Crear venta");
        VentaDTO nuevaVenta = ventaService.registrarVenta(venta, username);
        return ResponseEntity.ok(ApiResponse.success("Venta registrada exitosamente", nuevaVenta));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VentaDTO>> obtenerVenta(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Venta obtenida", ventaService.obtenerPorId(id)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> listarVentas(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(ApiResponse.success("Ventas listadas", ventaService.listarVentasPaginadas(page, size)));
        }
        return ResponseEntity.ok(ApiResponse.success("Ventas listadas", ventaService.listarVentasPaginadas(0, 10)));
    }
}
package com.tia.inventario.controller;

import com.tia.inventario.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Generación de reportes en PDF y Excel")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas")
    @Operation(summary = "Generar reporte de ventas")
    public ResponseEntity<byte[]> reporteVentas(
        @RequestParam String fechaInicio,
        @RequestParam String fechaFin,
        @RequestParam(defaultValue = "pdf") String formato,
        @RequestHeader(value = "X-User", defaultValue = "system") String username
    ) {
        try {
            byte[] reporte = reporteService.generarReporteVentas(fechaInicio, fechaFin, formato, username);
            
            String extension = "excel".equalsIgnoreCase(formato) ? "xlsx" : "pdf";
            MediaType mediaType = "excel".equalsIgnoreCase(formato) 
                ? MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                : MediaType.APPLICATION_PDF;
                
            String filename = String.format("ventas_%s_a_%s.%s", fechaInicio, fechaFin, extension);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(mediaType)
                .body(reporte);
                
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No hay ventas")) {
                return ResponseEntity.status(404).body(e.getMessage().getBytes());
            }
            log.error("Error servidor: ", e);
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("Error inesperado en reporte ventas", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/inventario")
    @Operation(summary = "Generar reporte de inventario")
    public ResponseEntity<byte[]> reporteInventario(
        @RequestParam Long localId,
        @RequestParam(defaultValue = "pdf") String formato,
        @RequestHeader(value = "X-User", defaultValue = "system") String username
    ) {
        try {
            byte[] reporte = reporteService.generarReporteInventario(localId, formato, username);
            
            String extension = "excel".equalsIgnoreCase(formato) ? "xlsx" : "pdf";
            MediaType mediaType = "excel".equalsIgnoreCase(formato) 
                ? MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                : MediaType.APPLICATION_PDF;
                
            String filename = String.format("inventario_local_%d.%s", localId, extension);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(mediaType)
                .body(reporte);
                
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No hay productos")) {
                return ResponseEntity.status(404).body(e.getMessage().getBytes());
            }
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("Error inesperado en reporte inventario", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

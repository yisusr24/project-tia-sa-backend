package com.tia.inventario.controller;

import com.tia.inventario.dto.ApiResponse;
import com.tia.inventario.dto.ImportResultDTO;
import com.tia.inventario.service.ProductoImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/productos/import")
@RequiredArgsConstructor
@Tag(name = "Importación Masiva", description = "Carga masiva de productos desde CSV")
@CrossOrigin(origins = "*")
public class ProductoImportController {

    private final ProductoImportService importService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Importar productos desde CSV")
    public ResponseEntity<ApiResponse<ImportResultDTO>> importProducts(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "X-User", defaultValue = "system") String username) {
        
        ImportResultDTO result = importService.importProducts(file, username);
        return ResponseEntity.ok(ApiResponse.success("Importación procesada", result));
    }

    @GetMapping("/template")
    @Operation(summary = "Descargar plantilla CSV")
    public ResponseEntity<String> downloadTemplate() {
        String template = importService.generateTemplate();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos_template.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(template);
    }
}

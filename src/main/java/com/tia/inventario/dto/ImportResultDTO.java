package com.tia.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResultDTO {
    private String status;
    private int totalProcessed;
    private int successCount;
    private int errorCount;
    private List<ImportErrorDTO> errors;
    private String duration;
    private String message;
}

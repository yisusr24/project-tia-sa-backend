package com.tia.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorDTO {
    private int lineNumber;
    private String productCode;
    private String errorMessage;
}

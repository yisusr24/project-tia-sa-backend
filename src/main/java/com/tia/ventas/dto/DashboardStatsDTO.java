package com.tia.ventas.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
@Data
@Builder
public class DashboardStatsDTO {
    private long totalProductos;
    private long totalLocales;
    private BigDecimal ventasHoy;
    private long stockBajo;
}
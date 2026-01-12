package com.tia.ventas.service;
import com.tia.inventario.repository.ProductoRepository;
import com.tia.ventas.dto.DashboardStatsDTO;
import com.tia.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    public DashboardStatsDTO getStats(Long localId) {
        return DashboardStatsDTO.builder()
                .totalProductos(productoRepository.count())
                .ventasHoy(ventaRepository.sumAllVentasHoy())
                .stockBajo(0)
                .totalLocales(0)
                .build();
    }
}
package com.tia.ventas.service;
import com.tia.ventas.dto.VentaDTO;
import com.tia.ventas.repository.VentaRepository;
import com.tia.inventario.model.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class VentaService {
    private final VentaRepository ventaRepository;
    @Transactional
    public VentaDTO registrarVenta(VentaDTO venta, String username) {
        log.info("Registrando venta para local: {}", venta.getLocalId());
        try {
            return ventaRepository.create(venta, username);
        } catch (Exception e) {
            log.error("Error al registrar venta: ", e);
            if (e.getMessage() != null && e.getMessage().contains("Stock insuficiente")) {
                 throw new RuntimeException(e.getMessage());
            }
            throw new RuntimeException("Error en Base de Datos: " + e.getMessage(), e);
        }
    }
    public PageResponse<VentaDTO> listarVentasPaginadas(int page, int size) {
        long totalElements = ventaRepository.countAll();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<VentaDTO> data = ventaRepository.findAllPaginated(page, size);
        return new PageResponse<>(data, totalElements, totalPages, page, size);
    }
    public VentaDTO obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + id));
    }
    
    public List<VentaDTO> findAll() {
        log.info("Obteniendo todas las ventas para reporte");
        return ventaRepository.findAllPaginated(0, 10000);
    }
}
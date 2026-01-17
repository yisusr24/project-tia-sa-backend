package com.tia.inventario.service;
import com.tia.inventario.dto.InventarioDTO;
import com.tia.inventario.dto.MovimientoInventarioDTO;
import com.tia.inventario.model.Inventario;
import com.tia.inventario.model.MovimientoInventario;
import com.tia.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class InventarioService {
    @Autowired
    private InventarioRepository inventarioRepository;
    public List<InventarioDTO> listarPorLocal(Long localId) {
        return inventarioRepository.findByLocalId(localId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public void asignarProducto(Long localId, Long productoId, int stockInicial, String username) {
        Optional<Inventario> existing = inventarioRepository.findByLocalAndProducto(localId, productoId);
        if (existing.isPresent()) {
            throw new RuntimeException("El producto ya está asignado a este local");
        }
        Inventario i = new Inventario();
        i.setLocalId(localId);
        i.setProductoId(productoId);
        i.setStockActual(stockInicial);
        i.setStockMinimo(10);
        i.setCreatedBy(username);
        i.setUpdatedBy(username);
        inventarioRepository.save(i);
        if (stockInicial > 0) {
            MovimientoInventario m = new MovimientoInventario();
            m.setLocalId(localId);
            m.setProductoId(productoId);
            m.setTipoMovimiento("ENTRADA");
            m.setCantidad(stockInicial);
            m.setStockAnterior(0);
            m.setStockNuevo(stockInicial);
            m.setMotivo("Stock Inicial por Asignación");
            m.setCreatedBy(username);
            inventarioRepository.saveMovimiento(m);
        }
    }
    @Transactional
    public void registrarMovimiento(MovimientoInventarioDTO dto, String username) {
        Inventario inventario = inventarioRepository.findByLocalAndProducto(dto.getLocalId(), dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en este local"));
        int currentStock = inventario.getStockActual();
        int cantidad = dto.getCantidad();
        int newStock = currentStock;
        if ("ENTRADA".equals(dto.getTipoMovimiento())) {
            newStock += cantidad;
        } else if ("SALIDA".equals(dto.getTipoMovimiento()) || "VENTA".equals(dto.getTipoMovimiento())) {
            if (currentStock < cantidad) {
                throw new RuntimeException("Stock insuficiente. Disponible: " + currentStock);
            }
            newStock -= cantidad;
        }
        inventario.setStockActual(newStock);
        inventario.setUpdatedBy(username);
        inventarioRepository.save(inventario);
        MovimientoInventario m = new MovimientoInventario();
        m.setLocalId(dto.getLocalId());
        m.setProductoId(dto.getProductoId());
        m.setTipoMovimiento(dto.getTipoMovimiento());
        m.setCantidad(cantidad);
        m.setStockAnterior(currentStock);
        m.setStockNuevo(newStock);
        m.setPrecioUnitario(dto.getPrecioUnitario());
        m.setMotivo(dto.getMotivo());
        m.setNumeroDocumento(dto.getNumeroDocumento());
        m.setCreatedBy(username);
        inventarioRepository.saveMovimiento(m);
    }
    private InventarioDTO mapToDTO(Inventario i) {
        InventarioDTO dto = new InventarioDTO();
        dto.setId(i.getId());
        dto.setProductoId(i.getProductoId());
        dto.setLocalId(i.getLocalId());
        dto.setStockActual(i.getStockActual());
        dto.setStockMinimo(i.getStockMinimo());
        dto.setStockMaximo(i.getStockMaximo());
        dto.setUbicacion(i.getUbicacion());
        dto.setLote(i.getLote());
        dto.setFechaVencimiento(i.getFechaVencimiento());
        dto.setProductoNombre(i.getProductoNombre());
        dto.setProductoCodigo(i.getProductoCodigo());
        dto.setLocalNombre(i.getLocalNombre());
        dto.setPrecioVenta(i.getPrecioVenta());
        return dto;
    }
}
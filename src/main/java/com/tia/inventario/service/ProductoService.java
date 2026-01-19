package com.tia.inventario.service;
import com.tia.inventario.constant.ErrorMessages;
import com.tia.inventario.exception.BusinessException;
import com.tia.inventario.dto.PageResponse;
import com.tia.inventario.dto.ProductoDTO;
import com.tia.inventario.model.entity.Producto;
import com.tia.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository repository;
    public List<Producto> findAll() {
        return repository.findAll();
    }
    public PageResponse<Producto> findAllPaginated(int page, int size) {
        List<Producto> data = repository.findAllPaginated(page, size);
        long totalElements = repository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponse<>(data, totalElements, totalPages, page, size);
    }
    public PageResponse<Producto> findDeletedPaginated(int page, int size) {
        List<Producto> data = repository.findDeletedPaginated(page, size);
        long totalElements = repository.countDeleted();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponse<>(data, totalElements, totalPages, page, size);
    }
    public Producto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.PRODUCTO_NO_ENCONTRADO));
    }
    public List<Producto> buscarPorNombre(String query) {
        return repository.buscarPorNombre(query);
    }

    public PageResponse<Producto> buscarPorNombrePaginado(String query, int page, int size) {
        List<Producto> data = repository.buscarPorNombrePaginado(query, page, size);
        long totalElements = repository.countBuscarPorNombre(query);
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponse<>(data, totalElements, totalPages, page, size);
    }
    @Transactional
    public Producto crear(ProductoDTO dto, String username) {
        repository.findByCodigo(dto.getCodigo()).ifPresent(p -> {
            throw new BusinessException(ErrorMessages.CODIGO_DUPLICADO);
        });
        Producto producto = Producto.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .categoriaId(dto.getCategoriaId())
                .proveedorId(dto.getProveedorId())
                .unidadMedidaId(dto.getUnidadMedidaId())
                .precioCompra(dto.getPrecioCompra())
                .precioVenta(dto.getPrecioVenta())
                .precioVentaMinimo(dto.getPrecioVentaMinimo())
                .stockMinimo(dto.getStockMinimo())
                .stockMaximo(dto.getStockMaximo())
                .imagenUrl(dto.getImagenUrl())
                .esPerecedero(dto.getEsPerecedero())
                .diasVigencia(dto.getDiasVigencia())
                .activo(true)
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();
        return repository.save(producto);
    }
    @Transactional
    public Producto actualizar(Long id, ProductoDTO dto, String username) {
        log.info("Actualizando producto ID {} - Usuario: {}", id, username);
        
        Producto producto = findById(id);
        String codigoAnterior = producto.getCodigo();
        
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoriaId(dto.getCategoriaId());
        producto.setProveedorId(dto.getProveedorId());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setPrecioVentaMinimo(dto.getPrecioVentaMinimo());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setStockMaximo(dto.getStockMaximo());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setEsPerecedero(dto.getEsPerecedero());
        producto.setDiasVigencia(dto.getDiasVigencia());
        producto.setUpdatedBy(username);
        
        Producto updated = repository.update(producto);
        log.info("Producto actualizado: '{}' (ID {})", codigoAnterior, id);
        return updated;
    }
    @Transactional
    public void eliminar(Long id) {
        Producto producto = findById(id);
        log.info("Eliminando producto: '{}' (ID {}, Código: {})", producto.getNombre(), id, producto.getCodigo());
        repository.delete(id);
        log.info("Producto eliminado exitosamente: ID {}", id);
    }
    public List<Producto> findDeleted() {
        return repository.findDeleted();
    }
    @Transactional
    public void restaurar(Long id) {
        log.info("Restaurando producto ID {}", id);
        repository.restore(id);
        log.info("Producto restaurado: ID {}", id);
    }
}
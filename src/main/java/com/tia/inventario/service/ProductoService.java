package com.tia.inventario.service;
import com.tia.inventario.constant.ErrorMessages;
import com.tia.inventario.exception.BusinessException;
import com.tia.inventario.model.dto.PageResponse;
import com.tia.inventario.model.dto.ProductoDTO;
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
        Producto producto = findById(id);
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
        return repository.update(producto);
    }
    @Transactional
    public void eliminar(Long id) {
        findById(id);
        repository.delete(id);
    }
    public List<Producto> findDeleted() {
        return repository.findDeleted();
    }
    @Transactional
    public void restaurar(Long id) {
        repository.restore(id);
    }
}
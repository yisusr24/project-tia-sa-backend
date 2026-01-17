package com.tia.inventario.service;

import com.tia.inventario.model.entity.Proveedor;
import com.tia.inventario.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;

    public List<Proveedor> listarActivos() {
        return proveedorRepository.findAllActive();
    }
}

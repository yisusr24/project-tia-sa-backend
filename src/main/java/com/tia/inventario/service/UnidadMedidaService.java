package com.tia.inventario.service;

import com.tia.inventario.repository.UnidadMedidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadMedidaService {
    private final UnidadMedidaRepository unidadMedidaRepository;

    public List<Long> getActiveUnitIds() {
        return unidadMedidaRepository.findAllActiveIds();
    }
}

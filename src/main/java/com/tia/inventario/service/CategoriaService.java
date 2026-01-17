package com.tia.inventario.service;

import com.tia.inventario.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public List<Long> getActiveCategoryIds() {
        return categoriaRepository.findAllActiveIds();
    }
}

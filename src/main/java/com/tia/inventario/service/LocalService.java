package com.tia.inventario.service;
import com.tia.inventario.dto.LocalDTO;
import com.tia.inventario.model.entity.Local;
import com.tia.inventario.repository.LocalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class LocalService {
    private final LocalRepository localRepository;
    public List<Local> getAll() {
        return localRepository.findAll();
    }
    public Local getById(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local no encontrado con ID: " + id));
    }
    public Local create(LocalDTO dto, String username) {
        log.info("Creando nuevo local: {}", dto.getNombre());
        Local local = new Local();
        local.setCodigo(dto.getCodigo());
        local.setNombre(dto.getNombre());
        local.setDireccion(dto.getDireccion());
        local.setCiudad(dto.getCiudad());
        local.setCanton(dto.getCanton());
        local.setPais(dto.getPais());
        local.setTelefono(dto.getTelefono());
        local.setCorreo(dto.getCorreo());
        local.setTipo(dto.getTipo());
        local.setActivo(true);
        local.setCreatedBy(username);
        return localRepository.save(local);
    }
    public Local update(Long id, LocalDTO dto, String username) {
        log.info("Actualizando local ID: {}", id);
        Local local = getById(id);
        local.setNombre(dto.getNombre());
        local.setDireccion(dto.getDireccion());
        local.setCiudad(dto.getCiudad());
        local.setCanton(dto.getCanton());
        local.setPais(dto.getPais());
        local.setTelefono(dto.getTelefono());
        local.setCorreo(dto.getCorreo());
        local.setTipo(dto.getTipo());
        if (dto.getActivo() != null) {
            local.setActivo(dto.getActivo());
        }
        local.setUpdatedBy(username);
        return localRepository.update(local);
    }
    public void delete(Long id) {
        getById(id);
        localRepository.delete(id);
    }
    public List<Local> getDeleted() {
        return localRepository.findDeleted();
    }
    public void restore(Long id) {
        localRepository.restore(id);
    }
}
package com.tia.inventario.service;
import com.tia.inventario.dto.UsuarioDTO;
import com.tia.inventario.model.entity.Usuario;
import com.tia.inventario.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public UsuarioDTO login(String nombreUsuario, String clave) {
        log.info("Intento de login para usuario: {}", nombreUsuario);
        
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuario == null) {
            log.warn("Login fallido: Usuario '{}' no encontrado", nombreUsuario);
            throw new RuntimeException("Usuario no encontrado");
        }
        if (!usuario.getActivo()) {
            log.warn("Login fallido: Usuario '{}' está inactivo", nombreUsuario);
            throw new RuntimeException("Usuario inactivo");
        }
        if (!usuario.getClave().equals(clave)) {
            log.warn("Login fallido: Contraseña incorrecta para '{}'", nombreUsuario);
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        log.info("Login exitoso: {} {} (Rol: {})", usuario.getNombre(), usuario.getApellido(), usuario.getRolNombre());
        return convertToDTO(usuario);
    }
    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setCorreo(usuario.getCorreo());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRolNombre());
        dto.setActivo(usuario.getActivo());
        return dto;
    }
}
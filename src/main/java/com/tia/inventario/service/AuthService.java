package com.tia.inventario.service;
import com.tia.inventario.model.dto.UsuarioDTO;
import com.tia.inventario.model.entity.Usuario;
import com.tia.inventario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public UsuarioDTO login(String nombreUsuario, String clave) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }
        if (!usuario.getClave().equals(clave)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
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
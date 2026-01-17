package com.tia.inventario.controller;

import com.tia.inventario.dto.ApiResponse;
import com.tia.inventario.model.dto.LoginRequestDTO;
import com.tia.inventario.model.dto.UsuarioDTO;
import com.tia.inventario.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "API de gestión de productos")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioDTO>> login(@RequestBody LoginRequestDTO request) {
        try {
            UsuarioDTO usuario = authService.login(
                request.getNombreUsuario(),
                request.getClave()
            );
            return ResponseEntity.ok(ApiResponse.success("Login exitoso", usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<UsuarioDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
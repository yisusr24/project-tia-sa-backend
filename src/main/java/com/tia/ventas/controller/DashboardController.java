package com.tia.ventas.controller;
import com.tia.inventario.dto.ApiResponse;
import com.tia.ventas.dto.DashboardStatsDTO;
import com.tia.ventas.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getStats(@RequestParam(defaultValue = "1") Long localId) {
        DashboardStatsDTO stats = dashboardService.getStats(localId);
        return ResponseEntity.ok(ApiResponse.success("Estadísticas obtenidas", stats));
    }
}
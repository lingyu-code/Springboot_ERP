package org.example.backend.controller;

import org.example.backend.dto.MpsRequest;
import org.example.backend.dto.PlanResponse;
import org.example.backend.service.MpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mps")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend to access
public class MpsController {

    @Autowired
    private MpsService mpsService;

    @PostMapping("/create")
    public ResponseEntity<PlanResponse> createMpsAndGeneratePlans(@RequestBody MpsRequest request) {
        try {
            PlanResponse response = mpsService.createMpsAndGeneratePlans(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("MPS System is running!");
    }
}

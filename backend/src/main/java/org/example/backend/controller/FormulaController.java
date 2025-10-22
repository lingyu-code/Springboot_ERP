package org.example.backend.controller;

import org.example.backend.dto.FormulaQueryRequest;
import org.example.backend.dto.FormulaResponse;
import org.example.backend.service.FormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formulas")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend to access
public class FormulaController {

    @Autowired
    private FormulaService formulaService;

    /**
     * Query formula by variable name
     */
    @GetMapping("/variable/{variableName}")
    public ResponseEntity<FormulaResponse> getFormulaByVariableName(@PathVariable String variableName) {
        try {
            FormulaResponse response = formulaService.getFormulaByVariableName(variableName);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Query formulas by asset class
     */
    @GetMapping("/asset-class/{assetClass}")
    public ResponseEntity<List<FormulaResponse>> getFormulasByAssetClass(@PathVariable String assetClass) {
        try {
            List<FormulaResponse> responses = formulaService.getFormulasByAssetClass(assetClass);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Query formulas by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<FormulaResponse>> getFormulasByCategory(@PathVariable String category) {
        try {
            List<FormulaResponse> responses = formulaService.getFormulasByCategory(category);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Search formulas by keyword
     */
    @GetMapping("/search")
    public ResponseEntity<List<FormulaResponse>> searchFormulas(@RequestParam String keyword) {
        try {
            List<FormulaResponse> responses = formulaService.searchFormulas(keyword);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all active formulas
     */
    @GetMapping("/active")
    public ResponseEntity<List<FormulaResponse>> getAllActiveFormulas() {
        try {
            List<FormulaResponse> responses = formulaService.getAllActiveFormulas();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Flexible query endpoint that accepts multiple query parameters
     */
    @PostMapping("/query")
    public ResponseEntity<List<FormulaResponse>> queryFormulas(@RequestBody FormulaQueryRequest request) {
        try {
            List<FormulaResponse> responses = formulaService.queryFormulas(request);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Formula Service is running!");
    }
}

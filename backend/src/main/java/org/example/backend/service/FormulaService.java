package org.example.backend.service;

import org.example.backend.dto.FormulaQueryRequest;
import org.example.backend.dto.FormulaResponse;
import org.example.backend.entity.BalanceSheetFormula;
import org.example.backend.repository.BalanceSheetFormulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormulaService {

    @Autowired
    private BalanceSheetFormulaRepository formulaRepository;

    /**
     * Query formula by variable name
     */
    public FormulaResponse getFormulaByVariableName(String variableName) {
        Optional<BalanceSheetFormula> formulaOpt = formulaRepository.findByVariableName(variableName);
        
        if (formulaOpt.isPresent()) {
            return convertToResponse(formulaOpt.get());
        } else {
            throw new RuntimeException("Formula not found for variable: " + variableName);
        }
    }

    /**
     * Query formulas by asset class
     */
    public List<FormulaResponse> getFormulasByAssetClass(String assetClass) {
        List<BalanceSheetFormula> formulas = formulaRepository.findByAssetClassAndActiveOrdered(assetClass);
        return formulas.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Query formulas by category
     */
    public List<FormulaResponse> getFormulasByCategory(String category) {
        List<BalanceSheetFormula> formulas = formulaRepository.findByCategory(category);
        return formulas.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Search formulas by keyword
     */
    public List<FormulaResponse> searchFormulas(String keyword) {
        List<BalanceSheetFormula> formulas = formulaRepository.searchByKeyword(keyword);
        return formulas.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all active formulas
     */
    public List<FormulaResponse> getAllActiveFormulas() {
        List<BalanceSheetFormula> formulas = formulaRepository.findByIsActiveTrue();
        return formulas.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Flexible query method that can handle different query parameters
     */
    public List<FormulaResponse> queryFormulas(FormulaQueryRequest request) {
        List<BalanceSheetFormula> formulas;
        
        if (request.getVariableName() != null && !request.getVariableName().isEmpty()) {
            // Query by variable name
            Optional<BalanceSheetFormula> formulaOpt = formulaRepository.findByVariableName(request.getVariableName());
            formulas = formulaOpt.map(List::of).orElse(List.of());
        } else if (request.getAssetClass() != null && !request.getAssetClass().isEmpty()) {
            // Query by asset class
            formulas = formulaRepository.findByAssetClassAndActiveOrdered(request.getAssetClass());
        } else if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            // Query by category
            formulas = formulaRepository.findByCategory(request.getCategory());
        } else {
            // Default: get all active formulas
            formulas = formulaRepository.findByIsActiveTrue();
        }
        
        return formulas.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to response DTO
     */
    private FormulaResponse convertToResponse(BalanceSheetFormula formula) {
        FormulaResponse response = new FormulaResponse();
        response.setId(formula.getId());
        response.setVariableName(formula.getVariableName());
        response.setAssetClass(formula.getAssetClass());
        response.setFormula(formula.getFormula());
        response.setDescription(formula.getDescription());
        response.setCategory(formula.getCategory());
        response.setIsActive(formula.getIsActive());
        response.setCalculationOrder(formula.getCalculationOrder());
        return response;
    }
}

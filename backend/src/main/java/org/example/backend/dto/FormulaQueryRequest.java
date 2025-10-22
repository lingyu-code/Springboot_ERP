package org.example.backend.dto;

import lombok.Data;

@Data
public class FormulaQueryRequest {
    private String variableName;
    private String assetClass;
    private String category;
}

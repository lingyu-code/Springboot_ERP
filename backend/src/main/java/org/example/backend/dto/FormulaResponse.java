package org.example.backend.dto;

import lombok.Data;

@Data
public class FormulaResponse {
    private Long id;
    private String variableName;
    private String assetClass;
    private String formula;
    private String description;
    private String category;
    private Boolean isActive;
    private Integer calculationOrder;
}

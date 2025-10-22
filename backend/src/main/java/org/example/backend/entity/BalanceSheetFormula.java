package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "balance_sheet_formulas")
@Data
public class BalanceSheetFormula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "variable_name", nullable = false, unique = true)
    private String variableName;
    
    @Column(name = "asset_class", nullable = false)
    private String assetClass;
    
    @Column(name = "formula", nullable = false, length = 1000)
    private String formula;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "category")
    private String category; // ASSET, LIABILITY, EQUITY, etc.
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "calculation_order")
    private Integer calculationOrder;
}

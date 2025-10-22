package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProcurementItem {
    private String materialCode;
    private String materialName;
    private Double requiredQuantity;
    private Double availableStock;
    private Double netRequirement;
    private String unit;
    private String supplier;
    private Double leadTime;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private Double cost;
    private String remarks;
}

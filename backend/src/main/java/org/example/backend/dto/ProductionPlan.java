package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProductionPlan {
    private List<ProductionOperation> operations;
    private Double totalProductionTime;
    private LocalDate startDate;
    private LocalDate completionDate;
}

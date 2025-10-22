package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProcurementPlan {
    private List<ProcurementItem> procurementItems;
    private Double totalCost;
    private LocalDate earliestStartDate;
    private LocalDate latestDeliveryDate;
}

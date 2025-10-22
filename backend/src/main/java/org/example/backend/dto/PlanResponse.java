package org.example.backend.dto;

import lombok.Data;

@Data
public class PlanResponse {
    private MpsResponse mpsRecord;
    private ProcurementPlan procurementPlan;
    private ProductionPlan productionPlan;
}

package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductionOperation {
    private String workCenterCode;
    private String workCenterName;
    private Integer sequence;
    private String operationDescription;
    private Double setupTime;
    private Double processingTime;
    private Double totalTime;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private String requiredTools;
    private String requiredSkills;
}

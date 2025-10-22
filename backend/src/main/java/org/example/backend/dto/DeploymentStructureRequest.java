package org.example.backend.dto;

import lombok.Data;

@Data
public class DeploymentStructureRequest {
    private String workCenterCode;
    private String workCenterName;
    private Integer sequence;
    private Double processingTime;
    private String operationDescription;
    private String requiredTools;
    private String requiredSkills;
    private Double setupTime;
    private String remarks;
}

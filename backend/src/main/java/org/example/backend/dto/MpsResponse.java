package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MpsResponse {
    private Long id;
    private String productCode;
    private String productName;
    private Integer quantity;
    private LocalDate plannedDate;
    private LocalDate dueDate;
    private String status;
    private String remarks;
    
    private List<BomItemResponse> bomItems;
    private List<InventoryRecordResponse> inventoryRecords;
    private List<DeploymentStructureResponse> deploymentStructures;
}

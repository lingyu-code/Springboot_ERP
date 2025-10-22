package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MpsRequest {
    private String productCode;
    private String productName;
    private Integer quantity;
    private LocalDate plannedDate;
    private LocalDate dueDate;
    private String remarks;
    
    private List<BomItemRequest> bomItems;
    private List<InventoryRecordRequest> inventoryRecords;
    private List<DeploymentStructureRequest> deploymentStructures;
}

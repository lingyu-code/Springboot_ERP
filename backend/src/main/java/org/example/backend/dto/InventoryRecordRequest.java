package org.example.backend.dto;

import lombok.Data;

@Data
public class InventoryRecordRequest {
    private String materialCode;
    private String materialName;
    private Double currentStock;
    private Double safetyStock;
    private String unit;
    private String warehouseLocation;
    private String remarks;
}

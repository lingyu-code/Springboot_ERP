package org.example.backend.dto;

import lombok.Data;

@Data
public class BomItemRequest {
    private String materialCode;
    private String materialName;
    private String materialType;
    private Double quantityPerUnit;
    private String unit;
    private String supplier;
    private Double leadTime;
    private Double costPerUnit;
    private String remarks;
}

package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory_records")
@Data
public class InventoryRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mps_record_id", nullable = false)
    private MpsRecord mpsRecord;
    
    @Column(nullable = false)
    private String materialCode;
    
    @Column(nullable = false)
    private String materialName;
    
    @Column(nullable = false)
    private Double currentStock;
    
    @Column(nullable = false)
    private Double safetyStock;
    
    @Column(nullable = false)
    private String unit;
    
    @Column
    private String warehouseLocation;
    
    @Column
    private String remarks;
}

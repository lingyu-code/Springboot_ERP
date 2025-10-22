package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bom_items")
@Data
public class BomItem {
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
    private String materialType; // RAW, COMPONENT, SUBASSEMBLY
    
    @Column(nullable = false)
    private Double quantityPerUnit;
    
    @Column(nullable = false)
    private String unit;
    
    @Column
    private String supplier;
    
    @Column
    private Double leadTime; // in days
    
    @Column
    private Double costPerUnit;
    
    @Column
    private String remarks;
}

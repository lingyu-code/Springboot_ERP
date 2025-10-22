package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "mps_records")
@Data
public class MpsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String productCode;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private LocalDate plannedDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    @Column(nullable = false)
    private String status; // PLANNED, IN_PROGRESS, COMPLETED
    
    @OneToMany(mappedBy = "mpsRecord", cascade = CascadeType.ALL)
    private List<BomItem> bomItems;
    
    @OneToMany(mappedBy = "mpsRecord", cascade = CascadeType.ALL)
    private List<InventoryRecord> inventoryRecords;
    
    @OneToMany(mappedBy = "mpsRecord", cascade = CascadeType.ALL)
    private List<DeploymentStructure> deploymentStructures;
    
    @Column
    private String remarks;
}

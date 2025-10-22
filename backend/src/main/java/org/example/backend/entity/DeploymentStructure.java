package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "deployment_structures")
@Data
public class DeploymentStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mps_record_id", nullable = false)
    private MpsRecord mpsRecord;
    
    @Column(nullable = false)
    private String workCenterCode;
    
    @Column(nullable = false)
    private String workCenterName;
    
    @Column(nullable = false)
    private Integer sequence;
    
    @Column(nullable = false)
    private Double processingTime; // hours per unit
    
    @Column(nullable = false)
    private String operationDescription;
    
    @Column
    private String requiredTools;
    
    @Column
    private String requiredSkills;
    
    @Column
    private Double setupTime; // hours
    
    @Column
    private String remarks;
}

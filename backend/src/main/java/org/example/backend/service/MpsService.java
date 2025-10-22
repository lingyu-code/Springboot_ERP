package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.*;
import org.example.backend.repository.MpsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MpsService {

    @Autowired
    private MpsRecordRepository mpsRecordRepository;

    @Transactional
    public PlanResponse createMpsAndGeneratePlans(MpsRequest request) {
        // Create MPS record
        MpsRecord mpsRecord = new MpsRecord();
        mpsRecord.setProductCode(request.getProductCode());
        mpsRecord.setProductName(request.getProductName());
        mpsRecord.setQuantity(request.getQuantity());
        mpsRecord.setPlannedDate(request.getPlannedDate());
        mpsRecord.setDueDate(request.getDueDate());
        mpsRecord.setStatus("PLANNED");
        mpsRecord.setRemarks(request.getRemarks());

        // Create BOM items
        if (request.getBomItems() != null) {
            List<BomItem> bomItems = request.getBomItems().stream()
                    .map(bomRequest -> {
                        BomItem bomItem = new BomItem();
                        bomItem.setMpsRecord(mpsRecord);
                        bomItem.setMaterialCode(bomRequest.getMaterialCode());
                        bomItem.setMaterialName(bomRequest.getMaterialName());
                        bomItem.setMaterialType(bomRequest.getMaterialType());
                        bomItem.setQuantityPerUnit(bomRequest.getQuantityPerUnit());
                        bomItem.setUnit(bomRequest.getUnit());
                        bomItem.setSupplier(bomRequest.getSupplier());
                        bomItem.setLeadTime(bomRequest.getLeadTime());
                        bomItem.setCostPerUnit(bomRequest.getCostPerUnit());
                        bomItem.setRemarks(bomRequest.getRemarks());
                        return bomItem;
                    })
                    .collect(Collectors.toList());
            mpsRecord.setBomItems(bomItems);
        }

        // Create inventory records
        if (request.getInventoryRecords() != null) {
            List<InventoryRecord> inventoryRecords = request.getInventoryRecords().stream()
                    .map(invRequest -> {
                        InventoryRecord inventoryRecord = new InventoryRecord();
                        inventoryRecord.setMpsRecord(mpsRecord);
                        inventoryRecord.setMaterialCode(invRequest.getMaterialCode());
                        inventoryRecord.setMaterialName(invRequest.getMaterialName());
                        inventoryRecord.setCurrentStock(invRequest.getCurrentStock());
                        inventoryRecord.setSafetyStock(invRequest.getSafetyStock());
                        inventoryRecord.setUnit(invRequest.getUnit());
                        inventoryRecord.setWarehouseLocation(invRequest.getWarehouseLocation());
                        inventoryRecord.setRemarks(invRequest.getRemarks());
                        return inventoryRecord;
                    })
                    .collect(Collectors.toList());
            mpsRecord.setInventoryRecords(inventoryRecords);
        }

        // Create deployment structures
        if (request.getDeploymentStructures() != null) {
            List<DeploymentStructure> deploymentStructures = request.getDeploymentStructures().stream()
                    .map(depRequest -> {
                        DeploymentStructure deploymentStructure = new DeploymentStructure();
                        deploymentStructure.setMpsRecord(mpsRecord);
                        deploymentStructure.setWorkCenterCode(depRequest.getWorkCenterCode());
                        deploymentStructure.setWorkCenterName(depRequest.getWorkCenterName());
                        deploymentStructure.setSequence(depRequest.getSequence());
                        deploymentStructure.setProcessingTime(depRequest.getProcessingTime());
                        deploymentStructure.setOperationDescription(depRequest.getOperationDescription());
                        deploymentStructure.setRequiredTools(depRequest.getRequiredTools());
                        deploymentStructure.setRequiredSkills(depRequest.getRequiredSkills());
                        deploymentStructure.setSetupTime(depRequest.getSetupTime());
                        deploymentStructure.setRemarks(depRequest.getRemarks());
                        return deploymentStructure;
                    })
                    .collect(Collectors.toList());
            mpsRecord.setDeploymentStructures(deploymentStructures);
        }

        // Save MPS record with all related entities
        MpsRecord savedRecord = mpsRecordRepository.save(mpsRecord);

        // Generate plans
        ProcurementPlan procurementPlan = generateProcurementPlan(savedRecord);
        ProductionPlan productionPlan = generateProductionPlan(savedRecord);

        // Build response
        return buildPlanResponse(savedRecord, procurementPlan, productionPlan);
    }

    private ProcurementPlan generateProcurementPlan(MpsRecord mpsRecord) {
        List<ProcurementItem> procurementItems = new ArrayList<>();
        double totalCost = 0.0;
        LocalDate earliestStartDate = null;
        LocalDate latestDeliveryDate = null;

        for (BomItem bomItem : mpsRecord.getBomItems()) {
            // Find corresponding inventory record
            InventoryRecord inventoryRecord = mpsRecord.getInventoryRecords().stream()
                    .filter(inv -> inv.getMaterialCode().equals(bomItem.getMaterialCode()))
                    .findFirst()
                    .orElse(null);

            double requiredQuantity = bomItem.getQuantityPerUnit() * mpsRecord.getQuantity();
            double availableStock = inventoryRecord != null ? inventoryRecord.getCurrentStock() : 0;
            double netRequirement = Math.max(0, requiredQuantity - availableStock);

            if (netRequirement > 0) {
                ProcurementItem procurementItem = new ProcurementItem();
                procurementItem.setMaterialCode(bomItem.getMaterialCode());
                procurementItem.setMaterialName(bomItem.getMaterialName());
                procurementItem.setRequiredQuantity(requiredQuantity);
                procurementItem.setAvailableStock(availableStock);
                procurementItem.setNetRequirement(netRequirement);
                procurementItem.setUnit(bomItem.getUnit());
                procurementItem.setSupplier(bomItem.getSupplier());
                procurementItem.setLeadTime(bomItem.getLeadTime());
                
                // Calculate dates
                LocalDate orderDate = mpsRecord.getPlannedDate().minusDays((long) (bomItem.getLeadTime() != null ? bomItem.getLeadTime() : 0));
                LocalDate deliveryDate = mpsRecord.getPlannedDate();
                
                procurementItem.setOrderDate(orderDate);
                procurementItem.setDeliveryDate(deliveryDate);
                
                // Calculate cost
                double cost = netRequirement * (bomItem.getCostPerUnit() != null ? bomItem.getCostPerUnit() : 0);
                procurementItem.setCost(cost);
                totalCost += cost;
                
                procurementItem.setRemarks(bomItem.getRemarks());
                
                procurementItems.add(procurementItem);

                // Update earliest and latest dates
                if (earliestStartDate == null || orderDate.isBefore(earliestStartDate)) {
                    earliestStartDate = orderDate;
                }
                if (latestDeliveryDate == null || deliveryDate.isAfter(latestDeliveryDate)) {
                    latestDeliveryDate = deliveryDate;
                }
            }
        }

        ProcurementPlan procurementPlan = new ProcurementPlan();
        procurementPlan.setProcurementItems(procurementItems);
        procurementPlan.setTotalCost(totalCost);
        procurementPlan.setEarliestStartDate(earliestStartDate);
        procurementPlan.setLatestDeliveryDate(latestDeliveryDate);

        return procurementPlan;
    }

    private ProductionPlan generateProductionPlan(MpsRecord mpsRecord) {
        List<ProductionOperation> operations = new ArrayList<>();
        double totalProductionTime = 0.0;
        LocalDate startDate = mpsRecord.getPlannedDate();
        LocalDate completionDate = mpsRecord.getPlannedDate();

        for (DeploymentStructure deployment : mpsRecord.getDeploymentStructures()) {
            ProductionOperation operation = new ProductionOperation();
            operation.setWorkCenterCode(deployment.getWorkCenterCode());
            operation.setWorkCenterName(deployment.getWorkCenterName());
            operation.setSequence(deployment.getSequence());
            operation.setOperationDescription(deployment.getOperationDescription());
            operation.setSetupTime(deployment.getSetupTime());
            operation.setProcessingTime(deployment.getProcessingTime());
            
            // Calculate total time
            double totalTime = (deployment.getSetupTime() != null ? deployment.getSetupTime() : 0) + 
                             (deployment.getProcessingTime() != null ? deployment.getProcessingTime() : 0) * mpsRecord.getQuantity();
            operation.setTotalTime(totalTime);
            totalProductionTime += totalTime;
            
            // Calculate planned dates (simplified - assuming operations happen sequentially)
            operation.setPlannedStartDate(startDate);
            operation.setPlannedEndDate(startDate.plusDays(1)); // Simplified: 1 day per operation
            
            operation.setRequiredTools(deployment.getRequiredTools());
            operation.setRequiredSkills(deployment.getRequiredSkills());
            
            operations.add(operation);
            
            // Update completion date
            completionDate = operation.getPlannedEndDate();
        }

        ProductionPlan productionPlan = new ProductionPlan();
        productionPlan.setOperations(operations);
        productionPlan.setTotalProductionTime(totalProductionTime);
        productionPlan.setStartDate(startDate);
        productionPlan.setCompletionDate(completionDate);

        return productionPlan;
    }

    private PlanResponse buildPlanResponse(MpsRecord mpsRecord, ProcurementPlan procurementPlan, ProductionPlan productionPlan) {
        PlanResponse response = new PlanResponse();
        
        // Build MPS response
        MpsResponse mpsResponse = new MpsResponse();
        mpsResponse.setId(mpsRecord.getId());
        mpsResponse.setProductCode(mpsRecord.getProductCode());
        mpsResponse.setProductName(mpsRecord.getProductName());
        mpsResponse.setQuantity(mpsRecord.getQuantity());
        mpsResponse.setPlannedDate(mpsRecord.getPlannedDate());
        mpsResponse.setDueDate(mpsRecord.getDueDate());
        mpsResponse.setStatus(mpsRecord.getStatus());
        mpsResponse.setRemarks(mpsRecord.getRemarks());
        
        // Build BOM items response
        if (mpsRecord.getBomItems() != null) {
            List<BomItemResponse> bomResponses = mpsRecord.getBomItems().stream()
                    .map(bom -> {
                        BomItemResponse bomResponse = new BomItemResponse();
                        bomResponse.setId(bom.getId());
                        bomResponse.setMaterialCode(bom.getMaterialCode());
                        bomResponse.setMaterialName(bom.getMaterialName());
                        bomResponse.setMaterialType(bom.getMaterialType());
                        bomResponse.setQuantityPerUnit(bom.getQuantityPerUnit());
                        bomResponse.setUnit(bom.getUnit());
                        bomResponse.setSupplier(bom.getSupplier());
                        bomResponse.setLeadTime(bom.getLeadTime());
                        bomResponse.setCostPerUnit(bom.getCostPerUnit());
                        bomResponse.setRemarks(bom.getRemarks());
                        return bomResponse;
                    })
                    .collect(Collectors.toList());
            mpsResponse.setBomItems(bomResponses);
        }
        
        // Build inventory response
        if (mpsRecord.getInventoryRecords() != null) {
            List<InventoryRecordResponse> invResponses = mpsRecord.getInventoryRecords().stream()
                    .map(inv -> {
                        InventoryRecordResponse invResponse = new InventoryRecordResponse();
                        invResponse.setId(inv.getId());
                        invResponse.setMaterialCode(inv.getMaterialCode());
                        invResponse.setMaterialName(inv.getMaterialName());
                        invResponse.setCurrentStock(inv.getCurrentStock());
                        invResponse.setSafetyStock(inv.getSafetyStock());
                        invResponse.setUnit(inv.getUnit());
                        invResponse.setWarehouseLocation(inv.getWarehouseLocation());
                        invResponse.setRemarks(inv.getRemarks());
                        return invResponse;
                    })
                    .collect(Collectors.toList());
            mpsResponse.setInventoryRecords(invResponses);
        }
        
        // Build deployment response
        if (mpsRecord.getDeploymentStructures() != null) {
            List<DeploymentStructureResponse> depResponses = mpsRecord.getDeploymentStructures().stream()
                    .map(dep -> {
                        DeploymentStructureResponse depResponse = new DeploymentStructureResponse();
                        depResponse.setId(dep.getId());
                        depResponse.setWorkCenterCode(dep.getWorkCenterCode());
                        depResponse.setWorkCenterName(dep.getWorkCenterName());
                        depResponse.setSequence(dep.getSequence());
                        depResponse.setProcessingTime(dep.getProcessingTime());
                        depResponse.setOperationDescription(dep.getOperationDescription());
                        depResponse.setRequiredTools(dep.getRequiredTools());
                        depResponse.setRequiredSkills(dep.getRequiredSkills());
                        depResponse.setSetupTime(dep.getSetupTime());
                        depResponse.setRemarks(dep.getRemarks());
                        return depResponse;
                    })
                    .collect(Collectors.toList());
            mpsResponse.setDeploymentStructures(depResponses);
        }
        
        response.setMpsRecord(mpsResponse);
        response.setProcurementPlan(procurementPlan);
        response.setProductionPlan(productionPlan);
        
        return response;
    }
}

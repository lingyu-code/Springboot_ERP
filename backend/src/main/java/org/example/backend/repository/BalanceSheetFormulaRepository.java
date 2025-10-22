package org.example.backend.repository;

import org.example.backend.entity.BalanceSheetFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceSheetFormulaRepository extends JpaRepository<BalanceSheetFormula, Long> {
    
    Optional<BalanceSheetFormula> findByVariableName(String variableName);
    
    List<BalanceSheetFormula> findByAssetClass(String assetClass);
    
    List<BalanceSheetFormula> findByCategory(String category);
    
    List<BalanceSheetFormula> findByIsActiveTrue();
    
    @Query("SELECT f FROM BalanceSheetFormula f WHERE f.variableName LIKE %:keyword% OR f.description LIKE %:keyword%")
    List<BalanceSheetFormula> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT f FROM BalanceSheetFormula f WHERE f.assetClass = :assetClass AND f.isActive = true ORDER BY f.calculationOrder ASC")
    List<BalanceSheetFormula> findByAssetClassAndActiveOrdered(@Param("assetClass") String assetClass);
}

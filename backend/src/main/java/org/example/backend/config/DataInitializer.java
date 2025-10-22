package org.example.backend.config;

import org.example.backend.entity.BalanceSheetFormula;
import org.example.backend.repository.BalanceSheetFormulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private BalanceSheetFormulaRepository formulaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no formulas exist
        if (formulaRepository.count() == 0) {
            initializeSampleFormulas();
        }
    }

    private void initializeSampleFormulas() {
        List<BalanceSheetFormula> formulas = Arrays.asList(
            // Current Assets
            createFormula("cash_and_equivalents", "Current Assets", "Cash + Bank Deposits + Marketable Securities", "Total cash and cash equivalents", "ASSET", 1),
            createFormula("accounts_receivable", "Current Assets", "Sum of all customer receivables - Allowance for doubtful accounts", "Net accounts receivable", "ASSET", 2),
            createFormula("inventory", "Current Assets", "Raw Materials + Work in Progress + Finished Goods", "Total inventory value", "ASSET", 3),
            createFormula("prepaid_expenses", "Current Assets", "Insurance Prepaid + Rent Prepaid + Other Prepayments", "Total prepaid expenses", "ASSET", 4),
            createFormula("current_assets", "Current Assets", "cash_and_equivalents + accounts_receivable + inventory + prepaid_expenses", "Total current assets", "ASSET", 5),

            // Fixed Assets
            createFormula("property_plant_equipment", "Fixed Assets", "Land + Buildings + Machinery + Equipment", "Gross property, plant and equipment", "ASSET", 6),
            createFormula("accumulated_depreciation", "Fixed Assets", "Sum of all depreciation expenses to date", "Total accumulated depreciation", "ASSET", 7),
            createFormula("net_ppe", "Fixed Assets", "property_plant_equipment - accumulated_depreciation", "Net property, plant and equipment", "ASSET", 8),
            createFormula("intangible_assets", "Fixed Assets", "Goodwill + Patents + Trademarks + Copyrights", "Total intangible assets", "ASSET", 9),
            createFormula("fixed_assets", "Fixed Assets", "net_ppe + intangible_assets", "Total fixed assets", "ASSET", 10),

            // Total Assets
            createFormula("total_assets", "Total Assets", "current_assets + fixed_assets", "Total assets", "ASSET", 11),

            // Current Liabilities
            createFormula("accounts_payable", "Current Liabilities", "Sum of all supplier payables", "Total accounts payable", "LIABILITY", 12),
            createFormula("short_term_debt", "Current Liabilities", "Bank Loans (current portion) + Commercial Paper", "Total short-term debt", "LIABILITY", 13),
            createFormula("accrued_expenses", "Current Liabilities", "Wages Payable + Taxes Payable + Interest Payable", "Total accrued expenses", "LIABILITY", 14),
            createFormula("current_liabilities", "Current Liabilities", "accounts_payable + short_term_debt + accrued_expenses", "Total current liabilities", "LIABILITY", 15),

            // Long-term Liabilities
            createFormula("long_term_debt", "Long-term Liabilities", "Bonds Payable + Long-term Loans", "Total long-term debt", "LIABILITY", 16),
            createFormula("deferred_tax_liability", "Long-term Liabilities", "Temporary differences * Tax Rate", "Deferred tax liability", "LIABILITY", 17),
            createFormula("long_term_liabilities", "Long-term Liabilities", "long_term_debt + deferred_tax_liability", "Total long-term liabilities", "LIABILITY", 18),

            // Total Liabilities
            createFormula("total_liabilities", "Total Liabilities", "current_liabilities + long_term_liabilities", "Total liabilities", "LIABILITY", 19),

            // Equity
            createFormula("common_stock", "Equity", "Number of Shares * Par Value", "Common stock at par value", "EQUITY", 20),
            createFormula("additional_paid_in_capital", "Equity", "Total Capital - Common Stock", "Additional paid-in capital", "EQUITY", 21),
            createFormula("retained_earnings", "Equity", "Beginning RE + Net Income - Dividends", "Retained earnings", "EQUITY", 22),
            createFormula("treasury_stock", "Equity", "Cost of repurchased shares", "Treasury stock", "EQUITY", 23),
            createFormula("total_equity", "Equity", "common_stock + additional_paid_in_capital + retained_earnings - treasury_stock", "Total equity", "EQUITY", 24),

            // Balance Sheet Equation
            createFormula("balance_sheet_equation", "Balance Sheet", "total_assets = total_liabilities + total_equity", "Fundamental accounting equation", "EQUATION", 25)
        );

        formulaRepository.saveAll(formulas);
    }

    private BalanceSheetFormula createFormula(String variableName, String assetClass, String formula, String description, String category, Integer order) {
        BalanceSheetFormula formulaEntity = new BalanceSheetFormula();
        formulaEntity.setVariableName(variableName);
        formulaEntity.setAssetClass(assetClass);
        formulaEntity.setFormula(formula);
        formulaEntity.setDescription(description);
        formulaEntity.setCategory(category);
        formulaEntity.setCalculationOrder(order);
        formulaEntity.setIsActive(true);
        return formulaEntity;
    }
}

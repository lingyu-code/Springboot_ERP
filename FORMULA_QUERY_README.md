# Balance Sheet Formula Query Functionality

This feature allows you to query balance sheet formulas by variable name, asset class, or category. The system stores formulas in a database table and provides REST API endpoints for querying them.

## API Endpoints

### 1. Query by Variable Name
```
GET /api/formulas/variable/{variableName}
```
Example: `GET /api/formulas/variable/cash_and_equivalents`

### 2. Query by Asset Class
```
GET /api/formulas/asset-class/{assetClass}
```
Example: `GET /api/formulas/asset-class/Current Assets`

### 3. Query by Category
```
GET /api/formulas/category/{category}
```
Example: `GET /api/formulas/category/ASSET`

### 4. Search by Keyword
```
GET /api/formulas/search?keyword={keyword}
```
Example: `GET /api/formulas/search?keyword=assets`

### 5. Get All Active Formulas
```
GET /api/formulas/active
```

### 6. Flexible Query (POST)
```
POST /api/formulas/query
```
Request Body:
```json
{
  "variableName": "cash_and_equivalents",
  "assetClass": "Current Assets",
  "category": "ASSET"
}
```

## Sample Formulas Included

The system comes pre-loaded with common balance sheet formulas:

### Current Assets
- `cash_and_equivalents`: Cash + Bank Deposits + Marketable Securities
- `accounts_receivable`: Sum of all customer receivables - Allowance for doubtful accounts
- `inventory`: Raw Materials + Work in Progress + Finished Goods
- `current_assets`: cash_and_equivalents + accounts_receivable + inventory + prepaid_expenses

### Fixed Assets
- `property_plant_equipment`: Land + Buildings + Machinery + Equipment
- `net_ppe`: property_plant_equipment - accumulated_depreciation
- `fixed_assets`: net_ppe + intangible_assets

### Liabilities
- `accounts_payable`: Sum of all supplier payables
- `current_liabilities`: accounts_payable + short_term_debt + accrued_expenses
- `total_liabilities`: current_liabilities + long_term_liabilities

### Equity
- `common_stock`: Number of Shares * Par Value
- `retained_earnings`: Beginning RE + Net Income - Dividends
- `total_equity`: common_stock + additional_paid_in_capital + retained_earnings - treasury_stock

### Balance Sheet Equation
- `balance_sheet_equation`: total_assets = total_liabilities + total_equity

## Usage Examples

### Query a specific variable:
```bash
curl -X GET "http://localhost:8080/api/formulas/variable/cash_and_equivalents"
```

### Query all formulas for an asset class:
```bash
curl -X GET "http://localhost:8080/api/formulas/asset-class/Current%20Assets"
```

### Search for formulas containing "assets":
```bash
curl -X GET "http://localhost:8080/api/formulas/search?keyword=assets"
```

## Database Schema

The formulas are stored in the `balance_sheet_formulas` table with the following structure:

- `id`: Primary key
- `variable_name`: Unique identifier for the variable
- `asset_class`: Classification (e.g., "Current Assets", "Fixed Assets")
- `formula`: The calculation formula
- `description`: Human-readable description
- `category`: ASSET, LIABILITY, EQUITY, or EQUATION
- `is_active`: Boolean flag for active/inactive formulas
- `calculation_order`: Order for calculation sequence

## Implementation Details

- **Entity**: `BalanceSheetFormula`
- **Repository**: `BalanceSheetFormulaRepository` with custom query methods
- **Service**: `FormulaService` with business logic
- **Controller**: `FormulaController` with REST endpoints
- **Data Initialization**: `DataInitializer` loads sample data on startup

The system automatically initializes with sample balance sheet formulas when the application starts for the first time.

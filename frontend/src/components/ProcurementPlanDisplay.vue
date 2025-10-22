<template>
    <div class="procurement-plan">
        <div class="plan-card">
            <div class="plan-header">
                <h2>采购计划</h2>
                <div class="plan-summary">
                    <div class="summary-item">
                        <span class="label">总成本:</span>
                        <span class="value">{{ formatCurrency(plan.totalCost) }}</span>
                    </div>
                    <div class="summary-item">
                        <span class="label">最早开始日期:</span>
                        <span class="value">{{ formatDate(plan.earliestStartDate) }}</span>
                    </div>
                    <div class="summary-item">
                        <span class="label">最晚交付日期:</span>
                        <span class="value">{{ formatDate(plan.latestDeliveryDate) }}</span>
                    </div>
                </div>
            </div>

            <div class="plan-content">
                <div v-if="plan.procurementItems && plan.procurementItems.length > 0" class="items-table">
                    <table>
                        <thead>
                            <tr>
                                <th>物料编码</th>
                                <th>物料名称</th>
                                <th>需求数量</th>
                                <th>可用库存</th>
                                <th>净需求</th>
                                <th>单位</th>
                                <th>供应商</th>
                                <th>提前期(天)</th>
                                <th>下单日期</th>
                                <th>交付日期</th>
                                <th>成本</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in plan.procurementItems" :key="index">
                                <td>{{ item.materialCode }}</td>
                                <td>{{ item.materialName }}</td>
                                <td>{{ formatNumber(item.requiredQuantity) }}</td>
                                <td>{{ formatNumber(item.availableStock) }}</td>
                                <td class="net-requirement">{{ formatNumber(item.netRequirement) }}</td>
                                <td>{{ item.unit }}</td>
                                <td>{{ item.supplier || '-' }}</td>
                                <td>{{ formatNumber(item.leadTime) }}</td>
                                <td>{{ formatDate(item.orderDate) }}</td>
                                <td>{{ formatDate(item.deliveryDate) }}</td>
                                <td class="cost">{{ formatCurrency(item.cost) }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div v-else class="no-data">
                    <p>暂无采购需求</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
defineProps({
    plan: {
        type: Object,
        required: true
    }
})

const formatCurrency = (value) => {
    if (value === null || value === undefined) return '-'
    return `¥${value.toFixed(2)}`
}

const formatNumber = (value) => {
    if (value === null || value === undefined) return '-'
    return value.toFixed(2)
}

const formatDate = (dateString) => {
    if (!dateString) return '-'
    const date = new Date(dateString)
    return date.toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.procurement-plan {
    width: 100%;
}

.plan-card {
    background: white;
    border-radius: 12px;
    padding: 1.5rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    border: 1px solid #e1e5e9;
}

.plan-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
    padding-bottom: 1rem;
    border-bottom: 2px solid #28a745;
}

.plan-header h2 {
    color: #28a745;
    font-size: 1.5rem;
    margin: 0;
}

.plan-summary {
    display: flex;
    gap: 2rem;
}

.summary-item {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.summary-item .label {
    font-size: 0.8rem;
    color: #666;
    margin-bottom: 0.25rem;
}

.summary-item .value {
    font-weight: 600;
    color: #333;
    font-size: 1rem;
}

.items-table {
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.9rem;
}

th {
    background: #f8f9fa;
    padding: 0.75rem;
    text-align: left;
    font-weight: 600;
    color: #495057;
    border-bottom: 2px solid #dee2e6;
}

td {
    padding: 0.75rem;
    border-bottom: 1px solid #dee2e6;
    color: #495057;
}

tr:hover {
    background-color: #f8f9fa;
}

.net-requirement {
    font-weight: 600;
    color: #dc3545;
}

.cost {
    font-weight: 600;
    color: #28a745;
}

.no-data {
    text-align: center;
    padding: 3rem;
    color: #6c757d;
}

.no-data p {
    font-size: 1.1rem;
    margin: 0;
}

@media (max-width: 768px) {
    .plan-header {
        flex-direction: column;
        gap: 1rem;
        align-items: flex-start;
    }

    .plan-summary {
        width: 100%;
        justify-content: space-between;
    }

    .summary-item {
        align-items: flex-start;
    }

    table {
        font-size: 0.8rem;
    }

    th,
    td {
        padding: 0.5rem;
    }
}
</style>

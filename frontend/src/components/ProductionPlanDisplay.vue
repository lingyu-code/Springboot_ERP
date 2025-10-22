<template>
    <div class="production-plan">
        <div class="plan-card">
            <div class="plan-header">
                <h2>生产计划</h2>
                <div class="plan-summary">
                    <div class="summary-item">
                        <span class="label">总生产时间:</span>
                        <span class="value">{{ formatTime(plan.totalProductionTime) }}</span>
                    </div>
                    <div class="summary-item">
                        <span class="label">开始日期:</span>
                        <span class="value">{{ formatDate(plan.startDate) }}</span>
                    </div>
                    <div class="summary-item">
                        <span class="label">完成日期:</span>
                        <span class="value">{{ formatDate(plan.completionDate) }}</span>
                    </div>
                </div>
            </div>

            <div class="plan-content">
                <div v-if="plan.operations && plan.operations.length > 0" class="operations-list">
                    <div v-for="(operation, index) in plan.operations" :key="index" class="operation-item">
                        <div class="operation-header">
                            <div class="operation-info">
                                <h3 class="operation-title">
                                    {{ operation.sequence }}. {{ operation.operationDescription }}
                                </h3>
                                <div class="operation-details">
                                    <span class="work-center">{{ operation.workCenterName }} ({{
                                        operation.workCenterCode }})</span>
                                </div>
                            </div>
                            <div class="operation-timing">
                                <div class="time-info">
                                    <span class="time-label">准备时间:</span>
                                    <span class="time-value">{{ formatTime(operation.setupTime) }}</span>
                                </div>
                                <div class="time-info">
                                    <span class="time-label">加工时间:</span>
                                    <span class="time-value">{{ formatTime(operation.processingTime) }}/件</span>
                                </div>
                                <div class="time-info">
                                    <span class="time-label">总时间:</span>
                                    <span class="time-value total">{{ formatTime(operation.totalTime) }}</span>
                                </div>
                            </div>
                        </div>

                        <div class="operation-dates">
                            <div class="date-range">
                                <span class="date-label">计划开始:</span>
                                <span class="date-value">{{ formatDate(operation.plannedStartDate) }}</span>
                            </div>
                            <div class="date-range">
                                <span class="date-label">计划结束:</span>
                                <span class="date-value">{{ formatDate(operation.plannedEndDate) }}</span>
                            </div>
                        </div>

                        <div v-if="operation.requiredTools || operation.requiredSkills" class="operation-requirements">
                            <div v-if="operation.requiredTools" class="requirement">
                                <span class="requirement-label">所需工具:</span>
                                <span class="requirement-value">{{ operation.requiredTools }}</span>
                            </div>
                            <div v-if="operation.requiredSkills" class="requirement">
                                <span class="requirement-label">所需技能:</span>
                                <span class="requirement-value">{{ operation.requiredSkills }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-else class="no-data">
                    <p>暂无生产工序</p>
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

const formatTime = (hours) => {
    if (hours === null || hours === undefined) return '-'
    if (hours < 1) {
        const minutes = Math.round(hours * 60)
        return `${minutes}分钟`
    } else if (hours === 1) {
        return '1小时'
    } else {
        return `${hours.toFixed(1)}小时`
    }
}

const formatDate = (dateString) => {
    if (!dateString) return '-'
    const date = new Date(dateString)
    return date.toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.production-plan {
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
    border-bottom: 2px solid #007bff;
}

.plan-header h2 {
    color: #007bff;
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

.operations-list {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}

.operation-item {
    background: #f8f9fa;
    border: 1px solid #e1e5e9;
    border-radius: 8px;
    padding: 1.5rem;
    transition: box-shadow 0.3s, transform 0.2s;
}

.operation-item:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
}

.operation-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 1rem;
}

.operation-info {
    flex: 1;
}

.operation-title {
    color: #333;
    font-size: 1.2rem;
    margin: 0 0 0.5rem 0;
    font-weight: 600;
}

.operation-details {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.work-center {
    background: #e9ecef;
    color: #495057;
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.85rem;
    font-weight: 500;
}

.operation-timing {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    min-width: 150px;
}

.time-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.time-label {
    font-size: 0.85rem;
    color: #666;
}

.time-value {
    font-weight: 600;
    color: #333;
}

.time-value.total {
    color: #007bff;
    font-size: 1rem;
}

.operation-dates {
    display: flex;
    gap: 2rem;
    margin-bottom: 1rem;
    padding: 1rem;
    background: white;
    border-radius: 6px;
    border: 1px solid #dee2e6;
}

.date-range {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.date-label {
    font-size: 0.8rem;
    color: #666;
    margin-bottom: 0.25rem;
}

.date-value {
    font-weight: 600;
    color: #333;
}

.operation-requirements {
    display: flex;
    gap: 2rem;
    padding-top: 1rem;
    border-top: 1px solid #dee2e6;
}

.requirement {
    display: flex;
    flex-direction: column;
}

.requirement-label {
    font-size: 0.8rem;
    color: #666;
    margin-bottom: 0.25rem;
}

.requirement-value {
    font-weight: 500;
    color: #333;
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

    .operation-header {
        flex-direction: column;
        gap: 1rem;
    }

    .operation-timing {
        width: 100%;
        flex-direction: row;
        justify-content: space-between;
    }

    .operation-dates {
        flex-direction: column;
        gap: 1rem;
    }

    .operation-requirements {
        flex-direction: column;
        gap: 1rem;
    }

    .date-range {
        align-items: flex-start;
    }
}
</style>

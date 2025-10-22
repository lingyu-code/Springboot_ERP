<template>
    <div class="mps-input-form">
        <div class="form-card">
            <h2>MPS 记录输入</h2>

            <form @submit.prevent="submitForm" class="form">
                <!-- Basic Product Information -->
                <div class="form-section">
                    <h3>产品基本信息</h3>
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="productCode">产品编码 *</label>
                            <input id="productCode" v-model="formData.productCode" type="text" required
                                placeholder="请输入产品编码" />
                        </div>
                        <div class="form-group">
                            <label for="productName">产品名称 *</label>
                            <input id="productName" v-model="formData.productName" type="text" required
                                placeholder="请输入产品名称" />
                        </div>
                        <div class="form-group">
                            <label for="quantity">生产数量 *</label>
                            <input id="quantity" v-model.number="formData.quantity" type="number" required min="1"
                                placeholder="请输入生产数量" />
                        </div>
                        <div class="form-group">
                            <label for="plannedDate">计划日期 *</label>
                            <input id="plannedDate" v-model="formData.plannedDate" type="date" required />
                        </div>
                        <div class="form-group">
                            <label for="dueDate">交付日期 *</label>
                            <input id="dueDate" v-model="formData.dueDate" type="date" required />
                        </div>
                    </div>
                </div>

                <!-- BOM Items -->
                <div class="form-section">
                    <div class="section-header">
                        <h3>物料清单 (BOM)</h3>
                        <button type="button" @click="addBomItem" class="btn-add">+ 添加物料</button>
                    </div>
                    <div v-for="(item, index) in formData.bomItems" :key="index" class="dynamic-item">
                        <div class="form-grid">
                            <div class="form-group">
                                <label>物料编码</label>
                                <input v-model="item.materialCode" placeholder="物料编码" />
                            </div>
                            <div class="form-group">
                                <label>物料名称</label>
                                <input v-model="item.materialName" placeholder="物料名称" />
                            </div>
                            <div class="form-group">
                                <label>物料类型</label>
                                <select v-model="item.materialType">
                                    <option value="RAW">原材料</option>
                                    <option value="COMPONENT">组件</option>
                                    <option value="SUBASSEMBLY">子装配件</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>单位用量</label>
                                <input v-model.number="item.quantityPerUnit" type="number" step="0.01"
                                    placeholder="0.00" />
                            </div>
                            <div class="form-group">
                                <label>单位</label>
                                <input v-model="item.unit" placeholder="单位" />
                            </div>
                            <div class="form-group">
                                <label>供应商</label>
                                <input v-model="item.supplier" placeholder="供应商" />
                            </div>
                            <div class="form-group">
                                <label>提前期(天)</label>
                                <input v-model.number="item.leadTime" type="number" step="0.1" placeholder="0.0" />
                            </div>
                            <div class="form-group">
                                <label>单价</label>
                                <input v-model.number="item.costPerUnit" type="number" step="0.01" placeholder="0.00" />
                            </div>
                        </div>
                        <button type="button" @click="removeBomItem(index)" class="btn-remove">删除</button>
                    </div>
                </div>

                <!-- Inventory Records -->
                <div class="form-section">
                    <div class="section-header">
                        <h3>库存信息</h3>
                        <button type="button" @click="addInventoryItem" class="btn-add">+ 添加库存</button>
                    </div>
                    <div v-for="(item, index) in formData.inventoryRecords" :key="index" class="dynamic-item">
                        <div class="form-grid">
                            <div class="form-group">
                                <label>物料编码</label>
                                <input v-model="item.materialCode" placeholder="物料编码" />
                            </div>
                            <div class="form-group">
                                <label>物料名称</label>
                                <input v-model="item.materialName" placeholder="物料名称" />
                            </div>
                            <div class="form-group">
                                <label>当前库存</label>
                                <input v-model.number="item.currentStock" type="number" step="0.01"
                                    placeholder="0.00" />
                            </div>
                            <div class="form-group">
                                <label>安全库存</label>
                                <input v-model.number="item.safetyStock" type="number" step="0.01" placeholder="0.00" />
                            </div>
                            <div class="form-group">
                                <label>单位</label>
                                <input v-model="item.unit" placeholder="单位" />
                            </div>
                            <div class="form-group">
                                <label>仓库位置</label>
                                <input v-model="item.warehouseLocation" placeholder="仓库位置" />
                            </div>
                        </div>
                        <button type="button" @click="removeInventoryItem(index)" class="btn-remove">删除</button>
                    </div>
                </div>

                <!-- Deployment Structure -->
                <div class="form-section">
                    <div class="section-header">
                        <h3>生产部署结构</h3>
                        <button type="button" @click="addDeploymentItem" class="btn-add">+ 添加工序</button>
                    </div>
                    <div v-for="(item, index) in formData.deploymentStructures" :key="index" class="dynamic-item">
                        <div class="form-grid">
                            <div class="form-group">
                                <label>工作中心编码</label>
                                <input v-model="item.workCenterCode" placeholder="工作中心编码" />
                            </div>
                            <div class="form-group">
                                <label>工作中心名称</label>
                                <input v-model="item.workCenterName" placeholder="工作中心名称" />
                            </div>
                            <div class="form-group">
                                <label>工序顺序</label>
                                <input v-model.number="item.sequence" type="number" placeholder="1" />
                            </div>
                            <div class="form-group">
                                <label>加工时间(小时/件)</label>
                                <input v-model.number="item.processingTime" type="number" step="0.01"
                                    placeholder="0.00" />
                            </div>
                            <div class="form-group">
                                <label>工序描述</label>
                                <input v-model="item.operationDescription" placeholder="工序描述" />
                            </div>
                            <div class="form-group">
                                <label>所需工具</label>
                                <input v-model="item.requiredTools" placeholder="所需工具" />
                            </div>
                            <div class="form-group">
                                <label>所需技能</label>
                                <input v-model="item.requiredSkills" placeholder="所需技能" />
                            </div>
                            <div class="form-group">
                                <label>准备时间(小时)</label>
                                <input v-model.number="item.setupTime" type="number" step="0.01" placeholder="0.00" />
                            </div>
                        </div>
                        <button type="button" @click="removeDeploymentItem(index)" class="btn-remove">删除</button>
                    </div>
                </div>

                <!-- Remarks -->
                <div class="form-section">
                    <h3>备注</h3>
                    <div class="form-group">
                        <textarea v-model="formData.remarks" placeholder="请输入备注信息..." rows="3"></textarea>
                    </div>
                </div>

                <!-- Submit Button -->
                <div class="form-actions">
                    <button type="submit" :disabled="loading" class="btn-submit">
                        {{ loading ? '处理中...' : '生成计划' }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

const emit = defineEmits(['plans-generated'])

const loading = ref(false)

const formData = reactive({
    productCode: '',
    productName: '',
    quantity: 100,
    plannedDate: '',
    dueDate: '',
    remarks: '',
    bomItems: [],
    inventoryRecords: [],
    deploymentStructures: []
})

// Initialize with some default data for testing
const initializeDefaultData = () => {
    const today = new Date().toISOString().split('T')[0]
    const nextWeek = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]

    formData.plannedDate = today
    formData.dueDate = nextWeek

    // Add default BOM item
    addBomItem()
    formData.bomItems[0] = {
        materialCode: 'MAT001',
        materialName: '原材料A',
        materialType: 'RAW',
        quantityPerUnit: 2.5,
        unit: 'kg',
        supplier: '供应商A',
        leadTime: 5,
        costPerUnit: 10.5
    }

    // Add default inventory
    addInventoryItem()
    formData.inventoryRecords[0] = {
        materialCode: 'MAT001',
        materialName: '原材料A',
        currentStock: 100,
        safetyStock: 50,
        unit: 'kg',
        warehouseLocation: '仓库A'
    }

    // Add default deployment
    addDeploymentItem()
    formData.deploymentStructures[0] = {
        workCenterCode: 'WC001',
        workCenterName: '加工中心1',
        sequence: 1,
        processingTime: 0.5,
        operationDescription: '初步加工',
        requiredTools: '工具A',
        requiredSkills: '操作技能',
        setupTime: 1.0
    }
}

// Dynamic form item management
const addBomItem = () => {
    formData.bomItems.push({
        materialCode: '',
        materialName: '',
        materialType: 'RAW',
        quantityPerUnit: 0,
        unit: '',
        supplier: '',
        leadTime: 0,
        costPerUnit: 0,
        remarks: ''
    })
}

const removeBomItem = (index) => {
    formData.bomItems.splice(index, 1)
}

const addInventoryItem = () => {
    formData.inventoryRecords.push({
        materialCode: '',
        materialName: '',
        currentStock: 0,
        safetyStock: 0,
        unit: '',
        warehouseLocation: '',
        remarks: ''
    })
}

const removeInventoryItem = (index) => {
    formData.inventoryRecords.splice(index, 1)
}

const addDeploymentItem = () => {
    formData.deploymentStructures.push({
        workCenterCode: '',
        workCenterName: '',
        sequence: formData.deploymentStructures.length + 1,
        processingTime: 0,
        operationDescription: '',
        requiredTools: '',
        requiredSkills: '',
        setupTime: 0,
        remarks: ''
    })
}

const removeDeploymentItem = (index) => {
    formData.deploymentStructures.splice(index, 1)
}

// Form submission
const submitForm = async () => {
    loading.value = true

    try {
        const response = await fetch('http://localhost:8080/api/mps/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })

        if (response.ok) {
            const plans = await response.json()
            emit('plans-generated', plans)
        } else {
            alert('提交失败，请检查数据格式')
        }
    } catch (error) {
        console.error('Error:', error)
        alert('网络错误，请检查后端服务是否启动')
    } finally {
        loading.value = false
    }
}

// Initialize with default data
initializeDefaultData()
</script>

<style scoped>
.mps-input-form {
    width: 100%;
}

.form-card {
    background: white;
    border-radius: 12px;
    padding: 2rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.form-card h2 {
    color: #333;
    margin-bottom: 2rem;
    font-size: 1.8rem;
    border-bottom: 2px solid #667eea;
    padding-bottom: 0.5rem;
}

.form-section {
    margin-bottom: 2rem;
    padding: 1.5rem;
    border: 1px solid #e1e5e9;
    border-radius: 8px;
    background: #fafbfc;
}

.form-section h3 {
    color: #555;
    margin-bottom: 1rem;
    font-size: 1.3rem;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
}

.form-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    margin-bottom: 1rem;
}

.form-group {
    display: flex;
    flex-direction: column;
}

.form-group label {
    font-weight: 600;
    margin-bottom: 0.5rem;
    color: #555;
    font-size: 0.9rem;
}

.form-group input,
.form-group select,
.form-group textarea {
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 6px;
    font-size: 0.95rem;
    transition: border-color 0.3s, box-shadow 0.3s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.dynamic-item {
    background: white;
    padding: 1.5rem;
    border: 1px solid #e1e5e9;
    border-radius: 8px;
    margin-bottom: 1rem;
    position: relative;
}

.btn-add {
    background: #28a745;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    transition: background-color 0.3s;
}

.btn-add:hover {
    background: #218838;
}

.btn-remove {
    background: #dc3545;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    margin-top: 1rem;
    transition: background-color 0.3s;
}

.btn-remove:hover {
    background: #c82333;
}

.btn-submit {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    padding: 1rem 2rem;
    border-radius: 8px;
    font-size: 1.1rem;
    font-weight: 600;
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
    width: 100%;
}

.btn-submit:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.3);
}

.btn-submit:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.form-actions {
    margin-top: 2rem;
}

@media (max-width: 768px) {
    .form-grid {
        grid-template-columns: 1fr;
    }

    .section-header {
        flex-direction: column;
        gap: 1rem;
        align-items: flex-start;
    }

    .form-card {
        padding: 1rem;
    }
}
</style>

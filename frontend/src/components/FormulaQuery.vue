<template>
    <div class="formula-query">
        <div class="query-header">
            <h2>资产负债表公式查询</h2>
            <p>输入变量名称，查询对应的计算公式</p>
        </div>

        <div class="search-section">
            <div class="search-input-group">
                <label for="variable-input">变量名称:</label>
                <input id="variable-input" v-model="variableInput" type="text" placeholder="请输入变量名称..."
                    @keyup.enter="searchFormula" class="search-input" />
                <button @click="searchFormula" class="search-button">查询</button>
            </div>
        </div>

        <div v-if="loading" class="loading">
            正在查询中...
        </div>

        <div v-if="error" class="error-message">
            {{ error }}
        </div>

        <div v-if="formulaResult" class="result-section">
            <h3>查询结果</h3>
            <div class="formula-card">
                <div class="formula-item">
                    <strong>变量名称:</strong> {{ formulaResult.variableName }}
                </div>
                <div class="formula-item">
                    <strong>资产类别:</strong> {{ formulaResult.assetClass }}
                </div>
                <div class="formula-item">
                    <strong>计算公式:</strong> {{ formulaResult.formula }}
                </div>
                <div v-if="formulaResult.description" class="formula-item">
                    <strong>描述:</strong> {{ formulaResult.description }}
                </div>
            </div>
        </div>

        <div class="formula-table-section">
            <h3>所有公式列表</h3>
            <div class="table-container">
                <table class="formula-table">
                    <thead>
                        <tr>
                            <th>序号</th>
                            <th>资产类别</th>
                            <th>变量名称</th>
                            <th>计算公式</th>
                            <th>描述</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(formula, index) in allFormulas" :key="formula.id">
                            <td>{{ index + 1 }}</td>
                            <td>{{ formula.assetClass }}</td>
                            <td>{{ formula.variableName }}</td>
                            <td>{{ formula.formula }}</td>
                            <td>{{ formula.description }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const variableInput = ref('')
const formulaResult = ref(null)
const allFormulas = ref([])
const loading = ref(false)
const error = ref('')

const searchFormula = async () => {
    if (!variableInput.value.trim()) {
        error.value = '请输入变量名称'
        return
    }

    loading.value = true
    error.value = ''
    formulaResult.value = null

    try {
        const response = await fetch(`http://localhost:8080/api/formulas/variable/${encodeURIComponent(variableInput.value)}`)
        if (response.ok) {
            const data = await response.json()
            formulaResult.value = data
        } else if (response.status === 404) {
            error.value = '未找到该变量对应的公式'
        } else {
            error.value = '查询失败，请稍后重试'
        }
    } catch (err) {
        error.value = '网络错误，请检查后端服务是否启动'
        console.error('Error fetching formula:', err)
    } finally {
        loading.value = false
    }
}

const loadAllFormulas = async () => {
    try {
        const response = await fetch('http://localhost:8080/api/formulas/active')
        if (response.ok) {
            allFormulas.value = await response.json()
        }
    } catch (err) {
        console.error('Error loading all formulas:', err)
    }
}

onMounted(() => {
    loadAllFormulas()
})
</script>

<style scoped>
.formula-query {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem;
}

.query-header {
    text-align: center;
    margin-bottom: 2rem;
}

.query-header h2 {
    color: #2c3e50;
    font-size: 2rem;
    margin-bottom: 0.5rem;
}

.query-header p {
    color: #7f8c8d;
    font-size: 1.1rem;
}

.search-section {
    background: white;
    padding: 1.5rem;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    margin-bottom: 2rem;
}

.search-input-group {
    display: flex;
    align-items: center;
    gap: 1rem;
    flex-wrap: wrap;
}

.search-input-group label {
    font-weight: 600;
    color: #2c3e50;
    min-width: 80px;
}

.search-input {
    flex: 1;
    min-width: 200px;
    padding: 0.75rem;
    border: 2px solid #e1e8ed;
    border-radius: 6px;
    font-size: 1rem;
    transition: border-color 0.3s ease;
}

.search-input:focus {
    outline: none;
    border-color: #3498db;
}

.search-button {
    background: linear-gradient(135deg, #3498db, #2980b9);
    color: white;
    border: none;
    padding: 0.75rem 1.5rem;
    border-radius: 6px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s ease;
}

.search-button:hover {
    background: linear-gradient(135deg, #2980b9, #1f6391);
    transform: translateY(-1px);
}

.loading {
    text-align: center;
    padding: 2rem;
    color: #3498db;
    font-size: 1.1rem;
}

.error-message {
    background: #e74c3c;
    color: white;
    padding: 1rem;
    border-radius: 6px;
    margin-bottom: 1rem;
    text-align: center;
}

.result-section {
    background: white;
    padding: 1.5rem;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    margin-bottom: 2rem;
}

.result-section h3 {
    color: #2c3e50;
    margin-bottom: 1rem;
    border-bottom: 2px solid #3498db;
    padding-bottom: 0.5rem;
}

.formula-card {
    background: #f8f9fa;
    padding: 1.5rem;
    border-radius: 6px;
    border-left: 4px solid #3498db;
}

.formula-item {
    margin-bottom: 0.75rem;
    line-height: 1.6;
}

.formula-item:last-child {
    margin-bottom: 0;
}

.formula-item strong {
    color: #2c3e50;
    min-width: 80px;
    display: inline-block;
}

.formula-table-section {
    background: white;
    padding: 1.5rem;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.formula-table-section h3 {
    color: #2c3e50;
    margin-bottom: 1rem;
    border-bottom: 2px solid #3498db;
    padding-bottom: 0.5rem;
}

.table-container {
    overflow-x: auto;
}

.formula-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
}

.formula-table th,
.formula-table td {
    padding: 0.75rem;
    text-align: left;
    border-bottom: 1px solid #e1e8ed;
}

.formula-table th {
    background: #f8f9fa;
    font-weight: 600;
    color: #2c3e50;
    position: sticky;
    top: 0;
}

.formula-table tr:hover {
    background: #f8f9fa;
}

.formula-table td:first-child {
    font-weight: 600;
    color: #3498db;
}

@media (max-width: 768px) {
    .formula-query {
        padding: 1rem;
    }

    .search-input-group {
        flex-direction: column;
        align-items: stretch;
    }

    .search-input-group label {
        margin-bottom: 0.5rem;
    }

    .search-button {
        width: 100%;
    }

    .formula-table {
        font-size: 0.9rem;
    }

    .formula-table th,
    .formula-table td {
        padding: 0.5rem;
    }
}
</style>

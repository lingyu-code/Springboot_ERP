<template>
  <div id="app">
    <header class="app-header">
      <h1>ERP 系统</h1>
      <nav class="app-nav">
        <button :class="['nav-button', { active: currentPage === 'mps' }]" @click="currentPage = 'mps'">
          主生产计划
        </button>
        <button :class="['nav-button', { active: currentPage === 'formula' }]" @click="currentPage = 'formula'">
          公式查询
        </button>
      </nav>
    </header>

    <main class="app-main">
      <div v-if="currentPage === 'mps'" class="page-content">
        <div class="page-header">
          <h2>MPS 系统 - 主生产计划管理系统</h2>
          <p>输入MPS记录，生成采购计划和生产计划</p>
        </div>

        <MpsInputForm @plans-generated="handlePlansGenerated" />

        <div v-if="plans" class="plans-container">
          <ProcurementPlanDisplay :plan="plans.procurementPlan" />
          <ProductionPlanDisplay :plan="plans.productionPlan" />
        </div>
      </div>

      <div v-if="currentPage === 'formula'" class="page-content">
        <FormulaQuery />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import MpsInputForm from './components/MpsInputForm.vue'
import ProcurementPlanDisplay from './components/ProcurementPlanDisplay.vue'
import ProductionPlanDisplay from './components/ProductionPlanDisplay.vue'
import FormulaQuery from './components/FormulaQuery.vue'

const plans = ref(null)
const currentPage = ref('mps')

const handlePlansGenerated = (generatedPlans) => {
  plans.value = generatedPlans
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f5f5f5;
  color: #333;
}

#app {
  min-height: 100vh;
}

.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 2rem;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.app-header h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  font-weight: 600;
}

.app-nav {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 1rem;
}

.nav-button {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.nav-button:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.nav-button.active {
  background: rgba(255, 255, 255, 0.4);
  border-color: rgba(255, 255, 255, 0.6);
  font-weight: 600;
}

.app-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.plans-container {
  margin-top: 3rem;
  display: grid;
  gap: 2rem;
  grid-template-columns: 1fr 1fr;
}

@media (max-width: 768px) {
  .plans-container {
    grid-template-columns: 1fr;
  }

  .app-header h1 {
    font-size: 2rem;
  }

  .app-main {
    padding: 1rem;
  }
}
</style>

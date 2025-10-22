<template>
  <div id="app">
    <header class="app-header">
      <h1>MPS 系统 - 主生产计划管理系统</h1>
      <p>输入MPS记录，生成采购计划和生产计划</p>
    </header>

    <main class="app-main">
      <MpsInputForm @plans-generated="handlePlansGenerated" />

      <div v-if="plans" class="plans-container">
        <ProcurementPlanDisplay :plan="plans.procurementPlan" />
        <ProductionPlanDisplay :plan="plans.productionPlan" />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import MpsInputForm from './components/MpsInputForm.vue'
import ProcurementPlanDisplay from './components/ProcurementPlanDisplay.vue'
import ProductionPlanDisplay from './components/ProductionPlanDisplay.vue'

const plans = ref(null)

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
  margin-bottom: 0.5rem;
  font-weight: 600;
}

.app-header p {
  font-size: 1.1rem;
  opacity: 0.9;
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

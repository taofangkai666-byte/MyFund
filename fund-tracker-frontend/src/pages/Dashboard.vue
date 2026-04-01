<template>
  <div class="dashboard">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="heading-1">投资概览</h1>
      <p class="body-text">实时监控您的基金投资组合表现</p>
    </div>

    <!-- Statistics Cards -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-label caption">总投资</div>
          <div class="stat-value">¥{{ statistics.totalInvestment.toFixed(2) }}</div>
          <div class="stat-description">累计投入本金</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-label caption">当前市值</div>
          <div class="stat-value">¥{{ statistics.currentValue.toFixed(2) }}</div>
          <div class="stat-description">实时估值</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-label caption">总收益</div>
          <div class="stat-value" :class="{ 'positive': totalProfit >= 0, 'negative': totalProfit < 0 }">
            ¥{{ totalProfit.toFixed(2) }}
          </div>
          <div class="stat-description">
            <span :class="{ 'positive': totalProfit >= 0, 'negative': totalProfit < 0 }">
              {{ totalProfit >= 0 ? '盈利' : '亏损' }}
            </span>
          </div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-label caption">收益率</div>
          <div class="stat-value" :class="{ 'positive': profitRate >= 0, 'negative': profitRate < 0 }">
            {{ profitRate.toFixed(2) }}%
          </div>
          <div class="stat-description">投资回报率</div>
        </div>
      </div>
    </div>

    <!-- Charts Section -->
    <div class="charts-section">
      <div class="chart-container card">
        <div class="chart-header">
          <h3 class="heading-3">收益趋势</h3>
          <p class="body-text">过去30天收益变化</p>
        </div>
        <div class="chart-content">
          <ProfitChart :data="chartData" />
        </div>
      </div>
    </div>

    <!-- Holdings Overview -->
    <div class="holdings-section">
      <div class="section-header">
        <div class="section-info">
          <h3 class="heading-3">持仓概览</h3>
          <p class="body-text">您的投资组合分布</p>
        </div>
        <button class="btn" @click="router.push('/holdings')">查看全部</button>
      </div>

      <div v-if="holdings.length === 0" class="loading-state">
        <p class="body-text">加载中...</p>
      </div>

      <div class="holdings-grid" v-if="holdings.length > 0">
        <template v-for="holding in holdings.slice(0, 4)" :key="holding.id">
          <div v-if="holding.fund" class="holding-card card">
            <div class="holding-header">
              <div class="fund-info">
                <div class="fund-code">{{ holding.fund.fundCode }}</div>
                <div class="fund-name">{{ holding.fund.fundName }}</div>
              </div>
              <div class="profit-indicator" :class="holding.profitRate >= 0 ? 'positive' : 'negative'">
                {{ holding.profitRate >= 0 ? '↗' : '↘' }}
              </div>
            </div>

            <div class="holding-stats">
              <div class="stat-row">
                <span class="stat-label">市值</span>
                <span class="stat-value">¥{{ holding.currentValue.toFixed(2) }}</span>
              </div>
              <div class="stat-row">
                <span class="stat-label">收益</span>
                <span class="stat-value" :class="holding.profit >= 0 ? 'positive' : 'negative'">
                  ¥{{ holding.profit.toFixed(2) }}
                </span>
              </div>
              <div class="stat-row">
                <span class="stat-label">收益率</span>
                <span class="stat-value" :class="holding.profitRate >= 0 ? 'positive' : 'negative'">
                  {{ holding.profitRate.toFixed(2) }}%
                </span>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useFundStore } from '../stores/fund'
import { getHoldings, getProfitStatistics } from '../api/fund'
import ProfitChart from '../components/ProfitChart.vue'

const router = useRouter()
const fundStore = useFundStore()

const chartData = ref<Array<{ date: string; value: number; profit: number }>>([])
const isComponentMounted = ref(true)

const loadData = async () => {
  try {
    const [holdingsRes, statisticsRes] = await Promise.all([
      getHoldings(),
      getProfitStatistics()
    ])

    if (!isComponentMounted.value) return

    if (holdingsRes.code === 200) {
      fundStore.holdings = holdingsRes.data
    }

    if (statisticsRes.code === 200) {
      fundStore.statistics = statisticsRes.data
    }

    if (isComponentMounted.value) {
      generateChartData()
    }
  } catch (error) {
    if (isComponentMounted.value) {
      console.error('加载数据失败:', error)
    }
  }
}

onUnmounted(() => {
  isComponentMounted.value = false
})

const generateChartData = () => {
  const data = []
  const today = new Date()
  let cumulativeValue = fundStore.statistics.totalInvestment

  for (let i = 30; i >= 0; i--) {
    const date = new Date(today)
    date.setDate(date.getDate() - i)

    const randomFactor = 0.95 + Math.random() * 0.1
    cumulativeValue = fundStore.statistics.totalInvestment * randomFactor
    const profit = cumulativeValue - fundStore.statistics.totalInvestment

    data.push({
      date: date.toISOString().split('T')[0],
      value: cumulativeValue,
      profit: profit
    })
  }

  chartData.value = data
}

onMounted(() => {
  loadData()
})

const { holdings, statistics, totalProfit, profitRate } = fundStore
</script>

<style scoped>
.dashboard {
  max-width: 100%;
}

.page-header {
  margin-bottom: var(--space-2xl);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--space-lg);
  margin-bottom: var(--space-2xl);
}

.stat-card {
  background: var(--color-surface);
  border-radius: 8px;
  padding: var(--space-xl);
  transition: all 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-content {
  text-align: center;
}

.stat-label {
  margin-bottom: var(--space-sm);
}

.stat-value {
  font-size: var(--font-size-2xl);
  font-weight: 700;
  margin-bottom: var(--space-xs);
  color: var(--color-text-primary);
}

.stat-description {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.positive {
  color: var(--color-success);
}

.negative {
  color: var(--color-error);
}

.charts-section {
  margin-bottom: var(--space-2xl);
}

.chart-container {
  padding: var(--space-xl);
}

.chart-header {
  margin-bottom: var(--space-lg);
}

.chart-content {
  height: 300px;
}

.holdings-section {
  margin-bottom: var(--space-xl);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-lg);
}

.section-info {
  flex: 1;
}

.holdings-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--space-base);
}

.holding-card {
  padding: var(--space-lg);
  transition: all 0.2s ease;
}

.holding-card:hover {
  transform: translateY(-1px);
  box-shadow: var(--shadow-sm);
}

.holding-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-base);
}

.fund-info {
  flex: 1;
}

.fund-code {
  font-size: var(--font-size-lg);
  font-weight: 600;
  margin-bottom: var(--space-xs);
  color: var(--color-text-primary);
}

.fund-name {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  line-height: 1.4;
}

.profit-indicator {
  font-size: var(--font-size-xl);
  font-weight: 600;
  margin-left: var(--space-base);
}

.holding-stats {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.stat-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-xs) 0;
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.stat-value {
  font-size: var(--font-size-sm);
  font-weight: 600;
  color: var(--color-text-primary);
}

.loading-state {
  text-align: center;
  padding: var(--space-xl);
  color: var(--color-text-secondary);
}

/* Responsive design */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
    gap: var(--space-base);
  }

  .holdings-grid {
    grid-template-columns: 1fr;
  }

  .section-header {
    flex-direction: column;
    gap: var(--space-base);
  }

  .stat-value {
    font-size: var(--font-size-xl);
  }
}
</style>
<template>
  <div class="my-holdings">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="heading-1">我的持仓</h1>
      <p class="body-text">管理您的投资组合，追踪收益表现</p>
    </div>

    <!-- Portfolio Overview -->
    <div class="portfolio-overview">
      <div class="overview-grid">
        <div class="overview-card">
          <div class="card-content">
            <div class="card-label caption">总投资</div>
            <div class="card-value">¥{{ statistics.totalInvestment.toFixed(2) }}</div>
            <div class="card-description">投入本金</div>
          </div>
        </div>

        <div class="overview-card">
          <div class="card-content">
            <div class="card-label caption">当前市值</div>
            <div class="card-value">¥{{ statistics.currentValue.toFixed(2) }}</div>
            <div class="card-description">实时估值</div>
          </div>
        </div>

        <div class="overview-card">
          <div class="card-content">
            <div class="card-label caption">总收益</div>
            <div class="card-value" :class="{ 'positive': totalProfit >= 0, 'negative': totalProfit < 0 }">
              ¥{{ totalProfit.toFixed(2) }}
            </div>
            <div class="card-description">
              <span :class="{ 'positive': totalProfit >= 0, 'negative': totalProfit < 0 }">
                {{ totalProfit >= 0 ? '盈利' : '亏损' }}
              </span>
            </div>
          </div>
        </div>

        <div class="overview-card">
          <div class="card-content">
            <div class="card-label caption">收益率</div>
            <div class="card-value" :class="{ 'positive': profitRate >= 0, 'negative': profitRate < 0 }">
              {{ profitRate.toFixed(2) }}%
            </div>
            <div class="card-description">投资回报率</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Holdings Table -->
    <div class="holdings-section">
      <div class="section-header">
        <div class="section-info">
          <h3 class="heading-3">持仓明细</h3>
          <p class="body-text">{{ holdings.length }} 只持仓基金</p>
        </div>
        <button class="btn" @click="addFundFormRef?.show()">添加持仓</button>
      </div>

      <div class="holdings-container card" v-loading="loading">
        <div class="table-wrapper" v-if="isComponentMounted">
          <el-table
            :data="holdings"
            style="width: 100%"
            v-if="holdings.length > 0"
            key="holdings-table"
            :key="tableKey"
          >
            <el-table-column label="基金信息" min-width="200">
              <template #default="scope">
                <div class="fund-info-cell" v-if="scope.row.fund">
                  <div class="fund-code">{{ scope.row.fund.fundCode }}</div>
                  <div class="fund-name">{{ scope.row.fund.fundName }}</div>
                </div>
                <div v-else class="fund-info-cell">
                  <div class="fund-code">--</div>
                  <div class="fund-name">数据加载中...</div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="买入价格" width="120">
              <template #default="scope">
                <div class="buy-price">¥{{ scope.row.buyPrice?.toFixed(4) || '0.0000' }}</div>
              </template>
            </el-table-column>

            <el-table-column label="当前价格" width="120">
              <template #default="scope">
                <div class="current-price">¥{{ scope.row.currentPrice?.toFixed(4) || '0.0000' }}</div>
              </template>
            </el-table-column>

            <el-table-column label="份额" width="100">
              <template #default="scope">
                <div class="shares">{{ scope.row.shares?.toFixed(2) || '0.00' }}</div>
              </template>
            </el-table-column>

            <el-table-column label="市值" width="120">
              <template #default="scope">
                <div class="market-value">¥{{ scope.row.currentValue?.toFixed(2) || '0.00' }}</div>
              </template>
            </el-table-column>

            <el-table-column label="收益" width="120">
              <template #default="scope">
                <div class="profit-cell" :class="(scope.row.profit >= 0) ? 'positive' : 'negative'">
                  <div class="profit-amount">¥{{ scope.row.profit?.toFixed(2) || '0.00' }}</div>
                  <div class="profit-rate">{{ scope.row.profitRate?.toFixed(2) || '0.00' }}%</div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="100" fixed="right">
              <template #default="scope">
                <button class="btn-outline btn-small" @click="handleDelete(scope.row.id)">
                  删除
                </button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- Empty State -->
        <div v-if="!loading && holdings.length === 0" class="empty-state">
          <div class="empty-icon">💼</div>
          <h3 class="empty-title">暂无持仓记录</h3>
          <p class="empty-description">开始添加您的第一只基金持仓吧</p>
          <button class="btn" @click="addFundFormRef?.show()">添加持仓</button>
        </div>
      </div>
    </div>

    <AddFundForm ref="addFundFormRef" @success="loadData" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useFundStore } from '../stores/fund'
import { getHoldings, getProfitStatistics, deleteHolding } from '../api/fund'
import AddFundForm from '../components/AddFundForm.vue'

const loading = ref(false)
const addFundFormRef = ref<InstanceType<typeof AddFundForm>>()
const fundStore = useFundStore()
const isComponentMounted = ref(true)
const tableKey = ref(0)

const loadData = async () => {
  if (!isComponentMounted.value) return

  loading.value = true
  try {
    const [holdingsRes, statisticsRes] = await Promise.all([
      getHoldings(),
      getProfitStatistics()
    ])

    if (!isComponentMounted.value) return

    if (holdingsRes.code === 200) {
      // 使用 nextTick 确保数据更新在下一个渲染周期
      setTimeout(() => {
        if (isComponentMounted.value) {
          fundStore.holdings = holdingsRes.data || []
          tableKey.value++ // 强制表格重新渲染
        }
      }, 0)
    }

    if (statisticsRes.code === 200) {
      setTimeout(() => {
        if (isComponentMounted.value) {
          fundStore.statistics = statisticsRes.data
        }
      }, 0)
    }
  } catch (error) {
    if (isComponentMounted.value) {
      ElMessage.error('加载数据失败')
    }
  } finally {
    if (isComponentMounted.value) {
      loading.value = false
    }
  }
}

onUnmounted(() => {
  isComponentMounted.value = false
})

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个持仓吗？此操作不可恢复。', '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteHolding(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadData()
})

const { holdings, statistics, totalProfit, profitRate } = fundStore
</script>

<style scoped>
.my-holdings {
  max-width: 100%;
}

.page-header {
  margin-bottom: var(--space-2xl);
}

.portfolio-overview {
  margin-bottom: var(--space-2xl);
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--space-lg);
}

.overview-card {
  background: var(--color-surface);
  border-radius: 8px;
  padding: var(--space-xl);
  transition: all 0.2s ease;
}

.overview-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.card-content {
  text-align: center;
}

.card-label {
  margin-bottom: var(--space-sm);
}

.card-value {
  font-size: var(--font-size-2xl);
  font-weight: 700;
  margin-bottom: var(--space-xs);
  color: var(--color-text-primary);
}

.card-description {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.positive {
  color: var(--color-success);
}

.negative {
  color: var(--color-error);
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

.holdings-container {
  padding: var(--space-lg);
}

.table-wrapper {
  overflow-x: auto;
}

.fund-info-cell {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
}

.fund-code {
  font-size: var(--font-size-base);
  font-weight: 600;
  color: var(--color-text-primary);
}

.fund-name {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  line-height: 1.3;
}

.buy-price,
.current-price,
.shares,
.market-value {
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--color-text-primary);
}

.profit-cell {
  display: flex;
  flex-direction: column;
  gap: var(--space-xs);
}

.profit-amount {
  font-size: var(--font-size-sm);
  font-weight: 600;
}

.profit-rate {
  font-size: var(--font-size-xs);
  font-weight: 500;
}

.btn-small {
  padding: var(--space-xs) var(--space-sm);
  font-size: var(--font-size-xs);
}

.empty-state {
  text-align: center;
  padding: var(--space-2xl) var(--space-lg);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: var(--space-base);
  opacity: 0.5;
}

.empty-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  margin: 0 0 var(--space-sm) 0;
  color: var(--color-text-primary);
}

.empty-description {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  margin: 0 0 var(--space-lg) 0;
}

/* Responsive design */
@media (max-width: 768px) {
  .overview-grid {
    grid-template-columns: 1fr;
    gap: var(--space-base);
  }

  .section-header {
    flex-direction: column;
    gap: var(--space-base);
  }

  .card-value {
    font-size: var(--font-size-xl);
  }

  .holdings-container {
    padding: var(--space-base);
  }
}
</style>
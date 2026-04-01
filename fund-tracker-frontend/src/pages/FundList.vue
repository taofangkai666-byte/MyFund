<template>
  <div class="fund-list">
    <!-- Page Header -->
    <div class="page-header">
      <h1 class="heading-1">基金市场</h1>
      <p class="body-text">发现优质投资机会，管理您的基金组合</p>
    </div>

    <!-- Search Section -->
    <div class="search-section card">
      <div class="search-container">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索基金代码或名称..."
          clearable
          @clear="loadFunds"
          class="search-input"
        />
        <button class="btn" @click="loadFunds">搜索</button>
      </div>
    </div>

    <!-- Fund List -->
    <div class="funds-section">
      <div class="section-header">
        <div class="section-info">
          <h3 class="heading-3">基金列表</h3>
          <p class="body-text">{{ pagination.total }} 只基金</p>
        </div>
        <button class="btn" @click="addFundFormRef?.show()">添加持仓</button>
      </div>

      <div class="funds-grid" v-loading="loading">
        <div v-for="fund in funds" :key="fund.fundCode" class="fund-card card">
          <div class="fund-header">
            <div class="fund-basic-info">
              <h4 class="fund-code">{{ fund.fundCode }}</h4>
              <p class="fund-name">{{ fund.fundName }}</p>
            </div>
            <div class="fund-type">{{ fund.fundType }}</div>
          </div>

          <div class="fund-details">
            <div class="detail-row">
              <span class="detail-label">基金公司</span>
              <span class="detail-value">{{ fund.company }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">当前价格</span>
              <span class="detail-value">¥{{ fund.currentPrice?.toFixed(4) || '--' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">更新时间</span>
              <span class="detail-value">{{ fund.priceDate || '暂无数据' }}</span>
            </div>
          </div>

          <div class="fund-actions">
            <button class="btn-outline" @click="handleAddHolding(fund)">添加持仓</button>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div class="pagination-section" v-if="pagination.total > 0">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[12, 24, 48, 96]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadFunds"
          @current-change="loadFunds"
        />
      </div>

      <!-- Empty State -->
      <div v-if="!loading && funds.length === 0" class="empty-state card">
        <div class="empty-icon">📊</div>
        <h3 class="empty-title">暂无基金数据</h3>
        <p class="empty-description">请尝试调整搜索条件或稍后再试</p>
      </div>
    </div>

    <AddFundForm ref="addFundFormRef" @success="onAddSuccess" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getFunds } from '../api/fund'
import type { Fund } from '../api/fund'
import AddFundForm from '../components/AddFundForm.vue'

const funds = ref<Fund[]>([])
const loading = ref(false)
const addFundFormRef = ref<InstanceType<typeof AddFundForm>>()

const searchForm = reactive({
  keyword: ''
})

const pagination = reactive({
  page: 1,
  size: 12,
  total: 0
})

const loadFunds = async () => {
  loading.value = true
  try {
    const res = await getFunds({
      page: pagination.page,
      size: pagination.size,
      keyword: searchForm.keyword
    })

    if (res.code === 200) {
      funds.value = res.data.list
      pagination.total = res.data.total
    }
  } catch (error) {
    ElMessage.error('加载基金列表失败')
  } finally {
    loading.value = false
  }
}

const handleAddHolding = (fund: Fund) => {
  if (addFundFormRef.value) {
    addFundFormRef.value.show()
    addFundFormRef.value.setFundInfo(fund)
  }
}

const onAddSuccess = () => {
  ElMessage.success('添加成功')
}

onMounted(() => {
  loadFunds()
})
</script>

<style scoped>
.fund-list {
  max-width: 100%;
}

.page-header {
  margin-bottom: var(--space-2xl);
}

.search-section {
  margin-bottom: var(--space-xl);
  padding: var(--space-lg);
}

.search-container {
  display: flex;
  gap: var(--space-base);
  align-items: center;
}

.search-input {
  flex: 1;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 4px;
  border: 1px solid var(--color-border);
}

.funds-section {
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

.funds-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: var(--space-base);
  margin-bottom: var(--space-xl);
}

.fund-card {
  padding: var(--space-lg);
  transition: all 0.2s ease;
}

.fund-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.fund-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-base);
}

.fund-basic-info {
  flex: 1;
}

.fund-code {
  font-size: var(--font-size-lg);
  font-weight: 600;
  margin: 0 0 var(--space-xs) 0;
  color: var(--color-text-primary);
}

.fund-name {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  margin: 0;
  line-height: 1.4;
}

.fund-type {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  background: var(--color-surface);
  padding: var(--space-xs) var(--space-sm);
  border-radius: 4px;
  border: 1px solid var(--color-border);
}

.fund-details {
  margin-bottom: var(--space-lg);
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-xs) 0;
  border-bottom: 1px solid var(--color-border);
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-label {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.detail-value {
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--color-text-primary);
}

.fund-actions {
  display: flex;
  justify-content: flex-end;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: var(--space-xl);
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
  margin: 0;
}

/* Responsive design */
@media (max-width: 768px) {
  .funds-grid {
    grid-template-columns: 1fr;
  }

  .search-container {
    flex-direction: column;
    align-items: stretch;
  }

  .section-header {
    flex-direction: column;
    gap: var(--space-base);
  }

  .fund-header {
    flex-direction: column;
    gap: var(--space-sm);
  }
}
</style>
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { FundHolding, ProfitStatistics } from '../api/fund'

export const useFundStore = defineStore('fund', () => {
  const holdings = ref<FundHolding[]>([])
  const statistics = ref<ProfitStatistics>({
    totalInvestment: 0,
    currentValue: 0,
    totalProfit: 0,
    profitRate: 0,
    fundCount: 0
  })

  const totalProfit = computed(() => {
    return statistics.value.currentValue - statistics.value.totalInvestment
  })

  const profitRate = computed(() => {
    if (statistics.value.totalInvestment === 0) return 0
    return (totalProfit.value / statistics.value.totalInvestment) * 100
  })

  return {
    holdings,
    statistics,
    totalProfit,
    profitRate
  }
})
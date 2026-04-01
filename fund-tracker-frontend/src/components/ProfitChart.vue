<template>
  <div ref="chartRef" style="width: 100%; height: 300px;"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, onUnmounted } from 'vue'
import * as echarts from 'echarts'

const props = defineProps<{
  data: Array<{ date: string; value: number; profit: number }>
}>()

const chartRef = ref<HTMLElement>()
let chart: echarts.ECharts | null = null
const isComponentMounted = ref(true)

const initChart = () => {
  if (!chartRef.value || !props.data.length || !isComponentMounted.value) return

  chart = echarts.init(chartRef.value)

  const dates = props.data.map(item => item.date)
  const values = props.data.map(item => item.value)
  const profits = props.data.map(item => item.profit)

  const option = {
    title: {
      text: '收益趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['市值', '收益']
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '市值',
        type: 'line',
        data: values
      },
      {
        name: '收益',
        type: 'line',
        data: profits
      }
    ]
  }

  chart.setOption(option)
}

onMounted(() => {
  initChart()
})

watch(() => props.data, () => {
  if (!isComponentMounted.value) return
  if (chart) {
    chart.dispose()
    chart = null
  }
  initChart()
})

onUnmounted(() => {
  isComponentMounted.value = false
  if (chart) {
    chart.dispose()
    chart = null
  }
})
</script>
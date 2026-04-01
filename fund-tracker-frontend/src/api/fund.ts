import request from '../utils/request'

export interface Fund {
  id: number
  fundCode: string
  fundName: string
  fundType: string
  company: string
  currentPrice: number
  priceDate: string
}

export interface FundHolding {
  id: number
  fund: Fund
  buyPrice: number
  currentPrice: number
  shares: number
  buyDate: string
  totalCost: number
  currentValue: number
  profit: number
  profitRate: number
}

export interface AddHoldingRequest {
  fundCode: string
  buyPrice: number
  shares: number
  buyDate: string
}

export interface ProfitStatistics {
  totalInvestment: number
  currentValue: number
  totalProfit: number
  profitRate: number
  fundCount: number
}

// 获取基金列表
export const getFunds = (params: { page?: number; size?: number; keyword?: string }) => {
  return request.get('/funds', { params })
}

// 添加基金持仓
export const addHolding = (data: AddHoldingRequest) => {
  return request.post('/holdings', data)
}

// 获取用户持仓
export const getHoldings = () => {
  return request.get('/holdings')
}

// 删除持仓
export const deleteHolding = (id: number) => {
  return request.delete(`/holdings/${id}`)
}

// 获取收益统计
export const getProfitStatistics = () => {
  return request.get('/statistics/profit')
}
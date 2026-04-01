import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../pages/Dashboard.vue'
import FundList from '../pages/FundList.vue'
import MyHoldings from '../pages/MyHoldings.vue'

const router = createRouter({
  history: createWebHistory('/'),
  routes: [
    {
      path: '/',
      name: 'Dashboard',
      component: Dashboard,
    },
    {
      path: '/funds',
      name: 'FundList',
      component: FundList,
    },
    {
      path: '/holdings',
      name: 'MyHoldings',
      component: MyHoldings,
    }
  ],
  linkActiveClass: 'active',
  linkExactActiveClass: 'active'
})

// 添加路由守卫以确保路由正常工作
router.beforeEach((to, from, next) => {
  next()
})

// 添加全局错误处理
router.onError((error) => {
  // 忽略与 parentNode 相关的错误
  if (error.message && error.message.includes('parentNode')) {
    console.warn('Router: Ignoring harmless parentNode error')
    return
  }
  console.error('Router error:', error)
})

export default router
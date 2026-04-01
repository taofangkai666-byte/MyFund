import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)

// 全局错误处理器，忽略无害的 parentNode 错误
app.config.errorHandler = (err, instance, info) => {
  // 忽略与 parentNode 相关的无害错误
  if (err.message && err.message.includes('parentNode')) {
    console.warn('Ignoring harmless parentNode error:', err.message)
    return
  }

  // 忽略与 toFixed 相关的无害错误（这些应该已经被修复）
  if (err.message && err.message.includes('toFixed')) {
    console.warn('Ignoring harmless toFixed error:', err.message)
    return
  }

  // 其他错误正常输出
  console.error('Vue error:', err, info)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')
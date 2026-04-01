<template>
  <div id="app" class="app-container">
    <header class="app-header">
      <div class="header-content">
        <div class="logo-section">
          <h1 class="app-title">基金追踪器</h1>
        </div>

        <nav class="main-nav">
          <router-link to="/" class="nav-item" :class="{ active: isActive('/') }">
            <span class="nav-text">仪表板</span>
          </router-link>
          <router-link to="/funds" class="nav-item" :class="{ active: isActive('/funds') }">
            <span class="nav-text">基金列表</span>
          </router-link>
          <router-link to="/holdings" class="nav-item" :class="{ active: isActive('/holdings') }">
            <span class="nav-text">我的持仓</span>
          </router-link>
        </nav>
      </div>
    </header>

    <main class="main-content">
      <div class="content-wrapper" style="min-height: 100%;">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()

const isActive = (path: string) => {
  return route.path === path
}
</script>

<style>
:root {
  /* Minimal color palette */
  --color-background: #ffffff;
  --color-surface: #f8f9fa;
  --color-border: #e9ecef;
  --color-text-primary: #212529;
  --color-text-secondary: #6c757d;
  --color-text-tertiary: #adb5bd;
  --color-accent: #000000;
  --color-success: #198754;
  --color-error: #dc3545;

  /* Typography */
  --font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  --font-size-xs: 12px;
  --font-size-sm: 14px;
  --font-size-base: 16px;
  --font-size-lg: 18px;
  --font-size-xl: 20px;
  --font-size-2xl: 24px;
  --font-size-3xl: 32px;

  /* Spacing */
  --space-xs: 4px;
  --space-sm: 8px;
  --space-base: 16px;
  --space-lg: 24px;
  --space-xl: 32px;
  --space-2xl: 48px;
  --space-3xl: 64px;

  /* Shadows */
  --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
  --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: var(--font-family);
  background: var(--color-background);
  color: var(--color-text-primary);
  line-height: 1.6;
}

.app-container {
  min-height: 100vh;
}

header.app-header {
  background: var(--color-background);
  border-bottom: 1px solid var(--color-border);
  height: 64px;
  padding: 0;
  display: block;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 var(--space-xl);
  max-width: 1200px;
  margin: 0 auto;
}

.logo-section {
  display: flex;
  align-items: center;
}

.app-title {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
  letter-spacing: -0.025em;
}

.main-nav {
  display: flex;
  gap: var(--space-base);
}

.nav-item {
  padding: var(--space-sm) var(--space-base);
  text-decoration: none;
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  font-weight: 500;
  transition: all 0.2s ease;
  border-radius: 4px;
}

.nav-item:hover {
  color: var(--color-text-primary);
  background: var(--color-surface);
}

.nav-item.active {
  color: var(--color-accent);
  background: var(--color-surface);
}

main.main-content {
  min-height: calc(100vh - 64px);
  padding: 0;
  background: var(--color-background);
  overflow: auto;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-2xl) var(--space-xl);
}

/* Typography utilities */
.heading-1 {
  font-size: var(--font-size-3xl);
  font-weight: 700;
  line-height: 1.2;
  letter-spacing: -0.025em;
  margin: 0 0 var(--space-lg) 0;
}

.heading-2 {
  font-size: var(--font-size-2xl);
  font-weight: 600;
  line-height: 1.3;
  letter-spacing: -0.025em;
  margin: 0 0 var(--space-base) 0;
}

.heading-3 {
  font-size: var(--font-size-xl);
  font-weight: 600;
  line-height: 1.4;
  margin: 0 0 var(--space-base) 0;
}

.body-text {
  font-size: var(--font-size-base);
  line-height: 1.6;
  color: var(--color-text-secondary);
  margin: 0 0 var(--space-base) 0;
}

.caption {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-weight: 500;
}

/* Card component */
.card {
  background: var(--color-background);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  padding: var(--space-lg);
}

/* Button styles */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-sm) var(--space-lg);
  background: var(--color-accent);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: var(--font-size-sm);
  font-weight: 500;
  text-decoration: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.btn-outline {
  background: transparent;
  color: var(--color-text-primary);
  border: 1px solid var(--color-border);
}

.btn-outline:hover {
  background: var(--color-surface);
  transform: none;
}

/* Responsive design */
@media (max-width: 768px) {
  .header-content {
    padding: 0 var(--space-base);
  }

  .content-wrapper {
    padding: var(--space-xl) var(--space-base);
  }

  .app-title {
    font-size: var(--font-size-lg);
  }

  .main-nav {
    gap: var(--space-sm);
  }

  .nav-item {
    padding: var(--space-xs) var(--space-sm);
    font-size: var(--font-size-xs);
  }
}
</style>
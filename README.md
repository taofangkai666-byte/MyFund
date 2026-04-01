# 基金追踪器 - 一期版本

这是一个基金收益追踪系统的一期版本，专注于核心的添加/删除基金持仓和查看收益功能。

## 技术栈

### 前端
- Vue 3 + TypeScript
- Element Plus UI 组件库
- ECharts 图表库
- Pinia 状态管理
- Vite 构建工具

### 后端
- Spring Boot 3.x
- MyBatis Plus
- MySQL 8.x
- Lombok

## 项目结构

```
my_todo/
├── fund-tracker-frontend/    # 前端项目
├── fund-tracker-backend/     # 后端项目
├── database/                 # 数据库脚本
└── fund/                    # 架构文档
```

## 快速开始

### 1. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source database/init.sql
```

### 2. 启动后端服务

```bash
cd fund-tracker-backend
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 3. 启动前端服务

```bash
cd fund-tracker-frontend
npm install
npm run dev
```

前端服务将在 http://localhost:3000 启动

## 核心功能

### 已实现功能

1. **基金列表** - 查看和搜索基金
2. **添加持仓** - 添加基金持仓记录
3. **查看持仓** - 显示所有持仓基金
4. **删除持仓** - 删除不需要的持仓记录
5. **收益统计** - 显示总投资、当前市值、总收益和收益率
6. **收益图表** - 展示收益趋势（模拟数据）

### 页面说明

- **仪表板** (/) - 显示收益统计和持仓概览
- **基金列表** (/funds) - 浏览和搜索基金，添加持仓
- **我的持仓** (/holdings) - 管理持仓，查看详细的收益信息

## API接口

### 基金相关
- `GET /api/funds` - 获取基金列表
- `POST /api/holdings` - 添加基金持仓
- `GET /api/holdings` - 获取用户持仓
- `DELETE /api/holdings/{id}` - 删除持仓
- `GET /api/statistics/profit` - 获取收益统计

## 后续计划

二期版本将添加：
- 用户登录和权限控制
- 基金数据实时获取
- 更详细的图表分析
- 数据导出功能
- 移动端适配

## 注意事项

- 一期版本使用模拟数据进行演示
- 用户系统暂未实现，使用固定用户ID=1
- 基金价格更新为模拟数据，实际项目中需要接入真实的基金数据API
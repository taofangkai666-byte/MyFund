@echo off
REM 基金追踪器开发环境启动脚本

echo 正在启动基金追踪器开发环境...
echo.

echo 1. 请确保已安装以下软件：
echo    - Node.js 18+
echo    - Java 17+
echo    - Maven 3.8+
echo    - MySQL 8.0+
echo.

echo 2. 请手动执行数据库初始化：
echo    mysql -u root -p
echo    source database/init.sql
echo.

echo 3. 按任意键继续启动服务...
pause > nul

echo.
echo 正在启动后端服务...
cd fund-tracker-backend
start cmd /k "mvn spring-boot:run"

echo.
echo 等待3秒...
timeout /t 3 /nobreak > nul

echo 正在启动前端服务...
cd ../fund-tracker-frontend
start cmd /k "npm install && npm run dev"

echo.
echo 启动完成！
echo 前端地址：http://localhost:3000
echo 后端地址：http://localhost:8080
pause
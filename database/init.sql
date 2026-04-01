SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
-- 创建数据库
CREATE DATABASE IF NOT EXISTS fund CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fund;

-- 基金表
CREATE TABLE `fund` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `fund_code` VARCHAR(20) UNIQUE NOT NULL,
  `fund_name` VARCHAR(100) NOT NULL,
  `fund_type` VARCHAR(20),
  `company` VARCHAR(50),
  `current_price` DECIMAL(10,4),
  `price_date` DATE,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 基金持仓表
CREATE TABLE `fund_holding` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL DEFAULT 1,
  `fund_id` BIGINT NOT NULL,
  `buy_price` DECIMAL(10,4) NOT NULL,
  `shares` DECIMAL(15,2) NOT NULL,
  `buy_date` DATE NOT NULL,
  `total_cost` DECIMAL(15,2) NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`fund_id`) REFERENCES `fund`(`id`)
);

-- 插入一些示例基金数据
INSERT INTO `fund` (`fund_code`, `fund_name`, `fund_type`, `company`, `current_price`, `price_date`) VALUES
('000001', '华夏成长混合', '混合型', '华夏基金', 1.2345, '2024-03-20'),
('000002', '南方稳健成长', '混合型', '南方基金', 1.4567, '2024-03-20'),
('110022', '易方达消费行业', '股票型', '易方达基金', 2.3456, '2024-03-20'),
('161725', '招商中证白酒', '股票型', '招商基金', 1.8765, '2024-03-20'),
('000478', '建信中证500指数增强', '指数型', '建信基金', 1.5432, '2024-03-20');
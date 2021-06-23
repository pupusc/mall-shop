/*
 Navicat Premium Data Transfer

 Source Server         : wanmi-172.19.25.12
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : 172.19.25.12:3306
 Source Schema         : sbc-crm

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 02/01/2020 10:52:25
*/

CREATE DATABASE `sbc-crm` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

use `sbc-crm`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for custom_group
-- ----------------------------
DROP TABLE IF EXISTS `custom_group`;
CREATE TABLE `custom_group`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分群名称',
  `definition` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人群定义',
  `group_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '分群信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `customer_tags` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 219 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自定义人群' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for custom_group_rel
-- ----------------------------
DROP TABLE IF EXISTS `custom_group_rel`;
CREATE TABLE `custom_group_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NULL DEFAULT NULL COMMENT '人群id',
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_group_id`(`group_id`) USING BTREE,
  INDEX `index_customer_id`(`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2456 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自定义人群的会员明细' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for custom_group_statistics
-- ----------------------------
DROP TABLE IF EXISTS `custom_group_statistics`;
CREATE TABLE `custom_group_statistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_date` date NULL DEFAULT NULL COMMENT '统计时间',
  `group_id` bigint(11) NULL DEFAULT NULL COMMENT '自定义人群id',
  `customer_count` bigint(11) NULL DEFAULT NULL COMMENT '会员数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_STAT_DATE`(`stat_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4827 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自定义人群统计' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for customer_base_info
-- ----------------------------
DROP TABLE IF EXISTS `customer_base_info`;
CREATE TABLE `customer_base_info`  (
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员id',
  `province_id` bigint(11) NULL DEFAULT NULL COMMENT '省份',
  `city_id` bigint(11) NULL DEFAULT NULL COMMENT '城市',
  `area_id` bigint(11) NULL DEFAULT NULL COMMENT '地区',
  `growth_value` bigint(11) NULL DEFAULT NULL COMMENT '成长值',
  `customer_level_id` bigint(11) NULL DEFAULT NULL COMMENT '会员等级',
  `points` bigint(11) NULL DEFAULT NULL COMMENT '积分',
  `account_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '账户余额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员基本信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for customer_recent_param_statistics
-- ----------------------------
DROP TABLE IF EXISTS `customer_recent_param_statistics`;
CREATE TABLE `customer_recent_param_statistics`  (
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员id',
  `pay_time` int(11) NULL DEFAULT NULL COMMENT '最近支付时间（天）',
  `trade_time` int(11) NULL DEFAULT NULL COMMENT '最近下单时间（天）',
  `flow_time` int(11) NULL DEFAULT NULL COMMENT '最近访问时间（天）',
  `cart_time` int(11) NULL DEFAULT NULL COMMENT '最近加购时间（天）',
  `favorite_time` int(11) NULL DEFAULT NULL COMMENT '最近收藏时间（天）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员最近的相关指标（下单、支付、访问、加购、收藏）' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for customer_tag
-- ----------------------------
DROP TABLE IF EXISTS `customer_tag`;
CREATE TABLE `customer_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `customer_num` int(11) NULL DEFAULT NULL COMMENT '会员人数',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标志位，0:未删除，1:已删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员标签表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for customer_tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `customer_tag_rel`;
CREATE TABLE `customer_tag_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员id',
  `tag_id` bigint(20) NOT NULL COMMENT '标签id',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员标签和会员关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for customer_trade_statistics
-- ----------------------------
DROP TABLE IF EXISTS `customer_trade_statistics`;
CREATE TABLE `customer_trade_statistics`  (
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员id',
  `period` int(4) NOT NULL COMMENT '统计周期，默认天',
  `trade_count` bigint(255) NULL DEFAULT NULL,
  `trade_price` decimal(20, 2) NULL DEFAULT NULL,
  `avg_trade_price` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`customer_id`, `period`) USING BTREE,
  INDEX `INDEX_PERIOD`(`period`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员订单统计' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rfm_customer_detail
-- ----------------------------
DROP TABLE IF EXISTS `rfm_customer_detail`;
CREATE TABLE `rfm_customer_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `r_score` int(11) NULL DEFAULT NULL COMMENT 'R分对应得分',
  `f_score` int(11) NULL DEFAULT NULL COMMENT 'F分对应得分',
  `m_score` int(11) NULL DEFAULT NULL COMMENT 'M分对应得分',
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员id',
  `system_group_id` bigint(20) NULL DEFAULT NULL COMMENT '系统分群id',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `stat_date` date NULL DEFAULT NULL COMMENT '统计时间',
  `r_value` int(11) NULL DEFAULT NULL COMMENT 'R值（最近一次消费时间/天）',
  `f_value` int(11) NULL DEFAULT NULL COMMENT 'F值（消费频次）',
  `m_value` decimal(20, 2) NULL DEFAULT NULL COMMENT 'M值（消费金额）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_STAT_DATE`(`stat_date`) USING BTREE,
  INDEX `INDEX_SYSTEM_GROUP_ID`(`system_group_id`) USING BTREE,
  INDEX `INDEX_CUSTOMER_ID`(`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4373375 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'rfm会员统计明细表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rfm_score_statistic
-- ----------------------------
DROP TABLE IF EXISTS `rfm_score_statistic`;
CREATE TABLE `rfm_score_statistic`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `score` decimal(14, 2) NOT NULL COMMENT '得分',
  `type` tinyint(4) NOT NULL COMMENT '参数类型：0:R,1:F,2:M',
  `person_num` bigint(20) NULL DEFAULT NULL COMMENT '得分对应人数',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `stat_date` date NULL DEFAULT NULL COMMENT '统计时间',
  `score_type` tinyint(4) NULL DEFAULT NULL COMMENT '得分类型:0:参数得分，1:平均分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5801 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'rfm得分结果统计' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rfm_setting
-- ----------------------------
DROP TABLE IF EXISTS `rfm_setting`;
CREATE TABLE `rfm_setting`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `param` int(11) NOT NULL COMMENT '参数',
  `score` int(11) NOT NULL COMMENT '得分',
  `type` tinyint(4) NOT NULL COMMENT '参数类型：0:R,1:F,2:M',
  `period` tinyint(4) NULL DEFAULT NULL COMMENT '统计周期：0:近一个月，1:近3个月，2:近6个月，3:近一年',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标识,0:未删除，1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2368 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'rfm参数配置' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rfm_setting_history
-- ----------------------------
DROP TABLE IF EXISTS `rfm_setting_history`;
CREATE TABLE `rfm_setting_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `param` int(11) NOT NULL COMMENT '参数',
  `score` int(11) NOT NULL COMMENT '得分',
  `type` tinyint(4) NOT NULL COMMENT '参数类型：0:R,1:F,2:M',
  `period` tinyint(4) NULL DEFAULT NULL COMMENT '统计周期：0:近一个月，1:近3个月，2:近6个月，3:近一年',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标识,0:未删除，1:已删除',
  `stat_data` date NULL DEFAULT NULL COMMENT '统计日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'rfm参数配置历史记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rfm_system_group
-- ----------------------------
DROP TABLE IF EXISTS `rfm_system_group`;
CREATE TABLE `rfm_system_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '人群名称',
  `definition` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '人群定义',
  `advise` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运营建议',
  `r_rule` tinyint(4) NULL DEFAULT NULL COMMENT 'r分规则(０:低 1:高)',
  `f_rule` tinyint(4) NULL DEFAULT NULL COMMENT 'f分规则(０:低 1:高)',
  `m_rule` tinyint(4) NULL DEFAULT NULL COMMENT 'm分规则(０:低 1:高)',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'rfm系统推荐人群划分表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rfm_system_group_statistics
-- ----------------------------
DROP TABLE IF EXISTS `rfm_system_group_statistics`;
CREATE TABLE `rfm_system_group_statistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_num` int(11) NULL DEFAULT NULL COMMENT '会员人数',
  `uv_num` int(11) NULL DEFAULT NULL COMMENT '访问数',
  `trade_num` int(11) NULL DEFAULT NULL COMMENT '成交数',
  `system_group_id` bigint(20) NULL DEFAULT NULL COMMENT '系统人群id',
  `stat_date` date NULL DEFAULT NULL COMMENT '统计日期',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_STAT_DATE`(`stat_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 604 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'rfm系统推荐人群统计结果表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

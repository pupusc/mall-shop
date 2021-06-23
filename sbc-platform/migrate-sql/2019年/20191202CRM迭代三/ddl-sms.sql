/*
 Navicat Premium Data Transfer

 Source Server         : wanmi-172.19.25.12
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : 172.19.25.12:3306
 Source Schema         : sbc-sms

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 02/01/2020 10:51:33
*/

CREATE DATABASE `sbc-sms` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

use `sbc-sms`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sms_send
-- ----------------------------
DROP TABLE IF EXISTS `sms_send`;
CREATE TABLE `sms_send`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `sms_setting_id` bigint(11) NULL DEFAULT NULL,
  `context` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信内容',
  `template_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板code',
  `sign_id` bigint(11) NULL DEFAULT NULL COMMENT '签名id',
  `receive_context` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人描述',
  `receive_type` tinyint(4) NULL DEFAULT NULL COMMENT '接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）',
  `receive_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接收人明细',
  `manual_add` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '手工添加的号码',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态（0-未开始，1-进行中，2-已结束，3-任务失败）',
  `message` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务执行信息',
  `send_type` tinyint(4) NULL DEFAULT NULL COMMENT '发送类型（0-立即发送，1-定时发送）',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `row_count` int(10) NULL DEFAULT NULL COMMENT '预计发送条数',
  `create_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `resend_type` tinyint(2) NULL DEFAULT 0 COMMENT '重发类型（0-不可重发，1-可重发）',
  `send_detail_count` int(11) NULL DEFAULT NULL COMMENT '发送明细条数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 162 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信发送任务' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sms_send_detail
-- ----------------------------
DROP TABLE IF EXISTS `sms_send_detail`;
CREATE TABLE `sms_send_detail`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `send_id` bigint(11) NULL DEFAULT NULL COMMENT '发送任务id',
  `phone_numbers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接收短信的号码',
  `biz_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回执id',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态（0-失败，1-成功）',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求状态码。',
  `message` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务执行信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `send_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 529 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发送明细' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sms_setting
-- ----------------------------
DROP TABLE IF EXISTS `sms_setting`;
CREATE TABLE `sms_setting`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `access_key_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用api参数key',
  `access_key_secret` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用api参数secret',
  `type` tinyint(255) NULL DEFAULT NULL COMMENT '短信平台类型：0：阿里云短信平台',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '是否启用：0：未启用；1：启用',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识：0：未删除；1：已删除',
  `creat_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '短信配置' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sms_sign
-- ----------------------------
DROP TABLE IF EXISTS `sms_sign`;
CREATE TABLE `sms_sign`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sms_sign_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名名称',
  `sign_source` tinyint(4) NULL DEFAULT NULL COMMENT '签名来源,0：企事业单位的全称或简称,1：工信部备案网站的全称或简称,2：APP应用的全称或简称,3：公众号或小程序的全称或简称,4：电商平台店铺名的全称或简称,5：商标名的全称或简称',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信签名申请说明',
  `involve_third_interest` tinyint(4) NULL DEFAULT NULL COMMENT '是否涉及第三方利益：0：否，1：是',
  `review_status` tinyint(4) NULL DEFAULT NULL COMMENT '审核状态：0:待审核，1:审核通过，2:审核未通过',
  `review_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `sms_setting_id` bigint(11) NULL DEFAULT NULL COMMENT '短信配置id',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识，0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '短信签名' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sms_sign_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sms_sign_file_info`;
CREATE TABLE `sms_sign_file_info`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sms_sign_id` bigint(11) NULL DEFAULT NULL COMMENT '短信签名id',
  `file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识，0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '短信签名文件信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sms_template
-- ----------------------------
DROP TABLE IF EXISTS `sms_template`;
CREATE TABLE `sms_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `template_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信模板申请说明',
  `template_type` tinyint(4) NULL DEFAULT NULL COMMENT '短信类型。其中：\r\n\r\n0：验证码。\r\n1：短信通知。\r\n2：推广短信。\r\n短信类型,0：验证码,1：短信通知,2：推广短信,3：国际/港澳台消息。',
  `review_status` tinyint(4) NULL DEFAULT NULL COMMENT '模板状态，0：待审核，1：审核通过，2：审核未通过 3：待提交',
  `template_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板code,模板审核通过返回的模板code，发送短信时使用',
  `review_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `sms_setting_id` bigint(11) NULL DEFAULT NULL COMMENT '短信配置id',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识位，0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `business_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `purpose` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用途',
  `sign_id` bigint(11) NULL DEFAULT NULL COMMENT '签名id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '短信模板' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

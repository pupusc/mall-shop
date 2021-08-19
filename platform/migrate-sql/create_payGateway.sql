/*
Navicat MySQL Data Transfer

Source Server         : 172.19.26.192
Source Server Version : 50634
Source Host           : 172.19.26.192:3306
Source Database       : b2b

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2017-08-16 15:11:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pay_channel_item
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel_item`;
CREATE TABLE `pay_channel_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '支付渠道项名称',
  `gateway_id` int(11) NOT NULL COMMENT '网关id',
  `channel` varchar(45) NOT NULL COMMENT '支付渠道',
  `is_open` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启:0关闭 1开启',
  `terminal` tinyint(1) DEFAULT NULL COMMENT '支付类型: 0 pc 1 h5 2 app',
  `code` varchar(20) DEFAULT NULL COMMENT '支付渠道项代码，同一支付项唯一',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_gateway_id_code_unique` (`gateway_id`,`code`) USING BTREE,
  KEY `pay_item_ibfk_1` (`gateway_id`),
  KEY `pay_item_name` (`name`) USING BTREE,
  CONSTRAINT `pay_channel_item_ibfk_1` FOREIGN KEY (`gateway_id`) REFERENCES `pay_gateway` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='支付渠道项';

-- ----------------------------
-- Records of pay_channel_item
-- ----------------------------
INSERT INTO `pay_channel_item` VALUES ('1', '支付宝当面付', '1', 'alipay\r', '1', '0', 'alipay_qr', '2017-08-14 14:49:52');
INSERT INTO `pay_channel_item` VALUES ('2', '支付宝电脑网站支付', '1', 'alipay', '1', '0', 'alipay_pc_direct', '2017-08-10 15:20:31');
INSERT INTO `pay_channel_item` VALUES ('3', '微信公众号扫码支付', '1', 'WeChat', '0', '0', 'wx_pub_qr', '2017-08-10 15:23:01');
INSERT INTO `pay_channel_item` VALUES ('4', '银联企业网银支付', '1', 'unionpay', '0', '0', 'upacp_pc', '2017-08-10 15:24:23');
INSERT INTO `pay_channel_item` VALUES ('5', '支付宝手机网页支付', '1', 'alipay\r', '1', '1', 'alipay_wap', '2017-08-10 15:25:23');
INSERT INTO `pay_channel_item` VALUES ('6', '微信公众号支付', '1', 'WeChat', '1', '1', 'wx_pub', '2017-08-10 15:26:28');
INSERT INTO `pay_channel_item` VALUES ('7', '银联手机网页支付', '1', 'unionpay', '1', '1', 'upacp_wap', '2017-08-10 15:27:11');
INSERT INTO `pay_channel_item` VALUES ('8', '支付宝 APP 支付', '1', 'alipay', '0', '2', 'alipay', '2017-08-10 15:37:07');
INSERT INTO `pay_channel_item` VALUES ('9', '微信 APP 支付', '1', 'WeChat', '1', '2', 'wx', '2017-08-10 15:37:37');
INSERT INTO `pay_channel_item` VALUES ('10', '银联支付', '1', 'unionpay', '0', '2', 'upacp', '2017-08-10 15:39:01');

-- ----------------------------
-- Table structure for pay_gateway
-- ----------------------------
DROP TABLE IF EXISTS `pay_gateway`;
CREATE TABLE `pay_gateway` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '网关名称',
  `is_open` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启:0关闭 1开启',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '网关类型：0单一支付，1聚合支付',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`,`name`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='网关';

-- ----------------------------
-- Records of pay_gateway
-- ----------------------------
INSERT INTO `pay_gateway` VALUES ('1', 'PING', '1', '1', '2017-08-03 18:52:31');

-- ----------------------------
-- Table structure for pay_gateway_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_gateway_config`;
CREATE TABLE `pay_gateway_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gateway_id` int(11) NOT NULL COMMENT '网关id',
  `api_key` varchar(66) DEFAULT NULL COMMENT '身份标识',
  `secret` varchar(66) DEFAULT NULL COMMENT 'secret key',
  `account` varchar(60) DEFAULT NULL COMMENT '收款账号',
  `app_id` varchar(40) DEFAULT NULL COMMENT '第三方应用标识',
  `app_id2` varchar(40) DEFAULT NULL COMMENT '身份标志(聚合支付时代表微信)',
  `private_key` varchar(255) DEFAULT NULL COMMENT '私钥',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pay_gateway_config_ibfk_1` (`gateway_id`) USING BTREE,
  CONSTRAINT `pay_gateway_config_ibfk_1` FOREIGN KEY (`gateway_id`) REFERENCES `pay_gateway` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='网关配置';

-- ----------------------------
-- Records of pay_gateway_config
-- ----------------------------
INSERT INTO `pay_gateway_config` VALUES ('1', '1', 'sk_test_K0e9888yfv9SP0uPOGrXD8C4', null, null, 'app_WbTCuHyjD4W9bDC8', 'wxca55d47d11c97292', '11www3', '2017-08-04 18:37:34');

-- ----------------------------
-- Table structure for pay_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_trade_record`;
CREATE TABLE `pay_trade_record` (
  `id` varchar(45) NOT NULL,
  `business_id` varchar(32) NOT NULL COMMENT '业务id(订单或退单号)',
  `charge_id` varchar(27) DEFAULT NULL COMMENT '支付渠道方返回的支付或退款对象id',
  `apply_price` decimal(20,2) NOT NULL COMMENT '申请价格',
  `practical_price` decimal(20,2) DEFAULT NULL COMMENT '实际成功交易价格',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0处理中(退款状态)/未支付(支付状态) 1成功 2失败',
  `channel_item_id` int(11) NOT NULL COMMENT '支付渠道项id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
  `finish_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端ip',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易流水号',
  `trade_type` varchar(20) DEFAULT NULL COMMENT '交易类型',
  PRIMARY KEY (`id`,`create_time`),
  KEY `business_id` (`business_id`),
  KEY `charge_id` (`charge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易记录'
/*!50100 PARTITION BY RANGE (YEAR(create_time))
(PARTITION p2017 VALUES LESS THAN (2017) ENGINE = InnoDB,
 PARTITION p2018 VALUES LESS THAN (2018) ENGINE = InnoDB,
 PARTITION p2019 VALUES LESS THAN (2019) ENGINE = InnoDB,
 PARTITION p2020 VALUES LESS THAN (2020) ENGINE = InnoDB,
 PARTITION p2021 VALUES LESS THAN (2021) ENGINE = InnoDB,
 PARTITION p2022 VALUES LESS THAN (2022) ENGINE = InnoDB,
 PARTITION p2023 VALUES LESS THAN (2023) ENGINE = InnoDB,
 PARTITION p2024 VALUES LESS THAN (2024) ENGINE = InnoDB,
 PARTITION p2025 VALUES LESS THAN (2025) ENGINE = InnoDB,
 PARTITION p2026 VALUES LESS THAN (2026) ENGINE = InnoDB,
 PARTITION p2027 VALUES LESS THAN (2027) ENGINE = InnoDB,
 PARTITION p2028 VALUES LESS THAN (2028) ENGINE = InnoDB,
 PARTITION p2029 VALUES LESS THAN (2029) ENGINE = InnoDB,
 PARTITION p2030 VALUES LESS THAN (2030) ENGINE = InnoDB,
 PARTITION p2031 VALUES LESS THAN (2031) ENGINE = InnoDB,
 PARTITION p2032 VALUES LESS THAN (2032) ENGINE = InnoDB,
 PARTITION p2033 VALUES LESS THAN (2033) ENGINE = InnoDB,
 PARTITION p2034 VALUES LESS THAN (2034) ENGINE = InnoDB,
 PARTITION p2035 VALUES LESS THAN (2035) ENGINE = InnoDB,
 PARTITION p2036 VALUES LESS THAN (2036) ENGINE = InnoDB) */;

-- ----------------------------
-- Records of pay_trade_record
-- ----------------------------

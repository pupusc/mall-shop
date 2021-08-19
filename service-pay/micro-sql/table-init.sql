-- 数据库：sbc-pay
SET FOREIGN_KEY_CHECKS=0;

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='支付渠道项';


CREATE TABLE `pay_gateway` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '网关名称',
  `is_open` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启:0关闭 1开启',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '网关类型：0单一支付，1聚合支付',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`,`name`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='网关';


CREATE TABLE `pay_gateway_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gateway_id` int(11) NOT NULL COMMENT '网关id',
  `api_key` varchar(66) DEFAULT NULL COMMENT '身份标识',
  `secret` varchar(66) DEFAULT NULL COMMENT 'secret key',
  `account` varchar(60) DEFAULT NULL COMMENT '收款账号',
  `app_id` varchar(40) DEFAULT NULL COMMENT '第三方应用标识',
  `app_id2` varchar(40) DEFAULT NULL COMMENT '身份标志(聚合支付时代表微信)',
  `private_key` varchar(1200) DEFAULT NULL COMMENT '私钥',
  `public_key` varchar(1200) DEFAULT NULL COMMENT '公钥',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pc_back_url` varchar(255) DEFAULT NULL COMMENT 'PC前端后台接口地址',
  `pc_web_url` varchar(255) DEFAULT NULL COMMENT 'PC前端web地址',
  `boss_back_url` varchar(255) DEFAULT NULL COMMENT 'boss后台接口地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pay_gateway_config_ibfk_1` (`gateway_id`) USING BTREE,
  CONSTRAINT `pay_gateway_config_ibfk_1` FOREIGN KEY (`gateway_id`) REFERENCES `pay_gateway` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='网关配置';


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
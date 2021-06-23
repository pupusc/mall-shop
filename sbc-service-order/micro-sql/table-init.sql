--数据库：sbc-order
SET FOREIGN_KEY_CHECKS=0;


--商品的客户收藏
DROP TABLE IF EXISTS `goods_customer_follow`;

CREATE TABLE `goods_customer_follow` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `goods_id` varchar(32) NOT NULL COMMENT '商品Id',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SKU编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `goods_num` bigint(10) NOT NULL COMMENT '购买数',
  `follow_flag` tinyint(4) DEFAULT NULL COMMENT '收藏标识0:all,1.采购,2:收藏',
  `follow_time` datetime DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`follow_id`),
  UNIQUE KEY `customer_id` (`customer_id`,`goods_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品的客户收藏';


--订单开票
DROP TABLE IF EXISTS `order_invoice`;

CREATE TABLE `order_invoice` (
  `order_invoice_id` varchar(32) NOT NULL COMMENT '订单开票ID',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '客户ID',
  `order_no` varchar(45) DEFAULT NULL COMMENT '订单号',
  `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺id',
  `invoice_type` tinyint(4) DEFAULT NULL COMMENT '发票类型 0普通发票 1增值税专用发票 -1无',
  `invoice_title` varchar(255) DEFAULT NULL COMMENT '发票抬头',
  `invoice_state` tinyint(4) DEFAULT NULL COMMENT '开票状态 0待开票 1 已开票',
  `invoice_address` varchar(225) DEFAULT NULL,
  `project_id` varchar(32) DEFAULT NULL COMMENT '开票项目id',
  `is_company` tinyint(4) DEFAULT NULL COMMENT '是否是企业 0 是 1 否',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标志 0未删除 1已删除',
  `invoice_time` datetime DEFAULT NULL COMMENT '开票时间',
  `create_time` datetime DEFAULT NULL COMMENT '开票时间',
  `operator_id` varchar(45) DEFAULT NULL COMMENT '操作人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `taxpayer_number` varchar(20) DEFAULT NULL COMMENT '纳税人识别号',
  PRIMARY KEY (`order_invoice_id`),
  UNIQUE KEY `order_invoice_id_UNIQUE` (`order_invoice_id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单开票表';


--财务支付单
DROP TABLE IF EXISTS `pay_order`;

CREATE TABLE `pay_order` (
  `pay_order_id` varchar(45) NOT NULL COMMENT '主键',
  `pay_order_no` varchar(45) DEFAULT NULL COMMENT '支付单号，一期用不到',
  `order_code` varchar(45) DEFAULT NULL COMMENT '订单编号',
  `pay_order_status` varchar(45) DEFAULT NULL COMMENT '支付单状态',
  `pay_type` tinyint(4) DEFAULT NULL COMMENT '支付方式 0线上 1线下',
  `customer_detail_id` varchar(32) DEFAULT NULL COMMENT '客户详情id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_time` datetime DEFAULT NULL,
  `del_flag` tinyint(4) DEFAULT NULL,
  `pay_order_price` decimal(20,2) DEFAULT NULL,
  `company_info_id` bigint(11) DEFAULT NULL COMMENT '商家id',
  PRIMARY KEY (`pay_order_id`),
  UNIQUE KEY `pay_order_id_UNIQUE` (`pay_order_id`),
  UNIQUE KEY `pay_order_no_UNIQUE` (`pay_order_no`),
  KEY `idx_order_code` (`order_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务支付单';

--采购单
DROP TABLE IF EXISTS `purchase`;

CREATE TABLE `purchase` (
  `purchase_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '会员Id',
  `goods_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品id',
  `goods_info_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT 'SKU编号',
  `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家编号',
  `goods_num` bigint(20) DEFAULT NULL COMMENT '购买数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`purchase_id`),
  UNIQUE KEY `customer_id` (`customer_id`,`goods_info_id`),
  KEY `idx_purchase_customerid_goodsinfoid` (`customer_id`,`goods_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--收款单
DROP TABLE IF EXISTS `receivable`;

CREATE TABLE `receivable` (
  `receivable_id` varchar(45) CHARACTER SET utf8 NOT NULL COMMENT '收款记录: 交易主键',
  `pay_order_id` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '支付单外键',
  `receivable_no` varchar(45) CHARACTER SET utf8 NOT NULL COMMENT '流水号',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `online_account_id` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '收款账号 账号id 外键',
  `offline_account_id` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '收款账号 线下',
  `receivable_account` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '收款账户',
  `comment` text COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除flag 0：未删除 1：已删除',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `pay_channel` varchar(45) DEFAULT NULL COMMENT '在支付渠道',
  `pay_channel_id` int(11) DEFAULT NULL COMMENT '在支付渠道id',
  `encloses` varchar(255) DEFAULT NULL COMMENT '收款单附件',
  PRIMARY KEY (`receivable_id`),
  UNIQUE KEY `bill_id_UNIQUE` (`receivable_id`),
  UNIQUE KEY `receivable_no_UNIQUE` (`receivable_no`) USING BTREE,
  KEY `rece_pay_order_id` (`pay_order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收款单';

--退款流水
DROP TABLE IF EXISTS `refund_bill`;

CREATE TABLE `refund_bill` (
  `refund_bill_id` varchar(45) CHARACTER SET utf8 NOT NULL COMMENT '退款单主键',
  `refund_order_id` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '退款单外键',
  `refund_bill_code` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '流水编号',
  `offline_account_id` bigint(10) DEFAULT NULL COMMENT '线下账号外键',
  `actual_return_price` decimal(10,2) DEFAULT NULL COMMENT '实付金额',
  `online_account_id` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '线上账号外键',
  `refund_comment` text,
  `create_time` datetime DEFAULT NULL,
  `del_flag` tinyint(4) DEFAULT NULL,
  `del_time` datetime DEFAULT NULL,
  `customer_account_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `pay_channel` varchar(45) DEFAULT NULL COMMENT '在支付渠道',
  `pay_channel_id` int(11) DEFAULT NULL COMMENT '在支付渠道id',
  PRIMARY KEY (`refund_bill_id`),
  UNIQUE KEY `refund_bill_id_UNIQUE` (`refund_bill_id`),
  UNIQUE KEY `refund_bill_code_UNIQUE` (`refund_bill_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款流水';

--退款单
DROP TABLE IF EXISTS `refund_order`;

CREATE TABLE `refund_order` (
  `refund_id` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `customer_detail_id` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '会员详情id外键',
  `update_time` datetime DEFAULT NULL,
  `return_order_code` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '退单编号',
  `return_price` decimal(20,2) DEFAULT NULL,
  `refund_code` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '退款单号',
  `pay_type` tinyint(4) DEFAULT NULL COMMENT '0 在线支付 1线下支付',
  `refund_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '退款单状态：1.待退款 2.拒绝退款 3.已完成',
  `refuse_reason` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '拒绝原因',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标志 0：未删除 1:已删除',
  `del_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '商家id',
  PRIMARY KEY (`refund_id`),
  UNIQUE KEY `refund_id_UNIQUE` (`refund_id`),
  UNIQUE KEY `refund_code_UNIQUE` (`refund_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='退款单';

SET FOREIGN_KEY_CHECKS=1;
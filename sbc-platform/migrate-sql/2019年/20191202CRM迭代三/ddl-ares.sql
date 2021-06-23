CREATE TABLE `replay_customer_funds` (
  `customer_funds_id` varchar(32) NOT NULL,
  `customer_id` varchar(32) NOT NULL COMMENT '会员ID',
  `customer_account` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '会员登录账号|手机号',
  `customer_name` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '会员名称',
  `account_balance` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
  `blocked_balance` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '冻结余额',
  `withdraw_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '可提现金额',
  `already_draw_amount` decimal(18,2) NOT NULL COMMENT '已提现金额',
  `income` bigint(30) NOT NULL DEFAULT '0' COMMENT '收入笔数',
  `amount_received` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '收入金额',
  `expenditure` bigint(30) NOT NULL DEFAULT '0' COMMENT '支出数',
  `amount_paid` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '支出金额',
  `is_distributor` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否分销员，0：否，1：是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`customer_funds_id`),
  UNIQUE KEY `index_customer_account` (`customer_account`) USING BTREE,
  UNIQUE KEY `index_customer_id` (`customer_id`) USING BTREE,
  KEY `index_customer_name` (`customer_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员资金表';

CREATE TABLE `replay_goods_customer_follow_action` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `goods_id` varchar(32) NOT NULL COMMENT '商品Id',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SKU编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `goods_num` bigint(10) NOT NULL COMMENT '购买数',
  `follow_flag` tinyint(4) DEFAULT NULL COMMENT '收藏标识0:all,1.采购,2:收藏',
  `follow_time` datetime DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`follow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1942 DEFAULT CHARSET=utf8 COMMENT='商品的客户收藏行为表（明细）';

CREATE TABLE `replay_purchase_action` (
  `purchase_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '会员Id',
  `goods_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品id',
  `goods_info_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT 'SKU编号',
  `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家编号',
  `goods_num` bigint(20) DEFAULT NULL COMMENT '购买数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `invitee_id` varchar(32) DEFAULT '0' COMMENT '默认为0、店铺精选时对应邀请人id（customerID）',
  PRIMARY KEY (`purchase_id`),
  KEY `idx_purchase_customerid_goodsinfoid` (`customer_id`,`goods_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7493 DEFAULT CHARSET=utf8mb4 COMMENT='购物车行为（明细）';
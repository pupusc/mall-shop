-- 社交分销会员提现管理表
-- ----------------------------
-- Table structure for customer_draw_cash
-- ----------------------------
DROP TABLE IF EXISTS `customer_draw_cash`;
CREATE TABLE `customer_draw_cash` (
  `draw_cash_id` varchar(32) NOT NULL COMMENT '提现id',
  `draw_cash_no` varchar(45) DEFAULT NULL COMMENT '提现单号',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `customer_id` varchar(45) DEFAULT NULL COMMENT '会员id',
  `customer_name` varchar(45) DEFAULT NULL COMMENT '会员名称',
  `customer_account` varchar(45) DEFAULT NULL COMMENT '会员账号',
  `draw_cash_channel` tinyint(4) DEFAULT NULL COMMENT '提现渠道 0:微信 1:支付宝',
  `draw_cash_account_name` varchar(45) DEFAULT NULL COMMENT '提现账户名称',
  `draw_cash_account` varchar(255) DEFAULT NULL COMMENT '提现账户账号',
  `draw_cash_sum` decimal(10,2) DEFAULT '0.00' COMMENT '本次提现金额',
  `draw_cash_remark` varchar(255) DEFAULT NULL COMMENT '提现备注',
  `audit_status` tinyint(4) DEFAULT NULL COMMENT '运营端审核状态(0:待审核,1:审核不通过,2:审核通过)',
  `reject_reason` varchar(255) DEFAULT NULL COMMENT '运营端驳回原因',
  `draw_cash_status` tinyint(4) DEFAULT NULL COMMENT '提现状态(0:未提现,1:提现失败,2:提现成功)',
  `draw_cash_failed_reason` varchar(255) DEFAULT NULL COMMENT '提现失败原因',
  `customer_operate_status` tinyint(4) DEFAULT NULL COMMENT '用户操作状态(0:已申请,1:已取消)',
  `finish_status` tinyint(4) DEFAULT NULL COMMENT '提现单完成状态(0:未完成,1:已完成)',
  `finish_time` datetime DEFAULT NULL COMMENT '提现单完成时间',
  `supplier_operate_id` varchar(45) DEFAULT NULL COMMENT '操作人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标志(0:未删除,1:已删除)',
  PRIMARY KEY (`draw_cash_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员提现管理';

CREATE TABLE `customer_funds` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '会员资金表';

CREATE TABLE `customer_funds_detail` (
  `customer_funds_detail_id` varchar(32) NOT NULL,
  `customer_id` varchar(32) NOT NULL,
  `business_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务编号',
  `funds_type` tinyint(4) NOT NULL COMMENT '资金类型 ''1''：分销佣金；''2''：佣金提现；''3''：邀新奖励',
  `draw_cash_id` varchar(32) DEFAULT NULL COMMENT '佣金提现id',
  `receipt_payment_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '收支金额',
  `funds_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '资金是否成功入账 0:否，1：成功',
  `account_balance` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`customer_funds_detail_id`),
  KEY `index_create_time` (`create_time`) USING BTREE,
  KEY `index_business_id` (`business_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
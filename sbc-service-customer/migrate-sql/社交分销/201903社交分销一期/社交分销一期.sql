-- 分销员
DROP TABLE IF EXISTS `distribution_customer`;
CREATE TABLE `distribution_customer` (
  `distribution_id` varchar(32) NOT NULL COMMENT '分销员标识UUID',
  `customer_id` varchar(32) NOT NULL COMMENT '会员ID',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '会员名称',
  `customer_account` varchar(20) DEFAULT NULL COMMENT '会员登录账号|手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人(后台新增分销员)',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `forbidden_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否禁止分销 0: 启用中  1：禁用中',
  `forbidden_reason` varchar(252) DEFAULT NULL COMMENT '禁用原因',
  `distributor_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否有分销员资格0：否，1：是',
  `invite_count` int(11) DEFAULT '0' COMMENT '邀新人数',
  `invite_available_count` int(11) DEFAULT '0' COMMENT '有效邀新人数',
  `reward_cash` decimal(20,2) DEFAULT '0.00' COMMENT '邀新奖金(元)',
  `reward_cash_not_recorded` decimal(20,2) DEFAULT '0.00' COMMENT '未入账邀新奖金(元)',
  `distribution_trade_count` int(11) DEFAULT '0' COMMENT '分销订单(笔)',
  `sales` decimal(20,2) DEFAULT '0.00' COMMENT '销售额(元) ',
  `commission` decimal(20,2) DEFAULT '0.00' COMMENT '分销佣金(元) ',
  `commission_not_recorded` decimal(20,2) DEFAULT '0.00' COMMENT '未入账分销佣金(元) ',
  PRIMARY KEY (`distribution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分销员';

SET FOREIGN_KEY_CHECKS = 1;

-- 分销员增加分销佣金字段

ALTER TABLE `distribution_customer`
ADD COLUMN `commission_total` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '佣金总额(元) ' AFTER `commission_not_recorded`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`distribution_id`) USING BTREE;

ALTER TABLE `distribution_customer`
ADD UNIQUE INDEX `customer_id`(`customer_id`) USING BTREE;

-- 余额提现会员表增加支付密码、支付密码错误次数和支付锁定时间
ALTER TABLE `customer`
ADD COLUMN `customer_pay_password` varchar(200) NULL COMMENT '支付密码' AFTER `login_lock_time`,
ADD COLUMN `pay_error_time` int(11) NULL COMMENT '支付密码错误次数' AFTER `customer_pay_password`,
ADD COLUMN `pay_lock_time` datetime(0) NULL COMMENT '支付锁定时间' AFTER `pay_error_time`;

-- 账户绑定微信增加微信昵称以及头像字段
ALTER TABLE `third_login_relation`
ADD COLUMN `nick_name` varchar(255) NULL COMMENT '微信昵称' AFTER `binding_time`,
ADD COLUMN `head_img_url` varchar(255) NULL COMMENT '微信头像路径' AFTER `nick_name`;

-- 邀新记录表
CREATE TABLE `invite_new_record` (
  `invited_customer_id` varchar(32) NOT NULL COMMENT '受邀人ID',
  `available_distribution` tinyint(4) DEFAULT '0' COMMENT '是否有效邀新，0：否，1：是',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `first_order_time` datetime DEFAULT NULL COMMENT '首次下单时间',
  `order_code` varchar(45) DEFAULT NULL COMMENT '订单编号',
  `order_complete_time` datetime DEFAULT NULL COMMENT '订单完成时间',
  `reward_recorded` tinyint(4) DEFAULT '0' COMMENT '奖励是否入账，0：否，1：是',
  `reward_cash_recorded_time` datetime DEFAULT NULL COMMENT '奖励入账时间',
  `reward_cash` decimal(20,2) DEFAULT '0.00' COMMENT '奖励金额(实际入账的金额)',
  `setting_coupons` varchar(350) DEFAULT '' COMMENT '后台配置的奖励优惠券id，多个以逗号分隔',
  `setting_amount` decimal(20,2) DEFAULT '0.00' COMMENT '后台配置的奖励金额',
  `request_customer_id` varchar(32) DEFAULT NULL COMMENT '邀请人id',
  `distributor` tinyint(4) DEFAULT NULL COMMENT '是否分销员，0：否 1：是',
  PRIMARY KEY (`invited_customer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邀新记录表';

ALTER TABLE `customer_detail`
ADD COLUMN `is_distributor` tinyint(2) DEFAULT '0' COMMENT '是否为分销员 0：否 1：是' AFTER `forbid_reason`;
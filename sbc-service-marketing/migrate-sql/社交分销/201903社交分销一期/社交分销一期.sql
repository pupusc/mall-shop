-- 分销设置表
create table distribution_setting
(
	setting_id varchar(32) not null comment '分销设置主键Id' primary key,
	distributor_name varchar(20) not null comment '分销员名称',
	shop_open_flag tinyint not null comment '是否开启分销小店 0：关闭，1：开启',
	shop_name varchar(20) not null comment '小店名称',
	shop_share_img varchar(255) not null comment '店铺分享图片',
	apply_flag tinyint not null comment '是否开启申请入口 0：关闭，1：开启',
	apply_type tinyint not null comment '申请条件 0：购买商品，1：邀请注册',
	recruit_img varchar(255) not null comment '招募海报',
	recruit_share_img varchar(255) null comment '招募邀新转发图片',
	recruit_desc text not null comment '招募规则说明',
	invite_count int null comment '邀请人数',
	limit_type tinyint not null comment '限制条件 0：不限，1：仅限有效邀新',
	commission_flag tinyint null comment '是否开启分销佣金 0：关闭，1：开启',
	goods_audit_flag tinyint null comment '是否开启分销商品审核 0：关闭，1：开启',
	invite_flag tinyint null comment '是否开启邀新奖励 0：关闭，1：开启',
	invite_img varchar(255) not null comment '邀新专题页海报',
	invite_share_img varchar(255) not null comment '邀新转发图片',
	invite_desc text not null comment '邀新奖励规则说明',
	reward_cash_flag tinyint not null comment '是否开启奖励现金 0：关闭，1：开启',
	reward_cash_count int null comment '奖励现金上限(人数)',
	reward_cash decimal(10,2) not null comment '每位奖励金额',
	reward_coupon_flag tinyint not null comment '是否开启奖励优惠券 0：关闭，1：开启',
	reward_coupon_count int not null comment '奖励优惠券上限(组数)',
	update_time datetime null comment '修改时间',
	update_person varchar(32) null comment '修改人',
	open_flag tinyint null comment '是否开启社交分销 0：关闭，1：开启',
	reward_limit_type tinyint null comment '邀新奖励限制 0：不限，1：仅限有效邀新',
	reward_cash_type tinyint null comment '奖励上限类型 0：不限， 1：限人数'
)
comment '分销设置表';

-- 分销招募礼包表
create table distribution_recruit_gift
(
	recruit_gift_id varchar(32) not null comment '主键id' primary key,
	goods_info_id varchar(32) not null comment '单品id'
)
comment '分销招募礼包表';

-- 分销邀新奖励优惠券表
create table distribution_reward_coupon
(
	reward_coupon_id varchar(32) not null comment '主键id' primary key,
	coupon_id varchar(32) not null comment '优惠券id',
	count int not null comment '每组张数'
)
comment '分销邀新奖励优惠券表';

-- 店铺分销设置表
create table distribution_store_setting
(
	setting_id varchar(32) not null comment '主键Id' primary key,
	store_id varchar(32) not null comment '店铺id',
	open_flag tinyint not null comment '是否开启社交分销 0：关闭，1：开启',
	commission_flag tinyint not null comment '是否开启通用分销佣金 0：关闭，1：开启',
	commission decimal(10,2) null comment '通用分销佣金'
)
comment '店铺分销设置表';

-- 分销记录表
CREATE TABLE `distribution_record` (
  `record_id` varchar(32) NOT NULL COMMENT '分销记录表主键',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT '货品Id',
  `trade_id` varchar(32) DEFAULT NULL COMMENT '订单交易号',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺Id',
  `company_id` bigint(20) DEFAULT NULL COMMENT '商家Id',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员Id',
  `distributor_id` varchar(32) DEFAULT NULL COMMENT '分销员标识UUID',
  `pay_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '付款时间',
  `finish_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '订单完成时间',
  `mission_received_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '佣金入账时间',
  `order_goods_price` decimal(20,2) DEFAULT NULL COMMENT '订单单个商品金额',
  `order_goods_count` bigint(20) DEFAULT NULL COMMENT '订单商品的数量',
  `commission_goods` decimal(20,2) DEFAULT NULL COMMENT '单个货品的佣金',
  `commission_state` tinyint(2) DEFAULT '0' COMMENT '佣金是否入账 0:未入账  1：已入账',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '是否删除的标志 0：未删除   1：已删除',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分销记录表';

-- 分销记录表时间去除ON UPDATE更新
ALTER TABLE `distribution_record` modify `pay_time` datetime DEFAULT NULL COMMENT '付款时间';
ALTER TABLE `distribution_record` modify `finish_time` datetime DEFAULT NULL COMMENT '订单完成时间';
ALTER TABLE `distribution_record` modify `mission_received_time` datetime DEFAULT NULL COMMENT '佣金入账时间';

-- 分销设置初始化脚本
INSERT INTO `sbc-marketing`.distribution_setting (setting_id, distributor_name, shop_open_flag, shop_name, shop_share_img, apply_flag, apply_type, recruit_img, recruit_share_img, recruit_desc, invite_count, limit_type, commission_flag, goods_audit_flag, invite_flag, invite_img, invite_share_img, invite_desc, reward_cash_flag, reward_cash_count, reward_cash, reward_coupon_flag, reward_coupon_count, update_time, update_person, open_flag, reward_limit_type, reward_cash_type) VALUES (''4028828169049d33016904ac75a60000'', ''分销员'', 0, '''', '''', 0, 0, '''', '''', '''', null, 0, 1, 0, 0, '''', '''', '''', 0, null, 0.00, 0, 0, null, '''', 0, 0, 0);
-- 企业信息
CREATE TABLE `sbc-customer`.`enterprise_info` (
  `enterprise_id` varchar(32) NOT NULL COMMENT '企业Id',
  `enterprise_name` varchar(100) NOT NULL COMMENT '企业名称',
  `social_credit_code` varchar(50) NOT NULL COMMENT '统一社会信用代码',
  `business_nature_type` int(11) NOT NULL COMMENT '企业性质',
  `business_industry_type` int(11) DEFAULT NULL COMMENT '企业行业',
  `business_employee_num` tinyint(4) DEFAULT NULL COMMENT '企业人数 0：1-49，1：50-99，2：100-499，3：500-999，4：1000以上',
  `business_license_url` varchar(1024) DEFAULT NULL COMMENT '营业执照地址',
  `customer_id` varchar(32) NOT NULL COMMENT '企业会员id',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '参加时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`enterprise_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业信息表';

ALTER TABLE `sbc-customer`.`customer`
ADD COLUMN `enterprise_check_state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '企业购会员审核状态  0：无状态 1：待审核 2：已审核 3：审核未通过',
ADD COLUMN `enterprise_check_reason` varchar(200) NULL COMMENT '企业购审核原因' AFTER `enterprise_check_state`;

-- goods_info 扩展企业购信息
ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN `enterprise_price` decimal(20, 2) NULL COMMENT '企业价' AFTER `sale_type`,
ADD COLUMN `enterprise_goods_audit` tinyint(4) default '0' NULL COMMENT '企业购商品审核状态 0：无状态 1:待审核 2:已审核通过 3:审核不通过 ' AFTER `enterprise_price`,
ADD COLUMN `enterprise_goods_audit_reason` varchar(100) NULL COMMENT '企业购商品审核不通过原因' AFTER `enterprise_goods_audit`;

-- 优惠券活动增加活动类型 7企业会员注册赠券注解
ALTER TABLE `sbc-marketing`.`coupon_activity`
MODIFY COLUMN `activity_type` tinyint(4) NOT NULL COMMENT '优惠券类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券，4权益赠券，5分销邀新赠券，6积分兑换券，7企业会员注册赠券' AFTER `end_time`;
-- 优惠券活动增加参加用户类型 -4：企业会员
ALTER TABLE `sbc-marketing`.`coupon_activity`
MODIFY COLUMN `join_level` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参加会员  -2 指定用户 -1:全部客户 0:全部等级 other:其他等级 -3：指定人群 -4：企业会员' AFTER `terminals`;
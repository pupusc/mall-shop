SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `plan_statistics_message`;
CREATE TABLE `plan_statistics_message` (
  `plan_id` bigint(11) NOT NULL COMMENT '运营计划id',
  `message_receive_num` int(10) DEFAULT NULL COMMENT '站内信收到人数',
  `message_receive_total` int(10) DEFAULT NULL COMMENT '站内信收到人次',
  `statistics_date` date DEFAULT NULL COMMENT '统计日期',
  `create_person` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='运营计划效果统计站内信收到人/次统计数据';

DROP TABLE IF EXISTS `customer_plan_send_count`;
CREATE TABLE `customer_plan_send_count` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '礼包优惠券发放统计id',
  `plan_id` bigint(11) DEFAULT NULL COMMENT '运营计划id',
  `gift_person_count` bigint(11) DEFAULT NULL COMMENT '发放礼包人数',
  `gift_count` bigint(11) DEFAULT NULL COMMENT '发放礼包次数',
  `coupon_person_count` bigint(11) DEFAULT NULL COMMENT '发放优惠券人数',
  `coupon_count` bigint(11) DEFAULT NULL COMMENT '发放优惠券张数',
  `coupon_person_use_count` bigint(11) DEFAULT NULL COMMENT '优惠券使用人数',
  `coupon_use_count` bigint(11) DEFAULT NULL COMMENT '优惠券使用张数',
  `coupon_use_rate` double(11,2) DEFAULT NULL COMMENT '优惠券转化率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `plan_id_index` (`plan_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权益礼包优惠券发放统计表';

-- customer_plan_covers_count运营计划转化效果--
DROP TABLE IF EXISTS `customer_plan_conversion`;
CREATE TABLE `customer_plan_conversion`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `plan_id` bigint(11) NULL DEFAULT NULL COMMENT '运营计划id',
  `visitors_uv_count` bigint(11) NULL COMMENT '访客数UV',
  `order_person_count` bigint(11) NULL COMMENT '下单人数',
  `order_count` bigint(11) NULL COMMENT '下单笔数',
  `pay_person_count` bigint(11) NULL COMMENT '付款人数',
  `pay_count` bigint(11) NULL COMMENT '付款笔数',
  `total_price` decimal(10, 2) NULL COMMENT '付款金额',
  `unit_price` decimal(10, 2) NULL COMMENT '客单价',
  `covers_count` bigint(11) NULL COMMENT '覆盖人数',
  `visitors_count` bigint(11) NULL COMMENT '访客人数',
  `covers_visitors_rate` double NULL COMMENT '访客人数/覆盖人数转换率',
  `pay_visitors_rate` double NULL COMMENT '付款人数/访客人数转换率',
  `pay_covers_rate` double NULL COMMENT '付款人数/覆盖人数转换率',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
);

SET FOREIGN_KEY_CHECKS = 1;
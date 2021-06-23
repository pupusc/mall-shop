-- 购物车改造
ALTER TABLE `purchase`
ADD COLUMN `invitee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '默认为0、店铺精选时对应邀请人id（customerID）' AFTER `create_time`;

ALTER TABLE `purchase`
DROP INDEX `customer_id`,
ADD UNIQUE INDEX `customer_id`(`customer_id`, `goods_info_id`, `invitee_id`) USING BTREE;

-- 消费记录表
CREATE TABLE `consume_record` (
  `order_id` varchar(32) NOT NULL COMMENT '订单id',
  `consume_sum` decimal(12,2) NOT NULL COMMENT '消费额',
  `valid_consume_sum` decimal(12,2) NOT NULL COMMENT '有效消费额',
  `store_id` varchar(32) DEFAULT NULL COMMENT '店铺id',
  `distribution_customer_id` varchar(32) DEFAULT NULL COMMENT '分销员id',
  `order_create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `customer_id` varchar(32) NOT NULL COMMENT '购买人的客户id',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '客户姓名',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消费记录表';
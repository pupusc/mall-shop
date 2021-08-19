
-- 分销任务临时表
CREATE TABLE `distribution_task_temp` (
  `id` varchar(32) NOT NULL,
  `customer_id` varchar(32) NOT NULL COMMENT '购买人id',
  `distribution_customer_id` varchar(32) DEFAULT NULL COMMENT '推荐分销员id',
  `first_valid_buy` tinyint(2) NOT NULL COMMENT '第一次有效完成订单',
  `order_id` varchar(32) NOT NULL COMMENT '订单id',
  `order_disable_time` datetime DEFAULT NULL COMMENT '订单可退时间',
  `distribution_order` tinyint(2) DEFAULT NULL COMMENT '分销订单',
  `return_order_num` tinyint(4) DEFAULT NULL COMMENT '退单中的数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分销任务临时表';





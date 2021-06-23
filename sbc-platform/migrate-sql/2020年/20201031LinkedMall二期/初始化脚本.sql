-- 商品标签表
CREATE TABLE `sbc-goods`.`goods_label` (
  `goods_label_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `label_name` varchar(45) NOT NULL COMMENT '标签名称',
  `label_visible` tinyint(4) DEFAULT '0' COMMENT '商品列表展示开关 0: 关闭 1:开启',
  `label_sort` int(11) DEFAULT '0' COMMENT '排序',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺id',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标识 0:未删除1:已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`goods_label_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_label_name` (`label_name`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品标签表';

-- 商品表增加标签关联
ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN `label_id_str` text CHARACTER SET utf8mb4 NULL COMMENT '标签id，以逗号拼凑' ;


UPDATE `sbc-message`.`message_send_node` SET `node_name` = '第三方支付失败自动退款通知', `node_title` = '第三方支付失败自动退款通知',
`node_content` = '很抱歉，您的订单{订单第一行商品名称}部分供应商商品采购失败，已为您自动退款~', `status` = 1, `create_time` = '2020-09-01 15:30:00',
`create_person` = NULL, `update_time` = '2020-09-01 15:30:00', `update_person` = '2c8080815cd3a74a015cd3ae86850001',
`del_flag` = 0, `send_sum` = 1, `open_sum` = 1, `route_name` = NULL, `node_type` = 3, `node_code` =
'THIRD_PAY_ERROR_AUTO_REFUND' WHERE `id` = 38;

UPDATE `sbc-message`.`push_send_node` SET `node_name` = '第三方支付失败自动退款通知', `node_type` = 3, `node_code` =
'THIRD_PAY_ERROR_AUTO_REFUND', `node_title` = '第三方支付失败自动退款通知', `node_context` = '很抱歉，您的订单{商品名称}部分供应商商品采购失败，已为您自动退款~',
 `expected_send_count` = 0, `actually_send_count` = 0, `open_count` = 0, `status` = 1, `del_flag` = 0,
 `create_person` = NULL, `create_time` = NULL, `update_person` = NULL, `update_time` = '2020-09-01 15:30:00' WHERE `id` = 38;
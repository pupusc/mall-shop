ALTER TABLE `sbc-goods`.`goods_info`
ADD INDEX(`goods_id`) USING BTREE;

ALTER TABLE `sbc-goods`.`goods_customer_price`
ADD INDEX `idx_goods_info_id`(`goods_info_id`) USING BTREE;

ALTER TABLE `sbc-goods`.`store_cate_goods_rela`
ADD INDEX(`goods_id`) USING BTREE;
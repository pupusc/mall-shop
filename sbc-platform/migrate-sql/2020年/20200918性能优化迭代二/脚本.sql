-- goodsInfo 索引
ALTER TABLE `sbc-goods`.`goods_info`
ADD INDEX `idx_goods_info_del_flag` (`del_flag`) USING BTREE ,
ADD INDEX `idx_goods_info_goods_id` (`goods_id`) USING BTREE ;
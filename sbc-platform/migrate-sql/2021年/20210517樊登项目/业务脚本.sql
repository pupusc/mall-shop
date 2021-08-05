

-- goods_info 新增字段
ALTER TABLE `sbc-goods`.`goods_info` ADD COLUMN combined_commodity tinyint(1) DEFAULT '0' COMMENT '是否是组合商品，0：否，1：是';
ALTER TABLE `sbc-goods`.`goods_info` ADD COLUMN erp_goods_no VARCHAR(45) DEFAULT NULL COMMENT 'spu编码(ERP)';
-- 商品库新增字段
ALTER TABLE `sbc-goods`.`standard_goods`
ADD COLUMN goods_source TINYINT ( 4 ) DEFAULT 1 COMMENT '商品来源，0供应商，1商家',
ADD COLUMN provider_name VARCHAR ( 45 ) DEFAULT NULL COMMENT '供应商名称';

-- 商品库新增字段
ALTER TABLE `sbc-goods`.`standard_goods`
ADD COLUMN supply_price DECIMAL ( 20, 2 ) DEFAULT NULL COMMENT '供货价',
ADD COLUMN recommended_retail_price DECIMAL ( 20, 2 ) DEFAULT NULL COMMENT '建议零售价';

--商品 新增字段
ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN provider_goods_id VARCHAR ( 45 ) DEFAULT NULL COMMENT '所属供应商商品Id',
ADD COLUMN provider_id  BIGINT ( 20 ) DEFAULT NULL COMMENT '供应商Id',
ADD COLUMN provider_name  VARCHAR ( 45 ) DEFAULT NULL COMMENT '供应商名称',
ADD COLUMN recommended_retail_price DECIMAL ( 20, 2 ) DEFAULT NULL COMMENT '建议零售价';

--商品详情表  新增字段
ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN provider_goods_info_id VARCHAR ( 45 ) DEFAULT NULL COMMENT '所属供应商商品skuId',
ADD COLUMN provider_id  BIGINT ( 20 ) DEFAULT NULL COMMENT '供应商Id';

--商品库SKU实体类  新增字段
ALTER TABLE `sbc-goods`.`standard_sku`
ADD COLUMN provider_goods_info_id VARCHAR ( 45 ) DEFAULT NULL COMMENT '所属供应商商品skuId',
ADD COLUMN supply_price DECIMAL ( 20, 2 ) DEFAULT NULL COMMENT '供货价';

--结算单 新增字段
ALTER TABLE settlement ADD store_type TINYINT ( 4 ) NOT NULL DEFAULT 0 COMMENT '商家类型0供应商，1商家';
ALTER TABLE settlement ADD provider_price DECIMAL ( 20, 2 ) DEFAULT 0 COMMENT '供货价总额';


--商品详情表  新增字段
ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN goods_source TINYINT ( 4 ) DEFAULT 1 COMMENT '商品来源，0供应商，1商家';

-- 商品库新增字段
ALTER TABLE `sbc-goods`.`standard_sku`
ADD COLUMN stock BIGINT (10) DEFAULT 0 COMMENT '库存';





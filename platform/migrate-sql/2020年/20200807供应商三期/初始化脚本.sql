-- 新增字段
ALTER TABLE `sbc-goods`.goods_info ADD COLUMN vendibility tinyint(4) DEFAULT 1 COMMENT '是否可售，0不可售，1可售';
-- 新增字段
ALTER TABLE `sbc-goods`.goods ADD COLUMN vendibility tinyint(4) DEFAULT 1 COMMENT '是否可售，0不可售，1可售';
-- 修改备注
ALTER TABLE `sbc-goods`.goods MODIFY goods_source TINYINT(4) COMMENT '商品来源，0供应商，1商家,2linkedmall,3平台';
-- 商品库新增字段
ALTER TABLE `sbc-goods`.standard_goods ADD COLUMN goods_no varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  COMMENT 'SPU编码';
ALTER TABLE `sbc-goods`.standard_goods ADD COLUMN `store_id` bigint(20) NULL COMMENT '店铺id';
-- 商品库新增字段
ALTER TABLE `sbc-goods`.standard_sku ADD COLUMN goods_info_no varchar(45) CHARACTER SET utf8mb4 COMMENT 'SKU编码';

-- 刷store_id(可不用代码已兼容)
UPDATE `sbc-goods`.standard_goods sg1,
( SELECT sgr.store_id, sgr.standard_id FROM standard_goods_rel sgr LEFT JOIN standard_goods sg ON sg.goods_id = sgr.standard_id WHERE sg.goods_source = 0 ) a
SET sg1.store_id = a.store_id
WHERE
	sg1.goods_id = a.standard_id;
-- 刷spu
UPDATE `sbc-goods`.standard_goods sg1,(SELECT g.goods_id ,g.goods_no from goods g) b,
( SELECT sgr.goods_id, sgr.standard_id FROM standard_goods_rel sgr LEFT JOIN standard_goods sg ON sg.goods_id = sgr.standard_id WHERE sg.goods_source = 0 ) a
SET sg1.goods_no = b.goods_no
WHERE
	sg1.goods_id = a.standard_id and a.goods_id = b.goods_id;

-- 刷su
UPDATE `sbc-goods`.standard_sku ss,
(select g.goods_info_no ,g.goods_info_id from goods_info g where g.goods_source = 0 ) a
set ss.goods_info_no = a.goods_info_no
where ss.provider_goods_info_id = a.goods_info_id
and ss.del_flag = 0;
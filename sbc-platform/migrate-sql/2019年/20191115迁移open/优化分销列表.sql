
-- 优化分销商品列表查询，冗余销售类型字段到goods-info表
-- 在goods-info表中冗余销售类型字段，方便分销商品查询
alter table sbc-goods.goods_info add column sale_type tinyint(1) default '0' comment '销售类别(0:批发,1:零售)';
alter table s2b_statistics.replay_goods_info add column sale_type tinyint(1) default '0' comment '销售类别(0:批发,1:零售)';
-- 初始化数据
update goods_info info
left join goods g on info.goods_id = g.goods_id
set info.sale_type = g.sale_type;
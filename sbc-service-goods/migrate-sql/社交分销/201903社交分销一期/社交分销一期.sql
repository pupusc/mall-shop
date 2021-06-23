-- 分销商品相关
alter table goods_info add
(
	distribution_commission decimal(11,2) null comment '分销佣金',
	distribution_sales_count int null comment '分销销量',
	distribution_goods_audit tinyint default '0' null comment '分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销',
	distribution_goods_audit_reason varchar(100) null comment '分销商品审核不通过或禁止分销原因',
	cate_id bigint default '0' not null comment '商品分类Id',
	brand_id bigint null comment '品牌Id'
);

update goods_info info
left join goods g on info.goods_id = g.goods_id
set info.cate_id = g.cate_id,
		info.brand_id = g.brand_id;

CREATE TABLE `distributor_goods_info` (
  `id` varchar(32) NOT NULL,
  `customer_id` varchar(32) NOT NULL COMMENT '分销员对应的会员ID',
  `goods_info_id` varchar(32) NOT NULL COMMENT '分销商品SKU编号',
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU编号',
  `store_id` bigint(20) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0：否，1：是',
  `sequence` int(3) NOT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_customer_id_goods_info_id` (`customer_id`,`goods_info_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分销员商品表';
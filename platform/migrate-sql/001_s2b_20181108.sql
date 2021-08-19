-- 商品补充字段-bail
ALTER TABLE `goods`
ADD COLUMN `store_id`  bigint(20) NULL COMMENT '店铺标识' AFTER `level_discount_flag`,
ADD COLUMN `submit_time`  datetime NULL COMMENT '提交审核时间' AFTER `is_third`,
ADD COLUMN `forbid_status`  tinyint(4) NULL COMMENT '禁售状态,0:未禁售 1:禁售中' AFTER `audit_reason`,
ADD COLUMN `forbid_reason`  varchar(255) NULL COMMENT '禁售原因' AFTER `forbid_status`;


-- 店铺等级折扣表-bail
create table store_customer_level_discount
(
   id                   varchar(40) not null comment '主键UUID',
   store_id             bigint(20) comment '店铺标识',
   company_info_id      int(11) comment '商家标识',
   customer_level_id    bigint(10) comment '客户等级标识',
   discount_rate        decimal(8,2) comment '折扣率',
   primary key (id)
);
alter table store_customer_level_discount comment '店铺等级折扣表';


-- 店铺客户关系表-bail
create table store_customer_rela
(
   id                   varchar(40) not null comment '主键UUID',
   customer_id          national varchar(32) comment '会员标识',
   store_id             bigint(20) comment '店铺标识',
   company_info_id      int(11) comment '商家标识',
   customer_level_id    bigint(10) comment '客户等级标识',
   primary key (id)
);
alter table store_customer_rela comment '店铺客户关系表';


-- 商品店铺分类关联表-bail
create table store_cate_goods_rela
(
   goods_id             varchar(32) not null comment '商品标识',
   store_cate_id        bigint(20) not null comment '店铺分类标识',
   primary key (goods_id, store_cate_id)
);
alter table store_cate_goods_rela comment '商品店铺分类关联表(允许一个商品有多个店铺分类)';


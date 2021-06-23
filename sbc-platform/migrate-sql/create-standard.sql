drop table if exists s2b.standard_goods;

/*==============================================================*/
/* Table: standard_goods                                        */
/*==============================================================*/
create table s2b.standard_goods
(
   goods_id             varchar(32) not null,
   cate_id              bigint(20) not null comment '商品分类Id',
   brand_id             bigint(20) comment '品牌Id',
   goods_name           varchar(255) not null comment '商品标题',
   goods_subtitle       varchar(255) comment '商品副标题',
   goods_unit           varchar(45) comment '计量单位',
   goods_img            varchar(255) comment '商品主图片',
   goods_weight         decimal(20,2) comment '商品重量',
   market_price         decimal(20,2) comment '市场价',
   cost_price           decimal(20,2) comment '成本价',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             tinyint(4) not null comment '删除标识,0:未删除1:已删除',
   more_spec_flag       tinyint(4) comment '规格类型,0:单规格1:多规格',
   goods_detail         text comment '商品详情',
   goods_mobile_detail  text comment '移动端图文详情',
   primary key (goods_id),
   key product_id_UNIQUE (goods_id)
);

alter table s2b.standard_goods comment 'SPU表';


drop table if exists s2b.standard_sku;

/*==============================================================*/
/* Table: standard_sku                                          */
/*==============================================================*/
create table s2b.standard_sku
(
   goods_info_id        varchar(32) not null comment '商品库SkuId',
   goods_id             varchar(32) not null comment '商品库SpuId',
   goods_info_name      varchar(255) not null comment '商品名称',
   goods_info_img       varchar(255) comment 'SKU图片',
   market_price         decimal(20,2) comment '市场价',
   cost_price           decimal(20,2) comment '成本价',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             tinyint(4) comment '删除标识,0:未删除1:已删除',
   primary key (goods_info_id),
   key goods_info_id_UNIQUE (goods_info_id)
);

alter table s2b.standard_sku comment '商品库-SKU';



drop table if exists s2b.standard_image;

/*==============================================================*/
/* Table: standard_image                                        */
/*==============================================================*/
create table s2b.standard_image
(
   image_id             bigint(20) not null auto_increment,
   goods_id             varchar(32) comment '商品库SPU编号',
   goods_info_id        varchar(32) comment '商品库SKU编号',
   artwork_url          varchar(255) comment '原图地址',
   middle_url           varchar(255) comment '中图地址',
   thumb_url            varchar(255) comment '小图地址',
   big_url              varchar(255) comment '大图地址',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             tinyint(4) comment '删除标识,0:未删除1:已删除',
   primary key (image_id)
);

alter table s2b.standard_image comment '商品库图片表';



drop table if exists s2b.standard_spec;

/*==============================================================*/
/* Table: standard_spec                                         */
/*==============================================================*/
create table s2b.standard_spec
(
   spec_id              bigint(20) not null auto_increment,
   goods_id             varchar(32) comment '商品库SPU编号',
   spec_name            varchar(45) not null comment '商品库规格名称',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             tinyint(4) comment '删除标识,0:未删除1:已删除',
   primary key (spec_id)
);

alter table s2b.standard_spec comment '商品库规格表';



drop table if exists s2b.standard_spec_detail;

/*==============================================================*/
/* Table: standard_spec_detail                                  */
/*==============================================================*/
create table s2b.standard_spec_detail
(
   spec_detail_id       bigint(20) not null auto_increment comment '规格值ID',
   goods_id             varchar(32) not null comment '商品库SPU编号',
   spec_id              bigint(20) not null comment '规格ID',
   detail_name          varchar(45) not null comment '规格值名称',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             tinyint(4) comment '删除标识,0:未删除1:已删除',
   primary key (spec_detail_id)
);

alter table s2b.standard_spec_detail comment '商品库规格值关联表';



drop table if exists s2b.standard_sku_spec_detail_rel;

/*==============================================================*/
/* Table: standard_sku_spec_detail_rel                          */
/*==============================================================*/
create table s2b.standard_sku_spec_detail_rel
(
   spec_detail_rel_id   bigint(20) not null auto_increment,
   goods_id             varchar(32) not null comment '商品库SPU编号',
   goods_info_id        varchar(32) not null comment '商品库SKU编号',
   spec_detail_id       bigint(20) not null,
   spec_id              bigint(20) not null comment '规格ID',
   detail_name          varchar(45) comment '规格值名称',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             tinyint(4) comment '删除标识,0:未删除1:已删除',
   primary key (spec_detail_rel_id)
);

alter table s2b.standard_sku_spec_detail_rel comment '商品库SKU规格值关联表';


drop table if exists s2b.standard_prop_detail_rel;

/*==============================================================*/
/* Table: standard_prop_detail_rel                              */
/*==============================================================*/
create table s2b.standard_prop_detail_rel
(
   rel_id               bigint(20) not null auto_increment comment '关联表主键',
   goods_id             varchar(32) not null comment '商品库SPU标识',
   detail_id            bigint(20) not null comment '属性值id',
   prop_id              bigint(20) not null comment '属性id',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   del_flag             tinyint(4) comment '删除标识,0:未删除1:已删除',
   primary key (rel_id)
);

alter table s2b.standard_prop_detail_rel comment '商品库SPU与属性值关联表';



drop table if exists s2b.standard_goods_rel;

/*==============================================================*/
/* Table: standard_goods_rel                                    */
/*==============================================================*/
create table s2b.standard_goods_rel
(
   rel_id               bigint(20) not null auto_increment comment '关联表主键',
   goods_id             varchar(32) not null comment '普通SPU编号',
   standard_id          varchar(32) not null comment '商品库SPU编号',
   store_id             bigint(20) not null comment '店铺编号',
   primary key (rel_id)
);

alter table s2b.standard_goods_rel comment '商品库与店铺关联表';

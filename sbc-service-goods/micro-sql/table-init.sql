-- 数据库：sbc-goods
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `check_brand` (
  `check_brand_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(45) DEFAULT NULL COMMENT '品牌名称',
  `nick_name` varchar(45) DEFAULT NULL COMMENT '品牌别名',
  `logo` varchar(500) DEFAULT NULL COMMENT '品牌logo',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '审核状态(0:未审核,1:通过,2:驳回)',
  PRIMARY KEY (`check_brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='待审核品牌(商家申请品牌)';

CREATE TABLE `contract_brand` (
  `contract_brand_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '签约品牌主键',
  `store_id` varchar(32) DEFAULT NULL COMMENT '店铺主键',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '商品品牌标识',
  `check_brand_id` bigint(20) DEFAULT NULL COMMENT '待审核品牌id',
  `authorize_pic` text COMMENT '授权图片路径',
  PRIMARY KEY (`contract_brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='签约品牌';

CREATE TABLE `contract_cate` (
  `contract_cate_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '签约分类主键',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺主键',
  `cate_id` bigint(20) DEFAULT NULL COMMENT '商品分类标识',
  `cate_rate` decimal(8,2) DEFAULT NULL COMMENT '分类扣率',
  `qualification_pics` text COMMENT '资质图片路径',
  PRIMARY KEY (`contract_cate_id`),
  UNIQUE KEY `store_id` (`store_id`,`cate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='签约分类';

CREATE TABLE `freight_template_goods` (
  `freight_temp_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '运费模板id',
  `freight_temp_name` varchar(60) DEFAULT NULL COMMENT '运费模板名称',
  `province_id` bigint(10) DEFAULT NULL COMMENT '发货地-省份',
  `city_id` bigint(10) DEFAULT NULL COMMENT '发货地-地市',
  `area_id` bigint(10) DEFAULT NULL COMMENT '发货地-区镇',
  `freight_free_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否包邮(0:不包邮,1:包邮)',
  `valuation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '计价方式(0:按件数,1:按重量,2:按体积)',
  `deliver_way` tinyint(1) NOT NULL DEFAULT '1' COMMENT '运送方式(1:快递配送)',
  `specify_term_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否指定条件包邮(0:不指定,1:指定)',
  `store_id` bigint(20) NOT NULL COMMENT '店铺标识',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `default_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否默认(0:否,1:是)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`freight_temp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='单品运费模板';

CREATE TABLE `freight_template_goods_express` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `freight_temp_id` bigint(12) DEFAULT NULL COMMENT '运费模板id',
  `destination_area` varchar(3000) NOT NULL COMMENT '配送地id(逗号分隔)',
  `destination_area_name` varchar(3000) NOT NULL COMMENT '配送地名称(逗号分隔)',
  `valuation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '计价方式(0:按件数,1:按重量,2:按体积)',
  `freight_start_num` decimal(10,2) NOT NULL DEFAULT '1.00' COMMENT '首件/重/体积',
  `freight_start_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '对应于首件/重/体积的起步价',
  `freight_plus_num` decimal(10,2) NOT NULL DEFAULT '1.00' COMMENT '续件/重/体积',
  `freight_plus_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '对应于续件/重/体积的价格',
  `default_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否默认(0:否,1:是)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`id`),
  KEY `freight_temp_id` (`freight_temp_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='单品运费模板快递运送';


CREATE TABLE `freight_template_goods_free` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `freight_temp_id` bigint(12) DEFAULT NULL COMMENT '运费模板id',
  `destination_area` varchar(3000) NOT NULL COMMENT '配送地id(逗号分隔)',
  `destination_area_name` varchar(3000) NOT NULL COMMENT '配送地名称(逗号分隔)',
  `deliver_way` tinyint(1) NOT NULL DEFAULT '1' COMMENT '运送方式(1:快递配送)',
  `valuation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '计价方式(0:按件数,1:按重量,2:按体积)',
  `condition_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '包邮条件类别(0:件/重/体积计价方式,1:金额,2:计价方式+金额)',
  `condition_one` decimal(12,2) DEFAULT NULL COMMENT '包邮条件1(件/重/体积)',
  `condition_two` decimal(12,2) DEFAULT NULL COMMENT '包邮条件2(金额)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`id`),
  KEY `freight_temp_id` (`freight_temp_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='单品运费模板指定包邮条件';

CREATE TABLE `freight_template_store` (
  `freight_temp_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '运费模板id',
  `freight_temp_name` varchar(60) DEFAULT NULL COMMENT '运费模板名称',
  `deliver_way` tinyint(1) NOT NULL DEFAULT '1' COMMENT '运送方式(1:快递配送)',
  `destination_area` varchar(3000) NOT NULL COMMENT '配送地id(逗号分隔)',
  `destination_area_name` varchar(3000) NOT NULL COMMENT '配送地名称(逗号分隔)',
  `freight_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '运费计费规则(0:满金额包邮,1:固定运费)',
  `satisfy_price` decimal(10,2) DEFAULT NULL COMMENT '满多少金额包邮',
  `satisfy_freight` decimal(10,2) DEFAULT NULL COMMENT '不满金额的运费',
  `fixed_freight` decimal(10,2) DEFAULT NULL COMMENT '固定的运费',
  `store_id` bigint(20) NOT NULL COMMENT '店铺标识',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `default_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否默认(0:否,1:是)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`freight_temp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='店铺运费模板';

CREATE TABLE `goods` (
  `goods_id` varchar(32) NOT NULL,
  `sale_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '销售类别(0:批发,1:零售)',
  `cate_id` bigint(20) NOT NULL COMMENT '商品分类Id',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌Id',
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品标题',
  `goods_subtitle` varchar(255) DEFAULT NULL COMMENT '商品副标题',
  `goods_no` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'SPU编码',
  `goods_unit` varchar(45) DEFAULT NULL COMMENT '计量单位',
  `goods_img` varchar(255) DEFAULT NULL COMMENT '商品主图片',
  `goods_video` varchar(255) DEFAULT NULL COMMENT '商品视频',
  `goods_weight` decimal(20,3) DEFAULT NULL COMMENT '商品重量',
  `goods_cubage` decimal(20,6) DEFAULT NULL COMMENT '商品体积',
  `freight_temp_id` bigint(12) DEFAULT NULL COMMENT '单品运费模板id',
  `market_price` decimal(20,2) DEFAULT NULL COMMENT '市场价',
  `line_price` decimal(20,2) DEFAULT NULL COMMENT '划线价',
  `cost_price` decimal(20,2) DEFAULT NULL COMMENT '成本价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `added_time` datetime DEFAULT NULL COMMENT '上下架时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  `added_flag` tinyint(4) NOT NULL COMMENT '上下架状态,0:下架1:上架2:部分上架',
  `more_spec_flag` tinyint(4) DEFAULT NULL COMMENT '规格类型,0:单规格1:多规格',
  `price_type` tinyint(4) DEFAULT NULL COMMENT '设价类型,0:按客户1:按订货量2:按市场价',
  `allow_price_set` tinyint(1) DEFAULT '1' COMMENT '订货量设价时,是否允许sku独立设阶梯价(0:不允许,1:允许)',
  `custom_flag` tinyint(4) DEFAULT NULL COMMENT '按客户单独定价,0:否1:是',
  `level_discount_flag` tinyint(4) DEFAULT NULL COMMENT '叠加客户等级折扣，0:否1:是',
  `store_id` bigint(20) NOT NULL COMMENT '店铺标识',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `supplier_name` varchar(255) DEFAULT NULL COMMENT '商家名称',
  `submit_time` datetime DEFAULT NULL COMMENT '提交审核时间',
  `audit_status` tinyint(4) NOT NULL COMMENT '审核状态,0:未审核1 审核通过2审核失败3禁用中',
  `audit_reason` varchar(255) DEFAULT NULL COMMENT '审核原因',
  `goods_detail` text COMMENT '商品详情',
  `goods_mobile_detail` text COMMENT '移动端图文详情',
  `company_type` tinyint(4) DEFAULT NULL COMMENT '自营标识',
  PRIMARY KEY (`goods_id`),
  UNIQUE KEY `product_id_UNIQUE` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SPU表';

CREATE TABLE `goods_brand` (
  `brand_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(45) DEFAULT NULL,
  `pin_yin` varchar(45) DEFAULT NULL COMMENT '拼音',
  `s_pin_yin` varchar(45) DEFAULT NULL COMMENT '简拼',
  `store_id` bigint(20) DEFAULT '0' COMMENT '店铺id',
  `brand_nick_name` varchar(50) DEFAULT NULL,
  `brand_logo` varchar(150) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品品牌表';

CREATE TABLE `goods_cate` (
  `cate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品分类主键',
  `cate_name` varchar(45) DEFAULT NULL COMMENT '分类名称',
  `cate_parent_id` bigint(20) DEFAULT NULL COMMENT '父分类ID',
  `cate_img` varchar(255) DEFAULT NULL COMMENT '分类图片',
  `cate_path` varchar(1000) NOT NULL COMMENT '分类层次路径,例1|01|001',
  `cate_grade` tinyint(4) NOT NULL COMMENT '分类层级',
  `cate_rate` decimal(8,2) DEFAULT NULL COMMENT '分类扣率',
  `is_parent_cate_rate` tinyint(4) DEFAULT NULL COMMENT '是否使用上级类目扣率 0 否  1 是',
  `pin_yin` varchar(45) DEFAULT NULL COMMENT '拼音',
  `s_pin_yin` varchar(45) DEFAULT NULL COMMENT '简拼',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(4) NOT NULL COMMENT '是否默认,0:否1:是',
  PRIMARY KEY (`cate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品分类表';

CREATE TABLE `goods_customer_num` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品的客户全局购买数(暂时无用)',
  `customer_id` varchar(32) NOT NULL COMMENT '会员编号',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SKU编号',
  `goods_num` bigint(10) NOT NULL COMMENT '购买数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品的客户全局购买数';

CREATE TABLE `goods_customer_price` (
  `customer_price_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU编号',
  `customer_id` varchar(32) NOT NULL COMMENT '客户ID',
  `price` decimal(20,2) DEFAULT NULL COMMENT '订货价',
  `count` bigint(10) DEFAULT NULL COMMENT '起订量',
  `max_count` bigint(10) DEFAULT NULL COMMENT '限定量',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT 'SKU编号',
  `type` tinyint(4) NOT NULL COMMENT '0:spu,1:sku',
  PRIMARY KEY (`customer_price_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品客户价格表';

CREATE TABLE `goods_image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) DEFAULT NULL COMMENT 'SPU编号',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT 'SKU编号',
  `artwork_url` varchar(255) DEFAULT NULL COMMENT '原图地址',
  `middle_url` varchar(255) DEFAULT NULL COMMENT '中图地址',
  `thumb_url` varchar(255) DEFAULT NULL COMMENT '小图地址',
  `big_url` varchar(255) CHARACTER SET big5 DEFAULT NULL COMMENT '大图地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品图片表';

CREATE TABLE `goods_info` (
  `goods_info_id` varchar(32) NOT NULL,
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_info_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品名称',
  `goods_info_no` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'SPU编码',
  `goods_info_img` varchar(255) DEFAULT NULL COMMENT 'SPU图片',
  `goods_info_barcode` varchar(45) DEFAULT NULL COMMENT '条形码',
  `stock` bigint(10) DEFAULT NULL COMMENT '库存',
  `market_price` decimal(20,2) DEFAULT NULL COMMENT '市场价',
  `cost_price` decimal(20,2) DEFAULT NULL COMMENT '成本价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `added_time` datetime DEFAULT NULL COMMENT ' 上下架时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `added_flag` tinyint(4) DEFAULT NULL COMMENT '上下架状态,0:下架1:上架',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '客户ID',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `custom_flag` tinyint(4) DEFAULT NULL COMMENT '按客户单独定价,0:否1:是',
  `level_discount_flag` tinyint(4) DEFAULT NULL COMMENT '叠加客户等级折扣，0:否1:是',
  `store_id` bigint(20) NOT NULL COMMENT '店铺Id',
  `audit_status` tinyint(4) NOT NULL,
  `company_type` tinyint(4) NOT NULL,
  `alone_flag` tinyint(4) NOT NULL DEFAULT '0',
  `small_program_code` varchar(250) DEFAULT NULL COMMENT '商品详情小程序码',
  PRIMARY KEY (`goods_info_id`),
  UNIQUE KEY `goods_info_id_UNIQUE` (`goods_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SKU货品表';

CREATE TABLE `goods_info_spec_detail_rel` (
  `spec_detail_rel_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU编号',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SKU编号',
  `spec_detail_id` bigint(20) NOT NULL,
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `detail_name` varchar(45) DEFAULT NULL COMMENT ' 规格值名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`spec_detail_rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='SKU与规格值关联表';

CREATE TABLE `goods_interval_price` (
  `interval_price_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU编号',
  `count` bigint(10) DEFAULT NULL COMMENT '订货区间',
  `price` decimal(20,2) DEFAULT NULL COMMENT '订货价',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT 'SKU编号',
  `type` tinyint(4) NOT NULL COMMENT '0:spu,1:sku',
  PRIMARY KEY (`interval_price_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品订货区间价格表';

CREATE TABLE `goods_level_price` (
  `level_price_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU编号',
  `level_id` bigint(20) NOT NULL COMMENT '客户等级ID',
  `price` decimal(20,2) DEFAULT NULL COMMENT '订货价',
  `count` bigint(10) DEFAULT NULL COMMENT '起订量',
  `max_count` bigint(10) DEFAULT NULL COMMENT '限定量',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT 'SKU编号',
  `type` tinyint(4) NOT NULL COMMENT '0:spu,1:sku',
  PRIMARY KEY (`level_price_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品级别价格表';

CREATE TABLE `goods_prop` (
  `prop_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性id',
  `cate_id` bigint(20) DEFAULT NULL COMMENT '商品分类外键',
  `prop_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '属性名称',
  `index_flag` tinyint(4) DEFAULT '1' COMMENT '是否开启索引',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0:未删除1:已删除',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`prop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';


CREATE TABLE `goods_prop_detail` (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性值id',
  `prop_id` bigint(20) DEFAULT NULL COMMENT '属性外键',
  `detail_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '属性值名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0:未删除1:已删除',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品属性值表';

CREATE TABLE `goods_prop_detail_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联表主键',
  `goods_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'SPU标识',
  `detail_id` bigint(20) NOT NULL COMMENT '属性值id',
  `prop_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='SPU与属性值关联表';

CREATE TABLE `goods_spec` (
  `spec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) DEFAULT NULL COMMENT 'SPU编号',
  `spec_name` varchar(45) NOT NULL COMMENT '规格名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`spec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品规格关联表';

CREATE TABLE `goods_spec_detail` (
  `spec_detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格值ID',
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU编号',
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `detail_name` varchar(45) NOT NULL COMMENT '规格值名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`spec_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT=' 商品规格值关联表';

CREATE TABLE `goods_tab_rela` (
  `goods_id` varchar(32) NOT NULL COMMENT 'spuId',
  `tab_id` bigint(20) NOT NULL COMMENT '详情模板id',
  `tab_detail` text COMMENT '内容详情',
  PRIMARY KEY (`goods_id`,`tab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品详情模板关联表';

CREATE TABLE `standard_goods` (
  `goods_id` varchar(32) NOT NULL,
  `cate_id` bigint(20) NOT NULL COMMENT '商品分类Id',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌Id',
  `goods_name` varchar(255) NOT NULL COMMENT '商品标题',
  `goods_subtitle` varchar(255) DEFAULT NULL COMMENT '商品副标题',
  `goods_unit` varchar(45) DEFAULT NULL COMMENT '计量单位',
  `goods_img` varchar(255) DEFAULT NULL COMMENT '商品主图片',
  `goods_video` varchar(255) DEFAULT NULL COMMENT '商品视频',
  `goods_weight` decimal(20,3) DEFAULT NULL COMMENT '商品重量',
  `goods_cubage` decimal(20,6) DEFAULT NULL COMMENT '商品体积',
  `market_price` decimal(20,2) DEFAULT NULL COMMENT '市场价',
  `cost_price` decimal(20,2) DEFAULT NULL COMMENT '成本价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  `more_spec_flag` tinyint(4) DEFAULT NULL COMMENT '规格类型,0:单规格1:多规格',
  `goods_detail` text COMMENT '商品详情',
  `goods_mobile_detail` text COMMENT '移动端图文详情',
  PRIMARY KEY (`goods_id`),
  KEY `product_id_UNIQUE` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SPU表';

CREATE TABLE `standard_goods_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联表主键',
  `goods_id` varchar(32) NOT NULL COMMENT '普通SPU编号',
  `standard_id` varchar(32) NOT NULL COMMENT '商品库SPU编号',
  `store_id` bigint(20) NOT NULL COMMENT '店铺编号',
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品库与店铺关联表';

CREATE TABLE `standard_image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) DEFAULT NULL COMMENT '商品库SPU编号',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT '商品库SKU编号',
  `artwork_url` varchar(255) DEFAULT NULL COMMENT '原图地址',
  `middle_url` varchar(255) DEFAULT NULL COMMENT '中图地址',
  `thumb_url` varchar(255) DEFAULT NULL COMMENT '小图地址',
  `big_url` varchar(255) DEFAULT NULL COMMENT '大图地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品库图片表';

CREATE TABLE `standard_prop_detail_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联表主键',
  `goods_id` varchar(32) NOT NULL COMMENT '商品库SPU标识',
  `detail_id` bigint(20) NOT NULL COMMENT '属性值id',
  `prop_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品库SPU与属性值关联表';

CREATE TABLE `standard_sku` (
  `goods_info_id` varchar(32) NOT NULL COMMENT '商品库SkuId',
  `goods_id` varchar(32) NOT NULL COMMENT '商品库SpuId',
  `goods_info_name` varchar(255) NOT NULL COMMENT '商品名称',
  `goods_info_img` varchar(255) DEFAULT NULL COMMENT 'SKU图片',
  `market_price` decimal(20,2) DEFAULT NULL COMMENT '市场价',
  `cost_price` decimal(20,2) DEFAULT NULL COMMENT '成本价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`goods_info_id`),
  KEY `goods_info_id_UNIQUE` (`goods_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品库-SKU';

CREATE TABLE `standard_sku_spec_detail_rel` (
  `spec_detail_rel_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) NOT NULL COMMENT '商品库SPU编号',
  `goods_info_id` varchar(32) NOT NULL COMMENT '商品库SKU编号',
  `spec_detail_id` bigint(20) NOT NULL,
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `detail_name` varchar(45) DEFAULT NULL COMMENT '规格值名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`spec_detail_rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品库SKU规格值关联表';

CREATE TABLE `standard_spec` (
  `spec_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) DEFAULT NULL COMMENT '商品库SPU编号',
  `spec_name` varchar(45) NOT NULL COMMENT '商品库规格名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`spec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品库规格表';

CREATE TABLE `standard_spec_detail` (
  `spec_detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格值ID',
  `goods_id` varchar(32) NOT NULL COMMENT '商品库SPU编号',
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `detail_name` varchar(45) NOT NULL COMMENT '规格值名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`spec_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品库规格值关联表';

CREATE TABLE `store_cate` (
  `store_cate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '店铺商品分类主键',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺主键',
  `cate_name` varchar(45) DEFAULT NULL COMMENT '分类名称',
  `cate_parent_id` bigint(20) DEFAULT NULL COMMENT '父分类ID',
  `cate_img` varchar(255) DEFAULT NULL COMMENT '分类图片',
  `cate_path` varchar(50) NOT NULL COMMENT '分类层次路径,例1|01|001',
  `cate_grade` tinyint(4) NOT NULL COMMENT '分类层级',
  `pin_yin` varchar(45) DEFAULT NULL COMMENT '拼音',
  `s_pin_yin` varchar(45) DEFAULT NULL COMMENT '简拼',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(4) NOT NULL COMMENT '是否默认,0:否1:是',
  PRIMARY KEY (`store_cate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店铺商品分类';

CREATE TABLE `store_cate_goods_rela` (
  `goods_id` varchar(32) NOT NULL COMMENT '商品标识',
  `store_cate_id` bigint(20) NOT NULL COMMENT '店铺分类标识',
  PRIMARY KEY (`goods_id`,`store_cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品店铺分类关联表(允许一个商品有多个店铺分类)';

CREATE TABLE `store_goods_tab` (
  `tab_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板Id',
  `tab_name` varchar(50) NOT NULL COMMENT '模板名称',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `sort` int(3) NOT NULL DEFAULT '0' COMMENT '排序号(越小越靠前)',
  `store_id` bigint(20) NOT NULL COMMENT '店铺id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`tab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='详情模板tab';

CREATE TABLE `distribution_goods_matter` (
  `id` varchar(32) NOT NULL COMMENT '分销素材id',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT '商品sku的id',
  `matter_type` tinyint(4) DEFAULT NULL COMMENT '素材类型 0 图片 1 视频',
  `matter` text COMMENT '素材',
  `recommend` text COMMENT '推荐语',
  `recommend_num` int(11) DEFAULT NULL COMMENT '推荐次数',
  `operator_id` varchar(32) DEFAULT NULL COMMENT '发布者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标识,0:未删除1:已删除',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


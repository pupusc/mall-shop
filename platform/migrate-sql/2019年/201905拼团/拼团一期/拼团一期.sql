#------------------------------------------------goods-------------------------------------------
DROP TABLE IF EXISTS `groupon_goods_info`;
CREATE TABLE `groupon_goods_info` (
  `groupon_goods_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拼团商品ID',
  `goods_info_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SKU编号',
  `groupon_price` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '拼团价格',
  `start_selling_num` int(10) DEFAULT '1' COMMENT '起售数量',
  `limit_selling_num` int(10) DEFAULT '1' COMMENT '限购数量',
  `groupon_activity_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拼团活动ID',
  `groupon_cate_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拼团分类ID',
  `sticky` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否精选，0：否，1：是',
  `store_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺ID',
  `goods_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SPU编号',
  `goods_sales_num` int(20) NOT NULL DEFAULT '0' COMMENT '商品销售数量',
  `order_sales_num` int(20) NOT NULL DEFAULT '0' COMMENT '订单数量',
  `trade_amount` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '交易额',
  `already_groupon_num` int(20) NOT NULL DEFAULT '0' COMMENT '已成团人数',
  `refund_num` int(20) NOT NULL DEFAULT '0' COMMENT '成团后退单数量',
  `refund_amount` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '成团后退单金额',
  `start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `audit_status` int(11) DEFAULT '0' COMMENT '活动审核状态',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`groupon_goods_id`),
  KEY `index_group_activity_id` (`groupon_activity_id`) USING BTREE,
  KEY `index_group_cate_id` (`groupon_cate_id`) USING BTREE,
  KEY `index_activity_id_spu_id` (`groupon_activity_id`,`goods_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='拼团商品表';


#------------------------------------------------marketing-------------------------------------------
DROP TABLE IF EXISTS `groupon_setting`;
CREATE TABLE `groupon_setting` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `goods_audit_flag` tinyint(1) DEFAULT '0' COMMENT '拼团商品审核',
  `advert` text COLLATE utf8mb4_unicode_ci COMMENT '广告',
  `rule` text COLLATE utf8mb4_unicode_ci COMMENT '拼团规则',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='拼团';


DROP TABLE IF EXISTS `groupon_activity`;
CREATE TABLE `groupon_activity` (
  `groupon_activity_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动ID',
  `groupon_num` tinyint(2) NOT NULL DEFAULT '2' COMMENT '拼团人数',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `groupon_cate_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拼团分类ID',
  `auto_groupon` tinyint(1) NOT NULL COMMENT '是否自动成团，0：否，1：是',
  `free_delivery` tinyint(1) NOT NULL COMMENT '是否包邮，0：否，1：是',
  `goods_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'spu编号',
  `goods_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'spu商品名称',
  `store_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺ID',
  `sticky` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否精选，0：否，1：是',
  `audit_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否审核通过，0：待审核，1：审核通过，2：审核不通过',
  `audit_fail_reason` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核不通过原因',
  `already_groupon_num` int(20) NOT NULL DEFAULT '0' COMMENT '已成团人数',
  `wait_groupon_num` int(20) NOT NULL DEFAULT '0' COMMENT '待成团人数',
  `fail_groupon_num` int(20) NOT NULL DEFAULT '0' COMMENT '团失败人数',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除，0：否，1：是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`groupon_activity_id`),
  KEY `index_start_time` (`start_time`) USING BTREE,
  KEY `index_end_time` (`end_time`) USING BTREE,
  KEY `index_group_cate_id` (`groupon_cate_id`) USING BTREE,
  KEY `index_store_id` (`store_id`) USING BTREE,
  KEY `index_status` (`audit_status`) USING BTREE,
  KEY `inde_spu_name` (`goods_name`(191)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='拼团活动表';

DROP TABLE IF EXISTS `groupon_cate`;
CREATE TABLE `groupon_cate` (
  `groupon_cate_id` varchar(32) NOT NULL COMMENT '拼团分类Id',
  `groupon_cate_name` varchar(20) NOT NULL COMMENT '拼团分类名称',
  `default_cate` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否是默认精选分类 0：否，1：是',
  `cate_sort` tinyint(4) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`groupon_cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='拼团分类表';
INSERT INTO `groupon_cate`(groupon_cate_id, groupon_cate_name, default_cate, cate_sort, del_flag, create_time, create_person, update_time, update_person, del_time, del_person) VALUES ('8a9bc76c628f4d8u86g54f559be76901', '精选', 1, 1, 0, now(), null, null, null, null, null);



DROP TABLE IF EXISTS `groupon_record`;
CREATE TABLE `groupon_record` (
  `groupon_record_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `groupon_activity_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拼团活动ID',
  `customer_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会员ID',
  `goods_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SPU编号',
  `goods_info_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'sku编号',
  `buy_num` int(10) NOT NULL COMMENT '已购数量',
  `limit_selling_num` int(10) COMMENT '限购数量',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`groupon_record_id`),
  UNIQUE KEY `index_activity_id_customer_id_sku_id` (`groupon_activity_id`,`customer_id`,`goods_info_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='拼团记录表';



#------------------------------------------------marketing-------------------------------------------
ALTER TABLE `groupon_activity` ADD COLUMN `goods_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'spu编码' AFTER `goods_id`;

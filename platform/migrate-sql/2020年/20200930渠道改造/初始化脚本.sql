
-- ----------------------------
-- Table structure for platform_address
-- ----------------------------
CREATE TABLE `sbc-setting`.`platform_address`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `addr_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址id',
  `addr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址名称',
  `addr_parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '父地址ID',
  `sort_no` int(10) NOT NULL COMMENT '排序号',
  `addr_level` tinyint(4) NOT NULL COMMENT '地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)',
  `create_time` datetime(0) NOT NULL COMMENT '入库时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除标志 0：否，1：是；默认0',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `data_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '数据类型 0:初始化 1:人工',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `addr_id`(`addr_id`) USING BTREE,
  INDEX `addr_parent_id`(`addr_parent_id`) USING BTREE,
  INDEX `addr_level`(`addr_level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '平台四级地址信息表' ROW_FORMAT = Compact;


-- 新增会员详情街道id
ALTER TABLE `sbc-customer`.`customer_detail`
ADD COLUMN `street_id`  bigint(10) NULL DEFAULT NULL COMMENT '街道' AFTER `area_id`;

-- 新增商家街道id
ALTER TABLE `sbc-customer`.`company_info`
ADD COLUMN `street_id`  bigint(10) NULL COMMENT '街道id' AFTER `area_id`;

-- 新增店铺街道id
ALTER TABLE `sbc-customer`.`store`
ADD COLUMN `street_id`  bigint(10) NULL DEFAULT NULL COMMENT '街道id' AFTER `area_id`;

-- 新增收货地址的街道id
ALTER TABLE `sbc-customer`.`customer_delivery_address`
ADD COLUMN `street_id` bigint(20) NULL  COMMENT '街道id' AFTER `area_id`;

-- 增加商家来源类型
ALTER TABLE `sbc-customer`.`company_info`
ADD COLUMN `company_source_type` tinyint(1) NULL DEFAULT 0 COMMENT '商家来源类型 0:商城入驻 1:linkMall初始化' AFTER `store_type`;

-- 增加商家来源类型
ALTER TABLE `sbc-customer`.`store`
ADD COLUMN `company_source_type` tinyint(1) NULL DEFAULT 0 COMMENT '商家来源类型 0:商城入驻 1:linkMall初始化' AFTER `supplier_pinyin_name`;

-- 第三方商品分类表
CREATE TABLE `sbc-goods`.`third_goods_cate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cate_id` bigint(20) NOT NULL COMMENT '三方商品分类主键',
  `cate_name` varchar(45) NOT NULL COMMENT '分类名称',
  `cate_parent_id` bigint(20) DEFAULT NULL COMMENT '父分类ID',
  `cate_path` varchar(1000) NOT NULL COMMENT '分类层次路径,例0|01|001',
  `cate_grade` tinyint(4) NOT NULL COMMENT '分类层级',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `third_platform_type` tinyint(4) NOT NULL COMMENT '第三方平台来源(0,linkedmall)',
  `del_flag` tinyint(4) unsigned NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方商品分类表';

-- 平台类目和第三方平台类目映射
CREATE TABLE `sbc-goods`.`goods_cate_third_cate_rel`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `cate_id` bigint NOT NULL COMMENT '平台类目主键',
  `third_cate_id` bigint NOT NULL COMMENT '第三方平台类目主键',
  `third_platform_type` tinyint UNSIGNED NOT NULL COMMENT '第三方渠道(0，linkedmall)',
  `create_time` datetime ,
  `update_time` datetime ,
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '平台类目和第三方平台类目映射';


-- 添加注释
ALTER TABLE `sbc-goods`.`goods`
MODIFY COLUMN `goods_source` tinyint(4) NULL DEFAULT 1 COMMENT '商品来源，0供应商，1商家,2linkedmall' AFTER `added_time`;

-- 添加注释
ALTER TABLE `sbc-goods`.`goods_info`
MODIFY COLUMN `goods_source` tinyint(4) NULL DEFAULT 1 COMMENT '商品来源，0供应商，1商家,2linkedmall' AFTER `provider_id`;

-- 添加注释
ALTER TABLE `sbc-goods`.`standard_goods`
MODIFY COLUMN `goods_source` tinyint(4) NULL DEFAULT 1 COMMENT '商品来源，0供应商，1商家，2linkedmall' AFTER `goods_mobile_detail`;

-- goods表添加linkedmall字段
ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN `third_platform_spu_id` varchar(30) CHARACTER SET utf8mb4 NULL COMMENT '第三方平台的spuId' AFTER `vendibility`,
ADD COLUMN `seller_id` bigint NULL COMMENT '第三方卖家id' AFTER `third_platform_spu_id`,
ADD COLUMN `third_cate_id` bigint NULL COMMENT '三方渠道类目id' AFTER `seller_id`,
ADD COLUMN `third_platform_type` tinyint(4) NULL COMMENT '三方平台类型，0，linkedmall' AFTER `third_cate_id`;

-- goodsinfo表添加linkedmall字段
ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN `third_platform_sku_id` varchar(30) CHARACTER SET utf8mb4 NULL COMMENT '第三方平台的skuId' AFTER `vendibility`,
ADD COLUMN `third_platform_spu_id` varchar(30) CHARACTER SET utf8mb4 NULL COMMENT '第三方平台的spuId' AFTER `third_platform_sku_id`,
ADD COLUMN `seller_id` bigint NULL COMMENT '第三方卖家id' AFTER `third_platform_sku_id`,
ADD COLUMN `third_cate_id` bigint NULL COMMENT '三方渠道类目id' AFTER `seller_id`,
ADD COLUMN `third_platform_type` tinyint(4) NULL COMMENT '三方平台类型，0，linkedmall' AFTER `third_cate_id`;

-- standard_goods表添加linkedmall字段
ALTER TABLE `sbc-goods`.`standard_goods`
ADD COLUMN `third_platform_spu_id` varchar(30) CHARACTER SET utf8mb4 NULL COMMENT '第三方平台的spuId' AFTER `delete_reason`,
ADD COLUMN `seller_id` bigint NULL COMMENT '第三方卖家id' AFTER `third_platform_spu_id`,
ADD COLUMN `third_cate_id` bigint NULL COMMENT '三方渠道类目id' AFTER `seller_id`,
ADD COLUMN `added_flag` tinyint(4) NULL COMMENT '上下架状态,0:下架1:上架2:部分上架' AFTER `third_cate_id`;

-- standard_sku表添加linkedmall字段
ALTER TABLE `sbc-goods`.`standard_sku`
ADD COLUMN `third_platform_sku_id` varchar(30) CHARACTER SET utf8mb4 NULL COMMENT '第三方平台的skuId' AFTER `stock`,
ADD COLUMN `third_platform_spu_id` varchar(30) CHARACTER SET utf8mb4 NULL COMMENT '第三方平台的spuId' AFTER `third_platform_sku_id`,
ADD COLUMN `seller_id` bigint NULL COMMENT '第三方卖家id' AFTER `third_platform_spu_id`,
ADD COLUMN `third_cate_id` bigint NULL COMMENT '三方渠道类目id' AFTER `seller_id`,
ADD COLUMN `added_flag` tinyint(4) NULL COMMENT '上下架状态,0:下架1:上架2:部分上架' AFTER `third_cate_id`;

-- 调整抢购的库存描述
ALTER TABLE `sbc-goods`.`flash_sale_goods`
MODIFY COLUMN `stock` int(8) NOT NULL COMMENT '上限数量' AFTER `price`;

-- 第三方地址表，有可能已存在替换它
CREATE TABLE `sbc-setting`.`third_address` (
  `id` varchar(32) NOT NULL COMMENT '第三方地址主键',
  `third_addr_id` varchar(20) NOT NULL COMMENT '第三方地址编码id',
  `third_parent_id` varchar(20) DEFAULT NULL COMMENT '第三方父级地址编码id',
  `addr_name` varchar(200) NOT NULL COMMENT '地址名称',
  `level` tinyint(4) NOT NULL COMMENT '地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)',
  `third_flag` tinyint(4) NOT NULL COMMENT '第三方标志 0:likedMall 1:京东',
  `platform_addr_id` varchar(20) DEFAULT NULL COMMENT '平台地址id',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `third_addr_id` (`third_addr_id`,`third_flag`) USING BTREE,
  KEY `level` (`level`),
  KEY `platform_addr_id` (`platform_addr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方地址映射表';


-- 自动退款短信模板
INSERT INTO `sbc-message`.`sms_template`(`template_name`, `template_content`, `remark`, `template_type`, `review_status`, `template_code`, `review_reason`, `sms_setting_id`, `del_flag`, `create_time`, `business_type`, `purpose`, `sign_id`, `open_flag`) VALUES ('供应商商品支付失败通知', '很抱歉，您的订单${name}部分供应商商品采购失败，已为您自动退款~', '供应商商品支付失败通知', 1, 0, NULL, NULL, NULL, 0, '2020-08-24 14:51:12', 'THIRD_PAY_ERROR_AUTO_REFUND', '供应商商品支付失败通知', NULL, 1);
INSERT INTO `sbc-message`.`message_send_node`(`node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES ('供应商商品支付失败通知', '供应商商品支付失败通知', '很抱歉，您的订单{订单第一行商品名称}部分供应商商品采购失败，已为您自动退款~', 1, '2020-09-01 15:30:00', NULL, '2020-09-01 15:30:00', '2c8080815cd3a74a015cd3ae86850001', 0, 0, 0, null, 2, 'THIRD_PAY_ERROR_AUTO_REFUND');
INSERT INTO `sbc-message`.`push_send_node`(`node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES ('供应商商品支付失败通知', 2, 'THIRD_PAY_ERROR_AUTO_REFUND', '供应商商品支付失败通知', '很抱歉，您的订单{商品名称}部分供应商商品采购失败，已为您自动退款~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-09-01 15:30:00');


-- s2b mongoDB4.0 有事务 要提前建集合
-- db.createCollection("thirdPlatformTrade");

-- 新增商家单品运费模板发货地址的街道id
ALTER TABLE `sbc-goods`.`freight_template_goods`
ADD COLUMN `street_id` bigint(20) NULL  COMMENT '发货地-街道' AFTER `area_id`;
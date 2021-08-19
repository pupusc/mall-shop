CREATE TABLE `s2b_statistics`.`replay_goods` (
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
  `supply_price` decimal(20,2) DEFAULT NULL COMMENT '供货价',
  `retail_price` decimal(20,2) DEFAULT NULL COMMENT '建议零售价',
  `goods_type` tinyint(1) DEFAULT '0' COMMENT '商品类型，0：实体商品，1：虚拟商品',
  `line_price` decimal(20,2) DEFAULT NULL COMMENT '划线价',
  `cost_price` decimal(20,2) DEFAULT NULL COMMENT '成本价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `added_time` datetime DEFAULT NULL COMMENT '上下架时间',
  `goods_source` tinyint(4) DEFAULT '1' COMMENT '商品来源，0供应商，1商家,2linkedmall',
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
  `goods_evaluate_num` bigint(11) DEFAULT '0' COMMENT '商品评论数',
  `goods_collect_num` bigint(11) DEFAULT '0' COMMENT '商品收藏量',
  `goods_sales_num` bigint(11) DEFAULT '0' COMMENT '商品销量',
  `goods_favorable_comment_num` bigint(11) DEFAULT '0' COMMENT '商品好评数量',
  `provider_goods_id` varchar(45) DEFAULT NULL COMMENT '所属供应商商品Id',
  `provider_id` bigint(20) DEFAULT NULL COMMENT '供应商Id',
  `provider_name` varchar(45) DEFAULT NULL COMMENT '供应商名称',
  `recommended_retail_price` decimal(20,2) DEFAULT NULL COMMENT '建议零售价',
  `sham_sales_num` bigint(11) DEFAULT '0' COMMENT '注水销量',
  `sort_no` bigint(11) DEFAULT '0' COMMENT '排序号',
  `single_spec_flag` tinyint(4) DEFAULT '1' COMMENT '0:多规格1:单规格',
  `added_timing_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否定时上架 0:否1:是',
  `added_timing_time` datetime DEFAULT NULL COMMENT '定时上架时间',
  `stock` bigint(11) DEFAULT NULL COMMENT '库存（准实时）',
  `sku_min_market_price` decimal(10,2) DEFAULT NULL COMMENT '最小市场价',
  `goods_buy_types` varchar(10) DEFAULT NULL COMMENT '购买方式 0立即购买,1购物车,内容以,相隔',
  `need_synchronize` tinyint(4) DEFAULT NULL COMMENT '是否需要同步 0：不需要同步 1：需要同步',
  `delete_reason` varchar(100) DEFAULT NULL COMMENT '删除原因',
  `add_false_reason` varchar(100) DEFAULT NULL COMMENT '下架原因',
  `provider_status` tinyint(4) DEFAULT NULL COMMENT '供应商状态 0: 关店 1:开店',
  `vendibility` tinyint(4) DEFAULT '1' COMMENT '是否可售，0不可售，1可售',
  `third_platform_spu_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '第三方平台的spuId',
  `seller_id` bigint(20) DEFAULT NULL COMMENT '第三方卖家id',
  `third_cate_id` bigint(20) DEFAULT NULL COMMENT '三方渠道类目id',
  `third_platform_type` tinyint(4) DEFAULT NULL COMMENT '三方平台类型，0，linkedmall',
  `label_id_str` text CHARACTER SET utf8mb4 COMMENT '标签id，以逗号拼凑',
  PRIMARY KEY (`goods_id`),
  UNIQUE KEY `product_id_UNIQUE` (`goods_id`),
  KEY `idx_company_info_id` (`company_info_id`),
  KEY `idx_store_id` (`store_id`),
  KEY `idx_del_flag` (`del_flag`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SPU表';

-- sbc_goods start ---
ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN `cate_top_id`  bigint(20) NULL COMMENT '商品一级类目Id' AFTER `distribution_goods_audit_reason`;

-- 初始化一级类目
update `sbc-goods`.`goods_info` i set i.cate_top_id = (SELECT substring_index(SUBSTRING_INDEX(c.cate_path, "|", 2),"|",-1)  from  `sbc-goods`.`goods_cate` c where c.cate_id = i.cate_id);

-- 商品评价
ALTER TABLE  `sbc-goods`.`goods_evaluate`
ADD COLUMN `cate_top_id`  bigint(20) NULL COMMENT '商品一级分类',
ADD COLUMN `cate_id`  bigint(20) NULL COMMENT '商品类目',
ADD COLUMN `brand_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品品牌',
ADD COLUMN `terminal_source`  tinyint(4) NULL COMMENT '终端来源';

CREATE TABLE  `sbc-goods`.`goods_share_record` (
  `share_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员Id',
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU id',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SKU id',
  `store_id` bigint(11) NOT NULL COMMENT '店铺ID',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `terminal_source` tinyint(3) NOT NULL COMMENT '终端：1 H5，2pc，3APP，4小程序',
  `share_channel` tinyint(1) DEFAULT NULL COMMENT '分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`share_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品的分享记录';
-- sbc_goods end ---

-- sbc_customer start ---
CREATE TABLE  `sbc-customer`.`store_share_record` (
  `share_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员Id',
  `store_id` bigint(11) DEFAULT NULL COMMENT '店铺ID',
  `company_info_id` bigint(11) DEFAULT NULL COMMENT '公司信息ID',
  `index_type` tinyint(1) NOT NULL COMMENT '0分享首页，1分享店铺首页',
  `terminal_source` tinyint(3) NOT NULL COMMENT '终端：1 H5，2pc，3APP，4小程序',
  `share_channel` tinyint(1) DEFAULT NULL COMMENT '分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`share_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商城分享记录';

ALTER TABLE `sbc-customer`.`store_evaluate`
ADD COLUMN `terminal_source`  tinyint(4) NULL DEFAULT NULL COMMENT '终端来源';

ALTER TABLE `sbc-customer`.`store_customer_follow`
ADD COLUMN `terminal_source`  tinyint(4) NULL DEFAULT NULL COMMENT '终端来源';

-- 店铺收藏新增记录表
CREATE TABLE `sbc-customer`.`store_customer_follow_action` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `store_id` bigint(11) NOT NULL COMMENT '商品Id',
  `company_info_id` bigint(11) NOT NULL COMMENT '商家ID',
  `follow_time` datetime NOT NULL COMMENT '收藏时间',
  `terminal_source` tinyint(4) DEFAULT NULL COMMENT '终端',
	`create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=658 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品的店铺收藏新增数据表';

CREATE DEFINER=`root`@`%` TRIGGER `sbc-customer`.`trigger_store_customer_follow_action` AFTER INSERT ON `sbc-customer`.`store_customer_follow`
FOR EACH ROW BEGIN
		INSERT INTO `sbc-customer`.`store_customer_follow_action`(follow_id, customer_id, store_id, company_info_id, follow_time, terminal_source,create_time)
		VALUES (NEW.follow_id, NEW.customer_id, NEW.store_id, NEW.company_info_id, NEW.follow_time, NEW.terminal_source,now());
	end;

-- 用户签到记录表 ---
CREATE TABLE `sbc-customer`.`customer_sign_record_action` (
  `sign_record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户签到记录表id',
  `customer_id` varchar(32) NOT NULL COMMENT '用户id',
  `create_time` datetime NOT NULL COMMENT '签到日期记录',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除区分：0 未删除，1 已删除',
  `sign_ip` varchar(50) DEFAULT NULL COMMENT '签到ip',
  `terminal_source` varchar(10) DEFAULT NULL COMMENT '终端：1 H5，2pc，3APP，4小程序',
  PRIMARY KEY (`sign_record_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='用户签到记录表';
-- 用户签到触发器 ---
CREATE DEFINER=`root`@`%` TRIGGER `sbc-customer`.`trigger_customer_sign_record_action` AFTER INSERT ON `sbc-customer`.`customer_sign_record`
FOR EACH ROW BEGIN
		INSERT INTO customer_sign_record_action(customer_id,create_time,del_flag,sign_ip,terminal_source)
		VALUES (NEW.customer_id, NEW.sign_record, NEW.del_flag, NEW.sign_ip, (case NEW.sign_terminal WHEN 'wechat' THEN 1 WHEN 'pc' THEN 2 WHEN 'app' THEN 3 WHEN 'minipro' THEN 4 ELSE NULL END));
end;

-- 好友邀请新增终端类型
ALTER TABLE `sbc-customer`.`invite_new_record`
ADD COLUMN `terminal_source`  tinyint(4) NULL DEFAULT NULL COMMENT '终端来源';

-- sbc_customer end ---

-- sbc_order start ---
-- 商品收藏
ALTER TABLE `sbc-order`.`goods_customer_follow`
ADD COLUMN `store_id`  bigint(20) NULL DEFAULT NULL COMMENT '店铺Id',
ADD COLUMN `cate_top_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品一级分类',
ADD COLUMN `cate_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品类目',
ADD COLUMN `brand_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品品牌',
ADD COLUMN `terminal_source`  tinyint(4) NULL DEFAULT NULL COMMENT '终端来源';

-- 商品收藏行为明细
ALTER TABLE `sbc-order`.`goods_customer_follow_action`
ADD COLUMN `store_id`  bigint(20) NULL DEFAULT NULL COMMENT '店铺Id',
ADD COLUMN `cate_top_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品一级分类',
ADD COLUMN `cate_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品类目',
ADD COLUMN `brand_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品品牌',
ADD COLUMN `terminal_source`  tinyint(4) NULL DEFAULT NULL COMMENT '终端来源';

-- 修改商品收藏触发器
DROP TRIGGER `sbc-order`.`trigger_goods_customer_follow_action`;
CREATE DEFINER=`root`@`%` TRIGGER `sbc-order`.`trigger_goods_customer_follow_action` AFTER INSERT ON `sbc-order`.`goods_customer_follow`
FOR EACH ROW BEGIN
  insert into goods_customer_follow_action(customer_id,goods_id,goods_info_id,company_info_id,goods_num,create_time,follow_flag,follow_time,store_id,cate_top_id,cate_id,brand_id,terminal_source)
  values (NEW.customer_id,NEW.goods_id,NEW.goods_info_id,NEW.company_info_id,NEW.goods_num,NEW.create_time,NEW.follow_flag,NEW.follow_time,NEW.store_id,NEW.cate_top_id,NEW.cate_id,NEW.brand_id,NEW.terminal_source);
end;

-- 商品加购
ALTER TABLE `sbc-order`.`purchase`
ADD COLUMN `store_id`  bigint(20) NULL DEFAULT NULL COMMENT '店铺Id',
ADD COLUMN `cate_top_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品一级分类',
ADD COLUMN `cate_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品类目',
ADD COLUMN `brand_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品品牌',
ADD COLUMN `terminal_source`  tinyint(4) NULL DEFAULT NULL COMMENT '终端来源';
-- 商品加购行为明细
ALTER TABLE `sbc-order`.`purchase_action`
ADD COLUMN `store_id`  bigint(20) NULL DEFAULT NULL COMMENT '店铺Id',
ADD COLUMN `cate_top_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品一级分类',
ADD COLUMN `cate_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品类目',
ADD COLUMN `brand_id`  bigint(20) NULL DEFAULT NULL COMMENT '商品品牌',
ADD COLUMN `terminal_source`  tinyint(4) NULL DEFAULT NULL COMMENT '终端来源';

-- 修改商品加购触发器
DROP TRIGGER `sbc-order`.`trigger_purchase_action`;
CREATE DEFINER=`root`@`%` TRIGGER `sbc-order`.`trigger_purchase_action` AFTER INSERT ON `sbc-order`.`purchase`
FOR EACH ROW BEGIN
		insert into purchase_action(customer_id,goods_id,goods_info_id,company_info_id,goods_num,create_time,invitee_id,store_id,cate_top_id,cate_id,brand_id,terminal_source)
		values (NEW.customer_id,NEW.goods_id,NEW.goods_info_id,NEW.company_info_id,NEW.goods_num,NEW.create_time,NEW.invitee_id,NEW.store_id,NEW.cate_top_id,NEW.cate_id,NEW.brand_id,NEW.terminal_source);
	end;
-- sbc_order end ---

-- crm菜单脚本
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a9247d43000c', 4, 'ff8080816f660586016fac4377a30013', '偏好详情', NULL, '/preferencetagdetail/list', 'POST', NULL, 9, '2020-12-28 19:38:30', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a925a8ec000d', 4, 'ff8080816f660586016fac4377a30013', '偏好标签', NULL, '/autotag/list', 'POST', NULL, 9, '2020-12-28 19:39:46', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a928b28b000e', 4, 'ff8080816f660586016fac4377a30013', '偏好标签分页', NULL, '/preferencetagdetail/page', 'POST', NULL, 10, '2020-12-28 19:43:05', 0);

INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8d82f2b0007', 4, 'ff8080816f5496f0016f5609e3d40002', '引用系统标签', 'f_system_tag', NULL, 5, '2020-12-28 18:15:09', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8c8b6d00000', 4, 'ff8080816f5496f0016f561226ec000c', '标签总数', NULL, '/autotag/count', 'GET', NULL, 2, '2020-12-28 17:58:15', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8c971bf0001', 4, 'ff8080816f5496f0016f561226ec000c', '标签分页', NULL, '/preferencetagdetail/page', 'POST', NULL, 3, '2020-12-28 17:59:03', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8e851f3000a', 4, 'ff8080816f5496f0016f561226ec000c', '偏好类标签', NULL, '/autotag/preferenceList', 'POST', NULL, 4, '2020-12-28 18:32:46', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a93b8809000f', 4, 'ff8080816f5496f0016f561226ec000c', '其他标签分类列表', NULL, '/autotag/page', 'POST', NULL, 5, '2020-12-28 20:03:40', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8cfa6030002', 4, 'ff8080816f5496f0016f56129356000d', 'bigJson', NULL, '/tagdimension/getBigJson', 'GET', NULL, 1, '2020-12-28 18:05:49', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8d25d140003', 4, 'ff8080816f5496f0016f56129356000d', '标签保存', NULL, '/autotag/add', 'POST', NULL, 3, '2020-12-28 18:08:47', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ac8a4f8a0010', 4, 'ff8080816f5496f0016f56129356000d', '偏好bigJson', NULL, '/tagdimension/getPreferenceBigJson', 'GET', NULL, 4, '2020-12-29 11:28:34', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ac8bcdf40011', 4, 'ff8080816f5496f0016f56129356000d', '店铺数据信息', NULL, '/tagdimension/getStoreSimpleInfoList', 'POST', NULL, 5, '2020-12-29 11:30:12', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ac8c6eb50012', 4, 'ff8080816f5496f0016f56129356000d', '商品品牌数据信息', NULL, '/tagdimension/getGoodsBrandSimpleInfoList', 'POST', NULL, 6, '2020-12-29 11:30:53', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ac8d16a40013', 4, 'ff8080816f5496f0016f56129356000d', '商品分类数据信息', NULL, '/tagdimension/getGoodsCateSImpleInfoList', 'POST', NULL, 7, '2020-12-29 11:31:36', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ac8e1a0b0014', 4, 'ff8080816f5496f0016f56129356000d', '有效分类平台分类', NULL, '/goods/goodsCatesTree', 'GET', NULL, 8, '2020-12-29 11:32:43', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ac8ece390015', 4, 'ff8080816f5496f0016f56129356000d', '商品分类数据信息', NULL, '/tagdimension/getGoodsCateSImpleInfoList', 'POST', NULL, 9, '2020-12-29 11:33:29', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8d3e4cc0004', 4, 'ff8080816f5496f0016f5612e832000e', '编辑标签的查询', NULL, '/autotag/*', 'GET', NULL, 1, '2020-12-28 18:10:28', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8d4ec290005', 4, 'ff8080816f5496f0016f5612e832000e', '编辑保存', NULL, '/autotag/modify', 'PUT', NULL, 2, '2020-12-28 18:11:35', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ad4545240016', 4, 'ff8080816f5496f0016f5612e832000e', 'bigJson', NULL, '/tagdimension/getBigJson', 'GET', NULL, 4, '2020-12-29 14:52:47', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ad4624e50017', 4, 'ff8080816f5496f0016f5612e832000e', '偏好bigJson', NULL, '/tagdimension/getPreferenceBigJson', 'GET', NULL, 5, '2020-12-29 14:53:44', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ad467ae00018', 4, 'ff8080816f5496f0016f5612e832000e', '店铺数据信息', NULL, '/tagdimension/getStoreSimpleInfoList', 'POST', NULL, 6, '2020-12-29 14:54:06', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ad46f0a60019', 4, 'ff8080816f5496f0016f5612e832000e', '商品品牌数据信息', NULL, '/tagdimension/getGoodsBrandSimpleInfoList', 'POST', NULL, 7, '2020-12-29 14:54:36', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ad47430c001a', 4, 'ff8080816f5496f0016f5612e832000e', '商品分类数据信息', NULL, '/tagdimension/getGoodsCateSImpleInfoList', 'POST', NULL, 8, '2020-12-29 14:54:57', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ad47ccd1001b', 4, 'ff8080816f5496f0016f5612e832000e', '有效分类平台分类', NULL, '/goods/goodsCatesTree', 'GET', NULL, 9, '2020-12-29 14:55:33', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176ad48231c001c', 4, 'ff8080816f5496f0016f5612e832000e', '商品分类数据信息', NULL, '/tagdimension/getGoodsCateSImpleInfoList', 'POST', NULL, 10, '2020-12-29 14:55:55', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8ecf47e000b', 4, 'ff8080816f5496f0016f56132334000f', '删除标签', NULL, '/autotag/**', 'DELETE', NULL, 2, '2020-12-28 18:37:50', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8d8c44c0008', 4, 'ff808081768f509b0176a8d82f2b0007', '系统标签引用', NULL, '/autotag/system-list', 'POST', NULL, 0, '2020-12-28 18:15:47', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081768f509b0176a8d9c8530009', 4, 'ff808081768f509b0176a8d82f2b0007', '引用标签保存', NULL, '/autotag/init', 'POST', NULL, 1, '2020-12-28 18:16:54', 0);

UPDATE `sbc-setting`.`menu_info` SET `sort` = 1 WHERE `menu_id` = 'ff8080816f5496f0016f5609b2b90001';

UPDATE `sbc-setting`.`menu_info` SET `sort` = 2 WHERE `menu_id` = 'ff8080816f5496f0016f5609e3d40002';


INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808176ae86e40176b1c2a6320001', 4, '0', 1, 'CRM', NULL, 'crm_pay.png', 6, '2020-12-30 11:48:12', 0);

delete from `sbc-setting`.`menu_info` where `menu_id` = 'ff8080816f5496f0016f5608682c0000';

INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808176ae86e40176b1c3876b0002', 4, 'ff80808176ae86e40176b1c2a6320001', 2, '会员管理', NULL, NULL, 1, '2020-12-30 11:49:10', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808176ae86e40176b1c3c61a0003', 4, 'ff80808176ae86e40176b1c2a6320001', 2, '会员运营', NULL, NULL, 2, '2020-12-30 11:49:26', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808176ae86e40176b1c4035a0004', 4, 'ff80808176ae86e40176b1c2a6320001', 2, '数据分析', NULL, NULL, 3, '2020-12-30 11:49:42', 0);

UPDATE `sbc-setting`.`menu_info` SET `system_type_cd` = 4, `parent_menu_id` = 'ff80808176ae86e40176b1c3876b0002', `menu_grade` = 3, `menu_name` = '会员分群', `menu_url` = '/customer-group', `menu_icon` = NULL, `sort` = 1, `create_time` = '2019-12-30 17:01:14', `del_flag` = 0 WHERE `menu_id` = 'ff8080816f5496f0016f5609b2b90001';
UPDATE `sbc-setting`.`menu_info` SET `system_type_cd` = 4, `parent_menu_id` = 'ff80808176ae86e40176b1c3876b0002', `menu_grade` = 3, `menu_name` = '标签管理', `menu_url` = '/custom-tag', `menu_icon` = NULL, `sort` = 2, `create_time` = '2019-12-30 17:01:26', `del_flag` = 0 WHERE `menu_id` = 'ff8080816f5496f0016f5609e3d40002';

UPDATE `sbc-setting`.`menu_info` SET `system_type_cd` = 4, `parent_menu_id` = 'ff80808176ae86e40176b1c3c61a0003', `menu_grade` = 3, `menu_name` = '人群运营', `menu_url` = '/customer-plan-list', `menu_icon` = NULL, `sort` = 3, `create_time` = '2020-01-10 13:50:21', `del_flag` = 0 WHERE `menu_id` = 'ff8080816f660586016f8e00e3c00000';

UPDATE `sbc-setting`.`menu_info` SET `system_type_cd` = 4, `parent_menu_id` = 'ff80808176ae86e40176b1c4035a0004', `menu_grade` = 3, `menu_name` = 'RFM分析', `menu_url` = '/rmf-model', `menu_icon` = NULL, `sort` = 4, `create_time` = '2019-12-30 17:02:14', `del_flag` = 0 WHERE `menu_id` = 'ff8080816f5496f0016f560a9e000004';

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808176b75fef0176b7d1d12c0001', 4, '2c93998c6e7c6da8016e8186938b0000', '会员自动标签', NULL, '/customerAutoTag/tag', 'POST', NULL, 10, '2020-12-31 16:02:30', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808176b75fef0176b7d0d8bf0000', 4, '2c93998c6e7c6da8016e8186938b0000', '获取会员偏好类标签', NULL, '/customerAutoTag/preferenceTag', 'POST', NULL, 9, '2020-12-31 16:01:26', 0);

-- sbc-crm end ---

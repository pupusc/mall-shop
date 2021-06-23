-- ***************************************** setting *********************************************
update menu_info set menu_url = '/customer-grade',parent_menu_id='8ab3690769a4ecb50169bdf8e93b0011',sort=1 where menu_url = '/customer-level' and system_type_cd = 4;
UPDATE `menu_info` SET `sort` = 1 WHERE `system_type_cd` = 4 and `menu_name` = '客户管理' and `del_flag` = 0;

INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('4431eaa040d911e99b6f0050568808e1', 4, '8ab3690769a4ecb50169bdf8e93b0011', 3, '成长值设置', '/growth-value-setting', NULL, 5, '2019-02-28 15:54:20', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`,
`menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('4431ea2640d911e99b6f0050568808e1', 4, '4431e9aa40d911e99b6f0050568808e1', 3, '权益设置', '/customer-equities', NULL, 0, '2019-02-26 15:00:45', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('4431e9aa40d911e99b6f0050568808e1', 4, 'fc8df0553fe311e9828800163e0fc468', 2, '权益管理', NULL, '1496727665078.jpg', 10, '2019-02-26 14:53:25', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769a4ecb50169bdf8e93b0011', 4, 'fc8df0553fe311e9828800163e0fc468', 2, '忠诚度管理', NULL, NULL, 5, '2019-03-27 15:06:40', 0);

INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a36fcd2000a', 4, '4431991040d911e99b6f0050568808e1', '客户成长值明细', 'f_customer-grow-value_list', NULL, 1, '2019-03-14 11:20:18', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a2dab710007', 4, '4431ea2640d911e99b6f0050568808e1', '会员权益删除', 'f_authority-manage_del', NULL, 1, '2019-03-14 11:10:07', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a2be23b0005', 4, '4431ea2640d911e99b6f0050568808e1', '会员权益编辑', 'f_authority-manage_edit', NULL, 3, '2019-03-14 11:08:10', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a299ba30003', 4, '4431ea2640d911e99b6f0050568808e1', '会员权益增加', 'f_authority-manage_add', NULL, 1, '2019-03-14 11:05:41', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a27c27d0001', 4, '4431ea2640d911e99b6f0050568808e1', '会员权益查看', 'f_authority-manage_list', NULL, 1, '2019-03-14 11:03:40', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4434205840d911e99b6f0050568808e1', 4, '4431eaa040d911e99b6f0050568808e1', '成长值设置编辑', 'f_growth_value_setting_edit', NULL, 2, '2019-03-01 16:22:53', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('44341ffb40d911e99b6f0050568808e1', 4, '4431eaa040d911e99b6f0050568808e1', '成长值设置查看', 'f_growth_value_setting_view', NULL, 1, '2019-03-01 16:20:35', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769905b6b01699390c4a30001', 3, 'dbcee4133f1211e99d7100163e1249d4', '客户等级删除', 'f_customer_level_2', NULL, 3, '2019-03-19 09:28:52', 0);


INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697afcc91f000d', 3, '4433e04a40d911e99b6f0050568808e1', '查询平台等级', NULL, '/store/storeLevel/listBoss', 'GET', NULL, 2, '2019-03-14 14:56:21', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a386f97000c', 4, '2c93a4886979daa501697a36fcd2000a', '客户基本信息', NULL, '/customer/*', 'GEY', NULL, 2, '2019-03-14 11:21:53', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a379b7b000b', 4, '2c93a4886979daa501697a36fcd2000a', '客户成长值明细', NULL, '/customer/queryToGrowthValue', 'POST', NULL, 1, '2019-03-14 11:20:58', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a2fc44e0009', 4, '2c93a4886979daa501697a2be23b0005', '会员权益拖拽排序', NULL, '/customer/customerLevelRights/editSort', 'PUT', NULL, 2, '2019-03-14 11:12:25', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a2ea1bf0008', 4, '2c93a4886979daa501697a2dab710007', '会员权益删除', NULL, '/customer/customerLevelRights/deleteById/*', 'DELETE', NULL, 1, '2019-03-14 11:11:10', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a2d38700006', 4, '2c93a4886979daa501697a2be23b0005', '会员权益编辑', NULL, '/customer/customerLevelRights/modify', 'PUT', NULL, 1, '2019-03-14 11:09:38', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a2b408b0004', 4, '2c93a4886979daa501697a299ba30003', '会员权益增加', NULL, '/customer/customerLevelRights/add', 'POST', NULL, 1, '2019-03-14 11:07:29', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a28f24a0002', 4, '2c93a4886979daa501697a27c27d0001', '会员权益列表查看', '', '/customer/customerLevelRights/list', 'GET', NULL, 1, '2019-03-14 11:04:58', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c93a4886979daa501697a151e3a0000', 4, '4433e04a40d911e99b6f0050568808e1', '查询权益列表', NULL, '/customer/customerLevelRights/valid/list', 'GET', NULL, 2, '2019-03-14 10:43:18', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4436f1b040d911e99b6f0050568808e1', 4, '4434205840d911e99b6f0050568808e1', '修改成长值购物获取规则', NULL, '/goods/goodsCate', 'PUT', NULL, 4, '2019-03-01 16:30:08', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4436f15a40d911e99b6f0050568808e1', 4, '44341ffb40d911e99b6f0050568808e1', '查询成长值购物获取规则', NULL, '/goods/goodsCates', 'GET', NULL, 3, '2019-03-01 16:29:03', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4436f10440d911e99b6f0050568808e1', 4, '4434205840d911e99b6f0050568808e1', '修改成长值基础获取规则', NULL, '/growthValue/basicRules', 'PUT', NULL, 3, '2019-03-01 16:27:48', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4436f0ad40d911e99b6f0050568808e1', 4, '44341ffb40d911e99b6f0050568808e1', '查询成长值基础获取规则', NULL, '/growthValue/basicRules', 'GET', NULL, 2, '2019-03-01 16:26:57', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4436f05740d911e99b6f0050568808e1', 4, '4434205840d911e99b6f0050568808e1', '关闭成长值开关', NULL, '/growthValue/close', 'POST', NULL, 2, '2019-03-01 16:26:32', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4436efff40d911e99b6f0050568808e1', 4, '4434205840d911e99b6f0050568808e1', '开启成长值开关', NULL, '/growthValue/open', 'POST', NULL, 1, '2019-03-01 16:26:07', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('4436efa540d911e99b6f0050568808e1', 4, '44341ffb40d911e99b6f0050568808e1', '查询成长值开关配置', NULL, '/growthValue/isOpen', 'GET', NULL, 1, '2019-03-01 16:25:13', 0);

-- 成长值设置 system_config
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value', 'growth_value_switch', '成长值开关', '成长值开关默认关闭，打开后若已有会员获取成长值，不允许关闭', '0', null, '2019-02-19 17:00:45', '2019-02-23 17:54:35', '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_register', '注册', '会员注册成功后可获得成长值', '0', '{"value":""}', '2019-02-19 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_bind_wechat', '绑定微信', '绑定微信成功获得成长值，每个会员仅可获得一次', '0', '{"value":""}', '2019-02-19 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_add_delivery_address', '添加收货地址', '添加收货地址后获得成长值，每个会员仅可获得一次', '0', '{"value":""}', '2019-02-19 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_follow_store', '关注店铺', '关注店铺可获得成长值', '0', '{"value":"","limit":""}', '2019-02-19 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_introduction', '成长值说明', '请编辑成长值规则介绍，用于前端成长值规则说明', '1', null, '2019-02-19 17:00:45', null, '0');


-- ***************************************** customer *********************************************
-- 客户等级权益表
CREATE TABLE `customer_level_rights` (
  `rights_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `rights_name` varchar(50) NOT NULL COMMENT '权益名称',
  `rights_type` tinyint(4) NOT NULL COMMENT '权益类型 0等级徽章 1专属客服 2会员折扣 3券礼包 4返积分',
  `rights_logo` varchar(255) DEFAULT NULL COMMENT 'logo地址',
  `rights_description` text COMMENT '权益介绍',
  `rights_rule` text COMMENT '权益规则(JSON)',
  `status` tinyint(4) DEFAULT NULL COMMENT '是否开启 0:关闭 1:开启',
  `sort` int(10) DEFAULT '0' COMMENT '排序 默认0',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标识 0:未删除1:已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`rights_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COMMENT='会员等级权益表';

-- 会员等级与权益关联表
CREATE TABLE `customer_level_rights_rel` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_level_id` bigint(10) DEFAULT NULL COMMENT '用户等级id',
  `rights_id` int(10) DEFAULT NULL COMMENT '权益id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COMMENT='会员等级与权益关联表';

-- 权益与优惠券关联表
CREATE TABLE `customer_rights_coupon_rel` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `rights_id` int(10) NOT NULL COMMENT '会员权益Id',
  `coupon_id` varchar(32) NOT NULL COMMENT '优惠券id',
  `coupon_num` int(10) NOT NULL COMMENT '优惠券发放数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权益与优惠券关联表';

-- 商户客户等级表
CREATE TABLE `store_level` (
  `store_level_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `store_id` bigint(20) NOT NULL COMMENT '店铺编号',
  `level_name` varchar(32) NOT NULL COMMENT '等级名称',
  `discount_rate` decimal(10,2) NOT NULL COMMENT '折扣率',
  `amount_conditions` decimal(10,2) DEFAULT NULL COMMENT '客户升级所需累积支付金额',
  `order_conditions` int(10) DEFAULT NULL COMMENT '客户升级所需累积支付订单笔数',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标记 0:未删除 1:已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '更新人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`store_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12313 DEFAULT CHARSET=utf8mb4 COMMENT='商户客户等级表';

-- 修改店铺客户关系表客户店铺等级字段
ALTER TABLE store_customer_rela CHANGE customer_level_id store_level_id BIGINT(10) COMMENT '客户等级标识';

-- 修改店铺客户关系表客户类型备注
ALTER TABLE store_customer_rela MODIFY customer_type TINYINT(4) COMMENT '客户类型,0:店铺关联的客户,1:店铺发展的客户';

-- 客户成长值明细
CREATE TABLE `customer_growth_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户id',
  `type` tinyint(4) NOT NULL COMMENT '操作类型 0:扣除 1:增长',
  `service_type` tinyint(4) NOT NULL COMMENT '业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买  5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信 9添加收货地址\r\n 10关注店铺 11订单完成',
  `growth_value` int(10) NOT NULL COMMENT '成长值value',
  `op_time` datetime NOT NULL COMMENT '操作时间',
  `trade_no` varchar(45) DEFAULT NULL COMMENT '相关单号',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标记 0:未删除 1:已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1675 DEFAULT CHARSET=utf8mb4 COMMENT='客户成长值明细';

ALTER TABLE `customer_level`
ADD COLUMN `growth_value` bigint(10) NULL DEFAULT NULL COMMENT '所需成长值' AFTER `customer_level_discount`,
ADD COLUMN `rank_badge_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '等级徽章' AFTER `growth_value`;
update `customer_level` set growth_value = 0;
-- 客户信息表增加成长值字段
ALTER TABLE `customer`
ADD COLUMN `growth_value` bigint(10) NULL DEFAULT 0 COMMENT '成长值' AFTER `customer_level_id`;

update `customer` set customer_type = 0 ;

-- 客户成长值明细 新增字段content
ALTER TABLE `customer_growth_value`
ADD COLUMN `content` varchar(255) DEFAULT NULL COMMENT '内容备注' AFTER `op_time`;

INSERT INTO `store_level`(`store_id`, `level_name`, `discount_rate`, `amount_conditions`, `order_conditions`, `del_flag`, `create_time`, `create_person`, `update_time`, `update_person`, `delete_time`, `delete_person`) SELECT s.store_id, '普通', 1.00, null, 1, 0, '2019-03-18 09:17:34', '8a9bc76c65ea668e0166148a9e80002c', NULL, NULL, NULL, NULL from store s;
-- 删除店铺等级折扣表
DROP TABLE store_customer_level_rela;
truncate store_customer_rela;
update customer set customer_level_id = 1 where customer_level_id is null;

CREATE TABLE `store_consumer_statistics` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `customer_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户id',
  `store_id` bigint(20) NOT NULL COMMENT '店铺id',
  `trade_count` int(10) DEFAULT '0' COMMENT '会员在该店铺下单数',
  `trade_price_count` decimal(10,2) DEFAULT '0.00' COMMENT '会员在该店铺消费额',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标记 0:未删除 1:已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺客户消费统计表';

ALTER TABLE `customer_growth_value`
ADD INDEX `ind_cust_gro_val_cust_id`(`customer_id`);

-- ***************************************** goods *********************************************
-- 成长值设置—购物获取规则
ALTER TABLE `goods_cate`
ADD COLUMN `growth_value_rate` decimal(8,2) DEFAULT NULL COMMENT '成长值获取比例' AFTER `is_parent_cate_rate`,
ADD COLUMN `is_parent_growth_value_rate` tinyint(4) DEFAULT 1 COMMENT '是否使用上级类目成长值获取比例 0 否  1 是' AFTER `growth_value_rate`;

update goods_cate set growth_value_rate = 0.00 and is_parent_growth_value_rate = 1;


-- ***************************************** order *********************************************
-- 会员权益处理订单成长值 临时表
CREATE TABLE `order_growth_value_temp` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `return_end_time` datetime DEFAULT NULL COMMENT '退货截止时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员权益处理订单成长值 临时表';



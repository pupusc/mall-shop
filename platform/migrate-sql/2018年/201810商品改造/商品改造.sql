-- s2b 业务库
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
/*==============================================================*/
/* Table: system_operation_log  操作日志表 third_id已删除**********************************                               */
/*==============================================================*/
DROP TABLE IF EXISTS `system_operation_log`;
CREATE TABLE `system_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺id',
  `company_info_id` int(11) DEFAULT NULL COMMENT '公司id',
  `op_account` varchar(50) DEFAULT NULL COMMENT '操作人账号',
  `op_name` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人姓名',
  `op_role_name` varchar(45) DEFAULT NULL COMMENT '操作人角色',
  `op_module` varchar(45) DEFAULT NULL COMMENT '所属模块',
  `op_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作类型',
  `op_context` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作内容',
  `op_time` datetime DEFAULT NULL COMMENT '操作时间',
  `op_ip` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作IP',
  `op_mac` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作MAC地址',
  `op_isp` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '网络运营商',
  `op_country` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '所在国家',
  `op_province` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '所在省份',
  `op_city` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '所在城市',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';



drop table if exists s2b.store_goods_tab;
/*==============================================================*/
/* Table: store_goods_tab   店铺配置的商品详情模板                                    */
/*==============================================================*/
create table s2b.store_goods_tab
(
   tab_id               bigint(20) not null auto_increment comment '模板Id',
   tab_name             varchar(50) not null comment '模板名称',
   del_flag             tinyint(4) not null default 0 comment '是否删除标志 0：否，1：是',
   sort                 int(3) not null default 0 comment '排序号(越小越靠前)',
   store_id             bigint(20) not null comment '店铺id',
   create_time          datetime comment '创建时间',
   create_person        national varchar(32) comment '创建人',
   update_time          datetime comment '修改时间',
   update_person        national varchar(32) comment '修改人',
   del_time             datetime comment '删除时间',
   del_person           national varchar(32) comment '删除人',
   primary key (tab_id)
);
alter table s2b.store_goods_tab comment '详情模板tab';


drop table if exists s2b.goods_tab_rela;
/*==============================================================*/
/* Table: goods_tab_rela     商品-详情模板关联表                                   */
/*==============================================================*/
create table s2b.goods_tab_rela
(
   goods_id             varchar(32) not null comment 'spuId',
   tab_id               bigint(20) not null comment '详情模板id',
   tab_detail           text comment '内容详情',
   primary key (goods_id, tab_id)
);
alter table s2b.goods_tab_rela comment '商品详情模板关联表';


ALTER TABLE `standard_goods`
ADD COLUMN `goods_video`  varchar(255) NULL COMMENT '商品视频' AFTER `goods_img`;


ALTER TABLE `goods`
ADD COLUMN `sale_type`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '销售类别(0:批发,1:零售)' AFTER `goods_id`,
ADD COLUMN `goods_video`  varchar(255) NULL COMMENT '商品视频' AFTER `goods_img`,
ADD COLUMN `line_price`  decimal(20,2) NULL COMMENT '划线价' AFTER `market_price`,
ADD COLUMN `allow_price_set`  tinyint(1) NULL DEFAULT 1 COMMENT '订货量设价时,是否允许sku独立设阶梯价(0:不允许,1:允许)' AFTER `price_type`;

INSERT INTO `system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('s2b_audit', 'user_audit', '用户设置', '用户访问商城是否需要登录', '0', null, '2018-09-25 11:41:40', '2018-09-27 10:28:35', '0');
INSERT INTO `system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('s2b_audit', 'pc_goods_image_switch', 'PC商城商品列表默认展示', 'PC商城商品列表默认展示', '1', null, '2018-10-15 19:14:35', '2018-11-01 16:26:59', '0');
INSERT INTO `system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('s2b_audit', 'pc_goods_spec_switch', 'PC商城商品列表展示维度', 'PC商城商品列表展示维度', '1', null, '2018-10-15 19:16:44', '2018-11-01 16:28:27', '0');
INSERT INTO `system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('s2b_audit', 'mobile_goods_image_switch', '移动商品列表默认展示', '移动商品列表默认展示', '0', null, '2018-10-15 19:17:19', '2018-11-01 16:26:56', '0');
INSERT INTO `system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('s2b_audit', 'mobile_goods_spec_switch', '移动商城商品列表展示维度', '移动商城商品列表展示维度', '0', null, '2018-10-15 19:17:54', '2018-10-30 20:16:24', '0');




-- open 开放平台库
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
-- ----------------------------
-- Table structure for store_resource
-- ----------------------------
DROP TABLE IF EXISTS `store_resource`;
CREATE TABLE `store_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '素材ID',
  `resource_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '资源类型(0:图片,1:视频)',
  `cate_id` bigint(20) NOT NULL COMMENT '素材分类ID',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺标识',
  `company_info_id` int(11) DEFAULT NULL COMMENT '商家标识',
  `resource_key` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '素材KEY',
  `resource_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '素材名称',
  `artwork_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '素材地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `server_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'oss服务器类型，对应system_config的config_type',
  `app_key` varchar(12) CHARACTER SET utf8 NOT NULL COMMENT '应用唯一标识',
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺素材资源表';

-- ----------------------------
-- Table structure for store_resource_cate
-- ----------------------------
DROP TABLE IF EXISTS `store_resource_cate`;
CREATE TABLE `store_resource_cate` (
  `cate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '素材分类id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺标识',
  `company_info_id` int(11) DEFAULT NULL COMMENT '商家标识',
  `cate_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '分类名称',
  `cate_parent_id` bigint(20) DEFAULT NULL COMMENT '父分类ID',
  `cate_img` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '分类图片',
  `cate_path` varchar(1000) CHARACTER SET utf8 NOT NULL COMMENT '分类层次路径,例1|01|001',
  `cate_grade` tinyint(4) NOT NULL COMMENT '分类层级',
  `pin_yin` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '拼音',
  `s_pin_yin` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '简拼',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `sort` tinyint(4) DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(4) DEFAULT NULL COMMENT '是否默认,0:否1:是',
  `app_key` varchar(12) CHARACTER SET utf8 NOT NULL COMMENT '应用唯一标识',
  PRIMARY KEY (`cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺素材资源分类表';

-- ----------------------------
-- Table structure for system_resource
-- ----------------------------
DROP TABLE IF EXISTS `system_resource`;
CREATE TABLE `system_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '素材资源ID',
  `resource_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '资源类型(0:图片,1:视频)',
  `cate_id` bigint(20) NOT NULL COMMENT '素材分类ID',
  `resource_key` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '素材KEY',
  `resource_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '素材名称',
  `artwork_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '素材地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `server_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'oss服务器类型，对应system_config的config_type',
  `app_key` varchar(12) CHARACTER SET utf8 NOT NULL COMMENT '应用唯一标识',
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台素材资源表';

-- ----------------------------
-- Table structure for system_resource_cate
-- ----------------------------
DROP TABLE IF EXISTS `system_resource_cate`;
CREATE TABLE `system_resource_cate` (
  `cate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '素材资源分类id',
  `cate_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '分类名称',
  `cate_parent_id` bigint(20) DEFAULT NULL COMMENT '父分类ID',
  `cate_img` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '分类图片',
  `cate_path` varchar(1000) CHARACTER SET utf8 NOT NULL COMMENT '分类层次路径,例1|01|001',
  `cate_grade` tinyint(4) NOT NULL COMMENT '分类层级',
  `pin_yin` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '拼音',
  `s_pin_yin` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '简拼',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `sort` tinyint(4) DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(4) DEFAULT NULL COMMENT '是否默认,0:否1:是',
  `app_key` varchar(12) CHARACTER SET utf8 NOT NULL COMMENT '应用唯一标识',
  PRIMARY KEY (`cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='素材资源分类表';

-- 迁移原有平台图片素材数据
INSERT INTO system_resource SELECT
	system_image.image_id,
	0,
	system_image.cate_id,
	system_image.image_key,
	system_image.image_name,
	system_image.artwork_url,
	system_image.create_time,
	system_image.update_time,
	system_image.del_flag,
	system_image.server_type,
	system_image.app_key
FROM
	system_image;
-- 迁移原有平台图片素材分类数据
INSERT INTO system_resource_cate SELECT
	system_image_cate.cate_id,
	system_image_cate.cate_name,
	system_image_cate.cate_parent_id,
	system_image_cate.cate_img,
	system_image_cate.cate_path,
	system_image_cate.cate_grade,
	system_image_cate.pin_yin,
	system_image_cate.s_pin_yin,
	system_image_cate.create_time,
	system_image_cate.update_time,
	system_image_cate.del_flag,
	system_image_cate.sort,
	system_image_cate.is_default,
	system_image_cate.app_key
FROM
	system_image_cate;

-- 迁移原有店铺图片素材数据
INSERT INTO store_resource SELECT
	store_image.image_id,
	0,
	store_image.cate_id,
	store_image.store_id,
	store_image.company_info_id,
	store_image.image_key,
	store_image.image_name,
	store_image.artwork_url,
	store_image.create_time,
	store_image.update_time,
	store_image.del_flag,
	store_image.server_type,
	store_image.app_key
FROM
	store_image;
-- 迁移原有店铺图片素材分类数据
INSERT INTO store_resource_cate SELECT
store_image_cate.cate_id,
store_image_cate.store_id,
store_image_cate.company_info_id,
store_image_cate.cate_name,
store_image_cate.cate_parent_id,
store_image_cate.cate_img,
store_image_cate.cate_path,
store_image_cate.cate_grade,
store_image_cate.pin_yin,
store_image_cate.s_pin_yin,
store_image_cate.create_time,
store_image_cate.update_time,
store_image_cate.del_flag,
store_image_cate.sort,
store_image_cate.is_default,
store_image_cate.app_key
FROM
store_image_cate;

--初始化所有app-key的resource_server
INSERT INTO system_config (
	config_key,
	config_type,
	config_name,
	remark,
	STATUS,
	context,
	create_time,
	update_time,
	del_flag,
	app_key
) SELECT
	'resource_server',
	'aliYun',
	'阿里云',
	NULL,
	1,
	'{"accessKeyId":"LTAIcvnPzCTKgdFW","accessKeySecret":"4By4Ag5zFQLjaMcvtePL9povz1zC7W","bucketName":"wanmi-b2b","endPoint":"oss-cn-shanghai.aliyuncs.com"}',
	'2018-07-09 15:25:50',
	'2018-07-09 15:25:50',
	0,
	t.app_key
FROM
	`system_config` t
GROUP BY
	t.app_key
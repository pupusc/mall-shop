-- ***************************************** xxl-job begin *********************************************
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0 1 * * ?', '店铺评分统计', '2019-04-16 14:20:47', '2019-04-28 19:49:58', '吕振伟', '', 'FIRST', 'storeEvaluateStatisticsHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-04-16 14:20:47', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0 1 * * ?', '商品自动评价定时任务', '2019-04-22 18:03:26', '2019-04-22 18:03:26', '吕振伟', '', 'FIRST', 'autoGoodsEvaluateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-04-22 18:03:26', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0 1 * * ?', '店铺服务自动评价定时任务', '2019-04-22 18:03:57', '2019-04-22 18:03:57', '吕振伟', '', 'FIRST', 'autoStoreEvaluateJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-04-22 18:03:57', '');
-- ***************************************** xxl-job end *********************************************





-- ***************************************** customer begin *********************************************
-- 删除重复账号的业务员信息,并给账号增加唯一索引
DELETE
FROM
	employee
WHERE
	account_name IN (
SELECT
	x.account_name
FROM
	( SELECT a.account_name FROM employee a GROUP BY a.account_name HAVING count( 1 ) > 1 ) x
	);
ALTER TABLE `employee`
ADD UNIQUE INDEX `emp_account_ind`(`account_name`);

DELETE
FROM
	customer_level_rights_rel
WHERE
	rights_id IN ( SELECT r.rights_id FROM `customer_level_rights` r WHERE r.rights_type = 3 );
delete from `customer_level_rights` WHERE rights_type = 3;
-- 删除会员权益与优惠券的联系
DROP TABLE customer_rights_coupon_rel;

-- 支持微信昵称为emoji
ALTER TABLE `third_login_relation`
MODIFY COLUMN `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信昵称' AFTER `binding_time`;

-- 会员积分明细表
CREATE TABLE `customer_points_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户id',
  `customer_account` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '用户名',
  `type` tinyint(4) NOT NULL COMMENT '操作类型 0:扣除 1:增长',
  `service_type` tinyint(4) NOT NULL COMMENT '业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买 5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信 9添加收货地址 10关注店铺 11订单完成 12订单抵扣 13优惠券兑换 14积分兑换 15退单返还 16订单取消返还',
  `points` int(10) NOT NULL COMMENT '积分数量',
  `content` varchar(255) DEFAULT NULL COMMENT '内容备注',
  `points_available` int(10) DEFAULT NULL COMMENT '积分余额',
  `op_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1702 DEFAULT CHARSET=utf8mb4 COMMENT='会员积分明细';

-- 会员表新增可用积分/已用积分字段
ALTER TABLE `customer`
ADD COLUMN `points_available` bigint(10) NULL DEFAULT '0' COMMENT '可用积分' AFTER `growth_value`,
ADD COLUMN `points_used` bigint(10) NULL DEFAULT '0' COMMENT '已用积分' AFTER `points_available`;
UPDATE customer SET points_available = 0, points_used =0;

ALTER TABLE `customer_level_rights`
ADD COLUMN `activity_id` varchar(32) NULL COMMENT '优惠券活动id' AFTER `rights_rule`;

CREATE TABLE `store_evaluate`(
	`evaluate_id` VARCHAR(32) NOT NULL COMMENT '评价id' ,
	`store_id` BIGINT(20) DEFAULT NULL COMMENT '店铺Id' ,
	`store_name` VARCHAR(150) DEFAULT NULL COMMENT '店铺名称' ,
	`order_no` VARCHAR(255) NOT NULL COMMENT '订单号' ,
	`customer_id` VARCHAR(32) NOT NULL COMMENT '会员Id' ,
	`customer_name` VARCHAR(128) DEFAULT NULL COMMENT '会员名称' ,
	`customer_account` VARCHAR(20) NOT NULL COMMENT '会员登录账号|手机号' ,
	`goods_score` TINYINT(4) NOT NULL DEFAULT '5' COMMENT '商品评分' ,
	`server_score` TINYINT(4) NOT NULL DEFAULT '5' COMMENT '服务评分' ,
	`logistics_score` TINYINT(4) NOT NULL DEFAULT '5' COMMENT '物流评分' ,
	`composite_score` DECIMAL(20 , 2) DEFAULT NULL COMMENT '综合评分（冗余字段看后面怎么做）' ,
	`del_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是' ,
	`create_time` DATETIME DEFAULT NULL COMMENT '创建时间' ,
	`create_person` VARCHAR(32) DEFAULT NULL COMMENT '创建人' ,
	`update_time` DATETIME DEFAULT NULL COMMENT '修改时间' ,
	`update_person` VARCHAR(32) DEFAULT NULL COMMENT '修改人' ,
	`del_time` DATETIME DEFAULT NULL COMMENT '删除时间' ,
	`del_person` VARCHAR(32) DEFAULT NULL COMMENT '删除人' ,
	PRIMARY KEY(`evaluate_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '店铺评价表';

CREATE TABLE `store_evaluate_sum`  (
  `sum_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id 主键',
  `store_id` bigint(20) NULL COMMENT '店铺id',
  `store_name` VARCHAR(150) DEFAULT NULL COMMENT '店铺名称' ,
  `sum_server_score` decimal(10, 2) NULL COMMENT '服务综合评分',
  `sum_goods_score` decimal(10, 2) NULL COMMENT '商品质量综合评分',
  `sum_logistics_score_score` decimal(10, 2) NULL COMMENT '物流综合评分',
  `sum_composite_score` DECIMAL(20 , 2) DEFAULT NULL COMMENT '综合评分' ,
  `order_num` int(11) NULL COMMENT '订单数',
  `score_cycle` tinyint(4) NULL COMMENT '评分周期 0：30天，1：90天，2：180天',
  PRIMARY KEY (`sum_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '店铺评分聚合表';

CREATE TABLE `store_evaluate_num`  (
  `num_id` VARCHAR(32) NOT NULL COMMENT 'id 主键',
  `store_id` bigint(20) NULL COMMENT '店铺id',
  `store_name` VARCHAR(150) DEFAULT NULL COMMENT '店铺名称' ,
  `excellent_num` bigint(10) NULL COMMENT '优秀评分数（5星-4星）',
  `medium_num` bigint(10) NULL COMMENT '中等评分数（3星）',
  `difference_num` bigint(10) NULL COMMENT '差的评分数（1星-2星）',
  `sum_composite_score` DECIMAL(20 , 2) DEFAULT NULL COMMENT '综合评分' ,
  `score_cycle` tinyint(4) NULL COMMENT '评分周期 0：30天，1：90天，2：180天',
  `num_type` tinyint(4) NULL COMMENT '统计类型 0：商品评分，1：服务评分，2：物流评分',
  PRIMARY KEY (`num_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '店铺评价数统计表';
-- ***************************************** customer end *********************************************





-- ***************************************** goods begin *********************************************
CREATE TABLE `goods_evaluate` (
  `evaluate_id` varchar(32) NOT NULL COMMENT '评价id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺Id',
  `store_name` varchar(150) DEFAULT NULL COMMENT '店铺名称',
  `goods_id` varchar(32) NOT NULL COMMENT '商品id(spuId)',
  `goods_info_id` varchar(32) NOT NULL COMMENT '货品id(skuId)',
  `goods_info_name` varchar(255) NOT NULL COMMENT '商品名称',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `buy_time` datetime DEFAULT NULL COMMENT '购买时间',
  `goods_img` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `spec_details` varchar(255) DEFAULT NULL COMMENT '规格描述信息',
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '会员名称',
  `customer_account` varchar(20) NOT NULL COMMENT '会员登录账号|手机号',
  `evaluate_score` tinyint(4) NOT NULL DEFAULT '5' COMMENT '商品评分',
  `evaluate_content` varchar(500) DEFAULT NULL COMMENT '商品评价内容',
  `evaluate_time` datetime DEFAULT NULL COMMENT '发表评价时间',
  `evaluate_answer` varchar(500) DEFAULT NULL COMMENT '评论回复',
  `evaluate_answer_time` datetime DEFAULT NULL COMMENT '回复时间',
  `evaluate_answer_account_name` varchar(45) DEFAULT NULL COMMENT '回复人账号',
  `evaluate_answer_employee_id` varchar(32) DEFAULT NULL COMMENT '回复员工Id',
  `history_evaluate_score` tinyint(4) DEFAULT NULL COMMENT '历史商品评分',
  `history_evaluate_content` varchar(500) DEFAULT NULL COMMENT '历史商品评价内容',
  `history_evaluate_time` datetime DEFAULT NULL COMMENT '历史发表评价时间',
  `history_evaluate_answer` varchar(500) DEFAULT NULL COMMENT '历史评论回复',
  `history_evaluate_answer_time` datetime DEFAULT NULL COMMENT '历史回复时间',
  `history_evaluate_answer_account_name` varchar(45) DEFAULT NULL COMMENT '历史回复人账号',
  `history_evaluate_answer_employee_id` varchar(32) DEFAULT NULL COMMENT '历史回复员工Id',
  `good_num` int(11) DEFAULT '0' COMMENT '点赞数',
  `is_anonymous` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否匿名 0：否，1：是',
  `is_answer` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已回复 0:否,1:是',
  `is_edit` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已经修改 0：否，1：是',
  `is_show` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否展示 0：否，1：是',
  `is_upload` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否晒单 0：否，1：是',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`evaluate_id`),
  KEY `customer_id_index` (`customer_id`) USING BTREE COMMENT 'customer_id索引',
  KEY `goods_id_index` (`goods_id`,`create_time`) USING BTREE COMMENT 'goods_id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品评价表';

CREATE TABLE `goods_evaluate_image`(
	`image_id` VARCHAR(32) NOT NULL COMMENT '图片Id' ,
	`evaluate_id` VARCHAR(32) NOT NULL COMMENT '评价id' ,
	`goods_id` varchar(32) NOT NULL COMMENT '商品id(spuId)',
	`image_key` VARCHAR(255) DEFAULT NULL COMMENT '图片KEY' ,
	`image_name` VARCHAR(45) DEFAULT NULL COMMENT '图片名称' ,
	`artwork_url` VARCHAR(255) DEFAULT NULL COMMENT '原图地址' ,
	`is_show` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否展示 0：否，1：是',
	`create_time` DATETIME DEFAULT NULL COMMENT '创建时间' ,
	`update_time` DATETIME DEFAULT NULL COMMENT '更新时间' ,
	`del_flag` TINYINT(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除' ,
	PRIMARY KEY(`image_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '评价图片';

CREATE TABLE `store_tobe_evaluate` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺Id',
  `store_logo` varchar(255) DEFAULT NULL COMMENT '店铺logo',
  `store_name` varchar(150) DEFAULT NULL COMMENT '店铺名称',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `goods_num` int(5) DEFAULT NULL COMMENT '购买数量',
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '会员名称',
  `customer_account` varchar(20) NOT NULL COMMENT '会员登录账号|手机号',
  `auto_store_evaluate_date` date DEFAULT NULL COMMENT '店铺自动评价日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `customer_id_index` (`customer_id`) USING BTREE COMMENT '会员id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='店铺待评价表';

CREATE TABLE `goods_tobe_evaluate` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺Id',
  `store_name` varchar(150) DEFAULT NULL COMMENT '店铺名称',
  `goods_id` varchar(32) NOT NULL COMMENT '商品id(spuId)',
  `goods_img` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `goods_info_id` varchar(32) NOT NULL COMMENT '货品id(skuId)',
  `goods_info_name` varchar(255) NOT NULL COMMENT '商品名称',
  `goods_spec_detail` varchar(45) DEFAULT NULL COMMENT '规格值名称',
  `buy_time` datetime DEFAULT NULL COMMENT '购买时间',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '会员名称',
  `customer_account` varchar(20) NOT NULL COMMENT '会员登录账号|手机号',
  `evaluate_status` tinyint(4) DEFAULT NULL COMMENT '是否评价 0：未评价，1：已评价',
  `evaluate_img_status` tinyint(4) DEFAULT NULL COMMENT '是否晒单 0：未晒单，1：已晒单',
  `auto_goods_evaluate_date` date DEFAULT NULL COMMENT '商品自动评价日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `customer_id_index` (`customer_id`) USING BTREE COMMENT 'customer_id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品待评价表';

-- 商品表增加商品评论数、商品收藏量、商品销量和商品好评率字段
ALTER TABLE `goods`
ADD COLUMN `goods_evaluate_num` bigint(11) DEFAULT '0' COMMENT '商品评论数' AFTER `company_type`,
ADD COLUMN `goods_collect_num` bigint(11) DEFAULT '0' COMMENT '商品收藏量' AFTER `goods_evaluate_num`,
ADD COLUMN `goods_sales_num` bigint(11) DEFAULT '0' COMMENT '商品销量' AFTER `goods_collect_num`,
ADD COLUMN `goods_favorable_comment_num` bigint(11) DEFAULT '0' COMMENT '商品好评数量' AFTER `goods_sales_num`;
-- 积分获取—购物获取规则
ALTER TABLE `goods_cate`
ADD COLUMN `points_rate` decimal(8,2) DEFAULT NULL COMMENT '积分获取比例',
ADD COLUMN `is_parent_points_rate` tinyint(4) DEFAULT NULL COMMENT '是否使用上级类目积分获取比例 0 否  1 是';

update goods_cate set points_rate = 0.00 , is_parent_points_rate = 1;
-- ***************************************** goods end *********************************************





-- ***************************************** setting bigin *********************************************
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a014734016a0159b5950001', 4, 'fc8df6013fe311e9828800163e0fc468', 3, '客户积分', '/points-list', NULL, 130, '2019-04-09 17:06:58', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a014734016a015883a10000', 4, '0', 1, '客户积分', 'points-list', NULL, 2, '2019-04-09 17:05:39', 1);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e15e9bc90000', 4, 'fc8e017c3fe311e9828800163e0fc468', 3, '敏感词库', '/sensitive-words', NULL, 150, '2019-04-03 12:04:28', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386e169dccf5d0169dd3a1d0c0000', 4, '4431e9aa40d911e99b6f0050568808e1', 3, '积分设置', '/points-setting', NULL, 1, '2019-04-02 16:46:07', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769bea2120169dcd2c2d00002', 4, 'fc8df3b83fe311e9828800163e0fc468', 3, '商家评价', '/supplier-evaluate-list', NULL, 116, '2019-04-02 14:53:14', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769bea2120169dcd147980001', 3, 'fc8e5a363fe311e9828800163e0fc468', 3, '评价管理', '/goods-evaluate-list', NULL, 14, '2019-04-02 14:51:37', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769bea2120169dcd039ef0000', 4, 'fc8df1b53fe311e9828800163e0fc468', 3, '评价管理', '/goods-evaluate-list', NULL, 6, '2019-04-02 14:50:28', 0);

INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a4d0c39016a4e18a5440001', 3, 'fc8e24d13fe311e9828800163e0fc468', '客户等级新增', 'f_store_level_add_1', NULL, 45, '2019-04-24 14:46:39', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44db71ec000b', 4, '8ab369076a014734016a0159b5950001', '客户积分详情', 'f_customer_points_d', NULL, 15, '2019-04-22 19:43:13', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44d690480006', 4, '8ab369076a014734016a0159b5950001', '积分列表导出', 'f_points_export', NULL, 10, '2019-04-22 19:37:53', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44d246560004', 4, '8ab369076a014734016a0159b5950001', '积分列表查询', 'f_points_list_q', NULL, 5, '2019-04-22 19:33:12', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a42e480004', 4, '2c9386e169dccf5d0169dd3a1d0c0000', '积分设置编辑', 'f_points_setting_edit', NULL, 1, '2019-04-09 18:28:18', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a088920000', 4, '2c9386e169dccf5d0169dd3a1d0c0000', '积分设置查看', 'f_points_setting_view', NULL, 1, '2019-04-09 18:24:19', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e182d1b10004', 4, '8ab3690769ddf8c50169e15e9bc90000', '编辑敏感词', 'f_edit_words', NULL, 15, '2019-04-03 12:44:01', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e182957f0003', 4, '8ab3690769ddf8c50169e15e9bc90000', '删除敏感词', 'f_delete_words', NULL, 10, '2019-04-03 12:43:45', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e18261d20002', 4, '8ab3690769ddf8c50169e15e9bc90000', '添加敏感词', 'f_add_words', NULL, 5, '2019-04-03 12:43:32', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e182175d0001', 4, '8ab3690769ddf8c50169e15e9bc90000', '敏感词列表查询', 'f_sensitive_words_0', NULL, 1, '2019-04-03 12:43:13', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d571e4000e', 3, '8ab3690769bea2120169dcd147980001', '商品评价查看', 'f_goods_eval_q', NULL, 10, '2019-04-29 14:43:23', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67c9a8b70007', 4, '8ab3690769bea2120169dcd039ef0000', '商品评价查看', 'f_goods_eval_manage', NULL, 10, '2019-04-29 14:30:30', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67c6bfc70003', 4, '8ab3690769bea2120169dcd2c2d00002', '商家评价查看', 'f_supplier_eval', NULL, 10, '2019-04-29 14:27:20', 0);

INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67bbaab60002', 4, 'fc926c0c3fe311e9828800163e0fc468', '获取商品评价开关配置', NULL, '/boss/config/audit/list-goods-configs', 'GET', NULL, 10, '2019-04-29 14:15:13', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d897c90014', 3, '8a9bc76c6a673f39016a67d571e4000e', '服务评价-评价历史', NULL, '/store/evaluate/num/page', 'POST', NULL, 60, '2019-04-29 14:46:49', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d8284c0013', 3, '8a9bc76c6a673f39016a67d571e4000e', '服务评价-评价汇总', NULL, '/store/evaluate/num/storeEvaluateNumByStoreIdAndScoreCycle', 'POST', NULL, 50, '2019-04-29 14:46:20', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d7519a0012', 3, '8a9bc76c6a673f39016a67d571e4000e', '评价回复', NULL, '/goods/evaluate/answer', 'POST', NULL, 40, '2019-04-29 14:45:26', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d6d2180011', 3, '8a9bc76c6a673f39016a67d571e4000e', '评价详情查看', NULL, '/goods/evaluate/detail', 'POST', NULL, 30, '2019-04-29 14:44:53', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d631310010', 3, '8a9bc76c6a673f39016a67d571e4000e', '店铺评价总况', NULL, '/store/evaluate/sum/getByStoreId', 'POST', NULL, 20, '2019-04-29 14:44:12', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d5d1ea000f', 3, '8a9bc76c6a673f39016a67d571e4000e', '商品评价列表查询', NULL, '/goods/evaluate/page', 'POST', NULL, 10, '2019-04-29 14:43:47', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67cecfc0000d', 4, '8a9bc76c6a673f39016a67c6bfc70003', '评价详情-评价总况', NULL, '/store/evaluate/getByStoreId', 'POST', NULL, 60, '2019-04-29 14:36:08', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67ce688e000c', 4, '8a9bc76c6a673f39016a67c6bfc70003', '评价详情-服务评价列表', NULL, '/boss/store/evaluate/page', 'POST', NULL, 50, '2019-04-29 14:35:42', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67cdfbd0000b', 4, '8a9bc76c6a673f39016a67c6bfc70003', '评价详情-服务评价', NULL, '/boss/store/evaluate/storeEvaluateNumByStoreIdAndScoreCycle', 'POST', NULL, 40, '2019-04-29 14:35:14', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67cb9e6e000a', 4, '8a9bc76c6a673f39016a67c9a8b70007', '商品评价回复', NULL, '/goods/evaluate/answer', 'POST', NULL, 30, '2019-04-29 14:32:39', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67cb00020009', 4, '8a9bc76c6a673f39016a67c9a8b70007', '商品评价详情', NULL, '/goods/evaluate/detail', 'POST', NULL, 20, '2019-04-29 14:31:58', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67ca9eeb0008', 4, '8a9bc76c6a673f39016a67c9a8b70007', '商品评价列表查看', NULL, '/goods/evaluate/page', 'POST', NULL, 10, '2019-04-29 14:31:33', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67c893210006', 4, '8a9bc76c6a673f39016a67c6bfc70003', '评价系数编辑', NULL, '/evaluate/ratio/update', 'PUT', NULL, 30, '2019-04-29 14:29:19', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67c7dd600005', 4, '8a9bc76c6a673f39016a67c6bfc70003', '评价系数查看', NULL, '/evaluate/ratio/getEvaluateInfo', 'GET', NULL, 20, '2019-04-29 14:28:33', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67c719ff0004', 4, '8a9bc76c6a673f39016a67c6bfc70003', '商家评价列表', NULL, '/store/evaluate/page', 'POST', NULL, 10, '2019-04-29 14:27:43', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a4d0c39016a4e466ae20005', 3, '8ab3690769905b6b01699390c4a30001', '非自营店铺等级删除', NULL, '/store/storeLevel/deleteById/*', 'DELETE', NULL, 10, '2019-04-24 15:36:39', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a4d0c39016a4e1eb57a0003', 3, 'fc9345b53fe311e9828800163e0fc468', '非自营店铺等级编辑', NULL, '/store/storeLevel/modify', 'PUT', NULL, 10, '2019-04-24 14:53:17', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a4d0c39016a4e1b888f0002', 3, '8ab369076a4d0c39016a4e18a5440001', '非自营店铺等级新增', NULL, '/store/storeLevel/add', 'POST', NULL, 10, '2019-04-24 14:49:48', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a4d0c39016a4e17376a0000', 3, 'fc9342743fe311e9828800163e0fc468', '非自营店铺等级查看', NULL, '/store/storeLevel/list', 'GET', NULL, 10, '2019-04-24 14:45:06', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a48279a016a4906546e0000', 4, 'fc9258493fe311e9828800163e0fc468', '获取登录人信息', NULL, '/customer/employee/myself', 'GET', NULL, 1, '2019-04-23 15:08:33', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44dc79b4000d', 4, '8ab369076a3e7013016a44db71ec000b', '获取客户信息', NULL, '/customer/*', 'GET', NULL, 10, '2019-04-22 19:44:21', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44dbd98c000c', 4, '8ab369076a3e7013016a44db71ec000b', '客户积分明细记录', NULL, '/customer/points/pageDetail', 'POST', NULL, 5, '2019-04-22 19:43:40', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44da9b4f000a', 4, '8ab369076a3e7013016a44d690480006', '积分列表导出', NULL, '/customer/points/export/*', 'GET', NULL, 5, '2019-04-22 19:42:18', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44d968e40009', 4, '8ab369076a3e7013016a44d246560004', '客户积分列表', NULL, '/customer/points/page', 'POST', NULL, 15, '2019-04-22 19:41:00', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44d89acd0008', 4, '8ab369076a3e7013016a44d246560004', '客户积分明细记录', NULL, '/customer/points/pageDetail', 'POST', NULL, 10, '2019-04-22 19:40:07', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076a3e7013016a44d7e4ef0007', 4, '8ab369076a3e7013016a44d246560004', '积分统计数据', NULL, '/customer/points/queryIssueStatistics', 'POST', NULL, 5, '2019-04-22 19:39:21', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a7c90e0007', 4, '2c9386bd6a014a2a016a01a42e480004', '修改积分购物获取规则', NULL, '/goods/goodsCate', 'PUT', NULL, 3, '2019-04-09 18:32:14', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a7350a0006', 4, '2c9386bd6a014a2a016a01a42e480004', '修改积分基础获取规则', NULL, '/points/basicRules', 'PUT', NULL, 2, '2019-04-09 18:31:36', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a6be0f0005', 4, '2c9386bd6a014a2a016a01a42e480004', '修改积分设置', NULL, '/boss/pointsConfig', 'PUT', NULL, 1, '2019-04-09 18:31:06', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a3b8fa0003', 4, '2c9386bd6a014a2a016a01a088920000', '查询积分购物获取规则', NULL, '/goods/goodsCates', 'GET', NULL, 3, '2019-04-09 18:27:48', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a350e10002', 4, '2c9386bd6a014a2a016a01a088920000', '查询积分基础获取规则', NULL, '/points/basicRules', 'GET', NULL, 2, '2019-04-09 18:27:21', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c9386bd6a014a2a016a01a1c8570001', 4, '2c9386bd6a014a2a016a01a088920000', '查询积分配置', NULL, '/boss/pointsConfig', 'GET', NULL, 1, '2019-04-09 18:25:41', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e1851ce9000a', 4, '8ab3690769ddf8c50169e182d1b10004', '编辑敏感词', NULL, '/sensitiveWords/edit', 'POST', NULL, 5, '2019-04-03 12:46:31', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e184eab40009', 4, '8ab3690769ddf8c50169e182d1b10004', '编辑查询敏感词', NULL, '/sensitiveWords/sensitiveWordsById/*', 'GET', NULL, 1, '2019-04-03 12:46:18', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e184a11f0008', 4, '8ab3690769ddf8c50169e182957f0003', '批量删除敏感词', NULL, '/sensitiveWords/batchDelete', 'POST', NULL, 5, '2019-04-03 12:45:59', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e184563e0007', 4, '8ab3690769ddf8c50169e182957f0003', '删除敏感词', NULL, '/sensitiveWords/delete/*', 'DELETE', NULL, 1, '2019-04-03 12:45:40', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e183ff420006', 4, '8ab3690769ddf8c50169e18261d20002', '添加敏感词', NULL, '/sensitiveWords/add', 'POST', NULL, 1, '2019-04-03 12:45:18', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769ddf8c50169e183901d0005', 4, '8ab3690769ddf8c50169e182175d0001', '获取敏感词列表', NULL, '/sensitiveWords/sensitiveWordsList', 'POST', NULL, 1, '2019-04-03 12:44:50', 0);



update `authority` set authority_url = '/store/storeLevel/listBoss', request_type='GET' where authority_id = 'fc98257d3fe311e9828800163e0fc468';
delete from `authority` where authority_id = 'fc9911093fe311e9828800163e0fc468';
delete from `authority` where authority_id = 'fc9825d93fe311e9828800163e0fc468';
delete from `authority` where authority_id = 'fc9abae63fe311e9828800163e0fc468';
update `function_info` set menu_id = 'fc8e24d13fe311e9828800163e0fc468' where function_id = '8ab3690769905b6b01699390c4a30001';

INSERT INTO `system_config`( `config_key` , `config_type` , `config_name` , `remark` , `status` , `create_time` , `update_time` , `del_flag` )
VALUES ( 'goods_setting' , 'goods_evaluate_setting' , '商品评价设置' , '开启评价功能，会员购买订单后可对商家服务和商品进行评价,在商品列表、商品详情、店铺列表、店铺详情页面查看相应评价内容' , '0' , now() , NULL , '0' );
-- 系统积分设置表
CREATE TABLE `system_points_config`  (
  `points_config_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `over_points_available` bigint(10) NULL DEFAULT 0 COMMENT '满x积分可用',
  `max_deduction_rate` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '积分抵扣限额',
  `points_worth` bigint(10) NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '积分说明',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '是否启用标志 0：停用，1：启用',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`points_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统积分设置' ROW_FORMAT = Compact;
INSERT INTO `system_points_config`(`points_config_id`, `over_points_available`, `max_deduction_rate`, `remark`, `status`, `del_flag`, `create_time`, `update_time`, `points_worth`) VALUES ('2c9386e169dbb25b0169dc9908f10000', 200, 100.00, '<p></p>', 1, 0, '2019-04-29 12:50:11', '2019-04-29 12:50:11', 100);

-- 系统成长值设置表
CREATE TABLE `system_growth_value_config` (
  `growth_value_config_id` varchar(32) NOT NULL COMMENT '主键',
  `rule` tinyint(4) DEFAULT '0' COMMENT '成长值获取规则',
  `remark` text COMMENT '成长值说明',
  `status` tinyint(4) DEFAULT '0' COMMENT '是否启用标志 0：停用，1：启用',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`growth_value_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统成长值设置';

CREATE TABLE `evaluate_ratio`(
	`ratio_id` VARCHAR(32) NOT NULL COMMENT '系数id' ,
	`goods_ratio` DECIMAL(20 , 2) DEFAULT NULL COMMENT '商品评论系数' ,
	`server_ratio` DECIMAL(20 , 2) DEFAULT NULL COMMENT '服务评论系数' ,
	`logistics_ratio` DECIMAL(20 , 2) DEFAULT NULL COMMENT '物流评分系数' ,
	`del_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是' ,
	`create_time` DATETIME DEFAULT NULL COMMENT '创建时间' ,
	`create_person` VARCHAR(32) DEFAULT NULL COMMENT '创建人' ,
	`update_time` DATETIME DEFAULT NULL COMMENT '修改时间' ,
	`update_person` VARCHAR(32) DEFAULT NULL COMMENT '修改人' ,
	`del_time` DATETIME DEFAULT NULL COMMENT '删除时间' ,
	`del_person` VARCHAR(32) DEFAULT NULL COMMENT '删除人' ,
	PRIMARY KEY(`ratio_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '评论系数表（有且只有一条数据）';
INSERT INTO `evaluate_ratio`(`ratio_id`, `goods_ratio`, `server_ratio`, `logistics_ratio`, `del_flag`, `create_time`, `create_person`, `update_time`, `update_person`, `del_time`, `del_person`) VALUES ('1', 0.83, 0.15, 0.02, 0, '2019-02-28 14:56:57', NULL, NULL, NULL, NULL, NULL);
CREATE TABLE `sensitive_words`  (
  `sensitive_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '敏感词id 主键',
  `sensitive_words` varchar(255) NULL COMMENT '敏感词内容',
  `del_flag` tinyint(4) NULL COMMENT '是否删除',
  `create_user` varchar(255) NULL COMMENT '创建人',
  `create_time` datetime(0) NULL COMMENT '创建时间',
  `update_user` varchar(255) NULL COMMENT '修改人',
  `update_time` datetime(0) NULL COMMENT '修改时间',
  `delete_user` varchar(255) NULL COMMENT '删除人',
  `delete_time` datetime(0) NULL COMMENT '删除时间',
  PRIMARY KEY (`sensitive_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '敏感词表';

-- 成长值基础规则补充
delete from system_config where config_key = 'growth_value_basic_rule';
-- insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_sign_in', '签到', '会员每日仅可完成一次签到', '0', '{"value":"","continue":"","extra":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_register', '注册', '会员注册成功后可获得成长值', '1', '{"value":10}', '2019-02-19 17:00:45', '2019-04-26 11:36:54', '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_share_goods', '分享商城', '会员分享商城页面可获得的成长值', '0', '{"value":"","limit":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_comment_goods', '评论', '仅针对4~5星评论且评论字数大于30字的评论进行发放', '0', '{"value":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_follow_store', '关注店铺', '关注店铺可获得成长值，每个会员ID相同店铺仅第一次关注可进行获得', '1', '{"value":20,"limit":200}', '2019-02-19 17:00:45', '2019-04-26 11:36:54', '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_complete_information', '完善个人信息', '完善个人基本信息可获得成长值，每个会员仅可获得一次', '0', '{"value":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_bind_wechat', '绑定微信', '绑定微信成功获得成长值，每个会员仅可获得一次', '1', '{"value":20}', '2019-02-19 17:00:45', '2019-04-26 11:36:54', '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'growth_value_basic_rule', 'growth_value_basic_rule_add_delivery_address', '添加收货地址', '添加收货地址后获得成长值，每个会员仅可获得一次', '1', '{"value":30}', '2019-02-19 17:00:45', '2019-04-26 11:36:54', '0');

-- 积分获取-基础获取规则
-- insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_sign_in', '签到', '会员每日仅可完成一次签到', '0', '{"value":"","continue":"","extra":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_register', '注册', '会员注册成功后可获得积分数', '0', '{"value":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_share_goods', '分享商城', '会员分享商城页面可获得的积分数', '0', '{"value":"","limit":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_comment_goods', '评论', '仅针对4~5星评论且评论字数大于30字的评论进行发放', '0', '{"value":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_follow_store', '关注店铺', '关注店铺可获得积分，每个会员ID相同店铺仅第一次关注可进行获得', '0', '{"value":"","limit":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_complete_information', '完善个人信息', '完善个人基本信息可获得积分，每个会员仅可获得一次', '0', '{"value":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_bind_wechat', '绑定微信', '绑定微信成功获得积分，每个会员仅可获得一次', '0', '{"value":""}', '2019-03-26 17:00:45', null, '0');
insert into `system_config` ( `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) values ( 'points_basic_rule', 'points_basic_rule_add_delivery_address', '添加收货地址', '添加收货地址后获得积分，每个会员仅可获得一次', '0', '{"value":""}', '2019-03-26 17:00:45', null, '0');
-- 成长值设置权限接口地址修改
update authority set authority_url = '/boss/growthValueConfig/close' where authority_title = '关闭成长值开关';
update authority set authority_url = '/boss/growthValueConfig/open' where authority_title = '开启成长值开关';
update authority set authority_url = '/boss/growthValueConfig',authority_title = '查询成长值配置' where authority_title = '查询成长值开关配置';

-- 成长值设置权限
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( '8ab369076a66a28a016a67b2db480000', '4', '4434205840d911e99b6f0050568808e1', '保存成长值配置', null, '/boss/growthValueConfig', 'PUT', null, '5', '2019-04-29 14:05:36', '0');

-- ***************************************** setting end *********************************************






-- ***************************************** marketing begin *********************************************
delete from coupon_activity where activity_type = 4;
-- 修改活动表优惠券类型备注
ALTER TABLE `coupon_activity`
MODIFY COLUMN `activity_type` tinyint(4) NOT NULL COMMENT '优惠券类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券，4权益赠券' AFTER `end_time`;
-- ***************************************** marketing end *********************************************





-- ***************************************** order begin *********************************************
-- 退款流水增加实退积分
ALTER TABLE `refund_bill` ADD actual_return_points BIGINT(10) DEFAULT 0 NULL COMMENT '实退积分' AFTER `actual_return_price`;

-- 退款单增加应退积分
ALTER TABLE `refund_order` ADD `return_points` BIGINT(10) DEFAULT 0 NULL COMMENT '应退积分' AFTER `return_price`;

-- 修改成唯一索引,防止重复收款单
ALTER TABLE `receivable`
DROP INDEX `rece_pay_order_id`,
ADD UNIQUE INDEX `rece_pay_order_id`(`pay_order_id`) USING BTREE;

-- 修改成唯一索引,防止重复退款单
ALTER TABLE `refund_order`
ADD UNIQUE INDEX `refund_order_code_UNIQUE`(`return_order_code`) USING BTREE;
-- ***************************************** order end *********************************************

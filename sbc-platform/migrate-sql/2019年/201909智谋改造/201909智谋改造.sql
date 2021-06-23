-- ***************************************** customer begin *********************************************
ALTER TABLE `customer` ADD COLUMN `check_time` datetime(0) NULL COMMENT '账户的审核时间' AFTER `check_state`;
-- ***************************************** customer end ***********************************************


-- ***************************************** statistics begin *********************************************
DROP TABLE IF EXISTS `replay_consignee`;
CREATE TABLE `replay_consignee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tid` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `cda_id` varchar(32) DEFAULT NULL COMMENT '客户收货地址id',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省',
  `city_id` bigint(20) DEFAULT NULL COMMENT '市',
  `area_id` bigint(20) DEFAULT NULL COMMENT '区',
  `detail_address` varchar(256) DEFAULT NULL COMMENT '详细地址',
  `name` varchar(99) DEFAULT NULL COMMENT '收货人姓名',
  `phone` varchar(99) DEFAULT NULL COMMENT '收货人手机号',
  `expect_time` datetime DEFAULT NULL COMMENT '期望收货时间',
  `update_time` datetime DEFAULT NULL COMMENT '收货地址修改时间',
  PRIMARY KEY (`id`),
  KEY `tid` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=7020 DEFAULT CHARSET=utf8mb4 COMMENT='订单收货人信息';

CREATE TABLE `replay_customer` (
  `customer_id` varchar(32) NOT NULL COMMENT '会员标识UUID',
  `customer_level_id` bigint(10) DEFAULT NULL COMMENT '客户级别',
  `points_available` bigint(10) DEFAULT '0' COMMENT '可用积分',
  `points_used` bigint(10) DEFAULT '0' COMMENT '已用积分',
  `growth_value` bigint(10) DEFAULT '0' COMMENT '成长值',
  `customer_account` varchar(20) NOT NULL COMMENT '会员登录账号|手机号',
  `customer_password` varchar(200) NOT NULL COMMENT '会员登录密码',
  `safe_level` tinyint(4) DEFAULT NULL COMMENT '密码安全等级：20危险 40低、60中、80高',
  `customer_salt_val` varchar(200) DEFAULT NULL COMMENT '盐值,用于密码加密',
  `customer_type` tinyint(4) DEFAULT NULL COMMENT '客户类型,0:平台客户,1:商家客户',
  `check_state` tinyint(4) NOT NULL COMMENT '账户的审核状态 0:待审核 1:已审核通过 2:审核未通过',
  `check_time` datetime DEFAULT NULL COMMENT '账户的审核时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  `login_ip` varchar(32) DEFAULT '0.0.0.0' COMMENT '登录IP',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `login_error_time` int(11) DEFAULT NULL COMMENT '登录错误次数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间|注册时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  `login_lock_time` datetime DEFAULT NULL COMMENT '登录锁定时间',
  `customer_pay_password` varchar(200) DEFAULT NULL COMMENT '支付密码',
  `pay_error_time` int(11) DEFAULT NULL COMMENT '支付密码错误次数',
  `pay_lock_time` datetime DEFAULT NULL COMMENT '支付锁定时间',
  PRIMARY KEY (`customer_id`) USING BTREE,
  UNIQUE KEY `customer_id_UNIQUE` (`customer_id`) USING BTREE,
  KEY `check_time_index` (`check_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='客户信息表';

CREATE TABLE `replay_customer_detail` (
  `customer_detail_id` varchar(32) NOT NULL COMMENT '会员详细信息标识UUID',
  `customer_id` varchar(32) NOT NULL COMMENT '会员ID',
  `customer_name` varchar(128) DEFAULT NULL COMMENT '会员名称',
  `province_id` bigint(10) DEFAULT NULL COMMENT '省份',
  `city_id` bigint(10) DEFAULT NULL COMMENT '市',
  `area_id` bigint(10) DEFAULT NULL COMMENT '区',
  `customer_address` varchar(225) DEFAULT NULL COMMENT '详细街道地址',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '是否删除标志 0：否，1：是',
  `customer_status` tinyint(4) DEFAULT NULL COMMENT '账号状态 0：启用中  1：禁用中',
  `contact_name` varchar(128) DEFAULT NULL COMMENT '联系人名字',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `employee_id` varchar(32) DEFAULT NULL COMMENT '负责业务员',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  `reject_reason` varchar(256) DEFAULT NULL COMMENT '审核驳回原因',
  `forbid_reason` varchar(256) DEFAULT NULL COMMENT '禁用原因',
  `is_distributor` tinyint(2) DEFAULT '0' COMMENT '是否为分销员 0：否 1：是',
  PRIMARY KEY (`customer_detail_id`) USING BTREE,
  UNIQUE KEY `customer_detail_id_UNIQUE` (`customer_detail_id`) USING BTREE,
  KEY `customer_id_index` (`customer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='客户详细信息';

CREATE TABLE `replay_customer_level` (
  `customer_level_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '客户等级ID',
  `customer_level_name` varchar(45) NOT NULL COMMENT '客户等级名称',
  `customer_level_discount` decimal(5,2) NOT NULL COMMENT '客户等级折扣',
  `growth_value` bigint(10) DEFAULT NULL COMMENT '所需成长值',
  `rank_badge_img` varchar(255) DEFAULT NULL COMMENT '等级徽章',
  `is_defalt` tinyint(4) NOT NULL COMMENT '是否是默认等级  0：否，1：是',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`customer_level_id`) USING BTREE,
  UNIQUE KEY `customer_level_id_UNIQUE` (`customer_level_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='客户等级表';

CREATE TABLE `replay_employee` (
  `employee_id` varchar(32) NOT NULL COMMENT '员工信息ID',
  `employee_name` varchar(45) DEFAULT NULL COMMENT '员工姓名',
  `employee_mobile` varchar(20) DEFAULT NULL COMMENT '员工手机号',
  `role_id` bigint(10) DEFAULT NULL COMMENT '员工角色',
  `is_employee` tinyint(4) NOT NULL COMMENT '是否业务员 0：是   1：否',
  `account_name` varchar(45) NOT NULL COMMENT '账户名',
  `account_password` varchar(45) NOT NULL COMMENT '账户密码',
  `employee_salt_val` varchar(200) DEFAULT NULL COMMENT '盐值,用于密码加密',
  `account_state` tinyint(4) NOT NULL COMMENT '账户状态  0：启用   1：禁用',
  `account_disable_reason` varchar(255) DEFAULT NULL COMMENT '账号禁用原因',
  `third_id` varchar(32) DEFAULT NULL COMMENT '第三方店铺ID',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员编号',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `login_error_time` tinyint(4) DEFAULT NULL COMMENT '错误登录次数',
  `login_lock_time` datetime DEFAULT NULL COMMENT '登录锁定时间',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  `company_info_id` bigint(11) DEFAULT NULL COMMENT '公司信息ID',
  `is_master_account` tinyint(4) DEFAULT NULL COMMENT '是否主账号 0、否 1、是',
  `account_type` tinyint(1) DEFAULT NULL COMMENT '账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号',
  PRIMARY KEY (`employee_id`) USING BTREE,
  UNIQUE KEY `employee_id_UNIQUE` (`employee_id`) USING BTREE,
  UNIQUE KEY `emp_account_ind` (`account_name`,`account_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='员工信息表';

CREATE TABLE `replay_flow_day_user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `flow_day_id` varchar(50) DEFAULT NULL COMMENT 'flow_day表关联id',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id',
  `flow_date` date DEFAULT NULL COMMENT '对应日期',
  `user_type` tinyint(255) DEFAULT NULL COMMENT '用户类型：0:总访客用户，1：sku访客用户',
  PRIMARY KEY (`id`),
  KEY `flow_day_id_index` (`flow_day_id`) USING BTREE COMMENT 'flow_day关联表id索引'
) ENGINE=InnoDB AUTO_INCREMENT=168879 DEFAULT CHARSET=utf8mb4 COMMENT='每日流量统计表对应访客访客会员信息';

CREATE TABLE `replay_goods_brand` (
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
  PRIMARY KEY (`brand_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=303 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品品牌表';

CREATE TABLE `replay_goods_cate` (
  `cate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品分类主键',
  `cate_name` varchar(45) DEFAULT NULL COMMENT '分类名称',
  `cate_parent_id` bigint(20) DEFAULT NULL COMMENT '父分类ID',
  `cate_img` varchar(255) DEFAULT NULL COMMENT '分类图片',
  `cate_path` varchar(1000) NOT NULL COMMENT '分类层次路径,例1|01|001',
  `cate_grade` tinyint(4) NOT NULL COMMENT '分类层级',
  `cate_rate` decimal(8,2) DEFAULT NULL COMMENT '分类扣率',
  `is_parent_cate_rate` tinyint(4) DEFAULT NULL COMMENT '是否使用上级类目扣率 0 否  1 是',
  `growth_value_rate` decimal(8,2) DEFAULT NULL COMMENT '成长值获取比例',
  `is_parent_growth_value_rate` tinyint(4) DEFAULT '1' COMMENT '是否使用上级类目成长值获取比例 0 否  1 是',
  `pin_yin` varchar(45) DEFAULT NULL COMMENT '拼音',
  `s_pin_yin` varchar(45) DEFAULT NULL COMMENT '简拼',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(4) NOT NULL COMMENT '是否默认,0:否1:是',
  `points_rate` decimal(8,2) DEFAULT NULL COMMENT '积分获取比例',
  `is_parent_points_rate` tinyint(4) DEFAULT NULL COMMENT '是否使用上级类目积分获取比例 0 否  1 是',
  PRIMARY KEY (`cate_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=943 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品分类表';

CREATE TABLE `replay_goods_info` (
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
  `distribution_commission` decimal(11,2) DEFAULT NULL COMMENT '分销佣金',
  `distribution_sales_count` int(11) DEFAULT NULL COMMENT '分销销量',
  `distribution_goods_audit` tinyint(4) DEFAULT '0' COMMENT '分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销',
  `distribution_goods_audit_reason` varchar(100) DEFAULT NULL COMMENT '分销商品审核不通过或禁止分销原因',
  `cate_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品分类Id',
  `commission_rate` decimal(4,2) DEFAULT NULL COMMENT '佣金比例',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌Id',
  PRIMARY KEY (`goods_info_id`) USING BTREE,
  UNIQUE KEY `goods_info_id_UNIQUE` (`goods_info_id`) USING BTREE,
  KEY `del_flag_index` (`del_flag`) USING BTREE,
  KEY `goods_id_index` (`goods_id`) USING BTREE,
  KEY `audit_status_index` (`audit_status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='SKU货品表';

CREATE TABLE `replay_goods_info_spec_detail_rel` (
  `spec_detail_rel_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` varchar(32) NOT NULL COMMENT 'SPU编号',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SKU编号',
  `spec_detail_id` bigint(20) NOT NULL,
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `detail_name` varchar(45) DEFAULT NULL COMMENT ' 规格值名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`spec_detail_rel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='SKU与规格值关联表';

DROP TABLE IF EXISTS `replay_return_item`;
CREATE TABLE `replay_return_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rid` varchar(32) DEFAULT NULL COMMENT '退单id',
  `spu_id` varchar(32) DEFAULT NULL COMMENT 'spu_id',
  `spu_name` varchar(256) DEFAULT NULL COMMENT 'spu_name',
  `sku_id` varchar(32) DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(256) DEFAULT NULL COMMENT 'sku_name',
  `sku_no` varchar(32) DEFAULT NULL COMMENT 'sku_no',
  `spec_details` varchar(256) DEFAULT NULL COMMENT '规格信息',
  `price` decimal(11,2) DEFAULT NULL COMMENT '退货商品单价 = 商品原单价 - 商品优惠单价',
  `split_price` decimal(11,2) DEFAULT NULL COMMENT '平摊价格',
  `order_split_price` decimal(11,2) DEFAULT NULL COMMENT '订单平摊价格',
  `num` int(11) DEFAULT NULL COMMENT '申请退货数量',
  `pic` varchar(256) DEFAULT NULL COMMENT '退货商品图片路径',
  `unit` varchar(99) DEFAULT NULL COMMENT '单位',
  `can_return_num` int(11) DEFAULT NULL COMMENT '仍可退数量',
  `cate_id` bigint(20) DEFAULT NULL COMMENT '分类id',
  `cate_name` varchar(32) DEFAULT NULL COMMENT '分类名称',
  `brand` bigint(20) DEFAULT NULL COMMENT '商品品牌',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='退单项';

DROP TABLE IF EXISTS `replay_return_order`;
CREATE TABLE `replay_return_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rid` varchar(32) DEFAULT NULL COMMENT '退单号',
  `tid` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '客户信息 买家信息',
  `company_id` bigint(20) DEFAULT NULL COMMENT '商家id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺id',
  `apply_price` decimal(11,2) DEFAULT NULL COMMENT '申请金额',
  `total_price` decimal(11,2) DEFAULT NULL COMMENT '商品总金额',
  `actual_return_price` decimal(11,2) DEFAULT NULL COMMENT '实退金额，从退款流水中取的',
  `return_flow_state` int(11) DEFAULT NULL COMMENT '退货单状态',
  `refund_status` int(11) DEFAULT NULL COMMENT '退款单状态:0.待退款 1.拒绝退款 2.已退款 3 商家申请退款(待平台退款)',
  `return_type` int(11) DEFAULT NULL COMMENT '退单类型 0：退货 1：退款',
  `create_time` datetime DEFAULT NULL COMMENT '退单创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '退单完成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='退单信息';

CREATE TABLE `replay_sku_flow` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sku_id` varchar(50) DEFAULT NULL COMMENT '商品id',
  `company_id` varchar(50) DEFAULT NULL COMMENT '商家id',
  `pc_pv` bigint(20) DEFAULT NULL COMMENT 'pc端pv',
  `h5_pv` bigint(20) DEFAULT NULL COMMENT 'h5端pv',
  `app_pv` bigint(20) DEFAULT NULL COMMENT 'app端pv',
  `total_pv` bigint(20) DEFAULT NULL COMMENT '商城浏览pv',
  `pc_uv` bigint(20) DEFAULT NULL COMMENT 'pc端uv',
  `h5_uv` bigint(20) DEFAULT NULL COMMENT 'h5端uv',
  `app_uv` bigint(20) DEFAULT NULL COMMENT 'app端uv',
  `total_uv` bigint(20) DEFAULT NULL COMMENT '商城访客数uv',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `receive_time` datetime DEFAULT NULL COMMENT '接收时间',
  `sku_flow_date` date DEFAULT NULL COMMENT '商品流量统计日期',
  `sku_flow_month` varchar(255) DEFAULT NULL COMMENT '某月的商品流量统计',
  PRIMARY KEY (`id`),
  KEY `sku_id_index` (`sku_id`) USING BTREE,
  KEY `company_id_index` (`company_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22722 DEFAULT CHARSET=utf8mb4 COMMENT='每日商品流量统计';

CREATE TABLE `replay_sku_flow_user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sku_flow_id` varchar(50) DEFAULT NULL COMMENT 'sku_flow关联表id',
  `sku_id` varchar(50) DEFAULT NULL COMMENT '商品id',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id',
  `sku_flow_date` date DEFAULT NULL COMMENT '统计时间',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `receive_time` datetime DEFAULT NULL COMMENT '接收时间',
  PRIMARY KEY (`id`),
  KEY `sku_flow_id_index` (`sku_flow_id`) USING BTREE COMMENT 'sku_flow_id索引'
) ENGINE=InnoDB AUTO_INCREMENT=30477 DEFAULT CHARSET=utf8mb4 COMMENT='每日商品流量统计uv对应会员信息';

CREATE TABLE `replay_store` (
  `store_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '店铺主键',
  `company_info_id` bigint(11) DEFAULT NULL COMMENT '公司信息ID',
  `freight_template_type` tinyint(1) DEFAULT '0' COMMENT '使用的运费模板类别(0:店铺运费,1:单品运费)',
  `contract_start_date` datetime DEFAULT NULL COMMENT '签约开始日期',
  `contract_end_date` datetime DEFAULT NULL COMMENT '签约结束日期',
  `store_name` varchar(150) DEFAULT NULL COMMENT '店铺名称',
  `store_logo` varchar(255) DEFAULT NULL COMMENT '店铺logo',
  `store_sign` varchar(255) DEFAULT NULL COMMENT '店铺店招图片',
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '商家名称',
  `company_type` tinyint(4) DEFAULT NULL COMMENT '商家类型 0、平台自营 1、第三方商家',
  `contact_person` varchar(150) DEFAULT NULL COMMENT '联系人',
  `contact_mobile` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `contact_email` varchar(50) DEFAULT NULL COMMENT '联系邮箱',
  `province_id` bigint(10) DEFAULT NULL COMMENT '省',
  `city_id` bigint(10) DEFAULT NULL COMMENT '市',
  `area_id` bigint(10) DEFAULT NULL COMMENT '区',
  `address_detail` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `account_day` varchar(50) DEFAULT NULL COMMENT '结算日',
  `audit_state` tinyint(4) DEFAULT NULL COMMENT '审核状态 0、待审核 1、已审核 2、审核未通过',
  `audit_reason` varchar(255) DEFAULT NULL COMMENT '审核未通过原因',
  `store_closed_reason` varchar(255) DEFAULT NULL COMMENT '店铺关店原因',
  `store_state` tinyint(4) DEFAULT NULL COMMENT '店铺状态 0、开启 1、关店',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '是否删除 0 否  1 是',
  `apply_enter_time` datetime DEFAULT NULL COMMENT '申请入驻时间',
  `small_program_code` varchar(250) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '店铺小程序码',
  PRIMARY KEY (`store_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=123457389 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='店铺';

CREATE TABLE `replay_store_cate` (
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
  PRIMARY KEY (`store_cate_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=603 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='店铺商品分类';

CREATE TABLE `replay_store_cate_goods_rela` (
  `goods_id` varchar(32) NOT NULL COMMENT '商品标识',
  `store_cate_id` bigint(20) NOT NULL COMMENT '店铺分类标识',
  PRIMARY KEY (`goods_id`,`store_cate_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品店铺分类关联表(允许一个商品有多个店铺分类)';

CREATE TABLE `replay_store_customer_rela` (
  `id` varchar(40) NOT NULL COMMENT '主键UUID',
  `customer_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '会员标识',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺标识',
  `company_info_id` int(11) DEFAULT NULL COMMENT '商家标识',
  `store_level_id` bigint(10) DEFAULT NULL COMMENT '客户等级标识',
  `employee_id` varchar(32) DEFAULT NULL COMMENT '负责业务员标识',
  `customer_type` tinyint(4) DEFAULT NULL COMMENT '客户类型,0:店铺关联的客户,1:店铺发展的客户',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `customer_id_index` (`customer_id`) USING BTREE,
  KEY `company_info_id_index` (`company_info_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='店铺客户关系表';

CREATE TABLE `replay_store_level` (
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
) ENGINE=InnoDB AUTO_INCREMENT=12866 DEFAULT CHARSET=utf8mb4 COMMENT='商户客户等级表';

DROP TABLE IF EXISTS `replay_trade`;
CREATE TABLE `replay_trade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tid` varchar(32) DEFAULT NULL COMMENT '订单号',
  `total_price` decimal(11,2) DEFAULT NULL COMMENT '订单应付金额',
  `goods_price` decimal(11,2) DEFAULT NULL COMMENT '商品总金额',
  `total_pay_cash` decimal(11,2) DEFAULT NULL COMMENT '订单实际支付金额',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `level_id` bigint(20) DEFAULT NULL COMMENT '客户级别',
  `company_id` bigint(20) DEFAULT NULL COMMENT '商家id',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '订单支付时间',
  `flow_state` int(11) DEFAULT NULL COMMENT '订单流程状态:0 创建订单 7已完成 8 已作废',
  `pay_state` int(11) DEFAULT NULL COMMENT '订单支付状态: 0:未支付 1 待确认 2 已支付',
  `order_source` int(11) DEFAULT NULL COMMENT '订单来源--区分h5,pc,app,小程序,代客下单',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tid_index` (`tid`) USING BTREE,
  KEY `pay_time_index` (`pay_time`) USING BTREE,
  KEY `customer_id_index` (`customer_id`) USING BTREE,
  KEY `pay_state` (`pay_state`) USING BTREE,
  KEY `create_time_index` (`create_time`) USING BTREE,
  KEY `company_id_index` (`company_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=629840 DEFAULT CHARSET=utf8mb4 COMMENT='订单';

DROP TABLE IF EXISTS `replay_trade_item`;
CREATE TABLE `replay_trade_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tid` varchar(32) DEFAULT NULL COMMENT '订单id',
  `admin_id` varchar(32) DEFAULT NULL COMMENT '商品所属的userId storeId?',
  `store_id` varchar(32) DEFAULT NULL COMMENT '店铺id',
  `supplier_code` varchar(32) DEFAULT NULL COMMENT '商家编码',
  `spu_id` varchar(32) DEFAULT NULL COMMENT 'spuId',
  `spu_name` varchar(256) DEFAULT NULL COMMENT 'spuName',
  `sku_id` varchar(32) DEFAULT NULL COMMENT 'skuId',
  `sku_name` varchar(256) DEFAULT NULL COMMENT 'skuName',
  `sku_no` varchar(32) DEFAULT NULL COMMENT 'skuNo',
  `cate_id` bigint(20) DEFAULT NULL COMMENT '分类',
  `cate_name` varchar(256) DEFAULT NULL COMMENT '分类名称',
  `brand` bigint(20) DEFAULT NULL COMMENT '商品品牌',
  `num` bigint(20) DEFAULT NULL COMMENT '购买数量',
  `unit` varchar(32) DEFAULT NULL COMMENT '单位',
  `price` decimal(11,2) DEFAULT NULL COMMENT '成交价格',
  `original_price` decimal(11,2) DEFAULT NULL COMMENT '商品原价 - 建议零售价',
  `level_price` decimal(11,2) DEFAULT NULL COMMENT '商品价格 - 会员价 & 阶梯设价',
  `cost` decimal(11,2) DEFAULT NULL COMMENT '成本价',
  `bn` varchar(32) DEFAULT NULL COMMENT '货物id',
  `can_return_num` int(11) DEFAULT NULL COMMENT '可退数量',
  PRIMARY KEY (`id`),
  KEY `tid_index` (`tid`) USING BTREE,
  KEY `sku_id_index` (`sku_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=630144 DEFAULT CHARSET=utf8mb4 COMMENT='订单项';

CREATE TABLE `flow_seven` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '流量统计汇总时间',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `pv` bigint(20) DEFAULT NULL COMMENT '访问量',
  `goods_uv` bigint(20) DEFAULT NULL COMMENT '商品访问人数',
  `goods_pv` bigint(255) DEFAULT NULL COMMENT '商品访问量',
  `company_id` varchar(50) DEFAULT NULL COMMENT '店铺id',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33307 DEFAULT CHARSET=utf8mb4 COMMENT='流量统计最近七天结果汇总表';

CREATE TABLE `flow_thirty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '流量统计汇总时间',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `pv` bigint(20) DEFAULT NULL COMMENT '访问量',
  `goods_uv` bigint(20) DEFAULT NULL COMMENT '商品访问人数',
  `goods_pv` bigint(255) DEFAULT NULL COMMENT '商品访问量',
  `company_id` varchar(50) DEFAULT NULL COMMENT '店铺id',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30279 DEFAULT CHARSET=utf8mb4 COMMENT='流量统计最近三十天结果汇总表';

CREATE TABLE `flow_week` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '流量统计汇总时间',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `pv` bigint(20) DEFAULT NULL COMMENT '访问量',
  `goods_uv` bigint(20) DEFAULT NULL COMMENT '商品访问人数',
  `goods_pv` bigint(255) DEFAULT NULL COMMENT '商品访问量',
  `company_id` varchar(50) DEFAULT NULL COMMENT '店铺id',
  `month` varchar(50) DEFAULT NULL COMMENT '统计月份',
  `type` tinyint(4) DEFAULT NULL COMMENT '统计类型，0：：最近30天按周统计，1：最近6个月按周统计',
  `week_start_date` date DEFAULT NULL COMMENT '按周统计，周开始日期',
  `week_end_date` date DEFAULT NULL COMMENT '按周统计，周结束日期',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98547 DEFAULT CHARSET=utf8mb4 COMMENT='流量统计按周统计结果汇总表';

CREATE TABLE `flow_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '流量统计汇总时间',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `pv` bigint(20) DEFAULT NULL COMMENT '访问量',
  `goods_uv` bigint(20) DEFAULT NULL COMMENT '商品访问人数',
  `goods_pv` bigint(255) DEFAULT NULL COMMENT '商品访问量',
  `company_id` varchar(50) DEFAULT NULL COMMENT '店铺id',
  `month` varchar(50) DEFAULT NULL COMMENT '统计月份',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19522 DEFAULT CHARSET=utf8mb4 COMMENT='流量统计最近六个月按月统计结果汇总表';

DROP TABLE IF EXISTS `trade_day`;
CREATE TABLE `trade_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '日期',
  `order_num` bigint(20) DEFAULT NULL COMMENT '下单笔数',
  `order_user_num` bigint(20) DEFAULT NULL COMMENT '下单人数',
  `order_money` decimal(20,2) DEFAULT '0.00' COMMENT '下单金额',
  `pay_num` bigint(20) DEFAULT NULL COMMENT '付款订单数',
  `pay_user_num` bigint(20) DEFAULT NULL COMMENT '付款人数',
  `pay_money` decimal(20,2) DEFAULT '0.00' COMMENT '付款金额',
  `order_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '下单转化率',
  `pay_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '付款转化率',
  `all_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '全店转化率',
  `user_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '客单价',
  `order_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '笔单价',
  `refund_num` bigint(20) DEFAULT NULL COMMENT '退单笔数',
  `refund_user_num` bigint(20) DEFAULT NULL COMMENT '退单人数',
  `refund_money` decimal(20,2) DEFAULT '0.00' COMMENT '退单金额',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `company_id` bigint(50) DEFAULT NULL COMMENT '店铺标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12406 DEFAULT CHARSET=utf8 COMMENT='当天交易统计表';

DROP TABLE IF EXISTS `trade_month`;
CREATE TABLE `trade_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '日期',
  `month` varchar(50) DEFAULT NULL COMMENT '统计月份',
  `order_num` bigint(20) DEFAULT NULL COMMENT '下单笔数',
  `order_user_num` bigint(20) DEFAULT NULL COMMENT '下单人数',
  `order_money` decimal(20,2) DEFAULT '0.00' COMMENT '下单金额',
  `pay_num` bigint(20) DEFAULT NULL COMMENT '付款订单数',
  `pay_user_num` bigint(20) DEFAULT NULL COMMENT '付款人数',
  `pay_money` decimal(20,2) DEFAULT '0.00' COMMENT '付款金额',
  `order_conversion` decimal(5,2) DEFAULT '0.00' COMMENT '下单转化率',
  `pay_conversion` decimal(5,2) DEFAULT '0.00' COMMENT '付款转化率',
  `all_conversion` decimal(5,2) DEFAULT '0.00' COMMENT '全店转化率',
  `user_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '客单价',
  `order_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '笔单价',
  `refund_num` bigint(20) DEFAULT NULL COMMENT '退单笔数',
  `refund_user_num` bigint(20) DEFAULT NULL COMMENT '退单人数',
  `refund_money` decimal(20,2) DEFAULT '0.00' COMMENT '退单金额',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `company_id` bigint(50) DEFAULT NULL COMMENT '店铺标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8 COMMENT='自然月交易统计表';

DROP TABLE IF EXISTS `trade_seven`;
CREATE TABLE `trade_seven` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '日期',
  `order_num` bigint(20) DEFAULT NULL COMMENT '下单笔数',
  `order_user_num` bigint(20) DEFAULT NULL COMMENT '下单人数',
  `order_money` decimal(20,2) DEFAULT '0.00' COMMENT '下单金额',
  `pay_num` bigint(20) DEFAULT NULL COMMENT '付款订单数',
  `pay_user_num` bigint(20) DEFAULT NULL COMMENT '付款人数',
  `pay_money` decimal(20,2) DEFAULT '0.00' COMMENT '付款金额',
  `order_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '下单转化率',
  `pay_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '付款转化率',
  `all_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '全店转化率',
  `user_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '客单价',
  `order_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '笔单价',
  `refund_num` bigint(20) DEFAULT NULL COMMENT '退单笔数',
  `refund_user_num` bigint(20) DEFAULT NULL COMMENT '退单人数',
  `refund_money` decimal(20,2) DEFAULT '0.00' COMMENT '退单金额',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `company_id` bigint(50) DEFAULT NULL COMMENT '店铺标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8 COMMENT='最近七天交易统计表';

DROP TABLE IF EXISTS `trade_thirty`;
CREATE TABLE `trade_thirty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '日期',
  `order_num` bigint(20) DEFAULT NULL COMMENT '下单笔数',
  `order_user_num` bigint(20) DEFAULT NULL COMMENT '下单人数',
  `order_money` decimal(20,2) DEFAULT '0.00' COMMENT '下单金额',
  `pay_num` bigint(20) DEFAULT NULL COMMENT '付款订单数',
  `pay_user_num` bigint(20) DEFAULT NULL COMMENT '付款人数',
  `pay_money` decimal(20,2) DEFAULT '0.00' COMMENT '付款金额',
  `order_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '下单转化率',
  `pay_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '付款转化率',
  `all_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '全店转化率',
  `user_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '客单价',
  `order_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '笔单价',
  `refund_num` bigint(20) DEFAULT NULL COMMENT '退单笔数',
  `refund_user_num` bigint(20) DEFAULT NULL COMMENT '退单人数',
  `refund_money` decimal(20,2) DEFAULT '0.00' COMMENT '退单金额',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `company_id` bigint(50) DEFAULT NULL COMMENT '店铺标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=utf8 COMMENT='最近三十天交易统计表';

DROP TABLE IF EXISTS `trade_week`;
CREATE TABLE `trade_week` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `date` date DEFAULT NULL COMMENT '日期',
  `month` varchar(50) DEFAULT NULL COMMENT '统计月份',
  `type` int(11) DEFAULT NULL COMMENT '统计类型，0：：最近30天按周统计，1：最近6个月按周统计',
  `week_start_date` date DEFAULT NULL COMMENT '按周统计，周开始日期',
  `week_end_date` date DEFAULT NULL COMMENT '按周统计，周结束日期',
  `order_num` bigint(20) DEFAULT NULL COMMENT '下单笔数',
  `order_user_num` bigint(20) DEFAULT NULL COMMENT '下单人数',
  `order_money` decimal(20,2) DEFAULT '0.00' COMMENT '下单金额',
  `pay_num` bigint(20) DEFAULT NULL COMMENT '付款订单数',
  `pay_user_num` bigint(20) DEFAULT NULL COMMENT '付款人数',
  `pay_money` decimal(20,2) DEFAULT '0.00' COMMENT '付款金额',
  `order_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '下单转化率',
  `pay_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '付款转化率',
  `all_conversion` decimal(20,2) DEFAULT '0.00' COMMENT '全店转化率',
  `user_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '客单价',
  `order_per_price` decimal(20,2) DEFAULT '0.00' COMMENT '笔单价',
  `refund_num` bigint(20) DEFAULT NULL COMMENT '退单笔数',
  `refund_user_num` bigint(20) DEFAULT NULL COMMENT '退单人数',
  `refund_money` decimal(20,2) DEFAULT '0.00' COMMENT '退单金额',
  `uv` bigint(20) DEFAULT NULL COMMENT '访问人数',
  `company_id` bigint(50) DEFAULT NULL COMMENT '店铺标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1616 DEFAULT CHARSET=utf8 COMMENT='周交易统计表';

CREATE TABLE `goods_total_ratio_day` (
  `ID` varchar(50) NOT NULL COMMENT '由店铺标识+统计日期',
  `STAT_DATE` date NOT NULL COMMENT '统计日期',
  `TOTAL_UV` int(11) NOT NULL COMMENT 'uv',
  `CUSTOMER_COUNT` int(11) NOT NULL COMMENT '访问过商品的下单人数',
  `SHOP_ID` varchar(50) NOT NULL COMMENT '店铺id',
  `RATIO` decimal(11,2) NOT NULL COMMENT '商品详情页转化率',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`),
  KEY `ind_goods_total_1` (`STAT_DATE`),
  KEY `ind_goods_total_2` (`SHOP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日商品概览';

CREATE TABLE `goods_total_ratio_month` (
  `ID` varchar(50) NOT NULL COMMENT '由店铺标识+统计日期',
  `STAT_MONTH` int(6) NOT NULL COMMENT '统计日期',
  `TOTAL_UV` int(11) NOT NULL COMMENT 'uv',
  `CUSTOMER_COUNT` int(11) NOT NULL COMMENT '访问过商品的下单人数',
  `SHOP_ID` varchar(50) NOT NULL COMMENT '店铺id',
  `RATIO` decimal(11,2) NOT NULL COMMENT '商品详情页转化率',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`),
  KEY `ind_goods_total_1` (`STAT_MONTH`),
  KEY `ind_goods_total_2` (`SHOP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日商品概览';

CREATE TABLE `goods_total_ratio_recent_seven` (
  `SHOP_ID` varchar(50) NOT NULL COMMENT '店铺id',
  `TOTAL_UV` int(11) NOT NULL COMMENT 'uv',
  `CUSTOMER_COUNT` int(11) NOT NULL COMMENT '访问过商品的下单人数',
  `RATIO` decimal(11,2) NOT NULL COMMENT '商品详情页转化率',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`SHOP_ID`) USING BTREE,
  KEY `ind_goods_total_2` (`SHOP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日商品概览';

CREATE TABLE `goods_total_ratio_recent_thirty` (
  `SHOP_ID` varchar(50) NOT NULL COMMENT '店铺id',
  `TOTAL_UV` int(11) NOT NULL COMMENT 'uv',
  `CUSTOMER_COUNT` int(11) NOT NULL COMMENT '访问过商品的下单人数',
  `RATIO` decimal(11,2) NOT NULL COMMENT '商品详情页转化率',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`SHOP_ID`) USING BTREE,
  KEY `ind_goods_total_2` (`SHOP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日商品概览';

ALTER TABLE `goods_day`
MODIFY COLUMN `PAY_CONVERSION` decimal(5, 2) NOT NULL DEFAULT 0 COMMENT '付款转化率';

ALTER TABLE `goods_month`
MODIFY COLUMN `PAY_CONVERSION` decimal(5, 2) NOT NULL DEFAULT 0 COMMENT '付款转化率';

ALTER TABLE `goods_recent_seven`
MODIFY COLUMN `PAY_CONVERSION` decimal(5, 2) NOT NULL DEFAULT 0 COMMENT '付款转化率';

ALTER TABLE `goods_recent_thirty`
MODIFY COLUMN `PAY_CONVERSION` decimal(5, 2) NOT NULL DEFAULT 0 COMMENT '付款转化率';

####################兼容历史数据，需要全部表全部迁移完之后执行##################################################################
## 更新客户和店铺的绑定的时间
update replay_store_customer_rela a join customer_and_level b on(a.customer_id=b.customer_id and a.store_id = b.store_id) set a.create_time = b.bind_time;
# 更新客户表的check_time
update replay_customer a join customer b on(a.customer_id= b.id) set a.check_time=b.check_date

-- ***************************************** statistics end ***********************************************

-- ***************************************** xxl-job begin *********************************************

INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '流量统计定时任务--最近7天、30天', '2019-09-06 09:50:08', '2019-09-24 13:54:29', '吕振伟', '', 'FIRST', 'reportFlowDataGenerate', '2,3', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-06 09:50:08', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '客户统计定时任务--昨天、最近7天、30天', '2019-09-23 19:44:57', '2019-09-24 13:55:22', '吕振伟', '', 'FIRST', 'reportCustomerGenerate', '1,2,3', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-23 19:44:57', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 */5 * * * ? ', '客户统计定时任务--当天', '2019-09-23 19:46:34', '2019-09-24 13:55:45', '吕振伟', '', 'FIRST', 'reportCustomerGenerate', '0', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-23 19:46:34', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 1 * ?', '客户统计定时任务--按月', '2019-09-23 19:47:34', '2019-09-24 13:56:32', '吕振伟', '', 'FIRST', 'reportCustomerGenerate', '4', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-23 19:47:34', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '业务员业绩报表--日结', '2019-09-24 10:20:56', '2019-09-24 14:15:41', '张领科', '', 'FIRST', 'employeeReportScheduledGenerate', '1,2,3', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 10:20:56', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0/5 * * * ?', '业务员统计获客报表--当天', '2019-09-24 10:23:30', '2019-09-26 14:12:20', '张浩', '', 'FIRST', 'ReplayStoreCustomerRelaGenerateToday', '0', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 10:23:30', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 */5 * * * ?', '业务员业绩报表--今日', '2019-09-24 10:23:56', '2019-09-24 14:15:29', '张领科', '', 'FIRST', 'employeeReportScheduledGenerate', '0', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 10:23:56', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '业务员统计获客报表--昨天', '2019-09-24 10:24:35', '2019-10-18 10:54:44', '张浩', '', 'FIRST', 'ReplayStoreCustomerRelaGenerate', '1', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 10:24:35', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 1 * ?	', '业务员业绩报表--月结', '2019-09-24 10:28:56', '2019-09-24 14:15:17', '张领科', '', 'FIRST', 'employeeReportScheduledGenerate', '4', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 10:28:56', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 1 * ?', '流量统计--按月统计', '2019-09-24 13:57:35', '2019-09-24 13:57:35', '吕振伟', '', 'FIRST', 'reportFlowDataGenerate', '4', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 13:57:35', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 */5 * * * ?', '交易统计--今日', '2019-09-24 14:17:16', '2019-09-24 14:17:16', '张领科', '', 'FIRST', 'tradeReportScheduledGenerateData', '0', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 14:17:16', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '交易统计--日结', '2019-09-24 14:18:12', '2019-09-24 14:18:12', '张领科', '', 'FIRST', 'tradeReportScheduledGenerateData', '1,2,3', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 14:18:12', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 1 * ?', '交易统计--月结', '2019-09-24 14:18:40', '2019-09-24 14:18:40', '张领科', '', 'FIRST', 'tradeReportScheduledGenerateData', '4', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 14:18:40', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '21 */5 * * * ?', '商品统计--今日', '2019-09-24 16:15:33', '2019-09-26 10:54:39', '张高磊', '', 'FIRST', 'goodsReportGenerate', '0', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 16:15:33', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 11 1 * * ?', '商品统计--昨日、7、30', '2019-09-24 16:17:33', '2019-09-26 16:53:35', '张高磊', '', 'FIRST', 'goodsReportGenerate', '1,2,3', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2019-09-24 16:17:33', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 2 1 * ?', '商品统计--月统计', '2019-09-25 16:53:48', '2019-09-25 16:53:48', '张高磊', '', 'FIRST', 'goodsReportGenerate', '4', 'SERIAL_EXECUTION', 0, 1, 'BEAN', '', 'GLUE代码初始化', '2019-09-25 16:53:48', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '业务员统计获客报表--最近7天', '2019-10-18 10:51:09', '2019-10-18 10:54:25', '张浩', '', 'FIRST', 'ReplayStoreCustomerRelaGenerate', '2', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-10-18 10:51:09', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '业务员统计获客报表--最近30天', '2019-10-18 10:53:39', '2019-10-18 10:54:20', '张浩', '', 'FIRST', 'ReplayStoreCustomerRelaGenerate', '3', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-10-18 10:53:39', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 1 * ?', '业务员统计获客报表--月结', '2019-10-18 10:53:57', '2019-10-18 10:54:15', '张浩', '', 'FIRST', 'ReplayStoreCustomerRelaGenerate', '4', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-10-18 10:53:57', '');
DELETE FROM `xxl_job_qrtz_trigger_info` WHERE `xxl_job_qrtz_trigger_info`.`executor_handler` = 'reportTaskHandler';
-- ***************************************** xxl-job end *********************************************

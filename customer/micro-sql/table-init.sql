-- 数据库：sbc-customer
SET FOREIGN_KEY_CHECKS=0;


--客户的银行账号
DROP TABLE IF EXISTS `customer_account`;

CREATE TABLE `customer_account` (
  `customer_account_id` varchar(32) NOT NULL COMMENT '客户银行账号ID',
  `customer_id` varchar(32) NOT NULL COMMENT '会员标识UUID',
  `customer_account_name` varchar(128) NOT NULL COMMENT '账户名字',
  `customer_account_no` varchar(30) NOT NULL COMMENT '银行账号',
  `bank_name` varchar(45) DEFAULT NULL COMMENT '银行名称',
  `customer_bank_name` varchar(128) NOT NULL COMMENT '开户行',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`customer_account_id`),
  UNIQUE KEY `customer_account_id_UNIQUE` (`customer_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户的银行账号';


--客户收货地址表
DROP TABLE IF EXISTS `customer_delivery_address`;

CREATE TABLE `customer_delivery_address` (
  `delivery_address_id` varchar(32) NOT NULL COMMENT '收货地址ID',
  `customer_id` varchar(32) DEFAULT NULL,
  `consignee_name` varchar(128) DEFAULT NULL COMMENT '收货人',
  `consignee_number` varchar(20) DEFAULT NULL COMMENT '收货人手机号码',
  `province_id` bigint(10) DEFAULT NULL COMMENT '省份',
  `city_id` bigint(10) DEFAULT NULL COMMENT '市',
  `area_id` bigint(10) DEFAULT NULL COMMENT '区',
  `delivery_address` varchar(225) DEFAULT NULL COMMENT '详细街道地址',
  `is_defalt_address` tinyint(4) DEFAULT NULL COMMENT '是否是默认地址',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`delivery_address_id`),
  UNIQUE KEY `delivery_address_id_UNIQUE` (`delivery_address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户收货地址表';

--公司信息
DROP TABLE IF EXISTS `company_info`;

CREATE TABLE `company_info` (
  `company_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司信息ID',
  `company_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '公司名称',
  `back_ID_card` varchar(1024) DEFAULT NULL COMMENT '法人身份证反面',
  `province_id` bigint(10) DEFAULT NULL COMMENT '省',
  `city_id` bigint(10) DEFAULT NULL COMMENT '市',
  `area_id` bigint(10) DEFAULT NULL COMMENT '区',
  `detail_address` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `contact_name` text COMMENT '联系人',
  `contact_phone` text COMMENT '联系方式',
  `copyright` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '版权信息',
  `company_descript` text COMMENT '公司简介',
  `operator` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `company_type` tinyint(4) DEFAULT NULL COMMENT '商家类型 0、平台自营 1、第三方商家',
  `is_account_checked` tinyint(4) DEFAULT NULL COMMENT '账户是否全部收到打款 0、否 1、是',
  `social_credit_code` varchar(50) DEFAULT NULL COMMENT '社会信用代码',
  `address` varchar(255) DEFAULT NULL COMMENT '住所',
  `legal_representative` varchar(150) DEFAULT NULL COMMENT '法定代表人',
  `registered_capital` decimal(12,2) DEFAULT NULL COMMENT '注册资本',
  `found_date` datetime DEFAULT NULL COMMENT ' 成立日期',
  `business_term_start` datetime DEFAULT NULL COMMENT '营业期限自',
  `business_term_end` datetime DEFAULT NULL COMMENT '营业期限至',
  `business_scope` varchar(1024) DEFAULT NULL COMMENT '经营范围',
  `business_licence` varchar(1024) DEFAULT NULL COMMENT '营业执照副本电子版',
  `front_ID_card` varchar(1024) DEFAULT NULL COMMENT '法人身份证正面',
  `company_code` varchar(32) DEFAULT NULL COMMENT '商家编号',
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '商家名称',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '是否删除 0 否  1 是',
  `remit_affirm` tinyint(1) DEFAULT '0' COMMENT '是否通过打款确认(0:否,1:是)',
  `apply_enter_time` datetime DEFAULT NULL COMMENT '入驻时间(第一次审核通过时间)',
  PRIMARY KEY (`company_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司信息';


--客户详细信息
DROP TABLE IF EXISTS `customer_detail`;

CREATE TABLE `customer_detail` (
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
  PRIMARY KEY (`customer_detail_id`),
  UNIQUE KEY `customer_detail_id_UNIQUE` (`customer_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户详细信息';

--客户财务邮箱配置表
DROP TABLE IF EXISTS `customer_email`;

CREATE TABLE `customer_email` (
  `customer_email_id` varchar(32) NOT NULL COMMENT '用户财务邮箱主键Id（UUID）',
  `customer_id` varchar(32) NOT NULL COMMENT '邮箱所属客户Id',
  `email_address` varchar(32) NOT NULL COMMENT '邮箱地址',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`customer_email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户财务邮箱配置表';


--员工信息表
DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
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
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `employee_id_UNIQUE` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工信息表';

--角色表
DROP TABLE IF EXISTS `role_info`;

CREATE TABLE `role_info` (
  `role_info_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(45) NOT NULL COMMENT '角色名称',
  `role_description` varchar(45) DEFAULT NULL COMMENT '角色描述',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  `company_info_id` bigint(11) DEFAULT NULL COMMENT '公司信息ID',
  PRIMARY KEY (`role_info_id`),
  UNIQUE KEY `role_info_id_UNIQUE` (`role_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8 COMMENT='角色表';

--商品的店铺收藏
DROP TABLE IF EXISTS `store_customer_follow`;

CREATE TABLE `store_customer_follow` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `store_id` bigint(11) NOT NULL COMMENT '商品Id',
  `company_info_id` bigint(11) NOT NULL COMMENT '商家ID',
  `follow_time` datetime NOT NULL COMMENT '收藏时间',
  PRIMARY KEY (`follow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品的店铺收藏';

--客户开票信息
DROP TABLE IF EXISTS `customer_invoice`;

CREATE TABLE `customer_invoice` (
  `customer_invoice_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '增专资质ID',
  `customer_id` varchar(32) NOT NULL COMMENT '会员ID',
  `company_name` varchar(225) DEFAULT NULL COMMENT '单位全称',
  `taxpayer_number` varchar(45) DEFAULT NULL COMMENT '纳税人识别号',
  `company_phone` varchar(20) DEFAULT NULL COMMENT '单位电话',
  `company_address` varchar(225) DEFAULT NULL COMMENT '单位地址',
  `bank_no` varchar(45) DEFAULT NULL COMMENT '银行基本户号',
  `bank_name` varchar(128) DEFAULT NULL COMMENT '开户行',
  `taxpayer_identification_img` varchar(200) DEFAULT NULL COMMENT '一般纳税人认证资格复印件',
  `check_state` tinyint(4) DEFAULT NULL COMMENT '增票资质审核状态  0:待审核 1:已审核通过 2:审核未通过',
  `reject_reason` varchar(100) DEFAULT NULL COMMENT '审核未通过原因',
  `business_license_img` varchar(200) DEFAULT NULL COMMENT '营业执照复印件',
  `invalid_flag` tinyint(4) DEFAULT NULL COMMENT '增专资质是否作废 0：否 1：是',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`customer_invoice_id`),
  UNIQUE KEY `customer_invoice_id_UNIQUE` (`customer_invoice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户开票信息';

--客户等级表
DROP TABLE IF EXISTS `customer_level`;

CREATE TABLE `customer_level` (
  `customer_level_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '客户等级ID',
  `customer_level_name` varchar(45) NOT NULL COMMENT '客户等级名称',
  `customer_level_discount` decimal(5,2) NOT NULL COMMENT '客户等级折扣',
  `is_defalt` tinyint(4) NOT NULL COMMENT '是否是默认等级  0：否，1：是',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`customer_level_id`),
  UNIQUE KEY `customer_level_id_UNIQUE` (`customer_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户等级表';

--客户信息表
DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `customer_id` varchar(32) NOT NULL COMMENT '会员标识UUID',
  `customer_level_id` bigint(10) DEFAULT NULL COMMENT '客户级别',
  `customer_account` varchar(20) NOT NULL COMMENT '会员登录账号|手机号',
  `customer_password` varchar(200) NOT NULL COMMENT '会员登录密码',
  `safe_level` tinyint(4) DEFAULT NULL COMMENT '密码安全等级：20危险 40低、60中、80高',
  `customer_salt_val` varchar(200) DEFAULT NULL COMMENT '盐值,用于密码加密',
  `customer_type` tinyint(4) DEFAULT NULL COMMENT '客户类型,0:平台客户,1:商家客户',
  `check_state` tinyint(4) NOT NULL COMMENT '账户的审核状态 0:待审核 1:已审核通过 2:审核未通过',
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
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `customer_id_UNIQUE` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息表';


--第三方关系表
DROP TABLE IF EXISTS `third_login_relation`;

CREATE TABLE `third_login_relation` (
  `third_login_id` varchar(32) NOT NULL COMMENT '第三方登录主键',
  `third_login_uid` varchar(255) DEFAULT NULL COMMENT '第三方关系关联(union)Id',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '用户Id',
  `third_login_type` tinyint(4) DEFAULT NULL COMMENT '第三方类型 0:wechat',
  `third_login_open_id` varchar(50) DEFAULT NULL COMMENT '微信授权openId, 该字段只有微信才有, 由于微信登录使用的是unionId, 但是微信模板消息发送需要使用openId, 所以需要union_id, 所以union_id和open_id单独存放',
  `binding_time` datetime DEFAULT NULL COMMENT '绑定时间',
  UNIQUE KEY `third_login_id` (`third_login_id`),
  UNIQUE KEY `third_login_relation_third_login_uid_uindex` (`third_login_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方关系表';

--店铺
DROP TABLE IF EXISTS `store`;

CREATE TABLE `store` (
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
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='店铺';


--店铺客户关系表
DROP TABLE IF EXISTS `store_customer_rela`;

CREATE TABLE `store_customer_rela` (
  `id` varchar(40) NOT NULL COMMENT '主键UUID',
  `customer_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '会员标识',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺标识',
  `company_info_id` int(11) DEFAULT NULL COMMENT '商家标识',
  `customer_level_id` bigint(10) DEFAULT NULL COMMENT '客户等级标识',
  `employee_id` varchar(32) DEFAULT NULL COMMENT '负责业务员标识',
  `customer_type` tinyint(4) DEFAULT NULL COMMENT '客户类型,0:平台客户,1:商家客户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺客户关系表';


--店铺等级折扣表
DROP TABLE IF EXISTS `store_customer_level_rela`;

CREATE TABLE `store_customer_level_rela` (
  `id` varchar(40) NOT NULL COMMENT '主键UUID',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺标识',
  `company_info_id` int(11) DEFAULT NULL COMMENT '商家标识',
  `customer_level_id` bigint(10) DEFAULT NULL COMMENT '客户等级标识',
  `discount_rate` decimal(8,2) DEFAULT NULL COMMENT '折扣率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺等级折扣表';


SET FOREIGN_KEY_CHECKS=1;
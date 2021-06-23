-- 增加登录锁定时间
ALTER TABLE b2b.customer ADD login_lock_time DATETIME COMMENT '登录锁定时间';

-- 系统设置修改字段长度
ALTER TABLE b2b.base_config MODIFY pc_logo TEXT COMMENT 'PC商城logo';
ALTER TABLE b2b.base_config MODIFY pc_banner TEXT COMMENT 'PC商城banner,最多可添加5个,多个图片间以"|"隔开';
ALTER TABLE b2b.base_config MODIFY mobile_banner TEXT COMMENT '移动商城banner,最多可添加5个,多个图片间以"|"隔开';

--删除没有用到的表
DROP TABLE menu_info;

DROP TABLE message_config;

drop TABLE privilege_info;

DROP TABLE role_privilege_rel;

DROP TABLE purchase;

DROP TABLE return_logistics;

DROP TABLE return_order;

DROP TABLE return_order_goods;

DROP TABLE return_order_image;

DROP TABLE return_order_log;

DROP TABLE spec;

DROP TABLE spec_detail;


-- 将品牌设为可为空
ALTER TABLE `goods` MODIFY COLUMN `brand_id`  bigint(20) NULL COMMENT '品牌Id' AFTER `cate_id`;

-- 采购单新增收藏时间
ALTER TABLE `goods_customer_follow`
MODIFY COLUMN `create_time`  datetime NULL COMMENT '创建时间',
ADD COLUMN `follow_time`  datetime NULL COMMENT '收藏时间';

--SKU单独设置价格相关
ALTER TABLE `goods_info`
ADD COLUMN `custom_flag`  tinyint(4) NULL COMMENT '按客户单独定价,0:否1:是',
ADD COLUMN `level_discount_flag`  tinyint(4) NULL COMMENT '叠加客户等级折扣，0:否1:是';

--订单开票收货地址字段长度修改
ALTER TABLE ORDER_INVOICE
  MODIFY COLUMN INVOICE_ADDRESS VARCHAR(225);

--增加pc商城首页banner
ALTER TABLE `base_config` ADD COLUMN `pc_main_banner` TEXT NULL COMMENT 'PC商城首页banner,最多可添加5个,多个图片间以"|"隔开';

--公司emoj
ALTER TABLE company_info CHARSET=utf8mb4 COMMENT='公司信息';
ALTER TABLE company_info MODIFY `company_descript` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '公司简介';

ALTER TABLE company_info MODIFY `company_descript` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '公司简介';
ALTER TABLE company_info  CHARSET=utf8mb4 COMMENT='公司信息';


-- 公司信息
ALTER TABLE company_info MODIFY `contact_name` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '联系人';
ALTER TABLE company_info MODIFY `contact_phone` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '联系方式';

-- 商品信息，标题可以输emoji表情
ALTER TABLE `goods`
MODIFY COLUMN `goods_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品标题' AFTER `brand_id`;
ALTER TABLE `goods_info`
MODIFY COLUMN `goods_info_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品名称' AFTER `goods_id`;

-- 商品信息，编码可以输emoji表情
ALTER TABLE `goods`
MODIFY COLUMN `goods_no`  varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'SPU编码' AFTER `goods_subtitle`;

ALTER TABLE `goods_info`
MODIFY COLUMN `goods_info_no`  varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'SKU编码' AFTER `goods_info_name`;

-- 收款，退款明细表新增字段标识支付渠道
ALTER TABLE `receivable`
  ADD COLUMN `pay_channel` varchar(45) NULL COMMENT '在支付渠道';
ALTER TABLE `refund_bill`
  ADD COLUMN `pay_channel` varchar(45) NULL COMMENT '在支付渠道';

ALTER TABLE `receivable`
  ADD COLUMN `pay_channel_id` int(11) NULL COMMENT '在支付渠道id';
ALTER TABLE `refund_bill`
  ADD COLUMN `pay_channel_id` int(11) NULL COMMENT '在支付渠道id';

--收款单据表的pay_order_id增加唯一约束
ALTER TABLE receivable ADD CONSTRAINT receivable_pay_order_id_UNIQUE UNIQUE (pay_order_id);


-- 换third_id字段名为company_info_id dyt
ALTER TABLE `goods`
CHANGE COLUMN `third_id` `company_info_id`  bigint(11) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `store_id`;
ALTER TABLE `goods`
DROP COLUMN `is_third`;
ALTER TABLE `goods_info`
CHANGE COLUMN `third_id` `company_info_id`  bigint(11) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `customer_id`;
ALTER TABLE `goods_customer_follow`
CHANGE COLUMN `third_id` `company_info_id`  bigint(11) NOT NULL COMMENT '公司信息ID' AFTER `create_time`;


-- system_config默认值设置
INSERT INTO s2b.system_config (id , config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag)
VALUES (9,'order_setting', 'order_setting_auto_receive', '订单设置自动收货', NULL, 0, '{"day": 10}', sysdate(), sysdate(),0);
INSERT INTO s2b.system_config (id,config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag)
VALUES (10, 'order_setting', 'order_setting_refund_auto_audit','退单自动审核', NULL, 0, '{"day": 3}',sysdate(), sysdate(), 0);
INSERT INTO s2b.system_config (id,config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag)
VALUES (11, 'order_setting', 'order_setting_refund_auto_receive','退单自动收货', NULL, 0, '{"day": 10}', sysdate(), sysdate(),0);
INSERT INTO s2b.system_config (id,config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag)
VALUES (12, 'order_setting', 'order_setting_apply_refund','允许申请退单', NULL, 1, '{"day": 9999}', sysdate(), sysdate(),0);

-- 商家收款账号
CREATE TABLE `company_account` (
  `account_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_info_id` bigint(11) DEFAULT NULL COMMENT '公司信息ID',
  `is_received` tinyint(4) DEFAULT '0' COMMENT '是否收到平台首次打款 0、否 1、是',
  `is_default_account` tinyint(4) DEFAULT '0' COMMENT '是否主账号 0、否 1、是',
  `account_name` varchar(150) DEFAULT NULL COMMENT '账户名称',
  `bank_name` varchar(150) DEFAULT NULL COMMENT '开户银行',
  `bank_branch` varchar(150) DEFAULT NULL COMMENT '支行',
  `bank_no` varchar(45) DEFAULT NULL COMMENT '银行账号',
  `bank_status` tinyint(4) DEFAULT NULL COMMENT '银行状态 0: 启用 1:禁用',
  `third_id` varchar(45) DEFAULT NULL COMMENT '第三方店铺ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除状态 0：否 1：是',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `remit_price` decimal(20,2) DEFAULT NULL COMMENT '打款金额',
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_id_UNIQUE` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8 COMMENT='商家线下账户';

-- company_account 新增银行编号字段
ALTER TABLE `company_account`
ADD COLUMN bank_code VARCHAR (20) DEFAULT NULL COMMENT '银行编号' AFTER `bank_no`

-- 为商品店铺标识、商家标识不允许为空
ALTER TABLE `goods`
MODIFY COLUMN `store_id`  bigint(20) NOT NULL COMMENT '店铺标识',
MODIFY COLUMN `company_info_id`  bigint(11) NOT NULL COMMENT '公司信息ID';

ALTER TABLE `goods_info`
  MODIFY COLUMN `company_info_id`  bigint(11) NOT NULL COMMENT '公司信息ID',
  MODIFY COLUMN `store_id`  bigint(20) NOT NULL COMMENT '店铺Id';

-- 商家id
ALTER TABLE refund_order ADD supplier_id BIGINT NULL COMMENT '商家id';


-- --------------------------------商品类目属性 start----------------------------------
-- ----------------------------
-- Table structure for goods_prop
-- ----------------------------
DROP TABLE IF EXISTS `goods_prop`;
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
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';

-- ----------------------------
-- Table structure for goods_prop_detail
-- ----------------------------
DROP TABLE IF EXISTS `goods_prop_detail`;
CREATE TABLE `goods_prop_detail` (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性值id',
  `prop_id` bigint(20) DEFAULT NULL COMMENT '属性外键',
  `detail_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '属性值名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0:未删除1:已删除',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8mb4 COMMENT='商品属性值表';

-- ----------------------------
-- Table structure for goods_prop_detail_rel
-- ----------------------------
DROP TABLE IF EXISTS `goods_prop_detail_rel`;
CREATE TABLE `goods_prop_detail_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联表主键',
  `goods_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'SPU标识',
  `detail_id` bigint(20) NOT NULL COMMENT '属性值id',
  `prop_id` bigint(20) NOT NULL COMMENT '属性id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COMMENT='SPU与属性值关联表';
-- --------------------------------商品类目属性 end----------------------------------

-- 营销
ALTER TABLE `Purchase` ADD INDEX idx_purchase_customerid_goodsinfoid (customer_id, goods_info_id);

ALTER TABLE `marketing_full_gift_detail` ADD INDEX idx_gift_detail_marketingid_levelid (marketing_id, gift_level_id);

-- 增加业务员的代客下单接口
INSERT INTO `authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`)
VALUES ('694', '3', '25', '账号搜索客户列表', '', '/customer/customerAccount/list', 'POST', '', '1', NULL, '0');

-- 金额字段统一位数20位，保留两位小数
ALTER TABLE pay_order      MODIFY COLUMN pay_order_price DECIMAL(20,2);
ALTER TABLE reconciliation MODIFY COLUMN amount DECIMAL(20,2);
ALTER TABLE reconciliation MODIFY COLUMN discounts DECIMAL(20,2);
ALTER TABLE refund_order   MODIFY COLUMN return_price DECIMAL(20,2);

-- 商品扩展设价类型
ALTER TABLE `goods`
  MODIFY COLUMN `price_type`  tinyint(4) NULL DEFAULT NULL COMMENT '设价类型,0:按客户1:按订货量2:按市场价';

-- 商品SKU扩展增加独立设价标识
ALTER TABLE `goods_info`
  ADD COLUMN `alone_flag`  tinyint(4) NOT NULL DEFAULT 0;

-- job
update scheduler_job set job_class = 'com.wanmi.sbc.job.SettlementAnalyseJob' where job_class = 'com.wanmi.b2b.job.SettlementAnalyseJob';

-- 增加财务对账和结算的导出权限
INSERT INTO `authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('869', '3', '256', '结算列表导出', NULL, '/finance/bill/exportIncome/*', 'GET', NULL, '1', NULL, '0');
INSERT INTO `authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('868', '3', '255', '对账列表导出', NULL, '/finance/settlement/export/*', 'GET', NULL, '1', NULL, '0');

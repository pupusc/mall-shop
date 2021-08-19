ALTER TABLE `sbc-goods`.`goods`
    ADD COLUMN `supply_price` decimal(20, 2) NULL COMMENT '供货价' AFTER `market_price`,
    ADD COLUMN `retail_price` decimal(20, 2) NULL COMMENT '建议零售价' AFTER `supply_price`,
    ADD COLUMN `goods_type` tinyint(1) NULL DEFAULT 0 COMMENT '商品类型，0：实体商品，1：虚拟商品' AFTER `retail_price`;


ALTER TABLE `sbc-goods`.`goods_info`
    ADD COLUMN `supply_price` decimal(20, 2) NULL COMMENT '供货价' AFTER `market_price`,
    ADD COLUMN `retail_price` decimal(20, 2) NULL COMMENT '建议零售价' AFTER `supply_price`;


use `sbc-customer`;
-- company_info增加商家类型字段
ALTER TABLE `sbc-customer`.`company_info`
ADD COLUMN `store_type` tinyint(4) NULL DEFAULT 1 COMMENT '商家类型0供应商，1商家' AFTER `apply_enter_time`;
-- store增加商家类型字段
ALTER TABLE `sbc-customer`.`store`
ADD COLUMN `store_type` tinyint(4) NULL DEFAULT 1 COMMENT '商家类型0供应商，1商家' AFTER `small_program_code`;
-- goods增加商品来源字段
use `sbc-goods`;
ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN `goods_source` tinyint(4) NULL DEFAULT 1 COMMENT '商品来源，0供应商，1商家' AFTER `added_time`;

-- 招商页设置，新增商家相关字段，原来的字段作为品牌商的配置
ALTER TABLE `sbc-setting`.`business_config`
ADD COLUMN `supplier_banner` text COLLATE utf8mb4_unicode_ci COMMENT '商家banner' AFTER `business_enter`,
ADD COLUMN `supplier_custom` text COLLATE utf8mb4_unicode_ci COMMENT '商家自定义' AFTER `supplier_banner`,
ADD COLUMN `supplier_register` text COLLATE utf8mb4_unicode_ci  COMMENT '商家注册协议' AFTER `supplier_custom`,
ADD COLUMN `supplier_enter` text COLLATE utf8mb4_unicode_ci COMMENT '商家入驻协议' AFTER `supplier_register`;

-- goods注释替换
ALTER TABLE `sbc-goods`.`goods`
    MODIFY COLUMN `goods_source` tinyint(4) NULL DEFAULT 1 COMMENT '商品来源，0供应商，1商家' AFTER `added_time`,
    MODIFY COLUMN `supplier_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家名称' AFTER `company_info_id`;

-- marketing注释替换
ALTER TABLE `sbc-marketing`.`distribution_record`
    MODIFY COLUMN `company_id` bigint(20) NULL DEFAULT NULL COMMENT '商家Id' AFTER `store_id`;

-- customer注释替换
ALTER TABLE `sbc-customer`.`company_info`
MODIFY COLUMN `company_type` tinyint(4) DEFAULT NULL COMMENT '商家类型 0、平台自营 1、第三方商家' AFTER `update_time`,
MODIFY COLUMN `company_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商家编号' AFTER `front_ID_card`,
MODIFY COLUMN `supplier_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商家名称' AFTER `company_code`,
MODIFY COLUMN `store_type` tinyint(4) DEFAULT NULL COMMENT '商家类型0供应商，1商家' AFTER `apply_enter_time`;

ALTER TABLE `sbc-customer`.`customer`
MODIFY COLUMN `customer_type` tinyint(4) DEFAULT NULL COMMENT '客户类型,0:平台客户,1:商家客户' AFTER `customer_salt_val`;

ALTER TABLE `sbc-customer`.`employee`
MODIFY COLUMN `account_type` tinyint(1) DEFAULT NULL COMMENT '账号类型 0 b2b账号 1 s2b平台端账号 2 商家端账号 3供应商端账号' AFTER `is_master_account`;

ALTER TABLE `sbc-customer`.`store`
MODIFY COLUMN `supplier_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商家名称' AFTER `store_sign`,
MODIFY COLUMN `company_type` tinyint(4) DEFAULT NULL COMMENT '商家类型 0、平台自营 1、第三方商家' AFTER `supplier_name`,
MODIFY COLUMN `store_type` tinyint(4) DEFAULT NULL COMMENT '商家类型0供应商，1商家' AFTER `small_program_code`;

ALTER TABLE `sbc-customer`.`store_customer_rela`
MODIFY COLUMN `company_info_id` int(11) DEFAULT NULL COMMENT '商家标识' AFTER `store_id`;


-- account注释替换
ALTER TABLE `sbc-account`.`company_account` COMMENT = '商家线下账户';

ALTER TABLE `sbc-account`.`invoice_project`
    MODIFY COLUMN `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家id' AFTER `operate_person`;

ALTER TABLE `sbc-account`.`invoice_project_switch`
    MODIFY COLUMN `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家id' AFTER `invoice_project_switch_id`;

ALTER TABLE `sbc-account`.`reconciliation`
    MODIFY COLUMN `supplier_id` int(11) NOT NULL COMMENT '商家id' AFTER `id`,
    MODIFY COLUMN `supplier_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商家名称' AFTER `supplier_id`;


-- statistics注释替换
ALTER TABLE `s2b_statistics`.`customer_and_level`
    MODIFY COLUMN `customer_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关系类型(0:平台绑定客户,1:商家客户)' AFTER `employee_id`,
    MODIFY COLUMN `bind_time` date DEFAULT NULL COMMENT '商家和平台的绑定时间' AFTER `customer_type`;

ALTER TABLE `s2b_statistics`.`export_data_request`
    MODIFY COLUMN `COMPANY_INFO_ID` bigint(11) DEFAULT NULL COMMENT '商家标识' AFTER `USER_ID`;

ALTER TABLE `s2b_statistics`.`replay_return_order`
    MODIFY COLUMN `company_id` bigint(20) DEFAULT NULL COMMENT '商家id' AFTER `customer_id`,
    MODIFY COLUMN `refund_status` int(11) DEFAULT NULL COMMENT '退款单状态:0.待退款 1.拒绝退款 2.已退款 3 商家申请退款(待平台退款)' AFTER `return_flow_state`;

ALTER TABLE `s2b_statistics`.`replay_sku_flow`
    MODIFY COLUMN `company_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商家id' AFTER `sku_id`;

ALTER TABLE `s2b_statistics`.`replay_store`
    MODIFY COLUMN `supplier_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商家名称' AFTER `store_sign`,
    MODIFY COLUMN `company_type` tinyint(4) DEFAULT NULL COMMENT '商家类型 0、平台自营 1、第三方商家' AFTER `supplier_name`;

ALTER TABLE `s2b_statistics`.`replay_trade_item`
    MODIFY COLUMN `supplier_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商家编码' AFTER `store_id`;

ALTER TABLE `s2b_statistics`.`store`
    MODIFY COLUMN `supplier_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商家名称' AFTER `store_name`;

ALTER TABLE `s2b_statistics`.`store_cate`
    MODIFY COLUMN `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家Id' AFTER `store_id`;

-- order注释替换
ALTER TABLE `sbc-order`.`order_invoice`
MODIFY COLUMN `company_info_id` bigint(20) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `order_no`;
ALTER TABLE `sbc-order`.`pay_order`
MODIFY COLUMN `company_info_id` bigint(11) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `pay_order_price`;
ALTER TABLE `sbc-order`.`purchase`
MODIFY COLUMN `company_info_id` bigint(20) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `goods_info_id`;
ALTER TABLE `sbc-order`.`purchase_action`
MODIFY COLUMN `company_info_id` bigint(20) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `goods_info_id`;
ALTER TABLE `sbc-order`.`refund_order`
MODIFY COLUMN `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `create_time`;
-- setting注释替换
ALTER TABLE `sbc-setting`.`base_config`
MODIFY COLUMN `supplier_website` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家后台登录网址' AFTER `pc_title`;
ALTER TABLE `sbc-setting`.`business_config`
MODIFY COLUMN `supplier_banner` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商家banner' AFTER `business_enter`,
MODIFY COLUMN `supplier_custom` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商家自定义' AFTER `supplier_banner`,
MODIFY COLUMN `supplier_register` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商家注册协议' AFTER `supplier_custom`,
MODIFY COLUMN `supplier_enter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商家入驻协议' AFTER `supplier_register`;
ALTER TABLE `sbc-setting`.`company_info`
MODIFY COLUMN `company_type` tinyint(4) NULL DEFAULT NULL COMMENT '商家类型 0、平台自营 1、第三方商家' AFTER `update_time`,
MODIFY COLUMN `company_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家编号' AFTER `front_ID_card`,
MODIFY COLUMN `supplier_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家名称' AFTER `company_code`;
ALTER TABLE `sbc-setting`.`store_express_company_rela`
MODIFY COLUMN `company_info_id` int(11) NULL DEFAULT NULL COMMENT '商家标识' AFTER `store_id`;
ALTER TABLE `sbc-setting`.`store_image`
MODIFY COLUMN `company_info_id` int(11) NULL DEFAULT NULL COMMENT '商家标识' AFTER `store_id`;
ALTER TABLE `sbc-setting`.`store_resource`
MODIFY COLUMN `company_info_id` int(11) NULL DEFAULT NULL COMMENT '商家标识' AFTER `store_id`;
ALTER TABLE `sbc-setting`.`store_resource_cate`
MODIFY COLUMN `company_info_id` int(11) NULL DEFAULT NULL COMMENT '商家标识' AFTER `store_id`;
ALTER TABLE `sbc-setting`.`menu_info`
MODIFY COLUMN `system_type_cd` tinyint(2) NULL DEFAULT NULL COMMENT '系统类别(3:商家,4:平台,5:品牌商,6:供应商)' AFTER `menu_id`;
ALTER TABLE `sbc-setting`.`function_info`
MODIFY COLUMN `system_type_cd` tinyint(2) NULL DEFAULT NULL COMMENT '系统类别(3:商家,4:平台,5:品牌商,6:供应商)' AFTER `function_id`;
ALTER TABLE `sbc-setting`.`authority`
MODIFY COLUMN `system_type_cd` tinyint(2) NULL DEFAULT NULL COMMENT '系统类别(3:商家,4:平台,5:品牌商,6:供应商)' AFTER `authority_id`;






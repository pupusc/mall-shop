ALTER TABLE `sbc-setting`.`system_points_config`
ADD COLUMN `points_usage_flag`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '使用方式 0:订单抵扣,1:商品抵扣' AFTER `remark`,
ADD COLUMN `switch_usage_time`  datetime NULL COMMENT '最近使用方式的切换时间' AFTER `update_time`;

CREATE TABLE `sbc-setting`.`search_rank` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `search_keyword` varchar(255) NOT NULL COMMENT '关键字',
  `search_rank` tinyint(4) NOT NULL COMMENT '搜索排行',
  `search_count` bigint(10) NOT NULL COMMENT '搜索量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN `buy_point`  bigint(10) NULL COMMENT '购买积分' AFTER `enterprise_goods_audit_reason`;

ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN `sham_sales_num`  bigint(11) NULL DEFAULT 0 COMMENT '注水销量',
ADD COLUMN `sort_no`  bigint(11) NULL DEFAULT 0 COMMENT '排序号' ,
ADD COLUMN `single_spec_flag`  tinyint(4) NULL DEFAULT 1 COMMENT '0:多规格1:单规格';

-- 初始化商品的单规格标识
UPDATE `sbc-goods`.goods g SET g.single_spec_flag = (SELECT if(count(1)>1,0,1) FROM `sbc-goods`.goods_spec_detail s WHERE s.goods_id = g.goods_id AND s.del_flag = 0)where g.single_spec_flag = 1;

-- 打包一口价-规则表
CREATE TABLE `sbc-marketing`.`marketing_buyout_price_level` (
  `reduction_level_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '打包级别Id',
  `marketing_id` bigint(20) NOT NULL COMMENT '营销Id',
  `full_amount` decimal(12,2) DEFAULT NULL COMMENT '满金额',
  `choice_count` bigint(5) DEFAULT NULL COMMENT '任选数量',
  PRIMARY KEY (`reduction_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='营销打包关联表';

ALTER TABLE `sbc-marketing`.`marketing_buyout_price_level`
ADD INDEX(`marketing_id`);

ALTER TABLE `sbc-customer`.`store`
ADD COLUMN `store_pinyin_name`  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺拼音名称' AFTER `store_type`,
ADD COLUMN `supplier_pinyin_name`  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商拼音名称' AFTER `store_pinyin_name`;

-- 热门搜索词表
CREATE TABLE `sbc-setting`.`popular_search_terms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `popular_search_keyword` varchar(50) NOT NULL COMMENT '热门搜索词',
  `related_landing_page` varchar(200) DEFAULT NULL COMMENT '关联落地页',
  `sort_number` bigint(20) DEFAULT NULL COMMENT '排序号',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标记  0：正常，1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='热门搜索词表';

-- 预置搜索词表
CREATE TABLE `sbc-setting`.`preset_search_terms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `preset_search_keyword` varchar(50) NOT NULL COMMENT '预置搜索词字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='预置搜索词表';

-- 搜索词
CREATE TABLE `sbc-setting`.`search_associational_word` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `search_terms` varchar(50) NOT NULL COMMENT '搜索词',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标记  0：正常，1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='搜索词表';

-- 联想长尾词
CREATE TABLE `sbc-setting`.`association_long_tail_word` (
  `association_long_tail_word_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `associational_word` varchar(50) DEFAULT NULL COMMENT '联想词',
  `long_tail_word` text COMMENT '长尾词',
  `search_associational_word_Id` bigint(20) DEFAULT NULL COMMENT '联想词Id',
  `sort_number` bigint(20) DEFAULT NULL COMMENT '排序号',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标记  0：正常，1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`association_long_tail_word_id`),
  KEY `index_search_associational_word_Id` (`search_associational_word_Id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='联想长尾词表';


-- 弹窗表
 CREATE TABLE `sbc-setting`.`popup` (
  `popup_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '弹窗Id',
  `popup_name` varchar(40) NOT NULL COMMENT '弹窗名称',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `popup_url` varchar(200) NOT NULL COMMENT '弹窗url',
  `application_page_ame` varchar(250) NOT NULL COMMENT '应用页面:商城首页：shoppingIndex,购物车:shoppingCart,个人中心：personalCenter,个人中心：personalCenter,会员中心：memberCenter,拼团频道：groupChannel,秒杀频道：seckillChannel,领劵中心：securitiesCenter,积分商城: integralMall',
  `jump_page` text NOT NULL COMMENT '跳转页面',
  `launch_frequency` varchar(200) NOT NULL COMMENT '投放频次',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标记  0：正常，1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  `is_pause` tinyint(4) NOT NULL COMMENT '是否暂停（1：暂停，0：正常）',
  PRIMARY KEY (`popup_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='弹窗表';

-- 弹窗关联应用页排序表
CREATE TABLE `sbc-setting`.`application_page` (
  `application_page_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应用页Id',
  `application_page_name` varchar(40) NOT NULL COMMENT '应用页面:商城首页：shoppingIndex,购物车:shoppingCart,个人中心：personalCenter,个人中心：personalCenter,会员中心：memberCenter,拼团频道：groupChannel,秒杀频道：seckillChannel,领劵中心：securitiesCenter,积分商城: integralMall',
  `popup_id` bigint(20) NOT NULL COMMENT '弹窗id',
  `sort_number` bigint(20) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`application_page_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='弹窗关联应用页排序表';


-- 限售商品的配置表
CREATE TABLE `sbc-goods`.`goods_restricted_sale` (
  `restricted_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '限售主键',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺ID',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT '货品的skuId',
  `restricted_type` tinyint(4) DEFAULT NULL COMMENT '限售方式 0: 按订单 1：按会员',
  `restricted_pre_person_flag` tinyint(4) DEFAULT NULL COMMENT '是否每人限售标识 ',
  `restricted_pre_order_flag` tinyint(4) DEFAULT NULL COMMENT '是否每单限售的标识',
  `restricted_assign_flag` tinyint(4) DEFAULT NULL COMMENT '是否指定会员限售的标识',
  `person_restricted_type` tinyint(4) DEFAULT NULL COMMENT '个人限售的方式(  0:终生限售  1:周期限售)',
  `assign_person_restricted_type` tinyint(4) DEFAULT NULL COMMENT '限售指定会员的类型 0:会员等级   1:指定会员',
  `person_restricted_cycle` tinyint(4) DEFAULT NULL COMMENT '个人限售的周期 (0:周   1:月  2:年)',
  `restricted_num` bigint(20) DEFAULT NULL COMMENT '限售数量',
  `start_sale_num` bigint(20) DEFAULT NULL COMMENT '起售数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`restricted_id`) USING BTREE,
  KEY `INDEX_GOODS_INFO_ID` (`goods_info_id`) USING BTREE COMMENT '增加goods_info的索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='限售商品的配置表';



-- 对指定会员的限售关系表
CREATE TABLE `sbc-goods`.`goods_restricted_customer_rela` (
  `rela_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '限售会员的关系主键',
  `restricted_id` bigint(20) DEFAULT NULL COMMENT '限售主键',
  `assign_person_restricted_type` tinyint(4) DEFAULT NULL COMMENT '特定会员的限售类型 0: 会员等级  1：指定会员',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员ID',
  `customer_level_id` bigint(20) DEFAULT NULL COMMENT '会员等级ID',
  PRIMARY KEY (`rela_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对指定会员的限售关系表';

-- 限售记录表
CREATE TABLE `sbc-goods`.`restricted_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员的主键',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT '货品主键',
  `purchase_num` bigint(20) DEFAULT NULL COMMENT '购买的数量',
  `restricted_cycle_type` tinyint(4) DEFAULT NULL COMMENT '周期类型 0:天 1:周 2:月 3:年 4:终生 5:订单',
  `start_date` date DEFAULT NULL COMMENT '开始时间',
  `end_date` date DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='限售记录表';

ALTER TABLE `sbc-goods`.`restricted_record`
ADD INDEX(`customer_id`),
ADD INDEX(`goods_info_id`);

-- 首页静态化信息
CREATE TABLE `sbc-setting`.`magic_page` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `html_string` TEXT NOT NULL COMMENT '魔方生成的页面静态html内容',
  `del_flag` TINYINT(4) NULL COMMENT '是否删除 0 否  1 是',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '修改时间',
  `operate_person` VARCHAR(45) NULL COMMENT '操作人',
  PRIMARY KEY (`id`))
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '魔方建站生成的静态dom表';

-- 调整快递公司
UPDATE `sbc-setting`.`express_company` SET `express_name`='百世汇通' WHERE express_code='huitongkuaidi';


-- 修改system_operation_log表中的op_context字段类型
ALTER TABLE system_operation_log MODIFY COLUMN op_context text CHARACTER SET utf8 DEFAULT NULL COMMENT '操作内容'

-- 修改sbc-setting popular_search_terms表中的related_landing_page字段类型
ALTER TABLE popular_search_terms MODIFY COLUMN related_landing_page text CHARACTER SET utf8 DEFAULT NULL COMMENT '关联落地页'

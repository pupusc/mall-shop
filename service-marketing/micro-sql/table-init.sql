-- 数据库：sbc-marketing
CREATE TABLE `marketing` (
  `marketing_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '促销Id',
  `marketing_name` varchar(40) NOT NULL COMMENT '促销名称',
  `marketing_type` tinyint(4) NOT NULL COMMENT '促销类型 0：满减 1:满折 2:满赠',
  `sub_type` tinyint(4) NOT NULL COMMENT '促销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `scope_type` tinyint(4) NOT NULL COMMENT '参加促销类型 0：全部货品 1：货品 2：品牌 3：分类',
  `join_level` varchar(500) NOT NULL COMMENT '参加会员  -1:全部客户 0:全部等级 other:其他等级',
  `is_boss` tinyint(4) NOT NULL COMMENT '是否是平台，1：boss，0：商家',
  `store_id` bigint(20) DEFAULT NULL COMMENT '商铺Id  0：boss,  other:其他商家',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标记  0：正常，1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  `is_pause` tinyint(4) NOT NULL COMMENT '是否暂停（1：暂停，0：正常）',
  PRIMARY KEY (`marketing_id`)
) ENGINE=InnoDB AUTO_INCREMENT=455 DEFAULT CHARSET=utf8 COMMENT='营销表';

CREATE TABLE `marketing_scope` (
  `marketing_scope_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '货品与促销规则表Id',
  `marketing_id` bigint(20) NOT NULL COMMENT '促销Id',
  `scope_id` varchar(32) NOT NULL COMMENT '促销范围Id',
  PRIMARY KEY (`marketing_scope_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1082 DEFAULT CHARSET=utf8 COMMENT='营销和商品关联表';

CREATE TABLE `coupon_activity` (
  `activity_id` varchar(32) NOT NULL COMMENT '优惠券活动id',
  `activity_name` varchar(50) NOT NULL COMMENT '优惠券活动名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `activity_type` tinyint(4) NOT NULL COMMENT '优惠券类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券',
  `receive_type` tinyint(4) DEFAULT '0' COMMENT '领取类型，0 每人限领次数不限，1 每人限领N次',
  `receive_count` int(11) DEFAULT NULL COMMENT '优惠券被使用后可再次领取的次数，每次仅限领取1张',
  `terminals` varchar(10) DEFAULT NULL COMMENT '生效终端，逗号分隔 0全部,1.PC,2.移动端,3.APP',
  `join_level` varchar(500) NOT NULL COMMENT '参加会员  -1:全部客户 0:全部等级 other:其他等级',
  `store_id` bigint(20) DEFAULT '0' COMMENT '商户id',
  `platform_flag` tinyint(4) DEFAULT '0' COMMENT '是否平台 1平台 0店铺',
  `pause_flag` tinyint(4) DEFAULT '0' COMMENT '是否暂停 0进行 1暂停',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  `left_group_num` int(11) DEFAULT NULL COMMENT '剩余优惠券组数',
  `activity_title` varchar(50) DEFAULT NULL COMMENT '参与成功通知标题',
  `activity_desc` varchar(50) DEFAULT NULL COMMENT '参与成功通知描述',
  PRIMARY KEY (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券活动表';

CREATE TABLE `coupon_activity_config` (
  `activity_config_id` varchar(32) NOT NULL COMMENT '优惠券活动配置表id',
  `activity_id` varchar(32) NOT NULL COMMENT '活动id',
  `coupon_id` varchar(32) NOT NULL COMMENT '优惠券id',
  `total_count` int(11) NOT NULL COMMENT '优惠券总张数',
  `has_left` tinyint(4) DEFAULT NULL COMMENT '是否有剩余，1是，2否',
  PRIMARY KEY (`activity_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券活动配置表';

CREATE TABLE `coupon_cate` (
  `coupon_cate_id` varchar(32) NOT NULL COMMENT '优惠券分类Id',
  `coupon_cate_name` varchar(20) NOT NULL COMMENT '优惠券分类名称',
  `cate_sort` tinyint(4) DEFAULT NULL COMMENT '排序',
  `only_platform_flag` tinyint(4) NOT NULL COMMENT '是否平台专用 0：否，1：是',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`coupon_cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券分类表';

CREATE TABLE `coupon_cate_rela` (
  `cate_rela_id` varchar(32) NOT NULL COMMENT '优惠券活动关联配置表id',
  `coupon_id` varchar(32) NOT NULL COMMENT '优惠券id',
  `cate_id` varchar(32) NOT NULL COMMENT '分类id',
  `platform_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否平台优惠券 1平台 0店铺',
  PRIMARY KEY (`cate_rela_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券活动关联配置表';

CREATE TABLE `coupon_code` (
  `coupon_code_id` varchar(32) NOT NULL COMMENT '优惠券码id',
  `coupon_code` varchar(50) NOT NULL COMMENT '券码 生成规则 10位（数字+大写英文）',
  `coupon_id` varchar(32) NOT NULL COMMENT '优惠券id',
  `activity_id` varchar(32) NOT NULL COMMENT '优惠券活动id',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '领取人id,同时表示领取状态',
  `use_status` tinyint(4) DEFAULT '0' COMMENT '使用状态,0(未使用)，1(使用)',
  `acquire_time` datetime DEFAULT NULL COMMENT '获得优惠券时间',
  `use_date` datetime DEFAULT NULL COMMENT '使用时间',
  `order_code` varchar(45) DEFAULT NULL COMMENT '使用的订单号',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`coupon_code_id`),
  KEY `idx_qm_coupon_code_coupon_id` (`coupon_id`),
  KEY `idx_qm_coupon_code_activity_id` (`activity_id`),
  KEY `code_index` (`coupon_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券券码表';

CREATE TABLE `coupon_info` (
  `coupon_id` varchar(32) NOT NULL COMMENT '优惠券主键Id',
  `coupon_name` varchar(20) NOT NULL COMMENT '优惠券名称',
  `range_day_type` tinyint(4) NOT NULL COMMENT '起止时间类型 0：按起止时间，1：按N天有效',
  `start_time` datetime DEFAULT NULL COMMENT '优惠券开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '优惠券结束时间',
  `effective_days` int(11) DEFAULT NULL COMMENT '有效天数',
  `fullbuy_price` decimal(10,2) DEFAULT NULL COMMENT '购满多少钱',
  `fullbuy_type` tinyint(4) NOT NULL COMMENT '购满类型 0：无门槛，1：满N元可使用',
  `denomination` decimal(10,2) NOT NULL COMMENT '优惠券面值',
  `store_id` bigint(20) DEFAULT '0' COMMENT '商户id',
  `platform_flag` tinyint(4) DEFAULT '0' COMMENT '是否平台优惠券 0店铺 1平台',
  `scope_type` tinyint(4) DEFAULT NULL COMMENT '营销类型(0,1,2,3,4),0全部，1品牌，2boss分类，3.店铺分类，4自定义货品（店铺可用）',
  `coupon_desc` text COMMENT '优惠券说明',
  `coupon_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '优惠券类型 0通用券 1店铺券 2运费券',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券表';

CREATE TABLE `coupon_marketing_scope` (
  `marketing_scope_id` varchar(32) NOT NULL COMMENT '主键id',
  `coupon_id` varchar(32) NOT NULL COMMENT '优惠券id',
  `scope_type` tinyint(4) NOT NULL COMMENT '营销类型(0,1,2,3,4),0全部，1品牌，2boss分类，3.店铺分类，4自定义货品',
  `scope_id` varchar(32) NOT NULL COMMENT '营销id,可以为0(全部)，brand_id(品牌id)，cate_id(分类id), goods_info_id(货品id)',
  `cate_grade` tinyint(4) DEFAULT NULL COMMENT '分类级别(1,2,3）',
  PRIMARY KEY (`marketing_scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券商品作用范围';

CREATE TABLE `marketing_full_discount_level` (
  `discount_level_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '满折级别Id',
  `marketing_id` bigint(20) NOT NULL COMMENT '营销Id',
  `full_amount` decimal(12,2) DEFAULT NULL COMMENT '满金额',
  `full_count` bigint(5) DEFAULT NULL COMMENT '满数量',
  `discount` decimal(10,2) NOT NULL COMMENT '满金额|数量后折扣',
  PRIMARY KEY (`discount_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=264 DEFAULT CHARSET=utf8 COMMENT='满折关联表';

CREATE TABLE `marketing_full_gift_detail` (
  `gift_detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '满赠赠品Id',
  `gift_level_id` bigint(20) NOT NULL COMMENT '满赠多级促销Id',
  `product_id` varchar(32) NOT NULL COMMENT '赠品Id',
  `product_num` bigint(5) NOT NULL COMMENT '赠品数量',
  `marketing_id` bigint(20) NOT NULL COMMENT '满赠ID',
  PRIMARY KEY (`gift_detail_id`),
  KEY `idx_gift_detail_marketingid_levelid` (`marketing_id`,`gift_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=358 DEFAULT CHARSET=utf8 COMMENT='满赠赠品细节表';

CREATE TABLE `marketing_full_gift_level` (
  `gift_level_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '满赠多级促销Id',
  `marketing_id` bigint(20) NOT NULL COMMENT '营销Id',
  `full_amount` decimal(12,2) DEFAULT NULL COMMENT '满金额赠',
  `full_count` bigint(5) DEFAULT NULL COMMENT '满数量赠',
  `gift_type` tinyint(4) NOT NULL COMMENT '赠品赠送的方式 0:全赠  1：赠一个',
  PRIMARY KEY (`gift_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=236 DEFAULT CHARSET=utf8 COMMENT='满赠关联表';

CREATE TABLE `marketing_full_reduction_level` (
  `reduction_level_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '满减级别Id',
  `marketing_id` bigint(20) NOT NULL COMMENT '营销Id',
  `full_amount` decimal(12,2) DEFAULT NULL COMMENT '满金额',
  `full_count` bigint(5) DEFAULT NULL COMMENT '满数量',
  `reduction` decimal(12,2) NOT NULL COMMENT '满金额|数量后减多少元',
  PRIMARY KEY (`reduction_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8 COMMENT='满减关联表';


CREATE TABLE `distribution_record` (
  `record_id` varchar(32) NOT NULL COMMENT '分销记录表主键',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT '货品Id',
  `trade_id` varchar(32) DEFAULT NULL COMMENT '订单交易号',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺Id',
  `company_id` bigint(20) DEFAULT NULL COMMENT '商家Id',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员Id',
  `distributor_id` varchar(32) DEFAULT NULL COMMENT '分销员标识UUID',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `finish_time` datetime DEFAULT NULL COMMENT '订单完成时间',
  `mission_received_time` datetime DEFAULT NULL COMMENT '佣金入账时间',
  `order_goods_price` decimal(20,2) DEFAULT NULL COMMENT '订单单个商品金额',
  `order_goods_count` bigint(20) DEFAULT NULL COMMENT '订单商品的数量',
  `commission_goods` decimal(20,2) DEFAULT NULL COMMENT '单个货品的佣金',
  `commission_state` tinyint(2) DEFAULT '0' COMMENT '佣金是否入账 0:未入账  1：已入账',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '是否删除的标志 0：未删除   1：已删除',
  `distributor_customer_id` varchar(32) DEFAULT NULL COMMENT '分销员的客户id',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
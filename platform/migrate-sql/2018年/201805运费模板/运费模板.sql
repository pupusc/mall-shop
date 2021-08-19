-- 商品增加重量,体积,运费模板id
ALTER TABLE `goods`
MODIFY COLUMN `goods_weight`  decimal(20,3) NULL DEFAULT NULL COMMENT '商品重量' AFTER `goods_img`,
ADD COLUMN `goods_cubage`  decimal(20,6) NULL COMMENT '商品体积' AFTER `goods_weight`,
ADD COLUMN `freight_temp_id`  bigint(12) NULL COMMENT '单品运费模板id' AFTER `goods_cubage`;

-- 商品库增加重量,体积
ALTER TABLE `standard_goods`
MODIFY COLUMN `goods_weight`  decimal(20,3) NULL DEFAULT NULL COMMENT '商品重量' AFTER `goods_img`,
ADD COLUMN `goods_cubage`  decimal(20,6) NULL COMMENT '商品体积' AFTER `goods_weight`;

-- 店铺增加使用运费模板类别
ALTER TABLE `store`
ADD COLUMN `freight_template_type`  tinyint(1) NULL DEFAULT 0 COMMENT '使用的运费模板类别(0:店铺运费,1:单品运费)' AFTER `company_info_id`;

-- 单品运费模板表
DROP TABLE IF EXISTS `freight_template_goods`;
CREATE TABLE `freight_template_goods` (
  `freight_temp_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '运费模板id',
  `freight_temp_name` varchar(60) DEFAULT NULL COMMENT '运费模板名称',
  `province_id` bigint(10) DEFAULT NULL COMMENT '发货地-省份',
  `city_id` bigint(10) DEFAULT NULL COMMENT '发货地-地市',
  `area_id` bigint(10) DEFAULT NULL COMMENT '发货地-区镇',
  `freight_free_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否包邮(0:不包邮,1:包邮)',
  `valuation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '计价方式(0:按件数,1:按重量,2:按体积)',
  `deliver_way` tinyint(1) NOT NULL DEFAULT '1' COMMENT '运送方式(1:快递配送)',
  `specify_term_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否指定条件包邮(0:不指定,1:指定)',
  `store_id` bigint(20) NOT NULL COMMENT '店铺标识',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `default_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否默认(0:否,1:是)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`freight_temp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COMMENT='单品运费模板';

-- 单品运费模板-配送地表
DROP TABLE IF EXISTS `freight_template_goods_express`;
CREATE TABLE `freight_template_goods_express` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `freight_temp_id` bigint(12) DEFAULT NULL COMMENT '运费模板id',
  `destination_area` varchar(3000) NOT NULL COMMENT '配送地id(逗号分隔)',
  `destination_area_name` varchar(3000) NOT NULL COMMENT '配送地名称(逗号分隔)',
  `valuation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '计价方式(0:按件数,1:按重量,2:按体积)',
  `freight_start_num` decimal(10,2) NOT NULL DEFAULT '1.00' COMMENT '首件/重/体积',
  `freight_start_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '对应于首件/重/体积的起步价',
  `freight_plus_num` decimal(10,2) NOT NULL DEFAULT '1.00' COMMENT '续件/重/体积',
  `freight_plus_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '对应于续件/重/体积的价格',
  `default_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否默认(0:否,1:是)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`id`),
  KEY `freight_temp_id` (`freight_temp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COMMENT='单品运费模板快递运送';

-- 单品运费模板-包邮条件表
DROP TABLE IF EXISTS `freight_template_goods_free`;
CREATE TABLE `freight_template_goods_free` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `freight_temp_id` bigint(12) DEFAULT NULL COMMENT '运费模板id',
  `destination_area` varchar(3000) NOT NULL COMMENT '配送地id(逗号分隔)',
  `destination_area_name` varchar(3000) NOT NULL COMMENT '配送地名称(逗号分隔)',
  `deliver_way` tinyint(1) NOT NULL DEFAULT '1' COMMENT '运送方式(1:快递配送)',
  `valuation_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '计价方式(0:按件数,1:按重量,2:按体积)',
  `condition_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '包邮条件类别(0:件/重/体积计价方式,1:金额,2:计价方式+金额)',
  `condition_one` decimal(12,2) DEFAULT NULL COMMENT '包邮条件1(件/重/体积)',
  `condition_two` decimal(12,2) DEFAULT NULL COMMENT '包邮条件2(金额)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`id`),
  KEY `freight_temp_id` (`freight_temp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='单品运费模板指定包邮条件';

-- 店铺运费模板
DROP TABLE IF EXISTS `freight_template_store`;
CREATE TABLE `freight_template_store` (
  `freight_temp_id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '运费模板id',
  `freight_temp_name` varchar(60) DEFAULT NULL COMMENT '运费模板名称',
  `deliver_way` tinyint(1) NOT NULL DEFAULT '1' COMMENT '运送方式(1:快递配送)',
  `destination_area` varchar(3000) NOT NULL COMMENT '配送地id(逗号分隔)',
  `destination_area_name` varchar(3000) NOT NULL COMMENT '配送地名称(逗号分隔)',
  `freight_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '运费计费规则(0:满金额包邮,1:固定运费)',
  `satisfy_price` decimal(10,2) DEFAULT NULL COMMENT '满多少金额包邮',
  `satisfy_freight` decimal(10,2) DEFAULT NULL COMMENT '不满金额的运费',
  `fixed_freight` decimal(10,2) DEFAULT NULL COMMENT '固定的运费',
  `store_id` bigint(20) NOT NULL COMMENT '店铺标识',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `default_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否默认(0:否,1:是)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`freight_temp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COMMENT='店铺运费模板';

-- ☆☆☆☆☆别忘了各个环境初始化店铺模板的MySQL Function : init_store_freight_template
BEGIN
  -- 定义局部变量,用于填充insert语句
	DECLARE store_id bigint;
	DECLARE company_info_id bigint;
	DECLARE province_id bigint;
	DECLARE city_id bigint;
	DECLARE area_id bigint;

	DECLARE done BOOLEAN DEFAULT 0;
	-- 使用游标
	DECLARE store_cursor CURSOR FOR select s.store_id,s.company_info_id,s.province_id,s.city_id,s.area_id from store as s where del_flag = 0;
  -- 循环结束关闭游标的条件
  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;

	OPEN store_cursor;
	REPEAT
		FETCH store_cursor INTO store_id,company_info_id,province_id,city_id,area_id;
		IF done!=1 THEN

			-- 1.插入店铺模板
			insert into freight_template_store(freight_temp_name,destination_area,destination_area_name,freight_type,fixed_freight,store_id,company_info_id,create_time) VALUES('默认模板','','未被划分的配送地区自动归于默认运费',1, 0,
				store_id, -- 店铺id
				company_info_id, -- 商家id
				NOW());

			-- 2.插入单品模板
			insert into freight_template_goods (freight_temp_name,province_id,city_id,area_id,store_id,company_info_id,create_time)values (
				'默认模板',
				province_id, -- 店铺省份
				city_id, -- 店铺地市
				area_id, -- 店铺区镇
				store_id, -- 店铺id
				company_info_id, -- 商家id
				NOW());

			-- 3.插入单品默认模板-配送地默认信息
			insert into freight_template_goods_express (freight_temp_id,destination_area,destination_area_name,freight_start_num,freight_start_price,freight_plus_num,freight_plus_price,create_time)VALUES(
			LAST_INSERT_ID(), -- 刚刚插入的单品运费模板id
			'','未被划分的配送地区自动归于默认运费',1,0,1,0,NOW());

		END IF;
	UNTIL DONE END REPEAT;
	CLOSE store_cursor;

	-- 4.修改store表中的使用模板类型
	update store set freight_template_type = 0;

	-- 5.修改goods表中的默认单品模板id
	UPDATE goods g
	SET g.freight_temp_id = (
		SELECT fr.freight_temp_id
		FROM freight_template_goods as fr
		where g.store_id = fr.store_id
	)
	where EXISTS (
		SELECT
		fr.freight_temp_id
		FROM freight_template_goods as fr
		where g.store_id = fr.store_id
	);

	-- 6.将goods中没有重量与体积的,设置为最小值(默认值)
	UPDATE goods g
	SET g.goods_weight = 0.001
	WHERE g.goods_weight is NULL;
	UPDATE goods g
	SET g.goods_cubage = 0.000001
	WHERE g.goods_cubage is NULL;

	-- 7.将standard_goods中没有重量与体积的,设置为最小值(默认值)
	UPDATE standard_goods g
	SET g.goods_weight = 0.001
	WHERE g.goods_weight is NULL;
	UPDATE standard_goods g
	SET g.goods_cubage = 0.000001
	WHERE g.goods_cubage is NULL;

	RETURN 1;
END
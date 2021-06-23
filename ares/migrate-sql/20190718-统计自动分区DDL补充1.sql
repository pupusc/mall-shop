-- 方案1. alter方式 （无需备份数据），直接运行，兼容历史数据

ALTER TABLE customer_area_distribute PARTITION BY RANGE (TO_DAYS(target_date))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE customer_level_day PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE customer_level_distribute PARTITION BY RANGE (TO_DAYS(target_date))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE employee_client_month PARTITION BY RANGE (TO_DAYS(target_date))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE customer_level_month DROP PRIMARY KEY,
 ADD PRIMARY KEY (ID, STAT_DATE);

ALTER TABLE customer_level_month PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE customer_month DROP PRIMARY KEY,
 ADD PRIMARY KEY (ID, STAT_DATE);

ALTER TABLE customer_month PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE customer_region_day DROP PRIMARY KEY,
 ADD PRIMARY KEY (ID, STAT_DATE);

ALTER TABLE customer_region_day PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE customer_region_month DROP PRIMARY KEY,
 ADD PRIMARY KEY (ID, STAT_DATE);

ALTER TABLE customer_region_month PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE employee_performance_month PARTITION BY RANGE (TO_DAYS(target_date))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

ALTER TABLE goods_day DROP PRIMARY KEY,
 ADD PRIMARY KEY (ID, STAT_DATE);

ALTER TABLE goods_day PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201907
	VALUES
		less than (TO_DAYS('2019-08-01')) ENGINE = INNODB
);

DROP TABLE
IF EXISTS `goods_month`;

CREATE TABLE `goods_month` (
	`ID` VARCHAR (50) NOT NULL COMMENT '由GOODS_INFO_ID+YYYYMM组成',
	`GOODS_INFO_ID` VARCHAR (50) NOT NULL,
	`STAT_MONTH` INT (6) NOT NULL COMMENT '年月，格式为YYYYMM',
	`ORDER_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '下单金额',
	`ORDER_NUM` DECIMAL (20, 0) NOT NULL COMMENT '下单件数',
	`PAY_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '付款订单数',
	`PAY_NUM` DECIMAL (20, 0) NOT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '付款金额',
	`ORDER_CONVERSION` DECIMAL (5, 2) NOT NULL COMMENT '单品转化率',
	`PAY_CONVERSION` DECIMAL (5, 2) NOT NULL COMMENT '付款转化率',
	`REFUND_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '退货笔数',
	`REFUND_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '退货金额',
	`REFUND_NUM` DECIMAL (20, 0) NOT NULL COMMENT '退货件数',
	`UV` DECIMAL (20, 0) NOT NULL COMMENT '访问人数',
	`SHOP_ID` VARCHAR (50) NOT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME NOT NULL COMMENT '创建时间',
	PRIMARY KEY (`ID`, `STAT_MONTH`),
	KEY `ind_goods_month_1` (`GOODS_INFO_ID`),
	KEY `ind_goods_month_2` (`STAT_MONTH`),
	KEY `ind_goods_month_3` (`SHOP_ID`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '每月商品统计表' PARTITION BY RANGE (STAT_MONTH)(
	PARTITION p201906
	VALUES
		LESS THAN (201907) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (201908) ENGINE = INNODB
);

DROP EVENT
IF EXISTS e_partition_goods_month;
DELIMITER $$


USE `s2b_statistics`$$

CREATE EVENT
IF NOT EXISTS `e_partition_goods_month` ON SCHEDULE EVERY 1 MINUTE STARTS now() ON COMPLETION PRESERVE ENABLE COMMENT 'Creating partitions' DO

BEGIN
	#调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
	CALL s2b_statistics.create_partition_by_month ('s2b_statistics', 'goods_month') ; END$$
DELIMITER ;




-- 方案2，重新创建方案，需将现有表数据备份，执行脚本后再导入

/*
 Navicat Premium Data Transfer

 Source Server         : s2b_prod
 Source Server Type    : MySQL
 Source Server Version : 50634
 Source Host           : 116.62.54.79
 Source Database       : s2b_statistics

 Target Server Type    : MySQL
 Target Server Version : 50634
 File Encoding         : utf-8

 Date: 07/18/2019 20:16:44 PM
*/
SET NAMES utf8;


SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `customer_area_distribute`
-- ----------------------------
DROP TABLE
IF EXISTS `customer_area_distribute`;

CREATE TABLE `customer_area_distribute` (
	`id` BIGINT (10) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`company_id` VARCHAR (30) NOT NULL COMMENT '商户id',
	`city_id` BIGINT (10) NOT NULL COMMENT '客户所在城市id',
	`num` BIGINT (10) NOT NULL COMMENT '当前城市下客户人数',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报表生成时间',
	`target_date` date NOT NULL COMMENT '目标数据日期',
	PRIMARY KEY (`id`, `target_date`),
	KEY `ind_customer_area_distribute_1` (`company_id`),
	KEY `ind_customer_area_distribute_2` (`target_date`)
) ENGINE = INNODB AUTO_INCREMENT = 2879363 DEFAULT CHARSET = utf8 COMMENT = '客户地区分布统计报表\r\n'
 PARTITION BY RANGE (TO_DAYS(target_date))
(PARTITION p201907 VALUES LESS THAN (737637) ENGINE = InnoDB);

-- ----------------------------
--  Table structure for `customer_level_day`
-- ----------------------------
DROP TABLE
IF EXISTS `customer_level_day`;

CREATE TABLE `customer_level_day` (
	`ID` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '标识',
	`STAT_DATE` date NOT NULL COMMENT '日期',
	`CUSTOMER_LEVEL_ID` DECIMAL (10, 0) DEFAULT NULL COMMENT '客户等级代码',
	`CUSTOMER_LEVEL_NAME` VARCHAR (50) DEFAULT NULL COMMENT '客户等级名称',
	`ORDER_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '下单金额',
	`ORDER_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单件数',
	`PAY_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款订单数',
	`PAY_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '付款金额',
	`USER_PER_PRICE_THIRTY` DECIMAL (20, 2) DEFAULT NULL COMMENT '客单价',
	`ORDER_PER_PRICE` DECIMAL (20, 2) DEFAULT NULL COMMENT '笔单价',
	`REFUND_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退单笔数',
	`REFUND_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '退单金额',
	`REFUND_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退货件数',
	`SHOP_ID` VARCHAR (50) DEFAULT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME DEFAULT NULL COMMENT '创建时间',
	`CUSTOMER_COUNT` BIGINT (10) NOT NULL DEFAULT '0' COMMENT '会员数',
	PRIMARY KEY (`ID`, `STAT_DATE`),
	KEY `ind_customer_level_day_1` (`STAT_DATE`)
) ENGINE = INNODB AUTO_INCREMENT = 85937 DEFAULT CHARSET = utf8 COMMENT = '每日客户等级统计表'
PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201807
	VALUES
		LESS THAN (737637) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (737654) ENGINE = INNODB
);

-- ----------------------------
--  Table structure for `customer_level_month`
-- ----------------------------
DROP TABLE
IF EXISTS `customer_level_month`;

CREATE TABLE `customer_level_month` (
	`ID` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '标识',
	`CUSTOMER_LEVEL_ID` DECIMAL (10, 0) DEFAULT NULL COMMENT '客户等级代码',
	`CUSTOMER_LEVEL_NAME` VARCHAR (50) DEFAULT NULL COMMENT '客户等级名称',
	`ORDER_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '下单金额',
	`ORDER_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单件数',
	`PAY_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款订单数',
	`PAY_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '付款金额',
	`USER_PER_PRICE_THIRTY` DECIMAL (20, 2) DEFAULT NULL COMMENT '客单价',
	`ORDER_PER_PRICE` DECIMAL (20, 2) DEFAULT NULL COMMENT '笔单价',
	`REFUND_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退单笔数',
	`REFUND_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '退单金额',
	`REFUND_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退货件数',
	`SHOP_ID` VARCHAR (50) DEFAULT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME DEFAULT NULL COMMENT '创建时间',
	`STAT_DATE` date NOT NULL DEFAULT '0000-00-00',
	PRIMARY KEY (`ID`, `STAT_DATE`),
	KEY `ind_customer_level_month_2` (`CUSTOMER_LEVEL_ID`),
	KEY `ind_customer_level_month_3` (`SHOP_ID`),
	KEY `ind_customer_level_month_1` (`STAT_DATE`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 366 DEFAULT CHARSET = utf8 COMMENT = '每月客户等级统计表'
PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201807
	VALUES
		LESS THAN (737637) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (737654) ENGINE = INNODB
);

-- ----------------------------
--  Table structure for `customer_month`
-- ----------------------------
DROP TABLE
IF EXISTS `customer_month`;

CREATE TABLE `customer_month` (
	`ID` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '标识',
	`CUSTOMER_ID` VARCHAR (50) DEFAULT NULL COMMENT '客户标识',
	`CUSTOMER_NAME` VARCHAR (128) DEFAULT NULL COMMENT '客户名称',
	`CUSTOMER_ACCOUNT` VARCHAR (20) DEFAULT NULL COMMENT '账号',
	`ORDER_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '下单金额',
	`ORDER_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单件数',
	`PAY_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款订单数',
	`PAY_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '付款金额',
	`ORDER_PER_PRICE` DECIMAL (20, 2) DEFAULT NULL COMMENT '笔单价',
	`REFUND_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退单笔数',
	`REFUND_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '退单金额',
	`REFUND_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退货件数',
	`SHOP_ID` VARCHAR (50) DEFAULT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME DEFAULT NULL COMMENT '创建时间',
	`STAT_DATE` date NOT NULL DEFAULT '0000-00-00',
	PRIMARY KEY (`ID`, `STAT_DATE`),
	KEY `ind_customer_month_1` (`CUSTOMER_NAME`),
	KEY `ind_customer_month_2` (`CUSTOMER_ACCOUNT`),
	KEY `ind_customer_month_3` (`SHOP_ID`)
) ENGINE = INNODB AUTO_INCREMENT = 796 DEFAULT CHARSET = utf8 COMMENT = '每月客户统计表'
PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201807
	VALUES
		LESS THAN (737637) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (737654) ENGINE = INNODB
);

-- ----------------------------
--  Table structure for `customer_region_day`
-- ----------------------------
DROP TABLE
IF EXISTS `customer_region_day`;

CREATE TABLE `customer_region_day` (
	`ID` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '标识',
	`STAT_DATE` date NOT NULL DEFAULT '0000-00-00' COMMENT '日期',
	`PROVINCE_ID` BIGINT (10) DEFAULT NULL COMMENT '省份标识',
	`CITY_ID` VARCHAR (50) DEFAULT NULL COMMENT '地市标识',
	`PROVINCE_NAME` VARCHAR (100) DEFAULT NULL COMMENT '省份名称',
	`CITY_NAME` VARCHAR (100) DEFAULT NULL COMMENT '地市名称',
	`ORDER_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '下单金额',
	`ORDER_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单件数',
	`PAY_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款订单数',
	`PAY_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '付款金额',
	`USER_PER_PRICE_THIRTY` DECIMAL (20, 2) DEFAULT NULL COMMENT '客单价',
	`ORDER_PER_PRICE` DECIMAL (20, 2) DEFAULT NULL COMMENT '笔单价',
	`REFUND_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退单笔数',
	`REFUND_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '退单金额',
	`REFUND_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退货件数',
	`SHOP_ID` VARCHAR (50) DEFAULT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME DEFAULT NULL COMMENT '创建时间',
	`CUSTOMER_COUNT` BIGINT (10) NOT NULL COMMENT '客户数量',
	PRIMARY KEY (`ID`, `STAT_DATE`),
	KEY `ind_customer_region_day_1` (`STAT_DATE`),
	KEY `ind_customer_region_day_2` (`CITY_ID`),
	KEY `ind_customer_region_day_3` (`SHOP_ID`)
) ENGINE = INNODB AUTO_INCREMENT = 68360 DEFAULT CHARSET = utf8 COMMENT = '每日客户统计表'
PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201807
	VALUES
		LESS THAN (737637) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (737654) ENGINE = INNODB
);

-- ----------------------------
--  Table structure for `customer_region_month`
-- ----------------------------
DROP TABLE
IF EXISTS `customer_region_month`;

CREATE TABLE `customer_region_month` (
	`ID` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '标识',
	`PROVINCE_ID` BIGINT (10) DEFAULT NULL COMMENT '省份标识',
	`CITY_ID` BIGINT (10) DEFAULT NULL COMMENT '地市标识',
	`PROVINCE_NAME` VARCHAR (100) DEFAULT NULL COMMENT '省份名称',
	`CITY_NAME` VARCHAR (100) DEFAULT NULL COMMENT '地市名称',
	`ORDER_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '下单金额',
	`ORDER_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '下单件数',
	`PAY_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款订单数',
	`PAY_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '付款金额',
	`USER_PER_PRICE_THIRTY` DECIMAL (20, 2) DEFAULT NULL COMMENT '客单价',
	`ORDER_PER_PRICE` DECIMAL (20, 2) DEFAULT NULL COMMENT '笔单价',
	`REFUND_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退单笔数',
	`REFUND_MONEY` DECIMAL (20, 2) DEFAULT NULL COMMENT '退单金额',
	`REFUND_GOODS_NUM` DECIMAL (20, 0) DEFAULT NULL COMMENT '退货件数',
	`SHOP_ID` VARCHAR (50) DEFAULT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME DEFAULT NULL COMMENT '创建时间',
	`STAT_DATE` date NOT NULL DEFAULT '0000-00-00',
	PRIMARY KEY (`ID`, `STAT_DATE`),
	KEY `ind_customer_region_month_1` (`STAT_DATE`),
	KEY `ind_customer_region_month_2` (`CITY_ID`),
	KEY `ind_customer_region_month_3` (`SHOP_ID`)
) ENGINE = INNODB AUTO_INCREMENT = 298 DEFAULT CHARSET = utf8 COMMENT = '每月客户地区统计表'
PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201807
	VALUES
		LESS THAN (737637) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (737654) ENGINE = INNODB
);

-- ----------------------------
--  Table structure for `employee_performance_month`
-- ----------------------------
DROP TABLE
IF EXISTS `employee_performance_month`;

CREATE TABLE `employee_performance_month` (
	`id` BIGINT (10) NOT NULL AUTO_INCREMENT,
	`company_id` VARCHAR (50) NOT NULL COMMENT '商户id',
	`employee_id` VARCHAR (50) NOT NULL COMMENT '业务员id',
	`order_count` BIGINT (10) NOT NULL COMMENT '下单笔数',
	`customer_count` BIGINT (10) NOT NULL COMMENT '下单人数',
	`amount` DECIMAL (20, 2) NOT NULL COMMENT '订单总金额',
	`pay_amount` DECIMAL (20, 2) NOT NULL COMMENT '支付总金额',
	`pay_count` BIGINT (20) NOT NULL COMMENT '付款总订单数',
	`pay_customer_count` BIGINT (20) NOT NULL COMMENT '付款总客户数',
	`return_count` BIGINT (20) NOT NULL COMMENT '退单总数',
	`return_customer_count` BIGINT (20) NOT NULL COMMENT '退单总客户数',
	`return_amount` DECIMAL (20, 2) NOT NULL COMMENT '退单总金额',
	`order_unit_price` DECIMAL (20, 2) NOT NULL COMMENT '笔单价',
	`customer_unit_price` DECIMAL (20, 2) NOT NULL COMMENT '客单价',
	`target_date` date NOT NULL COMMENT '报表目标数据日期',
	`create_time` DATETIME NOT NULL COMMENT '报表生成时间',
	PRIMARY KEY (`id`, `target_date`),
	KEY `ind_employee_performance_month_1` (`company_id`),
	KEY `ind_employee_performance_month_2` (`employee_id`),
	KEY `ind_employee_performance_month_3` (`target_date`)
) ENGINE = INNODB AUTO_INCREMENT = 70 DEFAULT CHARSET = utf8 COMMENT = '业务员业绩月报表'
PARTITION BY RANGE (TO_DAYS(target_date))(
	PARTITION p201807
	VALUES
		LESS THAN (737637) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (737654) ENGINE = INNODB
);

-- ----------------------------
--  Table structure for `goods_day`
-- ----------------------------
DROP TABLE
IF EXISTS `goods_day`;

CREATE TABLE `goods_day` (
	`ID` INT (20) NOT NULL AUTO_INCREMENT COMMENT '自增长字段',
	`GOODS_INFO_ID` VARCHAR (50) NOT NULL,
	`STAT_DATE` date NOT NULL COMMENT '日期',
	`ORDER_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '下单金额',
	`ORDER_NUM` DECIMAL (20, 0) NOT NULL COMMENT '下单件数',
	`PAY_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '付款订单数',
	`PAY_NUM` DECIMAL (20, 0) NOT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '付款金额',
	`ORDER_CONVERSION` DECIMAL (5, 2) NOT NULL COMMENT '单品转化率',
	`PAY_CONVERSION` DECIMAL (5, 2) NOT NULL COMMENT '付款转化率',
	`REFUND_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '退货笔数',
	`REFUND_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '退货金额',
	`REFUND_NUM` DECIMAL (20, 0) NOT NULL COMMENT '退货件数',
	`UV` DECIMAL (20, 0) NOT NULL COMMENT '访问人数',
	`SHOP_ID` VARCHAR (50) NOT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME NOT NULL COMMENT '创建时间',
	PRIMARY KEY (`ID`, `STAT_DATE`),
	KEY `ind_goods_day_1` (`GOODS_INFO_ID`),
	KEY `ind_goods_day_2` (`STAT_DATE`),
	KEY `ind_goods_day_3` (`SHOP_ID`)
) ENGINE = INNODB AUTO_INCREMENT = 112256 DEFAULT CHARSET = utf8 COMMENT = '每日商品统计表'
PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201807
	VALUES
		LESS THAN (737637) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (737654) ENGINE = INNODB
);

-- ----------------------------
--  Table structure for `goods_month`
-- ----------------------------
DROP TABLE
IF EXISTS `goods_month`;

CREATE TABLE `goods_month` (
	`ID` VARCHAR (50) NOT NULL COMMENT '由GOODS_INFO_ID+YYYYMM组成',
	`GOODS_INFO_ID` VARCHAR (50) NOT NULL,
	`STAT_MONTH` INT (6) NOT NULL COMMENT '年月，格式为YYYYMM',
	`ORDER_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '下单笔数',
	`ORDER_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '下单金额',
	`ORDER_NUM` DECIMAL (20, 0) NOT NULL COMMENT '下单件数',
	`PAY_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '付款订单数',
	`PAY_NUM` DECIMAL (20, 0) NOT NULL COMMENT '付款件数',
	`PAY_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '付款金额',
	`ORDER_CONVERSION` DECIMAL (5, 2) NOT NULL COMMENT '单品转化率',
	`PAY_CONVERSION` DECIMAL (5, 2) NOT NULL COMMENT '付款转化率',
	`REFUND_COUNT` DECIMAL (20, 0) NOT NULL COMMENT '退货笔数',
	`REFUND_MONEY` DECIMAL (20, 2) NOT NULL COMMENT '退货金额',
	`REFUND_NUM` DECIMAL (20, 0) NOT NULL COMMENT '退货件数',
	`UV` DECIMAL (20, 0) NOT NULL COMMENT '访问人数',
	`SHOP_ID` VARCHAR (50) NOT NULL COMMENT '店铺标识',
	`CREATE_TM` DATETIME NOT NULL COMMENT '创建时间',
	PRIMARY KEY (`ID`, `STAT_MONTH`),
	KEY `ind_goods_month_1` (`GOODS_INFO_ID`),
	KEY `ind_goods_month_2` (`STAT_MONTH`),
	KEY `ind_goods_month_3` (`SHOP_ID`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '每月商品统计表'
PARTITION BY RANGE (STAT_MONTH)(
		PARTITION p201905
	VALUES
		LESS THAN (201906) ENGINE = INNODB,
		PARTITION p201906
	VALUES
		LESS THAN (201907) ENGINE = INNODB,
		PARTITION p201907
	VALUES
		LESS THAN (201908) ENGINE = INNODB
);

DROP EVENT
IF EXISTS e_partition_goods_month;
DELIMITER $$


USE `s2b_statistics`$$

CREATE EVENT
IF NOT EXISTS `e_partition_goods_month` ON SCHEDULE EVERY 1 MINUTE STARTS now() ON COMPLETION PRESERVE ENABLE COMMENT 'Creating partitions' DO

BEGIN
	#调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
	CALL s2b_statistics.create_partition_by_month ('s2b_statistics', 'goods_month') ; END$$
DELIMITER ;



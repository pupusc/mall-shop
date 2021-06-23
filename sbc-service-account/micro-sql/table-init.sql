-- 数据库：sbc-account

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `company_account` (
  `account_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_info_id` bigint(11) DEFAULT NULL COMMENT '公司信息ID',
  `is_received` tinyint(4) DEFAULT '0' COMMENT '是否收到平台首次打款 0、否 1、是',
  `is_default_account` tinyint(4) DEFAULT '0' COMMENT '是否主账号 0、否 1、是',
  `account_name` varchar(150) DEFAULT NULL COMMENT '账户名称',
  `bank_name` varchar(150) DEFAULT NULL COMMENT '开户银行',
  `bank_branch` varchar(150) DEFAULT NULL COMMENT '支行',
  `bank_no` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `bank_code` varchar(20) DEFAULT NULL COMMENT '银行编号',
  `bank_status` tinyint(4) DEFAULT NULL COMMENT '银行状态 0: 启用 1:禁用',
  `third_id` varchar(45) DEFAULT NULL COMMENT '第三方店铺ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除状态 0：否 1：是',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `remit_price` decimal(20,2) DEFAULT NULL COMMENT '打款金额',
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_id_UNIQUE` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8 COMMENT='商家线下账户';


CREATE TABLE `reconciliation` (
  `id` varchar(45) CHARACTER SET utf8mb4 NOT NULL,
  `supplier_id` int(11) NOT NULL COMMENT '商家id',
  `supplier_name` varchar(128) DEFAULT NULL COMMENT '商家名称',
  `store_id` bigint(20) NOT NULL COMMENT '店铺id',
  `store_name` varchar(128) DEFAULT NULL COMMENT '店铺名称',
  `pay_way` varchar(30) NOT NULL COMMENT '支付/退款项id',
  `amount` decimal(20,2) DEFAULT NULL,
  `customer_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '客户id',
  `customer_name` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '客户昵称',
  `order_code` varchar(45) CHARACTER SET utf8mb4 NOT NULL COMMENT '订单号',
  `return_order_code` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '退单号，订单支付时可为空',
  `order_time` datetime NOT NULL COMMENT '订单/退单申请时间',
  `trade_time` datetime NOT NULL COMMENT '支付/退款时间',
  `type` tinyint(1) NOT NULL COMMENT '交易类型，0：收入 1：退款',
  `discounts` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`,`trade_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务交易对账明细表'
/*!50100 PARTITION BY RANGE (TO_DAYS(trade_time))
(PARTITION p201712 VALUES LESS THAN (737060) ENGINE = InnoDB,
 PARTITION p201801 VALUES LESS THAN (737091) ENGINE = InnoDB,
 PARTITION p201802 VALUES LESS THAN (737125) ENGINE = InnoDB,
 PARTITION p201803 VALUES LESS THAN (737158) ENGINE = InnoDB,
 PARTITION p201804 VALUES LESS THAN (737180) ENGINE = InnoDB,
 PARTITION p201805 VALUES LESS THAN (737211) ENGINE = InnoDB,
 PARTITION p201806 VALUES LESS THAN (737241) ENGINE = InnoDB,
 PARTITION p201807 VALUES LESS THAN (737272) ENGINE = InnoDB,
 PARTITION p201808 VALUES LESS THAN (737303) ENGINE = InnoDB,
 PARTITION p201809 VALUES LESS THAN (737333) ENGINE = InnoDB,
 PARTITION p201810 VALUES LESS THAN (737364) ENGINE = InnoDB,
 PARTITION p201811 VALUES LESS THAN (737394) ENGINE = InnoDB,
 PARTITION p201812 VALUES LESS THAN (737425) ENGINE = InnoDB,
 PARTITION p201901 VALUES LESS THAN (737456) ENGINE = InnoDB) */;

CREATE TABLE `settlement` (
  `settle_id` int(11) NOT NULL AUTO_INCREMENT,
  `settle_uuid` varchar(32) DEFAULT NULL COMMENT '结算单uuid，mongodb外键',
  `create_time` datetime DEFAULT NULL COMMENT '结算单创建时间',
  `settle_code` varchar(32) DEFAULT NULL COMMENT '结算单号',
  `start_time` varchar(32) DEFAULT NULL COMMENT '结算单开始时间',
  `end_time` varchar(32) DEFAULT NULL COMMENT '结算单结束时间',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺Id',
  `store_name` varchar(100) DEFAULT NULL COMMENT '店铺名称',
  `sale_price` decimal(20,2) DEFAULT NULL COMMENT '交易总额',
  `sale_num` mediumtext COMMENT '商品销售总数',
  `return_price` decimal(20,2) DEFAULT NULL COMMENT '退款总额',
  `return_num` mediumtext COMMENT '商品退货总数',
  `platform_price` decimal(20,6) DEFAULT NULL COMMENT '平台佣金总额',
  `store_price` decimal(20,6) DEFAULT NULL COMMENT '店铺应收',
  `delivery_price` decimal(20,2) DEFAULT NULL COMMENT '退还运费',
  `settle_status` tinyint(4) DEFAULT NULL COMMENT '结算状态(0:未结算,1:已结算,2:暂不处理)',
  PRIMARY KEY (`settle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=907 DEFAULT CHARSET=utf8 COMMENT='结算单';


CREATE TABLE `offline_account` (
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
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `account_id_UNIQUE` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8 COMMENT='线下账户';


CREATE TABLE `invoice_project` (
  `project_id` varchar(32) NOT NULL COMMENT '开票项目id',
  `project_name` varchar(45) DEFAULT NULL COMMENT '开票项目名称',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标志 默认0：未删除 1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `operate_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家id',
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `project_id_UNIQUE` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `invoice_project_switch` (
  `invoice_project_switch_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '签约分类主键',
  `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家id',
  `is_support_invoice` tinyint(1) DEFAULT NULL COMMENT '是否支持开票 0 不支持 1 支持',
  `is_paper_invoice` tinyint(1) DEFAULT NULL COMMENT '纸质发票 0 不支持 1 支持',
  `is_value_added_tax_invoice` tinyint(1) DEFAULT NULL COMMENT '增值税发票 0 不支持 1 支持',
  PRIMARY KEY (`invoice_project_switch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='开票项目开关';


#partition procedure
DELIMITER $$
#该表所在数据库名称
USE `sbc-account`$$
DROP PROCEDURE IF EXISTS `create_partition_by_year_month`$$
CREATE PROCEDURE `create_partition_by_year_month`(IN_SCHEMANAME VARCHAR(64), IN_TABLENAME VARCHAR(64))
BEGIN
    DECLARE ROWS_CNT INT UNSIGNED;
    DECLARE BEGINTIME DATE;
    DECLARE ENDTIME INT UNSIGNED;
    DECLARE PARTITIONNAME VARCHAR(16);
    SET BEGINTIME = DATE(NOW());
    SET PARTITIONNAME = DATE_FORMAT( BEGINTIME, 'p%Y%m' );
    SET ENDTIME = TO_DAYS(DATE(BEGINTIME + INTERVAL 1 MONTH));

    SELECT COUNT(*) INTO ROWS_CNT FROM information_schema.partitions
	WHERE table_schema = IN_SCHEMANAME AND table_name = IN_TABLENAME AND partition_name = PARTITIONNAME;
    IF ROWS_CNT = 0 THEN
        SET @SQL = CONCAT( 'ALTER TABLE `', IN_SCHEMANAME, '`.`', IN_TABLENAME, '`',
	    ' ADD PARTITION (PARTITION ', PARTITIONNAME, ' VALUES LESS THAN (', ENDTIME, ') ENGINE = InnoDB);' );
        PREPARE STMT FROM @SQL;
        EXECUTE STMT;
        DEALLOCATE PREPARE STMT;
     ELSE
	SELECT CONCAT("partition `", PARTITIONNAME, "` for table `",IN_SCHEMANAME, ".", IN_TABLENAME, "` already exists") AS result;
     END IF;
END$$
DELIMITER ;


# auto partition event
DELIMITER $$
######### employee
USE `sbc-account`$$
CREATE EVENT IF NOT EXISTS `e_partition_reconciliation`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL `sbc-account`.create_partition_by_year_month('sbc-account','reconciliation');
END$$
DELIMITER ;
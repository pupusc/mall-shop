CREATE TABLE `sbc-account`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sbc-bff`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sbc-crm`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sbc-customer`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sbc-goods`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sbc-marketing`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sbc-message`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sbc-order`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sbc-pay`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sbc-setting`.`undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- sbc-goods
CREATE TABLE `sbc-goods`.`goods_check_log` (
  `id` varchar(36) NOT NULL,
  `goods_id` varchar(50) DEFAULT NULL,
  `checker` varchar(36) DEFAULT NULL,
  `check_time` datetime DEFAULT NULL,
  `audit_status` int(4) DEFAULT NULL,
  `audit_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品审核记录';

CREATE TABLE `sbc-goods`.`goods_marketing` (
  `id` varchar(36) NOT NULL,
  `goods_info_id` varchar(36) DEFAULT NULL,
  `customer_id` varchar(36) DEFAULT NULL,
  `marketing_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_goods_info_id` (`goods_info_id`),
  KEY `index_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购单商品选择的营销';

-- sbc-pay
CREATE TABLE `sbc-pay`.`pay_trade_record_copy` (
  `id` varchar(45) NOT NULL,
  `business_id` varchar(32) NOT NULL COMMENT '业务id(订单或退单号)',
  `charge_id` varchar(27) DEFAULT NULL COMMENT '支付渠道方返回的支付或退款对象id',
  `apply_price` decimal(20,2) NOT NULL COMMENT '申请价格',
  `practical_price` decimal(20,2) DEFAULT NULL COMMENT '实际成功交易价格',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态:0处理中(退款状态)/未支付(支付状态) 1成功 2失败',
  `channel_item_id` int(11) NOT NULL COMMENT '支付渠道项id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
  `finish_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端ip',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易流水号',
  `trade_type` varchar(20) DEFAULT NULL COMMENT '交易类型',
  PRIMARY KEY (`id`),
  KEY `business_id` (`business_id`),
  KEY `charge_id` (`charge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易记录'
/*!50100 PARTITION BY RANGE COLUMNS (id)
(PARTITION p2016 VALUES LESS THAN ('TP2017') ENGINE = InnoDB,
 PARTITION p2017 VALUES LESS THAN ('TP2018') ENGINE = InnoDB,
 PARTITION p2018 VALUES LESS THAN ('TP2019') ENGINE = InnoDB,
 PARTITION p2019 VALUES LESS THAN ('TP2020') ENGINE = InnoDB,
 PARTITION p2020 VALUES LESS THAN ('TP2021') ENGINE = InnoDB,
 PARTITION p2021 VALUES LESS THAN ('TP2022') ENGINE = InnoDB,
 PARTITION p2022 VALUES LESS THAN ('TP2023') ENGINE = InnoDB,
 PARTITION p2023 VALUES LESS THAN ('TP2024') ENGINE = InnoDB,
 PARTITION p2024 VALUES LESS THAN ('TP2025') ENGINE = InnoDB,
 PARTITION p2025 VALUES LESS THAN ('TP2026') ENGINE = InnoDB,
 PARTITION p2026 VALUES LESS THAN ('TP2027') ENGINE = InnoDB,
 PARTITION p2027 VALUES LESS THAN ('TP2028') ENGINE = InnoDB,
 PARTITION p2028 VALUES LESS THAN ('TP2029') ENGINE = InnoDB,
 PARTITION p2029 VALUES LESS THAN ('TP2030') ENGINE = InnoDB,
 PARTITION p2030 VALUES LESS THAN ('TP2031') ENGINE = InnoDB,
 PARTITION p2031 VALUES LESS THAN ('TP2032') ENGINE = InnoDB,
 PARTITION p2032 VALUES LESS THAN ('TP2033') ENGINE = InnoDB,
 PARTITION p2033 VALUES LESS THAN ('TP2034') ENGINE = InnoDB,
 PARTITION p2034 VALUES LESS THAN ('TP2035') ENGINE = InnoDB,
 PARTITION p2035 VALUES LESS THAN ('TP2036') ENGINE = InnoDB) */;

insert into `sbc-pay`.pay_trade_record_copy select * from `sbc-pay`.pay_trade_record;

drop table `sbc-pay`.pay_trade_record;

rename table `sbc-pay`.pay_trade_record_copy to `sbc-pay`.pay_trade_record;

-- sbc-account库------------------------------------------
CREATE TABLE `sbc-account`.`reconciliation_copy` (
	`id` varchar(45) CHARACTER SET utf8mb4 NOT NULL,
  `supplier_id` int(11) NOT NULL COMMENT '供应商id',
  `supplier_name` varchar(128) DEFAULT NULL COMMENT '供应商名称',
  `store_id` bigint(20) NOT NULL COMMENT '店铺id',
  `store_name` varchar(128) DEFAULT NULL COMMENT '店铺名称',
  `pay_way` varchar(30) NOT NULL COMMENT '支付/退款项id',
  `amount` decimal(20,2) DEFAULT NULL,
  `points` bigint(20) DEFAULT NULL COMMENT '积分',
  `customer_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '客户id',
  `customer_name` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '客户昵称',
  `order_code` varchar(45) CHARACTER SET utf8mb4 NOT NULL COMMENT '订单号',
  `return_order_code` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '退单号，订单支付时可为空',
  `order_time` datetime NOT NULL COMMENT '订单/退单申请时间',
  `trade_time` datetime NOT NULL COMMENT '支付/退款时间',
  `type` tinyint(1) NOT NULL COMMENT '交易类型，0：收入 1：退款',
  `discounts` decimal(20,2) DEFAULT NULL,
  `trade_no` varchar(32) DEFAULT NULL COMMENT '交易流水号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务交易对账明细表'
/*!50100 PARTITION BY RANGE columns (id)
(PARTITION p201712 VALUES LESS THAN ('RN201801') ENGINE = InnoDB,
 PARTITION p201801 VALUES LESS THAN ('RN201802') ENGINE = InnoDB,
 PARTITION p201802 VALUES LESS THAN ('RN201803') ENGINE = InnoDB,
 PARTITION p201803 VALUES LESS THAN ('RN201804') ENGINE = InnoDB,
 PARTITION p201804 VALUES LESS THAN ('RN201805') ENGINE = InnoDB,
 PARTITION p201805 VALUES LESS THAN ('RN201806') ENGINE = InnoDB,
 PARTITION p201806 VALUES LESS THAN ('RN201807') ENGINE = InnoDB,
 PARTITION p201807 VALUES LESS THAN ('RN201808') ENGINE = InnoDB,
 PARTITION p201808 VALUES LESS THAN ('RN201809') ENGINE = InnoDB,
 PARTITION p201809 VALUES LESS THAN ('RN201810') ENGINE = InnoDB,
 PARTITION p201810 VALUES LESS THAN ('RN201811') ENGINE = InnoDB,
 PARTITION p201811 VALUES LESS THAN ('RN201812') ENGINE = InnoDB,
 PARTITION p201812 VALUES LESS THAN ('RN201901') ENGINE = InnoDB,
 PARTITION p201901 VALUES LESS THAN ('RN201902') ENGINE = InnoDB,
 PARTITION p201902 VALUES LESS THAN ('RN201903') ENGINE = InnoDB,
 PARTITION p201903 VALUES LESS THAN ('RN201904') ENGINE = InnoDB,
 PARTITION p201904 VALUES LESS THAN ('RN201905') ENGINE = InnoDB,
 PARTITION p201905 VALUES LESS THAN ('RN201906') ENGINE = InnoDB,
 PARTITION p201906 VALUES LESS THAN ('RN201907') ENGINE = InnoDB,
 PARTITION p201907 VALUES LESS THAN ('RN201908') ENGINE = InnoDB,
 PARTITION p201908 VALUES LESS THAN ('RN201909') ENGINE = InnoDB,
 PARTITION p201909 VALUES LESS THAN ('RN201910') ENGINE = InnoDB,
 PARTITION p201910 VALUES LESS THAN ('RN201911') ENGINE = InnoDB,
 PARTITION p201911 VALUES LESS THAN ('RN201912') ENGINE = InnoDB,
 PARTITION p201912 VALUES LESS THAN ('RN202001') ENGINE = InnoDB,
 PARTITION p202001 VALUES LESS THAN ('RN202002') ENGINE = InnoDB,
 PARTITION p202002 VALUES LESS THAN ('RN202003') ENGINE = InnoDB,
 PARTITION p202003 VALUES LESS THAN ('RN202004') ENGINE = InnoDB,
 PARTITION p202004 VALUES LESS THAN ('RN202005') ENGINE = InnoDB,
 PARTITION p202005 VALUES LESS THAN ('RN202006') ENGINE = InnoDB,
 PARTITION p202006 VALUES LESS THAN ('RN202007') ENGINE = InnoDB,
 PARTITION p202007 VALUES LESS THAN ('RN202008') ENGINE = InnoDB)
*/;

insert into `sbc-account`.reconciliation_copy select * from  `sbc-account`.reconciliation;
update `sbc-account`.reconciliation_copy set id=concat('RN',DATE_FORMAT(trade_time,'%Y%m%d%H%i%s'),CEILING(RAND()*9000+1000));
drop table `sbc-account`.reconciliation;
rename table `sbc-account`.reconciliation_copy to `sbc-account`.reconciliation;

use `sbc-customer`;
DROP PROCEDURE IF EXISTS `create_partition_by_year_month`;

CREATE DEFINER=`root`@`%` PROCEDURE `create_partition_by_year_month`(IN_SCHEMANAME VARCHAR(64), IN_TABLENAME VARCHAR(64))
BEGIN
    DECLARE ROWS_CNT INT UNSIGNED;
    DECLARE BEGINTIME DATE;
    DECLARE ENDTIME INT UNSIGNED;
    DECLARE PARTITIONNAME VARCHAR(16);
    SET BEGINTIME = DATE(NOW());
    SET PARTITIONNAME = DATE_FORMAT( BEGINTIME, 'p%Y%m' );
    SET ENDTIME = DATE_FORMAT(DATE(BEGINTIME + INTERVAL 1 MONTH),'RN%Y%m');

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
END;
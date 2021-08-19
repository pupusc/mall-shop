#partition procedure
DELIMITER $$
#该表所在数据库名称
USE `statistics`$$
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
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_employee_performance`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','employee_performance');
END$$
DELIMITER ;


DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_employee_client`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','employee_client');
    END$$
    DELIMITER ;


DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_employee_performance_month`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','employee_performance_month');
END$$
DELIMITER ;


DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_employee_client_month`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','employee_client_month');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_grow`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','customer_grow');
END$$
DELIMITER ;


DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_level_distribute`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','customer_level_distribute');
END$$
DELIMITER ;


DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_area_distribute`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','customer_area_distribute');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_goods_day`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_year_month('statistics','goods_day');
END$$
DELIMITER ;


#partition procedure
DELIMITER $$
#该表所在数据库名称
USE `statistics`$$
DROP PROCEDURE IF EXISTS `create_partition_by_month`$$
CREATE PROCEDURE `create_partition_by_month`(IN_SCHEMANAME VARCHAR(64), IN_TABLENAME VARCHAR(64))
BEGIN
    DECLARE ROWS_CNT INT UNSIGNED;
    DECLARE BEGINTIME DATE;
    DECLARE ENDTIME INT UNSIGNED;
    DECLARE PARTITIONNAME VARCHAR(16);
    SET BEGINTIME = DATE(NOW());
    SET ENDTIME = DATE_FORMAT(DATE(BEGINTIME + INTERVAL 1 MONTH), '%Y%m' );
    SET PARTITIONNAME = CONCAT('p', DATE_FORMAT(BEGINTIME, '%Y%m' ));

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

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_goods_month`
    ON SCHEDULE EVERY 1 MINUTE
    STARTS now()
    ON COMPLETION PRESERVE
ENABLE
    COMMENT 'Creating partitions'
DO BEGIN

    #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
    CALL statistics.create_partition_by_month('statistics','goods_month');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_day`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL statistics.create_partition_by_year_month('statistics','customer_day');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_month`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL statistics.create_partition_by_year_month('statistics','customer_month');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_level_day`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL statistics.create_partition_by_year_month('statistics','customer_level_day');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_level_month`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL statistics.create_partition_by_year_month('statistics','customer_level_month');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_region_day`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL statistics.create_partition_by_year_month('statistics','customer_region_day');
END$$
DELIMITER ;

DELIMITER $$
USE `statistics`$$
CREATE EVENT IF NOT EXISTS `e_partition_customer_region_month`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL statistics.create_partition_by_year_month('statistics','customer_region_month');
END$$
DELIMITER ;



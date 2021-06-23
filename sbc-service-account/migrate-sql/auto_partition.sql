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
  CALL sbc-account.create_partition_by_year_month('sbc-account','reconciliation');
END$$
DELIMITER ;
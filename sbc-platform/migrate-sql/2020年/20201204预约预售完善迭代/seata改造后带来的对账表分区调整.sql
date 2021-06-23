/** seata改造带来的对账表分区调整，如果其他迭代已调整过，无需执行 **/
-- step1. id生成规则由原来的uuid改为RN+时间戳+4位随机数格式（yyyyMMddHHmmssSSS+xxxx）
   检查 com.wanmi.sbc.account.finance.record.service.AccountRecordService.add 方法，是否有重新setId，如果没有，增加设置代码

    @Transactional
    @GlobalTransactional
    public void add(Reconciliation reconciliation) {
        //重新生成id
        reconciliation.setId(generatorService.generateRNid());
        repository.save(reconciliation);
    }

     com.wanmi.sbc.common.util.GeneratorService.generateRNid 类新增生成方法

    /**
     * 生成财务对账明细id
     *
     * @return
     */
    public String generateRNid() {
        return "RN" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

-- step2. 扩大数据库表字段id的长度
ALTER TABLE `sbc-account`.`reconciliation` MODIFY COLUMN id VARCHAR(45) CHARACTER SET utf8mb4 NOT NULL;

-- step3.修改sbc-account表存储过程：create_partition_by_year_month为以下内容
BEGIN
    DECLARE ROWS_CNT INT UNSIGNED;
    DECLARE BEGINTIME DATE;
    DECLARE ENDTIME varchar(50);
    DECLARE PARTITIONNAME VARCHAR(16);
    SET BEGINTIME = DATE(NOW());
    SET PARTITIONNAME = DATE_FORMAT( BEGINTIME, 'p%Y%m' );
    SET ENDTIME = DATE_FORMAT(DATE(BEGINTIME + INTERVAL 1 MONTH),'RN%Y%m');

    SELECT COUNT(*) INTO ROWS_CNT FROM information_schema.partitions
  WHERE table_schema = IN_SCHEMANAME AND table_name = IN_TABLENAME AND partition_name = PARTITIONNAME;
    IF ROWS_CNT = 0 THEN
        SET @SQL = CONCAT( 'ALTER TABLE `', IN_SCHEMANAME, '`.`', IN_TABLENAME, '`',
      ' ADD PARTITION (PARTITION ', PARTITIONNAME, ' VALUES LESS THAN (\'', ENDTIME, '\') ENGINE = InnoDB);' );
        PREPARE STMT FROM @SQL;
        EXECUTE STMT;
        DEALLOCATE PREPARE STMT;
     ELSE
  SELECT CONCAT("partition `", PARTITIONNAME, "` for table `",IN_SCHEMANAME, ".", IN_TABLENAME, "` already exists") AS result;
     END IF;
END;

-- step4. 执行以下语句修改表分区，将原来的分区字段从trade_time改为id
ALTER TABLE `sbc-account`.`reconciliation` PARTITION BY RANGE COLUMNS(id)
(
PARTITION p202001 VALUES LESS THAN ('RN202002')  ENGINE = InnoDB,
PARTITION p202002 VALUES LESS THAN ('RN202003')  ENGINE = InnoDB,
PARTITION p202003 VALUES LESS THAN ('RN202004')  ENGINE = InnoDB,
PARTITION p202004 VALUES LESS THAN ('RN202005')  ENGINE = InnoDB,
PARTITION p202005 VALUES LESS THAN ('RN202006')  ENGINE = InnoDB,
PARTITION p202006 VALUES LESS THAN ('RN202007')  ENGINE = InnoDB,
PARTITION p202007 VALUES LESS THAN ('RN202008')  ENGINE = InnoDB,
PARTITION p202008 VALUES LESS THAN ('RN202009')  ENGINE = InnoDB,
PARTITION p202009 VALUES LESS THAN ('RN202010')  ENGINE = InnoDB,
PARTITION p202010 VALUES LESS THAN ('RN202011')  ENGINE = InnoDB,
PARTITION p202011 VALUES LESS THAN ('RN202012')  ENGINE = InnoDB
);

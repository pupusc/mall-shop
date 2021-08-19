------------------------------------------------account-------------------------------------------
-- 结算单表
ALTER TABLE settlement ADD split_pay_price decimal(20,2) NULL COMMENT '商品实付总额' AFTER `delivery_price`;
ALTER TABLE settlement ADD common_coupon_price decimal(20,2) NULL COMMENT '通用券优惠总额' AFTER `split_pay_price`;
ALTER TABLE settlement ADD point_price decimal(20,2) NULL COMMENT '积分抵扣总额' AFTER `common_coupon_price`;
ALTER TABLE settlement ADD commission_price decimal(20,2) NULL COMMENT '分销佣金总额' AFTER `point_price`;
ALTER TABLE settlement ADD settle_time datetime NULL COMMENT '结算时间' AFTER `commission_price`;

-- 清空会员资金、余额明细、提现表（慎用）
DELETE FROM customer_funds;
DELETE FROM customer_funds_detail;
DELETE FROM customer_draw_cash;


ALTER TABLE `customer_funds_detail`
ADD COLUMN `sub_type` tinyint(4) NOT NULL COMMENT '资金子类型 \'1\'：推广返利；\'2\'：佣金提现；\'3\'：邀新奖励；\'4\':自购返利' AFTER `update_time`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`customer_funds_detail_id`) USING BTREE;
update customer_funds_detail set sub_type = funds_type where  sub_type is null;

-- 提现记录表新增字段
alter table customer_draw_cash add
(
	open_id varchar(32) null comment '微信openId',
	draw_cash_source tinyint(4) null comment '微信openId来源 0:PC 1:MOBILE 2:App'
);



-----------------------------------------------customer-------------------------------------------
-- 清空邀新记录、分销员、邀新记录任务表（慎用），更新所有用户为小C
UPDATE customer_detail set is_distributor = 0;
DELETE FROM invite_new_record;
DELETE FROM distribution_customer;
-- 邀新记录表
ALTER TABLE `invite_new_record` ADD COLUMN `record_id` varchar(32) COMMENT '邀新记录ID,主键' AFTER `invited_customer_id`;
ALTER TABLE `invite_new_record` ADD COLUMN `fail_reason_flag` tinyint(4) NULL COMMENT '未入账原因,0:非有效邀新，1：奖励达到上限' AFTER `distributor`;
ALTER TABLE `invite_new_record` ADD COLUMN `reward_flag` tinyint(4) NULL COMMENT '入账类型, 0:现金，1：优惠券' AFTER `fail_reason_flag`;
ALTER TABLE `invite_new_record` ADD COLUMN `reward_coupon` VARCHAR (255) NULL COMMENT '入账的优惠券名称，逗号分隔' AFTER  `reward_flag`;
ALTER TABLE `invite_new_record` DROP PRIMARY KEY;
ALTER TABLE `invite_new_record` ADD PRIMARY KEY PK_invite_new_record(`record_id`);
ALTER TABLE `invite_new_record` MODIFY COLUMN `record_id`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邀新记录ID,主键' FIRST ;
ALTER TABLE `invite_new_record` MODIFY COLUMN `fail_reason_flag`  tinyint(4) NULL DEFAULT NULL COMMENT '未入账原因,0:非有效邀新，1：奖励达到上限，2：奖励未开启' AFTER `distributor`;

ALTER TABLE `invite_new_record` MODIFY COLUMN `fail_reason_flag`  tinyint(4) NULL DEFAULT NULL COMMENT '未入账原因,0:非有效邀新，1：奖励达到上限，2：奖励未开启' AFTER `distributor`;

ALTER TABLE `invite_new_record` ADD COLUMN `setting_coupon_ids_counts`  varchar(350) NULL COMMENT '优惠券组集合信息（id：num）' AFTER `setting_coupons`;

ALTER TABLE `invite_new_record` MODIFY COLUMN `setting_coupons`  varchar(350) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '后台配置的奖励优惠券名称，多个以逗号分隔' AFTER `reward_cash`;

ALTER TABLE `invite_new_record` MODIFY COLUMN `setting_coupon_ids_counts`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '优惠券组集合信息（id：num）' AFTER `setting_coupons`;

-- 第三方绑定关系表
ALTER TABLE `third_login_relation` MODIFY COLUMN `nick_name`  varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '微信昵称' AFTER `binding_time`;
ALTER TABLE `third_login_relation` DROP INDEX `third_login_relation_third_login_uid_uindex`,ADD INDEX `third_login_relation_third_login_uid_uindex`(`third_login_uid`) USING BTREE;



-- ----------------------------
--  分销员日分销业绩记录
-- ----------------------------
DROP TABLE IF EXISTS `distribution_performance_day`;
CREATE TABLE `distribution_performance_day` (
  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
  `distribution_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '分销员id',
  `customer_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '会员id',
  `sale_amount` decimal(18,2) NOT NULL COMMENT '销售额',
  `commission` decimal(18,2) NOT NULL COMMENT '收益',
  `target_date` date NOT NULL COMMENT '统计的目标日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
  PRIMARY KEY (`id`,`target_date`),
  UNIQUE KEY `idx_distribution_id_target_date` (`distribution_id`,`target_date`),
  KEY `idx_distribution_id` (`distribution_id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='分销员日业绩记录'
/*!50100 PARTITION BY RANGE (TO_DAYS(target_date))
(PARTITION p201904 VALUES LESS THAN (737545) ENGINE = InnoDB,
 PARTITION p201905 VALUES LESS THAN (737576) ENGINE = InnoDB); */

-- ----------------------------
--  分销员月分销业绩记录
-- ----------------------------
DROP TABLE IF EXISTS `distribution_performance_month`;
CREATE TABLE `distribution_performance_month` (
  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
  `distribution_id` varchar(32) NOT NULL COMMENT '分销员id',
  `customer_id` varchar(32) NOT NULL COMMENT '会员id',
  `sale_amount` decimal(18,2) NOT NULL COMMENT '销售额',
  `commission` decimal(18,2) NOT NULL COMMENT '收益',
  `target_date` date NOT NULL COMMENT '统计的目标日期，月报表中记录的是该月最后一天，用于分区',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
  PRIMARY KEY (`id`,`target_date`),
  UNIQUE KEY `idx_distribution_id_target_date` (`distribution_id`,`target_date`) USING BTREE,
  KEY `idx_distribution_id` (`distribution_id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='分销员月业绩记录,该表为定时任务从日表中聚合生成'
/*!50100 PARTITION BY RANGE (TO_DAYS(target_date))
(PARTITION p201904 VALUES LESS THAN (737545) ENGINE = InnoDB,
 PARTITION p201905 VALUES LESS THAN (737576) ENGINE = InnoDB); */

-- ----------------------------
--  业绩记录表自动分区存储过程
-- ----------------------------
#partition procedure
DELIMITER $$
#该表所在数据库名称
USE `sbc-customer`$$
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

-- ----------------------------
--  定时任务调用分区过程
-- ----------------------------
-- 分销业绩日表
DELIMITER $$
######### employee
USE `sbc-customer`$$
CREATE EVENT IF NOT EXISTS `e_partition_distribution_performance_day`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating distribution_performance_day partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL create_partition_by_year_month('sbc-customer','distribution_performance_day');
END$$
DELIMITER ;

-- 分销业绩日表
DELIMITER $$
######### employee
USE `sbc-customer`$$
CREATE EVENT IF NOT EXISTS `e_partition_distribution_performance_month`
  ON SCHEDULE EVERY 1 MINUTE
  STARTS now()
  ON COMPLETION PRESERVE
ENABLE
  COMMENT 'Creating distribution_performance_month partitions'
DO BEGIN

  #调用刚才创建的存储过程，第一个参数是数据库名称，第二个参数是表名称
  CALL create_partition_by_year_month('sbc-customer','distribution_performance_month');
END$$
DELIMITER ;

-- 绑定关系表
ALTER TABLE `third_login_relation` ADD COLUMN `del_flag` tinyint(4) DEFAULT 0 COMMENT '解绑状态, 1:已解绑，0：未解绑' AFTER `head_img_url`;


-- 会员邀新信息表
DROP TABLE IF EXISTS `distribution_customer_invite_info`;
CREATE TABLE `distribution_customer_invite_info`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `distribution_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分销员标识UUID',
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员ID',
  `reward_cash_count` int(11) NULL DEFAULT 0 COMMENT '已发放邀新奖励现金人数 ',
  `reward_cash_limit_count` int(11) NULL DEFAULT 0 COMMENT '达到上限未发放奖励现金人数 ',
  `reward_cash_available_limit_count` int(11) NULL DEFAULT 0 COMMENT '达到上限未发放奖励现金有效邀新人数 ',
  `reward_coupon_count` int(11) NULL DEFAULT 0 COMMENT '已发放邀新奖励优惠券数',
  `reward_coupon_limit_count` int(11) NULL DEFAULT 0 COMMENT '达到上限未发放奖励优惠券数',
  `reward_coupon_available_limit_count` int(11) NULL DEFAULT 0 COMMENT '达到上限未发放奖励优惠券有效邀新人数 ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员邀新信息表' ROW_FORMAT = Compact;
-- 排行榜
DROP TABLE IF EXISTS `distribution_customer_ranking`;
CREATE TABLE `distribution_customer_ranking`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `customer_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会员ID',
  `invite_count` int(11) NULL DEFAULT 0 COMMENT '邀新人数',
  `invite_available_count` int(11) NULL DEFAULT 0 COMMENT '有效邀新人数',
  `sale_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '销售额(元) ',
  `commission` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '预估收益',
  `target_date` date NOT NULL COMMENT '统计的目标日期',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分销员排行榜' ROW_FORMAT = Compact;
-- invite_new_record索引
ALTER TABLE `invite_new_record`
ADD INDEX `index_register_time`(`register_time`) USING BTREE,
ADD INDEX `index_request_customer_id`(`request_customer_id`) USING BTREE,
ADD INDEX `index_invited_customer_id`(`invited_customer_id`) USING BTREE;
-- distribution_customer_ranking
ALTER TABLE `distribution_customer_ranking`
ADD UNIQUE INDEX `id_ranking_customer_id_target_date`(`customer_id`, `target_date`) USING BTREE;


------------------------------------------------order-----------------------------------------------
-- 消费记录表
ALTER TABLE `consume_record` ADD COLUMN `head_img` varchar(255) NULL COMMENT '客户头像' AFTER `customer_name`;

------------------------------------------------goods-----------------------------------------------
-- 商品表
ALTER TABLE `goods_info` ADD COLUMN `commission_rate` decimal(4,2) DEFAULT NULL  COMMENT '佣金比例' AFTER `cate_id`;

DELETE FROM distributor_goods_info;


------------------------------------------------marketing-----------------------------------------------
-- 清空分销记录表（慎用）
DELETE FROM distribution_record;
-- 优惠券活动表
ALTER TABLE coupon_activity MODIFY activity_type tinyint(4) NOT NULL COMMENT '优惠券类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券，4权益赠券，5分销邀新赠券';

-- 分销记录表
-- 新增分销佣金比例字段
alter table distribution_record add column commission_rate decimal(4,2) comment '分销佣金比例' after commission_goods;

ALTER TABLE `distribution_store_setting` ADD COLUMN `commission_rate` decimal(4,2) DEFAULT 0 COMMENT '通用的佣金比例' AFTER `commission`;

-- 分销设置表
-- 新增招募入口海报页字段，邀新入口海报页字段
ALTER TABLE `distribution_setting`  ADD COLUMN `buy_recruit_enter_img` VARCHAR(255) DEFAULT NULL COMMENT '购买商品时招募入口海报' AFTER `apply_type`;
ALTER TABLE `distribution_setting`  ADD COLUMN `invite_recruit_enter_img` VARCHAR(255) DEFAULT NULL COMMENT '邀请注册时招募入口海报' AFTER `buy_recruit_enter_img`;
ALTER TABLE `distribution_setting`  ADD COLUMN `invite_recruit_img` VARCHAR(255) DEFAULT NULL COMMENT '邀请注册时招募落地页海报' AFTER `invite_recruit_enter_img`;
ALTER TABLE `distribution_setting` MODIFY COLUMN `recruit_img` VARCHAR(255)  COMMENT '购买商品时招募落地页海报' AFTER `invite_recruit_img`;
ALTER TABLE `distribution_setting`  ADD COLUMN `invite_enter_img` VARCHAR(255) DEFAULT NULL COMMENT '邀新入口海报' AFTER `recruit_img`;
-- 新增分销业绩规则说明字段
ALTER TABLE `distribution_setting`  ADD COLUMN `performance_desc` text DEFAULT NULL COMMENT '分销业绩规则说明' AFTER `reward_coupon_count`;
-- 修改部分字段为可空
ALTER TABLE distribution_setting MODIFY shop_name varchar(20) COMMENT '小店名称';
ALTER TABLE distribution_setting MODIFY shop_share_img varchar(255) COMMENT '店铺分享图片';
ALTER TABLE distribution_setting MODIFY apply_type tinyint(4) COMMENT '申请条件 0：购买商品，1：邀请注册';
ALTER TABLE distribution_setting MODIFY recruit_desc text COMMENT '招募规则说明';
ALTER TABLE distribution_setting MODIFY limit_type tinyint(4) COMMENT '限制条件 0：不限，1：仅限有效邀新';
ALTER TABLE distribution_setting MODIFY invite_img varchar(255) COMMENT '邀新专题页海报';
ALTER TABLE distribution_setting MODIFY invite_share_img varchar(255) COMMENT '邀新转发图片';
ALTER TABLE distribution_setting MODIFY invite_desc text COMMENT '邀新奖励规则说明';
ALTER TABLE distribution_setting MODIFY reward_cash_flag tinyint(4) COMMENT '是否开启奖励现金 0：关闭，1：开启';
ALTER TABLE distribution_setting MODIFY reward_cash decimal(10,2) COMMENT '每位奖励金额';
ALTER TABLE distribution_setting MODIFY reward_coupon_flag tinyint(4) COMMENT '是否开启奖励优惠券 0：关闭，1：开启';
ALTER TABLE distribution_setting MODIFY reward_coupon_count int(11) COMMENT '奖励优惠券上限(组数)';
-- 清空之前的数据，在代码中初始化
truncate TABLE distribution_setting;
truncate TABLE distribution_recruit_gift;
truncate TABLE distribution_reward_coupon;

ALTER TABLE `distribution_record`
MODIFY COLUMN `order_goods_price`  decimal(20,2) NULL DEFAULT NULL COMMENT '订单商品总金额' AFTER `mission_received_time`,
MODIFY COLUMN `commission_goods`  decimal(20,2) NULL DEFAULT NULL COMMENT '货品的总佣金' AFTER `order_goods_count`;








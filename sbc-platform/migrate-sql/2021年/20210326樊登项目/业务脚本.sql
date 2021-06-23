ALTER TABLE  `sbc-goods`.`goods_info`
ADD COLUMN `erp_goods_info_no`  varchar(45) NULL COMMENT 'ERP商品编码',
ADD COLUMN `virtual_coupon_id`  tinyint(4) NULL COMMENT '电子卡券id';

ALTER TABLE `sbc-goods`.`goods_info`
    ADD COLUMN `virtual_coupon_id` bigint(20) NULL COMMENT '电子卡券id';

ALTER TABLE `sbc-goods`.`goods`
    MODIFY COLUMN `goods_type` tinyint(1) COMMENT '商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品';


-- 电子卡券表
DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon`;
CREATE TABLE `sbc-goods`.`virtual_coupon`
(
    `id`             bigint(11)  NOT NULL AUTO_INCREMENT COMMENT '电子卡券ID',
    `store_id`       bigint(20)  NOT NULL COMMENT '店铺标识',
    `name`           varchar(50) NOT NULL COMMENT '名称',
    `sum_number`     int(11)              DEFAULT '0' COMMENT '总数量',
    `saled_number`   int(11)              DEFAULT '0' COMMENT '已售总数量',
    `provide_type`   tinyint(2)  NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `publish_status` tinyint(1)  NOT NULL DEFAULT '0' COMMENT '0:未发布 1:已发布',
    `sku_id`         varchar(32)          DEFAULT NULL COMMENT '关联的skuId',
    `description`    varchar(60)          DEFAULT NULL COMMENT '备注',
    `del_flag`       tinyint(1)  NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`    datetime    NOT NULL COMMENT '创建时间',
    `create_person`  varchar(32) NOT NULL COMMENT '创建人',
    `update_time`    datetime    NOT NULL COMMENT '更新时间',
    `update_person`  varchar(32) NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_id` (`id`),
    KEY `idx_store_id` (`store_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='电子卡券表;';
-- 电子券码表

-- 券码表
DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code` (
                                       `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '券码ID',
                                       `coupon_id` bigint(11) NOT NULL COMMENT '电子卡券ID',
                                       `batch_no` varchar(32) NOT NULL COMMENT '批次号',
                                       `valid_days` int(11) NOT NULL COMMENT '有效期',
                                       `provide_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
                                       `coupon_no` varchar(150) NOT NULL COMMENT '兑换码/券码/链接',
                                       `coupon_secret` varchar(50) DEFAULT NULL COMMENT '密钥',
                                       `receive_end_time` datetime DEFAULT NULL COMMENT '领取结束时间',
                                       `exchange_start_time` datetime DEFAULT NULL COMMENT '兑换开始时间',
                                       `exchange_end_time` datetime DEFAULT NULL COMMENT '兑换结束时间',
                                       `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
                                       `tid` varchar(32) DEFAULT NULL COMMENT '订单号',
                                       `del_flag` tinyint(1) NOT NULL COMMENT '删除标识;0:未删除1:已删除',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `create_person` varchar(32) NOT NULL COMMENT '创建人',
                                       `update_time` datetime NOT NULL COMMENT '更新时间',
                                       `update_person` varchar(32) NOT NULL COMMENT '更新人',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE KEY `idx_coupon_no` (`coupon_no`),
                                       KEY `idx_coupon_code_id` (`id`) USING BTREE,
                                       KEY `idx_batch_no` (`batch_no`),
                                       KEY `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电子券码表;';

DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_0`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_0`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';

DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_1`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_1`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_2`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_2`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_3`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_3`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_4`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_4`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_5`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_5`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_6`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_6`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_7`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_7`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_8`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_8`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';
  DROP TABLE IF EXISTS `sbc-goods`.`virtual_coupon_code_9`;
CREATE TABLE `sbc-goods`.`virtual_coupon_code_9`
(
    `id`                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '券码ID',
    `coupon_id`           BIGINT(11)   NOT NULL COMMENT '电子卡券ID',
    `batch_no`            VARCHAR(32)  NOT NULL COMMENT '批次号',
    `valid_days`          INT(11)      COMMENT '有效期',
    `provide_type`        TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:兑换码 1:券码+密钥 2:链接',
    `coupon_no`           VARCHAR(150) NOT NULL COMMENT '兑换码/券码/链接',
    `coupon_secret`       VARCHAR(50)           DEFAULT NULL COMMENT '密钥',
    `receive_end_time`    datetime              DEFAULT NULL COMMENT '领取结束时间',
    `exchange_start_time` datetime              DEFAULT NULL COMMENT '兑换开始时间',
    `exchange_end_time`   datetime              DEFAULT NULL COMMENT '兑换结束时间',
    `status`              TINYINT(2)   NOT NULL DEFAULT '0' COMMENT '0:未发放 1:已发放 2:已过期',
    `tid`                 VARCHAR(32)           DEFAULT NULL COMMENT '订单号',
    `del_flag`            TINYINT(1)   NOT NULL COMMENT '删除标识;0:未删除1:已删除',
    `create_time`         datetime     NOT NULL COMMENT '创建时间',
    `create_person`       VARCHAR(32)  NOT NULL COMMENT '创建人',
    `update_time`         datetime     NOT NULL COMMENT '更新时间',
    `update_person`       VARCHAR(32)  NOT NULL COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_coupon_code_id` (`id`) USING BTREE,
    KEY `idx_batch_no` (`batch_no`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT = '电子券码表;';


-- 周期购活动主表
DROP TABLE IF EXISTS `sbc-goods`.`cycle_buy`;
CREATE TABLE `sbc-goods`.`cycle_buy`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '周期购Id',
    `origin_goods_id`    varchar(36)  NOT NULL COMMENT '关联商品Id',
    `goods_id`           varchar(36)  NOT NULL COMMENT '创建商品ID',
    `activity_name`      varchar(40)  NOT NULL COMMENT '周期购活动名称',
    `record_activities`  varchar(255) COMMENT '活动文案',
    `delivery_plan`      tinyint(1)   NOT NULL COMMENT '配送方案 0:商家主导配送 1:客户主导配送',
    `gift_give_method`   tinyint(1)  DEFAULT NULL COMMENT '赠送方式：0：默认全送 1:可选一种',
    `delivery_cycle`     tinyint(1)   NOT NULL COMMENT '配送周期 0:每日一期 1:每周一期 2:每月一期',
    `send_date_rule`     varchar(100) NOT NULL COMMENT '发货日期:a.配送周期是每日一期：1：每日送达 2.工作日送达  3.周末每天送达；b.配送周期每周一期：1-7对应 周一  --  周日；c.配送周期每月一期：取选中的日期；多个日期使用逗号拼接',
    `cycle_freight_type` tinyint(3)   NOT NULL COMMENT '周期购运费 =0:每期运费x期数， >0:满X期包邮',
    `added_flag`         tinyint(1)   NOT NULL COMMENT '上下架 0：下架 1:上架',
    `store_id`           bigint(20)  DEFAULT NULL COMMENT '商铺Id',
    `del_flag`           tinyint(1)   NOT NULL COMMENT '删除标记  0：正常，1：删除',
    `create_time`        datetime    DEFAULT NULL COMMENT '创建时间',
    `create_person`      varchar(36) DEFAULT NULL,
    `update_time`        datetime    DEFAULT NULL COMMENT '修改时间',
    `update_person`      varchar(36) DEFAULT NULL,
    `delete_time`        datetime    DEFAULT NULL COMMENT '删除时间',
    `delete_person`      varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_store_id` (`store_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='周期购活动表';

-- 周期购活动赠品表
DROP TABLE IF EXISTS `sbc-goods`.`cycle_buy_gift`;
CREATE TABLE `sbc-goods`.`cycle_buy_gift`
(
    `id`            bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '周期购赠品表Id',
    `cycle_buy_id`  bigint(20)  NOT NULL COMMENT '关联周期购主键Id',
    `goods_info_id` varchar(36) NOT NULL COMMENT '赠送商品Id',
    `free_quantity` bigint(11)  NOT NULL COMMENT '赠品数量',
    PRIMARY KEY (`id`),
    KEY `idx_cycle_buy_id` (`cycle_buy_id`),
    KEY `idx_goods_info_id` (`goods_info_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='周期购赠品表';

-- goods_info 表新增 cycle_num，goods_type字段
ALTER TABLE `sbc-goods`.`goods_info`
    ADD COLUMN cycle_num bigint(20) DEFAULT NULL COMMENT '周期购商品期数';
ALTER TABLE `sbc-goods`.`goods_info`
    ADD COLUMN goods_type tinyint(1) DEFAULT '0' COMMENT '商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品';
-- sbc-message
-- 短信模板
INSERT INTO `sbc-message`.`sms_template`(`id`, `template_name`, `template_content`, `remark`, `template_type`,
                                         `review_status`, `template_code`, `review_reason`, `sms_setting_id`,
                                         `del_flag`, `create_time`, `business_type`, `purpose`, `sign_id`, `open_flag`)
VALUES (178, '电子卡券券码类型购买成功信息模板', '恭喜您成功购买电子卡券，你购买的卡券券码是：${code}，请勿泄露给他人。', '电子卡券券码类型购买成功信息模板', 1, 1, 'SMS_210074743',
        NULL, 1, 0, '2021-01-27 11:07:18', 'VIRTUAL_COUPON_CODE', '电子卡券券码类型购买成功信息模板', 48, 1);
INSERT INTO `sbc-message`.`sms_template`(`id`, `template_name`, `template_content`, `remark`, `template_type`,
                                         `review_status`, `template_code`, `review_reason`, `sms_setting_id`,
                                         `del_flag`, `create_time`, `business_type`, `purpose`, `sign_id`, `open_flag`)
VALUES (179, '电子卡券券码+密码类型购买成功信息模板', '恭喜您成功购买电子卡券，你购买的卡券券码是：${code} ，密码是：${password}，请勿泄露给他人。', '电子卡券券码+密码类型购买成功信息模板', 1,
        1, 'SMS_210064856', NULL, 1, 0, '2021-01-27 11:07:18', 'VIRTUAL_COUPON_CODE_PASSWORD', '电子卡券券码+密码类型购买成功信息模板',
        48, 1);

-- 付费会员相关
DROP TABLE IF EXISTS `sbc-customer`.`paid_card`;
CREATE TABLE `sbc-customer`.`paid_card`
(
    `id`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主键',
    `name`          varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '名称',
    `background`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址',
    `icon`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费会员图标',
    `discount_rate` double(4, 0)                                                  NOT NULL COMMENT '折扣率',
    `rule`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '规则说明',
    `agreement`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '付费会员用户协议',
    `del_flag`      tinyint(1)                                                    NOT NULL COMMENT '删除标识 1：删除；0：未删除',
    `create_time`   datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`   datetime(0)                                                   NOT NULL COMMENT '更新时间',
    `enable`        tinyint(1)                                                    NOT NULL COMMENT '启动禁用标识 1：启用；2：禁用',
    `create_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '创建人ID',
    `update_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '修改人ID',
    `bg_type`       tinyint(1)                                                    NOT NULL COMMENT '背景类型0背景色；1背景图片',
    `text_color`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '前景色',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '付费会员类型表'
  ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sbc-customer`.`paid_card_buy_record`;
CREATE TABLE `sbc-customer`.`paid_card_buy_record`
(
    `pay_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流水号',
    `customer_id`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `customer_name`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
    `create_time`      datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `card_no`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡号',
    `paid_member_id`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费会员类型id',
    `paid_member_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费会员类型名称',
    `rule_id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费周期id',
    `rule_name`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费周期名称',
    `price`            decimal(10, 2)                                               NOT NULL COMMENT '价格',
    `type`             tinyint(1)                                                   NOT NULL COMMENT '购买类型 0：购买 1：续费',
    `customer_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
    `invalid_time`     datetime(0)                                                  NOT NULL COMMENT '失效时间',
    PRIMARY KEY (`pay_code`) USING BTREE,
    INDEX `idx_customer_time` (`customer_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '付费会员购买记录'
  ROW_FORMAT = Compact;
DROP TABLE IF EXISTS `sbc-customer`.`paid_card_customer_rel`;
CREATE TABLE `sbc-customer`.`paid_card_customer_rel`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `customer_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员id',
  `paid_card_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费会员类型ID',
  `begin_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `del_flag` tinyint(1) NOT NULL COMMENT '状态： 0：未删除 1：删除',
  `paid_source` tinyint(1) NOT NULL COMMENT '状态： 0：樊登同步的会员 1：商城的会员',
  `card_no` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_customer_id`(`customer_id`) USING BTREE,
  INDEX `idx_end_time`(`end_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '付费会员和用户的关系表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sbc-customer`.`paid_card_rights_rel`;
CREATE TABLE `sbc-customer`.`paid_card_rights_rel`
(
    `id`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `paid_card_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属会员权益id',
    `rights_id`    int(10)                                                      NOT NULL COMMENT '权益id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_paid_card_id` (`paid_card_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '付费会员类型会员权益关联表'
  ROW_FORMAT = Compact;
DROP TABLE IF EXISTS `sbc-customer`.`paid_card_rule`;
CREATE TABLE `sbc-customer`.`paid_card_rule`
(
    `id`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `type`         tinyint(1)                                                   NOT NULL COMMENT '配置类型 0：付费配置；1：续费配置',
    `name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `price`        decimal(10, 2)                                               NOT NULL COMMENT '价格',
    `status`       tinyint(1)                                                   NOT NULL COMMENT '0:禁用；1：启用',
    `paid_card_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费会员类型id',
    `time_unit`    tinyint(1)                                                   NOT NULL COMMENT '时间单位：0天，1月，2年',
    `time_val`     tinyint(3)                                                   NOT NULL COMMENT '时间（数值）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '付费会员付费续费配置表'
  ROW_FORMAT = Compact;

ALTER TABLE `sbc-customer`.`paid_card_buy_record`
CHANGE COLUMN `paid_member_id` `paid_card_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费会员类型id' AFTER `card_no`,
CHANGE COLUMN `paid_member_name` `paid_card_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '付费会员类型名称' AFTER `paid_card_id`;

ALTER TABLE `sbc-customer`.`paid_card_buy_record`
ADD COLUMN `customer_paidcard_id` varchar(32) NOT NULL COMMENT '付费卡实例ID' AFTER `invalid_time`;
ALTER TABLE `sbc-customer`.`paid_card_buy_record`
ADD COLUMN `begin_time` datetime(0) NOT NULL COMMENT '开始时间';

ALTER TABLE `sbc-customer`.`paid_card_customer_rel`
ADD COLUMN `send_msg_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0:未发送 1：已发送' AFTER `card_no`;

-- 加价购
DROP TABLE IF EXISTS `sbc-marketing`.`markup_level`;
CREATE TABLE `sbc-marketing`.`markup_level` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '加价购阶梯id',
  `markup_id` bigint(20) DEFAULT NULL COMMENT '加价购id',
  `level_amount` decimal(20,2) DEFAULT NULL COMMENT '加价购阶梯满足金额',
  PRIMARY KEY (`id`),
  KEY `idx_markup_id` (`markup_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='加价购阶梯表';

DROP TABLE IF EXISTS `sbc-marketing`.`markup_level_detail`;
CREATE TABLE `sbc-marketing`.`markup_level_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '加价购阶梯详情id',
  `markup_id` bigint(20) NOT NULL COMMENT '加价购活动关联id',
  `markup_level_id` bigint(20) NOT NULL COMMENT '加价购阶梯关联id',
  `markup_price` decimal(20,2) DEFAULT NULL COMMENT '加购商品加购价格',
  `goods_info_id` varchar(32) DEFAULT NULL COMMENT '加购商品关联sku ',
  PRIMARY KEY (`id`),
  KEY `idx_markup_id` (`markup_id`),
  KEY `idx_markup_level_id` (`markup_level_id`),
  KEY `idx_sku_id` (`goods_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='加价购阶梯详情表';

ALTER TABLE `sbc-marketing`.`marketing`
MODIFY COLUMN `marketing_type` tinyint(4) NOT NULL COMMENT '促销类型 0：满减 1:满折 2:满赠 3一口价优惠 4第二件半价 5秒杀(无用) 6组合套餐 7加价购' AFTER `marketing_name`,
MODIFY COLUMN `sub_type` tinyint(4) NOT NULL COMMENT '促销子类型 0:满金额减 1:满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠 6:一口价 7:第二件半价 8:组合商品 9：加价购' AFTER `marketing_type`;
-- 企业购
ALTER TABLE `sbc-goods`.`goods_info`
    ADD COLUMN `enterprise_price_type` TINYINT ( 1 ) NULL DEFAULT 0 COMMENT '设价类型,0:按市场价 1:按会员等级设价 2:按购买数量设价' AFTER `goods_type`,
    ADD COLUMN `enterprise_discount_flag` TINYINT ( 1 ) NULL DEFAULT 0 COMMENT '以折扣设价 0:否 1:是' AFTER `enterprise_price_type`,
    ADD COLUMN `enterprise_customer_flag` TINYINT ( 1 ) NULL DEFAULT 0 COMMENT '按客户单独定价,0:否 1:是' AFTER `enterprise_discount_flag`;

ALTER TABLE `sbc-goods`.`goods_customer_price`
    ADD COLUMN `discount` int(3) NULL DEFAULT 1 COMMENT '折扣(0,100)' AFTER `customer_id`,
    MODIFY COLUMN `type` tinyint(4) NOT NULL COMMENT '0:spu,1:sku,2:企业购sku' AFTER `goods_info_id`;

ALTER TABLE `sbc-goods`.`goods_interval_price`
    ADD COLUMN `discount` int(3) NULL DEFAULT 100 COMMENT '折扣(0,100)' AFTER `count`,
    MODIFY COLUMN `type` tinyint(4) NOT NULL COMMENT '0:spu,1:sku,2:企业购sku' AFTER `goods_info_id`;

ALTER TABLE `sbc-goods`.`goods_level_price`
    ADD COLUMN `discount` int(3) NULL COMMENT '折扣(0,' ||
     '100)' AFTER `level_id`,
    MODIFY COLUMN `type` tinyint(4) NOT NULL COMMENT '0:spu,1:sku,2:企业购sku' AFTER `goods_info_id`;

ALTER TABLE `sbc-customer`.`paid_card_customer_rel`
ADD COLUMN `paid_source` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态： 0：樊登同步的会员 1：商城的会员' AFTER `send_msg_flag`;


-- 营销参加会员说明
ALTER TABLE `sbc-marketing`.`marketing`
MODIFY COLUMN `join_level` varchar(500) NOT NULL COMMENT '参加会员 -5:付费会员 -4:企业会员 -1:全部客户 0:全部等级 other:其他等级';
ALTER TABLE `sbc-goods`.`appointment_sale`
MODIFY COLUMN `join_level` varchar(500) NOT NULL COMMENT '参加会员 -3:企业会员 -2:付费会员 -1:全部客户 0:全部等级 other:其他等级';
ALTER TABLE `sbc-goods`.`booking_sale`
MODIFY COLUMN `join_level` varchar(500) NOT NULL COMMENT '参加会员 -3:企业会员 -2:付费会员 -1:全部客户 0:全部等级 other:其他等级';

-- 付费会员实例增加过期短信发送标识
ALTER TABLE `sbc-customer`.`paid_card_customer_rel`
ADD COLUMN `send_expire_msg_flag` tinyint(1) NOT NULL COMMENT '发送过期短信标识 0:未发送 1：已发送' AFTER `send_msg_flag`;

ALTER TABLE `sbc-customer`.`paid_card`
MODIFY COLUMN `discount_rate` decimal(2, 2) NOT NULL COMMENT '折扣率' AFTER `icon`;


--------- 新建数据库 sbc-erp
CREATE DATABASE
IF
  NOT EXISTS `sbc-erp` DEFAULT CHARACTER
  SET utf8mb4 COLLATE utf8mb4_general_ci;

ALTER TABLE  `sbc-customer`.`customer`
ADD COLUMN `fandeng_user_no`  varchar(32) NULL COMMENT '樊登会员id';

-- 樊登付费会员映射商城付费会员
DROP TABLE IF EXISTS `sbc-customer`.`fd_paid_cast`;
CREATE TABLE `sbc-customer`.`fd_paid_cast` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '樊登付费类型 映射商城付费类型主键',
    `fd_pay_type` tinyint(1) NOT NULL COMMENT '樊登付费会员类型',
    `paid_card_id` varchar(32)  NOT NULL COMMENT '商城付费会员类型id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
    `create_person` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
    `update_person` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
    `del_flag` tinyint(1) NOT NULL COMMENT '删除标记  0：正常，1：删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='樊登付费类型 映射商城付费类型表';


ALTER TABLE `sbc-customer`.`customer`
ADD INDEX `idx_fd_user_no` (`fandeng_user_no`);

-- 付费卡表增加spu编码
ALTER TABLE `sbc-customer`.`paid_card`
ADD COLUMN `erp_spu_code` varchar(20) NULL COMMENT 'erp SPU编码' AFTER `text_color`;

ALTER TABLE `sbc-customer`.`paid_card`
ADD COLUMN `access_type` tinyint(4) NOT NULL COMMENT '获取方式 0：用户购买 1：有赞同步' AFTER `erp_spu_code`;

-- 卡券购买通知 app push
INSERT INTO `sbc-message`.`push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (42, '卡券密码电子卡券购买成功通知', 2, 'VIRTUAL_COUPON_CODE_PASSWORD', '卡券密码电子卡券购买成功通知', '恭喜您成功购买电子卡券，您购买的卡券券码是：{券码}，密码是：{****}，请勿泄露给他人，戳链接查看{订单详情链接}。', 0, 0, 0, 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sbc-message`.`push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (43, '兑换码电子卡券购买成功通知', 2, 'VIRTUAL_COUPON_CODE', '兑换码电子卡券购买成功通知', '恭喜您成功购买电子卡券，您购买的卡券兑换码是：{兑换码}，请勿泄露给他人，戳链接查看{订单详情链接}。', 0, 0, 0, 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sbc-message`.`push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (44, '链接电子卡券购买成功通知', 2, 'VIRTUAL_COUPON_CODE_LINK', '链接电子卡券购买成功通知', '恭喜您成功购买电子卡券，您购买的卡券链接是：{链接}，请勿泄露给他人，戳链接查看{订单详情链接}。', 0, 0, 0, 1, 0, NULL, NULL, NULL, NULL);
-- 卡券购买通知 站内信
INSERT INTO `sbc-message`.`message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (42, '卡券密码电子卡券购买成功通知', '卡券密码电子卡券购买成功通知', '恭喜您成功购买电子卡券，您购买的卡券券码是：{券码}，密码是：{****}，请勿泄露给他人，戳链接查看{订单详情链接}。', 1, '2021-02-25 16:59:33', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'VIRTUAL_COUPON_CODE_PASSWORD');
INSERT INTO `sbc-message`.`message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (43, '兑换码电子卡券购买成功通知', '兑换码电子卡券购买成功通知', '恭喜您成功购买电子卡券，您购买的卡券兑换码是：{兑换码}，请勿泄露给他人，戳链接查看{订单详情链接}。', 1, '2021-02-25 17:00:30', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'VIRTUAL_COUPON_CODE');
INSERT INTO `sbc-message`.`message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (44, '链接电子卡券购买成功通知', '链接电子卡券购买成功通知', '恭喜您成功购买电子卡券，您购买的卡券链接是：{链接}，请勿泄露给他人，戳链接查看{订单详情链接}。', 1, '2021-02-25 16:44:28', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'VIRTUAL_COUPON_CODE_LINK');


-- 付费卡表结构修改
ALTER TABLE `sbc-customer`.`paid_card`
MODIFY COLUMN `discount_rate` decimal(5, 2) NOT NULL COMMENT '折扣率' AFTER `icon`;

INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c90c72377ebf3ae0177ed45941c0000', 3, 'ff80808170860f0801708b50875b0005', '企业购设价', 'f_enterprise_goods_set_price', NULL, 10, '2021-03-01 18:11:36', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c90c72377ebf3ae0177ed47343e0002', 3, '2c90c72377ebf3ae0177ed45941c0000', '保存设价', NULL, '/enterprise/goodsInfo/save', 'POST', NULL, 2, '2021-03-01 18:13:23', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('2c90c72377ebf3ae0177ed467ef00001', 3, '2c90c72377ebf3ae0177ed45941c0000', '详情信息', NULL, '/enterprise/*', 'GET', NULL, 1, '2021-03-01 18:12:36', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081777fd8d80177802620590004', 3, 'ff808081777fd8d801778023e5bb0002', '加价购阶梯详情', NULL, '/marketing/markup/markupLevelList', 'GET', NULL, 2, '2021-02-08 13:38:38', 0);

-- 付费卡规则表增加ERP sku编码
ALTER TABLE `sbc-customer`.`paid_card_rule`
ADD COLUMN `erp_sku_code` varchar(20) NOT NULL COMMENT 'erp sku 编码' AFTER `time_val`;

ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN isbn_no VARCHAR(45) DEFAULT NULL COMMENT 'ISBN码';

ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN erp_goods_no VARCHAR(45) DEFAULT NULL COMMENT 'spu编码(ERP)';

ALTER TABLE `sbc-setting`.`store_resource`
    ADD COLUMN `audit_status` tinyint(1) NULL COMMENT '是否审核通过，0：待审核，1：审核通过，2：审核不通过';


ALTER TABLE `sbc-setting`.`system_resource`
    ADD COLUMN `audit_status` tinyint(1) NULL COMMENT '是否审核通过，0：待审核，1：审核通过，2：审核不通过';

-- 付费会员配置初始化数据
INSERT INTO `sbc-setting`.`system_config`(`id`, `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES (68, 'paid_card', 'paid_card_config', '付费会员基础配置', NULL, 0, '{\"defaultImage\":{\"uid\":\"rc-upload-1612689926529-3\",\"status\":\"done\",\"url\":\"https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/16cbff2736e7823016184b664cae83b9.png\"},\"configImage\":{\"uid\":\"rc-upload-1612689926529-5\",\"status\":\"done\",\"url\":\"https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/50674a4691a70aa517968d0b911497d6.png\"},\"remainDay\":7}', '2021-01-28 11:14:22', NULL, 0);


DROP TABLE IF EXISTS `sbc-order`.`exception_of_trade_points`;
CREATE TABLE `sbc-order`.`exception_of_trade_points` (
  `id` varchar(32) NOT NULL COMMENT '异常标识ID',
  `trade_id` varchar(32) NOT NULL COMMENT '订单id',
  `points` bigint(20) NOT NULL COMMENT '使用积分',
  `error_code` varchar(32) NOT NULL COMMENT '异常码',
  `error_desc` varchar(225) DEFAULT NULL COMMENT '异常描述',
  `deduct_code` varchar(32) DEFAULT NULL COMMENT '樊登积分抵扣码',
  `handle_status` tinyint(4) DEFAULT NULL COMMENT '处理状态,0：待处理，1：处理失败，2：处理成功',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_trade_id` (`trade_id`),
  KEY `idx_deduct_code` (`deduct_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分订单抵扣异常表';


-- 供应商初始化脚本
INSERT INTO `sbc-customer`.`employee`(`employee_id`, `employee_name`, `employee_mobile`, `role_ids`, `is_employee`, `account_name`, `account_password`, `employee_salt_val`, `account_state`, `account_disable_reason`, `third_id`, `customer_id`, `del_flag`, `login_error_time`, `login_lock_time`, `login_time`, `company_info_id`, `is_master_account`, `account_type`, `email`, `job_no`, `position`, `birthday`, `sex`, `become_member`, `heir_employee_id`, `create_time`, `create_person`, `update_time`, `update_person`, `delete_time`, `delete_person`, `department_ids`, `manage_department_ids`) VALUES ('2c93993a781f885d01782001f3c20001', '18616222688', '18616222688', NULL, 0, '18616222688', '20ad2731844545bcc168fcada8abe9d7', '19b4e88673d88d06ec42c956356382cc7ffffe878c12ea793e0a8694eb2ef307', 0, NULL, NULL, NULL, 0, 0, NULL, '2021-03-11 15:31:14', 1182, 1, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-03-11 14:38:22', '', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `sbc-customer`.`company_info`(`company_info_id`, `company_name`, `back_ID_card`, `province_id`, `city_id`, `area_id`, `street_id`, `detail_address`, `contact_name`, `contact_phone`, `copyright`, `company_descript`, `operator`, `create_time`, `update_time`, `company_type`, `is_account_checked`, `social_credit_code`, `address`, `legal_representative`, `registered_capital`, `found_date`, `business_term_start`, `business_term_end`, `business_scope`, `business_licence`, `front_ID_card`, `company_code`, `supplier_name`, `del_flag`, `remit_affirm`, `apply_enter_time`, `store_type`, `company_source_type`) VALUES (1182, '喜言夏', '', 320000, 320100, 320102, NULL, '软件大道', '喜言夏', '18616222688', NULL, NULL, '', '2021-03-11 14:38:22', '2021-03-11 15:18:12', 1, NULL, '5555555555666666', '雨花台', NULL, 111.00, '2021-03-11 00:00:00', '2021-03-11 00:00:00', '2022-03-11 00:00:00', '商品批发', 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/1e846193985e47f8ac77ca663b39e510.png', '', 'P01182', '喜言夏', 0, 0, '2021-03-11 15:18:14', 1, NULL);
INSERT INTO `sbc-customer`.`store`(`store_id`, `company_info_id`, `freight_template_type`, `contract_start_date`, `contract_end_date`, `store_name`, `store_logo`, `store_sign`, `supplier_name`, `company_type`, `contact_person`, `contact_mobile`, `contact_email`, `province_id`, `city_id`, `area_id`, `street_id`, `address_detail`, `account_day`, `audit_state`, `audit_reason`, `store_closed_reason`, `store_state`, `del_flag`, `apply_enter_time`, `small_program_code`, `store_type`, `store_pinyin_name`, `supplier_pinyin_name`, `company_source_type`) VALUES (123458038, 1182, 0, '2021-03-11 00:00:00', '2022-04-28 23:59:59', '喜言夏', NULL, NULL, '喜言夏', 1, '喜言夏', '18616222688', '18616222688@163.com', 320000, 320100, 320102, NULL, '软件大道', '1,2', 1, NULL, NULL, 0, 0, '2021-03-11 15:18:11', 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/f2a137c6cb39fcc3c67c6db3a9a6e30c.jpg', 0, 'modong', 'modong', NULL);
INSERT INTO `sbc-setting`.`store_resource_cate`(`cate_id`, `store_id`, `company_info_id`, `cate_name`, `cate_parent_id`, `cate_img`, `cate_path`, `cate_grade`, `pin_yin`, `s_pin_yin`, `create_time`, `update_time`, `del_flag`, `sort`, `is_default`) VALUES (3111, 123458038, 1182, '默认分类', 0, NULL, '0|', 1, NULL, NULL, '2021-03-11 14:38:22', '2021-03-11 14:38:22', 0, 0, 1);
INSERT INTO `sbc-account`.`company_account`(`account_id`, `company_info_id`, `is_received`, `is_default_account`, `account_name`, `bank_name`, `bank_branch`, `bank_no`, `bank_code`, `bank_status`, `third_id`, `create_time`, `update_time`, `del_flag`, `del_time`, `remit_price`) VALUES (694, 1182, 0, 0, '喜言夏', '北京银行', '雨花台区', '12121313131321', 'BJBANK', 0, NULL, '2021-03-11 15:09:23', NULL, 0, NULL, NULL);
INSERT INTO `sbc-goods`.`contract_cate`(`contract_cate_id`, `store_id`, `cate_id`, `cate_rate`, `qualification_pics`) VALUES (4203, 123458038, 1120, NULL, '');
INSERT INTO `sbc-goods`.`contract_brand`(`contract_brand_id`, `store_id`, `brand_id`, `check_brand_id`, `authorize_pic`) VALUES (1733, '123458038', 439, NULL, 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/be0d440b47a618efd712ac7bb07be78c.jpg');
INSERT INTO `sbc-customer`.`store_level`(`store_level_id`, `store_id`, `level_name`, `discount_rate`, `amount_conditions`, `order_conditions`, `del_flag`, `create_time`, `create_person`, `update_time`, `update_person`, `delete_time`, `delete_person`) VALUES (13542, 123458038, '普通', 1.00, NULL, 1, 0, '2021-03-11 15:18:14', '2c8080815cd3a74a015cd3ae86850001', NULL, NULL, NULL, NULL);
INSERT INTO `sbc-goods`.`store_cate`(`store_cate_id`, `store_id`, `cate_name`, `cate_parent_id`, `cate_img`, `cate_path`, `cate_grade`, `pin_yin`, `s_pin_yin`, `create_time`, `update_time`, `del_flag`, `sort`, `is_default`) VALUES (1247, 123458038, '默认分类', 0, NULL, '0|', 1, NULL, NULL, '2021-03-11 15:18:11', '2021-03-11 15:18:11', 0, 0, 1);
INSERT INTO `sbc-goods`.`freight_template_goods`(`freight_temp_id`, `freight_temp_name`, `province_id`, `city_id`, `area_id`, `street_id`, `freight_free_flag`, `valuation_type`, `deliver_way`, `specify_term_flag`, `store_id`, `company_info_id`, `default_flag`, `create_time`, `del_flag`) VALUES (428, '默认模板', NULL, NULL, NULL, NULL, 0, 0, 1, 0, 123458038, 1182, 1, '2021-03-11 15:18:12', 0);
INSERT INTO `sbc-goods`.`freight_template_goods_express`(`id`, `freight_temp_id`, `destination_area`, `destination_area_name`, `valuation_type`, `freight_start_num`, `freight_start_price`, `freight_plus_num`, `freight_plus_price`, `default_flag`, `create_time`, `del_flag`) VALUES (458, 428, '', '未被划分的配送地区自动归于默认运费', 0, 1.00, 0.00, 1.00, 0.00, 1, '2021-03-11 15:18:12', 0);
INSERT INTO `sbc-goods`.`freight_template_store`(`freight_temp_id`, `freight_temp_name`, `deliver_way`, `destination_area`, `destination_area_name`, `freight_type`, `satisfy_price`, `satisfy_freight`, `fixed_freight`, `store_id`, `company_info_id`, `default_flag`, `create_time`, `del_flag`) VALUES (496, '默认模板', 1, '', '未被划分的配送地区自动归于默认运费', 1, 0.00, 0.00, 0.00, 123458038, 1182, 1, '2021-03-11 15:18:12', 0);

-- 商家初始化脚本
INSERT INTO `sbc-customer`.`employee`(`employee_id`, `employee_name`, `employee_mobile`, `role_ids`, `is_employee`, `account_name`, `account_password`, `employee_salt_val`, `account_state`, `account_disable_reason`, `third_id`, `customer_id`, `del_flag`, `login_error_time`, `login_lock_time`, `login_time`, `company_info_id`, `is_master_account`, `account_type`, `email`, `job_no`, `position`, `birthday`, `sex`, `become_member`, `heir_employee_id`, `create_time`, `create_person`, `update_time`, `update_person`, `delete_time`, `delete_person`, `department_ids`, `manage_department_ids`) VALUES ('2c93993a781f885d0178204094380002', '18616222699', '18616222699', NULL, 0, '18616222699', '1eb15820e2a67092304d16e642d1c808', 'cdec0fcb4b0f766211b986af7994b66a7ffffe878c538d824840e4272e9833c3', 0, NULL, NULL, NULL, 0, 0, NULL, '2021-03-11 15:48:59', 1183, 1, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-03-11 15:46:46', '', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `sbc-customer`.`company_info`(`company_info_id`, `company_name`, `back_ID_card`, `province_id`, `city_id`, `area_id`, `street_id`, `detail_address`, `contact_name`, `contact_phone`, `copyright`, `company_descript`, `operator`, `create_time`, `update_time`, `company_type`, `is_account_checked`, `social_credit_code`, `address`, `legal_representative`, `registered_capital`, `found_date`, `business_term_start`, `business_term_end`, `business_scope`, `business_licence`, `front_ID_card`, `company_code`, `supplier_name`, `del_flag`, `remit_affirm`, `apply_enter_time`, `store_type`, `company_source_type`) VALUES (1183, '周四', '', 110000, 110100, 110101, NULL, '王府井', '周四', '18616222699', NULL, NULL, '', '2021-03-11 15:46:46', '2021-03-11 16:07:39', 0, NULL, '666666666699', '2222', NULL, 2222.00, '2021-03-11 00:00:00', '2021-03-11 00:00:00', '2022-03-11 00:00:00', '烟酒', 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/34462bf766dabaa41d907496699cfd32.PNG', '', 'S01183', '周四', 0, 0, '2021-03-11 16:07:42', 1, NULL);
INSERT INTO `sbc-customer`.`store`(`store_id`, `company_info_id`, `freight_template_type`, `contract_start_date`, `contract_end_date`, `store_name`, `store_logo`, `store_sign`, `supplier_name`, `company_type`, `contact_person`, `contact_mobile`, `contact_email`, `province_id`, `city_id`, `area_id`, `street_id`, `address_detail`, `account_day`, `audit_state`, `audit_reason`, `store_closed_reason`, `store_state`, `del_flag`, `apply_enter_time`, `small_program_code`, `store_type`, `store_pinyin_name`, `supplier_pinyin_name`, `company_source_type`) VALUES (123458039, 1183, 0, '2021-03-11 00:00:00', '2022-03-11 23:59:59', '周四', NULL, NULL, '周四', 0, '周四', '18616222699', '18616222699@163.com', 110000, 110100, 110101, NULL, '王府井', '2', 1, NULL, NULL, 0, 0, '2021-03-11 16:07:39', 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/89dfaee625396f058b454e7b6f0349e3.jpg', 1, 'zhousi', 'zhousi', NULL);
INSERT INTO `sbc-goods`.`contract_brand`(`contract_brand_id`, `store_id`, `brand_id`, `check_brand_id`, `authorize_pic`) VALUES (1734, '123458039', 444, NULL, 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/ef0a04f2c022bf46c15499f405dae302.JPG');
INSERT INTO `sbc-goods`.`contract_cate`(`contract_cate_id`, `store_id`, `cate_id`, `cate_rate`, `qualification_pics`) VALUES (4204, 123458039, 1078, NULL, '');
INSERT INTO `sbc-account`.`company_account`(`account_id`, `company_info_id`, `is_received`, `is_default_account`, `account_name`, `bank_name`, `bank_branch`, `bank_no`, `bank_code`, `bank_status`, `third_id`, `create_time`, `update_time`, `del_flag`, `del_time`, `remit_price`) VALUES (695, 1183, 0, 0, '周四', '支付宝', '王府井', '65656565', 'alipay', 0, NULL, '2021-03-11 15:55:38', NULL, 0, NULL, NULL);
INSERT INTO `sbc-setting`.`store_resource_cate`(`cate_id`, `store_id`, `company_info_id`, `cate_name`, `cate_parent_id`, `cate_img`, `cate_path`, `cate_grade`, `pin_yin`, `s_pin_yin`, `create_time`, `update_time`, `del_flag`, `sort`, `is_default`) VALUES (3112, 123458039, 1183, '默认分类', 0, NULL, '0|', 1, NULL, NULL, '2021-03-11 15:46:46', '2021-03-11 15:46:46', 0, 0, 1);
INSERT INTO `sbc-goods`.`store_cate`(`store_cate_id`, `store_id`, `cate_name`, `cate_parent_id`, `cate_img`, `cate_path`, `cate_grade`, `pin_yin`, `s_pin_yin`, `create_time`, `update_time`, `del_flag`, `sort`, `is_default`) VALUES (1248, 123458039, '默认分类', 0, NULL, '0|', 1, NULL, NULL, '2021-03-11 16:07:39', '2021-03-11 16:07:39', 0, 0, 1);
INSERT INTO `sbc-goods`.`freight_template_goods`(`freight_temp_id`, `freight_temp_name`, `province_id`, `city_id`, `area_id`, `street_id`, `freight_free_flag`, `valuation_type`, `deliver_way`, `specify_term_flag`, `store_id`, `company_info_id`, `default_flag`, `create_time`, `del_flag`) VALUES (429, '默认模板', NULL, NULL, NULL, NULL, 0, 0, 1, 0, 123458039, 1183, 1, '2021-03-11 16:07:39', 0);
INSERT INTO `sbc-goods`.`freight_template_goods_express`(`id`, `freight_temp_id`, `destination_area`, `destination_area_name`, `valuation_type`, `freight_start_num`, `freight_start_price`, `freight_plus_num`, `freight_plus_price`, `default_flag`, `create_time`, `del_flag`) VALUES (459, 429, '', '未被划分的配送地区自动归于默认运费', 0, 1.00, 0.00, 1.00, 0.00, 1, '2021-03-11 16:07:39', 0);
INSERT INTO `sbc-goods`.`freight_template_store`(`freight_temp_id`, `freight_temp_name`, `deliver_way`, `destination_area`, `destination_area_name`, `freight_type`, `satisfy_price`, `satisfy_freight`, `fixed_freight`, `store_id`, `company_info_id`, `default_flag`, `create_time`, `del_flag`) VALUES (497, '默认模板', 1, '', '未被划分的配送地区自动归于默认运费', 1, 0.00, 0.00, 0.00, 123458039, 1183, 1, '2021-03-11 16:07:39', 0);

-- 消费记录表增加订单流转状态
ALTER TABLE `sbc-order`.`consume_record` ADD COLUMN `flow_state` VARCHAR(32) DEFAULT NULL COMMENT '订单流转状态';

-- 有赞数据迁移增加字段
ALTER TABLE `sbc-customer`.`customer` ADD COLUMN `yz_uid` bigint(30) DEFAULT NULL COMMENT '有赞UID';
ALTER TABLE `sbc-customer`.`customer` ADD COLUMN `yz_open_id` varchar(200) DEFAULT NULL COMMENT '有赞OpenId';
ALTER TABLE `sbc-customer`.`customer` ADD COLUMN `wx_open_id` varchar(200) DEFAULT NULL COMMENT '微信OpenId';
ALTER TABLE `sbc-customer`.`customer` ADD COLUMN `wx_union_id` varchar(200) DEFAULT NULL COMMENT '微信UnionId';

ALTER TABLE `sbc-order`.`exception_of_trade_points` ADD COLUMN `error_time` int(11) NULL COMMENT '错误次数' AFTER `handle_status`;

-- 积分抵扣订单补偿定时任务
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES ( 1, '0 0/5 * * * ?', '积分抵扣订单--未处理和失败补偿', '2021-03-17 12:46:48', '2021-03-17 19:49:53', '曹方', '', 'FIRST', 'pointDeductionTradeJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-03-17 12:46:48', '');

-- 樊登365会员初始化数据
INSERT INTO `sbc-customer`.`paid_card`(`id`, `name`, `background`, `icon`, `discount_rate`, `rule`, `agreement`, `del_flag`, `create_time`, `update_time`, `enable`, `create_person`, `update_person`, `bg_type`, `text_color`, `erp_spu_code`, `access_type`) VALUES ('f6cbe3737dbc432aa30067a2d1612a11', '樊登365（体验卡）', '#ceaf7e', 'https://shangcheng-resource.oss-cn-hangzhou.aliyuncs.com/eb909dee8974b4ea5a867f266fb4f41a.png', 0.92, '<p>2号卡2号卡</p>', '<p>啊发生的给</p>', 0, '2021-03-17 14:16:05', '2021-03-19 15:10:46', 1, '2c8080815cd3a74a015cd3ae86850001', '2c8080815cd3a74a015cd3ae86850001', 0, '#000000', NULL, 1);
INSERT INTO `sbc-customer`.`paid_card_rights_rel`(`id`, `paid_card_id`, `rights_id`) VALUES ('a475d1b5e2624d2e9db24f8e71a50620', 'f6cbe3737dbc432aa30067a2d1612a11', 1);

INSERT INTO `sbc-customer`.`paid_card`(`id`, `name`, `background`, `icon`, `discount_rate`, `rule`, `agreement`, `del_flag`, `create_time`, `update_time`, `enable`, `create_person`, `update_person`, `bg_type`, `text_color`, `erp_spu_code`, `access_type`) VALUES ('47ca4b34aff245cb886effb167554804', '樊登365（正式卡）', '#ceaf7e', 'https://shangcheng-resource.oss-cn-hangzhou.aliyuncs.com/eb909dee8974b4ea5a867f266fb4f41a.png', 0.92, '<p>2号卡2号卡</p>', '<p>啊发生的给</p>', 0, '2021-03-17 14:16:05', '2021-03-19 15:10:46', 1, '2c8080815cd3a74a015cd3ae86850001', '2c8080815cd3a74a015cd3ae86850001', 0, '#000000', NULL, 1);
INSERT INTO `sbc-customer`.`paid_card_rights_rel`(`id`, `paid_card_id`, `rights_id`) VALUES ('d46923b027d346ebbdd2c77a34df0799', '47ca4b34aff245cb886effb167554804', 1);

INSERT INTO `sbc-customer`.`fd_paid_cast` (`id`, `fd_pay_type`, `paid_card_id`, `create_time`, `update_time`, `delete_time`, `create_person`, `update_person`, `del_flag`)
 VALUES (1, 3, '47ca4b34aff245cb886effb167554804', '2021-03-12 16:45:32', '2021-03-12 16:45:29', '2021-03-12 16:45:25', NULL, NULL, 0);
 INSERT INTO `sbc-customer`.`fd_paid_cast` (`id`, `fd_pay_type`, `paid_card_id`, `create_time`, `update_time`, `delete_time`, `create_person`, `update_person`, `del_flag`) VALUES (2, 1, 'f6cbe3737dbc432aa30067a2d1612a11', '2021-03-12 16:46:41', '2021-03-12 16:46:44', '2021-03-12 16:46:46', NULL, NULL, 0);


-- 修改商品评价字段为可空
ALTER TABLE `sbc-goods`.`goods_evaluate`
    MODIFY COLUMN `customer_account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '会员登录账号|手机号' AFTER `customer_name`;

ALTER TABLE `sbc-goods`.`goods`
    MODIFY COLUMN `goods_detail` text CHARACTER SET utf8mb4 NULL COMMENT '商品详情' AFTER `audit_reason`;
ALTER TABLE `sbc-goods`.`goods_evaluate`
    MODIFY COLUMN `evaluate_content` varchar(500) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '商品评价内容' AFTER `evaluate_score`;
ALTER TABLE `sbc-goods`.`goods`
    MODIFY COLUMN `goods_mobile_detail` text CHARACTER SET utf8mb4 NULL COMMENT '移动端图文详情' AFTER `goods_detail`;

INSERT INTO `sbc-goods`.`goods_brand`(`brand_id`, `brand_name`, `pin_yin`, `s_pin_yin`, `store_id`, `brand_nick_name`, `brand_logo`, `create_time`, `update_time`, `del_flag`) VALUES (439, '宾利', NULL, NULL, 0, '汽车', 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/2ec6815f5dd80a65df8fa0198fc00de6.png', '2020-10-31 10:36:31', '2020-10-31 10:40:56', 0);
INSERT INTO `sbc-goods`.`goods_brand`(`brand_id`, `brand_name`, `pin_yin`, `s_pin_yin`, `store_id`, `brand_nick_name`, `brand_logo`, `create_time`, `update_time`, `del_flag`) VALUES (444, '品牌名称', NULL, NULL, 123458030, '别名', 'https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/0c1e20eb3f7e25d39476b0c4e05da7a6.jpg', '0000-00-00 00:00:00', '2020-11-11 14:14:59', 0);

ALTER TABLE `sbc-goods`.`goods_info`
    ADD COLUMN `yz_goods_info_no` varchar(45) NULL COMMENT '有赞的sku编码' AFTER `isbn_no`;

ALTER TABLE `sbc-goods`.`goods`
    ADD COLUMN `yz_goods_no` varchar(45) NULL COMMENT '有赞spu编码' AFTER `erp_goods_no`;



-- erp系统物流编码转换表
CREATE TABLE `erp_logistics_mapping` (
       `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '基本设置ID',
       `name_logistics_company` varchar(128) DEFAULT NULL COMMENT '物流公司名称',
       `erp_logistics_code` varchar(128) DEFAULT NULL COMMENT 'erp系统物流编码',
       `wm_logistics_code` varchar(128) DEFAULT NULL COMMENT '快递100物流编码',
       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='erp系统物流映射关系';


INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (1, '京东快递', 'JDKD', 'jd');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (2, '中通快递', 'ZTO', 'zhongtong');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (3, '顺丰速运', 'SF', 'shunfeng');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (4, '圆通快递', 'YTKD', 'yuantong');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (5, '圆通速运', 'YTO', 'yuantong');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (6, '韵达快递', 'YUNDA', 'yunda');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (7, '申通快递', 'STO', 'shentong');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (8, '百世汇通', 'HTKY', 'huitongkuaidi');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (9, '百世快运', 'BSKY', 'huitongkuaidi');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (10, '德邦快递', 'DBKD', 'debangkuaidi');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (11, 'EMS', 'EMS', 'ems');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (12, '天天快递', 'TTKD', 'tiantian');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (13, '宅急送', 'ZJS', 'zhaijisong');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (14, '邮政快递包裹', 'yzkdbg', 'youzhengguonei');
INSERT INTO `sbc-setting`.`erp_logistics_mapping`(`id`, `name_logistics_company`, `erp_logistics_code`, `wm_logistics_code`) VALUES (15, '中国邮政快递包裹（国内）', 'ZGYZKDBG-GN', 'youzhengguonei');


update `sbc-setting`.`store_resource_cate` set cate_name = '全部分类' where cate_name='默认分类' and cate_parent_id = 0 and is_default = 1;

update `sbc-setting`.`system_resource_cate` set cate_name = '全部分类' where cate_name='默认分类' and cate_parent_id = 0 and is_default = 1;

ALTER TABLE `sbc-goods`.`goods_image`
ADD COLUMN `sort` int(10) NULL COMMENT '图片排序字段' AFTER `big_url`;

/**xxl-job****/
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info` (`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (205, 1, '0 0/10 * * * ?', 'erp发货状态同步(扫描次数)', '2021-04-12 19:25:42', '2021-04-12 19:25:42', '胡清杰', '', 'FIRST', 'tradeDeliversScanCountJobHandler', '100', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-04-12 19:25:42', '');
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info` (`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (206, 1, '0 0/30 * * * ?', '重置erp发货状态同步扫描次数', '2021-04-12 19:27:16', '2021-04-12 19:27:16', '胡清杰', '', 'FIRST', 'erpResetStatusSyncCountJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-04-12 19:27:16', '');
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info` (`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (207, 1, '0 0/10 * * * ?', '历史订单同步发货状态', '2021-04-28 19:58:13', '2021-04-29 20:14:11', '胡清杰', '', 'FIRST', 'historyOrderStatusSyncJobHandler', '1,2021-04-14 17:59:58,2021-04-14 19:59:58', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-04-28 19:58:13', '');

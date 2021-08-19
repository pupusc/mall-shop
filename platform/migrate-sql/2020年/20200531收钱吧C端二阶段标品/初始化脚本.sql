-- C端二阶段标品
ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN `added_timing_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否定时上架 0:否1:是' AFTER `single_spec_flag`,
ADD COLUMN `added_timing_time` datetime NULL COMMENT '定时上架时间' AFTER `added_timing_flag`,
ADD COLUMN `stock` bigint(11) NULL COMMENT '库存（准实时）' AFTER `added_timing_time`,
ADD COLUMN `sku_min_market_price` decimal(10, 2) NULL COMMENT '最小市场价' AFTER `stock`,
ADD COLUMN `goods_buy_types` varchar(10) NULL COMMENT '购买方式 0立即购买,1购物车,内容以,相隔' AFTER `sku_min_market_price`;

-- 初始化新增字段
UPDATE `sbc-goods`.`goods` g,(SELECT i.goods_id, sum( i.stock ) stock, min( i.market_price )  market_price FROM
`sbc-goods`.`goods_info` i WHERE i.del_flag = 0 GROUP BY i.goods_id) c set g.stock = c.stock, g.sku_min_market_price = c.market_price, g.goods_buy_types='0,1' WHERE g.goods_id = c.goods_id;

ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN `added_timing_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否定时上架 0:否1:是',
ADD COLUMN `added_timing_time` datetime NULL COMMENT '定时上架时间';

INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0/5 * * * ?', '商品SPU库存同步定时任务', '2020-05-22 11:48:58', '2020-05-22 11:48:58', '戴倚天', '', 'FIRST', 'goodsStockSyncJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-05-22 15:48:58', '');
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '1 0 0/1 * * ?', '商品上架定时任务', '2020-05-22 11:48:58', '2020-05-22 11:48:58', '戴倚天', '', 'FIRST', 'goodsAddedTimingJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-05-22 15:48:58', '');

-- 增加自动好评设置
INSERT INTO `sbc-setting`.`system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('order_setting', 'order_setting_timeout_evaluate', '超时自动评价订单', NULL, 1, '{\"day\":30,\"comment\":\"用户未填写评价内容\"}', '2020-05-28 16:36:06', '2020-05-28 16:36:08', 0);

-- 好评增加标识
ALTER TABLE `sbc-goods`.`goods_evaluate`
ADD COLUMN `is_sys` tinyint(255) NULL DEFAULT 0 COMMENT '是否系统评价 0：否，1：是' AFTER `del_person`;

--- 创建组合活动-商品sku关联表
CREATE TABLE `sbc-marketing`.`marketing_suits_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `suits_id` bigint(20) DEFAULT NULL COMMENT '组合id',
  `marketing_id` bigint(20) DEFAULT NULL COMMENT '促销活动id',
  `sku_id` varchar(32) DEFAULT NULL COMMENT 'skuId',
  `discount_price` decimal(10,2) DEFAULT NULL COMMENT '单个优惠价格（优惠多少）',
  `num` bigint(20) DEFAULT NULL COMMENT 'sku数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组合活动-商品sku关联表';

--- 创建组合商品主表
CREATE TABLE `sbc-marketing`.`marketing_suits` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `marketing_id` bigint(20) NOT NULL COMMENT '促销id',
  `main_image` varchar(255) DEFAULT NULL COMMENT '套餐主图（图片url全路径）',
  `suits_price` decimal(10,2) DEFAULT NULL COMMENT '套餐价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组合商品主表';

--- 重新调整备注
ALTER TABLE `sbc-marketing`.`marketing`
MODIFY COLUMN `marketing_type` tinyint(4) NOT NULL COMMENT '促销类型 0：满减 1:满折 2:满赠 3一口价优惠 4第二件半价 5秒杀(无用) 6组合套餐' AFTER `marketing_name`,
MODIFY COLUMN `sub_type` tinyint(4) NOT NULL COMMENT '促销子类型 0:满金额减 1:满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠 6:一口价 7:第二件半价 8:组合商品' AFTER `marketing_type`;


-- 预约抢购表
create table `sbc-goods`.`appointment_sale`
(
    id                     bigint auto_increment,
    activity_name          varchar(100)         not null comment '活动名称',
    store_id               bigint     default 0 not null comment '商户id',
    appointment_type       tinyint              not null comment '预约类型 0：不预约不可购买  1：不预约可购买',
    appointment_start_time datetime             not null comment '预约开始时间',
    appointment_end_time   datetime             not null comment '预约结束时间',
    snap_up_start_time     datetime             not null comment '抢购开始时间',
    snap_up_end_time       datetime             not null comment '抢购结束时间',
    deliver_time           varchar(10)          not null comment '发货日期 2020-01-10',
    join_level             varchar(500)         not null comment '参加会员  -1:全部客户 0:全部等级 other:其他等级',
    join_level_type        tinyint              not null comment '是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）',
    del_flag               tinyint    default 0 not null comment '是否删除标志 0：否，1：是',
    pause_flag             tinyint(4) default '0' COMMENT '是否暂停 0:否 1:是',
    create_time            datetime             null comment '创建时间',
    update_time            datetime             null comment '更新时间',
    create_person          varchar(36)          null comment '创建人',
    update_person          varchar(36)          null comment '更新人',
    primary key (id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT comment '预约抢购表';

-- 预约商品表
create table `sbc-goods`.`appointment_sale_goods`
(
    id                  bigint auto_increment,
    appointment_sale_id bigint           not null comment '预约id',
    store_id            bigint           not null comment '商户id',
    goods_info_id       varchar(32)      not null comment 'skuID',
    goods_id            varchar(32)      not null comment 'spuID',
    price               decimal(10, 2)   null comment '预约价',
    appointment_count   int(8) default 0 not null comment '预约数量',
    buyer_count         int(8) default 0 not null comment '购买数量',
    create_time         datetime         null comment '创建时间',
    update_time         datetime         null comment '更新时间',
    create_person       varchar(36)      null comment '创建人',
    update_person       varchar(36)      null comment '更新人',
    primary key (id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT comment '预约商品表';

create index appointment_store_index
    on `sbc-goods`.`appointment_sale` (store_id);

create index appointment_type_index
    on `sbc-goods`.`appointment_sale` (appointment_type);

create index appointment_goods_store_index
    on `sbc-goods`.`appointment_sale_goods` (store_id);

create index appointment_goods_info_id_index
    on `sbc-goods`.`appointment_sale_goods` (goods_info_id);

create index appointment_sale_id_index
    on `sbc-goods`.`appointment_sale_goods` (appointment_sale_id);


-- 规则信息表
create table `sbc-goods`.`rule_info`
(
    id            bigint auto_increment,
    rule_name     varchar(128) unique default null comment '规则名称',
    rule_content  varchar(1024)       default null comment '规则说明',
    rule_type     tinyint(4)                    not null comment '规则类型 0:预约 1:预售',
    del_flag      tinyint             default 0 not null comment '是否删除标志 0：否，1：是',
    create_time   datetime                      null comment '创建时间',
    update_time   datetime                      null comment '更新时间',
    create_person varchar(36)                   null comment '创建人',
    update_person varchar(36)                   null comment '更新人',
    primary key (id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT comment '规则信息表';


-- 预售表
create table `sbc-goods`.`booking_sale`
(
    id                  bigint auto_increment,
    activity_name       varchar(100)         not null comment '活动名称',
    store_id            bigint               not null comment '商户id',
    booking_type        tinyint              not null comment '预售类型 0：全款预售  1：定金预售',
    hand_sel_start_time datetime   default null comment '定金支付开始时间',
    hand_sel_end_time   datetime   default null comment '定金支付结束时间',
    tail_start_time     datetime   default null comment '尾款支付开始时间',
    tail_end_time       datetime   default null comment '尾款支付结束时间',
    booking_start_time  datetime   default null comment '预售开始时间',
    booking_end_time    datetime   default null comment '预售结束时间',
    start_time          datetime             not null comment '预售活动总开始时间',
    end_time            datetime             not null comment '预售活动总结束时间',
    deliver_time        varchar(10)          not null comment '发货日期 2020-01-10',
    join_level          varchar(500)         not null comment '参加会员  -1:全部客户 0:全部等级 other:其他等级',
    join_level_type     tinyint              not null comment '是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）',
    del_flag            tinyint    default 0 not null comment '是否删除标志 0：否，1：是',
    pause_flag          tinyint(4) default '0' COMMENT '是否暂停 0:否 1:是',
    create_time         datetime             null comment '创建时间',
    update_time         datetime             null comment '更新时间',
    create_person       varchar(36)          null comment '创建人',
    update_person       varchar(36)          null comment '更新人',
    primary key (id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT comment '预售表';

-- 预售商品表
create table `sbc-goods`.`booking_sale_goods`
(
    id                bigint auto_increment,
    booking_sale_id   bigint                   not null comment '预售id',
    store_id          bigint                   not null comment '商户id',
    goods_info_id     varchar(32)              not null comment 'skuID',
    goods_id          varchar(32)              not null comment 'spuID',
    hand_sel_price    decimal(10, 2) default null comment '定金',
    inflation_price   decimal(10, 2) default null comment '膨胀价格',
    booking_price     decimal(10, 2) default null comment '预售价',
    booking_count     int(8)         default null comment '预售数量',
    can_booking_count int(8)         default null comment '实际可售数量',
    hand_sel_count    int(8)         default 0 not null comment '定金支付数量',
    tail_count        int(8)         default 0 not null comment '尾款支付数量',
    pay_count         int(8)         default 0 not null comment '全款支付数量',
    create_time       datetime                 null comment '创建时间',
    update_time       datetime                 null comment '更新时间',
    create_person     varchar(36)              null comment '创建人',
    update_person     varchar(36)              null comment '更新人',
    primary key (id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT comment '预售商品表';


create index booking_store_index
    on `sbc-goods`.`booking_sale` (store_id);

create index booking_type_index
    on `sbc-goods`.`booking_sale` (booking_type);

create index booking_goods_info_id_index
    on `sbc-goods`.`booking_sale_goods` (goods_info_id);

create index booking_goods_store_index
    on `sbc-goods`.`booking_sale_goods` (store_id);

create index booking_sale_id_index
    on `sbc-goods`.`booking_sale_goods` (booking_sale_id);



INSERT INTO `sbc-message`.`sms_template` (`template_name`, `template_content`, `remark`, `template_type`,
                                          `review_status`, `template_code`, `review_reason`, `sms_setting_id`,
                                          `del_flag`, `create_time`, `business_type`, `purpose`, `sign_id`, `open_flag`)
VALUES ('预约开售通知通知', '您预约的${name}已经开售啦，快来带走它吧~', '预约开售通知通知', '1', '3', NULL, NULL, NULL, '0', '2020-06-01 16:00:00',
        'APPOINTMENT_SALE', '预约开售通知', NULL, '1');


INSERT INTO `sbc-message`.`push_send_node` (`node_name`, `node_type`, `node_code`, `node_title`, `node_context`,
                                            `expected_send_count`, `actually_send_count`, `open_count`, `status`,
                                            `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`)
VALUES ('预约开售通知通知', '2', 'APPOINTMENT_SALE', '预约开售通知通知', '您预约的{商品名称}已经开售啦，快来带走它吧~', '0', '0', '0', '1', '0', NULL, NULL,
        NULL, '2020-06-01 16:00:00');


INSERT INTO `sbc-message`.`message_send_node` (`node_name`, `node_title`, `node_content`, `status`, `create_time`,
                                               `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`,
                                               `open_sum`, `route_name`, `node_type`, `node_code`)
VALUES ('预约开售通知通知', '预约开售通知通知', '您预约的{商品名称}已经开售啦，快来带走它吧~', '1', '2020-06-01 16:00:00', NULL, NULL, NULL, '0', '0', '0',
        NULL, '2', 'APPOINTMENT_SALE');



INSERT INTO `sbc-message`.`sms_template` (`template_name`, `template_content`, `remark`, `template_type`,
                                          `review_status`, `template_code`, `review_reason`, `sms_setting_id`,
                                          `del_flag`, `create_time`, `business_type`, `purpose`, `sign_id`, `open_flag`)
VALUES ('预售订单尾款支付通知', '您的预售订单${name}尾款支付通道已开启，请及时支付~', '预售订单尾款支付通知', '1', '3', NULL, NULL, NULL, '0',
        '2020-06-01 16:00:00', 'BOOKING_SALE', '预约开售通知', NULL, '1');


INSERT INTO `sbc-message`.`push_send_node` (`node_name`, `node_type`, `node_code`, `node_title`, `node_context`,
                                            `expected_send_count`, `actually_send_count`, `open_count`, `status`,
                                            `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`)
VALUES ('预售订单尾款支付通知', '2', 'BOOKING_SALE', '预售订单尾款支付通知', '您的预售订单{商品名称}尾款支付通道已开启，请及时支付~', '0', '0', '0', '1', '0', NULL,
        NULL, NULL, '2020-06-01 16:00:00');


INSERT INTO `sbc-message`.`message_send_node` (`node_name`, `node_title`, `node_content`, `status`, `create_time`,
                                               `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`,
                                               `open_sum`, `route_name`, `node_type`, `node_code`)
VALUES ('预售订单尾款支付通知', '预售订单尾款支付通知', '您的预售订单{商品名称}尾款支付通道已开启，请及时支付~', '1', '2020-06-01 16:00:00', NULL, NULL, NULL, '0',
        '0', '0', NULL, '2', 'BOOKING_SALE');


-- 第二件半价规则表
CREATE TABLE `sbc-marketing`.`marketing_half_price_second_piece` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `marketing_id` bigint(20) NOT NULL COMMENT '营销Id',
  `number` int(11) DEFAULT NULL COMMENT '件数',
  `discount` decimal(2,1) DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`id`),
  KEY `marketing_id` (`marketing_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='第二件半价关联表';

-- 第三方绑定关系表，增加解绑时间
ALTER TABLE `sbc-customer`.`third_login_relation` ADD COLUMN `unbinding_time` datetime DEFAULT NULL COMMENT '解绑时间';


-- 预约预售定时任务配置
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info` (`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES ('1', '0 0 0/1 * * ?	', '预约开售通知	', '2020-06-02 11:21:23', '2020-06-02 11:21:23', '张小东', '', 'FIRST', 'appointmentSaleActivityJobHandler', '', 'SERIAL_EXECUTION', '0', '0', 'BEAN', '', 'GLUE代码初始化', '2020-06-02 11:21:23', '');


INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info` (`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES ('1', '0 0 0/1 * * ?', '预售开售通知', '2020-07-08 11:29:42', '2020-07-08 11:29:42', '张小东', '', 'FIRST', 'bookingSaleActivityJobController', '', 'SERIAL_EXECUTION', '0', '0', 'BEAN', '', 'GLUE代码初始化', '2020-07-08 11:29:42', '');



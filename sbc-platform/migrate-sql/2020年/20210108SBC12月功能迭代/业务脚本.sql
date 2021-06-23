
-- 调价记录表
CREATE TABLE `sbc-goods`.`price_adjustment_record`  (
  `id` varchar(32) NOT NULL COMMENT '调价单号',
  `price_adjustment_type` tinyint(1) NOT NULL COMMENT '调价类型：0 市场价、 1 等级价、2 阶梯价、3 供货价 ',
  `store_id` bigint(20) NULL COMMENT '店铺id',
  `goods_num` bigint(10) NULL COMMENT '调价商品数',
  `effective_time` datetime(0) NULL COMMENT '生效时间',
  `creator_name` varchar(20) NULL COMMENT '制单人名称',
  `creator_account` varchar(20) NULL COMMENT '制单人账号',
  `create_person` varchar(32) NULL COMMENT '创建人',
  `create_time` datetime(0) NULL COMMENT '创建时间',
  `confirm_flag` tinyint(1) default 0 COMMENT '是否确认：0 未确认、1 已确认',
  PRIMARY KEY (`id`),
  index `idx_price_adjustment_type`(`price_adjustment_type`),
  index `idx_store_id`(`store_id`),
  index `idx_create_time`(`create_time`),
  index `idx_confirm_flag`(`confirm_flag`)
) COMMENT = '调价记录表';

-- 调价单详情表
CREATE TABLE `sbc-goods`.`price_adjustment_record_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `price_adjustment_no` varchar(32) NOT NULL COMMENT '调价单号',
  `goods_info_id` varchar(32) not null  comment 'SKUID',
  `goods_info_name` varchar(255) NULL  comment '商品名称',
  `goods_info_no` varchar(45) not null  comment 'SKU编码',
  `goods_info_img` varchar(255) null  comment 'SKU图片',
  `goods_spec_text` varchar(255) NULL COMMENT '商品规格',
  `alone_flag` tinyint(1) NULL COMMENT '是否独立设价：0 否、1 是',
  `sale_type` tinyint(1) null comment '销售类别(0:批发,1:零售)',
  `price_type` tinyint null comment '设价类型,0:按客户(等级)1:按订货量(阶梯价)2:按市场价',
  `original_market_price` decimal(10, 2) NULL COMMENT '原市场价',
  `adjusted_market_price` decimal(10, 2) NULL COMMENT '调整后市场价',
  `price_difference` decimal(10, 2) NULL COMMENT '差异',
  `lever_price` text NULL COMMENT '等级价 eg:[{},{}...]',
  `interval_price` varchar(1024) NULL COMMENT '阶梯价 eg:[{},{}...]',
  `supply_price` decimal(10, 2) NULL COMMENT '原供货价',
  `adjust_supply_price` decimal(10,2) DEFAULT NULL COMMENT '调整后供货价',
  `adjust_result` tinyint(1) NOT NULL COMMENT '执行结果：0 未执行、1 执行成功、2 执行失败',
  `fail_reason` varchar(255) NULL COMMENT '失败原因',
  `confirm_flag` tinyint(1) default 0 COMMENT '是否确认：0 未确认、1 已确认',
  PRIMARY KEY (`id`),
  index `idx_price_adjustment_no`(`price_adjustment_no`),
  index `idx_goods_info_no`(`goods_info_no`),
  index `idx_price_difference`(`price_difference`),
  index `idx_adjust_result`(`adjust_result`)
) COMMENT = '调价单详情表';

-- xxl-job 定时清理未确认的调价记录
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`)
VALUES (1, '0 0 0 * * ?', '清理未确认的批量调价记录', '2020-12-22 20:06:50', '2020-12-22 20:06:50', '许云鹏', '', 'FIRST', 'priceAdjustJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-12-22 20:06:50', '');
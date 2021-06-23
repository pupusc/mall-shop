-- 商品库新增字段
ALTER TABLE `sbc-goods`.standard_sku ADD COLUMN goods_source TINYINT(4) COMMENT '商品来源，0供应商，1商家, 2linkedmall, 3平台';
ALTER TABLE `sbc-goods`.`standard_sku`
MODIFY COLUMN `goods_source` tinyint(4) NULL DEFAULT 1 COMMENT '商品来源，0供应商，1商家, 2linkedmall, 3平台' AFTER `goods_info_no`;

ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN `provider_status` tinyint(4) NULL COMMENT '供应商状态 0: 关店 1:开店';
ALTER TABLE `sbc-goods`.`goods_info`
ADD COLUMN `provider_status` tinyint(4) NULL COMMENT '供应商状态 0: 关店 1:开店';

-- 供应商店铺过期定时任务
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`)
VALUES (1, '0 0 0 * * ?', '供应商店铺过期定时任务', '2020-09-14 18:09:57', '2020-09-14 18:09:57', '许云鹏', '', 'FIRST', 'StoreExpireHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-09-14 18:09:57', '');
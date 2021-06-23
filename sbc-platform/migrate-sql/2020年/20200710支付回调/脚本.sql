CREATE TABLE `sbc-order`.`pay_call_back_result` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` varchar(45) NOT NULL COMMENT '订单号',
  `result_xml` text COMMENT '回到结果xml内容',
  `result_context` text NOT NULL COMMENT '回调结果内容',
  `result_status` tinyint(3) DEFAULT NULL COMMENT '结果状态，0：待处理；1:处理中 2：处理成功；3：处理失败',
  `error_num` tinyint(3) DEFAULT NULL COMMENT '处理失败次数',
  `pay_type` tinyint(3) DEFAULT NULL COMMENT '支付方式，0：微信；1：支付宝；2：银联',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_person` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=312 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付回调结果表';

ALTER TABLE `sbc-order`.`pay_call_back_result`
ADD UNIQUE INDEX `bussness_id_uni_index`(`business_id`) USING BTREE COMMENT '订单编号唯一索引';

INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES ( 1, '0 */1 * * * ?', '支付回调补偿--未处理和失败补偿', '2020-08-06 15:46:48', '2020-08-10 19:49:53', '吕振伟', '', 'FIRST', 'payCallBackJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-08-06 15:46:48', '');
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES ( 1, '0 */30 * * * ?', '支付回调补偿--处理中（默认30分钟之前）', '2020-07-09 14:48:56', '2020-08-10 19:51:38', '吕振伟', '', 'FIRST', 'payCallBackJobHandler', '1', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-07-09 14:48:56', '');
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES ( 1, '0 0 1 * * ?', '支付回调补偿--指定订单补偿（默认不启动）', '2020-08-10 19:56:32', '2020-08-10 19:56:32', '吕振伟', '', 'FIRST', 'payCallBackJobHandler', '4&O202008061122177914,O202008061122177915', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-08-10 19:56:32', '');
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES ( 1, '0 0 1 * * ?', '支付回调补偿--处理中（指定时间，默认不启动）', '2020-08-10 19:54:42', '2020-08-10 19:54:42', '吕振伟', '', 'FIRST', 'payCallBackJobHandler', '1&2020-08-01 10:10:00', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-08-10 19:54:42', '');



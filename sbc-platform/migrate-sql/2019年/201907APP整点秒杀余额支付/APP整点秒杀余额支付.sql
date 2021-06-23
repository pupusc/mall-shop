-- ***************************************** xxl-job begin *********************************************
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 2 1 * ?', '清理过期报表数据', '2019-05-17 14:34:51', '2019-05-17 14:34:51', 'XXL', '', 'FIRST', 'reportCleanExpirationTaskHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-05-17 14:34:51', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0 1 * * ?', '凌晨生成报表', '2019-05-17 14:41:39', '2019-05-17 14:41:39', 'XXL', '', 'FIRST', 'reportTaskHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-05-17 14:41:39', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (2, '0 0/10 * * * ?', '生成今日报表', '2019-05-17 14:42:57', '2019-05-17 14:43:19', 'XXL', '', 'FIRST', 'reportTodayTaskHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-05-17 14:42:57', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (3, '0 1 0 * * ?', 'PV/UV汇总redis统计数据并推送到统计系统', '2019-06-11 15:02:20', '2019-06-11 15:02:20', 'XXL', '', 'FIRST', 'PvUvJobHandler', '1', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-06-11 15:02:20', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (3, '0 0/5 * * * ? ', 'PV/UV汇总redis统计数据并推送到统计系统 今天每5分钟一次', '2019-06-11 15:04:31', '2019-06-11 15:04:31', 'XXL', '', 'FIRST', 'PvUvJobHandler', '2', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-06-11 15:04:31', '');

INSERT INTO `xxl_job_qrtz_trigger_group`(`app_name`, `title`, `order`, `address_type`, `address_list`) VALUES ('xxl-job-ares', 'xxl-job-ares', 1, 0, '10.1.5.84:9999');
INSERT INTO `xxl_job_qrtz_trigger_group`(`app_name`, `title`, `order`, `address_type`, `address_list`) VALUES ('xxl-job-persesus', 'persesus', 1, 0, '10.1.5.84:9998');
-- ***************************************** xxl-job end *********************************************





-- ***************************************** pay begin *********************************************
INSERT INTO `pay_gateway`(`id`, `name`, `is_open`, `type`, `create_time`) VALUES (5, 'BALANCE', 1, 0, '2019-07-08 17:26:17');
INSERT INTO `pay_gateway_config`(`id`, `gateway_id`, `api_key`, `secret`, `account`, `app_id`, `app_id2`, `private_key`, `public_key`, `create_time`, `pc_back_url`, `pc_web_url`, `boss_back_url`, `open_platform_app_id`, `open_platform_secret`, `open_platform_api_key`, `open_platform_account`, `wx_pay_certificate`, `wx_open_pay_certificate`) VALUES (5, 5, '', '', '', '', '', '', '', '2019-07-15 18:48:22', '', '', '', '', '', '', '', NULL, NULL);
INSERT INTO `pay_channel_item` (`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (21, '余额支付', 5, 'Balance', 1, 0, 'balance_pc', '2019-07-09 09:47:09');
INSERT INTO `pay_channel_item` (`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (22, '余额H5支付', 5, 'Balance', 1, 1, 'balance_h5', '2019-07-09 09:47:21');
INSERT INTO `pay_channel_item` (`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (23, '余额APP支付', 5, 'Balance', 1, 2, 'balance_app', '2019-07-09 09:47:32');
-- ***************************************** pay end *********************************************





-- ***************************************** setting begin*********************************************
-- ***************************************** setting end*********************************************





-- ***************************************** customer begin *********************************************
-- ***************************************** customer end ***********************************************





-- ***************************************** account begin *********************************************
alter table `customer_funds_detail` modify funds_type tinyint not null comment '资金类型 1：分销佣金；2：佣金提现；3：邀新奖励； 4：佣金提成；5：余额支付';
alter table `customer_funds_detail` modify sub_type tinyint null comment '资金子类型 1：推广返利；2：佣金提现；3：邀新奖励；4:自购返利；5:推广提成；6:余额支付'
-- ***************************************** account end ***********************************************





-- ***************************************** goods begin *********************************************
-- ***************************************** goods end *********************************************





-- ***************************************** marketing begin *********************************************
-- ***************************************** marketing end ***********************************************





-- ***************************************** order begin *********************************************
-- ***************************************** order end ***********************************************
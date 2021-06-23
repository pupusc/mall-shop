-- sbc-setting --
use `sbc-setting`;
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817019135101701e1dd1580000', 4, 'ff8080816f660586016f8e00e3c00000', '运营计划数据', 'f-customer-plan-data', NULL, 8, '2020-02-07 13:27:16', 0);

INSERT INTO  `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817019135101701e212c6d0001', 4, 'ff8080817019135101701e1dd1580000', '根据id查询运营计划', NULL, '/customerplan/*', 'GET', NULL, 1, '2020-02-07 13:30:55', 0);
INSERT INTO  `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817019135101701e2275b20002', 4, 'ff8080817019135101701e1dd1580000', '根据planId查询覆盖统计', NULL, '/customerplansendcount/*', 'GET', NULL, 2, '2020-02-07 13:32:20', 0);
INSERT INTO  `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817019135101701e232a750003', 4, 'ff8080817019135101701e1dd1580000', '根据planId查询推送统计', NULL, '/planstatisticsmessagepush/*', 'GET', NULL, 3, '2020-02-07 13:33:06', 0);
INSERT INTO  `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170526ffa0170579e2eae0000', 4, 'ff8080817019135101701e1dd1580000', '转换效果接口', '', '/customer/plan/conversion/*', 'GET', NULL, 4, '2020-02-18 17:25:49', 0);
-- sbc-setting  end--

-- xxl-job --
use `xxl-job`;
INSERT INTO  `xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (75, 6, '0 0 1 * * ?', '权益礼包优惠券发放统计', '2020-02-04 14:38:36', '2020-02-04 21:27:04', '张浩', '', 'FIRST', 'customerPlanningCountJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-02-04 14:38:36', '');

INSERT INTO `xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (70, 6, '0 0 1 * * ?', '运营计划统计--通知人次统计', '2020-02-10 10:40:24', '2020-02-10 10:40:24', '吕振伟', '', 'FIRST', 'customerPlanStatisticsPushJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-02-10 10:40:24', '');

INSERT INTO  `xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`,
`author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`,
`executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (73, 6, '0 0 1 * * ?', '运行计划转化效果统计', '2020-02-012 19:38:36', '2020-02-012 19:38:36', '张文昌', '', 'FIRST', 'customerPlanningConversionScheduledGenerate', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-02-12 19:38:36', '');
-- xxl-job end --

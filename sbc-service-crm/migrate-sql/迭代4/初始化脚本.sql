
-- sbc-setting start 权限菜单---

INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e00e3c00000', 4, 'ff8080816f5496f0016f5608682c0000', 3, '人群运营', '/customer-plan-list', NULL, 3, '2020-01-10 13:50:21', 0);

INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac4377a30013', 4, 'ff8080816f5496f0016f5609b2b90001', '自定义人群', 'f-custom-group', NULL, 3, '2020-01-16 10:51:40', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac34128c000f', 4, 'ff8080816f5496f0016f5609b2b90001', '短信群发', 'f-group-sms', NULL, 1, '2020-01-16 10:34:51', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e04521f0008', 4, 'ff8080816f660586016f8e00e3c00000', '开始运营计划', 'f-customer-plan-start', NULL, 6, '2020-01-10 13:54:05', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e03cffb0007', 4, 'ff8080816f660586016f8e00e3c00000', '暂停运营计划', 'f-customer-plan-pause', NULL, 5, '2020-01-10 13:53:32', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e0288be0005', 4, 'ff8080816f660586016f8e00e3c00000', '删除运营计划', 'f-customer-plan-delete', NULL, 4, '2020-01-10 13:52:08', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e023cbb0004', 4, 'ff8080816f660586016f8e00e3c00000', '编辑运营计划', 'f-customer-plan-modify', NULL, 3, '2020-01-10 13:51:49', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e01f7dd0003', 4, 'ff8080816f660586016f8e00e3c00000', '新增运营计划', 'f-customer-plan-add', NULL, 2, '2020-01-10 13:51:31', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e01a3a30002', 4, 'ff8080816f660586016f8e00e3c00000', '查看运营计划', 'f-customer-plan-detail', NULL, 1, '2020-01-10 13:51:10', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e0141a70001', 4, 'ff8080816f660586016f8e00e3c00000', '运营计划列表查看', 'f-customer-plan-list', NULL, 0, '2020-01-10 13:50:45', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e05a406000b', 4, 'ff8080816f660586016f8e04521f0008', '开始运营计划', NULL, '/customerplan/start/*', 'PUT', NULL, 0, '2020-01-10 13:55:32', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e0542df000a', 4, 'ff8080816f660586016f8e03cffb0007', '暂停运营计划', NULL, '/customerplan/pause/*', 'PUT', NULL, 0, '2020-01-10 13:55:07', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e04dd5c0009', 4, 'ff8080816f660586016f8e0288be0005', '删除运营计划', NULL, '/customerplan/*', 'DELETE', NULL, 0, '2020-01-10 13:54:41', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016f8e0334160006', 4, 'ff8080816f660586016f8e0141a70001', '运营计划列表查看', NULL, '/customerplan/page', 'POST', NULL, 0, '2020-01-10 13:52:52', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fa7da6348000e', 4, 'ff8080816f660586016f8e01a3a30002', '查看运营计划', NULL, '/customerplan/*', 'GET', NULL, '0', '2020-01-15 14:18:25', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fa7d9e75a000d', 4, 'ff8080816f660586016f8e023cbb0004', '编辑运营计划', NULL, '/customerplan/modify', 'PUT', NULL, '1', '2020-01-15 14:17:53', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fa7d95994000c', 4, 'ff8080816f660586016f8e01f7dd0003', '新增运营计划', NULL, '/customerplan/add', 'POST', NULL, '0', '2020-01-15 14:17:17', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc65153d0026', 4, 'ff8080816f660586016f8e023cbb0004', '根据活动id查询 人群运营计划', NULL, '/customerplan/activity/*', 'GET', NULL, 6, '2020-01-19 14:02:19', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc64c6d20025', 4, 'ff8080816f660586016f8e01f7dd0003', '根据活动id查询 人群运营计划', NULL, '/customerplan/activity/*', 'GET', NULL, 6, '2020-01-19 14:01:59', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc6476e30024', 4, 'ff8080816f660586016f8e01a3a30002', '根据活动id查询 人群运营计划', NULL, '/customerplan/activity/*', 'GET', NULL, 2, '2020-01-19 14:01:38', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc6276320023', 4, 'ff8080816f660586016f8e01f7dd0003', '分页查询短信签名', NULL, '/smssign/page', 'POST', NULL, 5, '2020-01-19 13:59:27', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc622d9d0022', 4, 'ff8080816f660586016f8e023cbb0004', '分页查询短信签名', NULL, '/smssign/page', 'POST', NULL, 5, '2020-01-19 13:59:08', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc61b9d50021', 4, 'ff8080816f660586016f8e023cbb0004', '分页查询短信模板', NULL, '/smstemplate/page', 'POST', NULL, 4, '2020-01-19 13:58:39', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc6164550020', 4, 'ff8080816f660586016f8e01f7dd0003', '分页查询短信模板', NULL, '/smstemplate/page', 'POST', NULL, 4, '2020-01-19 13:58:17', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc605c5b001f', 4, 'ff8080816f660586016f8e01f7dd0003', '会员分群人数查询', NULL, '/customer/group/customer-total', 'POST', NULL, 3, '2020-01-19 13:57:09', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc601b19001e', 4, 'ff8080816f660586016f8e023cbb0004', '会员分群人数查询', NULL, '/customer/group/customer-total', 'POST', NULL, 3, '2020-01-19 13:56:53', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc5d115a001d', 4, 'ff8080816f660586016f8e023cbb0004', '根据条件获取分群信息列表', NULL, '/customer/group/queryGroupInfoList', 'POST', NULL, 2, '2020-01-19 13:53:34', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fbc5cc2c3001c', 4, 'ff8080816f660586016f8e01f7dd0003', '根据条件获取分群信息列表', NULL, '/customer/group/queryGroupInfoList', 'POST', NULL, 2, '2020-01-19 13:53:13', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fb27de804001b', 4, 'ff8080816f660586016f8e0141a70001', '根据条件获取分群信息列表', NULL, '/customer/group/queryGroupInfoList', 'POST', NULL, 2, '2020-01-17 15:53:13', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac458423001a', 4, 'ff8080816f660586016fac4377a30013', '会员等级列表', NULL, '/customer/levellist', 'GET', NULL, 8, '2020-01-16 10:53:55', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac453c730019', 4, 'ff8080816f660586016fac4377a30013', '标签查询', NULL, '/customertag/list', 'POST', NULL, 7, '2020-01-16 10:53:36', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac44f1bd0018', 4, 'ff8080816f660586016fac4377a30013', '删除自定义分群信息', NULL, '/crm/customgroup/*', 'DELETE', NULL, 6, '2020-01-16 10:53:17', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac44a0ff0017', 4, 'ff8080816f660586016fac4377a30013', '通过ID查询自定义分群信息', NULL, '/crm/customgroup/*', 'GET', NULL, 4, '2020-01-16 10:52:56', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac4448010016', 4, 'ff8080816f660586016fac4377a30013', '修改自定义人群', NULL, '/crm/customgroup/modify', 'PUT', NULL, 3, '2020-01-16 10:52:34', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac440aec0015', 4, 'ff8080816f660586016fac4377a30013', '新增自定义人群', NULL, '/crm/customgroup/add', 'POST', NULL, 2, '2020-01-16 10:52:18', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac43b9a40014', 4, 'ff8080816f660586016fac4377a30013', '自定义人群查询', NULL, '/crm/customgroup/page', 'POST', NULL, 1, '2020-01-16 10:51:57', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac357eb00012', 4, 'ff8080816f660586016fac34128c000f', '分页查询短信模板', NULL, '/smstemplate/page', 'POST', NULL, 3, '2020-01-16 10:36:25', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac352d5a0011', 4, 'ff8080816f660586016fac34128c000f', '分页查询短信签名', NULL, '/smssign/page', 'POST', NULL, 2, '2020-01-16 10:36:04', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816f660586016fac34e0bf0010', 4, 'ff8080816f660586016fac34128c000f', '新增短信发送任务', NULL, '/sms-send/add', 'POST', NULL, 1, '2020-01-16 10:35:44', 0);
update function_info set del_flag = 1 where function_id = '8a9bc76c6f6b7aa2016f6bdee9510001';

update menu_info set sort = 9 where menu_id = 'fc8e20ff3fe311e9828800163e0fc468';
update menu_info set sort = 10 where menu_id = 'fc8df1663fe311e9828800163e0fc468';
-- sbc-setting start ---


-- xxl-job --
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (67, 6, '0 0 9 * * ?', '运营计划短信发送', '2020-01-16 19:47:43', '2020-01-16 19:47:43', '张高磊', '', 'FIRST', 'customerPlanJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-01-16 19:47:43', '');

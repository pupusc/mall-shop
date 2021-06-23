-- ***************************************** xxl-job begin *********************************************
UPDATE `xxl_job_qrtz_trigger_info` SET `job_cron` = '0 0 0 * * ?' WHERE `executor_handler` = 'orderGrowthValueIncreaseJobHandler';
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0 2 * * ?', '会员积分-扣除客户过期积分', '2019-05-13 15:21:43', '2019-05-13 15:21:43', '闵晨', '', 'FIRST', 'customerPointsExpireJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-05-13 15:21:43', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0 1 * * ?', '积分商品兑换活动到期自动关闭', '2019-05-27 13:57:00', '2019-05-27 13:57:00', '李杨洋', '', 'FIRST', 'pointsGoodsJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-05-27 13:57:00', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0 5 * * ?', '积分订单自动确认收货', '2019-05-28 10:26:23', '2019-05-28 10:26:23', '吕振伟', '', 'FIRST', 'pointsOrderAutoReceiveJobHandle', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-05-28 10:26:23', '');
-- ***************************************** xxl-job end *********************************************





-- ***************************************** pay begin *********************************************
-- 支付网关配置增加微信证书字段
ALTER TABLE `pay_gateway_config`
ADD COLUMN `wx_pay_certificate` blob NULL COMMENT '微信公众平台支付证书' AFTER `open_platform_account`,
ADD COLUMN `wx_open_pay_certificate` blob NULL COMMENT '微信开放平台支付证书' AFTER `wx_pay_certificate`;
-- ***************************************** pay end *********************************************





-- ***************************************** setting begin*********************************************
ALTER TABLE `system_points_config`
ADD COLUMN `points_expire_month` int(4) NULL COMMENT '积分过期月份' AFTER `status`,
ADD COLUMN `points_expire_day` int(4) NULL COMMENT '积分过期日期' AFTER `points_expire_month`;

UPDATE system_points_config SET points_expire_month = 0, points_expire_day =0;

-- 历史遗留问题，会员成长值明细的权限与menu不对应导致无法分配权限
UPDATE `authority` SET `request_type` = 'GET' WHERE `authority_id` = '2c93a4886979daa501697a386f97000c';
UPDATE `function_info` SET `menu_id` = 'fc8df6503fe311e9828800163e0fc468' WHERE `function_id` = '2c93a4886979daa501697a36fcd2000a';


INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('growth_value_basic_rule', 'growth_value_basic_rule_share_register', '分享注册', '仅被注册成功后才可获得相应奖励成长值', 0, '{"value":"","limit":""}', NULL, NULL, 0);
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('growth_value_basic_rule', 'growth_value_basic_rule_share_buy', '分享购买', '仅被购买成功后才可获得相应奖励成长值', 0, '{"value":"","limit":""}', NULL, NULL, 0);
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('points_basic_rule', 'points_basic_rule_share_register', '分享注册', '仅被注册成功后才可获得相应奖励积分', 0, '{"value":"","limit":""}', NULL, NULL, 0);
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('points_basic_rule', 'points_basic_rule_share_buy', '分享购买', '仅被购买成功后才可获得相应奖励积分', 0, '{"value":"","limit":""}', NULL, NULL, 0);

update `menu_info` set menu_name = '优惠券活动' where menu_name = '优惠券活动列表';
update `menu_info` set menu_name = '对象存储' where menu_name = '素材服务器接口';
update `menu_info` set menu_name = '促销活动' where menu_name = '促销活动列表';
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826add25d9016adda27da40000', 3, 'fc8dfb2a3fe311e9828800163e0fc468', 3, '积分订单', '/points-order-list', NULL, 18, '2019-05-22 11:42:55', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a3e7510014', 4, 'fc8e20ff3fe311e9828800163e0fc468', 2, '积分商城', NULL, '1496720232614.jpg', 3, '2019-05-08 16:51:25', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076abe297e016abe6441620000', 4, '40286c826a950c54016a96a3e7510014', 3, '积分商品', '/points-goods-list', null, 2, '2019-05-16 10:06:43', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1bec0c85000a', 4, 'fc8df45f3fe311e9828800163e0fc468', 3, '积分订单', '/points-order-list', NULL, 125, '2019-06-03 13:59:43', 0);

INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a6b3e6001a', 4, '8ab369076b15c29e016b1bec0c85000a', '积分订单详情查看', 'f_points_order_list_004', NULL, 3, '2019-05-08 16:54:29', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a6042f0018', 4, '8ab369076b15c29e016b1bec0c85000a', '积分订单导出', 'f_points_order_list_003', NULL, 2, '2019-05-08 16:53:44', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a50aa90016', 4, '8ab369076b15c29e016b1bec0c85000a', '积分订单列表查看', 'f_points_order_list_001', NULL, 1, '2019-05-08 16:52:40', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a9699f66e000e', 3, '40286c826add25d9016adda27da40000', '积分订单发货', 'f_points_order_list_005', NULL, 5, '2019-05-08 16:40:34', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a9695d4f10009', 3, '40286c826add25d9016adda27da40000', '积分订单详情查看', 'f_points_order_list_004', NULL, 4, '2019-05-08 16:36:03', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969470ee0007', 3, '40286c826add25d9016adda27da40000', '积分订单导出', 'f_points_order_list_003', NULL, 3, '2019-05-08 16:34:32', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969258780005', 3, '40286c826add25d9016adda27da40000', '订单确认收货', 'f_points_order_list_002', NULL, 2, '2019-05-08 16:32:14', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a968fea340003', 3, '40286c826add25d9016adda27da40000', '积分订单列表查看', 'f_points_order_list_001', NULL, 1, '2019-05-08 16:29:35', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b47b0040000', 4, '8ab369076abe297e016abe6441620000', '积分商品编辑', 'f_points_goods_edit', null, 4, '2019-06-03 11:00:11', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076abe297e016abe6df4e50005', 4, '8ab369076abe297e016abe6441620000', '积分商品删除', 'f_points_goods_del', null, 3, '2019-05-16 10:17:18', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076abe297e016abe6adacd0003', 4, '8ab369076abe297e016abe6441620000', '积分商品添加', 'f_points_goods_add', null, 2, '2019-05-16 10:13:55', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076abe297e016abe673f730001', 4, '8ab369076abe297e016abe6441620000', '积分商品列表查看', 'f_points_goods_list', null, 1, '2019-05-16 10:09:59', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b56e23b0005', 4, '8ab369076abe297e016abe6441620000', '积分商品分类删除', 'f_points_goods_cate_del', null, 8, '2019-06-03 11:16:47', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b5674df0004', 4, '8ab369076abe297e016abe6441620000', '积分商品分类编辑', 'f_points_goods_cate_edit', null, 7, '2019-06-03 11:16:19', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b55cdf00003', 4, '8ab369076abe297e016abe6441620000', '积分商品分类添加', 'f_points_goods_cate_add', null, 6, '2019-06-03 11:15:37', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b5466ce0002', 4, '8ab369076abe297e016abe6441620000', '积分商品分类列表查看', 'f_points_goods_cate_list', null, 5, '2019-06-03 11:14:05', 0);

INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a7e7b2001c', 4, '40286c826a950c54016a96a6b3e6001a', '积分订单付款单信息', NULL, '/account/payOrders', 'POST', NULL, 2, '2019-05-08 16:55:47', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a71f96001b', 4, '40286c826a950c54016a96a6b3e6001a', '积分订单详情查看', NULL, '/points/trade/*', 'GET', NULL, 1, '2019-05-08 16:54:56', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a6569f0019', 4, '40286c826a950c54016a96a6042f0018', '订单导出', NULL, '/points/trade/export/params/*', 'GET', NULL, 1, '2019-05-08 16:54:05', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96a5a07f0017', 4, '40286c826a950c54016a96a50aa90016', '积分订单列表查看', NULL, '/points/trade', 'POST', NULL, 1, '2019-05-08 16:53:18', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969f128a0013', 3, '40286c826a950c54016a9699f66e000e', '积分订单作废发货', NULL, '/points/trade/deliver/*/void/*', 'POST', NULL, 5, '2019-05-08 16:46:09', 1);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969e64de0012', 3, '40286c826a950c54016a9699f66e000e', '店铺物流公司', NULL, '/store/expressCompany', 'GET', NULL, 4, '2019-05-08 16:45:24', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969e05400011', 3, '40286c826a950c54016a9699f66e000e', '积分订单订单发货', NULL, '/points/trade/deliver/*', 'PUT', NULL, 3, '2019-05-08 16:45:00', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969d77210010', 3, '40286c826a950c54016a9699f66e000e', '积分订单发货前验证', NULL, '/points/trade/deliver/verify/*', 'POST', NULL, 2, '2019-05-08 16:44:23', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969a82b0000f', 3, '40286c826a950c54016a9699f66e000e', '积分订单详情查看', NULL, '/points/trade/*', 'GET', NULL, 1, '2019-05-08 16:41:10', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96982ff0000d', 3, '40286c826a950c54016a9695d4f10009', '店铺物流公司', NULL, '/store/expressCompany', 'GET', NULL, 4, '2019-05-08 16:38:37', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a9697b92a000c', 3, '40286c826a950c54016a9695d4f10009', '积分订单付款单信息', NULL, '/account/payOrders', 'POST', NULL, 3, '2019-05-08 16:38:07', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96974938000b', 3, '40286c826a950c54016a9695d4f10009', '积分订单详情查看', NULL, '/points/trade/*', 'GET', NULL, 2, '2019-05-08 16:37:38', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96965ada000a', 3, '40286c826a950c54016a9695d4f10009', '积分订单备注', NULL, '/points/trade/remark/*', 'POST', NULL, 1, '2019-05-08 16:36:37', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a969500850008', 3, '40286c826a950c54016a969470ee0007', '订单导出', NULL, '/points/trade/export/params/*', 'GET', NULL, 1, '2019-05-08 16:35:09', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a9693ba640006', 3, '40286c826a950c54016a969258780005', '积分订单确认收货', NULL, '/points/trade/confirm/*', 'GET', NULL, 1, '2019-05-08 16:33:45', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('40286c826a950c54016a96914d460004', 3, '40286c826a950c54016a968fea340003', '积分订单列表查看', NULL, '/points/trade', 'POST', NULL, 1, '2019-05-08 16:31:06', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b4c93570001', 4, '8ab369076b15c29e016b1b47b0040000', '积分商品编辑', null, '/pointsgoods/modify', 'PUT', null, 1, '2019-06-03 11:05:32', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076abe297e016abe6ed3dd0006', 4, '8ab369076abe297e016abe6df4e50005', '积分商品删除', null, '/pointsgoods/*', 'DELETE', null, 1, '2019-05-16 10:18:15', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076abe297e016abe6bfa290004', 4, '8ab369076abe297e016abe6adacd0003', '积分商品添加', null, '/pointsgoods/batchAdd', 'POST', null, 1, '2019-05-16 10:15:09', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076abe297e016abe68f31d0002', 4, '8ab369076abe297e016abe673f730001', '积分商品列表查看', null, '/pointsgoods/page', 'POST', null, 1, '2019-05-16 10:11:50', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b59423d0009', 4, '8ab369076b15c29e016b1b56e23b0005', '积分商品分类删除', null, '/pointsgoodscate/*', 'DELETE', null, 1, '2019-06-03 11:19:23', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b5899f30008', 4, '8ab369076b15c29e016b1b5674df0004', '积分商品分类编辑', null, '/pointsgoodscate/modify', 'PUT', null, 1, '2019-06-03 11:18:40', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b58207a0007', 4, '8ab369076b15c29e016b1b55cdf00003', '积分商品分类添加', null, '/pointsgoodscate/add', 'POST', null, 1, '2019-06-03 11:18:09', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1b5794210006', 4, '8ab369076b15c29e016b1b5466ce0002', '积分商品分类列表查看', null, '/pointsgoodscate/list', 'POST', null, 1, '2019-06-03 11:17:33', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1d28c95c0014', 4, '8ab369076abe297e016abe6adacd0003', '积分商品上传文件', null, '/pointsgoods/excel/upload', 'POST', null, 6, '2019-06-03 19:45:41', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1d0685dd0013', 4, '8ab369076abe297e016abe6adacd0003', '积分商品导入', null, '/pointsgoods/import/*', 'GET', null, 5, '2019-06-03 19:08:15', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1d0378320011', 4, '8ab369076abe297e016abe6adacd0003', '积分商品导入错误下载', null, '/pointsgoods/excel/err/*/*', 'GET', null, 4, '2019-06-03 19:04:55', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1d01bd1c0010', 4, '8ab369076abe297e016abe6adacd0003', '积分商品导入模板下载', null, '/pointsgoods/excel/template/*', 'GET', null, 3, '2019-06-03 19:03:02', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1cfd4bfc000f', 4, '8ab369076abe297e016abe673f730001', '积分商品启用停用', null, '/pointsgoods/modifyStatus', 'PUT', null, 2, '2019-06-03 18:58:11', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1cfa7234000e', 4, '8ab369076b15c29e016b1b5466ce0002', '积分商品分类拖拽排序', null, '/pointsgoodscate/editSort', 'PUT', null, 2, '2019-06-03 18:55:04', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab369076b15c29e016b1ceefb8b000b', 4, '8ab369076abe297e016abe6adacd0003', '积分商品获取商品列表', null, '/goods/skus', 'POST', null, 2, '2019-06-03 18:42:32', 0);
-- ***************************************** setting end*********************************************





-- ***************************************** customer begin *********************************************
ALTER TABLE `customer_points_detail`
MODIFY COLUMN `service_type` tinyint(4) NOT NULL COMMENT '业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买 5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信 9添加收货地址 10关注店铺 11订单完成 12订单抵扣 13优惠券兑换 14积分兑换 15退单返还 16订单取消返还 17过期扣除' AFTER `type`;
ALTER TABLE `store_evaluate`
ADD COLUMN `buy_time` datetime(0) NULL COMMENT '购买时间' AFTER `order_no`;
-- ***************************************** customer end ***********************************************





-- ***************************************** account begin *********************************************

ALTER TABLE `reconciliation` ADD COLUMN `points` BIGINT(20) NULL COMMENT '积分' AFTER `amount`;

-- ***************************************** account end ***********************************************





-- ***************************************** goods begin *********************************************
-- 初始化商品评价表点赞数
update goods_evaluate set good_num = 0;

-- 会员商品评价点赞关联表
CREATE TABLE `customer_goods_evaluate_praise` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员id',
  `goods_evaluate_id` varchar(32) DEFAULT NULL COMMENT '商品评价id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_customer_evaluate` (`customer_id`,`goods_evaluate_id`) USING BTREE COMMENT '会员id和评价id唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员商品评价点赞关联表';

ALTER TABLE `store_tobe_evaluate`
ADD COLUMN `buy_time` datetime(0) NULL COMMENT '购买时间' AFTER `order_no`;

-- 积分商品表
CREATE TABLE `points_goods` (
  `points_goods_id` varchar(32) NOT NULL COMMENT '积分商品id',
  `goods_id` varchar(32) NOT NULL COMMENT 'SpuId',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SkuId',
  `cate_id` tinyint(4) DEFAULT NULL COMMENT '分类id',
  `stock` bigint(10) DEFAULT NULL COMMENT '库存',
  `sales` bigint(10) NOT NULL DEFAULT '0' COMMENT '销量',
  `settlement_price` decimal(20,2) DEFAULT NULL COMMENT '结算价格',
  `points` bigint(10) DEFAULT NULL COMMENT '兑换积分',
  `status` tinyint(4) DEFAULT NULL COMMENT '是否启用 0：停用，1：启用',
  `recommend_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '推荐标价, 0: 未推荐 1: 已推荐',
  `begin_time` datetime DEFAULT NULL COMMENT '兑换开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '兑换结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0: 未删除 1: 已删除',
  PRIMARY KEY (`points_goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分商品表';

-- 积分商品分类表
CREATE TABLE `points_goods_cate` (
  `cate_id` tinyint(4) NOT NULL AUTO_INCREMENT COMMENT '积分商品分类主键',
  `cate_name` varchar(45) NOT NULL COMMENT '分类名称',
  `sort` tinyint(10) DEFAULT '0' COMMENT '排序 默认0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0: 未删除 1: 已删除',
  PRIMARY KEY (`cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分商品分类表';
-- ***************************************** goods end *********************************************





-- ***************************************** marketing begin *********************************************
-- ***************************************** marketing end ***********************************************





-- ***************************************** order begin *********************************************
ALTER TABLE pay_order ADD COLUMN pay_order_points BIGINT(20) DEFAULT NULL COMMENT '支付单积分'
-- ***************************************** order end ***********************************************
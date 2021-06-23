-- pay_gateway_config 添加银联b2b的相关字段
ALTER TABLE `pay_gateway_config`
  ADD COLUMN `pc_back_url` varchar(255) NULL COMMENT 'PC前端后台接口地址',
  ADD COLUMN `pc_web_url` varchar(255) NULL COMMENT 'PC前端web地址',
  ADD COLUMN `boss_back_url` varchar(255) NULL COMMENT 'boss后台接口地址';

-- 添加pay_gateway数据
INSERT INTO `pay_gateway`(`id`, `name`, `is_open`, `type`, `create_time`) VALUES (2, 'UNIONB2B', 1, 0, now());

-- 添加pay_gateway_config数据
INSERT INTO `pay_gateway_config`(`id`, `gateway_id`, `api_key`, `secret`, `account`, `app_id`, `app_id2`,
                                 `private_key`, `public_key`, `create_time`, `pc_back_url`, `pc_web_url`, `boss_back_url`) VALUES (2, 2, 'test', '', 'test', '', '', '', '', '2018-10-11 14:14:40', 'http://localhost:8000', 'http://localhost:8000', 'http://localhost:8000');

-- 添加pay_channel_item数据
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (11, '银联b2b', 2, 'unionpay_b2b', 1, 0, 'unionpay_b2b', '2018-10-11 14:16:57');
INSERT INTO pay_channel_item (id, name, gateway_id, channel, is_open, terminal, code, create_time) VALUES (12, '银联b2b微信', 2, 'unionpay_b2b', 1, 1, 'unionpay_b2b_wx', '2018-10-19 16:28:44');
INSERT INTO pay_channel_item (id, name, gateway_id, channel, is_open, terminal, code, create_time) VALUES (13, '银联b2bAPP', 2, 'unionpay_b2b', 1, 2, 'unionpay_b2b_app', '2018-10-19 16:29:36');

-- boss管理平台邮箱接口配置表
CREATE TABLE `system_email_config` (
  `email_config_id` varchar(32) NOT NULL COMMENT '邮箱配置Id',
  `from_email_address` varchar(50) DEFAULT NULL COMMENT '发信人邮箱地址',
  `from_person` varchar(20) DEFAULT NULL COMMENT '发信人',
  `email_smtp_host` varchar(20) DEFAULT NULL COMMENT 'smtp服务器主机名',
  `email_smtp_port` varchar(10) DEFAULT NULL COMMENT 'smtp服务器端口号',
  `auth_code` varchar(20) DEFAULT NULL COMMENT 'smtp服务器授权码',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用标志 0：停用，1：启用',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`email_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='boss管理平台邮箱接口配置表';

-- 客户财务邮箱配置表
CREATE TABLE `customer_email` (
  `customer_email_id` varchar(32) NOT NULL COMMENT '用户财务邮箱主键Id（UUID）',
  `customer_id` varchar(32) NOT NULL COMMENT '邮箱所属客户Id',
  `email_address` varchar(32) NOT NULL COMMENT '邮箱地址',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`customer_email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户财务邮箱配置表';

-- 订单支付顺序设置及超时未支付取消订单设置
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('order_setting', 'order_setting_payment_order', '订单支付顺序', '', 1, '', NOW(), null, 0);
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('order_setting', 'order_setting_timeout_cancel', '订单超时失效', '', 0, '{\"hour\":24}', NOW(), null, 0);

-- 定时任务：5分钟扫描一次超时未支付订单
INSERT INTO `scheduler_job`(`scheduler_id`, `job_key`, `job_group`, `description`, `expression`, `job_class`, `last_execute_time`, `job_status`, `delete_flag`) VALUES ('order-timeout-cancel', 'order-cancel', 'order-group', 'the job for analyse the order timeout', '0 0/5 * * * ?', 'com.wanmi.sbc.job.OrderTimeoutCancelJob', '2018-10-15 19:50:00', NULL, 0);

-- BOSS管理后台邮箱接口菜单
INSERT INTO `menu_info`(`system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES (4, 144, 3, '邮箱接口', '/email-interface', NULL, 151, NOW(), 0);

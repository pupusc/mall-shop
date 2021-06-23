-- 在线支付
-- 财务对账明细新增交易流水号字段
ALTER TABLE `reconciliation`
  ADD COLUMN `trade_no` varchar(32) NULL COMMENT '交易流水号';

-- 微信支付app支付，微信配置增加微信开放平台参数
ALTER TABLE `pay_gateway_config`
ADD COLUMN `open_platform_app_id` varchar(40) NULL COMMENT '微信开放平台app_id---微信app支付使用' AFTER `boss_back_url`,
ADD COLUMN `open_platform_secret` varchar(66) NULL COMMENT '微信开放平台secret---微信app支付使用' AFTER `open_platform_app_id`,
ADD COLUMN `open_platform_api_key` varchar(66) NULL COMMENT '微信开放平台api key---微信app支付使用' AFTER `open_platform_secret`,
ADD COLUMN `open_platform_account` varchar(60) NULL COMMENT '微信开放平台商户号---微信app支付使用' AFTER `open_platform_api_key`;

-- 微信支付相关
INSERT  INTO  `pay_gateway`(`id`,  `name`,  `is_open`,  `type`,  `create_time`)  VALUES  (3,  'WECHAT',  1,  0,  '2019-01-24  15:07:15');
INSERT INTO `pay_gateway`(`id`, `name`, `is_open`, `type`, `create_time`) VALUES (4, 'ALIPAY', 1, 0, '2019-02-19 11:02:17');

INSERT INTO `pay_gateway_config`(`id`, `gateway_id`, `api_key`, `secret`, `account`, `app_id`, `app_id2`, `private_key`, `public_key`, `create_time`, `pc_back_url`, `pc_web_url`, `boss_back_url`, `open_platform_app_id`, `open_platform_secret`, `open_platform_api_key`, `open_platform_account`) VALUES (3, 3, 'vBjQcpge1HXboGbJqpisY8DdNIMUhpht', 'e0108ff8a4893e48feb6f7cd5bd3b4e8', '1489104242', 'wxf7e90ffd3bee627a', '', '', '', '2019-01-24 15:36:57', '', '', 'https://bossbff.s2btest2.kstore.shop', 'wx77ec33e47647ae75', 'd829cd03e1e5a6dc98c92375d85cc043', 'ABCDEFGhijklmn12345677654321opqr', '1489562032');
INSERT INTO `pay_gateway_config`(`id`, `gateway_id`, `api_key`, `secret`, `account`, `app_id`, `app_id2`, `private_key`, `public_key`, `create_time`, `pc_back_url`, `pc_web_url`, `boss_back_url`, `open_platform_app_id`, `open_platform_secret`, `open_platform_api_key`, `open_platform_account`) VALUES (4, 4, '1', '', '', '2019011663011858', '', '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', '2019-02-19 11:09:28', '', '', 'https://bossbff.s2btest2.kstore.shop/', '', '', '', '');




-- 以下sql涉及 将菜单,功能,权限相关表主键修改为varchar类型
ALTER TABLE `authority`
ADD COLUMN `new_authority_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `del_flag`,
ADD COLUMN `new_function_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `new_authority_id`;

ALTER TABLE `function_info`
ADD COLUMN `new_function_id` varchar(50) NULL AFTER `del_flag`,
ADD COLUMN `new_menu_id` varchar(50) NULL AFTER `new_function_id`;

ALTER TABLE `menu_info`
ADD COLUMN `new_menu_id` varchar(50) NULL AFTER `del_flag`,
ADD COLUMN `new_parent_menu_id` varchar(50) NULL AFTER `new_menu_id`;

ALTER TABLE `authority`
MODIFY COLUMN `authority_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限id' FIRST,
MODIFY COLUMN `function_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能id' AFTER `system_type_cd`;

ALTER TABLE `function_info`
MODIFY COLUMN `function_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '功能id' FIRST,
MODIFY COLUMN `menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单id' AFTER `system_type_cd`;

ALTER TABLE `menu_info`
MODIFY COLUMN `menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单id' FIRST,
MODIFY COLUMN `parent_menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父菜单id' AFTER `system_type_cd`;

ALTER TABLE `role_function_rela`
MODIFY COLUMN `function_id` varchar(32) NOT NULL COMMENT '功能id' AFTER `role_info_id`;

ALTER TABLE `role_menu_rela`
MODIFY COLUMN `menu_id` varchar(32) NOT NULL COMMENT '菜单id' AFTER `role_info_id`;


UPDATE menu_info set new_menu_id = uuid();
update menu_info set new_menu_id = replace(new_menu_id,'-','');
UPDATE function_info set new_function_id = uuid();
update function_info set new_function_id = replace(new_function_id,'-','');
UPDATE authority set new_authority_id = uuid();
update authority set new_authority_id = replace(new_authority_id,'-','');

update menu_info c join menu_info p on c.parent_menu_id = p.menu_id
set c.new_parent_menu_id = p.new_menu_id;
update menu_info set new_parent_menu_id = '0' where new_parent_menu_id is null and menu_grade = 1;

update function_info f join menu_info m on f.menu_id = m.menu_id
set f.new_menu_id = m.new_menu_id;

update authority a join function_info f on a.function_id = f.function_id
set a.new_function_id = f.new_function_id;

UPDATE role_function_rela f
JOIN function_info m ON f.function_id = m.function_id
SET f.function_id = m.new_function_id;

UPDATE role_menu_rela f
JOIN menu_info m ON f.menu_id = m.menu_id
SET f.menu_id = m.new_menu_id;


update authority set authority_id = new_authority_id, function_id = new_function_id;
update menu_info set menu_id = new_menu_id, parent_menu_id = new_parent_menu_id;
update function_info set function_id = new_function_id, menu_id = new_menu_id;

ALTER TABLE `authority`
DROP COLUMN `new_authority_id`,
DROP COLUMN `new_function_id`;

ALTER TABLE `function_info`
DROP COLUMN `new_function_id`,
DROP COLUMN `new_menu_id`;

ALTER TABLE `menu_info`
DROP COLUMN `new_menu_id`,
DROP COLUMN `new_parent_menu_id`;


alter table pay_gateway_config modify column private_key varchar(3000) COMMENT '私钥';

-- boss支付宝配置权限
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab36907694cb09801694cdf653c0002', 4, '8ab36907694cb09801694cdd10ff0000', '支付宝网关详情', NULL, '/tradeManage/gateways', 'GET', NULL, 1, '2019-03-05 16:01:43', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab36907694cb09801694ce00f5b0003', 4, '8ab36907694cb09801694cdde6920001', '保存支付宝网关', NULL, '/tradeManage/save', 'POST', NULL, 1, '2019-03-05 16:02:26', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab36907694cb09801694cdd10ff0000', 4, 'dbceb9733f1211e99d7100163e1249d4', '支付宝配置查看', 'f_alipay_query', NULL, 7, '2019-03-05 15:59:10', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab36907694cb09801694cdde6920001', 4, 'dbceb9733f1211e99d7100163e1249d4', '支付宝配置编辑', 'f_alipay_edit', NULL, 8, '2019-03-05 16:00:05', 0);

INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769509da40169521ae2bd0002', 4, '8ab3690769509da40169521987d80000', '微信网关详情', NULL, '/tradeManage/gateways', 'GET', NULL, 1, '22019-03-06 16:24:48', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8ab3690769509da40169521bae880003', 4, '8ab3690769509da40169521a3ab80001', '保存微信网关', NULL, '/tradeManage/save', 'POST', NULL, 1, '2019-03-06 16:25:40', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`,`sort`, `create_time`, `del_flag`) VALUES ('8ab3690769509da40169521987d80000', 4, 'dbceb9733f1211e99d7100163e1249d4', '微信配置查看', 'f_wechat_query', NULL, 9, '2019-03-06 16:23:19', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`,`sort`, `create_time`, `del_flag`) VALUES ('8ab3690769509da40169521a3ab80001', 4, 'dbceb9733f1211e99d7100163e1249d4', '微信配置编辑', 'f_wechat_edit', NULL, 10, '2019-03-06 16:24:05', 0);

-- 支付宝、微信支付配置
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (14, '微信扫码支付', 3, 'WeChat', 1, 0, 'wx_qr_code', '2019-01-24 17:15:02');
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (15, '微信支付h5支付', 3, 'WeChat', 1, 1, 'wx_mweb', '2019-01-30 09:50:24');
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (16, '微信支付jsapi支付', 3, 'WeChat', 1, 1, 'js_api', '2019-01-30 09:50:24');
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (17, '支付宝支付', 4, 'Alipay', 1, 0, 'alipay_pc', '2019-02-19 11:00:40');
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (18, '支付宝H5支付', 4, 'Alipay', 1, 1, 'alipay_h5', '2019-02-19 11:03:54');
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (19, '支付宝APP支付', 4, 'Alipay', 1, 2, 'alipay_app', '2019-02-19 11:04:35');
INSERT INTO `pay_channel_item`(`id`, `name`, `gateway_id`, `channel`, `is_open`, `terminal`, `code`, `create_time`) VALUES (20, '微信支付App支付', 3, 'WeChat', 1, 2, 'wx_app', '2019-01-30 09:50:24');
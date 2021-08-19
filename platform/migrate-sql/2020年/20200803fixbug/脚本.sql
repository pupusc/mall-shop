-- 解决魔方显示过慢
ALTER TABLE `sbc-goods`.`goods_info`
ADD INDEX(`added_flag`),
ADD INDEX(`audit_status`),
ADD INDEX(`stock`);

ALTER TABLE `sbc-goods`.`groupon_goods_info`
ADD INDEX(`goods_info_id`);

-- boss小程序接口权限调整
update `sbc-setting`.`authority` set function_id = 'ff808081730fdc4b017322dc391a0026' where authority_id = 'ff808081730fdc4b017322e29ef9002f';
update `sbc-setting`.`authority` set function_id = 'ff808081730fdc4b017322dc391a0026' where authority_id = 'ff808081730fdc4b017322e53de50034';
update `sbc-setting`.`authority` set function_id = 'ff808081730fdc4b017322dc391a0026' where authority_id = 'ff808081730fdc4b017322dcf1000027';
delete from `sbc-setting`.`function_info` where function_id in ('ff808081730fdc4b017322e0d75b002d','ff808081730fdc4b017322e415eb0032');




-- boss端订单列表查看权限
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817402f8c50174058e62720000', 4, 'fc9370353fe311e9828800163e0fc468', '订单列表查看', 'boss端查看订单', '/trade/bossPage', 'POST', NULL, 1, '2020-08-19 15:10:52', 0);
-- 商家端新增活动权限
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817400fe4e0174020a7f740001', 3, 'ff80808172d9abf50172fe28b7fe0005', '校验商品数据', NULL, '/marketing/sku/exists', 'POST', NULL, 2, '2020-08-18 22:47:57', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817400fe4e01740209144f0000', 3, 'ff80808171a672390171a6d37a1f0006', '校验商品数据', NULL, '/marketing/sku/exists', 'POST', NULL, 2, '2020-08-18 22:46:24', 0);

-- 0828新增权限sql
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174334738017433c2fabc0003', 3, 'fc9279053fe311e9828800163e0fc468', '分页查询订单', NULL, '/trade/supplierPage', 'POST', NULL, 1, '2020-08-28 14:30:50', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174334738017433be47af0002', 3, 'fc924c7c3fe311e9828800163e0fc468', '查询平台会员信息', NULL, '/store/allBossCustomers', 'POST', NULL, 1, '2020-08-28 14:25:42', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174334738017433bc7ab50001', 3, 'fc924c7c3fe311e9828800163e0fc468', '检查商品中是否有企业购货品', NULL, '/enterprise/enterprise-check', 'POST', NULL, 1, '2020-08-28 14:23:44', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174334738017433bab29e0000', 3, 'fc924c7c3fe311e9828800163e0fc468', '查询是否是分销商品', NULL, '/goods/distribution-check/*', 'GET', NULL, 1, '2020-08-28 14:21:48', 0);
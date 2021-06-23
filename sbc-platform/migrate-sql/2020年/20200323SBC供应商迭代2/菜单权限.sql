INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a7cab530003', 4, 'fc8df7df3fe311e9828800163e0fc468', 3, '供应商结算', '/finance-manage-provider-settle', NULL, 4, '2020-03-24 11:01:15', 0);

INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('fc9262923fe311e9818800163e0fc468', 4, 'ff80808170f2a69001710a7cab530003', '结算列表查看', 'm_finance_manage_provider_settle_1', NULL, 1, NULL, 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('fc9262e23fe311e9823800163e0fc468', 4, 'ff80808170f2a69001710a7cab530003', '结算明细查看', 'm_billing_provider_details_1', NULL, 2, NULL, 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('fc9263313fe311e9828890163e0fc468', 4, 'ff80808170f2a69001710a7cab530003', '结算单结算', 'm_billing_provider_details_2', NULL, 3, NULL, 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('fc926386382311e9828800163e0fc468', 4, 'ff80808170f2a69001710a7cab530003', '结算明细导出', 'm_billing_provider_details_3', NULL, 4, NULL, 0);


INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8a30510004', 4, 'fc9262923fe311e9818800163e0fc468', '获取结算单列表', NULL, '/finance/provider/settlement/page', 'POST', NULL, 1, '2020-03-24 11:16:01', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8afd210005', 4, 'fc9262923fe311e9818800163e0fc468', '根据店铺名称查询店铺', NULL, '/store/provider/name', 'GET', NULL, 1, '2020-03-24 11:16:53', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8be15e0006', 4, 'fc9262e23fe311e9823800163e0fc468', '获取结算明细列表', NULL, '/finance/provider/settlement/detail/list/*', 'GET', NULL, 1, '2020-03-24 11:17:52', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8c57240007', 4, 'fc9262e23fe311e9823800163e0fc468', '更改结算单状态', NULL, '/finance/provider/settlement/*', 'GET', NULL, 1, '2020-03-24 11:18:22', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8cc4ad0008', 4, 'fc9262e23fe311e9823800163e0fc468', '更改结算单状态', NULL, '/finance/provider/settlement/status', 'PUT', NULL, 1, '2020-03-24 11:18:50', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8d90e50009', 4, 'fc9263313fe311e9828890163e0fc468', '更改结算单状态', NULL, '/finance/provider/settlement/status', 'PUT', NULL, 1, '2020-03-24 11:19:42', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8de9de000a', 4, 'fc9263313fe311e9828890163e0fc468', '结算批量导出', NULL, '/finance/provider/settlement/export/*', 'GET', NULL, 2, '2020-03-24 11:20:05', 0);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a8eb3b1000b', 4, 'fc926386382311e9828800163e0fc468', '结算明细导出', NULL, '/finance/provider/settlement/detail/export/*', 'GET', NULL, 1, '2020-03-24 11:20:56', 0);


UPDATE menu_info set menu_name = "商家结算" where menu_name ="财务结算" and del_flag = 0 and system_type_cd = 4;

UPDATE authority set authority_url = '/store/supplier/name' WHERE authority_title = '根据店铺名称查询店铺' and authority_url = '/store/name';

-- 重整商品库菜单
Delete from  `sbc-setting`.`menu_info` where  menu_id =  'fc8e124e3fe311e9828800163e0fc468';
Delete from  `sbc-setting`.`menu_info` where  menu_id =  'ff808081701f2f33017037544c1f0002';
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171303449017130b5f0370000', 4, 'fc8def483fe311e9828800163e0fc468', 2, '商品库', NULL, NULL, 3, '2020-03-31 21:09:22', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('fc8e124e3fe311e9828800163e0fc468', 4, 'ff80808171303449017130b5f0370000', 3, '商品资料库', '/goods-library-list', '', 3, '2018-04-16 15:49:55', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171303449017130b7de0f0001', 4, 'ff80808171303449017130b5f0370000', 3, '供货商品库', '/goods-library-provider-list', NULL, 1, '2020-03-31 21:11:29', 0);

INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a69001710a3c32100002', 3, 'ff80808170f2a69001710a3669410001', 3, '商品库列表', '/goods-library-provider-list', NULL, 114, '2020-03-24 09:50:49', 0);
-- INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`,`menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808170f2a690017107c7c6590000', 4, 'ff808081711a1cf101711ab387000000', 3, '供货商品库', '/goods-library-provider-list', NULL, 3, '2020-03-23 22:24:25', 0);


update `sbc-setting`.menu_info set del_flag = 1 where menu_name ='积分订单' and system_type_cd = 6;
update `sbc-setting`.menu_info set del_flag = 1 where menu_name ='基本设置' and system_type_cd = 6;
update `sbc-setting`.menu_info set del_flag = 1 where menu_name ='财务对账' and system_type_cd = 6;
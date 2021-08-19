-- 创建隐私政策表
CREATE TABLE `sbc-setting`.`system_privacy_policy`  (
  `privacy_policy_id` varchar(32) NOT NULL COMMENT '隐私政策id',
  `privacy_policy` text NULL COMMENT '隐私政策',
  `privacy_policy_pop` text NULL COMMENT '隐私政策弹窗',
  `create_person` varchar(32) NULL COMMENT '创建人',
  `create_time` datetime(0) NULL COMMENT '创建时间',
  `update_person` varchar(32) NULL COMMENT '修改人',
  `update_time` datetime(0) NULL COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '是否删除标志 0：否，1：是',
  PRIMARY KEY (`privacy_policy_id`)
);


-- 隐私政策菜单权限
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174c8a55b0174ce642e940000', 4, 'fc8e43953fe311e9828800163e0fc468', 3, '隐私政策', 'privacy-policy-setting', NULL, 3, '2020-09-27 15:08:26', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174c8a55b0174ce6660f40001', 4, 'ff80808174c8a55b0174ce642e940000', '隐私政策查看', 'f_privacy_policy', NULL, 0, '2020-09-27 15:10:50', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174c8a55b0174ce66e0400002', 4, 'ff80808174c8a55b0174ce642e940000', '隐私政策编辑', 'f_privacy_policy_edit', NULL, 1, '2020-09-27 15:11:23', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174c8a55b0174ce6894bc0003', 4, 'ff80808174c8a55b0174ce6660f40001', '隐私政策查看', NULL, '/boss/systemprivacypolicy', 'GET', NULL, 0, '2020-09-27 15:13:14', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808174c8a55b0174ce68cdb90004', 4, 'ff80808174c8a55b0174ce66e0400002', '隐私政策修改', NULL, '/boss/systemprivacypolicy', 'POST', NULL, 1, '2020-09-27 15:13:29', 0);


-- 历史遗留问题: 补充魔方缺失权限sql begin --------
-- 平台-pc建站-页面管理：
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752afbc9c10000', 4, 'fc9289a83fe311e9828800163e0fc468', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 6, '2020-10-15 14:39:06', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752afc59300001', 4, 'fc9289a83fe311e9828800163e0fc468', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 7, '2020-10-15 14:39:42', 0);


-- 平台-移动建站-页面管理
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752afe27000003', 4, 'fc9288f13fe311e9828800163e0fc468', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 7, '2020-10-15 14:41:41', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752afd92d20002', 4, 'fc9288f13fe311e9828800163e0fc468', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 6, '2020-10-15 14:41:03', 0);

-- 平台-pc建站-模版管理
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b2d27780004', 4, 'fc928a063fe311e9828800163e0fc468', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 6, '2020-10-15 15:33:01', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b2d98980005', 4, 'fc928a063fe311e9828800163e0fc468', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 7, '2020-10-15 15:33:30', 0);

-- 平台-移动建站-模版管理
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b3050a50007', 4, 'fc92894c3fe311e9828800163e0fc468', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 7, '2020-10-15 15:36:28', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b2e506a0006', 4, 'fc92894c3fe311e9828800163e0fc468', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 6, '2020-10-15 15:34:17', 0);

-- 商家-pc建站-页面管理
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b3f21ef0008', 3, 'ff8080816c6f1aff016c83dedba10006', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 7, '2020-10-15 15:52:39', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b3f6b350009', 3, 'ff8080816c6f1aff016c83dedba10006', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 8, '2020-10-15 15:52:58', 0);

-- 商家-pc建站-模版管理
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b3fd051000a', 3, 'ff8080816c6f1aff016c83e045d70008', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 7, '2020-10-15 15:53:24', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b40115f000b', 3, 'ff8080816c6f1aff016c83e045d70008', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 8, '2020-10-15 15:53:40', 0);

-- 商家-移动建站-页面管理
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b4125c0000c', 3, 'ff8080816c6f1aff016c83df2e170007', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 7, '2020-10-15 15:54:51', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b4179d9000d', 3, 'ff8080816c6f1aff016c83df2e170007', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 8, '2020-10-15 15:55:13', 0);

-- 商家-移动建站-模版管理
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b41ec9d000e', 3, 'ff8080816c6f1aff016c83e07b1e0009', '商品分页', NULL, '/xsite/skuPageForXsite', 'POST', NULL, 7, '2020-10-15 15:55:42', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081752aae2501752b421b89000f', 3, 'ff8080816c6f1aff016c83e07b1e0009', '商品组分页', NULL, '/xsite/spusForXsite', 'POST', NULL, 8, '2020-10-15 15:55:54', 0);
-- 历史遗留问题: 补充魔方缺失权限sql end    --------
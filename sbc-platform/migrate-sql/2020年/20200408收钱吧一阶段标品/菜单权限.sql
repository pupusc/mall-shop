-- 系统菜单表 use `sbc-setting`   menu_info
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171b46fb0fe0034', 4, 'fc8e1ac93fe311e9828800163e0fc468', 3, '页面设置', '/order-list-setting', NULL, 4, '2020-04-26 11:02:31', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081719c4e1d01719fdf10600001', 4, 'fc8e1ac93fe311e9828800163e0fc468', 3, '弹窗管理', '/popmodal-manage', NULL, 3, '2020-04-22 11:12:08', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817171f3f201718c7394cb0004', 4, 'fc8e21533fe311e9828800163e0fc468', 3, '打包一口价', '/pack-list', NULL, 4, '2020-04-18 16:41:57', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817171f3f201718c72794c0003', 3, 'fc8e114f3fe311e9828800163e0fc468', 3, '打包一口价', '/pack-list', NULL, 4, '2020-04-18 16:40:45', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817171f3f201717c69cb380002', 4, 'fc8e017c3fe311e9828800163e0fc468', 3, '搜索设置', '/search-manage', NULL, 145, '2020-04-15 13:57:20', 0);
INSERT INTO `sbc-setting`.`menu_info` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080817152574f01715887509f0017', '3', 'fc8e5a363fe311e9828800163e0fc468', '3', '商品限售', '/restricted-sale-list', NULL, '20', '2020-04-08 14:43:15', '0');
INSERT INTO `sbc-setting`.`menu_info` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171c4cfe50171c4ee22e40000', '4', 'fc8e1ac93fe311e9828800163e0fc468', '3', '悬浮导航', '/suspend-nav', NULL, '15', '2020-04-29 15:54:33', '0');

--  系统功能表  use `sbc-setting`  function_info
INSERT INTO `sbc-setting`.`function_info` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171c4cfe50171c4eed47f0001', '4', 'ff80808171c4cfe50171c4ee22e40000', '悬浮导航设置', 'f_suspend_nav_eidt', NULL, '1', '2020-04-29 15:55:19', '0');
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171b52c5d370035', 4, 'ff80808171a672390171b46fb0fe0034', '订单列表展示设置', 'f_order_show_edit', NULL, 1, '2020-04-26 14:28:36', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aafa0619002a', 4, 'ff808081719c4e1d01719fdf10600001', '应用页管理', 'f_page_management', NULL, 1, '2020-04-24 14:57:25', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaf907320029', 4, 'ff808081719c4e1d01719fdf10600001', '弹窗管理', 'f_popup_administration', NULL, 0, '2020-04-24 14:56:19', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaf0480b0025', 4, 'ff8080817171f3f201717c69cb380002', '预置搜索词', 'f_preset_search_terms', NULL, 2, '2020-04-24 14:46:46', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaedadf00020', 4, 'fc8df25c3fe311e9828800163e0fc468', '商品销量注水', 'f_sku_sham', NULL, 7, '2020-04-24 14:43:56', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaec8ac4001d', 4, 'fc8df25c3fe311e9828800163e0fc468', '商品排序', 'f_sku_sort', NULL, 6, '2020-04-24 14:42:41', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaeb11dc0019', 3, 'ff8080816c6f1aff016c83d84cba0003', '页面投放', 'page-extend', NULL, 1, '2020-04-24 14:41:04', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae8f1d50015', 4, 'ff8080817171f3f201717c69cb380002', '搜索词联想词', 'f_search_associational_word', NULL, 1, '2020-04-24 14:38:45', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae7e5ff0013', 4, 'fc8e1b223fe311e9828800163e0fc468', '页面投放', 'page-extend', NULL, 1, '2020-04-24 14:37:37', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae298ce000e', 4, 'ff8080817171f3f201717c69cb380002', '热门搜索词', 'f_popular_search_terms', NULL, 0, '2020-04-24 14:31:49', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6dd8889000c', 3, 'ff8080817171f3f201718c72794c0003', '启动打包一口价', 'f_start_buyout_price', NULL, 0, '2020-04-23 19:47:48', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6d9a479000a', 3, 'ff8080817171f3f201718c72794c0003', '暂停打包一口价', 'f_pause_buyout_price', NULL, 0, '2020-04-23 19:43:33', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6d4ca9d0008', 3, 'ff8080817171f3f201718c72794c0003', '编辑打包一口价', 'f_modify_buyout_price', NULL, 0, '2020-04-23 19:38:16', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6d37a1f0006', 3, 'ff8080817171f3f201718c72794c0003', '创建打包一口价', 'f_buyout_price_add', NULL, 0, '2020-04-23 19:36:49', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a68f42da0004', 4, 'ff8080817171f3f201718c7394cb0004', '打包一口价活动详情查看', 'f_buyout_price_details', NULL, 0, '2020-04-23 18:22:19', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a68e16780002', 4, 'ff8080817171f3f201718c7394cb0004', '打包一口价搜索查看', 'f_search_buyout_price', NULL, 0, '2020-04-23 18:21:02', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a68b4f450000', 4, 'ff8080817171f3f201718c7394cb0004', '打包一口价列表展示', 'f_buyout_price_list', NULL, 0, '2020-04-23 18:18:00', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081719c4e1d0171a65e33520006', 3, 'ff8080817171f3f201718c72794c0003', '删除打包一口价', 'f_delete_buyout_price', NULL, 0, '2020-04-23 17:28:44', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081719c4e1d0171a65b9f3b0004', 3, 'ff8080817171f3f201718c72794c0003', '打包一口价活动详情查看', 'f_buyout_price_details', NULL, 1, '2020-04-23 17:25:55', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081719c4e1d0171a6559d990002', 3, 'ff8080817171f3f201718c72794c0003', '打包一口价列表展示', 'f_buyout_price_list', NULL, 0, '2020-04-23 17:19:21', 0);


INSERT INTO `sbc-setting`.`function_info` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d5017185d4030a0000', '3', 'ff8080817152574f01715887509f0017', '商品限售列表', 'f_restricted_list', NULL, '0', '2020-04-17 09:49:56', '0');
INSERT INTO `sbc-setting`.`function_info` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d5017185d60f480001', '3', 'ff8080817152574f01715887509f0017', '商品限售编辑', 'f_restricted_edit', NULL, '1', '2020-04-17 09:52:11', '0');
INSERT INTO `sbc-setting`.`function_info` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d50171860e2ecf0005', '3', 'ff8080817152574f01715887509f0017', '限售商品删除', 'f_restricted_delete', NULL, '2', '2020-04-17 10:53:29', '0');
INSERT INTO `sbc-setting`.`function_info` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d50171860f5f220006', '3', 'ff8080817152574f01715887509f0017', '限售商品新增', 'f_restricted_add', NULL, '1', '2020-04-17 10:54:47', '0');




-- 系统权限表 use `sbc-setting`   authority
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171c4cfe50171c4f022650003', '4', 'ff80808171c4cfe50171c4eed47f0001', '悬浮导航编辑', NULL, '/hoverNavMobile/modify', 'PUT', NULL, '2', '2020-04-29 15:56:44', '0');
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171c4cfe50171c4efae800002', '4', 'ff80808171c4cfe50171c4eed47f0001', '悬浮导航查看', NULL, '/hoverNavMobile', 'GET', NULL, '1', '2020-04-29 15:56:14', '0');
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171b52d2c6c0037', 4, 'ff80808171a672390171b52c5d370035', '编辑订单列表展示设置', NULL, '/config/orderListShowType', 'PUT', NULL, 2, '2020-04-26 14:29:29', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171b52cc9850036', 4, 'ff80808171a672390171b52c5d370035', '查询订单列表展示设置', NULL, '/config/orderListShowType', 'GET', NULL, 1, '2020-04-26 14:29:03', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171ab10b5fe0033', 4, 'ff80808171a672390171aaf907320029', '查看弹窗详情', NULL, '/popup_administration/popupAdministration_id', 'POST', NULL, 6, '2020-04-24 15:22:11', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171ab0f01850032', 4, 'ff80808171a672390171aaf907320029', '查询弹窗列表', NULL, '/popup_administration/page', 'POST', NULL, 5, '2020-04-24 15:20:20', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171ab0051fb0031', 4, 'ff80808171a672390171aaf907320029', '启动弹窗管理', NULL, '/popup_administration/start_popup_administration/*', 'GET', NULL, 4, '2020-04-24 15:04:17', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaff9b740030', 4, 'ff80808171a672390171aaf907320029', '暂停弹窗', NULL, '/popup_administration/pause_popup_administration/*', 'GET', NULL, 3, '2020-04-24 15:03:30', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aafe66b5002f', 4, 'ff80808171a672390171aaf907320029', '删除弹窗管理', NULL, '/popup_administration/delete', 'POST', NULL, 2, '2020-04-24 15:02:11', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aafdcf17002e', 4, 'ff80808171a672390171aaf907320029', '修改弹窗管理', NULL, '/popup_administration/modify', 'POST', NULL, 1, '2020-04-24 15:01:33', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aafd3be2002d', 4, 'ff80808171a672390171aaf907320029', '新增弹窗管理', NULL, '/popup_administration/add', 'POST', NULL, 0, '2020-04-24 15:00:55', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aafc4971002c', 4, 'ff80808171a672390171aafa0619002a', '应用页下弹窗管理排序', NULL, '/popup_administration/sort_popup_administration', 'POST', NULL, 1, '2020-04-24 14:59:53', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aafaf751002b', 4, 'ff80808171a672390171aafa0619002a', '应用页写下弹窗查询', NULL, '/popup_administration/page_management_popup_administration', 'POST', NULL, 0, '2020-04-24 14:58:26', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaf48ca70028', 4, 'ff80808171a672390171aaf0480b0025', '查询预置搜索词', '', '/preset_search_terms/list', 'POST', NULL, 2, '2020-04-24 14:51:26', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaf3441e0027', 4, 'ff80808171a672390171aaf0480b0025', '编辑预置搜索词', NULL, '/preset_search_terms/modify', 'POST', NULL, 1, '2020-04-24 14:50:02', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaf2469b0026', 4, 'ff80808171a672390171aaf0480b0025', '新增预置搜索词', NULL, '/preset_search_terms/add', 'POST', NULL, 0, '2020-04-24 14:48:57', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaefea670024', 4, 'ff80808171a672390171aaedadf00020', '商品销量注水', NULL, '/goods/spu/sham', 'PUT', NULL, 1, '2020-04-24 14:46:22', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaef6d450023', 4, 'ff80808171a672390171aae8f1d50015', '删除联想词', NULL, '/search_associational_word/delete_associational_long_tail_word', 'POST', NULL, 6, '2020-04-24 14:45:50', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaee889a0022', 4, 'ff80808171a672390171aae8f1d50015', '联想词排序', NULL, '/search_associational_word/sort_associational_word', 'POST', NULL, 5, '2020-04-24 14:44:51', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaee441a0021', 4, 'ff80808171a672390171aaec8ac4001d', '商品排序', NULL, '/goods/spu/sort', 'PUT', NULL, 1, '2020-04-24 14:44:34', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaeda7e0001f', 4, 'ff80808171a672390171aae8f1d50015', '修改联想词', NULL, '/search_associational_word/modify_associational_word', 'POST', NULL, 4, '2020-04-24 14:43:54', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaece6a0001e', 4, 'ff80808171a672390171aae8f1d50015', '新增联想词', NULL, '/search_associational_word/add_associational_word', 'POST', NULL, 3, '2020-04-24 14:43:04', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaeba78c001c', 3, 'ff80808171a672390171aaeb11dc0019', '页面投放编辑', NULL, '/pageInfoExtend/modify', 'PUT', NULL, 2, '2020-04-24 14:41:43', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaeb885e001b', 4, 'ff80808171a672390171aae8f1d50015', '删除搜索词', NULL, '/search_associational_word/delete_search_associational_word', 'POST', NULL, 2, '2020-04-24 14:41:35', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaeb63b1001a', 3, 'ff80808171a672390171aaeb11dc0019', '页面投放查看', NULL, '/pageInfoExtend/query', 'POST', NULL, 1, '2020-04-24 14:41:25', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aaea970f0018', 4, 'ff80808171a672390171aae8f1d50015', '修改搜索词', NULL, '/search_associational_word/modify', 'POST', NULL, 1, '2020-04-24 14:40:33', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae9def10017', 4, 'ff80808171a672390171aae8f1d50015', '新增搜索词', NULL, '/search_associational_word/add', 'POST', NULL, 0, '2020-04-24 14:39:46', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae9627d0016', 4, 'ff80808171a672390171aae7e5ff0013', '页面投放编辑', NULL, '/pageInfoExtend/modify', 'PUT', NULL, 2, '2020-04-24 14:39:14', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae8e8e40014', 4, 'ff80808171a672390171aae7e5ff0013', '页面投放查看', NULL, '/pageInfoExtend/query', 'POST', NULL, 1, '2020-04-24 14:38:43', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae688550012', 4, 'ff80808171a672390171aae298ce000e', '热门搜索词排序', NULL, '/popular_search_terms/sort_popular_search_terms', 'POST', NULL, 3, '2020-04-24 14:36:07', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae545190011', 4, 'ff80808171a672390171aae298ce000e', '删除热门搜索词', NULL, '/popular_search_terms/delete', 'POST', NULL, 2, '2020-04-24 14:34:44', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae428080010', 4, 'ff80808171a672390171aae298ce000e', '编辑热门搜索词', NULL, '/popular_search_terms/modify', 'POST', NULL, 1, '2020-04-24 14:33:31', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171aae35a0d000f', 4, 'ff80808171a672390171aae298ce000e', '新增热门搜索词', NULL, '/popular_search_terms/add', 'POST', NULL, 0, '2020-04-24 14:32:39', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6df2946000d', 3, 'ff80808171a672390171a6dd8889000c', '启动打包一口价', NULL, '/marketing/start/*', 'PUT', NULL, 0, '2020-04-23 19:49:35', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6da74da000b', 3, 'ff80808171a672390171a6d9a479000a', '暂停打包一口价', NULL, '/marketing/pause/*', 'PUT', NULL, 0, '2020-04-23 19:44:27', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6d53bd80009', 3, 'ff80808171a672390171a6d4ca9d0008', '编辑打包一口价', NULL, '/marketing/buyoutPrice', 'PUT', NULL, 0, '2020-04-23 19:38:45', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a6d424c50007', 3, 'ff80808171a672390171a6d37a1f0006', '创建打包一口价', NULL, '/marketing/buyoutPrice', 'POST', NULL, 0, '2020-04-23 19:37:33', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a68fc7a20005', 4, 'ff80808171a672390171a68f42da0004', '获取打包一口价详情', NULL, '/marketing/buyout_price/*', 'GET', NULL, 0, '2020-04-23 18:22:53', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a68edf930003', 4, 'ff80808171a672390171a68e16780002', '获取打包一口价搜索查看', NULL, '/marketing/buyout_price/searchMarketing', 'POST', NULL, 0, '2020-04-23 18:21:53', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171a672390171a68c00960001', 4, 'ff80808171a672390171a68b4f450000', '获取打包一口价列表查询', NULL, '/marketing/buyout_price/list', 'POST', NULL, 0, '2020-04-23 18:18:45', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081719c4e1d0171a664742d0007', 3, 'ff808081719c4e1d0171a65e33520006', '删除打包一口价', NULL, '/marketing/delete/*', 'DELETE', NULL, 0, '2020-04-23 17:35:33', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081719c4e1d0171a65c945d0005', 3, 'ff808081719c4e1d0171a65b9f3b0004', '获取打包一口价详情', NULL, '/marketing/*', 'GET', NULL, 0, '2020-04-23 17:26:57', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081719c4e1d0171a657532c0003', 3, 'ff808081719c4e1d0171a6559d990002', '获取打包一口价列表查询', NULL, '/marketing/list', 'POST', NULL, 0, '2020-04-23 17:21:13', 0);
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171b6589d0171bff76e4d0001', '3', 'fc924c7c3fe311e9828800163e0fc468', '积分设置查询', NULL, '/boss/pointsConfig', 'GET', NULL, '10', '2020-04-28 16:46:36', '0');
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171b6589d0171bff68d0e0000', '3', 'fc93637e3fe311e9828800163e0fc468', '积分设置查询', '', '/boss/pointsConfig', 'GET', NULL, '10', '2020-04-28 16:45:38', '0');

INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d5017185d7bc430002', 3, 'ff808081718399d5017185d4030a0000', '列表查看', NULL, '/goodsrestrictedsale/page', 'POST', NULL, 0, '2020-04-17 09:54:00', 0);
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d50171860d12820003', 3, 'ff808081718399d5017185d60f480001', '限售编辑', NULL, '/goodsrestrictedsale/modify', 'PUT', NULL, 1, '2020-04-17 10:52:16', 0);
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d5017186115d960007', 3, 'ff808081718399d50171860e2ecf0005', '删除限售商品', NULL, '/goodsrestrictedsale/${id}', 'DELETE', NULL, 1, '2020-04-17 10:56:57', 0);
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081718399d50171861333f20008', 3, 'ff808081718399d50171860f5f220006', '限售商品新增', NULL, '/goodsrestrictedsale/add', 'POST', NULL, 1, '2020-04-17 10:58:58', 0);
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171c2d5890171c37fc3bb0000', 3, 'ff808081718399d5017185d60f480001', '限售—查询品台等级', NULL, '/store/storeLevel/list', 'GET', NULL, 2, '2020-04-29 09:14:23', 0);
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171c2d5890171c3805b990001', 3, 'ff808081718399d5017185d60f480001', '限售—查询会员列表', NULL, '/customer/page', 'POST', NULL, 3, '2020-04-29 09:15:01', 0);
INSERT INTO `sbc-setting`.`authority` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171c2d5890171c380f8fb0002', 3, 'ff808081718399d5017185d60f480001', '获取已选会员', NULL, '/customer/customerDetails', 'POST', NULL, 4, '2020-04-29 09:15:42', 0);



-- 新增接口权限配置
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171ecb0850171f1e3edc90000', 3, 'ff8080817171f3f201718c72794c0003', '打包一口价搜索查看', 'f_search_buyout_price', NULL, 0, '2020-05-08 09:26:19', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808171ecb0850171f1e958ba0001', 3, 'ff80808171ecb0850171f1e3edc90000', '获取打包一口价搜索查看', NULL, '/marketing/buyoutPrice/searchMarketing', 'POST', NULL, 0, '2020-05-08 09:32:14', 0);

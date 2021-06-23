
ALTER TABLE `sbc-setting`.operate_data_log MODIFY operate_before_data LONGTEXT,
MODIFY operate_after_data LONGTEXT;



-- 供应商端新增店铺分类菜单
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea79017323087be40001', 6, '2c9386be70520f2e01705233a3230001', 3, '店铺分类', '/goods-cate', NULL, 12, '2020-07-06 15:30:25', 0);

INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea7901732309cf730004', 6, 'ff80808172e0ea79017323087be40001', '店铺分类删除', 'f_goods_cate_2', NULL, 18, '2020-07-06 15:31:52', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea79017323096ae50003', 6, 'ff80808172e0ea79017323087be40001', '店铺分类新增/编辑', 'f_goods_cate_1', NULL, 17, '2020-07-06 15:31:27', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea7901732308fc820002', 6, 'ff80808172e0ea79017323087be40001', '店铺分类查看', 'f_goods_cate_0', NULL, 16, '2020-07-06 15:30:58', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230611dd0000', 6, '2c9386be70520f2e01705233a3230001', '店铺分类', '/goods-cate', NULL, 12, '2020-07-06 15:27:47', 1);

INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230d3281000b', 6, 'ff80808172e0ea7901732309cf730004', '查询是否关联商品', NULL, '/storeCate/checkHasGoods', 'POST', NULL, 23, '2020-07-06 15:35:34', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230cc7d3000a', 6, 'ff80808172e0ea7901732309cf730004', '查询是否有子分类', NULL, '/storeCate/checkHasChild', 'POST', NULL, 23, '2020-07-06 15:35:07', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230c53c00009', 6, 'ff80808172e0ea7901732309cf730004', '店铺分类删除', NULL, '/storeCate/*', 'DELETE', NULL, 23, '2020-07-06 15:34:37', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230bed050008', 6, 'ff80808172e0ea79017323096ae50003', '店铺分类排序', NULL, '/storeCate/allInLine', 'POST', NULL, 23, '2020-07-06 15:34:11', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230b8ae70007', 6, 'ff80808172e0ea79017323096ae50003', '店铺分类新增', NULL, '/storeCate', 'POST', NULL, 23, '2020-07-06 15:33:46', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230b19aa0006', 6, 'ff80808172e0ea79017323096ae50003', '店铺分类编辑', NULL, '/storeCate', 'PUT', NULL, 23, '2020-07-06 15:33:17', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808172e0ea790173230a716b0005', 6, 'ff80808172e0ea7901732308fc820002', '店铺分类查看', NULL, '/storeCate', 'GET', NULL, 23, '2020-07-06 15:32:34', 0);

-- 修改限售表限售方式注释
ALTER TABLE `sbc-goods`.`goods_restricted_sale`
MODIFY COLUMN `restricted_type` tinyint(4) NULL DEFAULT NULL COMMENT '限售方式 0: 按会员 1：按订单' ;


-- 限售表新增字段
ALTER TABLE `sbc-goods`.`goods_restricted_sale`
ADD COLUMN `restricted_way` tinyint(4)  COMMENT '是否打开限售方式开关 0关闭 1开启' AFTER `del_flag`,
ADD COLUMN `restricted_start_num` tinyint(4)  COMMENT '是否打开起售数量开关 0关闭 1开启' AFTER `restricted_way`;

-- 魔方菜单
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`,
`menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('808080816ca9bdfe016cacd48e980000', 4, '0', 1,
'魔方', NULL, 'mofang1.png', 8, '2019-08-20 10:21:47', 0);

update  `sbc-setting`.`menu_info`  set parent_menu_id = '808080816ca9bdfe016cacd48e980000' where  `menu_name` = '移动建站' or `menu_name` = 'PC建站';



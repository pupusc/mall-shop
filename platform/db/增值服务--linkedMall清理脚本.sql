-- 如果没有购买linkedMall, 需要执行该处sql
delete `sbc-setting`.`system_config` where config_type = 'third_platform_linked_mall';

-- 删除linkedMall商品菜单
update `sbc-setting`.`menu_info` set del_flag = 1 where menu_id = 'ff808081740747f301740aca49c80000';
-- 如果没有购买linkedMall end --


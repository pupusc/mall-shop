-- provider 删除运费模板菜单脚本
delete from `sbc-setting`.`menu_info` where `menu_id` = '2c9386c170560beb017056fa46c30093';

delete from `sbc-setting`.`authority` where `function_id` in (
select `function_id` from `sbc-setting`.`function_info` where `menu_id` = '2c9386c170560beb017056fa46c30093'
);

delete from `sbc-setting`.`function_info` where  `menu_id` = '2c9386c170560beb017056fa46c30093'

-- 修改LinkedMall菜单名称为LM商品库
update `sbc-setting`.`menu_info` set menu_name='LM商品库' where menu_id='ff808081740747f301740aca49c80000';

;
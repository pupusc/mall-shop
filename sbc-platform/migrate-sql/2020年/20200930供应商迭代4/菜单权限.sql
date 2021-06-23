DELETE FROM `sbc-setting`.`menu_info` WHERE `menu_id` = 'ff808081701f2f33017037544c1f0002';
DELETE FROM `sbc-setting`.`function_info` WHERE `menu_id` = 'ff808081701f2f33017037544c1f0002';
DELETE FROM `sbc-setting`.`authority` WHERE `function_id` in ('ff808081701f2f3301703cc34dce0041','ff808081701f2f3301703cc93ec10049','ff808081701f2f3301703ccee81c0050','ff808081701f2f3301703cd57f6d0060');
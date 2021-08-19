-- 根据客户是否购买crm服务，执行相应的脚本

-- 增加标记CRM（如果客户购买crm服务，则执行这条脚本）
INSERT INTO `sbc-setting`.`system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('crm', 'crm', 'CRM标记', ' 判断系统是否购买CRM', '1', NULL, '2019-11-15 10:48:51', '2019-11-15 10:48:53', '0');

-- 增加标记CRM（如果客户*没有*购买crm服务，则执行这条脚本）
INSERT INTO `sbc-setting`.`system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('crm', 'crm', 'CRM标记', ' 判断系统是否购买CRM', '0', NULL, '2019-11-15 10:48:51', '2019-11-15 10:48:53', '0');


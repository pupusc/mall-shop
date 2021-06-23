-- 如果没有购买企业购, 需要执行该处sql

-- 如果所有增值服务都没有购买, 则删除整个增值服务库
DROP SCHEMA IF EXISTS `sbc-vas`;

-- 如果没有购买企业购 begin --
DROP TABLE IF EXISTS `sbc-vas`.`iep_setting`;

delete `sbc-setting`.`system_config` where config_type = 'vas_iep_setting';

truncate table `sbc-customer`.`enterprise_info`;

update `sbc-goods`.`goods_info`
set `enterprise_price` = null ,`enterprise_goods_audit` = 0, `enterprise_goods_audit_reason` = null
where 1=1;

update `sbc-customer`.`customer`
set `enterprise_check_state` = 0, `enterprise_check_reason` = null
where 1=1;

delete `sbc-marketing`.`coupon_activity` where `activity_type` = 7;
-- 如果没有购买企业购 end --

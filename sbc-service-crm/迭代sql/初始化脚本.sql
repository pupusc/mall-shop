-- 增加标记CRM（如果客户购买crm服务，则执行这条脚本）
INSERT INTO `sbc-setting`.`system_config` (`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('crm', 'crm', 'CRM标记', ' 判断系统是否购买CRM', '1', NULL, '2019-11-15 10:48:51', '2019-11-15 10:48:53', '0');

-- 店铺收藏新增记录表
CREATE TABLE `store_customer_follow_action` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `store_id` bigint(11) NOT NULL COMMENT '商品Id',
  `company_info_id` bigint(11) NOT NULL COMMENT '商家ID',
  `follow_time` datetime NOT NULL COMMENT '收藏时间',
  `terminal_source` tinyint(4) DEFAULT NULL COMMENT '终端',
	`create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=658 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='商品的店铺收藏新增数据表';

CREATE DEFINER=`root`@`%` TRIGGER `trigger_store_customer_follow_action` AFTER INSERT ON `store_customer_follow`
FOR EACH ROW BEGIN
		INSERT INTO store_customer_follow_action(follow_id, customer_id, store_id, company_info_id, follow_time, terminal_source,create_time)
		VALUES (NEW.follow_id, NEW.customer_id, NEW.store_id, NEW.company_info_id, NEW.follow_time, NEW.terminal_source,now());
	end;
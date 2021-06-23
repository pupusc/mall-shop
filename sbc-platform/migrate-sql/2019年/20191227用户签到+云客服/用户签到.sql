--customer
CREATE TABLE `customer_sign_record` (
  `sign_record_id` varchar(32) NOT NULL COMMENT '用户签到记录表id',
  `customer_id` varchar(32) NOT NULL COMMENT '用户id',
  `sign_record` datetime NOT NULL COMMENT '签到日期记录',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除区分：0 未删除，1 已删除',
  `sign_ip` varchar(50) DEFAULT NULL COMMENT '签到ip',
  `sign_terminal` varchar(10) DEFAULT NULL COMMENT '签到终端：pc,wechat,app,minipro',
  PRIMARY KEY (`sign_record_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户签到记录表';

ALTER TABLE `customer`
ADD COLUMN `sign_continuous_days` int(8) NULL COMMENT '签到连续天数' ;


-- setting
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('online_service', 'aliyun_online_service', '阿里云客服', NULL, 1, '{\"key\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuYWZWFeJCXB2IlFJwRKrcU1Rev22h1wLHeRETSJW3nG4//jwZNKRK3NSFaRdhZ3ppaSDB7m8QBFgE6vWI2241S4W00F/TNCRa4ObnwlvV+nn9bOYFjHIVght6vEtKd2/u4mZgqYsFgcLyVLyE+62qF5W/IvKgb2zoVMvYOQfzPq5jME3VE3UsRpHGNAQXq/g1YRNyb4Ek27ipXjuvyNm9Z8vYkZvFPG+vYemCoLZnL4CK7PPVvCYM1Ihh6WPLoggbFE3eW8eZZZicbMOQUO06pNQYnLg9eGCMXKOeu1lRHoHKYp95XiMOYjTLzTlT625k/zjHrtttmEdRba/XxAa6wIDAQAB\", \"title\": \"客服\", \"aliyunChat\": \"https://cschat-ccs.aliyun.com/index.htm?tntInstId=_1TtdM7E&scene=SCE00005299\"}', '2019-12-28 16:10:46', NULL, 0);
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('points_basic_rule', 'points_basic_rule_sign_in', '签到', '会员每日签到后可获得积分数', 1, '{\"value\":10}', '2019-12-27 16:02:49', '2019-12-27 17:36:27', 0);
INSERT INTO `system_config`(`config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`) VALUES ('growth_value_basic_rule', 'growth_value_basic_rule_sign_in', '签到', '会员每日签到后可获的成长值', 1, '{\"value\":5}', '2019-03-26 17:00:45', '2019-12-27 18:16:28', 0);
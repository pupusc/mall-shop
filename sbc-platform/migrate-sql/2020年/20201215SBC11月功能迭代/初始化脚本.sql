ALTER TABLE `sbc-message`.`message_send`
MODIFY COLUMN `route_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '移动端落地页参数' AFTER `plan_id`,
ADD COLUMN `pc_route_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'PC端落地页参数' AFTER `route_params`;

ALTER TABLE `sbc-message`.`app_message`
MODIFY COLUMN `route_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '移动端路由参数' AFTER `route_name`,
ADD COLUMN `pc_route_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'PC端路由参数' AFTER `join_id`;
ALTER TABLE `sbc-message`.`app_message_0`
MODIFY COLUMN `route_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '移动端路由参数' AFTER `route_name`,
ADD COLUMN `pc_route_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'PC端路由参数' AFTER `join_id`;

-- 存储过程，动态创建站内信消息发送分表1-9
delimiter $
USE `sbc-message`$
drop procedure if exists  pro_app_message_alert_table;

create procedure pro_app_message_alert_table(tableCount  int)
begin
  declare i int;
  DECLARE table_name VARCHAR(20);
  DECLARE table_pre VARCHAR(20);
  DECLARE sql_text VARCHAR(500);
  set i = 1;
  SET table_pre = 'app_message_';
  while i <= tableCount  do
    SET @table_name = CONCAT(table_pre,CONCAT(i, ''));
    SET sql_text = CONCAT('ALTER TABLE ', @table_name, ' MODIFY COLUMN route_param varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT "移动端路由参数" AFTER route_name, ADD COLUMN pc_route_param varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT "PC端路由参数" AFTER join_id');
    SET @sql_text=sql_text;
    PREPARE stmt FROM @sql_text;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    set i = i + 1;
  end while;
end $

call pro_app_message_alert_table(9);
-- 存储过程用完删除
drop procedure if exists  pro_app_message_alert_table;

-- 店铺退货地址表
CREATE TABLE `sbc-customer`.`store_return_address` (
  `address_id` varchar(32) NOT NULL COMMENT '收货地址ID',
  `company_info_id` int(11) NOT NULL COMMENT '公司信息ID',
  `store_id` bigint(20) NOT NULL COMMENT '店铺信息ID',
  `consignee_name` varchar(128) DEFAULT NULL COMMENT '收货人',
  `consignee_number` varchar(20) DEFAULT NULL COMMENT '收货人手机号码',
  `province_id` bigint(10) DEFAULT NULL COMMENT '省份',
  `city_id` bigint(10) DEFAULT NULL COMMENT '市',
  `area_id` bigint(10) DEFAULT NULL COMMENT '区',
  `street_id` bigint(20) DEFAULT NULL COMMENT '街道id',
  `return_address` varchar(225) DEFAULT NULL COMMENT '详细街道地址',
  `is_default_address` tinyint(4) DEFAULT NULL COMMENT '是否是默认地址',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(32) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`address_id`),
  UNIQUE KEY `address_id_UNIQUE` (`address_id`),
  KEY `idx_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺退货地址表';

ALTER TABLE `sbc-order`.`purchase`
ADD COLUMN `update_time` datetime DEFAULT NULL COMMENT '更新时间' AFTER `create_time`;
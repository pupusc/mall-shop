
-------------------------开放平台-------------------------
-- 在线客服配置
CREATE TABLE `online_service` (
  `online_service_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '在线客服主键',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺ID',
  `server_status` tinyint(1) DEFAULT NULL COMMENT '在线客服是否启用 0 不启用， 1 启用',
  `service_title` varchar(10) DEFAULT NULL COMMENT '客服标题',
  `effective_pc` tinyint(1) DEFAULT NULL COMMENT '生效终端pc 0 不生效 1 生效',
  `effective_app` tinyint(1) DEFAULT NULL COMMENT '生效终端App 0 不生效 1 生效',
  `effective_mobile` tinyint(1) DEFAULT NULL COMMENT '生效终端移动版 0 不生效 1 生效',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志 默认0：未删除 1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `operate_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `app_key` varchar(12) NOT NULL COMMENT '应用唯一标识',
  PRIMARY KEY (`online_service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

-- 在线客服明细配置
CREATE TABLE `online_service_item` (
  `service_item_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '在线客服座席id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺ID',
  `online_service_id` int(11) NOT NULL COMMENT '在线客服主键',
  `customer_service_name` varchar(10) NOT NULL COMMENT '客服昵称',
  `customer_service_account` varchar(20) NOT NULL COMMENT '客服账号',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志 默认0：未删除 1：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `operate_person` varchar(45) DEFAULT NULL COMMENT '操作人',
  `app_key` varchar(12) NOT NULL COMMENT '应用唯一标识',
  PRIMARY KEY (`service_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8




-------------------------业务系统-------------------------
-- system_type_cd = 3 商家菜单 system_type_cd = 4
-- 因为存在相应的父子id关联，建议手动添加
INSERT INTO s2b.menu_info (system_type_cd, parent_menu_id, menu_grade, menu_name, menu_url, menu_icon, sort, create_time, del_flag) VALUES (3, 7, 2, '客服设置', '', 'kefudianhua.jpg', 43, '2018-01-03 10:36:38', 0);
INSERT INTO s2b.menu_info (system_type_cd, parent_menu_id, menu_grade, menu_name, menu_url, menu_icon, sort, create_time, del_flag) VALUES (3, 182, 3, '在线客服', '/online-service', '', 155, '2018-01-03 10:36:38', 0);
INSERT INTO s2b.menu_info (system_type_cd, parent_menu_id, menu_grade, menu_name, menu_url, menu_icon, sort, create_time, del_flag) VALUES (4, 108, 2, '客服设置', '', 'kefudianhua.jpg', 142, '2018-01-03 10:36:38', 0);
INSERT INTO s2b.menu_info (system_type_cd, parent_menu_id, menu_grade, menu_name, menu_url, menu_icon, sort, create_time, del_flag) VALUES (4, 180, 3, '在线客服', '/online-service', '', 155, '2018-01-03 10:36:38', 0);


INSERT INTO s2b.function_info (system_type_cd, menu_id, function_title, function_name, remark, sort, create_time, del_flag) VALUES (4, 181, '在线客服查看', 'f_online_server_view', null, 1, null, 0);
INSERT INTO s2b.function_info (system_type_cd, menu_id, function_title, function_name, remark, sort, create_time, del_flag) VALUES (4, 181, '在线客服编辑', 'f_online_server_edit', null, 2, null, 0);
INSERT INTO s2b.function_info (system_type_cd, menu_id, function_title, function_name, remark, sort, create_time, del_flag) VALUES (3, 183, '在线客服查看', 'f_online_server_view', null, 1, null, 0);
INSERT INTO s2b.function_info (system_type_cd, menu_id, function_title, function_name, remark, sort, create_time, del_flag) VALUES (3, 183, '在线客服编辑', 'f_online_server_edit', null, 2, null, 0);


INSERT INTO s2b.authority (system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (3, 224, '在线客服查看', null, '/customerService/qq/switch', 'GET', null, 1, null, 0);
INSERT INTO s2b.authority (system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (3, 224, '在线客服查看', null, '/customerService/qq/detail', 'GET', null, 2, null, 0);
INSERT INTO s2b.authority (system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (3, 225, '在线客服编辑', null, '/customerService/qq/saveDetail', 'POST', null, 3, null, 0);
INSERT INTO s2b.authority (system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (4, 222, '在线客服查看', null, '/customerService/qq/switch/*', 'GET', null, 1, null, null);
INSERT INTO s2b.authority (system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (4, 222, '在线客服查看', null, '/customerService/qq/detail/*', 'GET', null, 2, null, null);
INSERT INTO s2b.authority (system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (4, 223, '在线客服编辑', null, '/customerService/qq/saveDetail', 'POST', null, 3, null, 0);
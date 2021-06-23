-- ####开放平台####
-- 微信登录设置
CREATE TABLE `wechat_login_set`
(
  `wechat_set_id` VARCHAR
(32)                   
 NOT NULL COMMENT '微信授权登录配置主键',
  `mobile_server_status` tinyint
(4) DEFAULT NULL COMMENT 'h5-微信授权登录是否启用 0 不启用， 1 启用',
  `mobile_app_id` varchar
(60) DEFAULT NULL COMMENT 'h5-AppID(应用ID)',
  `mobile_app_secret` varchar
(60) DEFAULT NULL COMMENT 'h5-AppSecret(应用密钥)',  
  `pc_server_status` tinyint
(4) DEFAULT NULL COMMENT 'pc-微信授权登录是否启用 0 不启用， 1 启用',
  `pc_app_id` varchar
(60) DEFAULT NULL COMMENT 'pc-AppID(应用ID)',
  `pc_app_secret` varchar
(60) DEFAULT NULL COMMENT 'pc-AppSecret(应用密钥)',
  `app_server_status` tinyint
(4) DEFAULT NULL COMMENT 'app-微信授权登录是否启用 0 不启用， 1 启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `operate_person` varchar
(45) DEFAULT NULL COMMENT '操作人',
  `app_key` varchar
(12) NOT NULL COMMENT '应用唯一标识',
  UNIQUE
(`wechat_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '微信授权登录配置';


-- 微信分享设置
CREATE TABLE `wechat_share_set`
(
  `share_set_id`  VARCHAR
(32)                   
 NOT NULL COMMENT '微信分享参数配置主键',
  `share_app_id` varchar
(60) NOT NULL COMMENT '微信公众号App ID',
  `share_app_secret` varchar
(60) NOT NULL COMMENT '微信公众号 App Secret',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `operate_person` varchar
(45) DEFAULT NULL COMMENT '操作人',
  `app_key` varchar
(12) NOT NULL COMMENT '应用唯一标识',
  UNIQUE
(`share_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '微信分享参数配置';


-- 是否要求完善客户信息
INSERT INTO s2b.system_config
  (config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag)
VALUES
  ('s2b_audit', 'customer_info_audit', '是否完善客户信息', '是否要求完善客户信息，默认打开，打开则所有自行注册的客户都需要完善基本信息，关闭则不用完善基本信息,审核通过即可登录', 1, null, '2017-11-13 11:45:37', '2018-01-23 11:13:07', 0);


-- ####业务系统####
-- 第三方登录关系表
CREATE TABLE third_login_relation
(
  third_login_id VARCHAR(32)
    NOT NULL
  COMMENT '第三方登录主键',
    third_login_uid VARCHAR
  (255) COMMENT '第三方关系关联(union)Id',
    customer_id VARCHAR
  (32) COMMENT '用户Id',
    third_login_type TINYINT
  (4) COMMENT '第三方类型 0:wechat',
    third_login_open_id VARCHAR
  (50) COMMENT '微信授权openId, 该字段只有微信才有, 由于微信登录使用的是unionId, 但是微信模板消息发送需要使用openId, 所以需要union_id, 所以union_id和open_id单独存放',
    binding_time DATETIME COMMENT '绑定时间',
  UNIQUE
  (`third_login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '第三方关系表';
CREATE UNIQUE INDEX third_login_relation_third_login_uid_uindex ON third_login_relation (third_login_uid);

-- 菜单权限表，需要按需加入，因为有外键关系
INSERT INTO s2b.menu_info (menu_id, system_type_cd, parent_menu_id, menu_grade, menu_name, menu_url, menu_icon, sort, create_time, del_flag) VALUES (194, 4, 144, 3, '登录接口', '/login-interface', '', 149, '2018-08-16 13:50:18', 0);
INSERT INTO s2b.function_info (function_id, system_type_cd, menu_id, function_title, function_name, remark, sort, create_time, del_flag) VALUES (231, 4, 194, '登录接口配置编辑', 'f_login_interface_edit', null, 11, '2018-08-16 14:06:06', 0);
INSERT INTO s2b.function_info (function_id, system_type_cd, menu_id, function_title, function_name, remark, sort, create_time, del_flag) VALUES (230, 4, 194, '登录接口配置查询', 'f_login_interface_view', null, 10, '2018-08-16 14:04:57', 0);
INSERT INTO s2b.authority (authority_id, system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (781, 4, 231, '编辑登录接口配置', '编辑登录接口配置', '/third-login/wechat/set', 'PUT', null, 10, '2018-08-16 14:09:24', 0);
INSERT INTO s2b.authority (authority_id, system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES (780, 4, 230, '查询登录接口配置', '查询登录接口配置', '/third-login/wechat/set/detail', 'GET', null, 10, '2018-08-16 14:08:43', 0);


-- mongoBean

-- ThridLoginUserInfo.java
-- String Id;
-- String uid
-- String userInfo;


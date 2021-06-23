-- 数据库：sbc-setting
CREATE TABLE `authority` (
  `authority_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `system_type_cd` tinyint(2) DEFAULT NULL COMMENT '系统类别(3:商家,4:平台)',
  `function_id` bigint(10) DEFAULT NULL COMMENT '功能id',
  `authority_title` varchar(100) DEFAULT NULL COMMENT '权限显示名',
  `authority_name` varchar(100) DEFAULT NULL COMMENT '权限名称',
  `authority_url` varchar(200) DEFAULT NULL COMMENT '权限路径',
  `request_type` varchar(20) DEFAULT NULL COMMENT '权限请求类别(GET,POST,PUT,DELETE)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sort` int(5) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`authority_id`)
) ENGINE=InnoDB AUTO_INCREMENT=889 DEFAULT CHARSET=utf8 COMMENT='系统权限表';

CREATE TABLE `function_info` (
  `function_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '功能id',
  `system_type_cd` tinyint(2) DEFAULT NULL COMMENT '系统类别(3:商家,4:平台)',
  `menu_id` int(8) DEFAULT NULL COMMENT '菜单id',
  `function_title` varchar(100) DEFAULT NULL COMMENT '功能显示名',
  `function_name` varchar(100) DEFAULT NULL COMMENT '功能名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sort` int(5) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`function_id`),
  UNIQUE KEY `function_info_name_uk` (`system_type_cd`,`function_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8 COMMENT='系统功能表';

CREATE TABLE `menu_info` (
  `menu_id` int(8) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `system_type_cd` tinyint(2) DEFAULT NULL COMMENT '系统类别(3:商家,4:平台)',
  `parent_menu_id` int(8) DEFAULT NULL COMMENT '父菜单id',
  `menu_grade` tinyint(2) DEFAULT NULL COMMENT '菜单级别',
  `menu_name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `menu_url` varchar(200) DEFAULT NULL COMMENT '菜单路径',
  `menu_icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `sort` int(5) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

CREATE TABLE `role_function_rela` (
  `role_info_id` bigint(10) NOT NULL COMMENT '角色id',
  `function_id` bigint(10) NOT NULL COMMENT '功能id',
  PRIMARY KEY (`role_info_id`,`function_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色功能关联表';

CREATE TABLE `role_menu_rela` (
  `role_info_id` bigint(10) NOT NULL COMMENT '角色id',
  `menu_id` int(8) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_info_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色菜单关联表';

CREATE TABLE `store_image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `cate_id` bigint(20) NOT NULL COMMENT '图片分类ID',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺标识',
  `company_info_id` int(11) DEFAULT NULL COMMENT '商家标识',
  `image_key` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '图片KEY',
  `image_name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '图片名称',
  `artwork_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '原图地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `server_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '图片服务器类型，对应system_config的config_type',
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=322 DEFAULT CHARSET=utf8mb4 COMMENT='店铺图片表';

CREATE TABLE `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ' 编号',
  `config_key` varchar(255) NOT NULL COMMENT '键',
  `config_type` varchar(255) NOT NULL COMMENT '类型',
  `config_name` varchar(255) NOT NULL COMMENT '名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态,0:未启用1:已启用',
  `context` longtext COMMENT '配置内容，如JSON内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='系统配置表';

CREATE TABLE `system_email_config` (
  `email_config_id` varchar(32) NOT NULL COMMENT '邮箱配置Id',
  `from_email_address` varchar(50) DEFAULT NULL COMMENT '发信人邮箱地址',
  `from_person` varchar(20) DEFAULT NULL COMMENT '发信人',
  `email_smtp_host` varchar(20) DEFAULT NULL COMMENT 'smtp服务器主机名',
  `email_smtp_port` varchar(10) DEFAULT NULL COMMENT 'smtp服务器端口号',
  `auth_code` varchar(20) DEFAULT NULL COMMENT 'smtp服务器授权码',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用标志 0：停用，1：启用',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`email_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='boss管理平台邮箱接口配置表';

CREATE TABLE `system_image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `cate_id` bigint(20) NOT NULL COMMENT '图片分类ID',
  `image_key` varchar(255) DEFAULT NULL COMMENT '图片KEY',
  `image_name` varchar(45) DEFAULT NULL COMMENT ' 图片名称',
  `artwork_url` varchar(255) DEFAULT NULL COMMENT '原图地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `server_type` varchar(255) DEFAULT NULL COMMENT '图片服务器类型，对应system_config的config_type',
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=952 DEFAULT CHARSET=utf8 COMMENT='图片表';

CREATE TABLE `system_ip_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP',
  `isp` varchar(255) DEFAULT NULL COMMENT '运营商',
  `country` varchar(45) DEFAULT NULL COMMENT '国家',
  `province` varchar(45) DEFAULT NULL COMMENT '省份',
  `city` varchar(45) DEFAULT NULL COMMENT '城市',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=309 DEFAULT CHARSET=utf8 COMMENT='IP归属信息';

CREATE TABLE `system_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺id',
  `company_info_id` int(11) DEFAULT NULL COMMENT '公司id',
  `op_account` varchar(50) DEFAULT NULL COMMENT '操作人账号',
  `op_name` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人姓名',
  `op_role_name` varchar(45) DEFAULT NULL COMMENT '操作人角色',
  `op_module` varchar(45) DEFAULT NULL COMMENT '所属模块',
  `op_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作类型',
  `op_context` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作内容',
  `op_time` datetime DEFAULT NULL COMMENT '操作时间',
  `op_ip` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作IP',
  `op_mac` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作MAC地址',
  `op_isp` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '网络运营商',
  `op_country` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '所在国家',
  `op_province` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '所在省份',
  `op_city` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '所在城市',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10740 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

CREATE TABLE `system_switch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '开关id',
  `switch_name` varchar(32) NOT NULL COMMENT '开关名称',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '开关状态 0：关闭 1：开启',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标志 0：未删除 1：删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
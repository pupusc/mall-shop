CREATE DEFINER=`root`@`%` PROCEDURE `BACKUP`(in appkey VARCHAR(32) charset utf8)
BEGIN
-- 创建备份数据库
    CREATE DATABASE IF NOT EXISTS micro_system_temp;
-- 创建表
  -- 注意system_config 为已存在表 在之前基础上做修改system_config_temp
  -- 创建表s：base_config
			DROP TABLE IF EXISTS `micro_system_temp`.base_config;
			create table `micro_system_temp`.base_config(
				base_config_id   int auto_increment  comment '基本设置ID'    primary key,
				pc_website       varchar(128) null  comment 'PC端商城网址',
				mobile_website   varchar(128) null  comment '移动端商城网址',
				pc_logo          text         null  comment 'PC商城logo',
				pc_banner        text         null  comment 'PC商城banner,最多可添加5个,多个图片间以"|"隔开',
				mobile_banner    text         null  comment '移动商城banner,最多可添加5个,多个图片间以"|"隔开',
				pc_main_banner   text         null  comment 'PC商城首页banner,最多可添加5个,多个图片间以"|"隔开',
				pc_ico           text         null  comment '网页ico',
				pc_title         varchar(128) null  comment 'pc商城标题',
				supplier_website varchar(128) null  comment '商家后台登录网址',
				register_content text         null  comment '会员注册协议'
			)  comment '基本设置'  charset = utf8;

			-- 查询指定数据并插入
			--  select *  from base_config where app_key=appkey;
			 DELETE
				FROM
					`micro_system_temp`.`base_config`;
				INSERT INTO `micro_system_temp`.`base_config`
				SELECT
			 `base_config_id`, `pc_website`, `mobile_website`, `pc_logo`, `pc_banner`, `mobile_banner`, `pc_main_banner`, `pc_ico`, `pc_title`, `supplier_website`, `register_content`
				FROM
					`micro-system`.`base_config`
				WHERE
					app_key = appkey;


 --  重复

 -- 创建表：business_config
				-- 创建表：business_config
				DROP TABLE IF EXISTS `micro_system_temp`.business_config;
				CREATE TABLE `micro_system_temp`.business_config(
				business_config_id int auto_increment  comment '招商页设置主键'    primary key,
				business_banner    text        null  comment '招商页banner',
				business_custom    text        null  comment '招商页自定义',
				business_register  text        null  comment '招商页注册协议',
				business_enter     text        null  comment '招商页入驻协议'
			)  comment '招商页设置'  charset = utf8mb4;

				-- 查询指定数据并插入
			 DELETE
				FROM
					`micro_system_temp`.`business_config`;
				INSERT INTO `micro_system_temp`.`business_config`
				SELECT
				`business_config_id`, `business_banner`, `business_custom`, `business_register`, `business_enter`
				FROM
					`micro-system`.`business_config`
				WHERE
					app_key = appkey;


-- 创建表：company_info
			-- 创建表：company_info
			DROP TABLE IF EXISTS `micro_system_temp`.company_info;
			CREATE TABLE `micro_system_temp`.company_info (
				company_info_id BIGINT ( 11 ) auto_increment COMMENT '公司信息ID' PRIMARY KEY,
				company_name VARCHAR ( 255 ) charset utf8 NULL COMMENT '公司名称',
				back_ID_card VARCHAR ( 1024 ) NULL COMMENT '法人身份证反面',
				province_id BIGINT ( 10 ) NULL COMMENT '省',
				city_id BIGINT ( 10 ) NULL COMMENT '市',
				area_id BIGINT ( 10 ) NULL COMMENT '区',
				detail_address VARCHAR ( 255 ) charset utf8 NULL COMMENT '详细地址',
				contact_name text NULL COMMENT '联系人',
				contact_phone text NULL COMMENT '联系方式',
				copyright VARCHAR ( 255 ) charset utf8 NULL COMMENT '版权信息',
				company_descript text NULL COMMENT '公司简介',
				operator VARCHAR ( 45 ) charset utf8 NULL COMMENT '操作人',
				create_time datetime NULL COMMENT '创建时间',
				update_time datetime NULL COMMENT '修改时间',
				company_type TINYINT NULL COMMENT '商家类型 0、平台自营 1、第三方商家',
				is_account_checked TINYINT NULL COMMENT '账户是否全部收到打款 0、否 1、是',
				social_credit_code VARCHAR ( 50 ) NULL COMMENT '社会信用代码',
				address VARCHAR ( 255 ) NULL COMMENT '住所',
				legal_representative VARCHAR ( 150 ) NULL COMMENT '法定代表人',
				registered_capital DECIMAL ( 10, 2 ) NULL COMMENT '注册资本',
				found_date datetime NULL COMMENT ' 成立日期',
				business_term_start datetime NULL COMMENT '营业期限自',
				business_term_end datetime NULL COMMENT '营业期限至',
				business_scope VARCHAR ( 1024 ) NULL COMMENT '经营范围',
				business_licence VARCHAR ( 1024 ) NULL COMMENT '营业执照副本电子版',
				front_ID_card VARCHAR ( 1024 ) NULL COMMENT '法人身份证正面',
				company_code VARCHAR ( 32 ) NULL COMMENT '商家编号',
				supplier_name VARCHAR ( 50 ) NULL COMMENT '商家名称',
				del_flag TINYINT NULL COMMENT '是否删除 0 否  1 是'
			) COMMENT '公司信息' charset = utf8mb4;

			-- 查询指定数据并插入
			DELETE FROM `micro_system_temp`.`company_info`;
			INSERT INTO `micro_system_temp`.`company_info`  SELECT
				`company_info_id`,`company_name`,`back_ID_card`,`province_id`,`city_id`,`area_id`,`detail_address`,`contact_name`,`contact_phone`,`copyright`,`company_descript`,`operator`,`create_time`,`update_time`,`company_type`,
				`is_account_checked`,`social_credit_code`,`address`,`legal_representative`,`registered_capital`,`found_date`,`business_term_start`,`business_term_end`,`business_scope`,`business_licence`,`front_ID_card`,
				`company_code`,`supplier_name`,`del_flag`
			FROM
				`micro-system`.`company_info`
			WHERE
				app_key = appkey;


-- 创建表：express_company
				-- 创建表：express_company
				DROP TABLE IF EXISTS `micro_system_temp`.express_company;
				CREATE TABLE `micro_system_temp`.express_company (
				express_company_id BIGINT ( 5 ) auto_increment COMMENT '主键ID,自增' PRIMARY KEY,
				express_name VARCHAR ( 125 ) NULL COMMENT '物流公司名称',
				express_code VARCHAR ( 255 ) NULL COMMENT '物流公司代码',
				is_checked TINYINT ( 1 ) NULL COMMENT '是否是常用物流公司 0：否 1：是',
				is_add TINYINT ( 1 ) NULL COMMENT '是否是用户新增 0：否 1：是',
				del_flag TINYINT DEFAULT '0' NULL COMMENT '删除标志 默认0：未删除 1：删除'
				) COMMENT '物流公司' charset = utf8;

				-- 查询指定数据并插入
				DELETE
				FROM
					`micro_system_temp`.`express_company`;
				INSERT INTO `micro_system_temp`.`express_company`
				SELECT
			  `express_company_id`, `express_name`, `express_code`, `is_checked`, `is_add`, `del_flag`
				FROM
					`micro-system`.`express_company`
				WHERE
					app_key = appkey;

-- 创建表：online_service
			-- 创建表：online_service
			DROP TABLE IF EXISTS `micro_system_temp`.online_service;
			CREATE TABLE `micro_system_temp`.online_service(
				online_service_id int auto_increment  comment '在线客服主键'    primary key,
				store_id          bigint                 null  comment '店铺ID',
				server_status     tinyint(1)             null  comment '在线客服是否启用 0 不启用， 1 启用',
				service_title     varchar(10)            null  comment '客服标题',
				effective_pc      tinyint(1)             null  comment '生效终端pc 0 不生效 1 生效',
				effective_app     tinyint(1)             null  comment '生效终端App 0 不生效 1 生效',
				effective_mobile  tinyint(1)             null  comment '生效终端移动版 0 不生效 1 生效',
				del_flag          tinyint(1) default '0' null  comment '删除标志 默认0：未删除 1：删除',
				create_time       datetime               null  comment '创建时间',
				update_time       datetime               null  comment '更新时间',
				operate_person    varchar(45)            null  comment '操作人'
			) comment '在线客服设置'  charset = utf8;
			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`online_service`;
			INSERT INTO `micro_system_temp`.`online_service` SELECT
			`online_service_id`, `store_id`, `server_status`, `service_title`, `effective_pc`, `effective_app`, `effective_mobile`, `del_flag`, `create_time`, `update_time`, `operate_person`
			FROM
				`micro-system`.`online_service`
			WHERE
				app_key = appkey;

-- 创建表：online_service_item
			-- 创建表：online_service_item
			DROP TABLE IF EXISTS `micro_system_temp`.online_service_item;
			CREATE TABLE `micro_system_temp`.online_service_item(
				service_item_id          int auto_increment  comment '在线客服座席id'    primary key,
				store_id                 bigint                 null  comment '店铺ID',
				online_service_id        int                    not null  comment '在线客服主键',
				customer_service_name    varchar(10)            not null  comment '客服昵称',
				customer_service_account varchar(20)            not null  comment '客服账号',
				del_flag                 tinyint(1) default '0' null  comment '删除标志 默认0：未删除 1：删除',
				create_time              datetime               null  comment '创建时间',
				update_time              datetime               null  comment '更新时间',
				operate_person           varchar(45)            null  comment '操作人'
			) comment '在线客服座席表'  charset = utf8;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`online_service_item`;
			INSERT INTO `micro_system_temp`.`online_service_item` SELECT
			`service_item_id`, `store_id`, `online_service_id`, `customer_service_name`, `customer_service_account`, `del_flag`, `create_time`, `update_time`, `operate_person`
			FROM
				`micro-system`.`online_service_item`
			WHERE
				app_key = appkey;

-- 创建表：store_express_company_rela
			-- 创建表：store_express_company_rela
			DROP TABLE IF EXISTS `micro_system_temp`.store_express_company_rela;
			CREATE TABLE `micro_system_temp`.store_express_company_rela(
				id                 bigint(10) auto_increment  comment '主键UUID'    primary key,
				express_company_id bigint(5)                null  comment '主键ID,自增',
				store_id           bigint                   null  comment '店铺标识',
				company_info_id    int                      null  comment '商家标识'
			)  comment '店铺快递公司关联表'  charset = utf8mb4;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`store_express_company_rela`;
			INSERT INTO `micro_system_temp`.`store_express_company_rela`
			SELECT
			`id`, `express_company_id`, `store_id`, `company_info_id`
			FROM
				`micro-system`.`store_express_company_rela`
			WHERE
				app_key = appkey;

-- 创建表：store_resource
		-- 创建表：store_resource
			DROP TABLE IF EXISTS `micro_system_temp`.store_resource;
			CREATE TABLE `micro_system_temp`.store_resource(
			resource_id     bigint auto_increment  comment '素材ID'    primary key,
			resource_type   tinyint(2) default '0'    not null  comment '资源类型(0:图片,1:视频)',
			cate_id         bigint                    not null  comment '素材分类ID',
			store_id        bigint                    null  comment '店铺标识',
			company_info_id int                       null  comment '商家标识',
			resource_key    varchar(255) charset utf8 null  comment '素材KEY',
			resource_name   varchar(45) charset utf8  null  comment '素材名称',
			artwork_url     varchar(255) charset utf8 null  comment '素材地址',
			create_time     datetime                  null  comment '创建时间',
			update_time     datetime                  null  comment '更新时间',
			del_flag        tinyint                   null  comment '删除标识,0:未删除1:已删除',
			server_type     varchar(255) charset utf8 null  comment 'oss服务器类型，对应system_config的config_type'
		)  comment '店铺素材资源表'  charset = utf8mb4;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`store_resource`;
			INSERT INTO `micro_system_temp`.`store_resource`
			SELECT
			`resource_id`, `resource_type`, `cate_id`, `store_id`, `company_info_id`, `resource_key`, `resource_name`, `artwork_url`, `create_time`, `update_time`, `del_flag`, `server_type`
			FROM
				`micro-system`.`store_resource`
			WHERE
				app_key = appkey;

-- 创建表：store_resource_cate
    -- 创建表：store_resource_cate
		DROP TABLE IF EXISTS `micro_system_temp`.store_resource_cate;
		CREATE TABLE `micro_system_temp`.store_resource_cate(
		cate_id         bigint auto_increment  comment '素材分类id'    primary key,
		store_id        bigint                     null  comment '店铺标识',
		company_info_id int                        null  comment '商家标识',
		cate_name       varchar(45) charset utf8   null  comment '分类名称',
		cate_parent_id  bigint                     null  comment '父分类ID',
		cate_img        varchar(255) charset utf8  null  comment '分类图片',
		cate_path       varchar(1000) charset utf8 not null  comment '分类层次路径,例1|01|001',
		cate_grade      tinyint                    not null  comment '分类层级',
		pin_yin         varchar(45) charset utf8   null  comment '拼音',
		s_pin_yin       varchar(45) charset utf8   null  comment '简拼',
		create_time     datetime                   null  comment '创建时间',
		update_time     datetime                   null  comment '更新时间',
		del_flag        tinyint                    null  comment '删除标识,0:未删除1:已删除',
		sort            tinyint                    null  comment '排序',
		is_default      tinyint                    null  comment '是否默认,0:否1:是'
	)  comment '店铺素材资源分类表'  charset = utf8mb4;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`store_resource_cate`;
			INSERT INTO `micro_system_temp`.`store_resource_cate`
			SELECT
		`cate_id`, `store_id`, `company_info_id`, `cate_name`, `cate_parent_id`, `cate_img`, `cate_path`, `cate_grade`, `pin_yin`, `s_pin_yin`, `create_time`, `update_time`, `del_flag`, `sort`, `is_default`
		  FROM
				`micro-system`.`store_resource_cate`
			WHERE
				app_key = appkey;

-- 创建表：sys_sms
    -- 创建表：sys_sms
		DROP TABLE IF EXISTS `micro_system_temp`.sys_sms;
		CREATE TABLE `micro_system_temp`.sys_sms(
			sms_id       varchar(45) not null  comment '主键'  primary key,
			sms_url      varchar(45) null  comment '接口地址',
			sms_name     varchar(45) null  comment '名称',
			sms_pass     varchar(45) null  comment 'SMTP密码',
			sms_gateway  varchar(45) null  comment '网关',
			is_open      tinyint     null  comment '是否开启(0未开启 1已开启)',
			create_time  datetime    null,
			modify_time  datetime    null,
			sms_address  varchar(45) null,
			sms_provider varchar(45) null,
			sms_content  varchar(45) null
		)  comment '系统短信配置'  charset = utf8;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`sys_sms`;
			INSERT INTO `micro_system_temp`.`sys_sms`
			SELECT
		  `sms_id`, `sms_url`, `sms_name`, `sms_pass`, `sms_gateway`, `is_open`, `create_time`, `modify_time`, `sms_address`, `sms_provider`, `sms_content`
		  FROM
				`micro-system`.`sys_sms`
			WHERE
				app_key = appkey;


-- 创建表：system_resource
		-- 创建表：system_resource
		DROP TABLE IF EXISTS `micro_system_temp`.system_resource;
		CREATE TABLE `micro_system_temp`.system_resource(
			resource_id   bigint auto_increment  comment '素材资源ID'    primary key,
			resource_type tinyint(2) default '0'    not null  comment '资源类型(0:图片,1:视频)',
			cate_id       bigint                    not null  comment '素材分类ID',
			resource_key  varchar(255) charset utf8 null  comment '素材KEY',
			resource_name varchar(45) charset utf8  null  comment '素材名称',
			artwork_url   varchar(255) charset utf8 null  comment '素材地址',
			create_time   datetime                  null  comment '创建时间',
			update_time   datetime                  null  comment '更新时间',
			del_flag      tinyint                   null  comment '删除标识,0:未删除1:已删除',
			server_type   varchar(255) charset utf8 null  comment 'oss服务器类型，对应system_config的config_type'
		)  comment '平台素材资源表'  charset = utf8mb4;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`system_resource`;
			INSERT INTO `micro_system_temp`.`system_resource`
			SELECT
		 `resource_id`, `resource_type`, `cate_id`, `resource_key`, `resource_name`, `artwork_url`, `create_time`, `update_time`, `del_flag`, `server_type`
   		FROM
				`micro-system`.`system_resource`
			WHERE
				app_key = appkey;

-- 创建表：system_resource_cate
		-- 创建表：system_resource_cate
			DROP TABLE IF EXISTS `micro_system_temp`.system_resource_cate;
			CREATE TABLE `micro_system_temp`.system_resource_cate(
				cate_id        bigint auto_increment  comment '素材资源分类id'    primary key,
				cate_name      varchar(45) charset utf8   null  comment '分类名称',
				cate_parent_id bigint                     null  comment '父分类ID',
				cate_img       varchar(255) charset utf8  null  comment '分类图片',
				cate_path      varchar(1000) charset utf8 not null  comment '分类层次路径,例1|01|001',
				cate_grade     tinyint                    not null  comment '分类层级',
				pin_yin        varchar(45) charset utf8   null  comment '拼音',
				s_pin_yin      varchar(45) charset utf8   null  comment '简拼',
				create_time    datetime                   null  comment '创建时间',
				update_time    datetime                   null  comment '更新时间',
				del_flag       tinyint                    null  comment '删除标识,0:未删除1:已删除',
				sort           tinyint                    null  comment '排序',
				is_default     tinyint                    null  comment '是否默认,0:否1:是'
			)  comment '素材资源分类表'  charset = utf8mb4;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`system_resource_cate`;
			INSERT INTO `micro_system_temp`.`system_resource_cate`
			SELECT
		 `cate_id`, `cate_name`, `cate_parent_id`, `cate_img`, `cate_path`, `cate_grade`, `pin_yin`, `s_pin_yin`, `create_time`, `update_time`, `del_flag`, `sort`, `is_default`
   		FROM
				`micro-system`.`system_resource_cate`
			WHERE
				app_key = appkey;


-- 创建表：wechat_login_set
			-- 创建表：wechat_login_set
			DROP TABLE IF EXISTS `micro_system_temp`.wechat_login_set;
			CREATE TABLE `micro_system_temp`.wechat_login_set(
				wechat_set_id        varchar(32) not null  comment '微信授权登录配置主键'  primary key,
				mobile_server_status tinyint     null  comment 'h5-微信授权登录是否启用 0 不启用， 1 启用',
				mobile_app_id        varchar(60) null  comment 'h5-AppID(应用ID)',
				mobile_app_secret    varchar(60) null  comment 'h5-AppSecret(应用密钥)',
				pc_server_status     tinyint     null  comment 'pc-微信授权登录是否启用 0 不启用， 1 启用',
				pc_app_id            varchar(60) null  comment 'pc-AppID(应用ID)',
				pc_app_secret        varchar(60) null  comment 'pc-AppSecret(应用密钥)',
				app_server_status    tinyint     null  comment 'app-微信授权登录是否启用 0 不启用， 1 启用',
				create_time          datetime    null  comment '创建时间',
				update_time          datetime    null  comment '更新时间',
				operate_person       varchar(45) null  comment '操作人'
			)  comment '微信授权登录配置'  charset = utf8;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`wechat_login_set`;
			INSERT INTO `micro_system_temp`.`wechat_login_set`
			SELECT
		`wechat_set_id`, `mobile_server_status`, `mobile_app_id`, `mobile_app_secret`, `pc_server_status`, `pc_app_id`, `pc_app_secret`, `app_server_status`, `create_time`, `update_time`, `operate_person`
	  	FROM
				`micro-system`.`wechat_login_set`
			WHERE
				app_key = appkey;

-- 创建表：wechat_share_set
		-- 创建表：wechat_share_set
			DROP TABLE IF EXISTS `micro_system_temp`.wechat_share_set;
			CREATE TABLE `micro_system_temp`.wechat_share_set(
				share_set_id     varchar(32) not null  comment '微信分享参数配置主键'  primary key,
				share_app_id     varchar(60) not null  comment '微信公众号App ID',
				share_app_secret varchar(60) not null  comment '微信公众号 App Secret',
				create_time      datetime    null  comment '创建时间',
				update_time      datetime    null  comment '更新时间',
				operate_person   varchar(45) null  comment '操作人'
			)  comment '微信分享参数配置'  charset = utf8;

					-- 查询指定数据并插入
					DELETE
					FROM
						`micro_system_temp`.`wechat_share_set`;
					INSERT INTO `micro_system_temp`.`wechat_share_set`
					SELECT
					`share_set_id`, `share_app_id`, `share_app_secret`, `create_time`, `update_time`, `operate_person`
					FROM
						`micro-system`.`wechat_share_set`
					WHERE
						app_key = appkey;

-- 创建表：system_config_temp 注意system_config_temp表中数据需要比对后插入system_confi中
		-- 创建表：system_config_temp
			DROP TABLE IF EXISTS `micro_system_temp`.system_config_temp;
			CREATE TABLE `micro_system_temp`.system_config_temp(
				id          bigint auto_increment  comment ' 编号'  primary key,
				config_key  varchar(255) not null  comment '键',
				config_type varchar(255) not null  comment '类型',
				config_name varchar(255) not null  comment '名称',
				remark      varchar(255) null  comment '备注',
				status      tinyint(1)   null  comment '状态,0:未启用1:已启用',
				context     longtext     null  comment '配置内容，如JSON内容',
				create_time datetime     null  comment '创建时间',
				update_time datetime     null  comment '更新时间',
				del_flag    tinyint      null  comment '删除标识,0:未删除1:已删除'
			)  comment '系统配置表'  charset = utf8;

			-- 查询指定数据并插入
			DELETE
			FROM
				`micro_system_temp`.`system_config_temp`;
			INSERT INTO `micro_system_temp`.`system_config_temp`
			SELECT
		  `id`, `config_key`, `config_type`, `config_name`, `remark`, `status`, `context`, `create_time`, `update_time`, `del_flag`
		  FROM
				`micro-system`.`system_config`
			WHERE
				app_key = appkey;



-- 导出备份库为备份脚本
END

INSERT INTO `sbc-setting`.system_config (config_key, config_type, config_name, remark, status, context, create_time,
update_time, del_flag) VALUES ('value_added_services', 'vas_iep_setting', '增值服务-企业购', '判断商城是否购买增值服务-企业购', 1, null,
'2020-02-28 09:00:00', '2020-02-28 09:00:00', 0);

CREATE DATABASE IF NOT EXISTS `sbc-vas`;

create table `sbc-vas`.`iep_setting`
(
    `id`                                   varchar(32)  null comment ' id ',
    `enterprise_customer_name`             varchar(8)   not null comment ' 企业会员名称 ',
    `enterprise_price_name`                varchar(10)  not null comment ' 企业价名称 ',
    `enterprise_customer_logo`             varchar(500) null comment ' 企业会员logo ',
    `enterprise_customer_audit_flag`       tinyint(4)   not null comment ' 企业会员审核 0: 不需要审核 1: 需要审核 ',
    `enterprise_goods_audit_flag`          tinyint(4)   not null comment ' 企业商品审核 0: 不需要审核 1: 需要审核 ',
    `enterprise_customer_register_content` text         not null comment ' 企业会员注册协议 ',
    `create_person`                        varchar(32)  not null comment ' 创建人 ',
    `create_time`                          datetime     not null comment ' 创建时间 ',
    `update_person`                        varchar(32)  not null comment ' 修改人 ',
    `update_time`                          datetime     not null comment ' 修改时间 ',
    `del_flag`                             tinyint(4)   not null comment ' 是否删除标志 0：否，1：是 ',
    primary key (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 comment '企业购设置';

-- 初始数据
INSERT INTO `sbc-vas`.iep_setting (id, enterprise_customer_name, enterprise_price_name, enterprise_customer_logo,
                                   enterprise_customer_audit_flag, enterprise_goods_audit_flag,
                                   enterprise_customer_register_content, create_person, create_time, update_person,
                                   update_time, del_flag)
VALUES ('ff808081709b7d3e01709b7ea4a70000', '企业超级会员', '企业专享价', null, 0, 1, '企业会员注册协议', '初始化', '2020-03-02 21:45:33',
        '初始化', '2020-03-02 21:45:33', 0);

-- 短信模板
INSERT INTO `sbc-message`.`sms_template` (
	`template_content`,
	`template_name`,
	`review_reason`,
	`business_type`,
	`open_flag`,
	`purpose`,
	`remark`,
	`template_type`,
	`sms_setting_id`,
	`del_flag`,
	`create_time`,
	`review_status`,
	`sign_id`,
	`template_code`
)
VALUES
	(
		'恭喜您成为企业会员，您可享受企业会员专享价，您的账号是：${account}，密码是: ${password}，快去商城采购吧~',
		'企业会员新增通知',
		NULL,
		'ENTERPRISE_CUSTOMER_PASSWORD',
		'1',
		'企业会员新增通知',
		'企业会员注册成功通知',
		'1',
		NULL,
		'0',
		'2020-03-04 15:21:44',
		'1',
		'48',
		'SMS_184830937'
	);


-- 企业购设置信息权限
INSERT INTO `sbc-setting`.function_info (function_id, system_type_cd, menu_id, function_title, function_name, remark, sort, create_time, del_flag) VALUES ('ff80808170cdbf3b0170d1dda1720001', 4, 'ff80808170860f0801708b43fff60000', '企业购设置', 'f_iep_setting', null, 6, '2020-03-13 11:08:48', 0);
INSERT INTO `sbc-setting`.authority (authority_id, system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES ('ff80808170cdbf3b0170d1dea1670002', 4, 'ff80808170cdbf3b0170d1dda1720001', '企业购设置信息查询', null, '/vas/iep/setting', 'GET', null, 0, '2020-03-13 11:09:53', 0);
INSERT INTO `sbc-setting`.authority (authority_id, system_type_cd, function_id, authority_title, authority_name, authority_url, request_type, remark, sort, create_time, del_flag) VALUES ('ff80808170cdbf3b0170d1ded9f20003', 4, 'ff80808170cdbf3b0170d1dda1720001', '企业购设置信息修改', null, '/vas/iep/setting', 'PUT', null, 1, '2020-03-13 11:10:08', 0);



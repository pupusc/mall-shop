INSERT INTO b2b.system_config (id, config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag) VALUES (3, 'ticket_aduit', 'ticket_aduit', '增专资质审核开关', NULL, 1, NULL, '2017-06-20 17:00:54', '2017-06-20 17:01:04', 0);

ALTER TABLE b2b.employee MODIFY employee_mobile VARCHAR(20) COMMENT '员工手机号';

--将会员名称允许为空
ALTER TABLE `customer_detail` MODIFY COLUMN `customer_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '会员名称' AFTER `customer_id`;

-- 给客户表加个字段，安全等级
ALTER TABLE customer ADD COLUMN safe_level tinyint(4) NULL COMMENT '密码安全等级：20危险 40低、60中、80高' AFTER customer_password;

-- 账号类型
alter table employee add column account_type tinyint(1) DEFAULT NULL COMMENT '账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号'

-- 管理员
INSERT INTO employee (employee_id,employee_name,employee_mobile,role_id,is_employee,account_name,account_password,employee_salt_val,account_state,third_id,customer_id,del_flag,create_time,create_person,login_error_time,login_lock_time,login_time,update_time,update_person,delete_time,delete_person,company_info_id,is_master_account, account_type)VALUES('2c8080815cd3a74a015cd3ae86850001','system',NULL,NULL,0,'system','a61900a29997d3c9a2918e342a249643','991c96c88b421ac565b41f622d56978f7ffffea37fae86872711464da0e4c99b',0,NULL,NULL,0,'9999-06-23 14:40:49',NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,1,1,0);

-- 增票资质表
ALTER TABLE customer_invoice ADD COLUMN reject_reason varchar(100) DEFAULT NULL COMMENT '审核未通过原因' AFTER check_state;
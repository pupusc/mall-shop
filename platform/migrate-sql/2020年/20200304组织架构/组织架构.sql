use `sbc-customer`;

CREATE TABLE `department` (
                              `department_id` varchar(32) NOT NULL COMMENT '主键',
                              `department_name` varchar(50) NOT NULL COMMENT '部门名称',
                              `company_info_id` bigint(11) NOT NULL COMMENT '公司ID',
                              `department_grade` int(10) NOT NULL DEFAULT '1' COMMENT '层级',
                              `employee_id` varchar(32) DEFAULT NULL COMMENT '主管员工ID',
                              `employee_name` varchar(45) DEFAULT NULL COMMENT '主管名称',
                              `department_sort` int(10) NOT NULL DEFAULT '0' COMMENT '排序',
                              `parent_department_id` varchar(32) NOT NULL COMMENT '父部门id（上一级）',
                              `employee_num` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '员工数',
                              `parent_department_ids` varchar(500) NOT NULL COMMENT '父部门id集合（多级）,例如 1|15|88',
                              `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除，0：否，1：是',
                              `create_time` datetime NOT NULL COMMENT '创建时间',
                              `create_person` varchar(255) NOT NULL COMMENT '创建人',
                              `update_time` datetime NOT NULL COMMENT '更新时间',
                              `update_person` varchar(255) DEFAULT NULL COMMENT '更新人',
                              PRIMARY KEY (`department_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

ALTER TABLE `sbc-customer`.`employee`
    MODIFY COLUMN `account_state` tinyint(4) NOT NULL COMMENT '账户状态  0：启用   1：禁用  2：离职' AFTER `employee_salt_val`,
    MODIFY COLUMN `company_info_id` bigint(11) NULL DEFAULT NULL COMMENT '公司信息ID' AFTER `login_time`,
    MODIFY COLUMN `is_master_account` tinyint(4) NULL DEFAULT NULL COMMENT '是否主账号 0、否 1、是' AFTER `company_info_id`,
    MODIFY COLUMN `account_type` tinyint(1) NULL DEFAULT NULL COMMENT '账号类型 0 b2b账号 1 s2b平台端账号 2 商家端账号 3供应商端账号' AFTER `is_master_account`,
    ADD COLUMN `email` varchar(50) NULL COMMENT '邮箱' AFTER `account_type`,
    ADD COLUMN `job_no` varchar(32) NULL COMMENT '工号' AFTER `email`,
    ADD COLUMN `position` varchar(32) NULL COMMENT '职位' AFTER `job_no`,
    ADD COLUMN `birthday` date NULL COMMENT '生日' AFTER `position`,
    ADD COLUMN `sex` tinyint(1) NULL DEFAULT 0 COMMENT '性别，0：保密，1：男，2：女' AFTER `birthday`,
    ADD COLUMN `become_member` tinyint(1) NULL DEFAULT 0 COMMENT '是否激活会员账号，0：否，1：是' AFTER `sex`,
    ADD COLUMN `heir_employee_id` varchar(32) NULL COMMENT '交接人员工ID' AFTER `become_member`,
    MODIFY COLUMN `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间' AFTER `heir_employee_id`,
    MODIFY COLUMN `create_person` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `create_time`;

ALTER TABLE `sbc-customer`.`employee` change `role_id` `role_ids` varchar(255) null comment '员工角色集合' ,
            ADD COLUMN `department_ids` varchar(500) NULL COMMENT '所属部门集合',
            ADD COLUMN `manage_department_ids` varchar(500) NULL COMMENT '管理部门集合';

ALTER TABLE `sbc-customer`.`customer_detail` ADD COLUMN `is_employee` tinyint(1) default '0' COMMENT '是否为员工 0：否 1：是';

UPDATE `employee` set `manage_department_ids` = 0 where `is_master_account` = 1;


update `sbc-setting`.menu_info m set m.menu_name = '组织架构' where m.menu_name = '员工管理' and m.menu_grade = 2;

update `sbc-setting`.menu_info m set m.menu_name = '角色权限',m.menu_url = '/role-list' where m.menu_name = '权限分配' and m.menu_grade = 3;

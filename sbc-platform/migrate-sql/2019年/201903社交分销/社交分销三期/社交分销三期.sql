-- ----------------------------------------------xxl-job-------------------------------------------
insert into `xxl-job`.`xxl_job_qrtz_trigger_info` (`executor_fail_retry_count`, `executor_param`, `job_cron`, `glue_type`, `author`, `glue_source`, `glue_remark`, `glue_updatetime`, `executor_timeout`, `executor_block_strategy`, `executor_handler`, `alarm_email`, `child_jobid`, `job_group`, `executor_route_strategy`, `add_time`, `update_time`, `job_desc`) values ('0', '', '0 0 5 * * ?', 'BEAN', '吕振伟', '', 'GLUE代码初始化', '2019-05-28 10:26:23', '0', 'SERIAL_EXECUTION', 'pointsOrderAutoReceiveJobHandle', '', '', '1', 'FIRST', '2019-05-28 10:26:23', '2019-05-28 10:26:23', '积分订单自动确认收货');
insert into `xxl-job`.`xxl_job_qrtz_trigger_info` (`executor_fail_retry_count`, `executor_param`, `job_cron`, `glue_type`, `author`, `glue_source`, `glue_remark`, `glue_updatetime`, `executor_timeout`, `executor_block_strategy`, `executor_handler`, `alarm_email`, `child_jobid`, `job_group`, `executor_route_strategy`, `add_time`, `update_time`, `job_desc`) values ('0', '', '0 0 2 * * ?', 'BEAN', '李琼', '', 'GLUE代码初始化', '2019-07-05 11:01:55', '0', 'SERIAL_EXECUTION', 'distributionTempJobHandler', '', '', '1', 'FIRST', '2019-07-05 11:01:55', '2019-07-05 11:01:55', '社交分销临时表发放邀新奖励、分销佣金');
insert into `xxl-job`.`xxl_job_qrtz_trigger_info` (`executor_fail_retry_count`, `executor_param`, `job_cron`, `glue_type`, `author`, `glue_source`, `glue_remark`, `glue_updatetime`, `executor_timeout`, `executor_block_strategy`, `executor_handler`, `alarm_email`, `child_jobid`, `job_group`, `executor_route_strategy`, `add_time`, `update_time`, `job_desc`) values ( '0', '', '0 0 1 * * ?', 'BEAN', '李琼', '', 'GLUE代码初始化', '2019-07-05 11:07:23', '0', 'SERIAL_EXECUTION', 'distributorGoodsJobHandler', '', '', '1', 'FIRST', '2019-07-05 11:07:23', '2019-07-05 11:07:23', '社交分销-店铺签约时间到期分销商品删除');
insert into `xxl-job`.`xxl_job_qrtz_trigger_info` (`executor_fail_retry_count`, `executor_param`, `job_cron`, `glue_type`, `author`, `glue_source`, `glue_remark`, `glue_updatetime`, `executor_timeout`, `executor_block_strategy`, `executor_handler`, `alarm_email`, `child_jobid`, `job_group`, `executor_route_strategy`, `add_time`, `update_time`, `job_desc`) values ( '0', '', '0 0 1 * * ?', 'BEAN', '李琼', '', 'GLUE代码初始化', '2019-07-05 11:14:28', '0', 'SERIAL_EXECUTION', 'orderJobHandler', '', '', '1', 'FIRST', '2019-07-05 11:14:28', '2019-07-05 11:14:28', '订单定时任务自动确认收货、自动审核等');
insert into `xxl-job`.`xxl_job_qrtz_trigger_info` (`executor_fail_retry_count`, `executor_param`, `job_cron`, `glue_type`, `author`, `glue_source`, `glue_remark`, `glue_updatetime`, `executor_timeout`, `executor_block_strategy`, `executor_handler`, `alarm_email`, `child_jobid`, `job_group`, `executor_route_strategy`, `add_time`, `update_time`, `job_desc`) values ( '0', '', '0 0 0,4 * * ?', 'BEAN', '李琼', '', 'GLUE代码初始化', '2019-07-05 14:05:12', '0', 'SERIAL_EXECUTION', 'distributionRankingJobHandler', '', '', '1', 'FIRST', '2019-07-05 14:05:12', '2019-07-05 14:05:12', '社交分销周排行榜数据');

-- ----------------------------------------------account-------------------------------------------
ALTER TABLE `customer_funds_detail`
MODIFY COLUMN `sub_type`  tinyint(4) NULL DEFAULT NULL COMMENT '资金子类型 \'1\'：推广返利；\'2\'：佣金提现；\'3\'：邀新奖励；\'4\':自购返利；\'5\':推广提成' AFTER `update_time`;

ALTER TABLE `customer_funds_detail`
MODIFY COLUMN `funds_type`  tinyint(4) NOT NULL COMMENT '资金类型 \'1\'：分销佣金；\'2\'：佣金提现；\'3\'：邀新奖励；\'4\':佣金提成' AFTER `business_id`;


-- ---------------------------------------------customer-------------------------------------------

ALTER TABLE `distribution_customer`
  ADD COLUMN `distributor_level_id`  varchar(32) NULL COMMENT '分销员等级ID' AFTER `commission_total`,
  ADD COLUMN `invite_code`  varchar(8) COMMENT '邀请码' AFTER `distributor_level_id`,
  ADD COLUMN `invite_customer_ids`  varchar(256) NULL COMMENT '邀请人会员ID集合，后期可扩展N级' AFTER `invite_code`,
  ADD UNIQUE INDEX `index_invite_code` (`invite_code`) USING BTREE ;

-- 分销员等级表
create table distributor_level
(
  distributor_level_id   varchar(32)    not null
  comment '分销员等级id'
    primary key,
  distributor_level_name varchar(255)   not null
  comment '分销员等级名称',
  commission_rate        decimal(4,2)   not null
  comment '佣金比例',
  percentage_rate        decimal(4,2)   not null
  comment '佣金提成比例',
  sales_flag             tinyint        null
  comment '销售额门槛是否开启',
  sales_threshold        decimal(18, 2) null
  comment '销售额门槛',
  record_flag            tinyint        null
  comment '到账收益额门槛是否开启',
  record_threshold       decimal(18, 2) null
  comment '到账收益额门槛',
  invite_flag            tinyint        null
  comment '邀请人数门槛是否开启',
  invite_threshold       int            null
  comment '邀请人数门槛',
  update_time            datetime       null
  comment '修改时间',
  update_person          varchar(32)    null
  comment '修改人',
  sort                   tinyint        null
  comment '等级排序',
  del_flag               tinyint        null
  comment '删除标识'
)
  comment '分销员等级' collate = utf8mb4_unicode_ci;

-- ----------------------------------------------marketing-----------------------------------------------
-- 分销设置表
-- 新增注册限制、佣金返利优先级
ALTER TABLE distribution_setting ADD register_limit_type tinyint(4) DEFAULT 0 NOT NULL COMMENT '注册限制 0：不限，1：仅限邀请注册'
AFTER shop_share_img;
ALTER TABLE distribution_setting ADD commission_priority_type tinyint(4) DEFAULT 1 NOT NULL COMMENT '佣金返利优先级 0：邀请人优先，1：店铺优先' AFTER register_limit_type;
ALTER TABLE distribution_setting ADD commission_unhook_type tinyint(4) DEFAULT 0 NOT NULL COMMENT '佣金提成脱钩 0：不限，1：分销员和邀请人平级时脱钩，2：分销员高于邀请人等级时脱钩';
ALTER TABLE distribution_setting ADD distributor_level_desc text NULL COMMENT '分销员等级规则';
ALTER TABLE distribution_setting ADD base_limit_type tinyint(4) DEFAULT 0 NOT NULL COMMENT '基础邀新奖励限制 0：不限，1：仅限有效邀新'
AFTER register_limit_type;









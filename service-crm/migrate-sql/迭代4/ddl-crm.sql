SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer_plan
-- ----------------------------
DROP TABLE IF EXISTS `customer_plan`;
CREATE TABLE `customer_plan` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `plan_name` varchar(20) NOT NULL COMMENT '计划名称',
  `trigger_flag` tinyint(4) NOT NULL COMMENT '触发条件标志 0:否1:是',
  `trigger_condition` text COMMENT '触发条件，以逗号分隔（1-有访问；2-有收藏；3-有加购，4-有下单；5-有付款）',
  `start_date` date NOT NULL COMMENT '计划开始时间',
  `end_date` date NOT NULL COMMENT '计划结束时间',
  `receive_type` tinyint(4) NOT NULL COMMENT '目标人群类型（0-全部，1-会员等级，2-会员人群，3-自定义）',
  `receive_value` text CHARACTER SET utf8mb4 COMMENT '目标人群值 值以type_id结构组成,type代表类型,0:系统分群,1:自定义分群, id:对应分群数据id',
  `point_flag` tinyint(4) NOT NULL COMMENT '是否送积分 0:否1:是',
  `points` int(10) DEFAULT NULL COMMENT '赠送积分值',
  `coupon_flag` tinyint(4) NOT NULL COMMENT '是否送优惠券 0:否1:是',
  `customer_limit_flag` tinyint(4) NOT NULL COMMENT '是否每人限发次数 0:否1:是',
  `customer_limit` int(10) DEFAULT NULL COMMENT '每人限发次数值',
  `gift_package_total` int(10) NOT NULL COMMENT '权益礼包总数',
  `gift_package_count` int(10) DEFAULT NULL COMMENT '已发送礼包数',
  `sms_flag` tinyint(4) NOT NULL COMMENT '短信标识 0:否1:是',
  `app_push_flag` tinyint(4) NOT NULL COMMENT '站内信标识 0:否1:是',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志位，0:未删除，1:已删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_person` varchar(50) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_person` varchar(50) DEFAULT NULL COMMENT '更新人',
  `pause_flag` tinyint(4) NOT NULL COMMENT '是否暂停 0:进行1:暂停',
  `activity_id` varchar(32) DEFAULT NULL COMMENT '优惠券活动id',
  `coupon_discount` decimal(10,2) DEFAULT NULL COMMENT '优惠券总抵扣',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' 人群运营计划';

-- ----------------------------
-- Table structure for customer_plan_coupon_rel
-- ----------------------------
DROP TABLE IF EXISTS `customer_plan_coupon_rel`;
CREATE TABLE `customer_plan_coupon_rel` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `plan_id` bigint(11) NOT NULL COMMENT '计划id',
  `coupon_id` varchar(32) NOT NULL COMMENT '优惠券Id',
  `gift_count` int(10) NOT NULL COMMENT '赠送数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人群运营计划与优惠券关联表';

-- ----------------------------
-- Table structure for customer_plan_sms_rel
-- ----------------------------
DROP TABLE IF EXISTS `customer_plan_sms_rel`;
CREATE TABLE `customer_plan_sms_rel` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `sign_id` bigint(11) NOT NULL COMMENT '签名id',
  `sign_name` varchar(50) DEFAULT NULL COMMENT '签名名称',
  `template_code` varchar(50) NOT NULL COMMENT '模板id',
  `template_content` varchar(1024) DEFAULT NULL COMMENT '模板内容',
  `plan_id` bigint(11) NOT NULL COMMENT '计划id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人群运营计划与短信关联表';

-- ----------------------------
-- Table structure for customer_plan_app_push_rel
-- ----------------------------
DROP TABLE IF EXISTS `customer_plan_app_push_rel`;
CREATE TABLE `customer_plan_app_push_rel` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `name` varchar(20) NOT NULL COMMENT '任务名称',
  `notice_title` varchar(40) NOT NULL COMMENT '消息标题',
  `notice_context` varchar(255) NOT NULL COMMENT '消息内容',
  `cover_url` text COMMENT '封面地址',
  `page_url` varchar(255) DEFAULT NULL COMMENT '落页地地址',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人群运营计划与站内信关联表';

-- ----------------------------
-- Table structure for customer_plan_send
-- ----------------------------
DROP TABLE IF EXISTS `customer_plan_send`;
CREATE TABLE `customer_plan_send` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `plan_id` bigint(11) DEFAULT NULL COMMENT '计划id',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `point_flag` tinyint(1) DEFAULT NULL COMMENT '积分发送标识（0-未发送，1-已发送）',
  `coupon_flag` tinyint(1) DEFAULT NULL COMMENT '优惠券发送标识（0-未发送，1-已发送）',
  `sms_flag` tinyint(1) DEFAULT NULL COMMENT '短信发送标识（0-未发送，1-已发送）',
  `app_push_flag` tinyint(1) DEFAULT NULL COMMENT 'app通知发送标识（0-未发送，1-已发送）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营计划发送记录（无触发）';

-- ----------------------------
-- Table structure for customer_plan_trigger_send
-- ----------------------------
DROP TABLE IF EXISTS `customer_plan_trigger_send`;
CREATE TABLE `customer_plan_trigger_send` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `plan_id` bigint(11) DEFAULT NULL COMMENT '计划id',
  `stat_date` date DEFAULT NULL COMMENT '统计时间',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `point_flag` tinyint(1) DEFAULT NULL COMMENT '积分发送标识（0-未发送，1-已发送）',
  `coupon_flag` tinyint(1) DEFAULT NULL COMMENT '优惠券发送标识（0-未发送，1-已发送）',
  `sms_flag` tinyint(1) DEFAULT NULL COMMENT '短信发送标识（0-未发送，1-已发送）',
  `app_push_flag` tinyint(1) DEFAULT NULL COMMENT 'app通知发送标识（0-未发送，1-已发送）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营计划发送记录（有触发）';

SET FOREIGN_KEY_CHECKS = 1;
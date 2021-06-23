use `sbc-message`;
CREATE TABLE `message_send` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(40) DEFAULT NULL COMMENT '任务名称',
  `message_type` tinyint(4) DEFAULT NULL COMMENT '消息类型 0优惠促销',
  `title` varchar(40) DEFAULT NULL COMMENT '消息标题',
  `content` varchar(255) COMMENT '消息内容',
  `img_url` varchar(255) DEFAULT NULL COMMENT '封面图',
  `send_type` tinyint(4) DEFAULT NULL COMMENT '推送类型 0：全部会员、1：按会员等级、2：按标签、3：按人群、4：指定会员',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识 0：未删除 1：删除',
  `push_flag` tinyint(4) DEFAULT NULL COMMENT '消息同步标识 0：push消息、1：运营计划',
  `send_sum` int(11) DEFAULT NULL COMMENT '发送数',
  `open_sum` int(11) DEFAULT NULL COMMENT '打开数',
  `send_time_type` tinyint(4) DEFAULT NULL COMMENT '推送时间类型 0：立即、1：定时',
  `push_id` varchar(32) DEFAULT NULL COMMENT '推送id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=357 DEFAULT CHARSET=utf8mb4 COMMENT='站内信任务表';

CREATE TABLE `message_send_customer_scope` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `message_id` bigint(20) DEFAULT NULL COMMENT '消息id',
  `join_id` varchar(32) DEFAULT NULL COMMENT '关联的会员、等级、人群、标签id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=738 DEFAULT CHARSET=utf8mb4 COMMENT='站内信消息会员关联表';

CREATE TABLE `message_send_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `node_name` varchar(40) NOT NULL COMMENT '节点名称',
  `node_title` varchar(40) DEFAULT NULL COMMENT '节点标题',
  `node_content` varchar(255) DEFAULT NULL COMMENT '内容',
  `status` tinyint(4) NOT NULL COMMENT '启用状态：0未启用 1启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标识 0未删除 1删除',
  `send_sum` int(11) DEFAULT NULL COMMENT '发送数',
  `open_sum` int(11) DEFAULT NULL COMMENT '打开数',
  `route_name` varchar(50) DEFAULT NULL COMMENT '节点跳转路由',
  `node_type` tinyint(4) DEFAULT NULL COMMENT '节点类型 0:账户安全、1账户资产、2订单进度、3退单进度、4分销业务',
  `node_code` varchar(100) DEFAULT NULL COMMENT '节点code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='站内信通知节点表';

INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (1, '订单提交成功通知', '订单提交成功通知', '订单{订单第一行商品名称}已经提交成功，请及时付款哦～', 1, '2019-01-09 15:30:00', NULL, '2020-01-16 21:29:27', '2c8080815cd3a74a015cd3ae86850001', 0, 0, 0, 'OrderDetail', 2, 'ORDER_COMMIT_SUCCESS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (2, '订单提交成功审核通知', '订单提交成功审核通知', '订单{订单第一行商品名称}已经提交成功，该订单需要商家审核，请耐心等待哦~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'ORDER_COMMIT_SUCCESS_CHECK');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (3, '订单审核通过通知', '订单审核通过通知', '订单{订单第一行商品名称}已通过商家审核，请及时付款，别错过哦~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'ORDER_CHECK_PASS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (4, '订单审核未通过通知', '订单审核未通过通知', '订单{订单第一行商品名称}未通过商家审核，原因是：{驳回原因}，点击查看>', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'ORDER_CHECK_NOT_PASS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (5, '订单支付成功通知', '订单支付成功通知', '订单{订单第一行商品名称}支付成功，我们将尽快为您安排发货哦~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'ORDER_PAY_SUCCESS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (6, '订单发货通知', '订单发货通知', '订单{订单第一行商品名称}已发货，请注意物流进度哦~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'OrderDetail', 2, 'ORDER_SEND_GOODS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (7, '订单完成通知', '订单完成通知', '订单{订单第一行商品名称}已完成，期待您分享商品评价与购物心得哦~点击评价>', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'EvaluateDrying', 2, 'ORDER_COMPILE');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (8, '商品评价提醒', '商品评价提醒', '商品{订单第一行商品名称}还没有收到您的评价呢，期待您与我们分享哦~点击评价>', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'EvaluationList', 2, 'GOODS_EVALUATION');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (9, '服务评价提醒', '服务评价提醒', '订单{订单第一行商品名称}还没有收到您的服务评价呢，期待您与我们分享哦~点击评价>', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'EvaluationList', 2, 'SERVICE_EVALUATION');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (10, '开团成功通知', '开团成功通知', '您的拼团{商品名称}已开团成功，快去邀请好友一起拼团吧~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'SpellgroupDetail', 2, 'GROUP_OPEN_SUCCESS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (11, '参团人数不足提醒', '参团人数不足提醒', '您的拼团{商品名称}即将结束，还差{剩余人数}人就可成团了，快去邀请好友一起拼团吧~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'SpellgroupDetail', 2, 'GROUP_NUM_LIMIT');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (12, '拼团成功通知', '拼团成功通知', '恭喜，您的拼团{商品名称}已成团，我们将尽快为您安排发货哦~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'SpellgroupDetail', 2, 'JOIN_GROUP_SUCCESS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (13, '拼团失败通知', '拼团失败通知', '很抱歉，您的拼团{商品名称}组团失败了，将在1-3个工作日内自动退款~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'SpellgroupDetail', 2, 'JOIN_GROUP_FAIL');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (14, '售后单提交成功通知', '售后单提交成功通知', '售后单{退单第一行商品名称}已经提交成功，需要商家审核，请耐心等待哦~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'ReturnDetail', 3, 'AFTER_SALE_ORDER_COMMIT_SUCCESS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (15, '售后审核通过通知', '售后审核通过通知', '售后单{退单第一行商品名称}已通过商家审核，请及时退回您的货品哦~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'ReturnDetail', 3, 'AFTER_SALE_ORDER_CHECK_PASS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (16, '售后审核未通过通知', '售后审核未通过通知', '很抱歉，售后单{退单第一行商品名称}未通过商家审核，原因是：{驳回原因}，点击查看>', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'ReturnDetail', 3, 'AFTER_SALE_ORDER_CHECK_NOT_PASS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (17, '退货物品拒收通知', '退货物品拒收通知', '很抱歉，您的退货物品{退单第一行商品名称}被商家拒收，原因是：{驳回原因}，点击查看>', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'ReturnDetail', 3, 'RETURN_ORDER_GOODS_REJECT');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (18, '退款审核通过通知', '退款审核通过通知', '售后单{退单第一行商品名称}已通过退款审核，将在1-3个工作日内自动退款~', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'ReturnDetail', 3, 'REFUND_CHECK_PASS');
INSERT INTO `message_send_node`(`id`, `node_name`, `node_title`, `node_content`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`, `send_sum`, `open_sum`, `route_name`, `node_type`, `node_code`) VALUES (19, '退款审核未通过通知', '退款审核未通过通知', '很抱歉，售后单{退单第一行商品名称}未通过商家退款审核，原因是：{驳回原因}，点击查看>', 1, '2019-01-09 15:30:00', NULL, NULL, NULL, 0, 0, 0, 'ReturnDetail', 3, 'REFUND_CHECK_NOT_PASS');

-- 分表 0 - 9，先创建0，剩下的1-9由后面的存储过程生成
CREATE TABLE `app_message_0` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `message_type` tinyint(4) DEFAULT NULL COMMENT '消息一级类型：0优惠促销、1服务通知',
  `message_type_detail` tinyint(4) DEFAULT NULL COMMENT '消息二级类型',
  `img_url` varchar(255) DEFAULT NULL COMMENT '封面图',
  `title` varchar(40) DEFAULT NULL COMMENT '消息标题',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  `route_name` varchar(50) DEFAULT NULL COMMENT '跳转路由',
  `route_param` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `is_read` tinyint(4) DEFAULT NULL COMMENT '是否已读 0：未读、1：已读',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识 0：未删除、1：删除',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '会员id',
  `join_id` bigint(20) DEFAULT NULL COMMENT '关联的任务或节点id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='App站内信消息发送表';

-- 存储过程，动态创建站内信消息发送分表1-9
drop procedure if exists  pro_app_message_sub_table;

delimiter $
create procedure pro_app_message_sub_table(tableCount  int)
begin
  declare i int;
  DECLARE table_name VARCHAR(20);
  DECLARE table_pre VARCHAR(20);
  DECLARE sql_text VARCHAR(500);
  set i = 1;
  SET table_pre = 'app_message_';
  while i <= tableCount  do
    SET @table_name = CONCAT(table_pre,CONCAT(i, ''));
    SET sql_text = CONCAT('CREATE TABLE ', @table_name, ' like app_message_0');
    SET @sql_text=sql_text;
    PREPARE stmt FROM @sql_text;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    set i = i + 1;
  end while;
end $

call pro_app_message_sub_table(9);
-- 存储过程用完删除
drop procedure if exists  pro_app_message_sub_table;

CREATE TABLE `push_customer_enable` (
  `customer_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会员ID',
  `enable_status` tinyint(4) DEFAULT NULL COMMENT '开启状态 0:未开启 1:启用',
  `account_security` tinyint(4) DEFAULT NULL COMMENT '账号安全通知 0:未启用 1:启用',
  `account_assets` tinyint(4) DEFAULT NULL COMMENT '账户资产通知 0:未启用 1:启用',
  `order_progress_rate` tinyint(4) DEFAULT NULL COMMENT '订单进度通知 0:未启用 1:启用',
  `return_order_progress_rate` tinyint(4) DEFAULT NULL COMMENT '退单进度通知 0:未启用 1:启用',
  `distribution` tinyint(4) DEFAULT NULL COMMENT '分销业务通知 0:未启用 1:启用',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标志 0:未删除 1:删除',
  `create_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员推送通知开关';


CREATE TABLE `push_detail` (
  `task_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '友盟推送任务ID',
  `platform` tinyint(4) DEFAULT NULL COMMENT 'task_id对应的平台 0:iOS 1:android',
  `node_id` bigint(20) DEFAULT NULL COMMENT '通知节点ID',
  `send_sum` int(11) DEFAULT NULL COMMENT '实际发送',
  `open_sum` int(11) DEFAULT NULL COMMENT '打开数',
  `send_status` tinyint(4) DEFAULT NULL COMMENT '发送状态 0-排队中, 1-发送中，2-发送完成，3-发送失败，4-消息被撤销',
  `fail_remark` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '失败信息',
  `create_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推送消息详情';


CREATE TABLE `push_send` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `android_task_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '友盟安卓任务ID',
  `ios_task_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '友盟iOS任务ID',
  `msg_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息名称',
  `msg_title` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息标题',
  `msg_context` text COLLATE utf8mb4_unicode_ci COMMENT '消息内容',
  `msg_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息封面图片',
  `msg_router` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '点击消息跳转的页面路由',
  `msg_recipient` tinyint(4) DEFAULT NULL COMMENT '消息接受人 0:全部会员 1:按会员等级 2:按标签 3:按人群 4:指定会员',
  `msg_recipient_detail` longtext COLLATE utf8mb4_unicode_ci COMMENT '等级、标签、人群ID',
  `push_time` datetime DEFAULT NULL COMMENT '推送时间',
  `expected_send_count` int(11) DEFAULT NULL COMMENT '预计发送人数',
  `push_flag` tinyint(4) DEFAULT NULL COMMENT '消息标签 0:push消息 1:运营计划',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标志 0:未删除 1:已删除',
  `create_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=369 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='push推送';


CREATE TABLE `push_send_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `node_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点名称',
  `node_type` tinyint(4) DEFAULT NULL COMMENT '节点类型 0:账户安全、1账户资产、2订单进度、3退单进度、4分销业务',
  `node_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点code',
  `node_title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点标题',
  `node_context` text COLLATE utf8mb4_unicode_ci COMMENT '通知内容',
  `expected_send_count` bigint(20) DEFAULT NULL COMMENT '预计发送',
  `actually_send_count` bigint(11) DEFAULT NULL COMMENT '实际发送总数',
  `open_count` bigint(11) DEFAULT NULL COMMENT '打开总数',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 0:未启用 1:启用',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标志 0:未删除 1:删除',
  `create_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推送节点模版设置';

INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (1, '订单提交成功通知', 2, 'ORDER_COMMIT_SUCCESS', '订单提交成功通知', '订单{订单第一行商品名称}已经提交成功，请及时付款哦～', 0, 0, 0, 1, 0, NULL, NULL, '2c8080815cd3a74a015cd3ae86850001', '2020-01-19 16:26:38');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (2, '订单提交成功审核通知', 2, 'ORDER_COMMIT_SUCCESS_CHECK', '订单提交成功审核通知', '订单{订单第一行商品名称}已经提交成功，该订单需要商家审核，请耐心等待哦~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (3, '订单审核通过通知', 2, 'ORDER_CHECK_PASS', '订单审核通过通知', '订单{订单第一行商品名称}已通过商家审核，请及时付款，别错过哦~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-19 16:25:55');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (4, '订单审核未通过通知', 2, 'ORDER_CHECK_NOT_PASS', '订单审核未通过通知', '订单{订单第一行商品名称}未通过商家审核，原因是：{驳回原因}，点击查看>', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-19 16:26:38');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (5, '订单支付成功通知', 2, 'ORDER_PAY_SUCCESS', '订单支付成功通知', '订单{订单第一行商品名称}支付成功，我们将尽快为您安排发货哦~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (6, '订单发货通知', 2, 'ORDER_SEND_GOODS', '订单发货通知', '订单{订单第一行商品名称}已发货，请注意物流进度哦~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (7, '订单完成通知', 2, 'ORDER_COMPILE', '订单完成通知', '1111订单{订单第一行商品名称}已完成，期待您分享商品评价与购物心得哦~点击评价>', 0, 0, 0, 1, 0, NULL, NULL, '2c8080815cd3a74a015cd3ae86850001', '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (8, '商品评价提醒', 2, 'GOODS_EVALUATION', '商品评价提醒', '商品{订单第一行商品名称}还没有收到您的评价呢，期待您与我们分享哦~点击评价>', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 02:00:03');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (9, '服务评价提醒', 2, 'SERVICE_EVALUATION', '服务评价提醒', '订单{订单第一行商品名称}还没有收到您的服务评价呢，期待您与我们分享哦~点击评价>', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 02:00:03');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (10, '开团成功通知', 2, 'GROUP_OPEN_SUCCESS', '开团成功通知', '您的拼团{商品名称}已开团成功，快去邀请好友一起拼团吧~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (11, '参团人数不足提醒', 2, 'GROUP_NUM_LIMIT', '参团人数不足提醒', '您的拼团{商品名称}即将结束，还差{剩余人数}人就可成团了，快去邀请好友一起拼团吧~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (12, '拼团成功通知', 2, 'JOIN_GROUP_SUCCESS', '拼团成功通知', '恭喜，您的拼团{商品名称}已成团，我们将尽快为您安排发货哦~', 0, 0, 0, 0, 0, NULL, NULL, NULL, '2020-01-19 16:25:27');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (13, '拼团失败通知', 2, 'JOIN_GROUP_FAIL', '拼团失败通知', '很抱歉，您的拼团{商品名称}组团失败了，将在1-3个工作日内自动退款~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (14, '售后单提交成功通知', 3, 'AFTER_SALE_ORDER_COMMIT_SUCCESS', '售后单提交成功通知', '售后单{退单第一行商品名称}已经提交成功，需要商家审核，请耐心等待哦~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (15, '售后审核通过通知', 3, 'AFTER_SALE_ORDER_CHECK_PASS', '售后审核通过通知', '广泛大锅饭规范化{退单第一行商品名称}已通过商家审核，请及时退回您的货品哦~', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (16, '售后审核未通过通知', 3, 'AFTER_SALE_ORDER_CHECK_NOT_PASS', '售后审核未通过通知11231132', '很抱歉，售后单12{退单第一行商品名称}未通过商家审核，原因是：23{驳回原因}，点击查看>314', 0, 0, 0, 1, 0, NULL, NULL, NULL, '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (17, '退货物品拒收通知', 3, 'RETURN_ORDER_GOODS_REJECT', '退货物品拒收通知', '很抱歉，您的退货物品{退单第一行商品名称}被商家拒收，原因是：{驳回原因}，点击查看>', 0, 0, 0, 1, 0, NULL, NULL, '2c8080815cd3a74a015cd3ae86850001', '2020-01-19 16:25:27');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (18, '退款审核通过通知', 3, 'REFUND_CHECK_PASS', '退款审核通过通知', '123售后单{退单第一行商品名称}已通过退款审核，将在1-3个工作日内自动退款~', 0, 0, 0, 1, 0, NULL, NULL, '2c8080815cd3a74a015cd3ae86850001', '2020-01-20 10:32:17');
INSERT INTO `push_send_node`(`id`, `node_name`, `node_type`, `node_code`, `node_title`, `node_context`, `expected_send_count`, `actually_send_count`, `open_count`, `status`, `del_flag`, `create_person`, `create_time`, `update_person`, `update_time`) VALUES (19, '退款审核未通过通知', 3, 'REFUND_CHECK_NOT_PASS', '退款审核未通过通知', '1很抱歉，售后单1{退单第一行商品名称}未通过商家退款审核，原因是：{驳回原因}点击查看>', 0, 0, 0, 1, 0, NULL, NULL, '2c8080815cd3a74a015cd3ae86850001', '2020-01-20 10:32:17');


CREATE TABLE `umeng_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会员ID',
  `devlce_token` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '友盟推送会员设备token',
  `platform` tinyint(4) DEFAULT NULL COMMENT '平台 0:iOS 1:android',
  `binding_time` datetime DEFAULT NULL COMMENT '绑定时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='友盟推送设备与会员关系';




-- xxl-job
use `xxl-job`;
INSERT INTO `xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (40, 1, '0 0 20 * * ?', '商品评价、服务评价提醒', '2020-01-14 20:33:25', '2020-01-14 20:33:25', '许云鹏', '', 'FIRST', 'goodsEvaluateRemindJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-01-14 20:33:25', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (68, 1, '0 0 2 * * ?', '通知节点打开率统计', '2020-01-17 15:09:38', '2020-01-17 15:09:38', '高波', '', 'FIRST', 'PushSendNodeJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-01-17 15:09:38', '');


use `sbc-setting`;
CREATE TABLE `umeng_push_config` (
  `id` int(11) NOT NULL,
  `android_key_id` varchar(64) DEFAULT NULL,
  `android_msg_secret` varchar(64) DEFAULT NULL,
  `android_key_secret` varchar(64) DEFAULT NULL,
  `ios_key_id` varchar(64) DEFAULT NULL,
  `ios_key_secret` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='友盟push接口配置';

INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc37577e90027', 4, 'ff8080816fa8b804016fac46e2860003', '查看站内信通知节点', 'f_view_station_node', NULL, 11, '2020-01-20 22:57:33', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3741e610025', 4, 'ff8080816fa8b804016fac433c130001', '查看推送任务节点', 'f_view_push_node', NULL, 11, '2020-01-20 22:56:05', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc37007690023', 4, 'ff8080816fa8b804016fac433c130001', '推送通知节点开关', 'f_push_node_open_close', NULL, 10, '2020-01-20 22:51:37', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36e9ea40021', 4, 'ff8080816fa8b804016fac433c130001', '编辑推送通知节点', 'f_edit_push_node', NULL, 9, '2020-01-20 22:50:04', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36db776001f', 4, 'ff8080816fa8b804016fac433c130001', '查看推送通知节点列表', 'f_view_push_node_list', NULL, 8, '2020-01-20 22:49:05', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36b2be0001d', 4, 'ff8080816fa8b804016fac433c130001', '删除推送任务', 'f_delete_push_task', NULL, 7, '2020-01-20 22:46:18', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36a1e91001b', 4, 'ff8080816fa8b804016fac433c130001', '添加推送任务', 'f_add_push_task', NULL, 6, '2020-01-20 22:45:09', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3691b0f0019', 4, 'ff8080816fa8b804016fac433c130001', '编辑推送任务', 'f_edit_push_task', NULL, 5, '2020-01-20 22:44:03', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3684e740017', 4, 'ff8080816fa8b804016fac433c130001', '查看推送任务', 'f_view_push_task', NULL, 4, '2020-01-20 22:43:11', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc366e8a30015', 4, 'ff8080816fa8b804016fac433c130001', '查看推送任务列表', 'f_view_push_list', NULL, 3, '2020-01-20 22:41:39', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc365318a0012', 4, 'ff8080816fa8b804016fac433c130001', '编辑友盟设置', 'f_edit_umeng_push_config', NULL, 2, '2020-01-20 22:39:47', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc364e2ef0011', 4, 'ff8080816fa8b804016fac433c130001', '查看友盟设置', 'f_view_umeng_push_config', NULL, 1, '2020-01-20 22:39:26', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35effac000f', 4, 'ff8080816fa8b804016fac46e2860003', '通知节点开关', 'f_station_message_notice_node_open_close', NULL, 8, '2020-01-20 22:33:01', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35cfadb000d', 4, 'ff8080816fa8b804016fac46e2860003', '编辑站内信节点', 'f_edit_station_message_node', NULL, 7, '2020-01-20 22:30:48', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc359cff5000b', 4, 'ff8080816fa8b804016fac46e2860003', '删除站内信', 'f_delete_station_message', NULL, 5, '2020-01-20 22:27:21', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35847790009', 4, 'ff8080816fa8b804016fac46e2860003', '编辑站内信', 'f_edit_station_message', NULL, 4, '2020-01-20 22:25:40', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc355b0190006', 4, 'ff8080816fa8b804016fac46e2860003', '查看站内信', 'f_view_station_message', NULL, 2, '2020-01-20 22:22:50', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35494ac0004', 4, 'ff8080816fa8b804016fac46e2860003', '发送站内信', 'f_add_station_message', NULL, 3, '2020-01-20 22:21:38', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3511c600001', 4, 'ff8080816fa8b804016fac46e2860003', '查看站内信列表', 'f_view_task_list', NULL, 1, '2020-01-20 22:17:50', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc34f52680000', 4, 'ff8080816fa8b804016fac46e2860003', '查看通知节点列表', 'f_view_notice_list', NULL, 10, '2020-01-20 22:15:53', 0);

INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc37675f80028', 4, 'ff8080816fbd1eb8016fc37577e90027', '查看站内信通知节点', NULL, '/messageNode/*', 'GET', NULL, 1, '2020-01-20 22:58:38', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3748a230026', 4, 'ff8080816fbd1eb8016fc3741e610025', '查看推送通知节点', NULL, '/pushsendnode/*', 'GET', NULL, 1, '2020-01-20 22:56:32', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc37080220024', 4, 'ff8080816fbd1eb8016fc37007690023', '推送通知节点开关', NULL, '/pushsendnode/enabled/*/*', 'PUT', NULL, 1, '2020-01-20 22:52:08', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36f5c0f0022', 4, 'ff8080816fbd1eb8016fc36e9ea40021', '编辑推送通知节点', NULL, '/pushsendnode/modify', 'POST', NULL, 1, '2020-01-20 22:50:53', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36e36180020', 4, 'ff8080816fbd1eb8016fc36db776001f', '查看推送通知节点列表', NULL, '/pushsendnode/page', 'POST', NULL, 1, '2020-01-20 22:49:38', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36d4716001e', 4, 'ff8080816fbd1eb8016fc36b2be0001d', '删除推送任务', NULL, '/pushsend/*', 'DELETE', NULL, 1, '2020-01-20 22:48:36', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36a7d18001c', 4, 'ff8080816fbd1eb8016fc36a1e91001b', '添加推送任务', NULL, '/pushsend/add', 'POST', NULL, 1, '2020-01-20 22:45:34', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3697f93001a', 4, 'ff8080816fbd1eb8016fc3691b0f0019', '编辑推送任务', NULL, '/pushsend/modify', 'PUT', NULL, 1, '2020-01-20 22:44:29', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc368ac170018', 4, 'ff8080816fbd1eb8016fc3684e740017', '查看推送任务', NULL, '/pushsend/*', 'GET', NULL, 1, '2020-01-20 22:43:35', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3677e620016', 4, 'ff8080816fbd1eb8016fc366e8a30015', '查看推送列表', NULL, '/pushsend/page', 'POST', NULL, 1, '2020-01-20 22:42:17', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc36616a00014', 4, 'ff8080816fbd1eb8016fc365318a0012', '编辑友盟设置', NULL, '/umengpushconfig/add', 'POST', NULL, 1, '2020-01-20 22:40:45', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3659d450013', 4, 'ff8080816fbd1eb8016fc364e2ef0011', '查看友盟设置', NULL, '/umengpushconfig/getConfig', 'GET', NULL, 1, '2020-01-20 22:40:14', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35f7a110010', 4, 'ff8080816fbd1eb8016fc35effac000f', '站内信通知节点开关', NULL, '/messageNode/status/*', 'GET', NULL, 1, '2020-01-20 22:33:32', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35de743000e', 4, 'ff8080816fbd1eb8016fc35cfadb000d', '编辑站内信节点', NULL, '/messageNode/save', 'PUT', NULL, 1, '2020-01-20 22:31:49', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35b7d57000c', 4, 'ff8080816fbd1eb8016fc359cff5000b', '删除站内信', NULL, '/messageSend/delete/*', 'DELETE', NULL, 1, '2020-01-20 22:29:11', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc359601f000a', 4, 'ff8080816fbd1eb8016fc35847790009', '编辑站内信', NULL, '/messageSend/modify', 'PUT', NULL, 1, '2020-01-20 22:26:52', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35749a70008', 4, 'ff8080816fbd1eb8016fc355b0190006', '查看站内信', NULL, '/messageSend/*', 'GET', NULL, 1, '2020-01-20 22:24:35', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc3552fe90005', 4, 'ff8080816fbd1eb8016fc35494ac0004', '添加站内信', NULL, '/messageSend/add', 'POST', NULL, 1, '2020-01-20 22:22:18', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc353c0d80003', 4, 'ff8080816fbd1eb8016fc34f52680000', '查看站内信通知节点列表', NULL, '/messageNode/page', 'POST', NULL, 1, '2020-01-20 22:20:44', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fbd1eb8016fc35334060002', 4, 'ff8080816fbd1eb8016fc3511c600001', '查看站内信列表', NULL, '/messageSend/page', 'POST', NULL, 1, '2020-01-20 22:20:08', 0);

INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fa8b804016fac46e2860003', 4, 'ff8080816fa8b804016fac4673d50002', 3, '站内信', '/station-message', NULL, 1, '2020-01-16 10:55:24', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fa8b804016fac4673d50002', 4, 'fc8e20ff3fe311e9828800163e0fc468', 2, '站内信', NULL, NULL, 9, '2020-01-16 10:54:56', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fa8b804016fac433c130001', 4, 'ff8080816fa8b804016fac3cf4790000', 3, 'App Push', '/app-push', NULL, 1, '2020-01-16 10:51:25', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816fa8b804016fac3cf4790000', 4, 'fc8e20ff3fe311e9828800163e0fc468', 2, 'App Push', NULL, NULL, 8, '2020-01-16 10:44:34', 0);
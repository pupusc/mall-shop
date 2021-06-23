-- 小程序直播商品表

CREATE TABLE `sbc-goods`.`live_goods` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` bigint(32) DEFAULT NULL COMMENT '微信端商品id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品标题',
  `cover_img_url` varchar(255) DEFAULT NULL COMMENT '填入mediaID',
  `price_type` tinyint(3) DEFAULT NULL COMMENT '价格类型，1：一口价，2：价格区间，3：显示折扣价',
  `price` decimal(10,2) DEFAULT NULL COMMENT '直播商品价格左边界',
  `price2` decimal(10,2) DEFAULT NULL COMMENT '直播商品价格右边界',
  `url` varchar(255) DEFAULT NULL COMMENT '商品详情页的小程序路径',
  `stock` bigint(10) DEFAULT NULL COMMENT '库存',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `goods_info_id` varchar(255) DEFAULT NULL COMMENT '商品详情id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺标识',
  `submit_time` datetime DEFAULT NULL COMMENT '提交审核时间',
  `audit_id` bigint(20) DEFAULT NULL COMMENT '审核单ID',
  `audit_status` tinyint(4) NOT NULL COMMENT '审核状态,0：代提审 1:待审核 2：审核通过 3：审核失败',
  `audit_reason` varchar(255) DEFAULT NULL COMMENT '审核原因',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(255) DEFAULT NULL COMMENT '删除人',
  `del_flag` tinyint(2) NOT NULL COMMENT '删除标记 0:未删除1:已删除',
  `third_party_tag` tinyint(2) DEFAULT NULL COMMENT '1,2：表示是为api添加商品，否则是在MP添加商品',
  `create_person` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_person` varchar(255) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `product_id_UNIQUE` (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=903 DEFAULT CHARSET=utf8 COMMENT='SPU表';

-- 小程序直播间表

CREATE TABLE `sbc-customer`.`live_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `room_id` bigint(20) DEFAULT NULL COMMENT '直播房间id',
  `name` varchar(255) DEFAULT NULL COMMENT '直播房间名',
  `recommend` tinyint(4) DEFAULT NULL COMMENT '是否推荐0不推荐 1推荐',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `anchor_name` varchar(255) DEFAULT NULL COMMENT '主播昵称',
  `anchor_wechat` varchar(255) DEFAULT NULL COMMENT '主播微信',
  `cover_img` varchar(255) DEFAULT NULL COMMENT '直播背景墙',
  `share_img` varchar(255) DEFAULT NULL COMMENT '分享卡片封面',
  `live_status` tinyint(7) DEFAULT NULL COMMENT '直播状态 0: 直播中,1: 暂停中, 2: 异常,3: 未开始, 4: 已结束, 5: 禁播, , 6: 已过期',
  `type` tinyint(4) DEFAULT NULL COMMENT '直播类型，1：推流，0：手机直播',
  `screen_type` tinyint(4) DEFAULT NULL COMMENT '1：横屏，0：竖屏',
  `close_like` tinyint(4) DEFAULT NULL COMMENT '1：关闭点赞 0：开启点赞，关闭后无法开启',
  `close_goods` tinyint(255) DEFAULT NULL COMMENT '1：关闭货架 0：打开货架，关闭后无法开启',
  `close_comment` tinyint(255) DEFAULT NULL COMMENT '1：关闭评论 0：打开评论，关闭后无法开启',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺id',
  `live_company_id` varchar(255) DEFAULT NULL COMMENT '直播商户id',
  `create_person` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(255) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `delete_person` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8mb4;

-- 直播间和直播商品中间表

CREATE TABLE `sbc-goods`.`live_room_live_goods_rel` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `room_id` bigint(20) NOT NULL COMMENT '直播房间id',
  `goods_id` bigint(20) NOT NULL COMMENT '直播商品id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识,0:未删除1:已删除',
  `create_person` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_person` varchar(255) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7451 DEFAULT CHARSET=utf8 COMMENT='直播房间和直播商品关联表';

-- 直播商家表

CREATE TABLE `sbc-customer`.`live_company` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `submit_time` datetime DEFAULT NULL COMMENT '提交审核时间',
  `live_broadcast_status` tinyint(5) DEFAULT NULL COMMENT '直播状态 0未开通，1待审核，2已开通，3审核未通过，4禁用中',
  `audit_reason` varchar(255) DEFAULT NULL COMMENT '直播审核原因',
  `create_person` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_person` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_person` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `del_flag` tinyint(2) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
  `company_info_id` bigint(20) DEFAULT NULL COMMENT '公司信息ID',
  `store_id` bigint(20) DEFAULT NULL COMMENT '店铺id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `store_id` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1787 DEFAULT CHARSET=utf8mb4 COMMENT='公司信息';

-- 直播回放表

CREATE TABLE `sbc-customer`.`live_room_replay` (
   `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `expire_time` datetime DEFAULT NULL COMMENT '视频过期时间',
  `create_time` datetime DEFAULT NULL COMMENT '视频创建时间',
  `media_url` varchar(255) DEFAULT NULL COMMENT '视频回放路径',
  `room_id` bigint(11) DEFAULT NULL COMMENT '直播房间id',
  `create_person` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(255) DEFAULT NULL COMMENT '修改人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_person` varchar(255) DEFAULT NULL COMMENT '删除人',
  `del_flag` tinyint(2) DEFAULT NULL COMMENT '删除逻辑 0：未删除 1 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4;

-- 定时任务xxl-job
INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`)
 VALUES (79, 1, '0 */1 * * * ?', '小程序直播房间任务调度', '2020-07-01 17:57:02', '2020-07-01 17:57:02', '朱文斌', '', 'FIRST', 'LiveRoomJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-07-01 17:57:02', '');

INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`)
 VALUES (80, 1, '0 */1 * * * ?', '小程序直播商品任务调度', '2020-07-01 17:57:33', '2020-07-01 17:57:33', '朱文斌', '', 'FIRST', 'LiveGoodsJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-07-01 17:57:33', '');

INSERT INTO `xxl-job`.`xxl_job_qrtz_trigger_info`(`id`, `job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`)
 VALUES (81, 1, '0 */1 * * * ?', '小程序直播回放调度', '2020-07-01 17:58:13', '2020-07-01 17:58:13', '朱文斌', '', 'FIRST', 'LiveReplayJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2020-07-01 17:58:13', '');


 -- 权限配置 menu_info 表
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322db2a3e0022', 4, 'ff808081730fdc4b0173142000790011', 3, '小程序直播', '/live', NULL, 1, '2020-07-06 14:40:55', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173142000790011', 4, 'fc8e20ff3fe311e9828800163e0fc468', 2, '小程序直播', '', NULL, 10, '2020-07-03 18:01:48', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173140c2e54000b', 3, 'ff808081730fdc4b017313e812510001', 3, '创建直播', '/live-add', NULL, 2, '2020-07-03 17:40:09', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017313f994b30006', 3, 'ff808081730fdc4b017313e812510001', 3, '直播商品', '/live-goods-add', NULL, 1, '2020-07-03 17:19:50', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017313ed6b680002', 3, 'ff808081730fdc4b017313e812510001', 3, '小程序直播', '/live-room/0', NULL, 0, '2020-07-03 17:06:33', 0);
INSERT INTO `sbc-setting`.`menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017313e812510001', 3, 'fc8e11003fe311e9828800163e0fc468', 2, '小程序直播', '', NULL, 6, '2020-07-03 17:00:43', 0);

-- function_info 表

INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808173244c480173270b7b200007', 3, 'ff808081730fdc4b017313ed6b680002', '直播申请', 'f_live_add', NULL, 4, '2020-07-07 10:12:11', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff80808173244c48017326de9af50002', 3, 'ff808081730fdc4b017313f994b30006', '商品详情查看', 'f_live_goods_detail', NULL, 1, '2020-07-07 09:23:10', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01732435d88b004c', 4, 'ff808081730fdc4b017324348717004b', '直播间详情查看', 'f_live_details', NULL, 0, '2020-07-06 20:59:35', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017323d4c8950046', 3, 'ff808081730fdc4b017313ed6b680002', '查看直播详情', 'f_live_detail', NULL, 4, '2020-07-06 19:13:34', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017323d2f1aa0044', 3, 'ff808081730fdc4b017313ed6b680002', '直播商家列表', 'f_live_company', NULL, 2, '2020-07-06 19:11:34', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173233b67ad0042', 3, 'ff808081730fdc4b017313ed6b680002', '直播商品库', 'f_live_good', NULL, 1, '2020-07-06 16:26:03', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01732324f6aa003f', 4, 'ff808081730fdc4b017322db2a3e0022', '直播商家审核/驳回', 'f_live_company_audit', NULL, 10, '2020-07-06 16:01:32', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173230a1671003c', 4, 'ff808081730fdc4b017322db2a3e0022', '直播商品提审/驳回', 'live_goods_aduit', NULL, 7, '2020-07-06 15:32:10', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01732302de5e003a', 4, 'ff808081730fdc4b017322db2a3e0022', '直播推荐', 'f_live_recommend', NULL, 3, '2020-07-06 15:24:17', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322fde8520035', 4, 'ff808081730fdc4b017322db2a3e0022', '直播开关编辑', 'f_live_open', NULL, 0, '2020-07-06 15:18:52', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322e415eb0032', 4, 'ff808081730fdc4b017322db2a3e0022', '直播商家列表查看', 'f_live__company', NULL, 8, '2020-07-06 14:50:40', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322e0d75b002d', 4, 'ff808081730fdc4b017322db2a3e0022', '直播商品库列表查看', 'f_live_good', NULL, 5, '2020-07-06 14:47:07', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322dc391a0026', 4, 'ff808081730fdc4b017322db2a3e0022', '直播列表查看', 'f_live', NULL, 1, '2020-07-06 14:42:05', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173140ca123000c', 3, 'ff808081730fdc4b0173140c2e54000b', '创建直播', 'f_live_room', NULL, 0, '2020-07-03 17:40:39', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017313fc9eb90007', 3, 'ff808081730fdc4b017313f994b30006', '直播商品', 'f_live_goods', NULL, 1, '2020-07-03 17:23:10', 0);
INSERT INTO `sbc-setting`.`function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017313eef6cf0003', 3, 'ff808081730fdc4b017313ed6b680002', '直播列表查看', 'f_live_list', NULL, 0, '2020-07-03 17:08:15', 0);

--authority表：
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173241b31b70048', 3, 'ff808081730fdc4b017313eef6cf0003', '直播功能是否开通', NULL, '/livecompany/*', 'GET', NULL, 4, '2020-07-06 20:30:29', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017323d5a01c0047', 3, 'ff808081730fdc4b017323d4c8950046', '查看直播详情接口', NULL, '/liveroom/*', 'GET', NULL, 0, '2020-07-06 19:14:30', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017323d38eca0045', 3, 'ff808081730fdc4b017323d2f1aa0044', '直播商家接口查询', NULL, '/livecompany', 'GET', NULL, 0, '2020-07-06 19:12:14', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173236fa0120043', 3, 'ff808081730fdc4b0173233b67ad0042', '直播商品库查询接口', NULL, '/livegoods/page', 'POST', NULL, 2, '2020-07-06 17:23:05', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01732325f9810040', 4, 'ff808081730fdc4b01732324f6aa003f', '直播商家审核/驳回', NULL, '/livecompany/modify', 'POST', NULL, 0, '2020-07-06 16:02:38', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173230bfce6003e', 4, 'ff808081730fdc4b0173230a1671003c', '直播商品驳回', NULL, '/livegoods/status', 'PUT', NULL, 1, '2020-07-06 15:34:15', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173230b57af003d', 4, 'ff808081730fdc4b0173230a1671003c', '商品提审接口', NULL, '/livegoods/audit', 'POST', NULL, 0, '2020-07-06 15:33:33', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01732305f1f7003b', 4, 'ff808081730fdc4b01732302de5e003a', '直播间是否推荐', NULL, '/liveroom/recommend', 'PUT', NULL, 1, '2020-07-06 15:27:39', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322ff70f50037', 4, 'ff808081730fdc4b017322fde8520035', '小程序直播开启状态接口', '', '/sysconfig/isOpen', 'GET', NULL, 1, '2020-07-06 15:20:33', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322fec71b0036', 4, 'ff808081730fdc4b017322fde8520035', '小程序直播开关接口', NULL, '/sysconfig/update', 'POST', NULL, 0, '2020-07-06 15:19:49', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322e53de50034', 4, 'ff808081730fdc4b017322e415eb0032', '直播商家查询接口', NULL, '/livecompany/page', 'POST', NULL, 1, '2020-07-06 14:51:56', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322e29ef9002f', 4, 'ff808081730fdc4b017322e0d75b002d', '直播商品查询', NULL, '/livegoods/page', 'POST', NULL, 1, '2020-07-06 14:49:04', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322dcf1000027', 4, 'ff808081730fdc4b017322dc391a0026', '直播间查询接口', NULL, '/liveroom/page', 'POST', NULL, 0, '2020-07-06 14:42:52', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322c792a90021', 4, 'ff808081730fdc4b017314275d250015', '小程序直播状态接口', NULL, '/sysconfig/isOpen', 'POST', NULL, 5, '2020-07-06 14:19:31', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017322c6ee740020', 4, 'ff808081730fdc4b017314275d250015', '小程序直播开启接口', NULL, '/sysconfig/update', 'POST', NULL, 4, '2020-07-06 14:18:49', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017314526215001f', 4, 'ff808081730fdc4b01731429448b0017', '直播商家查询接口', NULL, '/livecompany/page', 'POST', NULL, 1, '2020-07-03 18:56:50', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01731451e92a001e', 4, 'ff808081730fdc4b01731429448b0017', '直播商家审核', NULL, '/livecompany/modify', 'PUT', NULL, 0, '2020-07-03 18:56:19', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173144f7c3f001d', 4, 'ff808081730fdc4b01731428d9240016', '直播商品审核接口', NULL, '/livegoods/status', 'PUT', NULL, 2, '2020-07-03 18:53:40', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173144d9895001c', 4, 'ff808081730fdc4b01731428d9240016', '直播商品查询', NULL, '/livegoods/page', 'POST', NULL, 1, '2020-07-03 18:51:36', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173144cd90a001b', 4, 'ff808081730fdc4b01731428d9240016', '商品审核接口', NULL, '/livegoods/audit', 'POST', NULL, 0, '2020-07-03 18:50:47', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173144a728f001a', 4, 'ff808081730fdc4b017314275d250015', '直播间详情接口', NULL, '/liveroom', 'GET', NULL, 2, '2020-07-03 18:48:10', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173144809af0019', 4, 'ff808081730fdc4b017314275d250015', '直播间是否推荐接口', NULL, '/liveroom/recommend', 'PUT', NULL, 1, '2020-07-03 18:45:32', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173142488830014', 4, 'ff808081730fdc4b017314231aaf0012', '小程序直播开启状态接口', NULL, '/sysconfig/isOpen', 'POST', NULL, 1, '2020-07-03 18:06:45', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01731423c5c10013', 4, 'ff808081730fdc4b017314231aaf0012', '小程序直播开启接口', NULL, '/sysconfig/update', 'POST', NULL, 0, '2020-07-03 18:05:56', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01731418c12a0010', 3, 'ff808081730fdc4b017313fc9eb90007', '直播商品删除接口', NULL, '/livegoods/delete', 'DELETE', NULL, 2, '2020-07-03 17:53:53', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01731417364c000f', 3, 'ff808081730fdc4b017313eef6cf0003', '直播间导入直播商品接口', NULL, '/livegoods/add', 'POST', NULL, 4, '2020-07-03 17:52:12', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b0173140e36cf000d', 3, 'ff808081730fdc4b0173140ca123000c', '创建直播间接口', NULL, '/liveroom/add', 'POST', NULL, 0, '2020-07-03 17:42:23', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b01731401a5460009', 3, 'ff808081730fdc4b017313fc9eb90007', '直播商品库查询接口', NULL, '/goods/skus', 'POST', NULL, 1, '2020-07-03 17:28:39', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017313fd24980008', 3, 'ff808081730fdc4b017313fc9eb90007', '添加直播商品审核接口', NULL, '/livegoods/supplier', 'POST', NULL, 0, '2020-07-03 17:23:44', 0);
INSERT INTO `sbc-setting`.`authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff808081730fdc4b017313efb1f00004', 3, 'ff808081730fdc4b017313eef6cf0003', '直播列表查询接口', NULL, '/liveroom/page', 'POST', NULL, 0, '2020-07-03 17:09:03', 0);

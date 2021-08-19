-- ***************************************** xxl-job begin *********************************************
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 55 * * * ? ', '秒杀抢购-预热秒杀商品库存到redis中', '2019-06-24 16:19:42', '2019-06-24 16:20:57', '闵晨', '', 'FIRST', 'flashSalePreheatStockJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-06-24 16:19:42', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 5 * * * ?', '秒杀抢购-活动结束后秒杀商品还库存', '2019-06-24 16:20:40', '2019-06-24 16:21:04', '闵晨', '', 'FIRST', 'flashSaleReturnStockJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-06-24 16:20:40', '');
INSERT INTO `xxl_job_qrtz_trigger_info`(`job_group`, `job_cron`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, '0 0 1 * * ?', '积分兑换券兑换活动到期自动关闭', '2019-07-01 22:07:46', '2019-07-01 22:07:46', '李杨洋', '', 'FIRST', 'pointsCouponJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2019-07-01 22:07:46', '');


-- ***************************************** xxl-job end *********************************************





-- ***************************************** pay begin *********************************************
-- ***************************************** pay end *********************************************





-- ***************************************** setting begin*********************************************
-- 秒杀设置表
CREATE TABLE `flash_sale_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀设置主键',
  `time` varchar(32) NOT NULL COMMENT '每日场次整点时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '是否启用 0：停用，1：启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0: 未删除 1: 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀设置表';

-- 初始化数据
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '00:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '01:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '02:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '03:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '04:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '05:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '06:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '07:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '08:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '09:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '10:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '11:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '12:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '13:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '14:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '15:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '16:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '17:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '18:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '19:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '20:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '21:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '22:00', '0', '2019-06-11 14:48:22', null, null, null, '0');
insert into `flash_sale_setting` ( `time`, `status`, `create_time`, `create_person`, `update_time`, `update_person`, `del_flag`) values ( '23:00', '0', '2019-06-11 14:48:22', null, null, null, '0');

-- 会员权益迭代遗漏的权限配置，线上环境已添加
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b44ac7f016b54e912f60000', 4, 'fc925bda3fe311e9828800163e0fc468', '列表查询开启会员权益', NULL, '/customer/customerLevelRights/valid/list', 'GET', NULL, 2, '2019-06-14 15:34:49', 0);

-- boss后台整点秒杀
insert into `menu_info` ( `menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values ( '2c9386a86b6d7552016b6d7c0c2f0000', '4', 'fc8e20ff3fe311e9828800163e0fc468', '2', '抢购活动', null, null, '4', '2019-06-19 10:06:15', '0');
insert into `menu_info` ( `menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values ( '2c9386a86b6d7552016b6d7d8fa20001', '4', '2c9386a86b6d7552016b6d7c0c2f0000', '3', '整点秒杀', '/flash-sale', null, '1', '2019-06-19 10:07:54', '0');

insert into `function_info` ( `function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbba7611e0000', '4', '2c9386a86b6d7552016b6d7d8fa20001', '秒杀设置查看', 'f_flash_sale_setting_list', null, '1', '2019-07-04 14:23:57', '0');
insert into `function_info` ( `function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbba86c340001', '4', '2c9386a86b6d7552016b6d7d8fa20001', '秒杀设置编辑', 'f_flash_sale_setting_edit', null, '2', '2019-07-04 14:25:06', '0');
insert into `function_info` ( `function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbaaf3d30002', '4', '2c9386a86b6d7552016b6d7d8fa20001', '秒杀分类查看', 'f_flash_sale_cate_list', null, '3', '2019-07-04 14:27:51', '0');
insert into `function_info` ( `function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbab602b0003', '4', '2c9386a86b6d7552016b6d7d8fa20001', '秒杀分类编辑', 'f_flash_sale_cate_edit', null, '4', '2019-07-04 14:28:19', '0');
insert into `function_info` ( `function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbabf1ce0004', '4', '2c9386a86b6d7552016b6d7d8fa20001', '秒杀分类删除', 'f_flash_sale_cate_delete', null, '5', '2019-07-04 14:28:56', '0');
insert into `function_info` ( `function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbac66320005', '4', '2c9386a86b6d7552016b6d7d8fa20001', '秒杀分类新增', 'f_flash_sale_cate_add', null, '6', '2019-07-04 14:29:26', '0');
insert into `function_info` ( `function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbad1d990006', '4', '2c9386a86b6d7552016b6d7d8fa20001', '秒杀活动查看', 'f_flash_sale_activity_list', null, '7', '2019-07-04 14:30:13', '0');

insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( '8a9bc76c6bbd0080016bbd4733040000', '4', 'ff8080816bba8fcf016bbba7611e0000', '秒杀活动详情', null, '/flashsaleactivity/detail', 'POST', null, '3', '2019-07-04 21:58:08', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbf93b510019', '4', 'ff8080816bba8fcf016bbba86c340001', '秒杀分类拖拽排序', null, '/flashsalecate/editSort', 'PUT', null, '2', '2019-07-04 15:53:21', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbf816f60018', '4', 'ff8080816bba8fcf016bbba7611e0000', '参与商家数量查看', null, '/flashsalesetting/storeCount', 'POST', null, '2', '2019-07-04 15:52:07', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbf606ab0017', '4', 'ff8080816bba8fcf016bbbad1d990006', '已结束活动列表查看', null, '/flashsaleactivity/endlist', 'POST', null, '3', '2019-07-04 15:49:51', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbf598e10016', '4', 'ff8080816bba8fcf016bbbad1d990006', '进行中活动列表查看', null, '/flashsaleactivity/salelist', 'POST', null, '2', '2019-07-04 15:49:23', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbf2b7dc0015', '4', 'ff8080816bba8fcf016bbbad1d990006', '即将开场活动列表查看', null, '/flashsaleactivity/soonlist', 'POST', null, '1', '2019-07-04 15:46:15', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbe7bb5a0014', '4', 'ff8080816bba8fcf016bbbac66320005', '秒杀分类新增', null, '/flashsalecate/add', 'POST', null, '1', '2019-07-04 15:34:15', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbe72dba0013', '4', 'ff8080816bba8fcf016bbbabf1ce0004', '秒杀分类删除', null, '/flashsalecate/*', 'DELETE', null, '1', '2019-07-04 15:33:38', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbe64b750012', '4', 'ff8080816bba8fcf016bbbab602b0003', '秒杀分类编辑', null, '/flashsalecate/modify', 'PUT', null, '1', '2019-07-04 15:32:40', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbc930c90011', '4', 'ff8080816bba8fcf016bbbaaf3d30002', '秒杀分类查看', null, '/flashsalecate/list', 'POST', null, '1', '2019-07-04 15:00:53', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbc3214a0010', '4', 'ff8080816bba8fcf016bbba86c340001', '秒杀设置编辑', null, '/flashsalesetting/modifyList', 'PUT', null, '1', '2019-07-04 14:54:16', '0');
insert into `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ( 'ff8080816bba8fcf016bbbc13fdd000f', '4', 'ff8080816bba8fcf016bbba7611e0000', '秒杀设置查看', null, '/flashsalesetting/list', 'POST', null, '1', '2019-07-04 14:52:13', '0');
INSERT INTO `authority` ( `authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values ('8a9bc76c6bbd0080016bd04466160001', 3, 'ff8080816bba8fcf016bbbbd8945000d', '积分商品分类列表', null, '/flashsalecate/list', 'POST', null, 2, '2019-07-08 14:27:52', 0);


-- boss后台会员导入
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b799da3016b88f746d40000', 4, 'fc8df6013fe311e9828800163e0fc468', 3, '客户导入', '/customer-import', NULL, 125, '2019-06-24 18:10:35', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb0acce640000', 4, 'ff8080816b799da3016b88f746d40000', '批量导入', 'f_customer_import', NULL, 1, '2019-07-02 11:14:03', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb0aeac5f0001', 4, 'ff8080816bb03f76016bb0acce640000', '上传客户导入文件', NULL, '/customer/customerImport/excel/upload', 'POST', NULL, 1, '2019-07-02 11:16:06', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb0afb50a0002', 4, 'ff8080816bb03f76016bb0acce640000', '下载客户导入模板', NULL, '/customer/customerImport/downloadTemplate/*', 'GET', NULL, 2, '2019-07-02 11:17:14', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb0b0f6080003', 4, 'ff8080816bb03f76016bb0acce640000', '下载错误文档', NULL, '/customer/customerImport/excel/err/*/*', 'GET', NULL, 3, '2019-07-02 11:18:36', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb0b1fa380004', 4, 'ff8080816bb03f76016bb0acce640000', '确认导入', NULL, '/customer/customerImport/import/*/*', 'GET', NULL, 4, '2019-07-02 11:19:42', 0);



-- 商家端秒杀活动列表菜单
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b919166420001', 3, 'fc8e11003fe311e9828800163e0fc468', 2, '抢购活动', NULL, NULL, 4, '2019-06-26 10:15:54', 0);
INSERT INTO `menu_info`(`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b9192a5400002', 3, 'ff8080816b915d6c016b919166420001', 3, '整点秒杀', '/flash-sale-list', NULL, 0, '2019-06-26 10:17:15', 0);

INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b9199f24a0003', 3, 'ff8080816b915d6c016b9192a5400002', '即将开始的秒杀活动列表', 'f_soonList', NULL, 0, '2019-06-26 10:25:14', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b919aa31e0004', 3, 'ff8080816b915d6c016b9192a5400002', '进行中的秒杀活动列表', 'f_saleList', NULL, 1, '2019-06-26 10:25:59', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b919b3db90005', 3, 'ff8080816b915d6c016b9192a5400002', '已结束的秒杀活动列表', 'f_saleEndList', NULL, 2, '2019-06-26 10:26:39', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb159e8260005', 4, '8ab369076abe297e016abe6441620000', '积分商品状态编辑', 'f_points_goods_status_modify', null, 9, '2019-07-02 14:23:08', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb190ea7a000b', 4, '8ab369076abe297e016abe6441620000', '积分兑换券状态编辑', 'f_points_coupon_status_modify', null, 14, '2019-07-02 15:23:13', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb18f837a000a', 4, '8ab369076abe297e016abe6441620000', '积分兑换券编辑', 'f_points_coupon_modify', null, 13, '2019-07-02 15:21:41', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb18f18460009', 4, '8ab369076abe297e016abe6441620000', '积分兑换券删除', 'f_points_coupon_del', null, 12, '2019-07-02 15:21:14', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb18d2cfc0008', 4, '8ab369076abe297e016abe6441620000', '积分兑换券列表查看', 'f_points_coupon_list', null, 11, '2019-07-02 15:19:08', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb17cce980007', 4, '8ab369076abe297e016abe6441620000', '积分兑换券添加', 'f_points_coupon_add', null, 10, '2019-07-02 15:01:15', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbbd8945000d', 3, 'ff8080816b915d6c016b9192a5400002', '秒杀商品添加', 'f_flash_sale_goods_add', null, 7, '2019-07-04 14:48:09', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbb7e361000b', 3, 'ff8080816b915d6c016b9192a5400002', '秒杀商品删除', 'f_flash_sale_goods_del', null, 6, '2019-07-04 14:41:59', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbb5def60009', 3, 'ff8080816b915d6c016b9192a5400002', '秒杀商品编辑', 'f_flash_sale_goods_modify', null, 5, '2019-07-04 14:39:47', 0);
INSERT INTO `function_info`(`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbae54e80007', 3, 'ff8080816b915d6c016b9192a5400002', '秒杀商品列表', 'f_flash_sale_goods_list', null, 4, '2019-07-04 14:31:33', 0);

INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b919c5e0f0006', 3, 'ff8080816b915d6c016b9199f24a0003', '即将开始的秒杀活动列表', NULL, '/flashsaleactivity/soonlist', 'POST', NULL, 0, '2019-06-26 10:27:52', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b919cdb540007', 3, 'ff8080816b915d6c016b919aa31e0004', '进行中的秒杀活动列表', NULL, '/flashsaleactivity/salelist', 'POST', NULL, 1, '2019-06-26 10:28:25', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816b915d6c016b919d7e8c0008', 3, 'ff8080816b915d6c016b919b3db90005', '已结束的秒杀活动列表', NULL, '/flashsaleactivity/endlist', 'POST', NULL, 2, '2019-06-26 10:29:06', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb15de52a0006', 4, 'ff8080816bb03f76016bb159e8260005', '积分商品状态编辑', null, '/pointsgoods/modifyStatus', 'PUT', null, 1, '2019-07-02 14:27:29', 0);
UPDATE `authority` SET del_flag = 1 WHERE authority_id = '8ab369076b15c29e016b1cfd4bfc000f';
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb1a221560011', 4, 'ff8080816bb03f76016bb190ea7a000b', '积分兑换券状态编辑', null, '/pointscoupon/modifyStatus', 'PUT', null, 1, '2019-07-02 15:42:01', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb1a13f700010', 4, 'ff8080816bb03f76016bb18f837a000a', '积分兑换券编辑', null, '/pointscoupon/modify', 'PUT', null, 1, '2019-07-02 15:41:03', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb1a0a73d000f', 4, 'ff8080816bb03f76016bb18f18460009', '积分兑换券删除', null, '/pointscoupon/*', 'DELETE', null, 1, '2019-07-02 15:40:24', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb19f51aa000e', 4, 'ff8080816bb03f76016bb18d2cfc0008', '积分兑换券列表查看', null, '/pointscoupon/page', 'POST', null, 1, '2019-07-02 15:38:57', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb19e0a09000d', 4, 'ff8080816bb03f76016bb17cce980007', '积分兑换券获取优惠券列表', null, '/coupon-info/page', 'POST', null, 2, '2019-07-02 15:37:33', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bb03f76016bb19d05da000c', 4, 'ff8080816bb03f76016bb17cce980007', '积分兑换券添加', null, '/pointscoupon/batchAdd', 'POST', null, 1, '2019-07-02 15:36:26', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbbe2387000e', 3, 'ff8080816bba8fcf016bbbbd8945000d', '秒杀商品增加', null, '/flashsalegoods/batchAdd', 'POST', null, 1, '2019-07-04 14:48:49', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbb8a515000c', 3, 'ff8080816bba8fcf016bbbb7e361000b', '秒杀商品删除', null, '/flashsalegoods/*', 'DELETE', null, 1, '2019-07-04 14:42:49', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbb70c6b000a', 3, 'ff8080816bba8fcf016bbbb5def60009', '秒杀商品编辑', null, '/flashsalegoods/modify', 'PUT', null, 1, '2019-07-04 14:41:04', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('ff8080816bba8fcf016bbbaef8600008', 3, 'ff8080816bba8fcf016bbbae54e80007', '秒杀商品列表', null, '/flashsalegoods/page', 'POST', null, 1, '2019-07-04 14:32:15', 0);




-- ***************************************** setting end*********************************************





-- ***************************************** customer begin *********************************************
-- ***************************************** customer end ***********************************************





-- ***************************************** account begin *********************************************
-- ***************************************** account end ***********************************************





-- ***************************************** goods begin *********************************************
-- 秒杀分类表
CREATE TABLE `flash_sale_cate` (
  `cate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀分类主键',
  `cate_name` varchar(45) NOT NULL COMMENT '分类名称',
  `sort` tinyint(10) DEFAULT '0' COMMENT '排序 默认0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0: 未删除 1: 已删除',
  PRIMARY KEY (`cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀分类表';

-- 秒杀商品
CREATE TABLE `flash_sale_goods` (
`id` bigint ( 20 ) NOT NULL AUTO_INCREMENT,
`activity_date` VARCHAR ( 10 ) DEFAULT NULL COMMENT '活动日期：2019-06-11',
`activity_time` VARCHAR ( 10 ) DEFAULT NULL COMMENT '活动时间：13:00',
`goods_info_id` VARCHAR ( 32 ) NOT NULL COMMENT 'skuID',
`goods_id` VARCHAR ( 32 ) NOT NULL COMMENT 'spuID',
`price` DECIMAL ( 10, 2 ) NOT NULL COMMENT '抢购价',
`stock` INT ( 8 ) NOT NULL COMMENT '抢购库存',
`sales_volume` bigint( 20 ) DEFAULT '0' COMMENT '抢购销量',
`cate_id` bigint( 20 ) DEFAULT NULL COMMENT '分类ID',
`max_num` INT ( 8 ) NOT NULL COMMENT '限购数量',
`min_num` INT ( 8 ) DEFAULT NULL COMMENT '起售数量',
`store_id` bigint(20) DEFAULT NULL COMMENT '商家ID',
`postage` TINYINT ( 4 ) DEFAULT NULL COMMENT '包邮标志，0：不包邮 1:包邮',
`del_flag` TINYINT ( 4 ) DEFAULT NULL COMMENT '删除标志，0:未删除 1:已删除',
`activity_full_time` datetime DEFAULT NULL COMMENT '活动日期+时间',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`create_person` VARCHAR ( 32 ) DEFAULT NULL COMMENT '创建人',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`update_person` VARCHAR ( 32 ) DEFAULT NULL COMMENT '更新人',
PRIMARY KEY ( `id` )
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '秒杀商品表';

-- ***************************************** goods end *********************************************





-- ***************************************** marketing begin *********************************************
-- 积分兑换券表
CREATE TABLE `points_coupon`
(
    `points_coupon_id` BIGINT(10) PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '积分兑换券id',
    `activity_id` VARCHAR(32) NOT NULL COMMENT '优惠券活动id',
    `coupon_id` VARCHAR(32) NOT NULL COMMENT '优惠券id',
    `total_count` BIGINT(10) DEFAULT 0 NOT NULL COMMENT '兑换总数',
    `exchange_count` BIGINT(10) DEFAULT 0 NOT NULL COMMENT '已兑换数量',
    `points` BIGINT(10) NOT NULL COMMENT '兑换积分',
    `sell_out_flag` tinyint(4) NOT NULL COMMENT '是否售罄 0：否，1：是',
    `status` TINYINT(4) NOT NULL COMMENT '是否启用 0：停用，1：启用',
    `begin_time` DATETIME NOT NULL COMMENT '兑换开始时间',
    `end_time` DATETIME NOT NULL COMMENT '兑换结束时间',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `create_person` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `update_person` varchar(32) DEFAULT NULL COMMENT '修改人',
    `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识,0: 未删除 1: 已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分兑换券表';
-- ***************************************** marketing end ***********************************************





-- ***************************************** order begin *********************************************
-- ***************************************** order end ***********************************************
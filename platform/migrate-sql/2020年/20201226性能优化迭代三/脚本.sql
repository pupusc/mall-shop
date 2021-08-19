-- 全部员工下拉框查询时加入索引
ALTER TABLE `sbc-customer`.`employee`
ADD INDEX `idx_account_type`(`is_employee`, `del_flag`, `account_type`) USING BTREE;

-- 商品SKU加入索引
ALTER TABLE `sbc-goods`.`goods_info`
ADD INDEX `goods_id`(`goods_id`),
ADD INDEX `del_flag`(`del_flag`),
ADD INDEX `idx_goods_info_no`(`goods_info_no`),
ADD INDEX `idx_provider_goods_info_id`(`provider_goods_info_id`),
ADD INDEX `idx_brand_id`(`brand_id`),
ADD INDEX `idx_third_platform_spu_id`(`third_platform_spu_id`),
ADD INDEX `idx_goods_source`(`goods_source`),
ADD INDEX `idx_enterprise_goods_audit`(`enterprise_goods_audit`),
ADD INDEX `idx_store_id`(`store_id`),
ADD INDEX `idx_update_time`(`update_time`),
ADD INDEX `idx_distribution_goods_audit`(`distribution_goods_audit`),
ADD INDEX `idx_sale_type`(`sale_type`),
ADD INDEX `idx_create_time`(`create_time`);

-- 商品规格值加入索引
ALTER TABLE `sbc-goods`.`goods_info_spec_detail_rel`
ADD INDEX `idx_goods_id`(`goods_id`),
ADD INDEX `del_flag`(`del_flag`);

-- goods和goods_info添加索引
ALTER TABLE `sbc-goods`.`goods`
ADD INDEX `idx_goods_source`(`goods_source`),
ADD INDEX `idx_audit_status`(`audit_status`),
ADD INDEX `idx_brand_id`(`brand_id`),
ADD INDEX `idx_goods_no`(`goods_no`),
ADD INDEX(`provider_goods_id`);

-- 商品店铺分类加入索引
ALTER TABLE `sbc-goods`.`store_cate_goods_rela`
ADD INDEX `idx_goods_id`(`goods_id`);

-- 商品区间价加入索引
ALTER TABLE `sbc-goods`.`goods_interval_price`
ADD INDEX `idx_goods_id_type`(`goods_id`, `type`);

-- 商品会员价加入索引
ALTER TABLE `sbc-goods`.`goods_customer_price`
DROP INDEX `idx_goods_id`,
ADD INDEX `idx_goods_id`(`goods_id`, `type`) USING BTREE;


ALTER TABLE `sbc-goods`.`standard_goods_rel`
ADD INDEX(`goods_id`, `del_flag`);

-- 优惠券加入索引
ALTER TABLE `sbc-marketing`.`coupon_info`
ADD INDEX `idx_create_time`(`create_time`) USING BTREE;

-- 优惠券活动加入索引
ALTER TABLE `sbc-marketing`.`coupon_activity`
ADD INDEX `idx_create_time`(`create_time`) ,
ADD INDEX `idx_start_time`(`start_time`) ,
ADD INDEX `idx_end_time`(`end_time`),
ADD INDEX `idx_activity_type`(`activity_type`) USING BTREE;

ALTER TABLE `sbc-goods`.`standard_sku`
ADD INDEX `idx_goods_id`(`goods_id`),
ADD INDEX `idx_third_platform_spu_id`(`third_platform_spu_id`),
ADD INDEX `idx_goods_source`(`goods_source`),
ADD INDEX `idx_del_flag`(`del_flag`);

ALTER TABLE `sbc-goods`.`standard_goods`
ADD INDEX `idx_goods_id`(`goods_id`),
ADD INDEX `idx_third_platform_spu_id`(`third_platform_spu_id`),
ADD INDEX `idx_goods_source`(`goods_source`),
ADD INDEX `idx_del_flag`(`del_flag`);

-- 隐藏C端银联企业支付选项
UPDATE `sbc-pay`.`pay_gateway` SET is_open = 0 WHERE name = 'UNIONB2B';

-- 修改编码
ALTER TABLE `sbc-goods`.`groupon_goods_info` COLLATE = utf8mb4_general_ci;

-- 将与供应商品关联的商家商品库存同步
update `sbc-goods`.`goods` a, `sbc-goods`.`goods` b set a.stock = b.stock where a.provider_goods_id = b.goods_id and a.goods_source = 1;

-- 添加缺失的定时任务
INSERT INTO `xxl-job`.xxl_job_qrtz_trigger_info (job_group, job_cron, job_desc, add_time, update_time, author,
                                                 alarm_email, executor_route_strategy, executor_handler, executor_param,
                                                 executor_block_strategy, executor_timeout, executor_fail_retry_count,
                                                 glue_type, glue_source, glue_remark, glue_updatetime, child_jobid)
VALUES (1, '0 0 1 * * ?', '结算-供应商结算', '2020-09-04 10:38:03', '2020-09-04 10:38:03', '关凤亮', '', 'FIRST',
        'ProviderSettlementAnalyseJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        '2020-09-04 10:38:03', '');

-- 员工列表权限改动
UPDATE `sbc-setting`.`authority` a
SET a.authority_url = '/customer/es/employees'
WHERE
  a.authority_id IN ( SELECT c.authority_id FROM ( SELECT b.authority_id FROM `sbc-setting`.`authority` b WHERE b.authority_url = '/customer/employees' ) c )
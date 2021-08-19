

-- 商家独立支付
-- 支付网关添加store_id

ALTER TABLE `sbc-pay`.`pay_gateway`
ADD COLUMN `store_id` bigint(20) NOT NULL DEFAULT -1 COMMENT '商户id (平台默认值-1)' AFTER `create_time`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `name`) USING BTREE;

-- 支付网关配置信息添加store_id
ALTER TABLE `sbc-pay`.`pay_gateway_config`
ADD COLUMN `store_id` bigint(20) NULL DEFAULT -1 COMMENT '商户id (平台默认值-1)' AFTER `wx_open_pay_certificate`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE;



-- pay_channel_item
ALTER TABLE `sbc-pay`.`pay_channel_item`
ADD COLUMN `gateway_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付渠道项类型-代替gateway_id和gateway做关联' AFTER `name`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE;
-- 初始化数据
UPDATE `sbc-pay`.`pay_channel_item` set  gateway_name ='PING' where gateway_id=1;
UPDATE `sbc-pay`.`pay_channel_item` set  gateway_name ='UNIONB2B' where gateway_id=2;
UPDATE `sbc-pay`.`pay_channel_item` set  gateway_name ='WECHAT' where gateway_id=3;
UPDATE `sbc-pay`.`pay_channel_item` set  gateway_name ='ALIPAY' where gateway_id=4;
UPDATE `sbc-pay`.`pay_channel_item` set  gateway_name ='BALANCE' where gateway_id=5;



-- 初始化脚本
-- 删除非boss端配置信息
DELETE FROM `sbc-pay`.`pay_gateway_config` where store_id != -1;
DELETE FROM `sbc-pay`.`pay_gateway` where store_id != -1;



-- 微信登录
ALTER TABLE `sbc-setting`.`wechat_login_set`
ADD COLUMN `store_id` bigint(20) NOT NULL DEFAULT -1 COMMENT '门店id 平台默认storeId=0' AFTER `app_server_status`,
ADD UNIQUE INDEX `store_id_uni`(`store_id`);


-- 微信分享
ALTER TABLE `sbc-setting`.`wechat_share_set`
ADD COLUMN `store_id` bigint(20) NOT NULL DEFAULT -1 COMMENT '门店id 平台默认storeId=0' AFTER `share_app_secret`,
ADD UNIQUE INDEX `store_id_uni`(`store_id`);

-- 更新门店小程序码
UPDATE `sbc-customer`.`store` set `small_program_code` = null;
ALTER TABLE `sbc-setting`.`wechat_share_set`
MODIFY COLUMN `share_app_id` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '微信公众号App ID' AFTER `share_set_id`,
MODIFY COLUMN `share_app_secret` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '微信公众号 App Secret' AFTER `share_app_id`;

-- 微信授权登录
ALTER TABLE `sbc-customer`.`third_login_relation`
ADD COLUMN `store_id` bigint(20) NOT NULL DEFAULT -1 COMMENT '判断店铺id' AFTER `third_login_uid`;


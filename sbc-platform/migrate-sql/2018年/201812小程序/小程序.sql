-- 还需要考虑菜单权限表的增加
ALTER TABLE `goods_info`
ADD COLUMN `small_program_code`  varchar(250) NULL COMMENT '商品详情小程序码' AFTER `alone_flag`;

ALTER TABLE `store`
ADD COLUMN `small_program_code`  varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺小程序码' AFTER `apply_enter_time`;


INSERT INTO `system_config` (config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag) VALUES 
('s2b_audit', 'applet_share_setting', '小程序分享配置', null, '1', '{"title":"小程序标题测试","imgUrl":[{"uid":"rc-upload-1504848759548-9","url":"https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/201709081335501812"}]}', '2019-01-02 17:19:45', '2019-01-02 17:19:47', '0');
INSERT INTO `system_config` (config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag) VALUES 
('small_program_setting', 'small_program_setting_customer', '小程序配置', null, '1', '{"appId":"wx6497fa5d6beeb834","appSecret":"a34eccd07416ea7979e0c83d540e80a0"}', '2019-01-02 17:19:45', '2019-01-02 17:19:47', '0');
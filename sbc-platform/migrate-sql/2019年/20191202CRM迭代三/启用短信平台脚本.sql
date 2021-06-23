-- sbc-sms   短信运营商设置，启用阿里云请执行对应的启用阿里云的脚本，如果启用华信请执行对应的启用华信的脚本

-- 启用阿里云
INSERT INTO `sbc-sms`.`sms_setting` (`id`, `access_key_id`, `access_key_secret`, `type`, `status`, `del_flag`, `creat_time`) VALUES ('1', 'LTAI4FgM6vgMcg73ipewuVZe', 'lITXF1xaCK8ubtd3rTdNvH5I3b5dqm', '0', '1', '0', '2019-12-03 16:16:17');
INSERT INTO `sbc-sms`.`sms_setting` (`id`, `access_key_id`, `access_key_secret`, `type`, `status`, `del_flag`, `creat_time`) VALUES ('2', 'huaxin', 'huaxin', '1', '0', '0', NULL);

-- 启用华信
INSERT INTO `sbc-sms`.`sms_setting` (`id`, `access_key_id`, `access_key_secret`, `type`, `status`, `del_flag`, `creat_time`) VALUES ('1', 'LTAI4FgM6vgMcg73ipewuVZe', 'lITXF1xaCK8ubtd3rTdNvH5I3b5dqm', '0', '0', '0', '2019-12-03 16:16:17');
INSERT INTO `sbc-sms`.`sms_setting` (`id`, `access_key_id`, `access_key_secret`, `type`, `status`, `del_flag`, `creat_time`) VALUES ('2', 'huaxin', 'huaxin', '1', '1', '0', NULL);

-- ***************************************** setting begin*********************************************
--UPDATE system_config SET config_key = 'resource_server' WHERE config_key = 'image_server';
INSERT INTO system_config ( config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag) VALUES ( 'resource_server', 'hwYun', '华为云', null, 0, '{"accessKeyId":"SGPKCMX0ZTGWPQHFE3IR","accessKeySecret":"MpqzyMpLyeJLT4iuWy6lu6kc5kiVGSYeDMbrjVpm","bucketName":"wanmi","endPoint":"obs.cn-east-2.myhuaweicloud.com"}', now(), now(), 0);
INSERT INTO system_config ( config_key, config_type, config_name, remark, status, context, create_time, update_time, del_flag) VALUES ( 'resource_server', 'txYun', '腾讯云', null, 0, '{}', now(), now(), 0);

update system_resource set artwork_url = substring_index(artwork_url, '/', -1);
update store_resource set artwork_url = substring_index(artwork_url, '/', -1);
-- ***************************************** setting end*********************************************

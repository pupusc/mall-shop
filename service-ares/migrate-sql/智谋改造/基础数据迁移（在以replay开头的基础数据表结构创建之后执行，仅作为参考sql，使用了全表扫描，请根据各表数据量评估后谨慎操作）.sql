INSERT into `s2b_statistics`.replay_store_level SELECT * FROM `sbc-customer`.store_level;
INSERT into `s2b_statistics`.replay_goods_info_spec_detail_rel SELECT * FROM `sbc-goods`.goods_info_spec_detail_rel;
INSERT into `s2b_statistics`.replay_store SELECT * FROM `sbc-customer`.store;
INSERT into `s2b_statistics`.replay_store_cate SELECT * FROM `sbc-goods`.store_cate;
INSERT into `s2b_statistics`.replay_store_cate_goods_rela SELECT * FROM `sbc-goods`.store_cate_goods_rela;
INSERT into `s2b_statistics`.replay_store_customer_rela(id,customer_id,store_id,company_info_id,store_level_id,employee_id,customer_type) SELECT id,customer_id,store_id,company_info_id,store_level_id,employee_id,customer_type FROM `sbc-customer`.store_customer_rela;
INSERT INTO `s2b_statistics`.replay_customer_detail SELECT * FROM `sbc-customer`.customer_detail;    
INSERT INTO `s2b_statistics`.replay_customer SELECT * FROM `sbc-customer`.customer;    
INSERT INTO `s2b_statistics`.replay_customer_level SELECT * FROM `sbc-customer`.customer_level;   
INSERT INTO `s2b_statistics`.replay_employee SELECT * FROM `sbc-customer`.employee;   
-- replay_flow_day_user_info 流量埋点数据，业务表没有
INSERT INTO `s2b_statistics`.replay_goods_brand SELECT * FROM `sbc-goods`.`goods_brand`;      
INSERT INTO `s2b_statistics`.replay_goods_cate SELECT * FROM `sbc-goods`.goods_cate;    
INSERT INTO `s2b_statistics`.replay_goods_info SELECT * FROM `sbc-goods`.goods_info;
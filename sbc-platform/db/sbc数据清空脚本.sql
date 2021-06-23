#本文件用于记录各库交付客户的时候,可以清空的数据,尤其是敏感信息必须要清理掉(比如ossId,secret,微信appId,appSecret)

#20200324清理数据排除的表 - 田正鑫
#---库名:goods------
#goods_info_spec_detail_rel
#goods_spec_detail
#goods_spec
#goods_evaluate_image
#goods_evaluate
#goods_info
#goods

#------mongoDB------
#yzGoods
#yzImage

#20200324清理数据排除的表 - 贺家宾
#sbc-customer.yz_salesman_account
#sbc-customer.distribution_customer
#sbc-order.yz_salesman_customer
#sbc-order.consume_record


#20200324清理数据排除的表 - 魏文号
#sbc-customer.yz_customer_3
#sbc-customer.paid_card_customer_rel
#sbc-customer.order_card_customer
#sbc-customer.paid_card_rights_rel
#sbc-customer.paid_card
#sbc-customer.yz_customer_4
#sbc-bff.yz_customer_4

#20200324清理数据排除的表 - 许云鹏
#------mongoDB------
#Trade
#ProviderTrade
#yzOrder






#数据库：sbc-pay
#pay_channel_item;
#pay_gateway;
delete from pay_gateway_config;
INSERT INTO pay_gateway_config(id, gateway_id, create_time) VALUES (1, 1, now());
INSERT INTO pay_gateway_config(id, gateway_id, create_time) VALUES (2, 2, now());
INSERT INTO pay_gateway_config(id, gateway_id, create_time) VALUES (3, 3, now());
INSERT INTO pay_gateway_config(id, gateway_id, create_time) VALUES (4, 4, now());
INSERT INTO pay_gateway_config(id, gateway_id, create_time) VALUES (5, 5, now());
delete from pay_trade_record;


#数据库：sbc-setting
#authority;
#evaluate_ratio;
#function_info;
#menu_info;
delete from role_function_rela where role_info_id != 1;
delete from role_menu_rela where role_info_id != 1;
#sensitive_words;
delete from store_image;
#system_config;
UPDATE system_config SET context = '{\"appId\":\"\",\"appSecret\":\"\"}' WHERE config_type = 'small_program_setting_customer';
UPDATE system_config SET context = '{}' WHERE config_key = 'resource_server';
UPDATE system_config SET context = '{}' WHERE config_type = 'kuaidi100';
delete from system_email_config;
#system_growth_value_config
delete from system_image;
delete from system_ip_info;
delete from system_operation_log;
#system_switch;
truncate from third_address;
delete from platform_address where data_type = 1;

-- delete from base_config;
-- delete from business_config;
-- delete from company_info;
-- delete from express_company;
-- delete from online_service;
-- delete from online_service_item;
-- delete from store_express_company_rela;
-- delete from store_resource;
-- delete from store_resource_cate where is_default != 1;
-- delete from sys_sms;
-- delete from system_resource;
-- delete from system_resource_cate where is_default != 1;
-- delete from wechat_login_set;
-- delete from wechat_share_set;


#数据库：sbc-customer
delete from company_info where company_info_id != 0;
delete from customer;
delete from customer_account;
delete from customer_delivery_address;
delete from customer_detail;
delete from customer_email;
delete from customer_growth_value;
delete from customer_invoice;
delete from customer_level where is_defalt != 1;
delete from customer_level_rights;
delete from customer_level_rights_rel;
delete from customer_points_detail;
delete from distribution_customer;
delete from distribution_customer_invite_info;
delete from distribution_customer_ranking;
delete from distribution_performance_day;
delete from distribution_performance_month;
delete from live_room;
delete from live_company;
delete from live_room_company;

delete from employee where account_name != 'system';
delete from invite_new_record;
delete from role_info where role_info_id != 1;
delete from store;
delete from store_consumer_statistics;
delete from store_customer_follow;
delete from store_customer_rela;
delete from store_evaluate;
delete from store_evaluate_num;
delete from store_evaluate_sum;
delete from store_level;
delete from third_login_relation;
truncate table distributor_level;

#数据库：sbc-account
delete from company_account;
delete from customer_draw_cash;
delete from customer_funds;
delete from customer_funds_detail;
delete from invoice_project where project_id != '00000000000000000000000000000000'; 
delete from invoice_project_switch;
delete from offline_account;
delete from reconciliation;
delete from settlement;


#数据库：sbc-goods
delete from check_brand;
delete from contract_brand;
delete from contract_cate;
delete from customer_goods_evaluate_praise;
delete from distributor_goods_info;
delete from flash_sale_cate;
delete from flash_sale_goods;
delete from freight_template_goods;
delete from freight_template_goods_express;
delete from freight_template_goods_free;
delete from freight_template_store;
delete from goods;
delete from goods_brand;
delete from goods_cate;
delete from goods_customer_num;
delete from goods_customer_price;
delete from goods_evaluate;
delete from goods_evaluate_image;
delete from goods_image;
delete from goods_info;
delete from goods_info_spec_detail_rel; 
delete from goods_interval_price;
delete from goods_level_price;
delete from goods_prop;
delete from goods_prop_detail;
delete from goods_prop_detail_rel;
delete from goods_spec; 
delete from goods_spec_detail; 
delete from goods_tab_rela;
delete from goods_tobe_evaluate;
delete from points_goods;
delete from points_goods_cate;
delete from standard_goods;
delete from standard_goods_rel;
delete from standard_image;
delete from standard_prop_detail_rel;
delete from standard_sku;
delete from standard_sku_spec_detail_rel;
delete from standard_spec;
delete from standard_spec_detail;
delete from store_cate;
delete from store_cate_goods_rela;
delete from store_goods_tab;
delete from store_tobe_evaluate;
truncate table groupon_goods_info;
delete from distribution_goods_matter;
truncate table goods_restricted_sale;
truncate table goods_restricted_customer_rela;
truncate table restricted_record;

delete from live_goods;
delete from live_room_live_goods_rel;
truncate table goods_label;
truncate table third_goods_cate;
truncate table goods_cate_third_cate_rel;

#数据库：sbc-marketing
delete from coupon_activity;
delete from coupon_activity_config;
delete from coupon_cate;
delete from coupon_cate_rela;
delete from coupon_code;
delete from coupon_info;
delete from coupon_marketing_scope;
delete from distribution_record;
delete from distribution_recruit_gift;
delete from distribution_reward_coupon;
delete from distribution_setting;
delete from distribution_store_setting;
delete from marketing;
delete from marketing_full_discount_level;
delete from marketing_full_gift_detail;
delete from marketing_full_gift_level;
delete from marketing_full_reduction_level;
delete from marketing_scope;
truncate table groupon_setting;
truncate table groupon_activity;
truncate table groupon_cate;
truncate table groupon_record;
delete from points_coupon;
truncate table coupon_marketing_customer_scope;


-- 批量删除优惠券券码分表数据
drop procedure if exists del_voucher_code_sub_table;

delimiter $
create procedure del_voucher_code_sub_table(tableCount  int)
begin
  declare i int;
  DECLARE table_name VARCHAR(20);
  DECLARE table_pre VARCHAR(20);
  DECLARE sql_text VARCHAR(20000);
  set i = 0;
  SET table_pre = 'coupon_code_';
  while i < tableCount  do
    SET @table_name = CONCAT(table_pre,CONCAT(i, ''));
    SET sql_text = CONCAT('truncate table ', @table_name, '');
    SELECT sql_text;
    SET @sql_text=sql_text;
    PREPARE stmt FROM @sql_text;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    set i = i + 1;
  end while;
end $

call del_voucher_code_sub_table(5);



#数据库：sbc-order
delete from distribution_task_temp;
delete from consume_record;
delete from goods_customer_follow;
delete from order_growth_value_temp;
delete from order_invoice;
delete from pay_order;
delete from purchase;
delete from receivable;
delete from refund_bill;
delete from refund_order;



#数据库：sbc-bff
truncate table distribution_task_temp;
truncate table qrtz_locks;
truncate table qrtz_scheduler_state;
truncate table task_info;

#数据库：sbc-message
-- truncate table所有的app_message_? 动态生成的表
truncate table app_message_0;
truncate table message_send;
truncate table message_send_customer_scope;
update message_send_node set send_sum = 0, open_sum = 0;
truncate table push_customer_enable;
truncate table push_detail;
truncate table push_send;
update push_send_node set expected_send_count = 0, actually_send_count = 0, open_count = 0;
truncate table sms_send;
truncate table sms_send_detail;
truncate table sms_setting;
truncate table sms_sign;
truncate table sms_sign_file_info;
truncate table sms_template;
truncate table umeng_token;

#数据库：sbc-crm
truncate table custom_group;
truncate table custom_group_rel;
truncate table custom_group_statistics;
truncate table customer_base_info;
truncate table customer_plan;
truncate table customer_plan_app_push_rel;
truncate table customer_plan_conversion;
truncate table customer_plan_coupon_rel;
truncate table customer_plan_send;
truncate table customer_plan_send_count;
truncate table customer_plan_sms_rel;
truncate table customer_plan_trigger_send;
truncate table customer_recent_param_statistics;
truncate table customer_tag;
truncate table customer_tag_rel;
truncate table customer_trade_statistics;
truncate table plan_statistics_message;

truncate table rfm_customer_detail;
truncate table rfm_score_statistic;
truncate table rfm_setting_history;
truncate table rfm_system_group_statistics;


#收钱吧一阶段新增表
truncate table preset_search_terms;
truncate table search_associational_word;
truncate table association_long_tail_word;
truncate table popular_search_terms;
truncate table popup;
truncate table application_page;

truncate table marketing_buyout_price_level;
-- 组合套装
truncate table  marketing_suits;
truncate table  marketing_suits_sku;

#收钱吧二阶段标品
truncate table goods_restricted_sale;
truncate table goods_restricted_customer_rela;
truncate table restricted_record;

truncate table appointment_sale;
truncate table appointment_sale_goods;
truncate table booking_sale;
truncate table booking_sale_goods;
truncate table rule_info;

-- 二期标品第二件半价规则表
truncate table marketing_half_price_second_piece;



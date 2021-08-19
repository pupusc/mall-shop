-- 1、分销记录表新增分销佣金比例字段
alter table `sbc-marketing`.distribution_record add column commission_rate decimal(4,2) comment '分销佣金比例' after commission_goods;
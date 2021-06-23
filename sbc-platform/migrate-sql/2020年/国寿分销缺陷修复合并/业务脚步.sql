-- 消费记录表增加订单流转状态
ALTER TABLE `sbc-order`.`consume_record` ADD COLUMN `flow_state` VARCHAR(32) DEFAULT NULL COMMENT '订单流转状态';
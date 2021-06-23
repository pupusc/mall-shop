
-- 卡券表修改
ALTER TABLE `sbc-goods`.`virtual_coupon_code`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_0`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_0`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_0`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_0`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_1`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_1`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_1`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_1`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_2`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_2`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_2`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_2`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_3`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_3`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_3`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_3`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_4`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_4`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_4`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_4`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_5`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_5`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_5`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_5`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_6`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_6`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_6`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_6`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_7`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_7`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_7`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_7`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_8`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_8`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_8`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_8`
    DROP COLUMN `exchange_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_9`
    ADD COLUMN `exchange_limit_desc` varchar(55) DEFAULT NULL COMMENT '兑换有效期' AFTER `receive_end_time`;

ALTER TABLE `sbc-goods`.`virtual_coupon_code_9`
    MODIFY COLUMN `valid_days` varchar(55) DEFAULT NULL COMMENT '使用有效期';

ALTER TABLE `sbc-goods`.`virtual_coupon_code_9`
    DROP COLUMN `exchange_start_time`;
ALTER TABLE `sbc-goods`.`virtual_coupon_code_9`
    DROP COLUMN `exchange_end_time`;
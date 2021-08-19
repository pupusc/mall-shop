ALTER TABLE `sbc-setting`.`system_privacy_policy`
MODIFY COLUMN `privacy_policy` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '隐私政策' AFTER `privacy_policy_id`,
MODIFY COLUMN `privacy_policy_pop` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '隐私政策弹窗' AFTER `privacy_policy`;

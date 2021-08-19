-- 评价默认展示调整为评价默认不展示
ALTER TABLE `sbc-goods`.`goods_evaluate`
MODIFY COLUMN `is_show` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否展示 0：否，1：是' AFTER `is_edit`;
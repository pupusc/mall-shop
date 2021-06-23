-- 商品 新增字段
ALTER TABLE `sbc-goods`.`goods`
ADD COLUMN need_synchronize tinyint(4) DEFAULT NULL COMMENT '是否需要同步 0：不需要同步 1：需要同步',
ADD COLUMN delete_reason varchar(100) DEFAULT NULL COMMENT '删除原因',
ADD COLUMN add_false_reason varchar(100) DEFAULT NULL COMMENT '下架原因';

-- 商品库关联表新增字段
ALTER TABLE `sbc-goods`.`standard_goods_rel`
ADD COLUMN del_flag tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除';

-- 供应商商品库新增字段
ALTER TABLE `sbc-goods`.`standard_goods`
ADD COLUMN del_flag tinyint(4) NOT NULL COMMENT '删除标识,0:未删除1:已删除',
ADD COLUMN delete_reason varchar(100) DEFAULT NULL COMMENT '删除原因';

-- 操作日志
CREATE TABLE `sbc-setting`.`operate_data_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `operate_content` varchar(255) DEFAULT NULL COMMENT '操作内容',
  `operate_id` varchar(255) DEFAULT NULL COMMENT '操作标识',
  `operate_before_data` varchar(1000) DEFAULT NULL COMMENT '操作前数据',
  `operate_after_data` varchar(1000) DEFAULT NULL COMMENT '操作后数据',
  `operate_account` varchar(50) DEFAULT NULL COMMENT '操作人账号',
  `operate_name` varchar(128) DEFAULT NULL COMMENT '操作人名称',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `del_flag` smallint(4) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- 商品库关联表新增字段
ALTER TABLE `sbc-goods`.`standard_goods_rel`
ADD COLUMN need_synchronize tinyint(4) DEFAULT 0 COMMENT '是否需要同步 0：不需要同步 1：需要同步';

-- 新增字段
ALTER TABLE `sbc-goods`.goods_info ADD COLUMN vendibility tinyint(4) DEFAULT 1 COMMENT '是否可售，0不可售，1可售';
-- 新增字段
ALTER TABLE `sbc-goods`.goods ADD COLUMN vendibility tinyint(4) DEFAULT 1 COMMENT '是否可售，0不可售，1可售';
-- 热门搜索词表 新增 PC落地页 字段
ALTER TABLE `sbc-setting`.popular_search_terms ADD COLUMN `pc_landing_page` varchar(200) DEFAULT NULL COMMENT
'PC落地页' after `related_landing_page`;

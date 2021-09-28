-- sbc-setting 书友说 START
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d6d2180013', 3, '8a9bc76c6a673f39016a67d571e4000e', '添加书友说评价', NULL, '/goods/evaluate/bookFriend/add', 'POST', NULL, 30, '2021-09-01 18:44:53', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d6d2180014', 3, '8a9bc76c6a673f39016a67d571e4000e', '编辑书友说评价', NULL, '/goods/evaluate/bookFriend/edit', 'POST', NULL, 30, '2021-09-01 18:44:53', 0);
INSERT INTO `authority`(`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) VALUES ('8a9bc76c6a673f39016a67d6d2180015', 3, 'fc924c7c3fe311e9828800163e0fc468', '标签查询', NULL, '/goods/tags', 'GET', NULL, 30, '2021-09-01 18:44:53', 0);
-- sbc-setting 书友说 END

-- sbc-goods 商品上架 START
alter table goods_cate add column book_flag tinyint comment '是否书籍';

alter table goods_prop_detail_rel add column prop_value varchar(255) comment '属性值';

alter table goods_prop add column prop_type tinyint comment '属性类型';

INSERT INTO `sbc-goods`.`goods_prop`(`prop_name`, `index_flag`, `create_time`, `update_time`, `del_flag`, `sort`, `prop_type`) VALUES ('作者', 1, '2021-09-07 16:39:50', '2021-09-28 13:22:10', 0, 1, 3);
INSERT INTO `sbc-goods`.`goods_prop`(`prop_name`, `index_flag`, `create_time`, `update_time`, `del_flag`, `sort`, `prop_type`) VALUES ('出版社', 1, '2021-09-07 16:39:50', '2021-09-28 13:22:10', 0, 1, 1);
INSERT INTO `sbc-goods`.`goods_prop`(`prop_name`, `index_flag`, `create_time`, `update_time`, `del_flag`, `sort`, `prop_type`) VALUES ('评分', 1, '2021-09-07 16:39:50', '2021-09-28 13:22:10', 0, 1, 4);
INSERT INTO `sbc-goods`.`goods_prop`(`prop_name`, `index_flag`, `create_time`, `update_time`, `del_flag`, `sort`, `prop_type`) VALUES ('定价', 1, '2021-09-07 16:39:50', '2021-09-28 13:22:10', 0, 1, 4);
INSERT INTO `sbc-goods`.`goods_prop`(`prop_name`, `index_flag`, `create_time`, `update_time`, `del_flag`, `sort`, `prop_type`) VALUES ('ISBN', 1, '2021-09-07 16:39:50', '2021-09-28 13:22:10', 0, 1, 3);
-- alter table goods_prop drop column cate_id;

CREATE TABLE `t_goods_prop_cate_rel` (
  `rel_id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_id` bigint(20) comment '属性id',
  `cate_id` bigint(20) COMMENT '类目id',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime comment '更新时间',
  `del_flag` tinyint comment '是否删除',
  PRIMARY KEY (`rel_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品属性与分类关联表';
-- sbc-goods 商品上架 END
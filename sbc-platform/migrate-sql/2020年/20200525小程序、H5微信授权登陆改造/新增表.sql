CREATE TABLE `sbc-customer`.`wechat_quick_login` (
  `id` varchar(32) NOT NULL,
  `open_id` varchar(50) DEFAULT NULL COMMENT '用户在各应用唯一标示',
  `union_id` varchar(255) DEFAULT NULL COMMENT '用户在开放平台唯一标示',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '是否删除标志 0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序授权登陆用户信息';
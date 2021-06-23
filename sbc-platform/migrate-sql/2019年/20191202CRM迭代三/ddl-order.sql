CREATE TABLE `goods_customer_follow_action` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(32) NOT NULL COMMENT '会员Id',
  `goods_id` varchar(32) NOT NULL COMMENT '商品Id',
  `goods_info_id` varchar(32) NOT NULL COMMENT 'SKU编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `company_info_id` bigint(11) NOT NULL COMMENT '公司信息ID',
  `goods_num` bigint(10) NOT NULL COMMENT '购买数',
  `follow_flag` tinyint(4) DEFAULT NULL COMMENT '收藏标识0:all,1.采购,2:收藏',
  `follow_time` datetime DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1942 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品的客户收藏行为表（明细）';

CREATE TABLE `purchase_action` (
  `purchase_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '会员Id',
  `goods_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品id',
  `goods_info_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT 'SKU编号',
  `company_info_id` bigint(20) DEFAULT NULL COMMENT '商家编号',
  `goods_num` bigint(20) DEFAULT NULL COMMENT '购买数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `invitee_id` varchar(32) DEFAULT '0' COMMENT '默认为0、店铺精选时对应邀请人id（customerID）',
  PRIMARY KEY (`purchase_id`) USING BTREE,
  KEY `idx_purchase_customerid_goodsinfoid` (`customer_id`,`goods_info_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7271 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='购物车行为（明细）';



-- 建立触发器
DELIMITER $$
create trigger trigger_purchase_action after INSERT on purchase
for each row
	BEGIN
		insert into purchase_action(purchase_id,customer_id,goods_id,goods_info_id,company_info_id,goods_num,create_time,invitee_id)
		values (NEW.purchase_id,NEW.customer_id,NEW.goods_id,NEW.goods_info_id,NEW.company_info_id,NEW.goods_num,NEW.create_time,NEW.invitee_id);
	end;
$$

DELIMITER $$
create trigger trigger_goods_customer_follow_action after INSERT on goods_customer_follow
for each row
	BEGIN
		insert into goods_customer_follow_action(follow_id,customer_id,goods_id,goods_info_id,company_info_id,goods_num,create_time,follow_flag,follow_time)
		values (NEW.follow_id,NEW.customer_id,NEW.goods_id,NEW.goods_info_id,NEW.company_info_id,NEW.goods_num,NEW.create_time,NEW.follow_flag,NEW.follow_time);
	end;
$$
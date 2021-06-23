


#       menu_info

insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('196','4','0','1','营销','','1520923608795.jpg','4','2018-09-18 14:20:36','0');
insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('197','4','196','2','平台营销管理','','1520924731862.jpg','0','2018-09-18 14:28:44','0');
insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('198','4','197','3','营销中心','/marketing-center','','0','2018-09-18 14:29:19','0');
insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('199','4','197','3','优惠券活动列表','/coupon-activity-list','','1','2018-09-19 10:02:22','0');
insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('200','4','197','3','优惠券分类','/coupon-cate','','2','2018-09-19 10:02:22','0');
insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('201','4','197','3','优惠券列表','/coupon-list','','1','2018-09-21 10:59:49','0');
insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('202','3','172','3','优惠券活动列表','/coupon-activity-list','','1','2018-09-25 11:18:40','0');
insert into `a` (`menu_id`, `system_type_cd`, `parent_menu_id`, `menu_grade`, `menu_name`, `menu_url`, `menu_icon`, `sort`, `create_time`, `del_flag`) values('203','3','172','3','优惠券列表','/coupon-list','','1','2018-09-26 13:45:48','0');




#       menu_info



#       function_info

insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('234','4','199','优惠券活动列表查看','f_coupon_activity_list','','1','2018-09-19 11:36:52','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('235','4','199','优惠券活动删除','f_coupon_activity_delete','','2','2018-09-19 11:41:31','1');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('236','4','199','优惠券活动修改/开始/暂停/删除','f_coupon_activity_editor','','3','2018-09-19 11:42:26','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('237','4','199','优惠券活动详情查看','f_coupon_activity_detail','','4','2018-09-19 11:43:53','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('238','4','201','优惠券列表查看','f_coupon_list','','1','2018-09-21 11:00:23','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('240','4','198','创建优惠券','f_create_coupon','','0','2018-09-26 17:14:51','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('241','4','198','创建全场赠券活动','f_create_all_coupon_activity','','1','2018-09-26 17:15:33','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('242','4','201','优惠券编辑/复制/删除','f_coupon_editor','','0','2018-09-26 17:18:31','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('243','4','200','优惠券分类查询','f_coupon_cate_query','','0','2018-09-26 17:26:32','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('244','4','200','优惠券分类新增/编辑/删除','f_coupon_cate_editor','','1','2018-09-26 17:27:20','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('245','4','201','优惠券详情查看','f_coupon_detail','','2','2018-09-26 17:48:21','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('246','3','173','创建优惠券','f_create_coupon','','3','2018-09-28 10:15:40','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('247','3','173','创建全场赠券活动','f_create_all_coupon_activity','','4','2018-09-28 10:16:00','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('248','3','202','优惠券活动列表查看','f_coupon_activity_list','','0','2018-09-28 10:16:30','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('249','3','202','优惠券活动修改/开始/暂停/删除','f_coupon_activity_editor','','1','2018-09-28 10:16:46','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('250','3','202','优惠券活动详情查看','f_coupon_activity_detail','','2','2018-09-28 10:17:01','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('251','3','203','优惠券编辑/复制/删除','f_coupon_editor','','0','2018-09-28 10:17:24','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('252','3','203','优惠券列表查看','f_coupon_list','','1','2018-09-28 10:17:39','0');
insert into `a` (`function_id`, `system_type_cd`, `menu_id`, `function_title`, `function_name`, `remark`, `sort`, `create_time`, `del_flag`) values('253','3','203','优惠券详情查看','f_coupon_detail','','2','2018-09-28 10:17:54','0');



#       function_info



#    authority



insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('784','4','234','优惠券活动列表查询','','/coupon-activity/page','POST','','1','2018-09-19 11:38:03','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('785','4','238','优惠券列表查询','','/coupon-info/page','POST','','1','2018-09-21 11:01:47','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('786','4','235','删除优惠券活动','','/coupon-activity/*','DELETE','','1','2018-09-21 13:30:39','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('787','4','236','开始活动','','/coupon-activity/start/*','PUT','','1','2018-09-21 13:31:26','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('788','4','236','暂停活动','','/coupon-activity/pause/*','PUT','','2','2018-09-21 13:32:04','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('789','4','237','查看活动基本信息','','/coupon-activity/*','GET','','1','2018-09-21 13:32:49','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('790','4','236','删除活动','','/coupon-activity/*','DELETE','','0','2018-09-26 16:44:16','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('791','4','236','编辑活动','','/coupon-activity/modify','PUT','','3','2018-09-26 16:47:12','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('792','4','240','创建优惠券','','/couponInfo','POST','','0','2018-09-26 17:44:33','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('793','4','240','查询优惠券分类列表','','/coupon-cate/list','GET','','1','2018-09-26 17:46:17','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('794','4','240','查询全部品牌','','/goods/allGoodsBrands','GET','','2','2018-09-26 17:46:40','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('795','4','240','查询商品全部分类','','/goods/goodsCatesTree','GET','','3','2018-09-26 17:47:09','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('796','4','241','新增优惠券活动','','/coupon-activity/add','POST','','0','2018-09-26 17:58:03','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('797','4','241','分页查询优惠券信息','','/couponInfo/page','POST','','1','2018-09-26 17:58:41','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('798','4','242','删除优惠券','','/couponInfo/*','GET','','0','2018-09-26 18:00:00','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('799','4','242','复制优惠券','','/couponInfo/copy/*','GET','','1','2018-09-26 18:00:26','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('800','4','242','查询优惠券信息','','/couponInfo/*','GET','','2','2018-09-26 18:05:19','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('801','4','245','查询优惠券信息','','/couponInfo/*','GET','','0','2018-09-26 18:07:08','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('802','4','242','修改优惠券','','/couponInfo','PUT','','3','2018-09-26 18:08:22','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('803','4','242','查询商品全部分类','','/goods/goodsCatesTree','GET','','4','2018-09-26 18:08:58','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('804','4','242','查询全部品牌','','/goods/allGoodsBrands','GET','','5','2018-09-26 18:09:17','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('805','4','242','查询优惠券分类列表','','/coupon-cate/list','GET','','6','2018-09-26 18:09:34','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('806','4','243','获取优惠券分类列表','','/coupon-cate/list','GET','','0','2018-09-26 18:10:31','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('807','4','244','添加优惠券分类','','/coupon-cate/*','POST','','1','2018-09-26 18:11:07','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('808','4','244','修改优惠券分类','','/coupon-cate/*','PUT','','1','2018-09-26 18:11:50','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('809','4','244','删除优惠券分类','','/coupon-cate/*','DELETE','','3','2018-09-26 18:12:14','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('810','4','244','更改是否平台可用','','/coupon-cate/platform*','PUT','','3','2018-09-27 09:32:33','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('811','4','244','拖拽排序','','/coupon-cate/sort','PUT','','4','2018-09-27 09:32:55','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('812','3','246','查询优惠券分类列表','','/coupon-cate/list','GET','','0','2018-09-29 14:23:18','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('813','3','246','查询店铺签约的品牌','','/contract/goods/brand/list','GET','','1','2018-09-29 14:23:34','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('814','3','246','查询店铺分类','','/contract/goods/cate/list','GET','','2','2018-09-29 14:23:53','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('815','3','246','新增优惠券','','/couponInfo','POST','','3','2018-09-29 14:24:15','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('816','3','246','查询商品列表','','/goods/skus','POST','','4','2018-09-29 14:24:39','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('817','3','247','分页查询优惠券信息','','/couponInfo/page','POST','','0','2018-09-29 14:25:35','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('818','3','247','新增优惠券活动','','/coupon-activity/add','POST','','1','2018-09-29 14:25:59','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('819','3','247','获取所有客户等级','','/storelevel/levels','GET','','2','2018-09-29 14:26:17','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('820','3','248','获取活动列表','','/coupon-activity/page','POST','','0','2018-09-29 14:29:33','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('821','3','248','获取所有客户等级','','/storelevel/levels','GET','','1','2018-09-29 14:29:49','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('822','3','249','开始活动','','/coupon-activity/start/*','PUT','','0','2018-09-29 14:30:57','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('823','3','249','暂停活动','','/coupon-activity/pause/*','PUT','','1','2018-09-29 14:31:19','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('824','3','249','删除活动','','/coupon-activity/*','DELETE','','3','2018-09-29 14:31:45','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('825','3','249','获取所有客户等级','','/storelevel/levels','GET','','2','2018-09-29 14:32:16','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('826','3','249','分页查询优惠券信息','','/couponInfo/page','POST','','4','2018-09-29 14:32:42','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('827','3','249','修改优惠券活动','','/coupon-activity/modify','PUT','','5','2018-09-29 14:33:05','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('828','3','249','获取优惠券详情','','/coupon-activity/','GET','','5','2018-09-29 14:33:36','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('829','3','250','获取优惠券活动详情','','/coupon-activity/*','GET','','0','2018-09-29 14:34:21','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('830','3','252','获取优惠券列表','','/couponInfo/page','POST','','0','2018-09-29 14:35:01','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('831','3','251','删除优惠券','','/couponInfo/*','DELETE','','1','2018-09-29 14:35:31','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('832','3','251','复制优惠券','','/couponInfo/copy/*','GET','','2','2018-09-29 14:35:56','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('833','3','251','查询商品列表','','/goods/skus','POST','','1','2018-09-29 14:36:38','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('834','3','251','修改优惠券','','/couponInfo','PUT','','3','2018-09-29 14:36:59','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('835','3','251','查询优惠券信息','','/couponInfo/*','GET','','4','2018-09-29 14:37:16','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('836','3','251','查询店铺分类','','/contract/goods/cate/list','GET','','5','2018-09-29 14:37:47','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('837','3','251','查询店铺签约的品牌','','/contract/goods/brand/list','GET','','6','2018-09-29 14:38:04','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('838','3','251','查询优惠券分类列表','','/coupon-cate/list','GET','','7','2018-09-29 14:38:24','0');
insert into `a` (`authority_id`, `system_type_cd`, `function_id`, `authority_title`, `authority_name`, `authority_url`, `request_type`, `remark`, `sort`, `create_time`, `del_flag`) values('839','3','253','优惠券详情查看','','/couponInfo/*','GET','','0','2018-09-29 14:42:00','0');


#   authority 





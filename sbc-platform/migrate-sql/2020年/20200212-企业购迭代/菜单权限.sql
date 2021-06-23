-- 商家端菜单权限中“供应商”-》“商家”
update menu_info set menu_name = '商家收款账户' where menu_id = 'fc8e49ac3fe311e9828800163e0fc468';
update function_info set function_title = '商家收款账户查看' where function_id = 'fc93463c3fe311e9828800163e0fc468';
-- 平台端菜单权限中“供应商”-》“商家”
update menu_info set menu_name = '商家收款账户' where menu_id = 'fc8df78c3fe311e9828800163e0fc468';
-- 商家一级菜单不该有url
update `sbc-setting`.`menu_info` set menu_url='' where menu_id = 'fc8defa73fe311e9828800163e0fc468';


-- setting,平台供应商端商品菜单改造
use `sbc-setting`;
update menu_info set del_flag= 1 where menu_name = "商品列表" and system_type_cd = 4 and sort = 1;
update menu_info set del_flag= 1 where menu_name = "待审核商品" and system_type_cd = 4 and sort = 2;
INSERT INTO `menu_info` VALUES ('ff808081701f2f3301703837ef2e0003', 4, 'fc8def483fe311e9828800163e0fc468', 2, '商家商品', NULL, '1496720449610.jpg', 2, '2020-02-12 15:05:55', 0);
INSERT INTO `menu_info` VALUES ('ff808081701f2f3301703838d0bd0004', 4, 'ff808081701f2f3301703837ef2e0003', 3, '待审核商品', '/goods-check-supplier', NULL, 1, '2020-02-12 15:06:52', 0);
INSERT INTO `menu_info` VALUES ('ff808081701f2f33017038390e470005', 4, 'ff808081701f2f3301703837ef2e0003', 3, '商品列表', '/goods-list-supplier', NULL, 2, '2020-02-12 15:07:08', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703ca046690010', 4, 'ff808081701f2f3301703838d0bd0004', '待审核商品列表查看', 'f_goods_check_1_mall', NULL, 1, '2020-02-13 11:38:22', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703ca4d3550016', 4, 'ff808081701f2f3301703838d0bd0004', '商品审核/驳回', 'f_goods_audit_mall', NULL, 2, '2020-02-13 11:43:20', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703ca9d0ea0019', 4, 'ff808081701f2f3301703838d0bd0004', '商品详情查看', 'f_goods_detail_2_mall', NULL, 3, '2020-02-13 11:48:47', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703cb0c7bc002a', 4, 'ff808081701f2f3301703838d0bd0004', '商品SKU查看', 'f_goods_sku_detail_2_mall', NULL, 4, '2020-02-13 11:56:23', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703cc3a75b0042', 4, 'ff808081701f2f33017038390e470005', '商品列表查看', 'f_goods_1_mall', NULL, 1, '2020-02-13 12:17:00', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703cc9b600004b', 4, 'ff808081701f2f33017038390e470005', '商品禁售', 'f_goods_2_mall', NULL, 2, '2020-02-13 12:23:37', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703ccf2a940051', 4, 'ff808081701f2f33017038390e470005', '商品详情查看', 'f_goods_detail_1_mall', NULL, 3, '2020-02-13 12:29:35', 0);
INSERT INTO `function_info` VALUES ('ff808081701f2f3301703cd5c4820061', 4, 'ff808081701f2f33017038390e470005', '商品SKU查看', 'f_goods_sku_detail_1_mall', NULL, 4, '2020-02-13 12:36:47', 0);
INSERT INTO `function_info` VALUES ('ff8080817067857d017076f528bf0003', 4, 'ff808081701f2f33017038390e470005', '加入商品库', 'f_add_to_goods_library_1', NULL, 5, '2020-02-24 19:29:03', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703ca0907b0011', 4, 'ff808081701f2f3301703ca046690010', '商品列表', NULL, '/goods/spus', 'POST', NULL, 1, '2020-02-13 11:38:41', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703ca0e23a0012', 4, 'ff808081701f2f3301703ca046690010', '查询全部品牌', NULL, '/goods/allGoodsBrands', 'GET', NULL, 2, '2020-02-13 11:39:02', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703ca1324c0013', 4, 'ff808081701f2f3301703ca046690010', '查询全部分类', NULL, '/goods/goodsCatesTree', 'GET', NULL, 3, '2020-02-13 11:39:22', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703ca56c680018', 4, 'ff808081701f2f3301703ca4d3550016', 'spu审核/驳回', NULL, '/goods/check', 'PUT', NULL, 1, '2020-02-13 11:43:59', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cab224f001c', 4, 'ff808081701f2f3301703ca9d0ea0019', '获取商品详情', NULL, '/goods/spu/*', 'GET', NULL, 1, '2020-02-13 11:50:13', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cabf32d001e', 4, 'ff808081701f2f3301703ca9d0ea0019', '获取类目列表', NULL, '/contract/goods/cate/list/*', 'GET', NULL, 2, '2020-02-13 11:51:07', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cac490c001f', 4, 'ff808081701f2f3301703ca9d0ea0019', '获取店铺分类列表', NULL, '/storeCate/*', 'GET', NULL, 3, '2020-02-13 11:51:29', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cad04c60022', 4, 'ff808081701f2f3301703ca9d0ea0019', '获取品牌列表', NULL, '/contract/goods/brand/list/*', 'GET', NULL, 4, '2020-02-13 11:52:17', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cad667c0023', 4, 'ff808081701f2f3301703ca9d0ea0019', '获取客户等级列表', NULL, '/store/levels/*', 'GET', NULL, 5, '2020-02-13 11:52:42', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cae1c020026', 4, 'ff808081701f2f3301703ca9d0ea0019', '获取客户列表', NULL, '/store/allCustomers/*', 'POST', NULL, 6, '2020-02-13 11:53:28', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703caea91d0027', 4, 'ff808081701f2f3301703ca9d0ea0019', '获取店铺信息', NULL, '/store/store-info/*', 'GET', NULL, 7, '2020-02-13 11:54:04', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cb1613f002c', 4, 'ff808081701f2f3301703cb0c7bc002a', '获取商品sku详情', NULL, '/goods/sku/*', 'GET', NULL, 1, '2020-02-13 11:57:03', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cb20fd1002e', 4, 'ff808081701f2f3301703cb0c7bc002a', '获取类目列表', NULL, '/contract/goods/cate/list/*', 'GET', NULL, 2, '2020-02-13 11:57:47', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cb272ee002f', 4, 'ff808081701f2f3301703cb0c7bc002a', '获取店铺分类列表', NULL, '/storeCate/*', 'GET', NULL, 3, '2020-02-13 11:58:13', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cb314ef0032', 4, 'ff808081701f2f3301703cb0c7bc002a', '获取品牌列表', NULL, '/contract/goods/brand/list/*', 'GET', NULL, 4, '2020-02-13 11:58:54', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cb3c2d20033', 4, 'ff808081701f2f3301703cb0c7bc002a', '获取客户等级列表', NULL, '/store/levels/*', 'GET', NULL, 5, '2020-02-13 11:59:39', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cb4a7270036', 4, 'ff808081701f2f3301703cb0c7bc002a', '获取客户列表', NULL, '/store/allCustomers/*', 'POST', NULL, 6, '2020-02-13 12:00:37', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cb4fb800037', 4, 'ff808081701f2f3301703cb0c7bc002a', '获取店铺信息', NULL, '/store/store-info/*', 'GET', NULL, 7, '2020-02-13 12:00:59', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cc6c74d0044', 4, 'ff808081701f2f3301703cc3a75b0042', '商品列表', NULL, '/goods/spus', 'POST', NULL, 1, '2020-02-13 12:20:25', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cc713fd0045', 4, 'ff808081701f2f3301703cc3a75b0042', '查询全部品牌', NULL, '/goods/allGoodsBrands', 'GET', NULL, 2, '2020-02-13 12:20:45', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cc7c82f0048', 4, 'ff808081701f2f3301703cc3a75b0042', '查询全部分类', NULL, '/goods/goodsCatesTree', 'GET', NULL, 3, '2020-02-13 12:21:31', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cca3695004d', 4, 'ff808081701f2f3301703cc9b600004b', 'spu禁售', NULL, '/goods/check', 'PUT', NULL, 1, '2020-02-13 12:24:10', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703ccad2d1004f', 4, 'ff808081701f2f3301703cc9b600004b', 'spu禁售', NULL, '/goods/forbid', 'PUT', NULL, 1, '2020-02-13 12:24:50', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703ccf9d230053', 4, 'ff808081701f2f3301703ccf2a940051', '获取商品详情', NULL, '/goods/spu/*', 'GET', NULL, 1, '2020-02-13 12:30:04', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703ccfe2a40054', 4, 'ff808081701f2f3301703ccf2a940051', '获取类目列表', NULL, '/contract/goods/cate/list/*', 'GET', NULL, 2, '2020-02-13 12:30:22', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd084e40057', 4, 'ff808081701f2f3301703ccf2a940051', '获取店铺分类列表', NULL, '/storeCate/*', 'GET', NULL, 3, '2020-02-13 12:31:03', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd0fc4e0058', 4, 'ff808081701f2f3301703ccf2a940051', '获取品牌列表', '', '/contract/goods/brand/list/*', 'GET', NULL, 4, '2020-02-13 12:31:34', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd1b51e005b', 4, 'ff808081701f2f3301703ccf2a940051', '获取客户等级列表', NULL, '/store/levels/*', 'GET', NULL, 5, '2020-02-13 12:32:21', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd20c8a005c', 4, 'ff808081701f2f3301703ccf2a940051', '获取客户列表', NULL, '/store/allCustomers/*', 'POST', NULL, 6, '2020-02-13 12:32:44', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd2fba7005f', 4, 'ff808081701f2f3301703ccf2a940051', '获取店铺信息', NULL, '/store/store-info/*', 'GET', NULL, 7, '2020-02-13 12:33:45', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd64bd30063', 4, 'ff808081701f2f3301703cd5c4820061', '获取商品sku详情', NULL, '/goods/sku/*', 'GET', NULL, 1, '2020-02-13 12:37:22', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd6c9c90065', 4, 'ff808081701f2f3301703cd5c4820061', '获取类目列表', NULL, '/contract/goods/cate/list/*', 'GET', NULL, 2, '2020-02-13 12:37:54', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd70bcd0066', 4, 'ff808081701f2f3301703cd5c4820061', '获取店铺分类列表', NULL, '/storeCate/*', 'GET', NULL, 3, '2020-02-13 12:38:11', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd7cc7b0069', 4, 'ff808081701f2f3301703cd5c4820061', '获取品牌列表', NULL, '/contract/goods/brand/list/*', 'GET', NULL, 4, '2020-02-13 12:39:00', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd81bca006a', 4, 'ff808081701f2f3301703cd5c4820061', '获取客户等级列表', NULL, '/store/levels/*', 'GET', NULL, 5, '2020-02-13 12:39:21', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd8d62e006d', 4, 'ff808081701f2f3301703cd5c4820061', '获取客户列表', NULL, '/store/allCustomers/*', 'POST', NULL, 6, '2020-02-13 12:40:08', 0);
INSERT INTO `authority` VALUES ('ff808081701f2f3301703cd93c14006e', 4, 'ff808081701f2f3301703cd5c4820061', '获取店铺信息', NULL, '/store/store-info/*', 'GET', NULL, 7, '2020-02-13 12:40:35', 0);
INSERT INTO `authority` VALUES ('ff8080817067857d017076f5afb40004', 4, 'ff8080817067857d017076f528bf0003', '将商品加入到商品库中', NULL, '/goods/standard', 'POST', NULL, 1, '2020-02-24 19:29:38', 0);


-- 商家端商品列表编辑，缺少两个权限。
INSERT INTO `authority` VALUES ('ff8080817067857d01707b307bed0007', 3, 'fc924c7c3fe311e9828800163e0fc468', '查询平台客户等级列表', NULL, '/store/storeLevel/listBoss', 'GET', NULL, 32, '2020-02-25 15:12:20', 0);
INSERT INTO `authority` VALUES ('ff8080817067857d01707b30bbae0008', 3, 'fc924c7c3fe311e9828800163e0fc468', '获取店铺客户等级列表', NULL, '/store/storeLevel/list', 'GET', NULL, 35, '2020-02-25 15:12:36', 0);
INSERT INTO `authority` VALUES ('ff8080817067857d01707f797b83000b', 3, 'fc924c7c3fe311e9828800163e0fc468', '查询某个运费模板', NULL, '/freightTemplate/freightTemplateGoods/*', 'GET', NULL, 11, '2020-02-26 11:10:33', 0);
INSERT INTO `authority` VALUES ('ff8080817067857d01707f79d44e000c', 3, 'fc924c7c3fe311e9828800163e0fc468', '查询某个运费模板配送地', NULL, '/freightTemplate/freightTemplateExpress/*', 'GET', NULL, 12, '2020-02-26 11:10:56', 0);
INSERT INTO `authority` VALUES ('ff8080817067857d01707f7da624000f', 3, 'fc93637e3fe311e9828800163e0fc468', '查询某个运费模板', NULL, '/freightTemplate/freightTemplateGoods/*', 'GET', NULL, 11, '2020-02-26 11:15:06', 0);
INSERT INTO `authority` VALUES ('ff8080817067857d01707f7ded850010', 3, 'fc93637e3fe311e9828800163e0fc468', '查询某个运费模板配送地', NULL, '/freightTemplate/freightTemplateExpress/*', 'GET', NULL, 12, '2020-02-26 11:15:24', 0);

-- 商家端，供应商端财务对账，财务结算导出列表的两个权限颠倒了，现更正。
update `sbc-setting`.`authority` set authority_url='/finance/bill/exportIncome/*' where authority_id = 'fc9b19ea3fe311e9828800163e0fc468';
update `sbc-setting`.`authority` set authority_url='/finance/settlement/export/*' where authority_id = 'fc9b1a4f3fe311e9828800163e0fc468';

-- boss端商品类目管理缺少权限
INSERT INTO `authority` VALUES ('ff8080817067857d0170847e5cc60014', 4, 'fc936e4a3fe311e9828800163e0fc468', '类目拖拽排序', NULL, '/goods/goods-cate/sort', 'PUT', NULL, 1, '2020-02-27 10:33:59', 0);

-- boss商家管理修改为供应商管理，商家管理
INSERT INTO `sbc-setting`.`menu_info` VALUES ('ff8080817067857d01708b136b7b0015', 4, 'fc8defa73fe311e9828800163e0fc468', 2, '供应商管理', NULL, 'store-icon.png', 0, '2020-02-28 17:14:31', 0);
update `sbc-setting`.`menu_info` set parent_menu_id = 'ff8080817067857d01708b136b7b0015' where menu_id = '2c9386c1705b7add01705b8a722d0000';
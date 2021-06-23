-- 商家端，boss端新增企业会员菜单
use `sbc-setting`;
INSERT INTO `sbc-setting`.`menu_info` VALUES ('ff80808170860f0801708b43fff60000', 4, 'fc8df6013fe311e9828800163e0fc468', 3, '企业会员', '/enterprise-customer-list', NULL, 122, '2020-02-28 18:07:34', 0);
INSERT INTO `sbc-setting`.`menu_info` VALUES ('ff80808170860f0801708b4fc0460003', 3, 'fc8e13dc3fe311e9828800163e0fc468', 3, '企业会员', '/enterprise-customer-list', NULL, 19, '2020-02-28 18:20:25', 0);
INSERT INTO `function_info` VALUES ('ff8080817098f61601709e6b27ed0000', 4, 'ff80808170860f0801708b43fff60000', '企业会员列表', 'f_enterprise_customer_list', NULL, 0, '2020-03-03 11:23:08', 0);
INSERT INTO `function_info` VALUES ('ff8080817098f61601709e70047c0001', 4, 'ff80808170860f0801708b43fff60000', '新增企业会员', 'f_enterprise_customer_add', NULL, 1, '2020-03-03 11:28:26', 0);
INSERT INTO `function_info` VALUES ('ff80808170a4a0210170a95541e00003', 4, 'ff80808170860f0801708b43fff60000', '企业会员启用/禁用', 'f_enterprise_customer_4', NULL, 4, '2020-03-05 14:15:02', 0);
INSERT INTO `function_info` VALUES ('ff80808170a4a0210170a959f8e40009', 4, 'ff80808170860f0801708b43fff60000', '企业会员审核/驳回', 'f_enterprisecustomer_3', NULL, 3, '2020-03-05 14:20:11', 0);
INSERT INTO `function_info` VALUES ('ff80808170a4a0210170a95f4017000b', 4, 'ff80808170860f0801708b43fff60000', '企业会员详情查看', 'f_enterprise_customer_detail_view', NULL, 5, '2020-03-05 14:25:57', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a956b8a10005', 4, 'ff8080817098f61601709e6b27ed0000', '获取企业会员列表', NULL, '/enterpriseCustomer/page', 'POST', NULL, 1, '2020-03-05 14:16:38', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a9578f1f0006', 4, 'ff8080817098f61601709e6b27ed0000', '获取所有业务员', NULL, '/customer/employee/allEmployees', 'GET', NULL, 1, '2020-03-05 14:17:33', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a95809f70007', 4, 'ff8080817098f61601709e6b27ed0000', '获取所有客户等级', NULL, '/customer/levellist', 'GET', NULL, 2, '2020-03-05 14:18:04', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a9596b8a0008', 4, 'ff8080817098f61601709e70047c0001', '新增企业会员', NULL, '/enterpriseCustomer', 'POST', NULL, 1, '2020-03-05 14:19:35', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a955b29c0004', 4, 'ff80808170a4a0210170a95541e00003', '禁用启用客户', NULL, '/customer/detailState', 'POST', NULL, 1, '2020-03-05 14:15:31', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a95b24c0000a', 4, 'ff80808170a4a0210170a959f8e40009', '企业会员审核/驳回', NULL, '/enterpriseCustomer/checkEnterpriseCustomer', 'POST', NULL, 1, '2020-03-05 14:21:28', 0);
INSERT INTO `function_info` VALUES ('ff80808170a3283d0170a372fd930000', 3, 'ff80808170860f0801708b4fc0460003', '企业会员列表查看', 'f_customer_iep_page', NULL, 1, '2020-03-04 10:49:47', 0);
INSERT INTO `authority` VALUES ('ff80808170a3283d0170a374f6110001', 3, 'ff80808170a3283d0170a372fd930000', '获取登录人信息', NULL, '/customer/employee/myself', 'GET', NULL, 1, '2020-03-04 10:51:56', 0);
INSERT INTO `authority` VALUES ('ff80808170a3283d0170a4861d090002', 3, 'ff80808170a3283d0170a372fd930000', '查询企业会员列表', NULL, '/enterpriseCustomer/page', 'POST', NULL, 1, '2020-03-04 15:50:18', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a99c5091000c', 3, 'ff80808170a3283d0170a372fd930000', '获取所有业务员', NULL, '/customer/boss/allEmployees', 'GET', NULL, 1, '2020-03-05 15:32:39', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170a99cb160000d', 3, 'ff80808170a3283d0170a372fd930000', '获取所有客户等级', NULL, '/customer/levellist', 'GET', NULL, 2, '2020-03-05 15:33:03', 0);


-- boss和商家端新增企业购商品的菜单和功能
use `sbc-setting`;
INSERT INTO `menu_info` VALUES ('ff80808170860f0801708b50597c0004', 3, 'fc8e11003fe311e9828800163e0fc468', 2, '企业购', NULL, NULL, 0, '2020-02-28 18:21:04', 0);
INSERT INTO `menu_info` VALUES ('ff80808170860f0801708b50875b0005', 3, 'ff80808170860f0801708b50597c0004', 3, '企业购商品', '/enterprise-goods-list', NULL, 0, '2020-02-28 18:21:15', 0);
INSERT INTO `menu_info` VALUES ('ff80808170860f0801708b4e9ec80001', 4, 'fc8e20ff3fe311e9828800163e0fc468', 2, '企业购', NULL, NULL, 0, '2020-02-28 18:19:10', 0);
INSERT INTO `menu_info` VALUES ('ff80808170860f0801708b4ef53e0002', 4, 'ff80808170860f0801708b4e9ec80001', 3, '企业购商品', '/enterprise-goods-list', NULL, 0, '2020-02-28 18:19:33', 0);

INSERT INTO `authority` VALUES ('ff80808170a4a0210170ada2dd6a0010', 3, 'ff80808170a4a0210170ada0791e000f', '获取企业购商品', NULL, '/enterprise/goodsInfo/page', 'POST', NULL, 0, '2020-03-06 10:18:17', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170ada484730012', 4, 'ff80808170a4a0210170ada3d17f0011', '获取企业购商品列表', NULL, '/enterprise/goodsInfo/page', 'POST', NULL, 0, '2020-03-06 10:20:05', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170ada561e90013', 4, 'ff80808170a4a0210170aa01ff98000e', '企业购商品单个审核接口', NULL, '/enterprise/goodsInfo/audit', 'POST', NULL, 0, '2020-03-06 10:21:02', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170ada811d30014', 3, 'ff80808170a4a0210170ada0791e000f', '新增企业购商品', NULL, '/enterprise/batchAdd', 'POST', NULL, 1, '2020-03-06 10:23:58', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170adab45b40015', 3, 'ff80808170a4a0210170a8afada80000', '修改企业价格', NULL, '/enterprise/modify', 'POST', NULL, 0, '2020-03-06 10:27:28', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170adac01790016', 3, 'ff80808170a4a0210170a8b01f8f0001', '删除企业商品', NULL, '/enterprise/delete', 'POST', NULL, 0, '2020-03-06 10:28:16', 0);
INSERT INTO `authority` VALUES ('ff80808170a4a0210170adad02d80017', 3, 'ff80808170a4a0210170ada0791e000f', '获取普通商品的接口', NULL, '/enterprise/enterprise-sku', 'POST', NULL, 1, '2020-03-06 10:29:22', 0);

INSERT INTO `function_info` VALUES ('ff80808170a4a0210170a8afada80000', 3, 'ff80808170860f0801708b50875b0005', '修改企业购商品的价格', 'f_enterprise_goods_edit', NULL, 1, '2020-03-05 11:14:10', 0);
INSERT INTO `function_info` VALUES ('ff80808170a4a0210170a8b01f8f0001', 3, 'ff80808170860f0801708b50875b0005', '删除企业购商品', 'f_enterprise_goods_del', NULL, 2, '2020-03-05 11:14:40', 0);
INSERT INTO `function_info` VALUES ('ff80808170a4a0210170aa01ff98000e', 4, 'ff80808170860f0801708b4ef53e0002', '企业购商品审核', 'f_enterprise_goods_audit', NULL, 1, '2020-03-05 17:23:43', 0);
INSERT INTO `function_info` VALUES ('ff80808170a4a0210170ada0791e000f', 3, 'ff80808170860f0801708b50875b0005', '企业购商品列表', 'f_enterprise_goods_list', NULL, 0, '2020-03-06 10:15:40', 0);
INSERT INTO `function_info` VALUES ('ff80808170a4a0210170ada3d17f0011', 4, 'ff80808170860f0801708b4ef53e0002', '企业购商品列表', 'f_enterprise_goods_list', NULL, 0, '2020-03-06 10:19:19', 0);

-- 2020/3/10setting,平台商家端商品菜单改造，将原商品列表、待审核列表菜单移到“商家商品”菜单下
update `sbc-setting`.`menu_info` set parent_menu_id = 'ff808081701f2f3301703837ef2e0003' where menu_id = 'fc8df2af3fe311e9828800163e0fc468';
update `sbc-setting`.`menu_info` set parent_menu_id = 'ff808081701f2f3301703837ef2e0003' where menu_id = 'fc8df25c3fe311e9828800163e0fc468';
update `sbc-setting`.`menu_info` set sort = 3 where menu_id = 'fc8df25c3fe311e9828800163e0fc468';

-- 废除企业购迭代1商家端新增的商品列表，待审核列表。恢复原先的菜单。
update `sbc-setting`.`menu_info` set del_flag = 1 where menu_id = 'ff808081701f2f33017038390e470005';
update `sbc-setting`.`menu_info` set del_flag = 1 where menu_id = 'ff808081701f2f3301703838d0bd0004';

update `sbc-setting`.`menu_info` set del_flag = 0 where menu_id = 'fc8df2af3fe311e9828800163e0fc468';
update `sbc-setting`.`menu_info` set del_flag = 0 where menu_id = 'fc8df25c3fe311e9828800163e0fc468';

-- 更改boss供应商商品模块功能名称
update `sbc-setting`.`function_info` set function_name = 'f_goods_check_1_provider' where function_id = 'ff808081701f2f3301703c7861de000c';
update `sbc-setting`.`function_info` set function_name = 'f_goods_audit_provider' where function_id = 'ff808081701f2f3301703ca42c4a0014';
update `sbc-setting`.`function_info` set function_name = 'f_goods_detail_2_provider' where function_id = 'ff808081701f2f3301703caa7146001a';
update `sbc-setting`.`function_info` set function_name = 'f_goods_sku_detail_provider' where function_id = 'ff808081701f2f3301703cb079860029';

update `sbc-setting`.`function_info` set function_name = 'f_goods_1_provider' where function_id = 'ff808081701f2f3301703cc34dce0041';
update `sbc-setting`.`function_info` set function_name = 'f_goods_2_provider' where function_id = 'ff808081701f2f3301703cc93ec10049';
update `sbc-setting`.`function_info` set function_name = 'f_goods_detail_1_provider' where function_id = 'ff808081701f2f3301703ccee81c0050';
update `sbc-setting`.`function_info` set function_name = 'f_goods_sku_detail_1_provider' where function_id = 'ff808081701f2f3301703cd57f6d0060';

-- 2020/3/10删除之前新增的商家端数据
delete from menu_info where menu_id in ('ff808081701f2f33017038390e470005','ff808081701f2f3301703838d0bd0004');
delete from function_info where menu_id in ('ff808081701f2f33017038390e470005','ff808081701f2f3301703838d0bd0004');
delete from authority
where function_id in ('ff808081701f2f3301703ca046690010','ff808081701f2f3301703ca4d3550016','ff808081701f2f3301703ca9d0ea0019','ff808081701f2f3301703cb0c7bc002a',
'ff808081701f2f3301703cc3a75b0042','ff808081701f2f3301703cc9b600004b','ff808081701f2f3301703ccf2a940051','ff808081701f2f3301703cd5c4820061','ff8080817067857d017076f528bf0003');

-- 2020/3/13增加企业会员列表功能权限
INSERT INTO `sbc-setting`.`function_info` VALUES ('ff80808170cdbf3b0170d1f04b100004', 4, 'ff80808170860f0801708b43fff60000', '会员成长值明细', 'f_enterprise_customer-grow-value_list', NULL, 1, '2020-03-13 11:29:11', 0);
INSERT INTO `sbc-setting`.`function_info` VALUES ('ff80808170d2087a0170d231bb390006', 4, 'ff80808170860f0801708b43fff60000', '企业会员编辑', 'f_enterprise_customer_detail_edit', NULL, 6, '2020-03-13 12:40:39', 0);
INSERT INTO `sbc-setting`.`function_info` VALUES ('ff80808170d2087a0170d23aaa7b0011', 4, 'ff80808170860f0801708b43fff60000', 'CRM客户详情查看', 'f_crm_enterprise_customer_detail', NULL, 8, '2020-03-13 12:50:25', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d2248ece0000', 4, 'ff80808170cdbf3b0170d1f04b100004', '客户成长值明细', NULL, '/customer/queryToGrowthValue', 'POST', NULL, 1, '2020-03-13 12:26:16', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d224f52e0001', 4, 'ff80808170cdbf3b0170d1f04b100004', '客户基本信息', NULL, '/customer/*', 'GET', NULL, 2, '2020-03-13 12:26:42', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d22d5e580002', 4, 'ff80808170a4a0210170a95f4017000b', '通过客户ID查询客户详细信息', NULL, '/customer/*', 'GET', NULL, 1, '2020-03-13 12:35:54', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d22dafbd0003', 4, 'ff80808170a4a0210170a95f4017000b', '通过客户ID查询客户的收货地址信息', NULL, '/customer/addressList/*', 'GET', NULL, 1, '2020-03-13 12:36:14', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d22df3fa0004', 4, 'ff80808170a4a0210170a95f4017000b', '通过客户ID查询客户的银行账户信息', NULL, '/customer/accountList/*', 'GET', NULL, 1, '2020-03-13 12:36:32', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d22e36fd0005', 4, 'ff80808170a4a0210170a95f4017000b', '通过客户ID查询客户的增专资质信息', NULL, '/customer/invoice/*', 'GET', NULL, 1, '2020-03-13 12:36:49', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d235cbf00007', 4, 'ff80808170d2087a0170d231bb390006', '保存收货地址', NULL, '/customer/address', 'POST', NULL, 1, '2020-03-13 12:45:06', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d2360fd90008', 4, 'ff80808170d2087a0170d231bb390006', '修改收货地址', NULL, '/customer/address', 'PUT', NULL, 1, '2020-03-13 12:45:23', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23656300009', 4, 'ff80808170d2087a0170d231bb390006', '删除收货地址', NULL, '/customer/address/*', 'DELETE', NULL, 1, '2020-03-13 12:45:41', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d236a538000a', 4, 'ff80808170d2087a0170d231bb390006', '删除银行账户信息', NULL, '/customer/account/*', 'DELETE', NULL, 1, '2020-03-13 12:46:02', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d2370f9b000b', 4, 'ff80808170d2087a0170d231bb390006', '修改客户银行账号', NULL, '/customer/account', 'PUT', NULL, 1, '2020-03-13 12:46:29', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d237745a000c', 4, 'ff80808170d2087a0170d231bb390006', '保存客户银行账号', NULL, '/customer/account', 'POST', NULL, 1, '2020-03-13 12:46:55', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d237b55e000d', 4, 'ff80808170d2087a0170d231bb390006', '修改客户信息', NULL, '/customer', 'PUT', NULL, 1, '2020-03-13 12:47:11', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d237ef70000e', 4, 'ff80808170d2087a0170d231bb390006', '修改客户增票资质', NULL, '/customer/invoice', 'PUT', NULL, 1, '2020-03-13 12:47:26', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d2382a11000f', 4, 'ff80808170d2087a0170d231bb390006', '保存客户增票资质', NULL, '/customer/invoice', 'POST', NULL, 1, '2020-03-13 12:47:41', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23af7cd0012', 4, 'ff80808170d2087a0170d23aaa7b0011', '验证CRM标记', NULL, '/crm/config/flag', 'GET', NULL, 0, '2020-03-13 12:50:45', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23b3d420013', 4, 'ff80808170d2087a0170d23aaa7b0011', 'crm会员详情', NULL, '/crm/rfmstatistic/**', 'POST', NULL, 0, '2020-03-13 12:51:03', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23b73080014', 4, 'ff80808170d2087a0170d23aaa7b0011', '通过客户Id查询基本信息', NULL, '/customer/crm/*', 'GET', NULL, 1, '2020-03-13 12:51:16', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23bac620015', 4, 'ff80808170d2087a0170d23aaa7b0011', '通过客户id查询标签列表', NULL, '/customer/tag-rel/list', 'POST', NULL, 2, '2020-03-13 12:51:31', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23be6ab0016', 4, 'ff80808170d2087a0170d23aaa7b0011', 'crm会员详情信息', NULL, '/crm/rfmstatistic/**', 'GET', NULL, 2, '2020-03-13 12:51:46', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23c1bc90017', 4, 'ff80808170d2087a0170d23aaa7b0011', '新增客户标签', NULL, '/customer/tag-rel/add', 'POST', NULL, 3, '2020-03-13 12:52:00', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23c55100018', 4, 'ff80808170d2087a0170d23aaa7b0011', '删除客户标签', NULL, '/customer/tag-rel/*', 'DELETE', NULL, 4, '2020-03-13 12:52:14', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23c92350019', 4, 'ff80808170d2087a0170d23aaa7b0011', '通过客户id查询群体列表', NULL, '/customer/group/*', 'GET', NULL, 5, '2020-03-13 12:52:30', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23cf1f2001a', 4, 'ff80808170d2087a0170d23aaa7b0011', '根据客户id查询优惠券列表', NULL, '/customer/coupons', 'POST', NULL, 7, '2020-03-13 12:52:54', 0);
INSERT INTO `sbc-setting`.`authority` VALUES ('ff80808170d2087a0170d23d2ca2001b', 4, 'ff80808170d2087a0170d23aaa7b0011', '查询所有标签', NULL, '/customertag/list', 'POST', NULL, 8, '2020-03-13 12:53:09', 0);

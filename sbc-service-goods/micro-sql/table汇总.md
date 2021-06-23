#商品模块相关表汇总
#数据库：sbc-goods

###品牌
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
|check_brand| 待审核品牌(商家申请品牌)表| MySQL
|contract_brand| 签约品牌表| MySQL
|goods_brand| 商品品牌表| MySQL

###分类
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
contract_cate| 签约分类表| MySQL
goods_cate| 商品分类表| MySQL

###客户
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
goods_customer_num |商品的客户全局购买数| MySQL

###运费模板
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
freight_template_goods |单品运费模板| MySQL
freight_template_goods_express |单品运费模板快递运送| MySQL
freight_template_goods_free |单品运费模板指定包邮条件| MySQL
freight_template_store |店铺运费模板| MySQL

###商品
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
goods| 商品spu表| MySQL
goods_info| 商品sku表| MySQL
goods_image| 商品图片表| MySQL
goods_customer_price| 商品客户价| MySQL
goods_interval_price| 商品区间价| MySQL
goods_level_price| 商品等级价| MySQL
GoodsCheckLog |商品审核日志 |MongoDB

###商品属性
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
goods_prop| 属性表| MySQL
goods_prop_detail| 属性明细表| MySQL
goods_prop_detail_rel| SPU与属性值关联表| MySQL

###商品规格
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
goods_spec| 商品规格表|MySQL
goods_spec_detail| 商品规格明细表|MySQL
goods_info_spec_detail_rel| 商品SKU与规格明细关联表|MySQL

###商品库
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
standard_goods| 商品库spu表 |MySQL
standard_sku |商品库sku表 |MySQL
standard_image |商品图片表 |MySQL
standard_goods_rel |商品库与店铺关联表 |MySQL
standard_prop_detail_rel |商品库SPU与属性值关联表 |MySQL

###商品库规格
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
standard_spec |商品库规格表 |MySQL
standard_spec_detail |商品库规格值表 |MySQL
standard_sku_spec_detail_rel |商品库SKU规格值关联表 |MySQL

###店铺分类
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
store_cate |店铺分类表 |MySQL
store_cate_goods_rela |店铺分类与商品关联表 |MySQL

###商品标签
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
goods_tab_rela |商品标签表 |MySQL
store_goods_tab |店铺商品标签表 |MySQL

###营销
 |表(集合)名  |  说明 | 数据源
 |:-----:|  :----: | :----:
GoodsMarketing |商品营销 |MongoDB

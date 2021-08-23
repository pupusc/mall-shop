
package com.wanmi.ares.utils;

/**
 * 常量类
 *
 * @author daiyitian
 * @version 0.0.1
 * @since 2017年4月11日 下午3:46:12
 */
public final class EsConstants {

    public final static Integer yes = 1;

    public final static Integer no = 0;

    /**
     * ES去重统计预设阈值
     */
    public final static Long PRECISION_THRESHOLD = 18000L;

    /**
     * ES=索引-基础数据
     */
    public final static String ES_INDEX_BASIC = "ares_basic";

    public final static String ES_INDEX_REGION = "region";

    /**
     * ES-类型-商品基础信息
     */
    public final static String ES_TYPE_SKU = "goods_info";

    /**
     * ES-类型-商品分类基础信息
     */
    public final static String ES_TYPE_SKU_CATE = "goods_cate";

    /**
     * ES-类型-商品品牌基础信息
     */
    public final static String ES_TYPE_SKU_BRAND = "goods_brand";

    /**
     * ES-类型-商家基础信息
     */
    public final static String ES_TYPE_COMPANY_INFO = "company_info";

    /**
     * ES-类型-区域基础信息
     */
    public final static String ES_TYPE_AREA = "region";

    /**
     * 区域字典数据
     */
    public final static String ES_TYPE_REGION_AREA = "region_area";
    public final static String ES_TYPE_REGION_CITY = "region_city";
    public final static String ES_TYPE_REGION_PROVINCE = "region_province";

    /**
     * ES-类型-客户基础信息
     */
    public final static String ES_TYPE_CUSTOMER = "customer";

    /**
     * ES-类型-客户等级基础信息
     */
    public final static String ES_TYPE_CUSTOMER_LEVEL = "customer_level";

    /**
     * ES-类型-业务员基础信息
     */
    public final static String ES_TYPE_EMPLOYEE = "employee";

    /**
     * ES-类型-流量报表
     */
    public final static String ES_TYPE_FLOW_REPORT = "flow_report";

    public final static String ES_TYPE_TRADE_REPORT = "trade_report";

    /**
     * ES=索引
     */
    public final static String ES_INDEX = "ares";


    /**
     * ES-类型-数据池信息
     */
    public final static String ES_TYPE_POOL = "ares_data";


    /**
     * ES-索引和分类-SKU报表-前缀
     */
    public final static String ES_SKU_PREFIX = "ares_sku_";

    /**
     * ES-索引和分类-SKU分类报表-前缀
     */
    public final static String ES_SKU_CATE_PREFIX = "ares_sku_cate_";

    /**
     * ES-索引和分类-SKU品牌报表-前缀
     */
    public final static String ES_SKU_BRAND_PREFIX = "ares_sku_brand_";

    /**
     * ES-索引和分类-流量报表-前缀
     */
    public final static String ES_FLOW_PREFIX = "ares_flow_";

    /**
     * ES-索引和分类-流量报表-前缀
     */
    public final static String ES_FLOW_GOODS_PREFIX = "ares_flow_goods_";


    /**
     * ES-索引和分类-交易报表-前缀
     */
    public final static String ES_TRADE_PREFIX = "ares_trade_";

    /**
     * 单日-单品转化率
     */
    public final static String SKU_CONVERSION_RATE = "return doc['uvOrderCount'].value/doc['orderNum'].value;";

    private EsConstants() {
    }

}

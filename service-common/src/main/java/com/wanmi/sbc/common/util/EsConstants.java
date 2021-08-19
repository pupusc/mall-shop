package com.wanmi.sbc.common.util;

/**
 * ES常量相关类
 * Created by daiyitian
 */
public interface EsConstants {

    /**
     * ES索引
     */
    String INDEX_NAME = "s2b";

    /**
     * ES spu文档类型
     */
    String DOC_GOODS_TYPE = "es_goods";

    /**
     * ES sku文档类型
     */
    String DOC_GOODS_INFO_TYPE = "es_goods_info";

    /**
     * ES品牌分类联合文档类型
     */
    String DOC_CATE_BRAND_TYPE = "goods_cate_brand";

    /**
     * 默认，中文分词
     */
    String DEF_ANALYZER = "ik_max_word";

    /**
     * 空格为分隔符
     */
    String PINYIN_ANALYZER = "pinyin";

    /**
     * ES 优惠券文档类型
     */
    String DOC_COUPON_INFO_TYPE = "es_coupon_info";

    /**
     * ES employee文档类型
     */
    String DOC_EMPLOYEE_TYPE = "es_employee";

    /**
     * 分销员
     */
    String DISTRIBUTION_CUSTOMER = "es_distribution_customer";

    /**
     *
     */
    String STORE_EVALUATE_SUM = "es_store_evaluate_sum";

    /**
     * 拼团活动
     */
    String GROUPON_ACTIVITY = "es_groupon_activity";

    /**
     * ES 公司店铺
     */
    String DOC_STORE_INFORMATION_TYPE = "es_store_information";

    /**
     * ES 优惠券活动
     */
    String DOC_COUPON_ACTIVITY = "es_coupon_activity";

    /**
     * 商品库
     */
    String DOC_STANDARD_GOODS = "es_standard_goods";


    /**
     * 品牌
     */
    String ES_GOODS_BRAND = "es_goods_brand";

    /**
     * 敏感词库
     */
    String SENSITIVE_WORDS = "es_sensitive_words";

    /**
     * ES 结算单 合并供应商结算和商家结算
     */
    String DOC_SETTLEMENT = "es_settlement";


    /**
     * 素材
     */
    String SYSTEM_RESOURCE = "es_system_resource";

    /**
     * ES 会员详情信息
     */
    String DOC_CUSTOMER_DETAIL = "es_customer_detail";

    /**
     * ES 会员资金
     */
    String DOC_CUSTOMER_FUNDS = "es_customer_funds";

    /**
     *
     * 敏感词
     */
    String SEARCH_ASSOCIATIONAL_WORD = "es_search_associational_word";

    /**
     * 操作日志
     */
    String SYSTEM_OPERATION_LOG = "es_system_operation_log";

}

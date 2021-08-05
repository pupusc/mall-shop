package com.wanmi.sbc.goods.api.constant;

/**
 * <p>商品异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午4:29.
 */
public final class GoodsErrorCode {
    private GoodsErrorCode() {
    }

    /**
     * 商品不存在
     */
    public final static String NOT_EXIST = "K-030001";

    /**
     * SPU编码不可重复
     */
    public final static String SPU_NO_EXIST = "K-030002";

    /**
     * SKU编码不可重复
     */
    public final static String SKU_NO_EXIST = "K-030003";
    /**
     * ERP SKU编码不可重复
     */
    public final static String ERP_SKU_NO_EXIST = "K-030013";

    /**
     * SKU编码[{0}]重复
     */
    public final static String SKU_NO_H_EXIST = "K-030004";
    /**
     * ERP_SKU编码[{0}]重复
     */
    public final static String ERP_SKU_NO_H_EXIST = "K-030014";
    /**
     * ERP_SPU编码[{0}]重复
     */
    public final static String ERP_SPU_NO_H_EXIST = "K-030015";

    /**
     * 商品不允许修改商品分类
     */
    public final static String EDIT_GOODS_CATE = "K-030005";

    /**
     * 当前商品不属于当前的商家
     */
    public final static String NOT_BELONG_SUPPLIER = "K-030006";

    /**
     * 商品已下架
     */
    public final static String NOT_ADDEDFLAG = "K-030007";

    /**
     * 商品设价类型错误
     */
    public final static String PRICE_TYPE_ERROR = "K-030008";

    /**
     * 商品关联商品库已被平台删除
     */
    public final static String STANDARDGOODS_DELETED = "K-030009";

    /**
     * 小B端商品加入数量报错信息
     */
    public final static String CHECK_COUNTS_BY_CUSTOMER_ID="K-130001";

    /**
     * 添加企业购商品校验失败
     */
    public final static String ENTERPRISE_INVALID_ERROR = "K-030702";

    /**
     * 没有商品购买权限
     */
    public final static String NO_GOODS_AUTHORITY="K-130011";

    /**
     * 低于最少起订量
     */
    public final static String NO_RISE_RESTRICTED_START_NUM="K-130012";

    /**
     * 高于限售的数量
     */
    public final static String MORE_THAN_RESTRICTED_NUM = "K-130013";

    /**
     * 周期购活动不存
     */
    public final static String NOT_EXIST_CYCLEBUY = "K-0300014";

    /**
     * 周期购活动不存
     */
    public final static String GIFT_INVENTORY = "K-0300017";

    /**
     * 周期购活动期数大于0校验
     */
    public final static String NUMBER_PERIODS = "K-0300018";


    /**
     * spu编码是必填
     */
    public final static String SPU_REQUIRED = "K-0300019";

    /**
     *  在非组合商品的情况下，sku编码是必填
     */
    public final static String SKU_REQUIRED = "K-0300020";
}


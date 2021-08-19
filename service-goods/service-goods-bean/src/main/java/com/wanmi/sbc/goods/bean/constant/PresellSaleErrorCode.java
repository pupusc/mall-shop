package com.wanmi.sbc.goods.bean.constant;

public  final class PresellSaleErrorCode {
    private PresellSaleErrorCode(){

    }

    /**
     * 活动已开始，无法删除
     */
    public final static String DELETED_ERROR = "K-080310";

    /**
     * 预售活动定金开始时间早于当前时间
     */
    public final static String PRESELLSALE_BEFORE_NOW = "K-080311";


    /**
     * 预售活动定金结束时间早于定金开始时间
     */
    public final static String PRESELLSALE_BEFORE_END = "K-080312";



    /**
     * 预售活动尾款开始时间早于定金结束时间
     */
    public final static String PRESELLSALE_FINAL_BEFORE = "K-080313";


    /**
     * 预售活动尾款结束时间早于尾款开始时间
     */
    public final static String PRESELLSALE_FINAL_END_BEFORE = "K-080314";

    /**
     * 预售活动尾款结束时间早于尾款开始时间
     */
    public final static String PRESELLSALE_END_BEFORE = "K-080315";

    /**
     * 预售活动不存在，或已删除
     */
    public final static String PRESELLSALE_DELETE = "K-080316";


    /**
     * 预售活动已开始，无法修改
     */
    public final static String PRESELLSALE_ALREADY_BEGUN = "K-080317";

    /**
     * 预售活动已开始，无法删除
     */
    public final static String PRESELLSALE_ALREADY_END_DEL = "K-080318";


    /**
     * 预售活动已开始，无法删除
     */
    public final static String PRESELLSALE_ALREADY_BEGUN_DEL = "K-080319";


    /**
     * 预售活动支付定金时间未开始
     */
    public final static String PRESELLSALE_HANDSEL_NOT_BEGIN = "K-080320";


    /**
     * 预售活动支付定金时间已结束
     */
    public final static String PRESELLSALE_HANDSEL_YET_END = "K-080321";


    /**
     * 预售活动关联商品售罄
     */
    public final static String PRESELLSALE_SELL_OUT = "K-080322";


    /**
     * 预售活动关联商品数量不足
     */
    public final static String PRESELLSALE_INADEQUATE = "K-080323";


    /**
     * 你没有达到购买的等级资格
     */
    public final static String PRESELLSALE_NOT_QUALIFICATION = "K-080324";
}

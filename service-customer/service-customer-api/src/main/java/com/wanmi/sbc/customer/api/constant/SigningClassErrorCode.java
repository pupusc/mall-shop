package com.wanmi.sbc.customer.api.constant;

/**
 * <p>签约分类异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午5:00.
 */
public final class SigningClassErrorCode {
    private SigningClassErrorCode() {
    }

    /**
     * 已签约
     */
    public final static String HAS_SIGNED_CONTRACT = "K-110301";

    /**
     * 最多签约数量
     */
    public final static String MOST_CONTRACT_NUMBER = "K-110302";

    /**
     * 最多资质数量
     */
    public final static String HIGHEST_NUMBER_QUALIFICATION = "K-110303";

    /**
     * 平台分类不存在
     */
    public final static String NOT_EXIST = "K-110304";

    /**
     * 所选签约分类不存在
     */
    public final static String CONTRACT_CATE_NOT_EXIST = "K-110305";

    /**
     * 该类目关联了商品，请先删除商品后再删除签约类目
     */
    public final static String RELATES_THE_ITEM = "K-110306";
}

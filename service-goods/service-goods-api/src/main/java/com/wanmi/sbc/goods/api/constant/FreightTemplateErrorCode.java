package com.wanmi.sbc.goods.api.constant;

/**
 * <p>运费模板异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午5:11.
 */
public final class FreightTemplateErrorCode {
    private FreightTemplateErrorCode() {
    }

    /**
     * 模板名称超出限制
     */
    public final static String NAME_OVER_LIMIT = "K-110701";

    /**
     * 模板名称已存在
     */
    public final static String NAME_EXIST = "K-110702";

    /**
     * 地区不可重复设置
     */
    public final static String AREA_REPETITION_SETTING = "K-110703";

    /**
     * 店铺模板名称已存在
     */
    public final static String STORE_NAME_EXIST = "K-110704";

    /**
     * 模板不存在，请选择其他模板！
     */
    public final static String NOT_EXIST = "K-110705";
}

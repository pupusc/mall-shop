package com.wanmi.sbc.order.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 短信模板
 * Created by aqlu on 15/12/4.
 */
@ApiEnum(dataType = "java.lang.String")
public enum OrderSmsTemplate {

    @ApiEnumProperty("电子卡券券码类型购买成功信息模板")
    VIRTUAL_COUPON_CODE("恭喜您成功购买电子卡券，你购买的卡券券码是：%s ，请勿泄露给他人。"),
    @ApiEnumProperty("电子卡券券码+密码类型购买成功信息模板")
    VIRTUAL_COUPON_CODE_PASSWORD("恭喜您成功购买电子卡券，你购买的卡券券码是：%1s ，密码是：%2s ，请勿泄露给他人。"),
    @ApiEnumProperty("电子卡券链接类型购买成功信息模板")
    VIRTUAL_COUPON_CODE_LINK("恭喜您成功购买电子卡券，你购买的卡券链接是：%1s ，请勿泄露给他人。"),
    ;


    private String content;

    OrderSmsTemplate(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}

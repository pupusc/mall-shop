package com.wanmi.sbc.customer.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 短信模板
 * Created by aqlu on 15/12/4.
 */
@ApiEnum(dataType = "java.lang.String")
public enum PaidCardSmsTemplate {

    @ApiEnumProperty("购买付费卡发送短信")
    PAID_CARD_BUY_CODE("恭喜，您已成为%s，有效期至%s年%s月%s日"),
    @ApiEnumProperty("付费卡快过期提醒")
    PAID_CARD_WILL_VALID_REMAIN_CODE("您好，您的%s将于%s年%s月%s日到期，为了不影响您的权益使用，请登陆商城及时续费。"),
    @ApiEnumProperty("付费会员过期提醒")
    PAID_CARD_VALID_REMAIN_CODE("您的%s已过期，如希望继续享受%s会员权益请前往商城重新开通。");


    private String content;

    PaidCardSmsTemplate(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}

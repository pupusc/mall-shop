package com.wanmi.sbc.customer.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 短信模板
 * Created by aqlu on 15/12/4.
 */
@ApiEnum(dataType = "java.lang.String")
public enum SmsTemplate {
    @ApiEnumProperty("注册验证码短信模板")
    REGISTRY("您本次注册的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("支付密码验证码短信模板")
    SET_PAY_PASSWORD("您本次设置支付密码的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("找回密码验证码短信模板")
    REFUND_PASSWORD("您本次找回密码的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("修改支付密码验证码短信模板")
    CHANGE_PAY_PASSWORD("您本次修改支付密码的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("修改密码验证码短信模板")
    CHANGE_PASSWORD("您本次修改密码的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("更改绑定手机验证码短信模板")
    CHANGE_PHONE("您本次更改绑定手机的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("用户密码短信模板")
    CUSTOMER_PASSWORD("您本次账号是：%1s，密码是: %2s"),
    @ApiEnumProperty("企业会员密码短信模板")
    ENTERPRISE_CUSTOMER_PASSWORD("恭喜您成为企业会员，您可享受企业会员专享价，您的账号是：%1s，密码是:  %2s，快去商城采购吧~"),
    @ApiEnumProperty("员工密码短信模板")
    EMPLOYEE_PASSWORD("您本次账号是：%1s，密码是: %2s"),
    @ApiEnumProperty("登录验证码短信模板")
    CUSTOMER_LOGIN("您本次的登录验证码是：%s，如非本人操作，请忽略本短信。"),
    @ApiEnumProperty("操作验证码短信模板")
    WX_CUSTOMER_LOGIN("您本次操作的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("设置支付密码验证码信息模板")
    BALANCE_PAY_PASSWORD("您本次设置支付密码的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("找回支付密码验证码信息模板")
    FIND_BALANCE_PAY_PASSWORD("您本次找回支付密码的验证码是：%s，有效期5分钟，请勿泄露给他人。"),
    @ApiEnumProperty("会员导入成功信息模板")
    CUSTOMER_IMPORT_SUCCESS("欢迎加入SBC商城，您的万米账号创建成功，您可使用短信验证码进行快捷登录，默认密码为手机号后6位，登陆后请及时更改！");

    private String content;

    SmsTemplate(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}

package com.wanmi.sbc.message.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SmsTemplateParamDTO
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/12/3 15:08
 **/
@Data
public class SmsTemplateParamDTO {

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "名称 用于账户称号、商品名称、订单第一行商品名称")
    private String name;

    @ApiModelProperty(value = "人数 张数 积分值 手机号")
    private String number;

    @ApiModelProperty(value = "原因")
    private String remark;

    @ApiModelProperty(value = "商品名称")
    private String product;

    @ApiModelProperty(value = "金额")
    private String money;

    @ApiModelProperty(value = "第二个金额")
    private String price;

    @ApiModelProperty(value = "付费卡名称")
    private String paidCardName;
    @ApiModelProperty(value = "年")
    private String year;
    @ApiModelProperty(value = "月")
    private String month;
    @ApiModelProperty(value = "日")
    private String day;
    private String cardName;

}

package com.wanmi.sbc.third.login.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.third.login.TerminalStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by gaomuwei on 2018/8/16.
 */
@ApiModel
@Data
public class WechatQuickLoginRequest extends BaseRequest {

    /**
     * 授权临时票据
     */
    @ApiModelProperty(value = "授权临时票据")
    @NotBlank
    private String code;

    /**
     * 类型终端
     */
    @ApiModelProperty(value = "类型终端")
    @NotNull
    private TerminalStringType channel;

    /**
     * 微信登录appId
     */
    @ApiModelProperty(value = "微信登录appId")
    private String appId;

    /**
     * 微信登录appSecret
     */
    @ApiModelProperty(value = "微信登录appSecret")
    private String appSecret;

    /**
     * 小程序登录，unionId可从前台传过来
     */
    @ApiModelProperty(value = "微信用户唯一标识")
    private String unionId;

    /**
     * 小程序端的手机号，由前台直接传过来
     */
    @ApiModelProperty(value = "小程序端获取手机号")
    private String phonNumber;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headimgurl;

    /**
     * 小程序openId
     */
    @ApiModelProperty(value = "小程序openId")
    private String openId;


    @Override
    public void checkParam() {
//        if(TerminalStringType.APP == channel) {
//            Validate.notBlank(appId, ValidateUtil.BLANK_EX_MESSAGE, "appId");
//            Validate.notBlank(appSecret, ValidateUtil.BLANK_EX_MESSAGE, "appSecret");
//        }
    }

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty("解密密钥")
    private String iv;

    @ApiModelProperty("微信加密数据")
    private String encryptedData;
}

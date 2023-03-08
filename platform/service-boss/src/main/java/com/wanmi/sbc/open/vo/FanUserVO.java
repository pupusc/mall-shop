package com.wanmi.sbc.open.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FanUserVO {
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号 ")
    private String userNo;

    /**
     * 用户状态（0：未入会 1：体验中 2：体验过期 3：正式会员 4：会员过期 5：停用 ）
     */
    @ApiModelProperty(value = "用户状态（0：未入会 1：体验中 2：体验过期 3：正式会员 4：会员过期 5：停用 ） ")
    private Integer userStatus;

    /**
     * 会期开始时间
     */
    @ApiModelProperty(value = "会期开始时间 ")
    private Long vipStartTime;
    /**
     * 会期结束时间
     */
    @ApiModelProperty(value = "会期结束时间 ")
    private Long vipEndTime;
    /**
     * 有效状态(true:有效；false:失效)
     */
    @ApiModelProperty(value = "有效状态(true:有效；false:失效) ")
    private Boolean validFlag;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String profilePhoto;
}

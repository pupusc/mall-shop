package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.enums.RecruitApplyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>分销设置VO</p>
 *
 * @author baijz
 * @date 2019-02-19 10:08:02
 */
@ApiModel
@Data
public class DistributionSettingSimVO {

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启社交分销")
    private DefaultFlag openFlag;

    /**
     * 分销员名称
     */
    @ApiModelProperty(value = "分销员名称")
    private String distributorName;

    /**
     * 是否开启分销小店 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启分销小店")
    private DefaultFlag shopOpenFlag;

    /**
     * 小店名称
     */
    @ApiModelProperty(value = "小店名称")
    private String shopName;

    /**
     * 店铺分享图片
     */
    @ApiModelProperty(value = "店铺分享图片")
    private String shopShareImg;

    /**
     * 申请条件 0：购买商品，1：邀请注册
     */
    @ApiModelProperty(value = "申请条件")
    private RecruitApplyType applyType;

    /**
     * 是否开启申请入口  0：关闭  1：开启
     */
    @ApiModelProperty(value = "申请入口是否开启")
    private DefaultFlag applyFlag;

    /**
     * 是否开启邀请奖励
     */
    @ApiModelProperty("是否开启邀新奖励")
    private DefaultFlag inviteFlag;

    /**
     * 邀新入口海报
     */
    @ApiModelProperty(value = "邀新入口海报")
    private String inviteEnterImg;

    /**
     * 购买商品时招募入口海报
     */
    @ApiModelProperty(value = "购买商品时招募入口海报")
    private String buyRecruitEnterImg;

    /**
     * 邀请注册时招募入口海报
     */
    @ApiModelProperty(value = "邀请注册时招募入口海报")
    private String inviteRecruitEnterImg;

    /**
     * 邀请注册时招募落地页海报
     */
    @ApiModelProperty(value = "邀请注册时招募落地页海报")
    private String inviteRecruitImg;

    /**
     * 分销业绩规则说明
     */
    @ApiModelProperty(value = "分销业绩规则说明")
    private String performanceDesc;

    /**
     * 分销员等级规则
     */
    @ApiModelProperty(value = "分销员等级规则")
    private String distributorLevelDesc;
}
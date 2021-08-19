package com.wanmi.sbc.crm.bean.vo;

import com.wanmi.sbc.crm.bean.dto.AutoTagDTO;
import com.wanmi.sbc.crm.bean.dto.RegionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-11
 * \* Time: 13:44
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomGroupDetailVo {
    @ApiModelProperty(value = "自定义分组名")
    private String groupName;

    @ApiModelProperty(value = "所在地区")
    private List<RegionDTO> regionList;

    @ApiModelProperty(value = "最近一次消费统计时间")
    private Integer lastTradeTime;

    @ApiModelProperty(value = "累计消费次数统计时间")
    private Integer tradeCountTime;
    @ApiModelProperty(value = "累计消费次数大于值")
    private Long gtTradeCount;
    @ApiModelProperty(value = "累计消费次数小于值")
    private Long ltTradeCount;

    @ApiModelProperty(value = "累计消费金额统计时间")
    private Integer tradeAmountTime;
    @ApiModelProperty(value = "累计消费金额大于值")
    private Long gtTradeAmount;
    @ApiModelProperty(value = "累计消费金额小于值")
    private Long ltTradeAmount;

    @ApiModelProperty(value = "笔单价统计时间")
    private Integer avgTradeAmountTime;
    @ApiModelProperty(value = "笔单价大于值")
    private Long gtAvgTradeAmount;
    @ApiModelProperty(value = "笔单价小于值")
    private Long ltAvgTradeAmount;

    @ApiModelProperty(value = "会员成长值大于值")
    private Long gtCustomerGrowth;
    @ApiModelProperty(value = "会员成长值小于值")
    private Long ltCustomerGrowth;


    @ApiModelProperty(value = "会员积分大于值")
    private Long gtPoint;
    @ApiModelProperty(value = "会员积分小于值")
    private Long ltPoint;


    @ApiModelProperty(value = "会员余额大于值")
    private Long gtBalance;
    @ApiModelProperty(value = "会员余额小于值")
    private Long ltBalance;

    @ApiModelProperty(value = "会员等级")
    private List<Long> customerLevel;

    @ApiModelProperty(value = "最近有下单时间")
    private Integer recentTradeTime;

    @ApiModelProperty(value = "最近无下单时间")
    private Integer noRecentTradeTime;

    @ApiModelProperty(value = "最近有付款时间")
    private Integer recentPayTradeTime;

    @ApiModelProperty(value = "最近无付款时间")
    private Integer noRecentPayTradeTime;

    @ApiModelProperty(value = "最近有加购时间")
    private Integer recentCartTime;

    @ApiModelProperty(value = "最近无加购时间")
    private Integer noRecentCartTime;

    @ApiModelProperty(value = "最近有浏览时间")
    private Integer recentFlowTime;

    @ApiModelProperty(value = "最近无浏览时间")
    private Integer noRecentFlowTime;

    @ApiModelProperty(value = "最近有收藏时间")
    private Integer  recentFavoriteTime;

    @ApiModelProperty(value = "最近无收藏时间")
    private Integer noRecentFavoriteTime;


    @ApiModelProperty(value = "会员标签")
    private List<Long> customerTag;

    @ApiModelProperty(value = "性别：0：女，1：男")
    private Integer gender;

    @ApiModelProperty(value = "年龄大于值")
    private Integer gtAge;

    @ApiModelProperty(value = "年龄小于值")
    private Integer ltAge;

    @ApiModelProperty(value = "入会时间大于值")
    private Integer gtAdmissionTime;

    @ApiModelProperty(value = "入会时间小于值")
    private Integer ltAdmissionTime;

    @ApiModelProperty(value = "自动标签")
    private List<AutoTagDTO> autoTags;
}

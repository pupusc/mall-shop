package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>用户分销排行榜VO</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@ApiModel
@Data
public class DistributionCustomerRankingVO implements Serializable {


    private static final long serialVersionUID = -3420283297601885834L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 邀新人数
     */
    @ApiModelProperty(value = "邀新人数")
    private Integer inviteCount;

    /**
     * 有效邀新人数
     */
    @ApiModelProperty(value = "有效邀新人数")
    private Integer inviteAvailableCount;

    /**
     * 销售额(元)
     */
    @ApiModelProperty(value = "销售额(元) ")
    private BigDecimal saleAmount;

    /**
     * 预估收益
     */
    @ApiModelProperty(value = "预估收益")
    private BigDecimal commission;

    /**
     * 排行
     */
    @ApiModelProperty(value = "排行")
    private String ranking;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String img;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String name;


}
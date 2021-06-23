package com.wanmi.sbc.marketing.api.request.grouponrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>拼团活动参团信息表新增参数</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponRecordIncrBuyNumRequest implements Serializable{

    /**
     * 拼团活动ID
     */
    @ApiModelProperty(value = "拼团活动ID")
    @NotBlank
    private String grouponActivityId;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    @NotBlank
    private String customerId;

    /**
     * sku编号
     */
    @ApiModelProperty(value = "SKU编号")
    @NotBlank
    private String goodsInfoId;

    /**
     * 购买数
     */
    @ApiModelProperty(value = "购买数")
    @NotNull
    private Integer buyNum;

    /**
     * SPU编号
     */
    @ApiModelProperty(value = "SPU编号")
    @NotBlank
    private String goodsId;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer limitSellingNum;

}
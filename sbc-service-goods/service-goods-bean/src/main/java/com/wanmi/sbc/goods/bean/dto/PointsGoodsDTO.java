package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <P>积分商品DTO</P>
 *
 * @author yang
 * @since 2019/5/22
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsDTO implements Serializable {

    private static final long serialVersionUID = 6219094020590560821L;

    /**
     * SpuId
     */
    @ApiModelProperty(value = "SpuId")
    @NotBlank
    private String goodsId;

    /**
     * SkuId
     */
    @ApiModelProperty(value = "SkuId")
    @NotBlank
    private String goodsInfoId;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Integer cateId;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Long stock;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Long sales;

    /**
     * 结算价格
     */
    @ApiModelProperty(value = "结算价格")
    private BigDecimal settlementPrice;

    /**
     * 兑换积分
     */
    @ApiModelProperty(value = "兑换积分")
    private Long points;

    /**
     * 是否启用 0：停用，1：启用
     */
    @ApiModelProperty(value = "是否启用 0：停用，1：启用")
    private EnableStatus status;

    /**
     * 推荐标价, 0: 未推荐 1: 已推荐
     */
    @ApiModelProperty(value = "推荐标价, 0: 未推荐 1: 已推荐")
    private BoolFlag recommendFlag;

    /**
     * 兑换开始时间
     */
    @ApiModelProperty(value = "兑换开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 兑换结束时间
     */
    @ApiModelProperty(value = "兑换结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 删除标识,0: 未删除 1: 已删除
     */
    @ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
    private DeleteFlag delFlag;
}

package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:03 2019/5/16
 * @Description: 拼团活动商品信息
 */
@Data
public class GrouponGoodsInfoForEditDTO implements Serializable {

    private static final long serialVersionUID = -6455036576503096044L;

    /**
     * 拼团商品id
     */
    @ApiModelProperty(value = "拼团商品id")
    @NotBlank
    private String grouponGoodsId;

    /**
     * 拼团价格
     */
    @ApiModelProperty(value = "拼团价格")
    @NotNull
    private BigDecimal grouponPrice;

    /**
     * 起售数量
     */
    @ApiModelProperty(value = "起售数量")
    private Integer startSellingNum;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer limitSellingNum;

    /**
     * 拼团分类id
     */
    private String grouponCateId;

    /**
     * 活动开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 活动审核状态，0：待审核，1：审核通过，2：审核不通过
     */
    private AuditStatus auditStatus;

}


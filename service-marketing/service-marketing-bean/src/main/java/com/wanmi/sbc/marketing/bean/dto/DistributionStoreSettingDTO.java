package com.wanmi.sbc.marketing.bean.dto;

import java.math.BigDecimal;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;

/**
 * <p>店铺分销设置DTO</p>
 *
 * @author gaomuwei
 * @date 2019-02-19 15:46:27
 */
@ApiModel
@Data
public class DistributionStoreSettingDTO {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private String settingId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String storeId;

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启社交分销")
    @NotNull
    private DefaultFlag openFlag;

    /**
     * 是否开启通用分销佣金 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启通用分销佣金")
    @NotNull
    private DefaultFlag commissionFlag;

    /**
     * 通用分销佣金比例
     */
    @ApiModelProperty(value = "通用分销佣金比例")
    private BigDecimal commissionRate;

}
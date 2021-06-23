package com.wanmi.sbc.setting.api.request.storeresourcecate;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * <p>店铺资源资源分类表新增参数</p>
 *
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResourceCateInitRequest extends SettingBaseRequest {

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    @NotNull
    private Long storeId;

    /**
     * 商家标识
     */
    @ApiModelProperty(value = "商家标识")
    @NotNull
    private Long companyInfoId;

    /**
     * 父分类ID
     */
    @ApiModelProperty(value = "父分类ID")
    @Max(9223372036854775807L)
    private Long cateParentId;
}
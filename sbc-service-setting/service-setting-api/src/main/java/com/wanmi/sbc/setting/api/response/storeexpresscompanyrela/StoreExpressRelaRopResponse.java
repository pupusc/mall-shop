package com.wanmi.sbc.setting.api.response.storeexpresscompanyrela;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by feitingting on 2019/11/14.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreExpressRelaRopResponse {
    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键")
    private Long id;

    /**
     * 快递公司标识
     */
    @ApiModelProperty(value = "自增主键")
    private Long expressCompanyId;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "自增主键")
    private Long storeId;

    /**
     * 商家标识
     */
    @ApiModelProperty(value = "自增主键")
    private Long companyInfoId;
}

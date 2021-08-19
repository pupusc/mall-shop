package com.wanmi.sbc.customer.api.request.store;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 店铺基本信息参数
 * Created by CHENLI on 2017/11/2.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreContractModifyRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 2022217738677041274L;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    @NotNull
    private Long storeId;

    /**
     * 签约开始日期
     */
    @ApiModelProperty(value = "签约开始日期")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractStartDate;

    /**
     * 签约结束日期
     */
    @ApiModelProperty(value = "签约结束日期")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型(0、平台自营 1、第三方商家)")
    @NotNull
    private BoolFlag companyType;
}

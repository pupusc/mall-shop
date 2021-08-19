package com.wanmi.sbc.customer.api.request.store;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel
@Data
public class StoreAuditStateModifyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 6625946344043048822L;
    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    @NotNull
    private Long storeId;

    /**
     * 驳回状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "驳回状态")
    @NotNull
    private CheckState auditState;

    /**
     * 申请入驻时间
     */
    @ApiModelProperty(value = "申请入驻时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime applyEnterTime;

    /**
     * 审核未通过原因
     */
    @ApiModelProperty(value = "审核未通过原因")
    private String auditReason;
}
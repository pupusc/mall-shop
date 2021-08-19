package com.wanmi.sbc.order.api.request.returnorder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 根据结束时间查询退单列表请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderListByEndDateRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    /**
     * 偏移始点
     */
    @ApiModelProperty(value = "偏移始点")
    @NotNull
    private Integer start;

    /**
     * 偏移终点
     */
    @ApiModelProperty(value = "偏移终点")
    @NotNull
    private Integer end;

    /**
     * 退货流程状态
     */
    @ApiModelProperty(value = "退单流程状态")
    private ReturnFlowState returnFlowState;
}

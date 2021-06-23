package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 退货日志记录
 * Created by jinwei on 21/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ReturnEventLogVO implements Serializable {

    private static final long serialVersionUID = 8850207356082370873L;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

    /**
     * eventType
     */
    @ApiModelProperty(value = "eventType")
    private String eventType;

    /**
     * eventDetail
     */
    @ApiModelProperty(value = "eventDetail")
    private String eventDetail;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    /**
     * eventTime
     */
    @ApiModelProperty(value = "eventTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime eventTime = LocalDateTime.now();
}

package com.wanmi.sbc.account.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 开票项返回
 */
@ApiModel
@Data
public class InvoiceProjectListVO implements Serializable{

    /**
     * 主键
     */
    @ApiModelProperty(value = "开票项id")
    private String projectId;

    /**
     * 开票项目名称
     */
    @ApiModelProperty(value = "开票项目名称")
    private String projectName;

    /**
     * 开票项目修改时间
     */
    @ApiModelProperty(value = "开票项目修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime projectUpdateTime;
}

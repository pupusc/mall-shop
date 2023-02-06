package com.wanmi.sbc.goods.bean.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuspensionDTO {

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 跳转id
     */
    @ApiModelProperty(value = "跳转id")
    private String toId;

    /**
     * 跳转url
     */
    @ApiModelProperty(value = "跳转url")
    private String toHref;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Boolean publishState;

    /**
     * 投放开始时间
     */
    @ApiModelProperty(value = "投放开始时间")
    private LocalDateTime startTime;

    /**
     * 投放结束时间
     */
    @ApiModelProperty(value = "投放结束时间")
    private LocalDateTime endTime;



    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private Long type;
}

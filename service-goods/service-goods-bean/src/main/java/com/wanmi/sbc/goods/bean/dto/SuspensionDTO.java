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
     * 跳转url
     */
    @ApiModelProperty(value = "跳转url")
    private String toHref;
    /**
     * 背景颜色
     */
    private String backColor;

    /**
     * 字体颜色
     */
    private String wordColor;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     *结束时间
     */
    private LocalDateTime endTime;

}

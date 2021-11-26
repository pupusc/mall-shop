package com.wanmi.sbc.crm.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutotagOtherVO implements Serializable {
    // 标签id
    @ApiModelProperty(value = "id")
    private String id;

    // 标签类型
    @ApiModelProperty(value = "标签类型")
    private String type;

    // 数量统计
    @ApiModelProperty(value = "数量统计")
    private int num;

    // 时间
    @ApiModelProperty(value = "时间")
    private Date pDate;

    // 标签名称
    @ApiModelProperty(value = "标签名称")
    private String detailName;
}

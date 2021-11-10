package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;

@Data
public class HeadImageConfigDTO implements Serializable {
    private static final long serialVersionUID = 5582485587109992496L;

    @ApiModelProperty("主题id")
    private Integer topicId;

    @ApiModelProperty("头图图片地址")
    private String imageUrl;

    @ApiModelProperty("头图链接地址")
    private String linkUrl;
    
}

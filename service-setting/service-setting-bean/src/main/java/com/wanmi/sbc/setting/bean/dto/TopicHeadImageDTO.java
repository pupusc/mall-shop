package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel
public class TopicHeadImageDTO implements Serializable {
    private static final long serialVersionUID = 5582485587109992496L;

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("主题id")
    private Integer topicId;

    @NotBlank
    @ApiModelProperty("头图图片地址")
    private String imageUrl;

    @ApiModelProperty("头图链接地址")
    private String linkUrl;

    @ApiModelProperty("排序序号")
    private Integer sorting;
    
}

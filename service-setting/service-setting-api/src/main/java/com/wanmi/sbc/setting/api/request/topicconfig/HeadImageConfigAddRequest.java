package com.wanmi.sbc.setting.api.request.topicconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class HeadImageConfigAddRequest implements Serializable {

    private static final long serialVersionUID = -797995665161219409L;

    @ApiModelProperty("主键，编辑的时候传，新增接口过滤")
    private Integer id;

    @ApiModelProperty("主题id")
    private Integer topicId;

    @NotBlank
    @ApiModelProperty("头图图片地址")
    private String imageUrl;

    @ApiModelProperty("头图链接地址")
    private String linkUrl;

    @ApiModelProperty("排序顺序")
    private Integer sorting;
}

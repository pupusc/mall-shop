package com.wanmi.sbc.crm.api.request.customertag;

import com.wanmi.sbc.dw.bean.enums.TagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAutoTagRequest {


    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id", required = true)
    @NotBlank
    private String customerId;


    /**
     * 标签类型
     */
    @ApiModelProperty
    private TagType tagType;
}

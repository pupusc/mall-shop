package com.wanmi.sbc.sensitivewords.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 拒绝收货
 * Created by jinwei on 22/4/2017.
 */
@ApiModel
@Data
public class SensitiveWordsValidateRequest implements Serializable{

    /**
     * 文本内容
     */
    @NotBlank
    @ApiModelProperty(value = "文本内容", required = true)
    private String text;
}

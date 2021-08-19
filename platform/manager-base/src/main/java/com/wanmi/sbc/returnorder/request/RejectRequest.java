package com.wanmi.sbc.returnorder.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 拒绝收货
 * Created by jinwei on 22/4/2017.
 */
@ApiModel
@Data
public class RejectRequest implements Serializable{

    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    private String reason;
}

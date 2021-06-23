package com.wanmi.sbc.returnorder.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * Created by jinwei on 6/5/2017.
 */
@ApiModel
@Data
public class ReturnRequest {

    @ApiModelProperty(value = "退单Id")
    List<String> rids;
}

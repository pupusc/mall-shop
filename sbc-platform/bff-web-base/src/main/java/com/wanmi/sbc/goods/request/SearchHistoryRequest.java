package com.wanmi.sbc.goods.request;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 搜索历史参数
 * Created by dyt on 2017/8/8.
 */
@ApiModel
@Data
public class SearchHistoryRequest extends BaseRequest {

    /**
     * 关键字
     */
    @ApiModelProperty(value = "搜索历史关键字")
    @NotBlank
    private String keyword;
}

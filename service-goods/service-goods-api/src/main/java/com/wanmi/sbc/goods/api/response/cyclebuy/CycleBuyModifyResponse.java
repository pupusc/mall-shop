package com.wanmi.sbc.goods.api.response.cyclebuy;

import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 周期购编辑响应
 * Created by weiwenhao on 2021/01/21.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyModifyResponse {


    /**
     * 周期购编辑响应
     */
    @ApiModelProperty(value = "周期购编辑响应")
    private CycleBuyVO cycleBuyVO;


}

package com.wanmi.sbc.goods.api.response.cyclebuy;


import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 查询周期购响应结果
 * Created by weiwenhao on 2021/01/21.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyByIdResponse {

    /**
     * 周期购查询响应
     */
    @ApiModelProperty(value = "周期购查询响应")
    private CycleBuyVO cycleBuyVO;

}

package com.wanmi.sbc.goods.api.response.cyclebuy;


import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 周期购查询响应结果
 * Created by weiwenhao on 2021/01/21.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyListResponse {

    /**
     * 周期购分页查询响应结果
     */
    @ApiModelProperty(value = "周期购分页查询响应结果")
    private List<CycleBuyVO> cycleBuyVOS;

}

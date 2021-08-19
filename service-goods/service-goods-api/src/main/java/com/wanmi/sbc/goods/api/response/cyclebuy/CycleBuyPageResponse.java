package com.wanmi.sbc.goods.api.response.cyclebuy;


import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 周期购分页查询响应结果
 * Created by weiwenhao on 2021/01/21.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyPageResponse {


    /**
     * 周期购分页查询响应结果
     */
    @ApiModelProperty(value = "周期购分页查询响应结果")
    private MicroServicePage<CycleBuyVO>  cycleBuyVOS;

}

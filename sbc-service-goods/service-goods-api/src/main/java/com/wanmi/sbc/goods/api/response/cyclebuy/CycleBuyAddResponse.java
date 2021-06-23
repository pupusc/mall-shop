package com.wanmi.sbc.goods.api.response.cyclebuy;

import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;


/**
 * 周期购新增响应
 * Created by weiwenhao on 2021/01/21.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyAddResponse implements Serializable {


    /**
     * 周期购新增响应
     */
    @ApiModelProperty(value = "周期购新增响应")
    private CycleBuyVO cycleBuyVO;

}

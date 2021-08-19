package com.wanmi.sbc.goods.api.response.storetobeevaluate;

import com.wanmi.sbc.goods.bean.vo.StoreTobeEvaluateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）店铺服务待评价信息response</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreTobeEvaluateByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺服务待评价信息
     */
    @ApiModelProperty(value = "店铺服务待评价信息")
    private StoreTobeEvaluateVO storeTobeEvaluateVO;
}

package com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise;

import com.wanmi.sbc.goods.bean.vo.CustomerGoodsEvaluatePraiseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）会员商品评价点赞关联表信息response</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGoodsEvaluatePraiseByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员商品评价点赞关联表信息
     */
    @ApiModelProperty(value = "会员商品评价点赞关联表信息")
    private CustomerGoodsEvaluatePraiseVO customerGoodsEvaluatePraiseVO;
}

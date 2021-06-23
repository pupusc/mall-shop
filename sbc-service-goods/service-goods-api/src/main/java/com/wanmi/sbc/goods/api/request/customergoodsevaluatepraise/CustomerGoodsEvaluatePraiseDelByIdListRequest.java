package com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除会员商品评价点赞关联表请求参数</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGoodsEvaluatePraiseDelByIdListRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量删除-主键List
     */
    @ApiModelProperty(value = "批量删除-主键List")
    @NotEmpty
    private List<String> idList;
}
package com.wanmi.sbc.goods.api.request.pointsgoodscate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>单个查询积分商品分类表请求参数</p>
 *
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsGoodsCateByIdRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分商品分类主键
     */
    @ApiModelProperty(value = "积分商品分类主键")
    @NotNull
    private Integer cateId;
}
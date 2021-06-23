package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>根据平台分类主键列表修改签约分类扣率请求类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateModifyCateRateByCateIdsRequest implements Serializable {

    private static final long serialVersionUID = 2676923983373371906L;

    /**
     * 类目扣率
     */
    @ApiModelProperty(value = "类目扣率")
    @NotNull
    private BigDecimal cateRate;

    /**
     * 平台类目主键列表
     */
    @ApiModelProperty(value = "平台类目主键列表")
    @NotNull
    @Size(min = 1)
    private List<Long> cateIds;
}

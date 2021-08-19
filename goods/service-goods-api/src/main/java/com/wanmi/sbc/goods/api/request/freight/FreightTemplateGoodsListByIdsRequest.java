package com.wanmi.sbc.goods.api.request.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 根据批量单品运费模板ids查询单品运费模板请求
 * Created by daiyitian on 2018/10/31.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateGoodsListByIdsRequest implements Serializable {


    private static final long serialVersionUID = -3381987177603611250L;

    /**
     * 批量单品运费模板ids
     */
    @ApiModelProperty(value = "批量单品运费模板ids")
    @NotEmpty
    private List<Long> freightTempIds;

}

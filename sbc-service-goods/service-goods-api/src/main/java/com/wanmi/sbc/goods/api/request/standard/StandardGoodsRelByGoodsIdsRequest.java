package com.wanmi.sbc.goods.api.request.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardGoodsRelByGoodsIdsRequest implements Serializable {

    private static final long serialVersionUID = 7843151810350391795L;

    /**
     * 商品库id
     */
    @ApiModelProperty(value = "商品库id")
    @NotEmpty
    private List<String> standardIds;
}

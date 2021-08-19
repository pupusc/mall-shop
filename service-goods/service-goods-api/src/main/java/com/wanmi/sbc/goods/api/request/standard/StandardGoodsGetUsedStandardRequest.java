package com.wanmi.sbc.goods.api.request.standard;

import com.wanmi.sbc.common.enums.BoolFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
public class StandardGoodsGetUsedStandardRequest implements Serializable {

    private static final long serialVersionUID = 7843151810350391795L;

    /**
     * 商品库id
     */
    @ApiModelProperty(value = "商品库id")
    @NotNull
    @Size(min = 1)
    private List<String> standardIds;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    @Size(min = 1)
    private List<Long> storeIds;

    /**
     * 是否需要同步
     */
    @ApiModelProperty(value = "是否需要同步")
    private BoolFlag needSynchronize;
}

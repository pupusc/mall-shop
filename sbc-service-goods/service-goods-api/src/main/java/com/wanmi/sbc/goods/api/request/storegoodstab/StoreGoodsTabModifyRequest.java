package com.wanmi.sbc.goods.api.request.storegoodstab;

import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Author: xiemengnan
 * Time: 2018/10/13.10:22
 */
@ApiModel
@Data
public class StoreGoodsTabModifyRequest implements Serializable {

    private static final long serialVersionUID = 6678308253559963050L;

    /**
     * 模板标识
     */
    @ApiModelProperty(value = "模板标识")
    private Long tabId;

    /**
     * 批量模板标识
     */
    @ApiModelProperty(value = "批量模板标识")
    private List<Long> tabIds;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String tabName;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

}

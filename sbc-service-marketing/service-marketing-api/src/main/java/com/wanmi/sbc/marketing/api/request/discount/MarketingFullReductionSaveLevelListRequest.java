package com.wanmi.sbc.marketing.api.request.discount;

import com.wanmi.sbc.marketing.bean.dto.MarketingFullReductionLevelDTO;
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
 * Date: 2018-11-21
 */
@ApiModel
@Data
public class MarketingFullReductionSaveLevelListRequest implements Serializable {

    private static final long serialVersionUID = -1808443607867386749L;

    @ApiModelProperty(value = "营销满减多级优惠列表")
    @NotNull
    @Size(min = 1)
    private List<MarketingFullReductionLevelDTO> fullReductionLevelList;
}

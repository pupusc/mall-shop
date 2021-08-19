package com.wanmi.sbc.marketing.api.request.discount;

import com.wanmi.sbc.marketing.bean.dto.MarketingFullDiscountLevelDTO;
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
 * Date: 2018-11-20
 */
@ApiModel
@Data
public class MarketingFullDiscountSaveLevelListRequest implements Serializable {

    private static final long serialVersionUID = -8657585472463935812L;

    @ApiModelProperty(value = "营销满折多级优惠列表")
    @NotNull
    @Size(min = 1)
    private List<MarketingFullDiscountLevelDTO> discountLevelList;

}

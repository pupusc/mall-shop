package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.order.bean.vo.PurchaseMarketingCalcVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseGetStoreMarketingResponse implements Serializable {

    private static final long serialVersionUID = -4231776601876231542L;

    @ApiModelProperty(value = "店铺营销信息map,key为店铺id，value为营销信息列表")
    private HashMap<Long, List<PurchaseMarketingCalcVO>> map;
}

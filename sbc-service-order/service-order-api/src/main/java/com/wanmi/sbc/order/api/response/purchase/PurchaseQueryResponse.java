package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.order.bean.vo.PurchaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseQueryResponse implements Serializable {

    private static final long serialVersionUID = 2172301079240862040L;

    @ApiModelProperty(value = "采购单列表")
    private List<PurchaseVO> purchaseList = new ArrayList<>();
}

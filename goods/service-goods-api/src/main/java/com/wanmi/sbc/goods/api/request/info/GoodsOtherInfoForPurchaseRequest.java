package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 根据单品ids查询商品主要信息request
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsOtherInfoForPurchaseRequest implements Serializable {

    private static final long serialVersionUID = 9162661911693046275L;

    /**
     * 单品ids
     */
    @NotEmpty
    private List<String> goodsInfoIds;

    /**
     * 当前登录用户
     */
    private CustomerVO customer;

}

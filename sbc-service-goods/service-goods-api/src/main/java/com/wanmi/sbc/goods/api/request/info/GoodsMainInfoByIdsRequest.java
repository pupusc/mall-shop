package com.wanmi.sbc.goods.api.request.info;

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
public class GoodsMainInfoByIdsRequest implements Serializable {

    private static final long serialVersionUID = 9162661911693046275L;

    /**
     * 单品ids
     */
    @NotEmpty
    private List<String> goodsInfoIds;

}

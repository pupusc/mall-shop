package com.wanmi.sbc.marketing.api.response.markup;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupSkuIdsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已参加换购的商品sku
     */
    @ApiModelProperty(value = "活动规则列表")
    private List<String> levelList;


}

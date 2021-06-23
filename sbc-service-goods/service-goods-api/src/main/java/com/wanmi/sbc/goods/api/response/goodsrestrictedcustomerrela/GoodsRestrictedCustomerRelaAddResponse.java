package com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela;

import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedCustomerRelaVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售配置会员关系表新增结果</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedCustomerRelaAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的限售配置会员关系表信息
     */
    @ApiModelProperty(value = "已新增的限售配置会员关系表信息")
    private GoodsRestrictedCustomerRelaVO goodsRestrictedCustomerRelaVO;
}

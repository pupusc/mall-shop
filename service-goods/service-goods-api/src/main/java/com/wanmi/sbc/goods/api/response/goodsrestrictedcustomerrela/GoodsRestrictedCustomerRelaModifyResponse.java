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
 * <p>限售配置会员关系表修改结果</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedCustomerRelaModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的限售配置会员关系表信息
     */
    @ApiModelProperty(value = "已修改的限售配置会员关系表信息")
    private GoodsRestrictedCustomerRelaVO goodsRestrictedCustomerRelaVO;
}

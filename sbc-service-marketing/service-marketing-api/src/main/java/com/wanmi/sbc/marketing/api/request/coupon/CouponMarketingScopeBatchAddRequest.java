package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.dto.CouponMarketingScopeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 批量新增优惠券商品作用范围请求结构
 * @Author: daiyitian
 * @Date: Created In 下午7:47 2018/11/24
 * @Description:
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponMarketingScopeBatchAddRequest implements Serializable {

    private static final long serialVersionUID = 243845102259185848L;

    /**
     * 优惠券商品作用范围内容 {@link CouponMarketingScopeDTO}
     */
    @ApiModelProperty(value = "优惠券商品作用范围列表")
    @NotEmpty
    private List<CouponMarketingScopeDTO> scopeDTOList;

}

package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据客户id查询使用优惠券列表
 * @Author: daiyitian
 * @Date: Created In 下午5:58 2018/11/23
 * @Description: 使用优惠券列表请求对象
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCodeListForUseByCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = 2572855130915278064L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;

    /**
     * 确认订单中的商品列表
     */
    @ApiModelProperty(value = "确认订单中的商品列表")
    @NotEmpty
    private List<TradeItemInfoDTO> tradeItems = new ArrayList<>();

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    @ApiModelProperty(value = "订单金额/")
    private BigDecimal price;

    private Boolean hasSaveRedis;
}

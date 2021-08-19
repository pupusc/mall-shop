package com.wanmi.sbc.customer.api.response.storelevel;

import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yang
 * @since 2019/3/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelByCustomerIdAndStoreIdResponse implements Serializable {

    private static final long serialVersionUID = 505318747984142350L;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 会员该商铺购买订单数
     */
    private Integer totalOrder;

    /**
     * 会员该商铺消费总金额
     */
    private BigDecimal totalAmount;

    /**
     * 店铺等级信息
     */
    private StoreLevelVO storeLevelVO;
}

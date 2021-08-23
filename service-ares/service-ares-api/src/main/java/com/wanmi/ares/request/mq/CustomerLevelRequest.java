package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import lombok.*;

import java.math.BigDecimal;

/**
 * 客户等级
 * Created by sunkun on 2017/9/21.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLevelRequest extends BaseMqRequest {

    private static final long serialVersionUID = -8771266607939952820L;

    /**
     * 名称
     */
    private String name;

    /**
     * 折扣率
     */
    private BigDecimal discount;

    /**
     * 是否默认
     */
    @Builder.Default
    private boolean isDefault = false;

    /**
     * 商家id
     */
    private String companyId;
}

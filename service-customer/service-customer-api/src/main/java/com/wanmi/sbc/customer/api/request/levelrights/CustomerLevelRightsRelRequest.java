package com.wanmi.sbc.customer.api.request.levelrights;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;

/**
 * <p>会员等级与权益关联表查询参数</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelRightsRelRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -762439369557318197L;

    /**
     * 用户等级id
     */
    private Long customerLevelId;

    /**
     * 权益id
     */
    private Integer rightsId;
}
